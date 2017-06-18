package com.wakuang.hehe.websocket;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.wakuang.hehe.common.ConstantParam;
import com.wakuang.hehe.pingtai.PingTaiTradeService;
import com.wakuang.hehe.utils.WakuangStringUtils;

public class SocketSendMessage implements Runnable {
	
	private WebSocketSession user;
	
	private int count = 0;
	
	private Map<WebSocketSession, String> userType;
	
	private PingTaiTradeService service;
	
	private String rate;
	
	private String totalPrice;
	
	public SocketSendMessage(WebSocketSession user,
							Map<WebSocketSession, String> userType,
							PingTaiTradeService service,
							String rate,
							String totalPrice) {
		// TODO Auto-generated constructor stub
		this.user = user;
		this.userType = userType;
		this.service = service;
		this.rate = rate;
		this.totalPrice = totalPrice;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(StringUtils.isEmpty(rate)) {
			rate = "165";			
		}
		
		if(StringUtils.isEmpty(totalPrice)) {
			totalPrice = "10000000";
		}
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
				Map<String, Object> rs = service.compare(ConstantParam.PLAFORM_BIDUOBAO, ConstantParam.PLAFORM_BITHUM, rate, totalPrice);
				
				user.sendMessage(new TextMessage(WakuangStringUtils.beanToString(rs)));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
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
