package com.wakuang.hehe.pingtai;

import java.math.BigDecimal;
import java.util.Map;

import com.wakuang.hehe.common.ConstantParam;

public abstract interface SearchPingtaiPrice {

	public BigDecimal getTakerFee(BigDecimal amt,
	            String coinType);

	public BigDecimal getDepositFee(BigDecimal amt,
	              String coinType);

	/**
	 * @MethodName : getPrice
	 * @Description : 플렛폼 시세구하기
	 * @Date : 2017. 6. 16.
	 * @Author : 황원국
	 * @return
	 * @throws Exception
	 */
	public Map<String, Map<String, BigDecimal>> getPrice() throws Exception;
}