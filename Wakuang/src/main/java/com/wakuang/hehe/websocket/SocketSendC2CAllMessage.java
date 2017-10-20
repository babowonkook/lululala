package com.wakuang.hehe.websocket;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wakuang.hehe.common.ConstantParam;
import com.wakuang.hehe.pingtai.PingTaiTradeService;
import com.wakuang.hehe.utils.WakuangStringUtils;

public class SocketSendC2CAllMessage implements Runnable {
	
	private WebSocketSession user;
	
	private int count = 0;
	
	private Map<WebSocketSession, String> userType;
	
	private PingTaiTradeService service;
	
    private String                        rateCNY;
    private String                        targetCoin;
	
	private String totalPrice;
	
	private String tufaQingkuang;
	
    private Logger                        log   = LoggerFactory.getLogger(SocketSendC2CAllMessage.class);

	public SocketSendC2CAllMessage(WebSocketSession user,
							Map<WebSocketSession, String> userType,
							PingTaiTradeService service,
							JsonNode param) {
		String rateCNY = WakuangStringUtils.objectToString(param.get("rateCNY")).replaceAll("\"", "");
		String targetCoin = WakuangStringUtils.objectToString(param.get("targetCoin")).replaceAll("\"", "");
		String rateUSD = WakuangStringUtils.objectToString(param.get("rateUSD")).replaceAll("\"", "");
		String totalPrice = param.get("totalPrice").asText();
		String tufaQingkuang = param.get("tufaQingkuang").asText();
		this.user = user;
		this.userType = userType;
		this.service = service;
        this.rateCNY = rateCNY;
        this.targetCoin = targetCoin;
		this.totalPrice = totalPrice;
		this.tufaQingkuang = tufaQingkuang;
		
		
	}
	
	@Override
	public void run() {
        if (StringUtils.isEmpty(rateCNY)) {
            rateCNY = "168.5";
        }

        if (StringUtils.isEmpty(targetCoin)) {
        	targetCoin = "BTC";
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
				
                List<Map<String, Object>> comparList = service.compareC2CAll(rateCNY, totalPrice, targetCoin);

                Date endDate = new Date();
                String endTime = dateFormat.format(endDate);

                log.info("end Time: {}", endTime);

				ObjectMapper mapper = new ObjectMapper();
				ObjectNode jsonRoot = mapper.createObjectNode();
				
                jsonRoot.put(ConstantParam.RESPONSE_STATUS, "SUCCESS");
                jsonRoot.put(ConstantParam.RESPONSE_SYSTEM_TIME, startTime);
                jsonRoot.put(ConstantParam.RESPONSE_COMPAIRE_DATA, WakuangStringUtils.beanToString(comparList));
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
