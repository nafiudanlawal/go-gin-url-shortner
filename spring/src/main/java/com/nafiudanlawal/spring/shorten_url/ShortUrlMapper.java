package com.nafiudanlawal.spring.shorten_url;

import com.nafiudanlawal.spring.shorten_url.dto.ShortUrlResponseDto;
import org.springframework.stereotype.Service;

@Service
public class ShortUrlMapper {
    public ShortUrlResponseDto shortUrlResponseDtoFromShortUrl(ShortUrl shortUrl){
        return new ShortUrlResponseDto(
                shortUrl.getId(),
                shortUrl.getShortCode(),
                shortUrl.getUrl(),
                shortUrl.getCreatedAt(),
                shortUrl.getUpdatedAt()
        );
    }
}
