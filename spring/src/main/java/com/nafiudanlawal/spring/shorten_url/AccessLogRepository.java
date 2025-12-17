package com.nafiudanlawal.spring.shorten_url;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccessLogRepository extends JpaRepository<AccessLog, Integer> {
    List<AccessLog> findAllByShortUrlShortCode(String code);
}
