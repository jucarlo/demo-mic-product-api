package dev.jclp.demo.product_api.messaging;

import dev.jclp.demo.product_api.model.Reply;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ReplyInbox {

    private final ConcurrentHashMap<String, CompletableFuture<Reply<?>>> inbox = new ConcurrentHashMap<>();


    public CompletableFuture<Reply<?>> register(String correlationId) {
        CompletableFuture<Reply<?>> future = new CompletableFuture<>();
        inbox.put(correlationId, future);
        return future;
    }

    public void complete(String correlationId, Reply<?> reply) {

        if(correlationId == null) {
            throw new IllegalArgumentException("Correlation ID cannot be null");
        }

        CompletableFuture<Reply<?>> future = inbox.remove(correlationId);
        if (future != null) {
            future.complete(reply);
        }
    }
}
