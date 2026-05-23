package com.gerardo.churchmanager.backend.news.service;

import com.gerardo.churchmanager.backend.news.dto.CreateNewsRequest;
import com.gerardo.churchmanager.backend.news.dto.NewsResponse;
import com.gerardo.churchmanager.backend.news.entity.News;
import com.gerardo.churchmanager.backend.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    public NewsResponse create(
            CreateNewsRequest request
    ) {

        News news = News.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .build();

        News savedNews =
                newsRepository.save(news);

        return mapToResponse(savedNews);

    }

    public List<NewsResponse> getAll() {

        return newsRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();

    }

    private NewsResponse mapToResponse(
            News news
    ) {

        return NewsResponse.builder()
                .id(news.getId())
                .title(news.getTitle())
                .content(news.getContent())
                .createdAt(news.getCreatedAt())
                .build();

    }

}
