package dev.jclp.demo.product_api.services;

import dev.jclp.demo.product_api.model.dto.ProductDto;

public interface ProductCommandService {
    void sendCreate(ProductDto productDto);
}
