package com.juwechat.wechat.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 *  生成海报的工具类
 * @author 秋枫艳梦
 * @date 2019-06-08
 * */
public class PosterUtil {

    /**
     *  生成海报
     * @param qrcodeUrl 换取二维码图片的地址，这个地址返回的是一张图片，所以我们待会可以直接IO流读取它
     * @param backgroundUrl 背景图的地址，需要定位到一张图片，如http://xxx.com/statics/img/demo.jpg
     * */
    public static File createPoster(String qrcodeUrl,String backgroundUrl){
        //最终的海报图
        BufferedImage posterImg = new BufferedImage(530,950,BufferedImage.TYPE_INT_RGB);
        //二维码图片
        BufferedImage qrcodeImg;
        //海报背景图
        BufferedImage backgroundImg;
        //测试用户头像
        BufferedImage cesQImg;
        String url="http://thirdwx.qlogo.cn/mmopen/Q3auHgzwzM6icKGTcsxwhruMRruiad5YxYFEg0bHlJFT443MRADCibrrEVhhOB97uXZia8GIWWdo0MXtlvYCTRvDprbhNIL6HxlibaPPuJv0EhSY/132";
        //File对象，将生成的海报保存到这个随机文件名，最后再返回给调用者
        //File file = new File("C:\\Users\\Administrator\\Desktop\\poster\\"+System.currentTimeMillis()+".jpg");
        File file = new File("F:\\BaiduNetdiskDownload\\test"+System.currentTimeMillis()+".jpg");
        try {
            //读取二维码图片
            qrcodeImg = ImageIO.read(new URL(qrcodeUrl));
            backgroundImg = ImageIO.read(new URL(backgroundUrl));
            cesQImg = ImageIO.read(new URL(url));
            Graphics g = posterImg.getGraphics();//开启画图

            g.drawImage(backgroundImg.getScaledInstance(550, 978, Image.SCALE_DEFAULT), 0, 0, null); // 绘制缩小后的图
            g.drawImage(qrcodeImg.getScaledInstance(126, 126, Image.SCALE_DEFAULT), 47, 817, null); // 绘制缩小后的图
            g.drawImage(cesQImg.getScaledInstance(126, 126, Image.SCALE_DEFAULT), 126, 200, null); // 绘制缩小后的图
            g.drawString("宋铁强",100,135);
            g.setColor(Color.black);
            g.dispose();
            ImageIO.write(posterImg, "jpg", file);
        }catch (IOException e){

        }finally {

        }
        return file;
    }
}
