package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ProductDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    @PostMapping("path")
    public ResponseEntity<ProductDto.QrcodeResponse> postMethodName(
            @RequestBody @Valid ProductDto.ProductInfoDto entity) {

        return ResponseEntity.ok(null);
    }

}
