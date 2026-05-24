package com.gerardo.churchmanager.backend.events.controller;

import com.gerardo.churchmanager.backend.events.dto.CreateEventRequest;
import com.gerardo.churchmanager.backend.events.dto.EventResponse;
import com.gerardo.churchmanager.backend.events.dto.UpdateEventRequest;
import com.gerardo.churchmanager.backend.events.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponse create(
            @ModelAttribute @Valid CreateEventRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        return eventService.create(request, image);
    }

    @GetMapping
    public List<EventResponse> getAll() {
        return eventService.getAll();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public EventResponse update(
            @PathVariable Long id,
            @ModelAttribute @Valid UpdateEventRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        return eventService.update(id, request, image);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        eventService.delete(id);
    }
}
