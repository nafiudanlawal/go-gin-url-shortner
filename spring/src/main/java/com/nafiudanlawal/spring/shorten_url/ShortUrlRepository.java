package com.nafiudanlawal.spring.shorten_url;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository <ShortUrl, Integer> {
    Optional<ShortUrl> findByShortCode(String code);

    void deleteByShortCode(String code);

}
