package com.juwechat.wechat.utils;

import com.alibaba.fastjson.JSON;
import com.juwechat.wechat.conf.ChatConf;
import com.juwechat.wechat.conf.SSLConf;
import okhttp3.*;

import java.io.File;
import java.io.IOException;

/**
 *  上传素材的工具类
 * @author 秋枫艳梦
 * @date 2019-06-08
 * */
public class UploadUtil {

    /**
     *  上传图片素材
     * @param file 素材文件
     * @return 素材ID，media_id
     * */
    public static String uploadImage(File file){
        OkHttpClient client = null;
        Request request = null;
        Response response = null;
        String mediaId = "";

        try {
            client = new OkHttpClient.Builder()
                    .sslSocketFactory(SSLConf.getSslSocketFactory(),new SSLConf.TrustAllManager())
                    .hostnameVerifier(new SSLConf.TrustAllHost()).build();

            MultipartBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("media", file.getName(),RequestBody.create(MediaType.parse("application/octet-stream"),file))
                    .build();

            request = new Request.Builder().url(ChatConf.UPLOAD_FILE_URL+"?access_token="+ChatConf.getToken()+"&type=image")
                    .post(body).build();

            response = client.newCall(request).execute();
            if (response.isSuccessful()){
                //从响应体中获取media_id
                mediaId = JSON.parseObject(response.body().string()).getString("media_id");
            }
        }catch (IOException e){

        }finally {
            if (response!=null){
                response.close();
            }
            client.dispatcher().executorService().shutdown();
        }

        return mediaId;
    }
}
