package com.example.task2.controllers;

import com.example.task2.dto.ApiResponse;
import com.example.task2.dto.BookSearchRequest;
import com.example.task2.dto.CreateBookRequest;
import com.example.task2.dto.UpdateBookRequest;
import com.example.task2.entities.Book;
import com.example.task2.entities.BookTransaction;
import com.example.task2.services.BookService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@AllArgsConstructor
@Validated
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Book>>> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Book>> getBook(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Book>> createBook(@Valid @RequestBody CreateBookRequest request) {
        return bookService.createBook(request);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Book>> updateBook(@PathVariable Long id, @Valid @RequestBody UpdateBookRequest request) {
        return bookService.updateBook(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable Long id) {
        return bookService.deleteBook(id);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Book>>> searchBooks(@RequestBody BookSearchRequest searchRequest) {
        return bookService.searchBooks(searchRequest);
    }

    @PostMapping("/{isbn}/borrow")
    public ResponseEntity<ApiResponse<BookTransaction>> borrowBook(@PathVariable String isbn) {
        return bookService.borrowBook(isbn);
    }

    @PostMapping("/{isbn}/return")
    public ResponseEntity<ApiResponse<BookTransaction>> returnBook(@PathVariable String isbn) {
        return bookService.returnBook(isbn);
    }
}
