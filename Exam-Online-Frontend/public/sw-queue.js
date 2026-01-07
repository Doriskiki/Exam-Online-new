/*
 * Offline request queue with exponential backoff for replay.
 */
const DB_NAME = "exam-offline";
const STORE = "requests";
const BASE_DELAY = 800; // ms
const MAX_RETRY = 6;

function openDB() {
  return new Promise((resolve, reject) => {
    const req = indexedDB.open(DB_NAME, 1);
    req.onupgradeneeded = () => {
      const db = req.result;
      if (!db.objectStoreNames.contains(STORE)) {
        db.createObjectStore(STORE, { keyPath: "id", autoIncrement: true });
      }
    };
    req.onsuccess = () => resolve(req.result);
    req.onerror = () => reject(req.error);
  });
}

async function queueRequest(request) {
  const db = await openDB();
  const tx = db.transaction(STORE, "readwrite");
  const bodyNeeded = !["GET", "HEAD"].includes(request.method);
  const body = bodyNeeded ? await request.clone().arrayBuffer() : null;
  tx.objectStore(STORE).add({
    url: request.url,
    method: request.method,
    headers: Array.from(request.headers.entries()),
    body,
    attempt: 0,
    requestId: crypto.randomUUID(),
    createdAt: Date.now(),
  });
  await tx.complete;

  if (self.registration && "sync" in self.registration) {
    try {
      await self.registration.sync.register("exam-sync");
    } catch (e) {
      // ignore registration errors; replay will be triggered on next fetch
    }
  }
}

async function replayQueuedRequests() {
  const db = await openDB();
  const tx = db.transaction(STORE, "readwrite");
  const store = tx.objectStore(STORE);
  const all = await new Promise((resolve) => {
    const req = store.getAll();
    req.onsuccess = () => resolve(req.result || []);
  });

  for (const item of all) {
    await backoffSend(item, store);
  }
}

async function backoffSend(item, store) {
  const delay = BASE_DELAY * Math.pow(2, item.attempt) + Math.random() * 200;
  await new Promise((r) => setTimeout(r, delay));
  try {
    const res = await fetch(item.url, {
      method: item.method,
      headers: new Headers(item.headers),
      body: item.body,
    });
    if (res.ok) {
      store.delete(item.id);
      return;
    }
    throw new Error("non-2xx");
  } catch (e) {
    item.attempt += 1;
    if (item.attempt > MAX_RETRY) {
      store.delete(item.id);
      return;
    }
    store.put(item);
  }
}

self.queueRequest = queueRequest;
self.replayQueuedRequests = replayQueuedRequests;
