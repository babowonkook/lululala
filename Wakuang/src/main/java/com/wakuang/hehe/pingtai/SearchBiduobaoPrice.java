package com.wakuang.hehe.pingtai;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.wakuang.hehe.common.ConstantParam;
import com.wakuang.hehe.hanguo.util.SslTest;
import com.wakuang.hehe.utils.WakuangStringUtils;

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

    public Map<String, Map<String, BigDecimal>> getPrice() throws Exception {
        String url = "https://www.biduobao.com/coin/allcoin?t=123123";
        String html = SslTest.getRequest(url, 3000);
        JsonNode rootNode = WakuangStringUtils.stringToJsonNode(html);
        Iterator<Entry<String, JsonNode>> jsonNodes = rootNode.fields();
        Map coins = new HashMap<String, Map<String, BigDecimal>>();
        while (jsonNodes.hasNext()) {
            Entry<String, JsonNode> node = jsonNodes.next();

            JsonNode values = node.getValue();
            ArrayNode datas = (ArrayNode) values;
            if (values.isArray()) {
                Map<String, BigDecimal> coinInfo = new HashMap<String, BigDecimal>();
                int i = 0;
                for (final JsonNode objNode : values) {

                    switch (i) {
                        case 1:
                            coinInfo.put(ConstantParam.COIN_INFO_PRICE, new BigDecimal(objNode.toString()));
                            break;
                        case 2:
                            coinInfo.put(ConstantParam.COIN_INFO_BUY, new BigDecimal(objNode.toString()));
                            break;
                        case 3:
                            coinInfo.put(ConstantParam.COIN_INFO_SELL, new BigDecimal(objNode.toString()));
                            break;
                        case 4:
                            coinInfo.put(ConstantParam.COIN_INFO_MAX, new BigDecimal(objNode.toString()));
                            break;
                        case 5:
                            coinInfo.put(ConstantParam.COIN_INFO_MIN, new BigDecimal(objNode.toString()));
                            break;
                        case 6:
                            coinInfo.put(ConstantParam.COIN_INFO_SUM, new BigDecimal(objNode.toString()));
                            break;
                        case 7:
                            coinInfo.put(ConstantParam.COIN_INFO_VOLUME, new BigDecimal(objNode.toString()));
                            break;
                        default:
                            break;
                    }
                    i++;
                }
                System.out.println(coinInfo.toString());
                coins.put(node.getKey().toString().toUpperCase(), coinInfo);
            }

        }
        System.out.println(coins.toString());
        return null;

    }

}
