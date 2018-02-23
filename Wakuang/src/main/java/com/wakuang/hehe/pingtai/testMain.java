package com.wakuang.hehe.pingtai;

import java.math.BigDecimal;
import java.util.Map;

public class testMain {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub	
		System.out.println("hahah");
		
		
//        SearchBitfinexPrice searchBitfinexPrice = new SearchBitfinexPrice();
//        Map<String, Map<String, BigDecimal>> prices = searchBitfinexPrice.getPriceByCoin("BTC");
//		System.out.println("hahah");
//		
//		SearchBithumbPrice searchBithumbPrice = new SearchBithumbPrice();
//		Map<String, Map<String, BigDecimal>> bithumbPrices = searchBithumbPrice.getPriceByCoin("BTC");
//		System.out.println("hahah");
		
		SearchBithumbPrice searchBithumbPrice = new SearchBithumbPrice();
		Map<String, Map<String, BigDecimal>> haha = searchBithumbPrice.getBalance();
		System.out.println("hahah");
	}

}
