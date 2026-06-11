package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ProductDto;
import com.example.demo.dto.ProductDto.QrcodeResponse;
import com.example.demo.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductController {
    private final ProductService productService;

    @PostMapping("/get-product")
    public ResponseEntity<List<ProductDto.ProductResponse>> getProduct(@RequestBody Long id) {
        return ResponseEntity.ok(productService.getProductModels(id));
    }

    @PostMapping("/create-product")
    public ResponseEntity<ProductDto.QrcodeResponse> postMethodName(@RequestBody @Valid ProductDto.ProductInfoDto dto)
            throws Exception {
        ProductDto.ContentQrCodeDto cQrCodeDto = ProductDto.ContentQrCodeDto.builder()
                .price(dto.price())
                .name(dto.name())
                .currencySymbol(dto.currencySymbol())
                .build();

        return ResponseEntity.ok(ProductDto.QrcodeResponse.builder()
                .id(productService.saveProduct(dto))
                .qrcode(productService.generateQrCodeBlob(productService.createContentQrcode(cQrCodeDto)))
                .build());
    }

    @PostMapping("/print")
    public ResponseEntity<byte[]> printProduct(@RequestBody @Valid ProductDto.PrintProductDto dto) {
        try {
            String qrCodeText = productService.createContentQrcode(dto.contentQrCodeDto());
            String qrCodeBase64 = productService.generateQrCodeBlob(qrCodeText);
            byte[] pdfBlob = productService.generatePdfStrict(dto, qrCodeBase64);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "produit_qrcode.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBlob);
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/generate-qrcode")
    public ResponseEntity<QrcodeResponse> postMethodName(@RequestBody @Valid ProductDto.GenerateQrcodeRequest dto)
            throws Exception {
        return ResponseEntity.ok(ProductDto.QrcodeResponse.builder()
                .id(productService.getIdProductByName(dto.contentQrCodeDto().name()))
                .qrcode(productService.generateQrCodeBlob(productService.createContentQrcode(dto.contentQrCodeDto())))
                .build());
    }
}
