package io.yueya.spring.client;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class SimpleClientWebSocketHandler extends TextWebSocketHandler {

    private final GreetingService greetingService;

    private final CountDownLatch latch;

    private final AtomicReference<String> messagePayload;

    public SimpleClientWebSocketHandler(GreetingService greetingService,
                                        CountDownLatch latch,
                                        AtomicReference<String> message) {
        this.greetingService = greetingService;
        this.latch = latch;
        this.messagePayload = message;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        TextMessage message = new TextMessage(this.greetingService.getGreeting());
        session.sendMessage(message);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("\n[SimpleClientWebSocketHandler.handleTextMessage()]Received: " + message + " (" + this.latch.getCount() + ")");
        session.close();
        this.messagePayload.set(message.getPayload());
        this.latch.countDown();
    }

}
