package dev.jclp.demo.product_api.services;

import dev.jclp.demo.product_api.messaging.ReplyInbox;
import dev.jclp.demo.product_api.model.Reply;
import org.springframework.cloud.stream.function.StreamBridge;


import dev.jclp.demo.product_api.model.Command;
import dev.jclp.demo.product_api.model.dto.ProductDto;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Service
public class ProductCommandServiceImpl implements ProductCommandService {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ProductCommandServiceImpl.class);
    private final StreamBridge streamBridge;
    private final ReplyInbox replyInbox;

    public ProductCommandServiceImpl(StreamBridge streamBridge, ReplyInbox replyInbox) {
        this.streamBridge = streamBridge;
        this.replyInbox = replyInbox;
    }

    @Override
    public Reply<?> sendCreateAndAwait(ProductDto productDto, Duration timeout) {
        Command<ProductDto> command = new Command<>("CREATE", null, productDto);
        return sendAndAwait(command, timeout);
    }

    @Override
    public Reply<?> sendReadAndAwait(Long id, Duration timeout) {
        Command<ProductDto> command = new Command<>("READ", id, null);
        return sendAndAwait(command, timeout);
    }

    @Override
    public Reply<?> sendReadAllAndAwait(Duration timeout) {
        Command<ProductDto> command = new Command<>("READ_ALL", null, null);
        return sendAndAwait(command, timeout);
    }

    private Reply<?> sendAndAwait(Command<ProductDto> command, Duration timeout) {
        String correlationId = java.util.UUID.randomUUID().toString();
        LOGGER.info("Sending command with correlationId: {}", correlationId);
        CompletableFuture<Reply<?>> futureReply = replyInbox.register(correlationId);

        Message<Command<ProductDto>> message = MessageBuilder.withPayload(command)
                .setHeader("correlationId", correlationId)
                .build();

        boolean sent = streamBridge.send("commands-out-0", message);
        if (!sent) {
            throw new IllegalStateException("Failed sending kafka command");
        }

        try {
            return futureReply.get(timeout.toMillis(), java.util.concurrent.TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            LOGGER.error("Failed to receive reply", e);
            throw new RuntimeException("Failed to receive reply", e);
        }
    }
}
