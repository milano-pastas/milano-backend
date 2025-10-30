package com.milano.milanopastas.controller;

import com.milano.milanopastas.dto.ProductDTO;
import com.milano.milanopastas.mapper.ProductMapper;
import com.milano.milanopastas.model.Product;
import com.milano.milanopastas.repository.ProductRepository;
import com.milano.milanopastas.service.SupabaseStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    @Autowired
    private SupabaseStorageService supabaseStorageService;

    //publico
    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productRepository.findByActiveTrue().stream()
                .map(productMapper::toDTO)
                .toList();
    }

    //publico
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //admin
    @PostMapping
    public ResponseEntity<?> createProduct(@Validated @RequestBody ProductDTO dto) {
        Product product = productMapper.toEntity(dto);
        Product saved = productRepository.save(product);
        return ResponseEntity.ok(productMapper.toDTO(saved));
    }

    @PostMapping(value = "/{id}/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(
            @PathVariable Long id,
            @RequestParam("image") MultipartFile image) {

        System.out.println("🟢 Archivo recibido: " + image.getOriginalFilename());

        return productRepository.findById(id)
                .map(product -> {
                    try {
                        String fileName = "product_" + id + "_" + image.getOriginalFilename();
                        String imageUrl = supabaseStorageService.uploadImage(image, fileName);
                        product.setImageUrl(imageUrl);
                        productRepository.save(product);
                        return ResponseEntity.ok(Map.of(
                                "message", "Imagen subida correctamente",
                                "imageUrl", imageUrl
                        ));
                    } catch (Exception e) {
                        e.printStackTrace();
                        return ResponseEntity.internalServerError().body(
                                Map.of("error", "Error al subir imagen: " + e.getMessage())
                        );
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }



    //admin
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO dto) {
        return productRepository.findById(id)
                .map(existing -> {
                    existing.setName(dto.getName());
                    existing.setDescription(dto.getDescription());
                    existing.setPrice(dto.getPrice());
                    existing.setImageUrl(dto.getImageUrl());
                    existing.setUnit(dto.getUnit());
                    Product updated = productRepository.save(existing);
                    return ResponseEntity.ok(productMapper.toDTO(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    //admin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (!productRepository.existsById(id)) return ResponseEntity.notFound().build();
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
