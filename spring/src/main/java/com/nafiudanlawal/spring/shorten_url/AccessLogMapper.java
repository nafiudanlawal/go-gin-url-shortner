package com.nafiudanlawal.spring.shorten_url;

import com.nafiudanlawal.spring.shorten_url.dto.AccessLogResponseDto;
import org.springframework.stereotype.Service;

@Service
public class AccessLogMapper {

    private final AccessLogRepository accessLogRepository;
    public AccessLogMapper(AccessLogRepository accessLogRepository) {
        this.accessLogRepository = accessLogRepository;
    }
    public AccessLogResponseDto accessLogResponseDtoFromShortUrl(ShortUrl shortUrl){
        Integer count = this.accessLogRepository.countAllByShortUrlShortCode(shortUrl.getShortCode());
        return new AccessLogResponseDto(
                shortUrl.getId(),
                shortUrl.getShortCode(),
                shortUrl.getUrl(),
                shortUrl.getCreatedAt(),
                shortUrl.getUpdatedAt(),
                count
        );
    }
}
