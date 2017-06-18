package com.wakuang.hehe.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.wakuang.hehe.common.ConstantParam;
import com.wakuang.hehe.pingtai.PingTaiTradeService;

public class SystemWebSocketHandler implements WebSocketHandler {
	
	private Logger log = LoggerFactory.getLogger(SystemWebSocketHandler.class);  
    
    private static final ArrayList<WebSocketSession> users = new ArrayList<WebSocketSession>();;
    
    private Map<WebSocketSession, String> userType = new ConcurrentHashMap<>();
    
    private ExecutorService ExecutorService;
    
    private PingTaiTradeService service;
    
    public SystemWebSocketHandler(PingTaiTradeService service) {
		// TODO Auto-generated constructor stub
    	this.service = service;
    }

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus arg1) throws Exception {
		// TODO Auto-generated method stub
		users.remove(session);  
        log.debug("afterConnectionClosed" + arg1.getReason()); 
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("ConnectionEstablished");  
        log.debug("ConnectionEstablished");  
        users.add(session);  
          
        session.sendMessage(new TextMessage("connect"));  
        session.sendMessage(new TextMessage("new_msg")); 
	}

	@Override
	public void handleMessage(WebSocketSession arg0, WebSocketMessage<?> arg1) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("handleMessage" + arg1.toString());  
        log.debug("handleMessage" + arg1.toString());
        if("1".equals(arg1.getPayload().toString()) && userType.get(arg0) == null || ConstantParam.N.equals(userType.get(arg0)) ) {
        	ExecutorService = Executors.newFixedThreadPool(1);
        	userType.put(arg0, ConstantParam.Y);
        	ExecutorService.execute(new SocketSendMessage(arg0, userType, service));
        	ExecutorService.shutdown();
        }else if("2".equals(arg1.getPayload().toString())) {
        	userType.put(arg0, ConstantParam.N);
        }
	}

	@Override
	public void handleTransportError(WebSocketSession arg0, Throwable arg1) throws Exception {
		// TODO Auto-generated method stub
		if (arg0.isOpen()) {
			arg0.close();
		}
		users.remove(arg0);

		log.debug("handleTransportError" + arg1.getMessage());
	}

	@Override
	public boolean supportsPartialMessages() {
		// TODO Auto-generated method stub
		return false;
	}
	
	 public void sendMessageToUsers(TextMessage message) {  
        for (WebSocketSession user : users) {  
            try {  
                if (user.isOpen()) {  
                    user.sendMessage(message);  
                }  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  

}
