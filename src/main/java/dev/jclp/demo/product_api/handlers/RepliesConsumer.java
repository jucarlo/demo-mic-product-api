package dev.jclp.demo.product_api.handlers;

import dev.jclp.demo.product_api.messaging.ReplyInbox;
import dev.jclp.demo.product_api.model.Reply;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Configuration
public class RepliesConsumer {

    private final ReplyInbox replyInbox;

    public RepliesConsumer(ReplyInbox replyInbox) {
        this.replyInbox = replyInbox;
    }

    @Bean
    public Consumer<Message<Reply<?>>> handleReplies() {
        return msg -> {
            Reply<?> reply = msg.getPayload();
            String correlationId = msg.getHeaders().get("correlationId", String.class);
            replyInbox.complete(correlationId, reply);
        };
    }

}
