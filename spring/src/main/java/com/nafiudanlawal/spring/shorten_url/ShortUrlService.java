package com.nafiudanlawal.spring.shorten_url;

import com.nafiudanlawal.spring.shorten_url.dto.AccessLogResponseDto;
import com.nafiudanlawal.spring.shorten_url.dto.ShortUrlResponseDto;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ShortUrlService {
    private final ShortUrlRepository shortUrlRepository;
    private final ShortUrlMapper shortUrlMapper;
    private final AccessLogMapper accessLogMapper;

    public ShortUrlService(ShortUrlRepository shortUrlRepository, ShortUrlMapper shortUrlMapper, AccessLogMapper accessLogMapper) {
        this.shortUrlRepository = shortUrlRepository;
        this.shortUrlMapper = shortUrlMapper;
        this.accessLogMapper = accessLogMapper;
    }


    List<ShortUrlResponseDto> getAllShortUrls() {
        return this.shortUrlRepository
                .findAll()
                .stream()
                .map(shortUrlMapper::shortUrlResponseDtoFromShortUrl)
                .collect(Collectors.toList());
    }

    ShortUrlResponseDto createShortUrl(String url) {
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
            shortUrlRepository.save(shortUrl);
            return shortUrlMapper.shortUrlResponseDtoFromShortUrl(shortUrl);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    ShortUrlResponseDto updateShortUrl(String code, String url) {
        var rx = shortUrlRepository.findByShortCode(code);
        if (rx.isEmpty()) {
            throw new NoSuchElementException("Shortcode:" + code + " not found");
        }
        ShortUrl shortUrl = rx.get();
        shortUrl.setUrl(url);
        shortUrl.setUpdatedAt(new Date());
        shortUrlRepository.save(shortUrl);
        return shortUrlMapper.shortUrlResponseDtoFromShortUrl(shortUrl);
    }


    ShortUrlResponseDto getShortUrlByCode(String code, String requestHost) {
        var rx = this.shortUrlRepository.findByShortCode(code);
        if (rx.isEmpty()) {
            throw new NoSuchElementException("Shortcode:%s not found".formatted(code));
        }
        ShortUrl shortUrl = rx.get();
        AccessLog accessLog = new AccessLog();
        accessLog.setShortUrl(shortUrl);
        accessLog.setOriginIP(requestHost);
        accessLog.setCreatedAt(new Date());
        accessLog.setUpdatedAt(new Date());

        shortUrl.setUpdatedAt(new Date());

        shortUrl.getStats().add(accessLog);
        shortUrlRepository.save(shortUrl);
        return shortUrlMapper.shortUrlResponseDtoFromShortUrl(shortUrl);
    }

    AccessLogResponseDto getShortUrlStatsByCode(String code){
        var rx = this.shortUrlRepository.findByShortCode(code);
        if (rx.isEmpty()) {
            throw new NoSuchElementException("Shortcode:%s not found".formatted(code));
        }
        ShortUrl shortUrl = rx.get();
        return this.accessLogMapper.accessLogResponseDtoFromShortUrl(shortUrl);
    }

    void deleteShortUrlByCode(String code){
        var rx = this.shortUrlRepository.findByShortCode(code);
        if (rx.isEmpty()) {
            throw new NoSuchElementException("Shortcode:%s not found".formatted(code));
        }
        this.shortUrlRepository.deleteByShortCode(code);
    }
}
