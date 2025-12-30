package com.nafiudanlawal.spring.shorten_url.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CommonUtil {
    public static String generateCode(String url){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = md.digest(url.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);
            String hashText = no.toString(16);

            // pick random substring
            int start = (int) Math.round(Math.random() * 10);
            return hashText.substring(start, start + 5);
        }catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
