package com.example.task2.dto;

import lombok.Data;

@Data
public class BookSearchRequest {
    private String title;
    private String author;
    private Long genreId;
    private Long publisherId;
}
