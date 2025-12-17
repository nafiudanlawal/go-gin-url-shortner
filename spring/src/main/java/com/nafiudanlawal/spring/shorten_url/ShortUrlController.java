package com.nafiudanlawal.spring.shorten_url;

import com.nafiudanlawal.spring.shorten_url.dto.CreateShortUrlDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/shorten")
public class ShortUrlController {
    private final ShortUrlRepository shortUrlRepository;
    private final ShortUrlService shortUrlService;
    private final AccessLogRepository accessLogRepository;

    public ShortUrlController(ShortUrlRepository shortUrlRepository, ShortUrlService shortUrlService, AccessLogRepository accessLogRepository) {
        this.shortUrlRepository = shortUrlRepository;
        this.shortUrlService = shortUrlService;
        this.accessLogRepository = accessLogRepository;
    }

    @GetMapping("/")
    public List<ShortUrl> getAll() {
        return this.shortUrlRepository.findAll();
    }

    @PostMapping("/")
    public ShortUrl create(@Valid @RequestBody CreateShortUrlDTO shortUrlData) {
        ShortUrl shortUrl = shortUrlService.createShortUrl(shortUrlData.getUrl());
        return this.shortUrlRepository.save(shortUrl);
    }

    @GetMapping("/{code}")
    @Transactional
    public ShortUrl getOne(@PathVariable("code") String code, @RequestHeader(HttpHeaders.HOST) String host) {
        var rx = this.shortUrlRepository.findShortUrlByShortCode(code);
        if(rx.isPresent()){
            ShortUrl shortUrl = rx.get();
            AccessLog accessLog = new AccessLog();
            accessLog.setShortUrl(shortUrl);
            accessLog.setOriginIP(host);
            shortUrl.getStats().add(accessLog);
            shortUrlRepository.save(shortUrl);
            return shortUrl;
        }
        throw  new NoSuchElementException("Shortcode:" + code + " not found");
    }

    @GetMapping("/{code}/stats")
    public List<AccessLog> getStat(@PathVariable("code") String code) {
        return this.accessLogRepository.findAllByShortUrlShortCode(code);
    }

    @DeleteMapping("/{code}")
    public void deleteOne(@PathVariable("code") String code) {
        this.shortUrlRepository.deleteShortUrlByShortCode(code);
    }

}
