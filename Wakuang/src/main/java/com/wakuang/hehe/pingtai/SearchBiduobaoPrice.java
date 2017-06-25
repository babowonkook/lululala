package com.wakuang.hehe.pingtai;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.wakuang.hehe.common.ConstantParam;
import com.wakuang.hehe.hanguo.util.SslTest;
import com.wakuang.hehe.utils.WakuangStringUtils;

@Service("biduobaoService")
public class SearchBiduobaoPrice implements SearchPingtaiPrice {
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
                coins.put(node.getKey().toString().toUpperCase(), coinInfo);
            }

        }
        System.out.println(ConstantParam.PLAFORM_BIDUOBAO + ":  " + coins.toString());
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

}
