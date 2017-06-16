package com.wakuang.hehe.pingtai;

import java.math.BigDecimal;
import java.util.Map;

public class SearchBiduobaoPrice implements SearchPingtaiPrice {

    @Override
    public BigDecimal buyCoin(String plaform,
                              String coinType,
                              BigDecimal totalPrice,
                              BigDecimal unitCost) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BigDecimal sellCoin(String plaform,
                               String coinType,
                               BigDecimal coinCnt,
                               BigDecimal unitCost) {
        // TODO Auto-generated method stub
        return null;
    }

    public Map<String, Map<String, BigDecimal>> getPrice() {

        // JsonNode rootNode = CBTStringUtils.stringToJsonNode(html);

        return null;

    }

}
