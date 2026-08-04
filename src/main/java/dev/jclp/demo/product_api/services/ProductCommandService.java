package dev.jclp.demo.product_api.services;

import dev.jclp.demo.product_api.model.Reply;
import dev.jclp.demo.product_api.model.dto.ProductDto;

import java.time.Duration;

public interface ProductCommandService {
    Reply<?> sendCreateAndAwait(ProductDto productDto, Duration timeout);

    Reply<?> sendReadAndAwait(Long id, Duration timeout);
}
