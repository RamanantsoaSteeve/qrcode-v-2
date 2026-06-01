package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.AuthModel;

@Repository
public interface AuthRepository extends JpaRepository<AuthModel, Long> {
    boolean existsByEmail(String email);

    Optional<AuthModel> findByEmail(String email);
}
