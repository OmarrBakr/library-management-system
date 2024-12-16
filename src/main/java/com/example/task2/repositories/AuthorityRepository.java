package com.example.task2.repositories;

import com.example.task2.entities.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority,Long> {

    @Query("SELECT a FROM Authority a WHERE a.name IN :names")
    List<Authority> findByNameIn(List<String> names);
}
