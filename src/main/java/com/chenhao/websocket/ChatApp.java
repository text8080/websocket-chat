package com.chenhao.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableWebSocket
@MapperScan({"com.chenhao.websocket.user.mapper","com.chenhao.websocket.emoji.mapper"})
public class ChatApp {
    public static void main(String[] args) {
        SpringApplication.run(ChatApp.class,args);
    }
}
