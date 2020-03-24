package com.juwechat.wechat.queue;

import com.juwechat.wechat.conf.ChatConf;
import com.juwechat.wechat.utils.PosterUtil;
import com.juwechat.wechat.utils.QRCodeUtil;
import com.juwechat.wechat.utils.ServiceUtil;
import com.juwechat.wechat.utils.UploadUtil;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class QRCodeQueue {
    //存放openid的阻塞队列.openid即微信推送的数据包中的FromUserName
    public static BlockingQueue<String> codeQueue = new LinkedBlockingDeque<>();
    //监听队列的线程数量，这里我们开启15个线程去处理(并不是越多越好)，提高吞吐量
    public static final int THREADS = 15;

    /**
     *  监听阻塞队列，执行相关业务
     * */
    public static void startListen(){
        for (int i = 0; i < THREADS; i++) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    while (true){
                        try {
                            String openId = codeQueue.take();
                            //创建二维码，将用户的openid作为参数，用于后期数据消费，同时获取ticket
                            String ticket = QRCodeUtil.createQRCode(openId);
                            //生成海报，背景图
                            File file = PosterUtil.createPoster(ChatConf.GET_QRCODE_URL+"?ticket="+ticket,
                                    "http://difkvg.natappfree.cc/static/img/back.jpg");
                            //将海报图片上传至素材
                            String mediaId = UploadUtil.uploadImage(file);
                            //调用客服接口，发送图片消息，将海报发送给用户
                            ServiceUtil.sendImage(openId,mediaId);
                        }catch (Exception e){

                        }
                    }
                }
            };
            new Thread(runnable).start();
        }
    }
}