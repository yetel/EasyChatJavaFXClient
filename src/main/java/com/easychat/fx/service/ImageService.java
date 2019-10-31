package com.easychat.fx.service;

import com.alibaba.fastjson.JSON;
import com.easychat.fx.bean.Constants;
import com.easychat.fx.bean.Result;
import com.easychat.fx.util.OkHttpUtils;
import com.easychat.fx.service.config.ServerConf;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ImageService {
    private static ImageService ourInstance = new ImageService();

    public static ImageService getInstance() {
        return ourInstance;
    }

    private ImageService() {
    }
    public String uploadImage(String imageBase64, String suffix) {
        String url = ServerConf.upload_image_url.replace("ip", ServerConf.ip).replace("port", ServerConf.port);

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("picBase64", imageBase64);
        paramMap.put("suffix", suffix);
        String jsonString = JSON.toJSONString(paramMap);
        String imageUrlJson = OkHttpUtils.postJsonParams(url, jsonString);
        Result result = JSON.parseObject(imageUrlJson, Result.class);
        return  (String) result.getData();
    }

    /**
     * 获取base64
     *
     * @param image     
     */
    public String getImageBase64(Image image) {
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();//io流
        try {
            ImageIO.write(bufferedImage, "png", baos);//写入流中
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] bytes = baos.toByteArray();//转换成字节
        BASE64Encoder encoder = new BASE64Encoder();
        String png_base64 =  encoder.encodeBuffer(bytes).trim();//转换成base64串
        return png_base64.replaceAll("\n", "").replaceAll("\r", "");
    }
    
    public Image getImageByUrl(String url) {
        try {
            //先获取本地的数据，如果本地文件中没有，则调用服务器获取
            String[] split = url.split("/");
            String imageName = split[split.length - 1];
            File localImage = new File(Constants.IMAGE_PATH + File.separator + imageName);
            if (localImage.exists()) {
                FileInputStream fileInputStream = new FileInputStream(localImage);
                return new Image(fileInputStream);
            }

            //网络获取
            File imagePath = new File(Constants.IMAGE_PATH);
            if (!imagePath.exists()) {
                boolean mkdir = imagePath.mkdir();
                if (!mkdir) {
                    return null;
                }
            }
            InputStream netImageInputStream = getNetImageInputStream(url);
            Image image = new Image(netImageInputStream);
            
            if (image != null) {
                //将网络获取的图片存到本地
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
                FileOutputStream fileOutputStream = new FileOutputStream(new File(Constants.IMAGE_PATH + File.separator + imageName));
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(bufferedImage, "png", baos);//写入流中
                    baos.writeTo(fileOutputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                fileOutputStream.flush();
                fileOutputStream.close();
                return image;
            }
        } catch (Exception e) {
            //加载图片失败
            e.printStackTrace();
        }

        return null;
    }

    public static InputStream getNetImageInputStream(String path) {
        URL url = null;
        InputStream is =null;
        try {
            url = new URL(path);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();//利用HttpURLConnection对象,我们可以从网络中获取网页数据.
            conn.setDoInput(true);
            conn.connect();
            is = conn.getInputStream();	//得到网络返回的输入流

        } catch (IOException e) {
            e.printStackTrace();
        }
        return is;
    }
}
