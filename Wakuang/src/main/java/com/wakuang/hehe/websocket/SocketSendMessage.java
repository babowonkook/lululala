package com.wakuang.hehe.websocket;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.wakuang.hehe.common.ConstantParam;

public class SocketSendMessage implements Runnable {
	
	private WebSocketSession user;
	
	private int count = 0;
	
	private Map<WebSocketSession, String> userType;
	
	public SocketSendMessage(WebSocketSession user,
							Map<WebSocketSession, String> userType) {
		// TODO Auto-generated constructor stub
		this.user = user;
		this.userType = userType;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			if(ConstantParam.N.equals(userType.get(user))) {
				break;
			}
			if(count >= 3600) {
				break;
			}
			count++;
			try {
				if(user == null || !user.isOpen()) {
					break;
				}
				user.sendMessage(new TextMessage(count+""));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
