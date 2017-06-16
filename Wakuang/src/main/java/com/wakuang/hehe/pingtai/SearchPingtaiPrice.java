package com.wakuang.hehe.pingtai;

import java.math.BigDecimal;
import java.util.Map;

public abstract interface SearchPingtaiPrice {

    /**
     * @MethodName : buyCoin
     * @Description :
     * @Date : 2017. 6. 16.
     * @Author : 황원국
     * @param plaform
     * @param coinType
     * @param totalPrice
     * @param unitCost
     * @return
     */
    BigDecimal buyCoin(String plaform,
                       String coinType,
                       BigDecimal totalPrice,
                       BigDecimal unitCost);

    /**
     * @MethodName : sellCoin
     * @Description :
     * @Date : 2017. 6. 16.
     * @Author : 황원국
     * @param plaform
     * @param coinType
     * @param coinCnt
     * @param unitCost
     * @return
     */
    BigDecimal sellCoin(String plaform,
                        String coinType,
                        BigDecimal coinCnt,
                        BigDecimal unitCost);

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