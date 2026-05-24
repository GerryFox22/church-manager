package com.gerardo.churchmanager.backend.news.controller;

import com.gerardo.churchmanager.backend.news.dto.CreateNewsRequest;
import com.gerardo.churchmanager.backend.news.dto.NewsResponse;
import com.gerardo.churchmanager.backend.news.dto.UpdateNewsRequest;
import com.gerardo.churchmanager.backend.news.service.NewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public NewsResponse create(
            @ModelAttribute @Valid CreateNewsRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        return newsService.create(request, image);
    }

    @GetMapping
    public List<NewsResponse> getAll() {

        return newsService.getAll();

    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public NewsResponse update(
            @PathVariable Long id,
            @ModelAttribute @Valid UpdateNewsRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        return newsService.update(id, request, image);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        newsService.delete(id);
    }

}
