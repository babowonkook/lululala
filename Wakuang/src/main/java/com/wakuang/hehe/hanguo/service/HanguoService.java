package com.wakuang.hehe.hanguo.service;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.wakuang.hehe.hanguo.util.HttpUtil;
import com.wakuang.hehe.hanguo.util.SslTest;

@Service
public class HanguoService {

    private String tickerUrl      = "ticker";

    private String orderbookUrl   = "https://api.bithumb.com/public/orderbook/";

    private String transactionUrl = "https://api.bithumb.com/public/recent_transactions/";
    
    public String getTicker(String type) throws Exception {
        return SslTest.getRequest(tickerUrl+type, 10000);
    }
    
    public String getOrderbook(String type) throws Exception {
        return SslTest.getRequest(orderbookUrl+type, 10000);
    }
    /**
     * 
     * @MethodName  : getTransaction
     * @Description : 
     * @Date        : 2017. 6. 15.
     * @Author      : ¿Ãµø±«
     * @param type
     * @return
     * @throws Exception
     */
    public String getTransaction(String type) throws Exception {
        return SslTest.getRequest(transactionUrl+type, 10000);
    }
}
