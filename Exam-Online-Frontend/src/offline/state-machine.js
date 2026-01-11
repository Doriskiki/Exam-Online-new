import CryptoJS from "crypto-js";
import { saveAnswer, loadAnswers, clearExam } from "./storage";

const STATE = { DRAFT: "draft", LOCAL: "local-saved", SYNCED: "synced" };

function deriveKey() {
  const cached = sessionStorage.getItem("exam-offline-key");
  if (cached) return cached;
  const token = localStorage.getItem("authorization") || "exam-offline";
  const derived = CryptoJS.SHA256(token).toString();
  sessionStorage.setItem("exam-offline-key", derived);
  return derived;
}

export async function recordAnswer({ examId, questionId, answer }) {
  const key = deriveKey();
  const cipher = CryptoJS.AES.encrypt(
    JSON.stringify({ answer, ts: Date.now() }),
    key
  ).toString();
  return saveAnswer({ examId, questionId, cipher, state: STATE.LOCAL });
}

export async function restoreAnswers(examId) {
  const key = deriveKey();
  const rows = await loadAnswers(examId);
  return rows
    .map((row) => {
      try {
        const bytes = CryptoJS.AES.decrypt(row.cipher, key);
        const plain = bytes.toString(CryptoJS.enc.Utf8);
        if (!plain) return null;
        const parsed = JSON.parse(plain);
        return {
          questionId: row.questionId,
          answer: parsed.answer,
          ts: parsed.ts,
        };
      } catch (e) {
        console.error("解密失败", e);
        return null;
      }
    })
    .filter(Boolean);
}

export async function clearExamState(examId) {
  await clearExam(examId);
}
