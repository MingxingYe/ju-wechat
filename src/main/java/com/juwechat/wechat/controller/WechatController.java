package com.juwechat.wechat.controller;

import com.juwechat.wechat.queue.QRCodeQueue;
import com.juwechat.wechat.queue.UserInfoQueue;
import com.juwechat.wechat.utils.MessageUtil;
import com.juwechat.wechat.utils.XMLUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 *  微信控制器，负责接收微信推送的消息及开发者验证
 * @author 秋枫艳梦
 * @date 2019-06-02
 * */
@Controller
public class WechatController {

    /**
     *  进行开发者验证，接入微信。注意这个方法是GET请求
     * @param signature 微信加密签名
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @param echostr 随机字符串
     * @return 验证通过，原样返回echostr字符串
     * */
    @RequestMapping(value = "/wechat",method = RequestMethod.GET)
    public void checkAuth(@RequestParam(value = "signature") String signature,
                          @RequestParam(value = "timestamp") String timestamp,
                          @RequestParam(value = "nonce") String nonce,
                          @RequestParam(value = "echostr") String echostr,
                          HttpServletResponse response) {
        //认证逻辑这里就不实现了，我们直接返回echostr，写入到响应体中
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            //返回结果
            writer.print(echostr);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (writer!=null){
                writer.close();
            }
        }
    }

    /**
     *  处理交互行为
     * @param request 请求体
     * @param response 响应体
     * */
    @PostMapping(value = "/wechat",produces = "application/xml;charset=utf-8")
    public void doRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //设置微信公众号返回字符格式
        response.setCharacterEncoding("UTF-8");
        //将XML转为Map
        Map<String,String> map = XMLUtil.getMap(request.getInputStream());
        PrintWriter writer = response.getWriter();

        //这里不要弄混了，微信推过来的信息是用户发过来的，所以ToUserName是我们的公众号，FromUserName是用户的微信openid
        //所以我们既然要回复过去，就要颠倒过来
        String fromUser = map.get("ToUserName");
        String toUser = map.get("FromUserName");
        //要返回给用户的信息
        String content = "";

        //先判断是事件消息，还是普通消息
        if (map.get("MsgType").equals("event")){
            //如果是被关注事件，向用户回复内容，只需要将整理好的XML文本参数返回给微信即可
            if (map.get("Event").equals("subscribe")){
                //如果没有EventKey，说明是普通关注，否则是扫码关注事件
                String eventKey = map.get("EventKey");
                if (eventKey==null){
                    content = "欢迎关注秋枫艳梦的测试公众号！";
                }else {
                    String param = eventKey.substring(eventKey.indexOf("_")+1);
                    //为了简单，这里直接返回一句话，实际业务场景要更复杂
                    content = "您是由openid为"+param+"的用户引进来的，我们已对其进行了奖励，您也可以生成海报，分享给朋友，可获得奖励";

                    //写入到阻塞队列
                    try {
                        UserInfoQueue.userQueue.put(toUser);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }else if (map.get("Event").equals("CLICK")){
                //点击菜单事件，判断EventKey
                if (map.get("EventKey").equals("CREATE_POSTER")){
                    //这里不返回空串了，没必要，因为所有的IO操作我们都是通过阻塞队列异步实现的
                    System.out.println("正在为您制作海报，请稍等");
                    content = "正在为您制作海报，请稍等";
                    //写入到阻塞队列
                    try {
                        QRCodeQueue.codeQueue.put(toUser);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }else if (map.get("Event").equals("SCAN")){
                content = "您已关注我们的公众号，此活动仅限首次关注时参加一次";
            }
        }else if (map.get("MsgType").equals("text")){
            //如果是普通文本消息，先拿到用户发送过来的内容，模拟自动答疑的场景
            String text = map.get("Content");

            if (text.equals("1")){
                content = "您可以在“我的账户——服务——退款”中查看您的退款明细";
            }else if (text.equals("2")){
                content = "如果您购买了本店的产品，订单页面会展示在您的主菜单中";
            }else if (text.equals("3")){
                content = "如有更多问题，请拨打我们的客服热线：xxxxx";
            }else {
                //否则，不管用户输入什么，都返回给ta这个列表，这也是最常见的场景
                content = "请输入您遇到的问题编号：\n"+
                        "1、如何查看退款进度？\n"+
                        "2、我的订单在哪里查看？\n"+
                        "3、其他问题";
            }
        }

        //把数据包返回给微信服务器，微信服务器再推给用户
        writer.print(MessageUtil.setMessage(fromUser,toUser,content));
        writer.close();
    }
}
