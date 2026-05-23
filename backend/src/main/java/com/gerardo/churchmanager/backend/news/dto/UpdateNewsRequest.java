package com.gerardo.churchmanager.backend.news.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateNewsRequest {

    @NotBlank
    @Size(max = 150)
    private String title;

    @NotBlank
    private String content;
}
