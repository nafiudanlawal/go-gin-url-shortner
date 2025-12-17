package com.nafiudanlawal.spring.shorten_url;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Table(name = EntityConstants.ACCESS_LOG_TABLE_NAME) @Entity
public class AccessLog {
    @Id() @GeneratedValue()
    private Integer id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "shortUrlId", nullable = false)
    @JsonIgnore
    private ShortUrl shortUrl;
    private String originIP;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ShortUrl getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(ShortUrl shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getOriginIP() {
        return originIP;
    }

    public void setOriginIP(String originIP) {
        this.originIP = originIP;
    }
}
