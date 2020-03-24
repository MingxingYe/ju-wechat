package com.juwechat.wechat.utils;

import com.alibaba.fastjson.JSON;
import com.juwechat.wechat.conf.ChatConf;
import com.juwechat.wechat.conf.SSLConf;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 *  获取access_token并定期刷新的工具类
 * @author 秋枫艳梦
 * @date 2019-06-07
 * */
public class TokenUtil {


    /**
     *  获取access_token并保存
     * */
    public static void getToken() throws InterruptedException {
        while (true){
            //客户端
            OkHttpClient client = null;
            //响应体
            Response response = null;
            //请求体
            Request request = null;
            try {
                //创建一个可以访问HTTPS的客户机
                client = new OkHttpClient.Builder()
                        .sslSocketFactory(SSLConf.getSslSocketFactory(),new SSLConf.TrustAllManager())
                        .hostnameVerifier(new SSLConf.TrustAllHost()).build();
                //构建请求体
                request = new Request.Builder().url(ChatConf.ACCESS_TOKEN_URL).get().build();
                //发起请求，获取响应体
                response = client.newCall(request).execute();

                if (response.isSuccessful()){
                    String token = JSON.parseObject(response.body().string()).getString("access_token");
                    ChatConf.setToken(token);
                }
            }catch (IOException e){

            }finally {
                if (response!=null){
                    response.close();
                }
                client.dispatcher().executorService().shutdown();
            }
            System.out.println("此次获取到的token是："+ChatConf.getToken());
            //不到两小时去获取一次
            TimeUnit.MINUTES.sleep(115);
        }
    }

    /**
     *  开始任务
     * */
    public static void startTask(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    TokenUtil.getToken();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }
}
