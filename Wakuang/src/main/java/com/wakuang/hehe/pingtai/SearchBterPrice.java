package com.wakuang.hehe.pingtai;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.wakuang.hehe.common.ConstantParam;
import com.wakuang.hehe.hanguo.util.SslTest;
import com.wakuang.hehe.utils.WakuangStringUtils;

@Service("bterService")
public class SearchBterPrice implements SearchPingtaiPrice {
    public Map<String, Map<String, BigDecimal>> getPrice() throws Exception {
        
		String coinTypes[] = {ConstantParam.COINTYPE_BTC, ConstantParam.COINTYPE_ETH, ConstantParam.COINTYPE_LTC, ConstantParam.COINTYPE_DASH, ConstantParam.COINTYPE_ETC, ConstantParam.COINTYPE_XRP};
		String result;
		Map<String, Map<String, BigDecimal>> coins = new HashMap<>();
		for(String coin : coinTypes) {
			
			String url = "http://data.bter.com/api2/1/ticker/";
			url = url + coin.toLowerCase() + "_cny";
			result = SslTest.getRequest(url, 3000);
			JsonNode rootNode = WakuangStringUtils.stringToJsonNode(result);
			if(rootNode != null) {
				if(rootNode.get("result") == null || "false".equals(rootNode.get("result"))){
					continue;
				}
				Map<String, BigDecimal> coinInfo = new HashMap<String, BigDecimal>();
				coinInfo.put(ConstantParam.COIN_INFO_MAX, new BigDecimal(rootNode.get("lowestAsk").asText()));
				coinInfo.put(ConstantParam.COIN_INFO_MIN, new BigDecimal(rootNode.get("highestBid").asText()));
				coinInfo.put(ConstantParam.COIN_INFO_BUY, new BigDecimal(rootNode.get("highestBid").asText()));
				coinInfo.put(ConstantParam.COIN_INFO_SELL, new BigDecimal(rootNode.get("lowestAsk").asText()));
				coinInfo.put(ConstantParam.COIN_INFO_PRICE, new BigDecimal(rootNode.get("last").asText()));
				coins.put(coin, coinInfo);
			}
		}
        
        
        
 
        System.out.println(ConstantParam.PLAFORM_BETR + ":  " + coins.toString());
        return coins;

    }
    @Override
    public BigDecimal getTakerFee(BigDecimal amt,
                                   String coinType) {
        BigDecimal feeRate = null;
        switch (coinType) {
            case ConstantParam.COINTYPE_BTC:
            case ConstantParam.COINTYPE_DASH:
                feeRate = new BigDecimal("0.002");
                break;
            case ConstantParam.COINTYPE_LTC:
            case ConstantParam.COINTYPE_ETH:
            case ConstantParam.COINTYPE_ETC:
            case ConstantParam.COINTYPE_XRP:
                feeRate = new BigDecimal("0.001");
                break;
            default:
                feeRate = new BigDecimal("100");
                break;
        }
        return amt.multiply(feeRate);
    }
    @Override
    public BigDecimal getDepositFee(BigDecimal amt,
                                     String coinType,
                                     String tufaQingkuang) {
        BigDecimal depositFee = null;
        switch (coinType) {
            case ConstantParam.COINTYPE_BTC:
            case ConstantParam.COINTYPE_LTC:
            case ConstantParam.COINTYPE_ETH:
            case ConstantParam.COINTYPE_ETC:
                depositFee = new BigDecimal("0.001");
                break;
            case ConstantParam.COINTYPE_DASH:
                depositFee = new BigDecimal("0.01");
                break;
            case ConstantParam.COINTYPE_XRP:
                depositFee = new BigDecimal("0.00");
                break;
            case ConstantParam.COINTYPE_CASH:
            	depositFee = new BigDecimal("0.005");
            	break;
            default:
                depositFee = new BigDecimal("100");
                break;
        }
    	if("yes".equals(tufaQingkuang)) {
    		depositFee = new BigDecimal("0.02");
    	}
        return amt.multiply(depositFee);
    }

}
