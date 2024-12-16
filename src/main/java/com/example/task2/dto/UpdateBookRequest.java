package com.example.task2.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UpdateBookRequest {
    private String isbn;
    private String title;
    private String author;
    private String description;
    private Integer copiesAvailable;
    private Long genreId;
    private Long publisherId;
    private Date publishedDate;
}
