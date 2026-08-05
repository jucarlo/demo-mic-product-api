package dev.jclp.demo.product_api.model;

public record Reply<T>(String status, String message, T body) {
}
