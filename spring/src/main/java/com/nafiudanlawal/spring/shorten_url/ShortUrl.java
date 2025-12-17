package com.nafiudanlawal.spring.shorten_url;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.List;

@Table(name=EntityConstants.SHORTEN_URL_TABLE_NAME)
@Entity
public class ShortUrl {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    @NotEmpty(message = "url must not be empty")
    @URL(message = "url must be a valid url")
    @NotNull(message = "url is required")
    private String url;
    @Column(nullable = false, unique = true, length = 20)
    private String shortCode;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @CreatedDate
    private Date createdAt;


    @LastModifiedDate
    private Date updatedAt;


    @OneToMany(mappedBy = "shortUrl", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<AccessLog> stats;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public List<AccessLog> getStats() {
        return stats;
    }

    public void setStats(List<AccessLog> stats) {
        this.stats = stats;
    }
}
