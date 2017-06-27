package com.wakuang.hehe.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.wakuang.hehe.pingtai.PingTaiTradeService;

@Configuration  
@EnableWebMvc  
@EnableWebSocket  
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {
	
	@Autowired
	private PingTaiTradeService service;
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry arg0) {
		arg0.addHandler(systemWebSocketHandler(),"/webSocketServer");  
		arg0.addHandler(systemWebSocketHandler(),"/webSocketServer/sockjs").setAllowedOrigins("*").withSockJS(); 
	}
	
	@Bean  
    public WebSocketHandler systemWebSocketHandler(){  
        return new SystemWebSocketHandler(service);  
    }
}
