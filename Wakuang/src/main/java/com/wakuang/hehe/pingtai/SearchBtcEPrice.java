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

@Service("btceService")
public class SearchBtcEPrice implements SearchPingtaiPrice {

    private Logger log = LoggerFactory.getLogger(SearchBtcEPrice.class);

    public Map<String, Map<String, BigDecimal>> getPrice() throws Exception {
        Date startDate = new Date();
        String coinTypes[] = {ConstantParam.COINTYPE_BTC, ConstantParam.COINTYPE_ETH, ConstantParam.COINTYPE_LTC, "DSH"};
		String result;
		Map<String, Map<String, BigDecimal>> coins = new HashMap<>();
		String url = "https://btc-e.com/api/3/ticker/";
		
		for(String coin : coinTypes) {
			if(ConstantParam.COINTYPE_DASH.equals(coin)){
				url = url + "dsh" + "_usd-";
			}else{
				url = url + coin.toLowerCase() + "_usd-";
			}
		}
		
		result = SslTest.getRequest(url, 3000);
		JsonNode rootNode = WakuangStringUtils.stringToJsonNode(result);
		
		for(String coin : coinTypes) {
			
			if(rootNode != null) {
				JsonNode eachCoin;
				if(ConstantParam.COINTYPE_DASH.equals(coin)){
					eachCoin = rootNode.get("dsh_usd");
				}else{
					eachCoin = rootNode.get(coin.toLowerCase()+"_usd");
				}
				Map<String, BigDecimal> coinInfo = new HashMap<String, BigDecimal>();
				coinInfo.put(ConstantParam.COIN_INFO_MAX, new BigDecimal(eachCoin.get("high").asText()));
				coinInfo.put(ConstantParam.COIN_INFO_MIN, new BigDecimal(eachCoin.get("low").asText()));
				coinInfo.put(ConstantParam.COIN_INFO_BUY, new BigDecimal(eachCoin.get("buy").asText()));
				coinInfo.put(ConstantParam.COIN_INFO_SELL, new BigDecimal(eachCoin.get("sell").asText()));
				coinInfo.put(ConstantParam.COIN_INFO_PRICE, new BigDecimal(eachCoin.get("last").asText()));
				coins.put(coin, coinInfo);
			}
		}
        Date endDate = new Date();
        long costSec = endDate.getTime() - startDate.getTime();
        if (log.isInfoEnabled()) {
            log.info("cost Time: {} seconds", costSec / 1000);
            log.info(ConstantParam.PLAFORM_BTCE + ":  " + coins.toString());
        }
        return coins;

    }
    @Override
    public BigDecimal getTakerFee(BigDecimal amt,
                                   String coinType) {
        BigDecimal feeRate = null;
        switch (coinType) {
            case ConstantParam.COINTYPE_BTC:
            case ConstantParam.COINTYPE_LTC:
                feeRate = new BigDecimal("0.002");
                break;
            case ConstantParam.COINTYPE_ETH:
            case ConstantParam.COINTYPE_DASH:
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
            case ConstantParam.COINTYPE_DASH:
            case ConstantParam.COINTYPE_ETC:
            case ConstantParam.COINTYPE_XRP:
                depositFee = new BigDecimal("0.001");
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
	@Override
	public Map<String, Map<String, BigDecimal>> getPriceByCoin(String coinType) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
