package com.gzseeing.sys.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebSocketConfig {
    @Bean
    //如果是tomcat，就不需要这个。
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}