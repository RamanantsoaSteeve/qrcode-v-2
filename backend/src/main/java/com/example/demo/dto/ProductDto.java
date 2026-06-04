package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProductDto {
    public record ProductInfoDto(
            @NotBlank(message = "price empty") @NotNull(message = "price null") Number price,
            @NotBlank(message = "name empty") @NotNull(message = "name not null") String name,
            @NotBlank(message = "currencySymbol empty") @NotNull(message = "currencySymbol not null") String currencySymbol,
            @NotBlank(message = "userId empty") @NotNull(message = "userId not null") String userId) {
    }

    public record QrcodeResponse(String qrcode, Long id) {
    }
}