package com.juwechat.wechat.conf;

/**
 *  公众号开发配置类，保存一些必要的配置
 * @author 秋枫艳梦
 * @date 2019-06-07
 * */
public class ChatConf {
    //获取到的凭证
    public static volatile String token;

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        ChatConf.token = token;
    }

    //第三方用户唯一凭证
    public static final String APPID = "";
    //第三方用户唯一凭证密钥
    public static final String SECRET = "kong";
    //获取access_token的接口请求地址
    public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+APPID+"&secret="+SECRET;
    //创建菜单的接口请求地址
    public static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create";
    //创建临时二维码的接口地址
    public static final String CREATE_QRCODE_URL = "https://api.weixin.qq.com/cgi-bin/qrcode/create";
    //换取二维码的接口地址，会返回一张图片，可直接使用IO流读取
    public static final String GET_QRCODE_URL = "https://mp.weixin.qq.com/cgi-bin/showqrcode";
    //上传素材的接口地址
    public static final String UPLOAD_FILE_URL = "https://api.weixin.qq.com/cgi-bin/media/upload";
    //添加客服的接口地址
    public static final String ADD_SERVICE_URL = "https://api.weixin.qq.com/customservice/kfaccount/add";
    //发送客服消息的接口地址
    public static final String SEND_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send";
    //直接获取用户基本信息的接口
    public static final String GET_INFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info";
    //通过code换取网页授权access_token的接口地址
    public static final String GET_USER_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+ChatConf.APPID+"&secret="+ChatConf.SECRET+"&code={code}&grant_type=authorization_code";
    //根据网页授权access_token和openid换取用户信息的接口地址
    public static final String GET_CODE_INFO_URL = "https://api.weixin.qq.com/sns/userinfo";
}
