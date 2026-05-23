package com.gerardo.churchmanager.backend.news.controller;

import com.gerardo.churchmanager.backend.news.dto.CreateNewsRequest;
import com.gerardo.churchmanager.backend.news.dto.NewsResponse;
import com.gerardo.churchmanager.backend.news.service.NewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public NewsResponse create(@RequestBody @Valid CreateNewsRequest request) {

        return newsService.create(request);

    }

    @GetMapping
    public List<NewsResponse> getAll() {

        return newsService.getAll();

    }

}
