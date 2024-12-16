package com.example.task2.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateBookReviewRequest {

    @NotNull(message = "Book ID is required.")
    private Long bookId;

    @Min(value = 1, message = "Rating must be at least 1.")
    @Max(value = 5, message = "Rating must not exceed 5.")
    private int rating;

    @NotBlank(message = "Review text is required.")
    @Size(max = 1000, message = "Review text must not exceed 1000 characters.")
    private String reviewText;
}
