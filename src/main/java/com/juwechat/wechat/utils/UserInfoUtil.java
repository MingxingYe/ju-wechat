package com.juwechat.wechat.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.juwechat.wechat.conf.ChatConf;
import com.juwechat.wechat.conf.SSLConf;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 *  获取用户基本信息的工具类
 * @author 秋枫艳梦
 * @date 2019-06-09
 * */
public class UserInfoUtil {

    /**
     *  根据openid直接获取用户的基本信息
     * @param openId 用户的openid
     * @return 获取到的信息，是一个JSON字符串
     * */
    public static String getInfoById(String openId){
        OkHttpClient client = null;
        Request request = null;
        Response response = null;
        String infoStr = "";

        try {
            client = new OkHttpClient.Builder()
                    .sslSocketFactory(SSLConf.getSslSocketFactory(),new SSLConf.TrustAllManager())
                    .hostnameVerifier(new SSLConf.TrustAllHost()).build();

            request = new Request.Builder()
                    .url(ChatConf.GET_INFO_URL+"?access_token="+ChatConf.getToken()+"&openid="+openId+"&lang=zh_CN")
                    .build();

            response = client.newCall(request).execute();
            if (response.isSuccessful()){
                infoStr = response.body().string();
            }
        }catch (IOException e){

        }finally {
            if (response!=null){
                response.close();
            }
            client.dispatcher().executorService().shutdown();
        }

        return infoStr;
    }

    /**
     *  网页授权，根据code获取用户的信息
     * @param code 微信回调页面时传来的code
     * @return 获取到的信息
     * */
    public static String getInfoByCode(String code){
        OkHttpClient client = null;
        Request request = null;
        Response response = null;
        String infoStr = "";

        try {
            client = new OkHttpClient.Builder()
                    .sslSocketFactory(SSLConf.getSslSocketFactory(),new SSLConf.TrustAllManager())
                    .hostnameVerifier(new SSLConf.TrustAllHost()).build();

            //先根据code换取access_token和openid
            request = new Request.Builder().url(ChatConf.GET_USER_TOKEN_URL.replace("{code}",code)).get().build();
            response = client.newCall(request).execute();
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            String accessToken = jsonObject.getString("access_token");
            String openId = jsonObject.getString("openid");

            //再根据access_token和openid，获取用户的基本信息
            request = new Request.Builder()
                    .url(ChatConf.GET_CODE_INFO_URL+"?access_token="+accessToken+"&openid="+openId+"&lang=zh_CN")
                    .build();
            response = client.newCall(request).execute();
            infoStr = response.body().string();
            System.out.println(infoStr);
        }catch (IOException e){

        }finally {
            if (response!=null){
                response.close();
            }
            client.dispatcher().executorService().shutdown();
        }

        return infoStr;
    }
}
