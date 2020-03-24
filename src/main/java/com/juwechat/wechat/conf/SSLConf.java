package com.juwechat.wechat.conf;
 
import javax.net.ssl.*;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/** HTTPS认证配置类
 * @author 秋枫艳梦
 * @date 2019-05-25
 * */
public class SSLConf {
    private static SSLSocketFactory sslSocketFactory;  //SSLSocketFactory对象
 
    /**
     *  返回SSLSocketFactory工厂
     * */
    public static SSLSocketFactory getSslSocketFactory() {
        return sslSocketFactory;
    }
 
    /**
     *  静态块，实例化SSLSocketFactory工厂对象
     * */
    static {
        SSLContext sslContext= null;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null,new TrustManager[]{new TrustAllManager()},new SecureRandom());
            sslSocketFactory=sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    /**
     * 静态内部类，实现X509TrustManager接口
     * */
    public static class TrustAllManager implements X509TrustManager {
 
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
 
        }
 
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
 
        }
 
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
 
    /**
     *  静态内部类，实现HostnameVerifier接口
     * */
    public static class TrustAllHost implements HostnameVerifier {
 
        /** 此方法用于验证客户机，省略验证逻辑，保证返回true即可通过验证
         * @param s 认证字符串，类似于token
         * @param sslSession SSL会话
         * @return 是否通过验证
         * */
        public boolean verify(String s, SSLSession sslSession) {
            return true;
        }
    }
}