package com.example.task2.services;

import com.example.task2.config.JwtService;
import com.example.task2.dto.ApiResponse;
import com.example.task2.dto.CreateBookReviewRequest;
import com.example.task2.dto.UpdateBookReviewRequest;
import com.example.task2.entities.Book;
import com.example.task2.entities.BookReview;
import com.example.task2.entities.User;
import com.example.task2.exceptions.UnauthorizedException;
import com.example.task2.repositories.BookRepository;
import com.example.task2.repositories.BookReviewRepository;
import com.example.task2.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class BookReviewService {

    private final BookReviewRepository bookReviewRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public ResponseEntity<ApiResponse<List<BookReview>>> getAllBookReviews() {
        List<BookReview> bookReviews = bookReviewRepository.findAll();
        ApiResponse<List<BookReview>> response = ApiResponse.success("Book reviews retrieved successfully", bookReviews, HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<BookReview>> getBookReviewById(String id) {
        BookReview bookReview = bookReviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book review not found with id: " + id));
        ApiResponse<BookReview> response = ApiResponse.success("Book review retrieved successfully", bookReview, HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<List<BookReview>>> getReviewsByBookId(String bookId) {
        List<BookReview> reviews = bookReviewRepository.findByBookId(bookId);
        ApiResponse<List<BookReview>> response = ApiResponse.success("Reviews retrieved successfully", reviews, HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<BookReview>> createReview(CreateBookReviewRequest request) {
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + request.getBookId()));
        User user = userRepository.findById(jwtService.extractUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + jwtService.extractUserId()));
        BookReview review = new BookReview();
        review.setBook(book);
        review.setUser(user);
        review.setRating(request.getRating());
        review.setReviewText(request.getReviewText());
        review.setReviewDate(new Date());
        BookReview createdReview = bookReviewRepository.save(review);
        ApiResponse<BookReview> response = ApiResponse.success("Review created successfully", createdReview, HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Transactional
    public ResponseEntity<ApiResponse<BookReview>> updateReview(String id, UpdateBookReviewRequest request) {
        BookReview review = bookReviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + id));
        if (!Objects.equals(review.getUserId(), jwtService.extractUserId())){
            throw new UnauthorizedException("You do not have permission to update this book review.");
        }
        if (request.getRating() != null) {
            review.setRating(request.getRating());
        }
        if (request.getReviewText() != null) {
            review.setReviewText(request.getReviewText());
        }
        BookReview updatedReview = bookReviewRepository.save(review);
        ApiResponse<BookReview> response = ApiResponse.success("Review updated successfully", updatedReview, HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<ApiResponse<Void>> deleteReview(String id) {
        BookReview review = bookReviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + id));
        if (!Objects.equals(review.getUserId(), jwtService.extractUserId())){
            throw new UnauthorizedException("You do not have permission to delete this book review.");
        }
        bookReviewRepository.delete(review);
        ApiResponse<Void> response = ApiResponse.success("Review deleted successfully", null, HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
