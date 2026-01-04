package com.wzz.Util;

import com.wzz.entity.Answer;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class SaltEncryption {

    // 盐值 + SHA-256 摘要
    public static String saltEncryption(String password, String salt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashed = digest.digest((password + salt).getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hashed) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) sb.append('0');
            sb.append(hex);
        }
        return sb.toString();
    }

    //根据题目id获取答案列表中的答案索引
    public static int getIndex(List<Answer> list, Integer questionId) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getQuestionId() == questionId) {
                return i;
            }
        }
        return -1;
    }
}
