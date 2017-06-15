package com.wakuang.hehe.hanguo.util;

import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;

public class SslTest {
    public static String getRequest(String url,int timeOut) throws Exception{
        URL u = new URL(url);
        if("https".equalsIgnoreCase(u.getProtocol())){
            SslUtils.ignoreSsl();
        }
        URLConnection conn = u.openConnection();
        conn.setConnectTimeout(timeOut);
        conn.setReadTimeout(timeOut);
        return IOUtils.toString(conn.getInputStream());
    }
}
