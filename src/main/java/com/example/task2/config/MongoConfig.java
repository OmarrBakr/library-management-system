package com.example.task2.config;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class MongoConfig {

    private final MongoTemplate mongoTemplate;

    @PostConstruct
    public void initIndexes() {
        mongoTemplate.indexOps("book_reviews")
                .ensureIndex(new Index().on("bookId", org.springframework.data.domain.Sort.Direction.ASC));
        mongoTemplate.indexOps("book_reviews")
                .ensureIndex(new Index().on("userId", org.springframework.data.domain.Sort.Direction.ASC));
    }
}
