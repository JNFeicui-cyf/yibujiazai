package com.example.dell.yibujiazai;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * 加载图片
 * Created by CYF on 2016/6/16.
 */
public class ImageLoader {


    private ImageView mImageView;
    private String mUrl;

    private Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mImageView.getTag().equals(mUrl)){
                mImageView.setImageBitmap((Bitmap) msg.obj);
            }
        }
    };




    /**
     * 代表我们使用多线程的方式加载图片
     * 哪一个ImageView需要显示图片
     * 标志图片
     * @param imageView
     * @param url
     */
    public void showImageByThread(ImageView imageView, final String url){
        mImageView=imageView;
        mUrl=url;



        //子线程
        new Thread(){
            @Override
            public void run() {
                super.run();
                Bitmap bitmap=getBitmapFromURL(url);
                Message message=Message.obtain();
                message.obj=bitmap;
                handler.sendMessage(message);

            }
        }.start();
    }

    /**
     * 通过urlString转化Bitmap
     * @param urlString
     * @return
     */
    public Bitmap getBitmapFromURL(String urlString){
        Bitmap bitmap;
        InputStream is = null;
        try {
            URL url=new URL(urlString);
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            is=new BufferedInputStream(connection.getInputStream());
            bitmap= BitmapFactory.decodeStream(is);
            connection.disconnect();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
