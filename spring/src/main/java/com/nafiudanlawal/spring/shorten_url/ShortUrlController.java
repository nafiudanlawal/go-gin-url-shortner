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

import java.util.List;

@RestController
@RequestMapping("/shorten")
@CrossOrigin
public class ShortUrlController {

    private final ShortUrlService shortUrlService;

    public ShortUrlController(ShortUrlService shortUrlService) {
        this.shortUrlService = shortUrlService;
    }

    @GetMapping("")
    public List<ShortUrlResponseDto> getAll() {
        return this.shortUrlService.getAllShortUrls();
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ShortUrlResponseDto create(@Valid @RequestBody CreateShortUrlDto dto) {
        return  this.shortUrlService.createShortUrl(dto);
    }

    @PutMapping("{code}")
    public ShortUrlResponseDto update(@Valid @RequestBody UpdateShortUrlDto shortUrlData, @PathVariable("code") String code) {
        return shortUrlService.updateShortUrl(code, shortUrlData.url());
    }

    @GetMapping("{code}")
    @Transactional
    public ShortUrlResponseDto getOne(@PathVariable("code") String code, @RequestHeader(HttpHeaders.HOST) String host) {
        return this.shortUrlService.getShortUrlByCode(code, host);
    }

    @GetMapping("{code}/stats")
    public AccessLogResponseDto getStat(@PathVariable("code") String code) {
       return this.shortUrlService.getShortUrlStatsByCode(code);
    }

    @DeleteMapping("{code}")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOne(@PathVariable("code") String code) {
        this.shortUrlService.deleteShortUrlByCode(code);
    }

}
