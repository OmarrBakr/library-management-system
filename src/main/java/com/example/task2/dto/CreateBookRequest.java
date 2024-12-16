package com.example.task2.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class CreateBookRequest {

    @NotEmpty(message = "ISBN is required")
    private String isbn;

    @NotEmpty(message = "Title is required")
    private String title;

    @NotEmpty
    private String author;

    @NotEmpty
    private String description;

    @NotNull(message = "Copies available is required")
    private int copiesAvailable;

    @NotNull(message = "Genre ID is required")
    private Long genreId;

    @NotNull(message = "Publisher ID is required")
    private Long publisherId;

    @NotNull(message = "Published date is required")
    private Date publishedDate;
}
