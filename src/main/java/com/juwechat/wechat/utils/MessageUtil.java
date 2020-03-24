package com.juwechat.wechat.utils;

import java.util.Date;

/**
 *  返回消息的工具类
 * @author 秋枫艳梦
 * @date 2019-06-02
 * */
public class MessageUtil {
    /**
     * 要回复的消息
     * @param fromUser 发送方
     * @param toUser 接收方
     * @param content 回复给用户的内容
     * @return 整理好的XML文本
     * */
    public static String setMessage(String fromUser,String toUser,String content){

        return "<xml>\n" +
                "  <ToUserName><![CDATA["+toUser+"]]></ToUserName>\n" +
                "  <FromUserName><![CDATA["+fromUser+"]]></FromUserName>\n" +
                "  <CreateTime>"+new Date().getTime()+"</CreateTime>\n" +
                "  <MsgType><![CDATA[text]]></MsgType>\n" +
                "  <Content><![CDATA["+content+"]]></Content>\n" +
                "</xml>";
    }
}
