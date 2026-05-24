package com.gerardo.churchmanager.backend.events.service;

import com.gerardo.churchmanager.backend.common.exception.ResourceNotFoundException;
import com.gerardo.churchmanager.backend.events.dto.CreateEventRequest;
import com.gerardo.churchmanager.backend.events.dto.EventResponse;
import com.gerardo.churchmanager.backend.events.dto.UpdateEventRequest;
import com.gerardo.churchmanager.backend.events.entity.Event;
import com.gerardo.churchmanager.backend.events.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public EventResponse create(CreateEventRequest request, MultipartFile image) {
        Event event = Event.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .eventDate(request.getEventDate())
                .location(request.getLocation())
                .build();

        if (image != null && !image.isEmpty()) {
            event.setImageUrl(saveImage(image));
        }

        Event savedEvent = eventRepository.save(event);

        return mapToResponse(savedEvent);
    }

    public List<EventResponse> getAll() {
        return eventRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public EventResponse update(Long id, UpdateEventRequest request, MultipartFile image) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setEventDate(request.getEventDate());
        event.setLocation(request.getLocation());

        if (image != null && !image.isEmpty()) {
            event.setImageUrl(saveImage(image));
        }

        Event updatedEvent = eventRepository.save(event);

        return mapToResponse(updatedEvent);
    }

    public void delete(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        eventRepository.delete(event);
    }

    private String saveImage(MultipartFile image) {
        try {
            String uploadDir = "uploads/events/";

            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();

            Path filePath = Paths.get(uploadDir + fileName);

            Files.createDirectories(filePath.getParent());

            Files.write(filePath, image.getBytes());

            return "/uploads/events/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException("Error while saving event image");
        }
    }

    private EventResponse mapToResponse(Event event) {
        return EventResponse.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .location(event.getLocation())
                .imageUrl(event.getImageUrl())
                .build();
    }
}
