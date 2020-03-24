package com.juwechat.wechat.utils;

import com.juwechat.wechat.conf.ChatConf;
import com.juwechat.wechat.conf.SSLConf;
import okhttp3.*;

import java.io.IOException;

/**
 *  客服接口的工具类
 * @author 秋枫艳梦
 * @date 2019-06-08
 * */
public class ServiceUtil {

    /**
     *  创建客服
     *
     * */
    public static void createService(){
        OkHttpClient client = null;
        Request request = null;
        Response response = null;

        try {
            client = new OkHttpClient.Builder()
                    .sslSocketFactory(SSLConf.getSslSocketFactory(),new SSLConf.TrustAllManager())
                    .hostnameVerifier(new SSLConf.TrustAllHost()).build();

            String param = "{\"kf_account\":\"service-1\",\"nickname\" : \"客服1\",\"password\" : \"pswd\"}";
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),param);
            request = new Request.Builder().url(ChatConf.ADD_SERVICE_URL+"?access_token=22_ueOrELkPh8yT5MTy4ESXYsPOupU9uxjGplVGbMokmkl12YYzYCXZt4c8dv67QTU1bHymP4YoqfPlO8ot8CpeHRCRlJKjIkGuKOQ9HVAXSYz99rjM0LL-i8w8ZUoPBGcAIAGSX")
                    .post(requestBody).build();

            response = client.newCall(request).execute();
            if (response.isSuccessful()){
                System.out.println(response.body().string());
            }
        }catch (IOException e){

        }finally {
            if (response!=null){
                response.close();
            }
            client.dispatcher().executorService().shutdown();
        }
    }

    public static void main(String[] args) {
        createService();
    }

    /**
     *  发送图片消息
     * @param openid 接收人
     * @param mediaId 素材ID
     * */
    public static void sendImage(String openid,String mediaId){
        OkHttpClient client = null;
        Request request = null;
        Response response = null;

        try {
            client = new OkHttpClient.Builder()
                    .sslSocketFactory(SSLConf.getSslSocketFactory(),new SSLConf.TrustAllManager())
                    .hostnameVerifier(new SSLConf.TrustAllHost()).build();

            String param = "{\"touser\":\""+openid+"\",\"msgtype\":\"image\",\"image\":{\"media_id\":\""+mediaId+"\"}}";
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),param);
            request = new Request.Builder().url(ChatConf.SEND_MESSAGE_URL+"?access_token="+ChatConf.getToken())
                    .post(requestBody).build();

            response = client.newCall(request).execute();
        }catch (IOException e){

        }finally {
            if (response!=null){
                response.close();
            }
            client.dispatcher().executorService().shutdown();
        }
    }
}
