package com.juwechat.wechat.listener;

import com.juwechat.wechat.queue.QRCodeQueue;
import com.juwechat.wechat.queue.UserInfoQueue;
import com.juwechat.wechat.utils.TokenUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ProjectListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //定时获取access_token
        TokenUtil.startTask();
        //监听生成海报的事件
        QRCodeQueue.startListen();
        UserInfoQueue.startListen();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
