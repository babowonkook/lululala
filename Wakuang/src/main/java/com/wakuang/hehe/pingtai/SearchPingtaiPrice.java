package com.wakuang.hehe.pingtai;

import java.math.BigDecimal;
import java.util.Map;

public abstract interface SearchPingtaiPrice {
	
	/**
	 * 交易手续费 
	 * @param amt
	 * @param coinType
	 * @return 币数
	 */
	public BigDecimal getTakerFee(BigDecimal amt,
	            String coinType);
	
	/**
	 * 转币手续费 
	 * @param amt
	 * @param coinType
	 * @return 币数
	 */
	public BigDecimal getDepositFee(BigDecimal amt,
	              String coinType,
	              String tufaQingkuang);

	/**
	 * @MethodName : getPrice
	 * @Description : 플렛폼 시세구하기
	 * @Date : 2017. 6. 16.
	 * @Author : 황원국
	 * @return
	 * @throws Exception
	 */
    public Map<String, Map<String, BigDecimal>> getPrice() throws Exception;
    
    
	/**
	 * @MethodName : getPrice
	 * @Description : 플렛폼 시세구하기(코인가)
	 * @Date : 2017. 6. 16.
	 * @Author : 황원국
	 * @return
	 * @throws Exception
	 */
    public Map<String, Map<String, BigDecimal>> getPriceByCoin(String coinType) throws Exception;
}