package com.gerardo.churchmanager.backend.news.controller;

import com.gerardo.churchmanager.backend.news.dto.CreateNewsRequest;
import com.gerardo.churchmanager.backend.news.dto.NewsResponse;
import com.gerardo.churchmanager.backend.news.dto.UpdateNewsRequest;
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

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public NewsResponse update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateNewsRequest request
    ) {
        return newsService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        newsService.delete(id);
    }

}
