package com.wakuang.hehe.hanguo.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.wakuang.hehe.utils.WakuangStringUtils;

public class SslTest {
    
    private static List<String> list = new ArrayList<>();
    
    static{
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("D:/ip.txt")),
                                                                         "UTF-8"));
            String lineTxt = null;
            while ((lineTxt = br.readLine()) != null) {
                list.add(lineTxt);
            }
            br.close();
        } catch (Exception e) {
            System.err.println("read errors :" + e);
        }
    }
    
    public static String getRequest(String url,int timeOut) throws Exception{
        CloseableHttpClient httpClient = createSSLClientDefault();
        HttpGet get = new HttpGet();
        get.setURI(new URI(url));
        HttpResponse res = httpClient.execute(get);
        String rs = "";
        if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {  
            HttpEntity entity = res.getEntity(); 
            rs = IOUtils.toString(new InputStreamReader(entity.getContent(), HTTP.UTF_8)); 
        }
        return rs;
    }
    
    private static class TrustAnyTrustManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[] {};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
    
    public static CloseableHttpClient createSSLClientDefault() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                // 信任所有
                public boolean isTrusted(X509Certificate[] chain,
                                         String authType) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();
    }
    
    public static void main(String[] args) throws URISyntaxException, ClientProtocolException, IOException {
        CloseableHttpClient httpClient = createSSLClientDefault();
        HttpGet get = new HttpGet();
        get.setURI(new URI("https://www.biduobao.com/coin/allcoin?t=123123"));
        HttpResponse res = httpClient.execute(get);
        String rs = "";
        if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {  
            HttpEntity entity = res.getEntity(); 
            rs = IOUtils.toString(new InputStreamReader(entity.getContent(), HTTP.UTF_8)); 
            System.out.println(rs);
        }
        
    }
    
    public static String HttpsProxy(String url, String param, String proxy, int port) {
        HttpsURLConnection httpsConn = null;
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        BufferedReader reader = null;
        try {
            URL urlClient = new URL(url);
            System.out.println("请求的URL========：" + urlClient);

                SSLContext sc = SSLContext.getInstance("SSL");
                // 指定信任https
                sc.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());
                //创建代理虽然是https也是Type.HTTP
                Proxy proxy1=new Proxy(Type.HTTP, new InetSocketAddress(proxy, port));
                //设置代理
                httpsConn = (HttpsURLConnection) urlClient.openConnection(proxy1);

                httpsConn.setSSLSocketFactory(sc.getSocketFactory());
                httpsConn.setHostnameVerifier(new TrustAnyHostnameVerifier());
                 // 设置通用的请求属性
                httpsConn.setRequestProperty("accept", "*/*");
                httpsConn.setRequestProperty("connection", "Keep-Alive");
                httpsConn.setRequestProperty("user-agent",
                        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                
                // 发送POST请求必须设置如下两行
                httpsConn.setDoOutput(true);
                httpsConn.setDoInput(true);
                // 获取URLConnection对象对应的输出流
                result = IOUtils.toString(httpsConn.getInputStream());
/*                out = new PrintWriter(httpsConn.getOutputStream());
                // 发送请求参数
                out.print(param);
                // flush输出流的缓冲
                out.flush();*/
                // 定义BufferedReader输入流来读取URL的响应
/*                in = new BufferedReader(
                        new InputStreamReader(httpsConn.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    result += line;
                }*/
                // 断开连接
                httpsConn.disconnect();
                System.out.println("====result===="+result);
                System.out.println("返回结果：" + httpsConn.getResponseMessage());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (out != null) {
                out.close();
            }
        }

         return result;
    }
}
