package dev.jclp.demo.product_api.controller;

import dev.jclp.demo.product_api.model.Reply;
import dev.jclp.demo.product_api.model.dto.ProductDto;
import dev.jclp.demo.product_api.services.ProductCommandService;
import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
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
        Reply<?> reply = productCommandService.sendCreateAndAwait(productDto, Duration.ofSeconds(5));
        return getResponseEntity(reply);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Reply<?> reply = productCommandService.sendReadAndAwait(id, Duration.ofSeconds(5));
        return getResponseEntity(reply);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        Reply<?> reply = productCommandService.sendReadAllAndAwait(Duration.ofSeconds(5));
        return getResponseEntity(reply);
    }

    private static @NonNull ResponseEntity<?> getResponseEntity(Reply<?> reply) {
        if ("SUCCESS".equalsIgnoreCase(reply.status())) {
            return ResponseEntity.ok(reply.body());
        }

        return ResponseEntity.badRequest().body(Map.of("error", reply.message()));
    }
}
