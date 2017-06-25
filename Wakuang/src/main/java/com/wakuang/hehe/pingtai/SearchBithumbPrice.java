package com.wakuang.hehe.pingtai;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.wakuang.hehe.common.ConstantParam;
import com.wakuang.hehe.hanguo.service.HanguoService;
import com.wakuang.hehe.utils.WakuangStringUtils;

@Service(value = "bithumbService")
public class SearchBithumbPrice implements SearchPingtaiPrice {
	
	@Autowired
	private HanguoService hanguoSerivce;
	
	@Override
	public BigDecimal getTakerFee(BigDecimal amt, String coinType) {
		// TODO Auto-generated method stub
		BigDecimal feeRate = null;
        switch (coinType) {
            case ConstantParam.COINTYPE_BTC:
            case ConstantParam.COINTYPE_LTC:

            case ConstantParam.COINTYPE_ETH:
            case ConstantParam.COINTYPE_DASH:
            case ConstantParam.COINTYPE_ETC:
            case ConstantParam.COINTYPE_XRP:
                feeRate = new BigDecimal("0.0015");
                break;
            default:
                feeRate = new BigDecimal("100");
                break;
        }
        return amt.multiply(feeRate);
	}

	@Override
	public BigDecimal getDepositFee(BigDecimal amt, String coinType, String tufaQingkuang) {
		// TODO Auto-generated method stub
		 BigDecimal depositFee = null;
	        switch (coinType) {
	            case ConstantParam.COINTYPE_BTC:
	            	depositFee = new BigDecimal("0.0005");
	            	break;
	            case ConstantParam.COINTYPE_LTC:
	            case ConstantParam.COINTYPE_ETH:
	            case ConstantParam.COINTYPE_DASH:
	            case ConstantParam.COINTYPE_ETC:
	            case ConstantParam.COINTYPE_XRP:
	                depositFee = new BigDecimal("0.01");
	                break;
	            case ConstantParam.COINTYPE_CASH:
	            	depositFee = new BigDecimal("0");
	            	break;
	            default:
	                depositFee = new BigDecimal("100");
	                break;
	        }
	        if("yes".equals(tufaQingkuang)) {
	    		depositFee = new BigDecimal("0.02");
	    	}
	        return depositFee;
	}

	@Override
	public Map<String, Map<String, BigDecimal>> getPrice() throws Exception {
		// TODO Auto-generated method stub
		String coinTypes[] = {ConstantParam.COINTYPE_BTC, ConstantParam.COINTYPE_ETH, ConstantParam.COINTYPE_LTC, ConstantParam.COINTYPE_DASH, ConstantParam.COINTYPE_ETC, ConstantParam.COINTYPE_XRP};
		String result;
		Map<String, Map<String, BigDecimal>> coins = new HashMap<>();
		for(String coin : coinTypes) {
			result = hanguoSerivce.getTicker(coin);
			JsonNode rootNode = WakuangStringUtils.stringToJsonNode(result);
			if(rootNode != null && rootNode.get(ConstantParam.STATUS) !=null && "0000".equals(rootNode.get(ConstantParam.STATUS).asText())) {
				JsonNode data = rootNode.get("data");
				Map<String, BigDecimal> coinInfo = new HashMap<String, BigDecimal>();
				coinInfo.put(ConstantParam.COIN_INFO_MAX, new BigDecimal(data.get(ConstantParam.MAX_PRICE).asText()));
				coinInfo.put(ConstantParam.COIN_INFO_MIN, new BigDecimal(data.get(ConstantParam.MIN_PRICE).asText()));
				coinInfo.put(ConstantParam.COIN_INFO_BUY, new BigDecimal(data.get(ConstantParam.BUY_PRICE).asText()));
				coinInfo.put(ConstantParam.COIN_INFO_SELL, new BigDecimal(data.get(ConstantParam.SELL_PRICE).asText()));
				coinInfo.put(ConstantParam.COIN_INFO_PRICE, new BigDecimal(data.get(ConstantParam.CLOSING_PRICE).asText()));
				coins.put(coin, coinInfo);
			}
		}
		System.out.println(ConstantParam.PLAFORM_BITHUM + ":  " + coins.toString());
		return coins;
	}

}
