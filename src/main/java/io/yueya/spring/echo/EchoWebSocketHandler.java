package io.yueya.spring.echo;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/*
    TextWebSocketHandler：
 */
public class EchoWebSocketHandler extends TextWebSocketHandler {

    private final EchoService echoService;

    public EchoWebSocketHandler(EchoService echoService) {
        this.echoService = echoService;
    }

    /**
     * 连接成功时回调函数
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("\n[EchoWebSocketHandler.afterConnectionEstablished()]在实例中打开新会话。");
    }

    /**
     * 接收到客户端消息的回调
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String echoMessage = this.echoService.getMessage(message.getPayload());
        System.out.println("\n[EchoWebSocketHandler.handleTextMessage()]" + echoMessage);
        // 给客户端发消息
        session.sendMessage(new TextMessage(echoMessage));
    }

    /**
     * 连接关闭时回调
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("\n[EchoWebSocketHandler.afterConnectionClosed()]会话已关闭。");
    }

    /**
     * 传输出现异常时回调
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        session.close(CloseStatus.SERVER_ERROR);
    }
}