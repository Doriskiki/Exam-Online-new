/* Service Worker for offline-first exam experience */
importScripts("./sw-queue.js");

const STATIC_CACHE = "exam-static-v2";
const RUNTIME_CACHE = "exam-runtime-v1";
const STATIC_ASSETS = ["/", "/index.html", "/config.js"];

self.addEventListener("install", (event) => {
  event.waitUntil(
    caches
      .open(STATIC_CACHE)
      .then((cache) => cache.addAll(STATIC_ASSETS))
      .then(() => self.skipWaiting())
  );
});

self.addEventListener("activate", (event) => {
  event.waitUntil(
    caches
      .keys()
      .then((keys) =>
        Promise.all(
          keys
            .filter(
              (k) =>
                k !== STATIC_CACHE &&
                k !== RUNTIME_CACHE &&
                !k.startsWith("exam-")
            )
            .map((k) => caches.delete(k))
        )
      )
      .then(() => self.clients.claim())
  );
});

self.addEventListener("fetch", (event) => {
  const { request } = event;
  const url = new URL(request.url);

  // 1. Cache-first for static assets (App Shell)
  const isStatic =
    request.method === "GET" &&
    (STATIC_ASSETS.includes(url.pathname) ||
      (url.origin === self.location.origin &&
        url.pathname.startsWith("/static")));
  if (isStatic) {
    event.respondWith(
      caches.match(request).then((cached) => cached || fetch(request))
    );
    return;
  }

  // 2. Runtime Caching for Fonts & Images (including Backend Images)
  const isBackendImage =
    url.origin.includes("localhost:8889") &&
    url.pathname.startsWith("/images/");
  const isGenericAsset =
    /\.(png|jpg|jpeg|gif|svg|ico|avif|webp|woff|woff2|ttf|eot|otf)$/i.test(
      url.pathname
    );

  if (request.method === "GET" && (isBackendImage || isGenericAsset)) {
    event.respondWith(
      caches.match(request).then((cached) => {
        if (cached) return cached;
        return fetch(request)
          .then((response) => {
            // Cache only valid responses (200 OK)
            if (
              !response ||
              response.status !== 200 ||
              (response.type !== "basic" && response.type !== "cors")
            ) {
              return response;
            }
            const responseToCache = response.clone();
            caches.open(RUNTIME_CACHE).then((cache) => {
              cache.put(request, responseToCache);
            });
            return response;
          })
          .catch((err) => {
            return new Response("Offline", {
              status: 404,
              statusText: "Offline",
            });
          });
      })
    );
    return;
  }

  // 3. API Handling
  const isApi =
    /\/common|\/teacher|\/student|\/admin|\/face/.test(url.pathname) ||
    url.origin.includes("localhost:8889");

  if (isApi) {
    // GET requests: Network First -> Cache -> Local Fallback
    if (request.method === "GET") {
      event.respondWith(
        caches.open("exam-api-cache").then((cache) => {
          return fetch(request)
            .then((res) => {
              // Update cache if successful
              if (res.ok) cache.put(request, res.clone());
              return res;
            })
            .catch(() => {
              return cache.match(request).then((cached) => {
                if (cached) return cached;
                // If neither network nor cache works, return a JSON error
                return new Response(
                  JSON.stringify({ code: 503, message: "Offline" }),
                  {
                    status: 503,
                    headers: { "Content-Type": "application/json" },
                  }
                );
              });
            });
        })
      );
      return;
    }

    // Non-GET requests (POST, PUT, etc.): background sync queue
    event.respondWith(handleApiRequest(request));
    return;
  }

  // 4. Default fallback
  event.respondWith(
    caches.match(request).then((cached) => {
      if (cached) return cached;
      return fetch(request).catch((err) => {
        return new Response("Offline", { status: 404, statusText: "Offline" });
      });
    })
  );
});

async function handleApiRequest(request) {
  const online = self.navigator.onLine !== false;
  if (online) {
    try {
      const res = await fetch(request.clone());
      if (res.ok) return res;
    } catch (err) {
      // fallthrough to queue when fetch fails
    }
  }

  await self.queueRequest(request);
  return new Response(
    JSON.stringify({ queued: true, message: "已离线缓存，网络恢复后自动重放" }),
    {
      status: 202,
      headers: { "Content-Type": "application/json" },
    }
  );
}

self.addEventListener("sync", (event) => {
  if (event.tag === "exam-sync") {
    event.waitUntil(self.replayQueuedRequests());
  }
});

self.addEventListener("message", (event) => {
  const { type, examId, urls = [] } = event.data || {};
  if (type === "cache-exam-assets" && examId && urls.length) {
    event.waitUntil(cacheExamAssets(examId, urls));
  }
});

async function cacheExamAssets(examId, urls) {
  const cache = await caches.open(`exam-${examId}`);
  // Use Promise.all with catch to ensure valid assets are cached even if some fail
  await Promise.all(
    urls.map((url) =>
      cache.add(url).catch((err) => {
        // Suppress errors for missing assets (e.g., 404s)
        // console.warn(`[SW] Failed to cache asset: ${url}`, err);
      })
    )
  );
}
