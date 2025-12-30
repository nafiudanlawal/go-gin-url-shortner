package com.nafiudanlawal.spring.shorten_url;

import com.nafiudanlawal.spring.shorten_url.dto.CreateShortUrlDto;
import com.nafiudanlawal.spring.shorten_url.dto.ShortUrlResponseDto;
import com.nafiudanlawal.spring.shorten_url.dto.UpdateShortUrlDto;
import com.nafiudanlawal.spring.shorten_url.utils.CommonUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ShortUrlServiceTest {

    @InjectMocks
    private ShortUrlService shortUrlService;
    @Mock
    private ShortUrlRepository shortUrlRepository;
    @Mock
    private ShortUrlMapper shortUrlMapper;
    @Mock
    private AccessLogMapper accessLogMapper;

    @Test
    void shouldCreateShortUrl() {
        mockStatic(CommonUtil.class);
        String generatedCode = "random";
        CreateShortUrlDto urlDto = new CreateShortUrlDto("url");
        ShortUrl shortUrl = new ShortUrl(urlDto.url(), generatedCode);
        ShortUrl savedShortUrl = new ShortUrl(1, urlDto.url(), shortUrl.getShortCode());
        savedShortUrl.setCreatedAt(shortUrl.getCreatedAt());
        savedShortUrl.setCreatedAt(shortUrl.getUpdatedAt());
        ShortUrlResponseDto responseDto = new ShortUrlResponseDto(
                savedShortUrl.getId(),
                savedShortUrl.getShortCode(),
                savedShortUrl.getUrl(),
                savedShortUrl.getCreatedAt(),
                savedShortUrl.getUpdatedAt()
        );

        when(shortUrlRepository.save(any(ShortUrl.class))).thenReturn(savedShortUrl);
        when(shortUrlMapper.responseDtoFromShortUrl(any(ShortUrl.class))).thenReturn(responseDto);
        when(CommonUtil.generateCode(urlDto.url())).thenReturn(generatedCode);

        ShortUrlResponseDto shortUrlResponseDto = shortUrlService.createShortUrl(urlDto);

        assertEquals(shortUrlResponseDto.url(), savedShortUrl.getUrl());
        assertEquals(shortUrlResponseDto.shortCode(), savedShortUrl.getShortCode());
        assertEquals(shortUrlResponseDto.id(), savedShortUrl.getId());
        assertEquals(shortUrlResponseDto.createAt(), savedShortUrl.getCreatedAt());
        assertEquals(shortUrlResponseDto.createAt(), savedShortUrl.getUpdatedAt());

        verify(shortUrlRepository, times(1)).save(any(ShortUrl.class));
        verify(shortUrlMapper, times(1)).responseDtoFromShortUrl(any(ShortUrl.class));
    }

    @Test
    void shouldThrowExceptionWhenCreateShortUrlWithNullInput() {
        assertThrows(NullPointerException.class, () -> shortUrlService.createShortUrl(null));
    }

    @Test
    void shouldUpdateShortUrl() {
        String code = "code";
        String newUrl = "https://url.com";
        UpdateShortUrlDto urlDto = new UpdateShortUrlDto(newUrl);

        ShortUrl shortUrl = new ShortUrl(1, "oldUrl", code);
        ShortUrl savedShortUrl = new ShortUrl(1, newUrl, code);
        ShortUrlResponseDto responseDto = new ShortUrlResponseDto(
                savedShortUrl.getId(),
                savedShortUrl.getShortCode(),
                savedShortUrl.getUrl(),
                savedShortUrl.getCreatedAt(),
                savedShortUrl.getUpdatedAt()
        );

        when(shortUrlRepository.save(any(ShortUrl.class))).thenReturn(savedShortUrl);
        when(shortUrlRepository.findByShortCode(code)).thenReturn(Optional.of(shortUrl));
        when(shortUrlMapper.responseDtoFromShortUrl(any(ShortUrl.class))).thenReturn(responseDto);

        ShortUrlResponseDto shortUrlResponseDto = shortUrlService.updateShortUrl(code, urlDto.url());

        assertEquals(shortUrlResponseDto.url(), newUrl);
        assertEquals(shortUrlResponseDto.shortCode(), savedShortUrl.getShortCode());
        assertEquals(shortUrlResponseDto.id(), savedShortUrl.getId());
        verify(shortUrlRepository, times(1)).findByShortCode(code);
        verify(shortUrlMapper, times(1)).responseDtoFromShortUrl(any(ShortUrl.class));
    }

    @Test
    void shouldGetAllShortUrls() {
        List<ShortUrl> shortUrlList = new ArrayList<>();
        shortUrlList.add(new ShortUrl(1, "url1", "code1"));
        shortUrlList.add(new ShortUrl(2, "url2", "code2"));
        shortUrlList.add(new ShortUrl(3, "url3", "code3"));
        when(shortUrlRepository.findAll()).thenReturn(shortUrlList);
        when(shortUrlMapper.responseDtoFromShortUrl(any(ShortUrl.class))).thenReturn(new ShortUrlResponseDto(
                1,
                "code1",
                "url1",
                new Date(),
                new Date()
        ));

        List<ShortUrlResponseDto> shortUrlResponseDtoList = shortUrlService.getAllShortUrls();
        assertEquals(shortUrlResponseDtoList.size(), shortUrlList.size());
        assertEquals(shortUrlResponseDtoList.get(0).url(), shortUrlList.get(0).getUrl());

        verify(shortUrlRepository, times(1)).findAll();
        verify(shortUrlMapper, times(shortUrlList.size())).responseDtoFromShortUrl(any(ShortUrl.class));

    }
}