package com.example.demo.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

public class ProductDto {

        @Builder
        public record QrcodeResponse(String qrcode, Long id) {
        }

        @Builder
        public record ContentQrCodeDto(String name, Number price, String currencySymbol) {
        }

        @Builder
        public record ProductResponse(String name, Number price, Long id, LocalDateTime createdat, Long user_id,
                        String currency) {
        }

        @Builder
        public record PrintProductDto(ContentQrCodeDto contentQrCodeDto, String username, Integer column, Integer rows,
                        Integer pages) {
        }

        public record ProductInfoDto(
                        @NotNull(message = "price null") @PositiveOrZero(message = "must great than 0") Number price,
                        @NotBlank(message = "name empty") @NotNull(message = "name not null") String name,
                        @NotBlank(message = "currencySymbol empty") @NotNull(message = "currencySymbol not null") String currencySymbol,
                        @NotBlank(message = "userId empty") @NotNull(message = "userId not null") String userId) {
        }

        public record GenerateQrcodeRequest(ContentQrCodeDto contentQrCodeDto, String username) {
        }

        public record RequestDelete(Long idProduct, Long idUser) {
        }

        public record RequestId(Long id) {
        }
}