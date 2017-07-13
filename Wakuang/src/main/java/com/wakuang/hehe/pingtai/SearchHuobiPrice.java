package com.wakuang.hehe.pingtai;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.wakuang.hehe.common.ConstantParam;
import com.wakuang.hehe.hanguo.util.SslTest;
import com.wakuang.hehe.utils.WakuangStringUtils;

@Service("huobiService")
public class SearchHuobiPrice implements SearchPingtaiPrice {

    private Logger log = LoggerFactory.getLogger(SearchHuobiPrice.class);

    public Map<String, Map<String, BigDecimal>> getPrice() throws Exception {
        Date startDate = new Date();
		String coinTypes[] = {ConstantParam.COINTYPE_BTC, ConstantParam.COINTYPE_ETH, ConstantParam.COINTYPE_LTC, ConstantParam.COINTYPE_DASH, ConstantParam.COINTYPE_ETC, ConstantParam.COINTYPE_XRP};
		String result;
		Map<String, Map<String, BigDecimal>> coins = new HashMap<>();
		for(String coin : coinTypes) {
            if (ConstantParam.COINTYPE_ETH.equals(coin) || ConstantParam.COINTYPE_ETC.equals(coin)) {
                String url = "https://be.huobi.com/market/detail/merged?symbol=";
                url = url + coin.toLowerCase() + "cny";
                result = SslTest.getRequest(url, 3000);
                JsonNode rootNode = WakuangStringUtils.stringToJsonNode(result);
                if (rootNode != null) {
                    if (rootNode.get("tick") == null || "error".equals(rootNode.get("status"))) {
                        continue;
                    }
                    JsonNode tick = rootNode.get("tick");
                    Map<String, BigDecimal> coinInfo = new HashMap<String, BigDecimal>();
                    coinInfo.put(ConstantParam.COIN_INFO_MAX, BigDecimal.ZERO);
                    coinInfo.put(ConstantParam.COIN_INFO_MIN, BigDecimal.ZERO);
                    coinInfo.put(ConstantParam.COIN_INFO_BUY, new BigDecimal(tick.get("bid").get(0).asText()));
                    coinInfo.put(ConstantParam.COIN_INFO_SELL, new BigDecimal(tick.get("ask").get(0).asText()));
                    coinInfo.put(ConstantParam.COIN_INFO_PRICE, new BigDecimal(tick.get("ask").get(0).asText()));
                    coins.put(coin, coinInfo);
                }

            } else if (ConstantParam.COINTYPE_BTC.equals(coin) || ConstantParam.COINTYPE_LTC.equals(coin)) {
                String url = "http://api.huobi.com/staticmarket/detail_";
                url = url + coin.toLowerCase() + "_json.js";
                result = SslTest.getRequest(url, 3000);
                JsonNode rootNode = WakuangStringUtils.stringToJsonNode(result);
                if (rootNode != null) {
                    if (rootNode.get("p_new") == null) {
                        continue;
                    }
                    Map<String, BigDecimal> coinInfo = new HashMap<String, BigDecimal>();
                    coinInfo.put(ConstantParam.COIN_INFO_MAX, BigDecimal.ZERO);
                    coinInfo.put(ConstantParam.COIN_INFO_MIN, BigDecimal.ZERO);
                    coinInfo.put(ConstantParam.COIN_INFO_BUY, new BigDecimal(rootNode.get("p_low").asText()));
                    coinInfo.put(ConstantParam.COIN_INFO_SELL, new BigDecimal(rootNode.get("p_high").asText()));
                    coinInfo.put(ConstantParam.COIN_INFO_PRICE, new BigDecimal(rootNode.get("p_new").asText()));
                    coins.put(coin, coinInfo);
                }

            }
		}
        
        Date endDate = new Date();
        long costSec = endDate.getTime() - startDate.getTime();
        if (log.isInfoEnabled()) {
            log.info("cost Time: {} seconds", costSec / 1000);
            log.info(ConstantParam.PLAFORM_HUOBI + ":  " + coins.toString());
        }
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
