package com.wakuang.hehe.websocket;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wakuang.hehe.common.ConstantParam;
import com.wakuang.hehe.pingtai.PingTaiTradeService;
import com.wakuang.hehe.utils.WakuangStringUtils;

public class SocketSendXiwangMessage implements Runnable {
	
	private WebSocketSession user;
	
	private int count = 0;
	
	private Map<WebSocketSession, String> userType;
	
	private PingTaiTradeService service;
	
    private String                        rateCNY;
    private String                        rateJPY;
    private String                        rateUSD;
	
	private String totalPrice;
	
	private String tufaQingkuang;
	
    private Logger                        log   = LoggerFactory.getLogger(SocketSendXiwangMessage.class);

	public SocketSendXiwangMessage(WebSocketSession user,
							Map<WebSocketSession, String> userType,
							PingTaiTradeService service,
            String rateCNY, String rateJPY, String rateUSD,
							String totalPrice,
            String tufaQingkuang) {
		this.user = user;
		this.userType = userType;
		this.service = service;
        this.rateCNY = rateCNY;
        this.rateJPY = rateJPY;
        this.rateUSD = rateUSD;
		this.totalPrice = totalPrice;
		this.tufaQingkuang = tufaQingkuang;
	}
	
	@Override
	public void run() {
        if (StringUtils.isEmpty(rateCNY)) {
            rateCNY = "168.5";
        }

        if (StringUtils.isEmpty(rateJPY)) {
            rateJPY = "10.31437174";
        }

        if (StringUtils.isEmpty(rateUSD)) {
            rateUSD = "1136.00";
		}
		
		if(StringUtils.isEmpty(totalPrice)) {
			totalPrice = "10000000";
		}
        while (true) {
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
				
                Date startDate = new Date();
				DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);
                String startTime = dateFormat.format(startDate);
                log.info("start Time: {}", startTime);
				
                List<Map<String, Object>> priceList = service.getForeinPrice(rateCNY, rateJPY, rateUSD, totalPrice, tufaQingkuang);

                
                Date endDate = new Date();
                String endTime = dateFormat.format(endDate);
                try{
                	System.out.println("I'm going to bed");
                	Thread.sleep(5000);
                	System.out.println("I wake up");
                	}
                	catch(InterruptedException e) {
                	}
                log.info("end Time: {}", endTime);

				ObjectMapper mapper = new ObjectMapper();
				ObjectNode jsonRoot = mapper.createObjectNode();
				
                jsonRoot.put(ConstantParam.RESPONSE_STATUS, "SUCCESS");
                jsonRoot.put(ConstantParam.RESPONSE_SYSTEM_TIME, startTime);
                jsonRoot.put(ConstantParam.RESPONSE_COMPAIRE_DATA, WakuangStringUtils.beanToString(priceList));
                if (log.isInfoEnabled()) {
                    log.info(jsonRoot.toString());
                }

				user.sendMessage(new TextMessage(jsonRoot.toString()));
			} catch (IOException e) {
                log.error("", e);
			} catch (Exception e) {
                log.error("", e);
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
                log.error("", e);
			}
		}
	}

}
