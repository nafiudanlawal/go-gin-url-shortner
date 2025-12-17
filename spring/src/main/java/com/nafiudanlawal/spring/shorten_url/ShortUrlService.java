package com.nafiudanlawal.spring.shorten_url;

import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Service
public class ShortUrlService {
    ShortUrl createShortUrl(String url) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = md.digest(url.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);
            String hashText = no.toString(16);

            // pick random substring
            int start = (int) Math.round(Math.random() * 10);
            String code = hashText.substring(start, start + 5);
            ShortUrl shortUrl = new ShortUrl();
            shortUrl.setUrl(url);
            shortUrl.setShortCode(code);
            shortUrl.setCreatedAt(new Date());
            shortUrl.setUpdatedAt(new Date());
            return shortUrl;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
