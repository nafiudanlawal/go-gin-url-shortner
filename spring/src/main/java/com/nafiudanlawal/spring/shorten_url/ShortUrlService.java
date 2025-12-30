package com.nafiudanlawal.spring.shorten_url;

import com.nafiudanlawal.spring.shorten_url.dto.AccessLogResponseDto;
import com.nafiudanlawal.spring.shorten_url.dto.CreateShortUrlDto;
import com.nafiudanlawal.spring.shorten_url.dto.ShortUrlResponseDto;
import com.nafiudanlawal.spring.shorten_url.utils.CommonUtil;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ShortUrlService {
    private final ShortUrlRepository shortUrlRepository;
    private final ShortUrlMapper shortUrlMapper;
    private final AccessLogMapper accessLogMapper;

    public ShortUrlService(
            ShortUrlRepository shortUrlRepository,
            ShortUrlMapper shortUrlMapper,
            AccessLogMapper accessLogMapper
    ) {
        this.shortUrlRepository = shortUrlRepository;
        this.shortUrlMapper = shortUrlMapper;
        this.accessLogMapper = accessLogMapper;
    }


    List<ShortUrlResponseDto> getAllShortUrls() {
        return this.shortUrlRepository
                .findAll()
                .stream()
                .map(shortUrlMapper::responseDtoFromShortUrl)
                .toList();
    }

    ShortUrlResponseDto createShortUrl(@NotNull CreateShortUrlDto dto) {
        String code = CommonUtil.generateCode(dto.url());
        ShortUrl shortUrl = new ShortUrl(dto.url(), code);
        shortUrlRepository.save(shortUrl);
        return shortUrlMapper.responseDtoFromShortUrl(shortUrl);
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
        return shortUrlMapper.responseDtoFromShortUrl(shortUrl);
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
        return shortUrlMapper.responseDtoFromShortUrl(shortUrl);
    }

    AccessLogResponseDto getShortUrlStatsByCode(String code) {
        var rx = this.shortUrlRepository.findByShortCode(code);
        if (rx.isEmpty()) {
            throw new NoSuchElementException("Shortcode:%s not found".formatted(code));
        }
        ShortUrl shortUrl = rx.get();
        return this.accessLogMapper.accessLogResponseDtoFromShortUrl(shortUrl);
    }

    void deleteShortUrlByCode(String code) {
        var rx = this.shortUrlRepository.findByShortCode(code);
        if (rx.isEmpty()) {
            throw new NoSuchElementException("Shortcode:%s not found".formatted(code));
        }
        this.shortUrlRepository.deleteByShortCode(code);
    }
}
