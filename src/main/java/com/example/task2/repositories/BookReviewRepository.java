package com.example.task2.repositories;

import com.example.task2.entities.BookReview;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

@Repository
public interface BookReviewRepository extends MongoRepository<BookReview, String> {

    @Query("{ 'book_id': ?0 }")
    List<BookReview> findByBookId(String bookId);

    @Query("{ 'book_id': ?0, 'user_id': ?1 }")
    List<BookReview> findByBookIdAndUserId(String bookId, String userId);
}
