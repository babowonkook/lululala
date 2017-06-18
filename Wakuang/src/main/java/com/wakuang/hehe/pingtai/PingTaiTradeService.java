package com.wakuang.hehe.pingtai;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wakuang.hehe.common.ConstantParam;

@Service
public class PingTaiTradeService {
	
	@Autowired 
	@Qualifier("bithumbService")
	private SearchPingtaiPrice bithumbService;
	
	@Autowired 
	@Qualifier("biduobaoService")
	private SearchPingtaiPrice biduobaoService;
	
    public BigDecimal buyCoin(String plaform,
            String coinType,
            BigDecimal totalPrice,
            BigDecimal unitCost) {
    	Map<String, SearchPingtaiPrice> serviceMap = initService();
    	SearchPingtaiPrice searchPingtaiPrice = serviceMap.get(plaform);
    	
		// 전체금액 / 단가 - 매입수수료 - 송금수수료
		BigDecimal buyCoin = totalPrice.divide(unitCost,8,BigDecimal.ROUND_HALF_UP);
		BigDecimal aftDivideTrakerFeeCoin =  buyCoin.subtract(searchPingtaiPrice.getTakerFee(buyCoin, coinType));
		BigDecimal aftDepositFeeCoin = aftDivideTrakerFeeCoin.subtract(searchPingtaiPrice.getDepositFee(aftDivideTrakerFeeCoin, coinType));
		return aftDepositFeeCoin;
    }


	public BigDecimal sellCoin(String plaform,
	             String coinType,
	             BigDecimal coinCnt,
	             BigDecimal unitCost) {
		Map<String, SearchPingtaiPrice> serviceMap = initService();
		SearchPingtaiPrice searchPingtaiPrice = serviceMap.get(plaform);		
		// 단가 * 코인갯수 - 판매수수료 - 인출수수료
		BigDecimal sellMoney = unitCost.multiply(coinCnt);
		BigDecimal aftTrakerFeeMoney = sellMoney.subtract(searchPingtaiPrice.getTakerFee(sellMoney, coinType));
		BigDecimal aftRemittanceFeeMoney = aftTrakerFeeMoney.subtract(searchPingtaiPrice.getDepositFee(aftTrakerFeeMoney, ConstantParam.COINTYPE_CASH));
		return aftRemittanceFeeMoney;
	}
	
	public Map<String, Object> compare(String pingTaiTp,
							  String pingTaiTp2,
							  String rate,
							  String amt) throws Exception {
		String coinTypes[] = {ConstantParam.COINTYPE_BTC, ConstantParam.COINTYPE_ETH, ConstantParam.COINTYPE_LTC, ConstantParam.COINTYPE_DASH, ConstantParam.COINTYPE_ETC, ConstantParam.COINTYPE_XRP};
		BigDecimal rateChange = new BigDecimal(rate);
		BigDecimal totalPrice = new BigDecimal(amt);
		Map<String, SearchPingtaiPrice> serviceMap = initService();
		SearchPingtaiPrice searchPingtaiPrice = serviceMap.get(pingTaiTp);
		SearchPingtaiPrice searchPingtaiPrice2 = serviceMap.get(pingTaiTp2);
		Map<String, Map<String, BigDecimal>> rs1 = searchPingtaiPrice.getPrice();
		Map<String, Map<String, BigDecimal>> rs2 = searchPingtaiPrice2.getPrice();
			
		Map<String, Object> result = new HashMap<>();
		Map<String, BigDecimal> coinMap;
		Map<String, BigDecimal> coinMap2;
		BigDecimal compareValue;
		Map<String, Object> temp;
		for(String coin : coinTypes) {
			if(rs1.get(coin) != null && rs2.get(coin) != null) {
				coinMap = rs1.get(coin);
				coinMap2 = rs2.get(coin);
				compareValue = coinMap2.get(ConstantParam.COIN_INFO_PRICE).subtract(coinMap.get(ConstantParam.COIN_INFO_PRICE).multiply(rateChange));
				temp = new HashMap<>();
				// 平台1 > 平台2
				// 平台1 卖， 平台2买
				
				// 收益率， 收益额
				if(compareValue.compareTo(BigDecimal.ZERO) == 1){
					BigDecimal coinCnt = buyCoin(pingTaiTp, coin, totalPrice, coinMap.get(ConstantParam.COIN_INFO_PRICE).multiply(rateChange));
					BigDecimal sellAmt = sellCoin(pingTaiTp2, coin, coinCnt, coinMap2.get(ConstantParam.COIN_INFO_PRICE));
					BigDecimal shouYi_e = sellAmt.subtract(totalPrice);
					BigDecimal shouYiRate = shouYi_e.divide(totalPrice,4,BigDecimal.ROUND_HALF_UP);
					temp.put(ConstantParam.SHOUYI_E, shouYi_e.setScale(0, BigDecimal.ROUND_HALF_UP));
					temp.put(ConstantParam.SHOUYI_RATE, shouYiRate.setScale(4, BigDecimal.ROUND_HALF_UP));
				}else{
					BigDecimal coinCnt = buyCoin(pingTaiTp2, coin, totalPrice, coinMap2.get(ConstantParam.COIN_INFO_PRICE));
					BigDecimal sellAmt = sellCoin(pingTaiTp, coin, coinCnt, coinMap.get(ConstantParam.COIN_INFO_PRICE));
					BigDecimal shouYi_e = sellAmt.multiply(rateChange).subtract(totalPrice);
					BigDecimal shouYiRate = shouYi_e.divide(totalPrice,4,BigDecimal.ROUND_HALF_UP);
					temp.put(ConstantParam.SHOUYI_E, shouYi_e.setScale(0, BigDecimal.ROUND_HALF_UP));
					temp.put(ConstantParam.SHOUYI_RATE, shouYiRate.setScale(4, BigDecimal.ROUND_HALF_UP));
				}
				temp.put(ConstantParam.COMPARE, compareValue);
				temp.put(ConstantParam.MAP, coinMap);
				temp.put(ConstantParam.MAP2, coinMap2);
				result.put(coin, temp);
			}
		}
		return result;
	}

	private Map<String, SearchPingtaiPrice> initService() {
		Map<String, SearchPingtaiPrice> rs = new HashMap<>();
		rs.put(ConstantParam.PLAFORM_BITHUM, bithumbService);
		rs.put(ConstantParam.PLAFORM_BIDUOBAO, biduobaoService);
		return rs;
	}
	
	
	
}
