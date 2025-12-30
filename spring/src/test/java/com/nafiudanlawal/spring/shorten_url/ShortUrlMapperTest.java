package com.nafiudanlawal.spring.shorten_url;

import com.nafiudanlawal.spring.shorten_url.dto.ShortUrlResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShortUrlMapperTest {
    ShortUrlMapper shortUrlMapper;
    @BeforeEach
    void setUp(){
        shortUrlMapper = new ShortUrlMapper();
    }

    @Test
    public void shouldMapShortUrlResponseDtoToShortUrl() {
        ShortUrl shortUrl = new ShortUrl("url", "code");
        ShortUrlResponseDto shortUrlResponseDto = shortUrlMapper.responseDtoFromShortUrl(shortUrl);
        assertEquals(shortUrlResponseDto.url(), shortUrl.getUrl());
        assertEquals(shortUrlResponseDto.shortCode(), shortUrl.getShortCode());
        assertEquals(shortUrlResponseDto.createAt(), shortUrl.getCreatedAt());
        assertEquals(shortUrlResponseDto.updateAt(), shortUrl.getUpdatedAt());
    }

    @Test
    public void shouldNotMapShortUrlResponseDtoToShortUrl() {
        ShortUrl shortUrl = new ShortUrl("url", "code");
        ShortUrl shortUrl1 = new ShortUrl("url1", "code1");

        ShortUrlResponseDto shortUrlResponseDto = shortUrlMapper.responseDtoFromShortUrl(shortUrl);

        assertNotEquals(shortUrlResponseDto.url(), shortUrl1.getUrl());
        assertNotEquals(shortUrlResponseDto.shortCode(), shortUrl1.getShortCode());
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenShortUrlIsNull() {
        var ex = assertThrows(NullPointerException.class, () -> shortUrlMapper.responseDtoFromShortUrl(null));
        assertEquals(ex.getMessage(), "shortUrl must not be null");
    }
}