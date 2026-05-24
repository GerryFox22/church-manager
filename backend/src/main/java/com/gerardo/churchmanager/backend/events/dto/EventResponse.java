package com.gerardo.churchmanager.backend.events.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class EventResponse {

    private Long id;

    private String title;

    private String description;

    private LocalDateTime eventDate;

    private String location;

    private String imageUrl;
}
