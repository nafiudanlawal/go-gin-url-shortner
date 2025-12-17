package com.nafiudanlawal.spring.shorten_url;

import com.nafiudanlawal.spring.shorten_url.dto.AccessLogResponseDto;
import com.nafiudanlawal.spring.shorten_url.dto.CreateShortUrlDto;
import com.nafiudanlawal.spring.shorten_url.dto.ShortUrlResponseDto;
import com.nafiudanlawal.spring.shorten_url.dto.UpdateShortUrlDto;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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
    public List<ShortUrlResponseDto> getAll() {
        return this.shortUrlRepository.findAll().stream().map(shortUrl -> new ShortUrlResponseDto(
                shortUrl.getId(),
                shortUrl.getShortCode(),
                shortUrl.getUrl(),
                shortUrl.getCreatedAt(),
                shortUrl.getUpdatedAt()
        )).collect(Collectors.toList());
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public ShortUrl create(@Valid @RequestBody CreateShortUrlDto shortUrlData) {
        ShortUrl shortUrl = shortUrlService.createShortUrl(shortUrlData.url());
        return this.shortUrlRepository.save(shortUrl);
    }

    @PutMapping("/{code}")
    public ShortUrlResponseDto update(@Valid @RequestBody UpdateShortUrlDto shortUrlData, @PathVariable("code") String code) {
        ShortUrl shortUrl = shortUrlService.updateShortUrl(code, shortUrlData.url());
        this.shortUrlRepository.save(shortUrl);
        return new ShortUrlResponseDto(
                shortUrl.getId(),
                shortUrl.getShortCode(),
                shortUrl.getUrl(),
                shortUrl.getCreatedAt(),
                shortUrl.getUpdatedAt()
        );
    }

    @GetMapping("/{code}")
    @Transactional
    public ShortUrlResponseDto getOne(@PathVariable("code") String code, @RequestHeader(HttpHeaders.HOST) String host) {
        var rx = this.shortUrlRepository.findByShortCode(code);
        if (rx.isEmpty()) {
            throw new NoSuchElementException("Shortcode:" + code + " not found");
        }
        ShortUrl shortUrl = rx.get();
        AccessLog accessLog = new AccessLog();
        accessLog.setShortUrl(shortUrl);
        accessLog.setOriginIP(host);
        accessLog.setCreatedAt(new Date());
        accessLog.setUpdatedAt(new Date());

        shortUrl.setUpdatedAt(new Date());

        shortUrl.getStats().add(accessLog);
        shortUrlRepository.save(shortUrl);

        return new ShortUrlResponseDto(
                shortUrl.getId(),
                shortUrl.getShortCode(),
                shortUrl.getUrl(),
                shortUrl.getCreatedAt(),
                shortUrl.getUpdatedAt()
        );
    }

    @GetMapping("/{code}/stats")
    public AccessLogResponseDto getStat(@PathVariable("code") String code) {
        var rx = this.shortUrlRepository.findByShortCode(code);
        if (rx.isEmpty()) {
            throw new NoSuchElementException("Shortcode:" + code + " not found");
        }
        ShortUrl shortUrl = rx.get();
        Integer count = this.accessLogRepository.countAllByShortUrlShortCode(code);
        return new AccessLogResponseDto(
                shortUrl.getId(),
                shortUrl.getShortCode(),
                shortUrl.getUrl(),
                shortUrl.getCreatedAt(),
                shortUrl.getUpdatedAt(),
                count
        );
    }

    @DeleteMapping("/{code}")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOne(@PathVariable("code") String code) {
        var rx = this.shortUrlRepository.findByShortCode(code);
        if (rx.isEmpty()) {
            throw new NoSuchElementException("Shortcode:" + code + " not found");
        }
        this.shortUrlRepository.deleteByShortCode(code);
    }

}
