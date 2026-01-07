/* Service Worker for offline-first exam experience */
importScripts("./sw-queue.js");

const STATIC_CACHE = "exam-static-v1";
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
          keys.filter((k) => k !== STATIC_CACHE).map((k) => caches.delete(k))
        )
      )
      .then(() => self.clients.claim())
  );
});

self.addEventListener("fetch", (event) => {
  const { request } = event;
  const url = new URL(request.url);

  // Cache-first for static assets
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

  // API queue and replay
  const isApi =
    /\/common|\/teacher|\/student|\/admin|\/face/.test(url.pathname) ||
    url.origin.includes("localhost:8889");
  if (!isApi) return; // non-api fall through

  event.respondWith(handleApiRequest(request));
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
  await cache.addAll(urls);
}
