package com.example.demo.service;

import java.util.Base64;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ProductDto;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.model.AuthModel;
import com.example.demo.model.ProductModel;
import com.example.demo.repository.AuthRepository;
import com.example.demo.repository.ProductRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final AuthRepository authRepository;
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    public Long saveProduct(ProductDto.ProductInfoDto dto) {
        AuthModel authModel = authRepository.findById(Long.parseLong(dto.userId())).orElseThrow(
                () -> new ResourceNotFoundException("Utilisateur introuvable avec l'ID : " + dto.userId()));

        List<ProductModel> list = authModel.getProductModels();
        if (list.stream().anyMatch(name -> name.getName().equals(dto.name()))) {
            throw new BadRequestException("product already exists");
        }

        list.add(productMapper.toEntity(dto));
        authRepository.save(authModel);

        return authModel.getProductModels().stream().filter(product -> product.getName().equals(dto.name())).count();
    }

    public String createContentQrcode(ProductDto.ContentQrCodeDto request) {
        return "Entreprise/User: " + request.name() +
                " | Produit: " + request.name() +
                " | Prix: " + request.price() + " " + request.currencySymbol();
    }

    public String generateQrCodeBlob(String texte) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(texte, BarcodeFormat.QR_CODE, 200, 200);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (Exception e) {
            throw new BadRequestException("Erreur technique lors de la génération du QR Code : " + e.getMessage());
        }
    }

    public byte[] generatePdfStrict(ProductDto.PrintProductDto request, String generateQrCodeBlob) {
        Document document = new Document();
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, pdfOutputStream);
            document.open();
            byte[] imageBytes = Base64.getDecoder().decode(generateQrCodeBlob);
            Image imgQr = Image.getInstance(imageBytes);
            imgQr.setAlignment(Element.ALIGN_CENTER);
            int maxPages = request.pages();
            int nbLignes = request.rows();
            int nbColonnes = request.column();
            for (int p = 0; p < maxPages; p++) {
                PdfPTable table = new PdfPTable(nbColonnes);
                table.setWidthPercentage(100);

                for (int l = 0; l < nbLignes; l++) {
                    for (int c = 0; c < nbColonnes; c++) {
                        PdfPCell cellule = new PdfPCell();

                        cellule.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cellule.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cellule.setPadding(5);
                        cellule.addElement(imgQr);
                        Paragraph legende = new Paragraph(
                                request.contentQrCodeDto().name() + " (" + request.contentQrCodeDto().price() + " "
                                        + request.contentQrCodeDto().currencySymbol() + ")");

                        legende.setAlignment(Element.ALIGN_CENTER);
                        cellule.addElement(legende);
                        table.addCell(cellule);
                    }
                }

                document.add(table);
                if (p < maxPages - 1) {
                    document.newPage();
                }
            }

            document.close();

            return pdfOutputStream.toByteArray();
        } catch (Exception e) {
            throw new BadRequestException("Erreur technique lors de la génération du PDF : " + e.getMessage());
        }
    }

    public List<ProductDto.ProductResponse> getProductModels(Long id) {
        AuthModel authModel = authRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Compte utilisateur introuvable pour l'ID : " + id));

        return authModel.getProductModels().stream()
                .map(product -> ProductDto.ProductResponse.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .price(product.getPrice())
                        .user_id(id)
                        .createdat(product.getCreatedAt())
                        .currency(product.getCurrency()).build())
                .toList();
    }

    public Long getIdProductByName(String name) {
        return productRepository.findIdByName(name).orElseThrow(
                () -> new ResourceNotFoundException("Aucun produit trouvé avec le nom : " + name));
    }

    public void deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new BadRequestException("Product not exists");
        }

        productRepository.deleteById(id);
    }
}