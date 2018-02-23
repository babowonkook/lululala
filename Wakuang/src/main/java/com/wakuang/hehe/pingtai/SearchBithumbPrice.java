package com.wakuang.hehe.pingtai;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.wakuang.hehe.common.ConstantParam;
import com.wakuang.hehe.hanguo.service.CodeService;
import com.wakuang.hehe.hanguo.util.Api_Client;
import com.wakuang.hehe.hanguo.util.SslTest;
import com.wakuang.hehe.utils.WakuangStringUtils;


@Service(value = "bithumbService")
public class SearchBithumbPrice implements SearchPingtaiPrice {
    private Logger        log = LoggerFactory.getLogger(SearchBithumbPrice.class);
	
	@Autowired 
	@Qualifier("codeServiceSourceImpl")
    CodeService codeService;
	
	@Override
	public BigDecimal getTakerFee(BigDecimal amt, String coinType) {
		BigDecimal feeRate = null;
        switch (coinType) {
            case ConstantParam.COINTYPE_BTC:
            case ConstantParam.COINTYPE_LTC:

            case ConstantParam.COINTYPE_ETH:
            case ConstantParam.COINTYPE_DASH:
            case ConstantParam.COINTYPE_ETC:
            case ConstantParam.COINTYPE_XRP:
            case ConstantParam.COINTYPE_XMR:
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
	            case ConstantParam.COINTYPE_XMR:
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
        Date startDate = new Date();
        String coinTypes[] = { ConstantParam.COINTYPE_BTC, ConstantParam.COINTYPE_ETH, ConstantParam.COINTYPE_LTC, ConstantParam.COINTYPE_DASH, ConstantParam.COINTYPE_ETC, ConstantParam.COINTYPE_XRP, ConstantParam.COINTYPE_BCH };
		String result;
		Map<String, Map<String, BigDecimal>> coins = new HashMap<>();
        String tickerUrl = "https://api.bithumb.com/public/ticker/";
        result = SslTest.getRequest(tickerUrl + ConstantParam.COINTYPE_ALL, 3000);
        JsonNode rootNode = WakuangStringUtils.stringToJsonNode(result).get("data");
        for (String coin : coinTypes) {
            if (rootNode != null) {
                JsonNode eachCoin;
                eachCoin = rootNode.get(coin);
                Map<String, BigDecimal> coinInfo = new HashMap<String, BigDecimal>();
                coinInfo.put(ConstantParam.COIN_INFO_MAX, new BigDecimal(eachCoin.get("min_price").asText()));
                coinInfo.put(ConstantParam.COIN_INFO_MIN, new BigDecimal(eachCoin.get("min_price").asText()));
                coinInfo.put(ConstantParam.COIN_INFO_BUY, new BigDecimal(eachCoin.get("buy_price").asText()));
                coinInfo.put(ConstantParam.COIN_INFO_SELL, new BigDecimal(eachCoin.get("sell_price").asText()));
                coinInfo.put(ConstantParam.COIN_INFO_PRICE, new BigDecimal(eachCoin.get("sell_price").asText()));
                coins.put(coin, coinInfo);
            }
        }

        Date endDate = new Date();
        long costSec = endDate.getTime() - startDate.getTime();
        if (log.isInfoEnabled()) {
            log.info("cost Time: {} seconds", costSec / 1000);
            log.info(ConstantParam.PLAFORM_BITHUM + ":  " + coins.toString());
        }
		return coins;
	}

	@Override
	public Map<String, Map<String, BigDecimal>> getPriceByCoin(String coinType) throws Exception {
		// TODO Auto-generated method stub
		
		// 1. 코인 가격구함
		Map<String, Map<String, BigDecimal>>prices = getPrice();
		// 2. 코인 가격큼 비트코인 구매 해야할 겟수(코인 비트코인 겟)
		
		String coinTypes[] = { ConstantParam.COINTYPE_BTC, ConstantParam.COINTYPE_ETH, ConstantParam.COINTYPE_LTC, ConstantParam.COINTYPE_DASH, ConstantParam.COINTYPE_ETC, ConstantParam.COINTYPE_XRP, ConstantParam.COINTYPE_BCH };
		
		BigDecimal tradeCoinBuyPrice = prices.get(coinType).get(ConstantParam.COIN_INFO_BUY);
		BigDecimal tradeCoinSellPrice = prices.get(coinType).get(ConstantParam.COIN_INFO_SELL);
		BigDecimal tradeCoinPrice = prices.get(coinType).get(ConstantParam.COIN_INFO_PRICE);
		for (String coin : coinTypes) {
			Map<String, BigDecimal> coinInfo = prices.get(coin);
			BigDecimal coinBuyPrice = coinInfo.get(ConstantParam.COIN_INFO_BUY);
			BigDecimal coinSellPrice = coinInfo.get(ConstantParam.COIN_INFO_SELL);
			BigDecimal coinPrice = coinInfo.get(ConstantParam.COIN_INFO_PRICE);
			
			// 중간코인 구매가에 팔아서 타겟코인 판매가에  구입 
			BigDecimal coinBuyNum = coinSellPrice.divide(tradeCoinBuyPrice, 8, BigDecimal.ROUND_UP);
			
			// 타겟코인 구매가에 팔아서 중간코인 판매가에 구입
			BigDecimal coinSellNum = coinBuyPrice.divide(tradeCoinSellPrice, 8, BigDecimal.ROUND_UP);
			BigDecimal coinNum = coinPrice.divide(tradeCoinPrice, 8, BigDecimal.ROUND_UP);
			coinInfo.put(ConstantParam.COIN_INFO_COINBUYPRICE, coinBuyNum);
			coinInfo.put(ConstantParam.COIN_INFO_COISELLPRICE, coinSellNum);
			coinInfo.put(ConstantParam.COIN_INFO_COINPRICE, coinNum);
		    log.info("coninType : "+coin+" coinBuyNum {}, coinSellNum {}", coinBuyNum.toString(), coinSellNum.toString());
		}
		
		
		return prices;
	}
	
	public Map<String, Map<String, BigDecimal>> getBalance() throws Exception {
			Api_Client api = new Api_Client("11", "22");
		
			HashMap<String, String> rgParams = new HashMap<String, String>();
			rgParams.put("currency", "ALL");
		
			List<Map<String, String>> coinList = (List<Map<String, String>>) codeService.getCode("CD002");
			List<Map<String, String>> balanceTpList = (List<Map<String, String>>) codeService.getCode("CD003");
		
			try {
			    String result = api.callApi("/info/balance", rgParams);
			    
			    JsonNode rootNode = WakuangStringUtils.stringToJsonNode(result).get("data");
			    if (rootNode != null) {
			    	Map<String, Map<String, String>> coin = new HashMap<>();
			    		for(int i = 0 ; i < coinList.size(); i++) {
			    			Map<String, String> balance = new HashMap<>();
			    			for(int j = 0 ; j < balanceTpList.size(); j++) {
			    				String value = rootNode.get(balanceTpList.get(j)+"_"+coinList.get(i).toString().toLowerCase()).toString();
			    				balance.put(balanceTpList.get(j).toString(), value);
			    			}
			    			coin.put(coinList.get(i).toString(), balance);
			    		}
			    		System.out.println(coin);
			    }
			    
			    System.out.println(result);
			} catch (Exception e) {
			    e.printStackTrace();
			}
		return null;
	}
	

}
