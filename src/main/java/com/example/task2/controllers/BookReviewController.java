package com.example.task2.controllers;

import com.example.task2.dto.ApiResponse;
import com.example.task2.dto.CreateBookReviewRequest;
import com.example.task2.dto.UpdateBookReviewRequest;
import com.example.task2.entities.BookReview;
import com.example.task2.services.BookReviewService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@AllArgsConstructor
public class BookReviewController {

    private final BookReviewService bookReviewService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<BookReview>>> getAllBookReviews() {
        return bookReviewService.getAllBookReviews();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookReview>> getBookReview(@PathVariable String id) {
        return bookReviewService.getBookReviewById(id);
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<ApiResponse<List<BookReview>>> getReviewsByBookId(@PathVariable String bookId) {
        return bookReviewService.getReviewsByBookId(bookId);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BookReview>> createReview(@Valid @RequestBody CreateBookReviewRequest request) {
        return bookReviewService.createReview(request);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<BookReview>> updateReview(@PathVariable String id, @Valid @RequestBody UpdateBookReviewRequest request) {
        return bookReviewService.updateReview(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable String id) {
        return bookReviewService.deleteReview(id);
    }
}
