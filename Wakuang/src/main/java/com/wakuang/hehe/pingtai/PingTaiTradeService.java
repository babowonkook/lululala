package com.wakuang.hehe.pingtai;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	
	@Autowired 
    @Qualifier("jubiService")
    private SearchPingtaiPrice jubiService;
	
    @Autowired
    @Qualifier("coincheckService")
    private SearchCoincheckPrice coincheckService;
    
    @Autowired
    @Qualifier("bterService")
    private SearchBterPrice searchBterPrice;
    
    @Autowired
    @Qualifier("btceService")
    private SearchBtcEPrice searchBtcePrice;

    @Autowired
    @Qualifier("huobiService")
    private SearchHuobiPrice     searchHuobiPrice;

    @Autowired
    @Qualifier("okCoinService")
    private SearchOKCoinPrice    searchOKCoinPrice;

    public BigDecimal buyCoin(String plaform,
            String coinType,
            BigDecimal totalPrice,
            BigDecimal unitCost,
            String tk) {
    	Map<String, SearchPingtaiPrice> serviceMap = initService();
    	SearchPingtaiPrice searchPingtaiPrice = serviceMap.get(plaform);
    	
		// 전체금액 / 단가 - 매입수수료 - 송금수수료
		BigDecimal buyCoin = new BigDecimal("0");
		try {
			buyCoin = totalPrice.divide(unitCost, 8, BigDecimal.ROUND_HALF_UP);
		} catch (Exception e) {
			// TODO: handle exception
		}
		BigDecimal aftDivideTrakerFeeCoin =  buyCoin.subtract(searchPingtaiPrice.getTakerFee(buyCoin, coinType));
		BigDecimal aftDepositFeeCoin = aftDivideTrakerFeeCoin.subtract(searchPingtaiPrice.getDepositFee(aftDivideTrakerFeeCoin, coinType, tk));
		return aftDepositFeeCoin;
    }


	public BigDecimal sellCoin(String plaform,
	             String coinType,
	             BigDecimal coinCnt,
	             BigDecimal unitCost,
	             String tk) {
		Map<String, SearchPingtaiPrice> serviceMap = initService();
		SearchPingtaiPrice searchPingtaiPrice = serviceMap.get(plaform);		
		// 단가 * 코인갯수 - 판매수수료 - 인출수수료
		BigDecimal sellMoney = unitCost.multiply(coinCnt);
		BigDecimal aftTrakerFeeMoney = sellMoney.subtract(searchPingtaiPrice.getTakerFee(sellMoney, coinType));
		BigDecimal aftRemittanceFeeMoney = aftTrakerFeeMoney.subtract(searchPingtaiPrice.getDepositFee(aftTrakerFeeMoney, ConstantParam.COINTYPE_CASH, tk));
		return aftRemittanceFeeMoney;
	}
	
	public Map<String, Object> compare(String pingTaiTp,
							  String pingTaiTp2,
							  String rate,
							  String amt,
							  String tk) throws Exception {

		BigDecimal rateChange = new BigDecimal(rate);
		BigDecimal totalPrice = new BigDecimal(amt);
		Map<String, SearchPingtaiPrice> serviceMap = initService();
		SearchPingtaiPrice searchPingtaiPrice = serviceMap.get(pingTaiTp);
		SearchPingtaiPrice searchPingtaiPrice2 = serviceMap.get(pingTaiTp2);
        Map<String, Map<String, BigDecimal>> result1 = searchPingtaiPrice.getPrice();
        Map<String, Map<String, BigDecimal>> result2 = searchPingtaiPrice2.getPrice();
			
        return compare(result1, result2, pingTaiTp, pingTaiTp2, totalPrice, rateChange, tk);
	}

    public List<Map<String, Object>> compareAll(String rateCNY,
                                                String rateJPY,
                                                String rateUSD,
                                                String amt,
                                                String tk) throws Exception {

        BigDecimal CNY = new BigDecimal(rateCNY);
        BigDecimal JPY = new BigDecimal(rateJPY);
        BigDecimal USD = new BigDecimal(rateUSD);
        BigDecimal totalPrice = new BigDecimal(amt);
        Map<String, SearchPingtaiPrice> serviceMap = initService();
        SearchPingtaiPrice searchBithumbPrice = serviceMap.get(ConstantParam.PLAFORM_BITHUM);
        SearchPingtaiPrice searchBiduobaoPrice = serviceMap.get(ConstantParam.PLAFORM_BIDUOBAO);
        SearchPingtaiPrice searchJubiPrice = serviceMap.get(ConstantParam.PLAFORM_JUBI);
        SearchPingtaiPrice searchBetrPrice = serviceMap.get(ConstantParam.PLAFORM_BETR);
        SearchPingtaiPrice searchCoincheckPrice = serviceMap.get(ConstantParam.PLAFORM_COINCHECK);
        // SearchPingtaiPrice searchBtcePrice = serviceMap.get(ConstantParam.PLAFORM_BTCE);
        SearchPingtaiPrice searchHuobiPrice = serviceMap.get(ConstantParam.PLAFORM_HUOBI);
        SearchPingtaiPrice searchOKCoinPrice = serviceMap.get(ConstantParam.PLAFORM_OKCOIN);
        Map<String, Map<String, BigDecimal>> bitumbPrice = searchBithumbPrice.getPrice();
        Map<String, Map<String, BigDecimal>> biduobaoPrice = searchBiduobaoPrice.getPrice();
        Map<String, Map<String, BigDecimal>> jubiPrice = searchJubiPrice.getPrice();
        Map<String, Map<String, BigDecimal>> betrPrice = searchBetrPrice.getPrice();
        Map<String, Map<String, BigDecimal>> coincheckPrice = searchCoincheckPrice.getPrice();
        // Map<String, Map<String, BigDecimal>> btcePrice = searchBtcePrice.getPrice();
        Map<String, Map<String, BigDecimal>> huobiPrice = searchHuobiPrice.getPrice();
        Map<String, Map<String, BigDecimal>> okCoinPrice = searchOKCoinPrice.getPrice();
        List<Map<String, Object>> comparList = new ArrayList<>();
        Map<String, Object> compare1 = compare(biduobaoPrice, bitumbPrice, ConstantParam.PLAFORM_BIDUOBAO, ConstantParam.PLAFORM_BITHUM, totalPrice, CNY, tk);
        Map<String, Object> compare2 = compare(jubiPrice, bitumbPrice, ConstantParam.PLAFORM_JUBI, ConstantParam.PLAFORM_BITHUM, totalPrice, CNY, tk);
        Map<String, Object> compare3 = compare(betrPrice, bitumbPrice, ConstantParam.PLAFORM_BETR, ConstantParam.PLAFORM_BITHUM, totalPrice, CNY, tk);
        Map<String, Object> compare4 = compare(coincheckPrice, bitumbPrice, ConstantParam.PLAFORM_COINCHECK, ConstantParam.PLAFORM_BITHUM, totalPrice, JPY, tk);
        // Map<String, Object> compare5 = compare(btcePrice, bitumbPrice, ConstantParam.PLAFORM_BTCE, ConstantParam.PLAFORM_BITHUM, totalPrice, USD, tk);
        Map<String, Object> compare6 = compare(huobiPrice, bitumbPrice, ConstantParam.PLAFORM_HUOBI, ConstantParam.PLAFORM_BITHUM, totalPrice, CNY, tk);
        Map<String, Object> compare7 = compare(okCoinPrice, bitumbPrice, ConstantParam.PLAFORM_OKCOIN, ConstantParam.PLAFORM_BITHUM, totalPrice, CNY, tk);
        comparList.add(compare1);
        comparList.add(compare2);
        comparList.add(compare3);
        comparList.add(compare6);
        comparList.add(compare7);
        comparList.add(compare4);
        // comparList.add(compare5);
        return comparList;
    }

    public Map<String, Object> compare(Map<String, Map<String, BigDecimal>> result1,
                                       Map<String, Map<String, BigDecimal>> result2,
                                       String pingTaiTp,
                                       String pingTaiTp2,
                                       BigDecimal totalPrice,
                                       BigDecimal rateChange,
                                       String tk) {
        String coinTypes[] = { ConstantParam.COINTYPE_BTC, ConstantParam.COINTYPE_ETH, ConstantParam.COINTYPE_LTC, ConstantParam.COINTYPE_DASH, ConstantParam.COINTYPE_ETC, ConstantParam.COINTYPE_XRP };
        Map<String, Object> result = new HashMap<>();
        Map<String, BigDecimal> coinMap;
        Map<String, BigDecimal> coinMap2;
        BigDecimal compareValue;
        Map<String, Object> temp;

        Map<String, Object> compaireInfo = new HashMap<>();
        compaireInfo.put(ConstantParam.RESPONSE_PLATFORM1, pingTaiTp);
        compaireInfo.put(ConstantParam.RESPONSE_PLATFORM2, pingTaiTp2);
        compaireInfo.put(ConstantParam.RESPONSE_EXCHANGERATE, rateChange);
        result.put(ConstantParam.RESPONSE_COMPAIRE_INFO, compaireInfo);
        for (String coin : coinTypes) {
            if (result1.get(coin) != null && result2.get(coin) != null) {
                coinMap = result1.get(coin);
                coinMap2 = result2.get(coin);
                compareValue = coinMap2.get(ConstantParam.COIN_INFO_PRICE).subtract(coinMap.get(ConstantParam.COIN_INFO_PRICE).multiply(rateChange));
                temp = new HashMap<>();
                // 平台1 > 平台2
                // 平台1 卖， 平台2买

                // 收益率， 收益额
                if (compareValue.compareTo(BigDecimal.ZERO) == 1) {
                    BigDecimal coinCnt = buyCoin(pingTaiTp, coin, totalPrice, coinMap.get(ConstantParam.COIN_INFO_PRICE).multiply(rateChange), tk);
                    BigDecimal sellAmt = sellCoin(pingTaiTp2, coin, coinCnt, coinMap2.get(ConstantParam.COIN_INFO_PRICE), tk);
                    BigDecimal shouYi_e = sellAmt.subtract(totalPrice);
                    BigDecimal shouYiRate = shouYi_e.divide(totalPrice, 4, BigDecimal.ROUND_HALF_UP);
                    temp.put(ConstantParam.SHOUYI_E, shouYi_e.setScale(0, BigDecimal.ROUND_HALF_UP));
                    temp.put(ConstantParam.SHOUYI_RATE, shouYiRate.setScale(4, BigDecimal.ROUND_HALF_UP));
                } else {
                    BigDecimal coinCnt = buyCoin(pingTaiTp2, coin, totalPrice, coinMap2.get(ConstantParam.COIN_INFO_PRICE), tk);
                    BigDecimal sellAmt = sellCoin(pingTaiTp, coin, coinCnt, coinMap.get(ConstantParam.COIN_INFO_PRICE), tk);
                    BigDecimal shouYi_e = sellAmt.multiply(rateChange).subtract(totalPrice);
                    BigDecimal shouYiRate = shouYi_e.divide(totalPrice, 4, BigDecimal.ROUND_HALF_UP);
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
		rs.put(ConstantParam.PLAFORM_JUBI, jubiService);
        rs.put(ConstantParam.PLAFORM_COINCHECK, coincheckService);
        rs.put(ConstantParam.PLAFORM_BETR, searchBterPrice);
        rs.put(ConstantParam.PLAFORM_BTCE, searchBtcePrice);
        rs.put(ConstantParam.PLAFORM_HUOBI, searchHuobiPrice);
        rs.put(ConstantParam.PLAFORM_OKCOIN, searchOKCoinPrice);
		return rs;
	}
	
	
	
}
