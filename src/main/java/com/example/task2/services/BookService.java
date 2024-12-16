package com.example.task2.services;

import com.example.task2.config.JwtService;
import com.example.task2.dto.ApiResponse;
import com.example.task2.dto.BookSearchRequest;
import com.example.task2.dto.CreateBookRequest;
import com.example.task2.dto.UpdateBookRequest;
import com.example.task2.entities.*;
import com.example.task2.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final PublisherRepository publisherRepository;
    private final BookTransactionRepository bookTransactionRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public ResponseEntity<ApiResponse<List<Book>>> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        ApiResponse<List<Book>> response = ApiResponse.success("Books retrieved successfully", books, HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<Book>> getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));
        ApiResponse<Book> response = ApiResponse.success("Book retrieved successfully", book, HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<Book>> createBook(CreateBookRequest request) {
        Genre genre = genreRepository.findById(request.getGenreId())
                .orElseThrow(() -> new EntityNotFoundException("Genre not found with id: " + request.getGenreId()));
        Publisher publisher = publisherRepository.findById(request.getPublisherId())
                .orElseThrow(() -> new EntityNotFoundException("Publisher not found with id: " + request.getPublisherId()));

        Book book = new Book();
        book.setIsbn(request.getIsbn());
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setDescription(request.getDescription());
        book.setCopiesAvailable(request.getCopiesAvailable());
        book.setGenre(genre);
        book.setPublisher(publisher);
        book.setPublishedDate(request.getPublishedDate());

        bookRepository.save(book);
        ApiResponse<Book> response = ApiResponse.success("Book created successfully", book, HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<ApiResponse<Book>> updateBook(Long id, UpdateBookRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));
        if (request.getIsbn() != null) {
            book.setIsbn(request.getIsbn());
        }
        if (request.getTitle() != null) {
            book.setTitle(request.getTitle());
        }
        if (request.getAuthor() != null) {
            book.setAuthor(request.getAuthor());
        }
        if (request.getDescription() != null) {
            book.setDescription(request.getDescription());
        }
        if (request.getCopiesAvailable() != null) {
            book.setCopiesAvailable(request.getCopiesAvailable());
        }
        if (request.getGenreId() != null) {
            Genre genre = genreRepository.findById(request.getGenreId())
                    .orElseThrow(() -> new EntityNotFoundException("Genre not found with id: " + request.getGenreId()));
            book.setGenre(genre);
        }
        if (request.getPublisherId() != null) {
            Publisher publisher = publisherRepository.findById(request.getPublisherId())
                    .orElseThrow(() -> new EntityNotFoundException("Publisher not found with id: " + request.getPublisherId()));
            book.setPublisher(publisher);
        }
        if (request.getPublishedDate() != null) {
            book.setPublishedDate(request.getPublishedDate());
        }
        bookRepository.save(book);
        ApiResponse<Book> response = ApiResponse.success("Book updated successfully", book, HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<Void>> deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));
        bookRepository.delete(book);
        ApiResponse<Void> response = ApiResponse.success("Book deleted successfully", null, HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<ApiResponse<List<Book>>> searchBooks(BookSearchRequest searchRequest) {
        List<Book> books = bookRepository.searchBooks(
                searchRequest.getTitle(),
                searchRequest.getAuthor(),
                searchRequest.getGenreId(),
                searchRequest.getPublisherId()
        );
        ApiResponse<List<Book>> response = ApiResponse.success("Books retrieved successfully", books, HttpStatus.OK);
        return ResponseEntity.ok(response);
    }


    public ResponseEntity<ApiResponse<BookTransaction>> borrowBook(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ISBN: " + isbn));
        if (book.getCopiesAvailable() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.failure("No copies available for borrowing", HttpStatus.BAD_REQUEST));
        }
        book.setCopiesAvailable(book.getCopiesAvailable() - 1);
        bookRepository.save(book);
        User user = userRepository.findById(jwtService.extractUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        BookTransaction transaction = new BookTransaction();
        transaction.setBook(book);
        transaction.setUser(user);
        transaction.setTransactionType(BookTransaction.TransactionType.BORROW);
        transaction.setTransactionDate(new Date());
        bookTransactionRepository.save(transaction);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Book borrowed successfully", transaction, HttpStatus.OK));
    }

    public ResponseEntity<ApiResponse<BookTransaction>> returnBook(String isbn) {
        Long userId = jwtService.extractUserId();
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ISBN: " + isbn));
        Optional<BookTransaction> latestBorrowTransaction = bookTransactionRepository.findLatestBorrowTransactionByBookIdAndUserId(book.getId(), userId);
        if (latestBorrowTransaction.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.failure("You cannot return a book you have not borrowed.", HttpStatus.BAD_REQUEST));
        }
        BookTransaction borrowTransaction = latestBorrowTransaction.get();
        int returnCount = bookTransactionRepository.countReturnTransactionsAfterBorrowDate(book.getId(), userId, borrowTransaction.getTransactionDate());
        if (returnCount > 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.failure("You have already returned this book.", HttpStatus.BAD_REQUEST));
        }
        book.setCopiesAvailable(book.getCopiesAvailable() + 1);
        bookRepository.save(book);
        BookTransaction returnTransaction = new BookTransaction();
        returnTransaction.setBook(book);
        returnTransaction.setUser(borrowTransaction.getUser());
        returnTransaction.setTransactionType(BookTransaction.TransactionType.RETURN);
        returnTransaction.setTransactionDate(new Date());
        bookTransactionRepository.save(returnTransaction);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("Book returned successfully.", returnTransaction, HttpStatus.OK));
    }
}