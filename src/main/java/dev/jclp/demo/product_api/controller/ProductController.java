package dev.jclp.demo.product_api.controller;

import dev.jclp.demo.product_api.model.dto.ProductDto;
import dev.jclp.demo.product_api.services.ProductCommandService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductCommandService productCommandService;

    public ProductController(ProductCommandService productCommandService) {
        this.productCommandService = productCommandService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ProductDto productDto) {
        productCommandService.sendCreate(productDto);
        return ResponseEntity.ok().body(Map.of("message","Success sent"));
    }
}
