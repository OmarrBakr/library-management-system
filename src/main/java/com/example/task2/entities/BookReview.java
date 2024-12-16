package com.example.task2.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "book_reviews")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookReview {

    @Id
    private String id;

    @Field(name = "book_id")
    private Long bookId;

    @Field(name = "user_id")
    private Long userId;

    private int rating;
    private String reviewText;
    private Date reviewDate;

    @Transient
    @JsonIgnore
    private Book book;

    @Transient
    @JsonIgnore
    private User user;

    public void setBook(Book book) {
        this.book = book;
        if (book != null) {
            this.bookId = book.getId();
        }
    }

    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            this.userId = user.getId();
        }
    }

}
