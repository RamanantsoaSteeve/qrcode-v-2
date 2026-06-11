package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.ProductModel;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, Long> {
    @Query("SELECT p.id FROM ProductModel p WHERE p.name = :name")
    Optional<Long> findIdByName(@Param("name") String name);
}
