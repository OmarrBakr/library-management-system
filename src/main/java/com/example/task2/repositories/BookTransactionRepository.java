package com.example.task2.repositories;

import com.example.task2.entities.Book;
import com.example.task2.entities.BookTransaction;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface BookTransactionRepository extends JpaRepository<BookTransaction, Long> {

    @Query(value = "SELECT bt FROM BookTransaction bt " +
            "WHERE bt.book.id = :bookId " +
            "AND bt.user.id = :userId " +
            "AND bt.transactionType = 'BORROW' " +
            "ORDER BY bt.transactionDate DESC " +
            "LIMIT 1")
    Optional<BookTransaction> findLatestBorrowTransactionByBookIdAndUserId(@Param("bookId") Long bookId, @Param("userId") Long userId);

    @Query("SELECT COUNT(bt) FROM BookTransaction bt WHERE bt.book.id = :bookId AND bt.user.id = :userId AND bt.transactionType = 'RETURN' AND bt.transactionDate > :borrowDate")
    int countReturnTransactionsAfterBorrowDate(@Param("bookId") Long bookId, @Param("userId") Long userId, @Param("borrowDate") Date borrowDate);

}
