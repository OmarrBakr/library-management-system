package com.example.task2.repositories;

import com.example.task2.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book,Long>{

    @Query("SELECT b FROM Book b " +
            "WHERE (:title IS NULL OR b.title LIKE %:title%) " +
            "AND (:author IS NULL OR b.author LIKE %:author%) " +
            "AND (:genreId IS NULL OR b.genre.id = :genreId) " +
            "AND (:publisherId IS NULL OR b.publisher.id = :publisherId)")
    List<Book> searchBooks(@Param("title") String title,
                           @Param("author") String author,
                           @Param("genreId") Long genreId,
                           @Param("publisherId") Long publisherId);


    @Query("SELECT b FROM Book b WHERE b.isbn = :isbn")
    Optional<Book> findByIsbn(@Param("isbn") String isbn);

}
