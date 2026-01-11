import { openDB } from "idb";

const DB_NAME = "exam-answers-data";
const VERSION = 1;
const ANSWER_STORE = "answers";

const dbPromise = openDB(DB_NAME, VERSION, {
  upgrade(db) {
    if (!db.objectStoreNames.contains(ANSWER_STORE)) {
      db.createObjectStore(ANSWER_STORE, { keyPath: ["examId", "questionId"] });
    }
  },
});

export async function saveAnswer(record) {
  const db = await dbPromise;
  return db.put(ANSWER_STORE, { ...record, updatedAt: Date.now() });
}

export async function loadAnswers(examId) {
  const db = await dbPromise;
  const tx = db.transaction(ANSWER_STORE);
  const all = await tx.store.getAll();
  return all.filter((row) => row.examId === examId);
}

export async function clearExam(examId) {
  const db = await dbPromise;
  const tx = db.transaction(ANSWER_STORE, "readwrite");
  const all = await tx.store.getAll();
  all.forEach((row) => {
    if (row.examId === examId) {
      tx.store.delete([row.examId, row.questionId]);
    }
  });
  return tx.done;
}
