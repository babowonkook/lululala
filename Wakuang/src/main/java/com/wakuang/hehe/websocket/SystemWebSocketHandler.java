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

import com.fasterxml.jackson.databind.JsonNode;
import com.wakuang.hehe.common.ConstantParam;
import com.wakuang.hehe.pingtai.PingTaiTradeService;
import com.wakuang.hehe.utils.WakuangStringUtils;

public class SystemWebSocketHandler implements WebSocketHandler {
	
	private Logger log = LoggerFactory.getLogger(SystemWebSocketHandler.class);  
    
    private static final ArrayList<WebSocketSession> users    = new ArrayList<>();;
    
    private Map<WebSocketSession, String> userType = new ConcurrentHashMap<>();
    
    private ExecutorService excutorService;
    
    private PingTaiTradeService service;
    
    public SystemWebSocketHandler(PingTaiTradeService service) {
    	this.service = service;
    }

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus arg1) throws Exception {
		users.remove(session);  
        log.info("afterConnectionClosed" + arg1.getReason());
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("ConnectionEstablished");
        users.add(session);  
 
	}

	@Override
	public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {

        if (log.isInfoEnabled()) {
            log.info("remote ip : " + webSocketSession.getRemoteAddress());;
            log.info("handleMessage" + webSocketMessage.toString());
        }
        JsonNode jsonNode = WakuangStringUtils.stringToJsonNode(webSocketMessage.getPayload().toString());
        if(jsonNode == null) {
        	return;
        }
        String type = jsonNode.get("type").asText();
        String rate = jsonNode.get("rate").asText();
        String rateCNY = jsonNode.get("rateCNY").asText();
        String rateJPY = jsonNode.get("rateJPY").asText();
        String rateUSD = jsonNode.get("rateUSD").asText();
        String totalPrice = jsonNode.get("totalPrice").asText();
        String tufaQingkuang = jsonNode.get("tufaQingkuang").asText();
        String buyPlatform = jsonNode.get("buyPlatform") != null ? jsonNode.get("buyPlatform").asText() : ConstantParam.PLAFORM_BIDUOBAO;
        String sellPlatform = jsonNode.get("sellPlatform") != null ? jsonNode.get("sellPlatform").asText() : ConstantParam.PLAFORM_BITHUM;
        excutorService = Executors.newFixedThreadPool(50);
        if("1".equals(type) && userType.get(webSocketSession) == null || ConstantParam.N.equals(userType.get(webSocketSession)) ) {
        	userType.put(webSocketSession, ConstantParam.Y);
        	excutorService.execute(new SocketSendMessage(webSocketSession, userType, service, rate, totalPrice, tufaQingkuang, buyPlatform, sellPlatform));
        	excutorService.shutdown();
        } else if ("3".equals(type) && userType.get(webSocketSession) == null || ConstantParam.N.equals(userType.get(webSocketSession))) {
            userType.put(webSocketSession, ConstantParam.Y);
            excutorService.execute(new SocketSendAllMessage(webSocketSession, userType, service, rateCNY, rateJPY, rateUSD, totalPrice, tufaQingkuang, buyPlatform, sellPlatform));
            excutorService.shutdown();
        }else if("2".equals(type)) {
        	userType.put(webSocketSession, ConstantParam.N);
        }
	}

	@Override
	public void handleTransportError(WebSocketSession arg0, Throwable arg1) throws Exception {
		if (arg0.isOpen()) {
			arg0.close();
		}
		users.remove(arg0);
        log.error("handleTransportError" + arg1.getMessage());
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}
	
	 public void sendMessageToUsers(TextMessage message) {  
        for (WebSocketSession user : users) {  
            try {  
                if (user.isOpen()) {  
                    user.sendMessage(message);  
                }  
            } catch (IOException e) {  
                log.error("", e);
            }  
        }  
    }  

}
