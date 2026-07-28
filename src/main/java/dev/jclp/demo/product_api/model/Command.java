package dev.jclp.demo.product_api.model;

public record Command<T>(String type, Long id, T body) {
}
