package com.nafiudanlawal.spring.shorten_url;

import org.springframework.data.jpa.repository.JpaRepository;


public interface AccessLogRepository extends JpaRepository<AccessLog, Integer> {
    Integer countAllByShortUrlShortCode(String code);
}
