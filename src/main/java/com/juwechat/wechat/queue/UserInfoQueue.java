package com.juwechat.wechat.queue;

import com.juwechat.wechat.utils.UserInfoUtil;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 *  异步获取用户信息的阻塞队列类
 * @author 秋枫艳梦
 * @date 2019-06-09
 * */
public class UserInfoQueue {
    //存放openid的阻塞队列.openid即微信推送的数据包中的FromUserName
    public static BlockingQueue<String> userQueue = new LinkedBlockingDeque<>();
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
                            String openId = userQueue.take();
                            String info = UserInfoUtil.getInfoById(openId);
                            System.out.println("获取到的用户信息：\n"+info);
                            //这里模拟一下即可
                            System.out.println("已写入数据库......");
                        }catch (Exception e){

                        }
                    }
                }
            };
            new Thread(runnable).start();
        }
    }
}
