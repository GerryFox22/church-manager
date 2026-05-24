package com.gerardo.churchmanager.backend.news.service;

import com.gerardo.churchmanager.backend.common.exception.ResourceNotFoundException;
import com.gerardo.churchmanager.backend.news.dto.CreateNewsRequest;
import com.gerardo.churchmanager.backend.news.dto.NewsResponse;
import com.gerardo.churchmanager.backend.news.dto.UpdateNewsRequest;
import com.gerardo.churchmanager.backend.news.entity.News;
import com.gerardo.churchmanager.backend.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    public NewsResponse create(CreateNewsRequest request, MultipartFile image) {
        News news = News.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .build();

        if (image != null && !image.isEmpty()) {
            news.setImageUrl(saveImage(image));
        }

        News savedNews = newsRepository.save(news);

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
                .imageUrl(news.getImageUrl())
                .createdAt(news.getCreatedAt())
                .build();

    }

    public NewsResponse update(Long id, UpdateNewsRequest request, MultipartFile image) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("News not found"));

        news.setTitle(request.getTitle());
        news.setContent(request.getContent());

        if (image != null && !image.isEmpty()) {
            news.setImageUrl(saveImage(image));
        }

        News updatedNews = newsRepository.save(news);

        return mapToResponse(updatedNews);
    }

    public void delete(Long id) {

        News news = newsRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("News not found")
                );

        newsRepository.delete(news);
    }

    private String saveImage(MultipartFile image) {

        try {
            String uploadDir = "uploads/news/";

            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();

            Path filePath = Paths.get(uploadDir + fileName);

            Files.createDirectories(filePath.getParent());

            Files.write(filePath, image.getBytes());

            return "/uploads/news/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException("Error while saving image");
        }
    }

}
