package com.wakuang.hehe.hanguo.service;



import org.springframework.stereotype.Service;

import com.wakuang.hehe.hanguo.util.SslTest;

@Service
public class HanguoService {

    private String tickerUrl      = "https://api.bithumb.com/public/ticker/";

    private String orderbookUrl   = "https://api.bithumb.com/public/orderbook/";

    private String transactionUrl = "https://api.bithumb.com/public/recent_transactions/";
    
    public String getTicker(String type) throws Exception {
        return SslTest.getRequest(tickerUrl+type, 100000);
    }
    
    public String getOrderbook(String type) throws Exception {
        return SslTest.getRequest(orderbookUrl+type, 100000);
    }
    /**
     * 
     * @MethodName  : getTransaction
     * @Description : 
     * @Date        : 2017. 6. 15.
     * @Author      : �̵���
     * @param type
     * @return
     * @throws Exception
     */
    public String getTransaction(String type) throws Exception {
        return SslTest.getRequest(transactionUrl+type, 10000);
    }
}
