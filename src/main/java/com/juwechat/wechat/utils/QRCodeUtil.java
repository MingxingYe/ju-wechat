package com.juwechat.wechat.utils;

import com.alibaba.fastjson.JSON;
import com.juwechat.wechat.conf.ChatConf;
import com.juwechat.wechat.conf.SSLConf;
import okhttp3.*;

import java.io.IOException;

/**
 *  创建二维码的工具类
 * @author 秋枫艳梦
 * @date 2019-06-08
 * */
public class QRCodeUtil {

    /**
     *  创建二维码
     * @param param 二维码参数
     * @return 获取到的ticket，调用换取二维码图片的接口时，需要使用此ticket
     * */
    public static String createQRCode(String param){
        OkHttpClient client = null;
        Request request = null;
        Response response = null;
        String ticket = "";

        try {
            client = new OkHttpClient.Builder()
                    .sslSocketFactory(SSLConf.getSslSocketFactory(),new SSLConf.TrustAllManager())
                    .hostnameVerifier(new SSLConf.TrustAllHost()).build();

            //请求体参数，JSON字符串，scene_str的value就是我们要放到二维码中的参数
            String body = "{\"expire_seconds\": 604800, \"action_name\": \"QR_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \""+param+"\"}}}";
            //将JSON字符串放入请求体中
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),body);
            //实例化请求体对象
            request = new Request.Builder().url(ChatConf.CREATE_QRCODE_URL+"?access_token="+ChatConf.getToken())
                    .post(requestBody).build();
            //发起请求，获取响应体
            response = client.newCall(request).execute();
            if (response.isSuccessful()){
                ticket = JSON.parseObject(response.body().string()).getString("ticket");
            }
        }catch (IOException e){

        }finally {
            if (response!=null){
                response.close();
            }
            client.dispatcher().executorService().shutdown();
        }

        return ticket;
    }
}
