package dev.jclp.demo.product_api.services;

import org.springframework.cloud.stream.function.StreamBridge;


import dev.jclp.demo.product_api.model.Command;
import dev.jclp.demo.product_api.model.dto.ProductDto;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class ProductCommandServiceImpl implements ProductCommandService {

    private final StreamBridge streamBridge;

    public ProductCommandServiceImpl(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Override
    public void sendCreate(ProductDto productDto) {
        Command<ProductDto> command = new Command<>("CREATE", null, productDto);

        String correlationId = java.util.UUID.randomUUID().toString();

        Message<Command<ProductDto>> message = MessageBuilder.withPayload(command)
                .setHeader("correlationId", correlationId)
                .build();

        boolean sent = streamBridge.send("commands-out-0", message);
        if (!sent) {
            throw new IllegalStateException("Failed sending kafka command");
        }
    }
}
