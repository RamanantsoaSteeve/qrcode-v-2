package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.AuthModel;

public interface AuthRepository extends JpaRepository<AuthModel, Long> {
    boolean existsByEmail(String email);

    @Query("SELECT p.id FROM AuthModel p WHERE p.email = :email")
    Optional<Long> getLongByEmail(String email);

    Optional<AuthModel> findByEmail(String email);
}
