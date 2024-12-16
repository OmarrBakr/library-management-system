package com.example.task2.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "authorities")
@AllArgsConstructor
@NoArgsConstructor
public class Authority implements GrantedAuthority{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "authority",fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<User> users;

    @Override
    public String getAuthority() {
        return name;
    }
}
