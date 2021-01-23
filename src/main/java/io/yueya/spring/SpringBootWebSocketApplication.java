package io.yueya.spring;

import io.yueya.spring.client.GreetingService;
import io.yueya.spring.client.SimpleGreetingService;
import io.yueya.spring.echo.DefaultEchoService;
import io.yueya.spring.echo.EchoService;
import io.yueya.spring.echo.EchoWebSocketHandler;
import io.yueya.spring.reverse.ReverseWebSocketEndpoint;
import io.yueya.spring.snake.SnakeWebSocketHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.PerConnectionWebSocketHandler;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@EnableAutoConfiguration
@EnableWebSocket
public class SpringBootWebSocketApplication extends SpringBootServletInitializer implements WebSocketConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebSocketApplication.class, args);
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        /*
            withSockJS：如果浏览器不支持websocket，使用SockJS模拟websocket的连接。
        WebSocket是一个相对比较新的规范，在Web浏览器和应用服务器上没有得到一致的支
        持。所以我们需要一种WebSocket的备选方案。

            什么是SockJS：SockJS是WebSocket的一种模拟，它尽可能对应对应WebSocket的API，
        但是底层非常智能，如果不支持WebSocket技术的话，就会选择其他通信方式。

            使用SockJS后URL的变化：SockJS所处理的URL是http/https，不再是ws/wss
         */
        registry.addHandler(echoWebSocketHandler(), "/echo").withSockJS();
        registry.addHandler(snakeWebSocketHandler(), "/snake").withSockJS();
    }

    @Bean
    public WebSocketHandler echoWebSocketHandler() {
        return new EchoWebSocketHandler(echoService());
    }

    @Bean
    public EchoService echoService() {
        return new DefaultEchoService("Did you say \"%s\"?");
    }

    @Bean
    public WebSocketHandler snakeWebSocketHandler() {
        return new PerConnectionWebSocketHandler(SnakeWebSocketHandler.class);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpringBootWebSocketApplication.class);
    }

    @Bean
    public GreetingService greetingService() {
        return new SimpleGreetingService();
    }

    @Bean
    public ReverseWebSocketEndpoint reverseWebSocketEndpoint() {
        return new ReverseWebSocketEndpoint();
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}