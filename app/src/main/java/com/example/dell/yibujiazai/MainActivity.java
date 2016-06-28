package com.example.dell.yibujiazai;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView mListView;
    private static String URL="http://118.244.212.82:9092/newsClient/news_list?ver=4&subid=1&dir=1&nid=2&stamp=20150601&cnt=20";

    /**
     * 1.在onCreate中new一个NewsAsyncTask将一个URL穿进去
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView= (ListView) findViewById(R.id.lv_main);

        new NewsAsyncTask().execute(URL);


    }

    /**
     * 3.将url对应的JSON格式数据转化为我们所封装的NewsBean
     * @param url
     * @return
     */
    private List<NewsBean> getJsonData(String url) {
        List<NewsBean>newsBeenList=new ArrayList<>();
        //获取网络返回的字符串
        try {
            /**
             * new URL(url).openStream()
             * 此句功能与url.openConnection().getInputStream()相同
             * kegenjuURL直接联网获取网络数据，返回值类型为InputStream
             */

            String jsonString=readStream(new URL(url).openStream());

            Log.d("xys111",jsonString);

            JSONObject jsonObject;
            NewsBean newsBean;
            //把json字符串转换成jsonObject
            try {
                jsonObject=new JSONObject(jsonString);

                Log.d("xys222", String.valueOf(jsonObject));

                JSONArray jsonArray=jsonObject.getJSONArray("data");

                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject=jsonArray.getJSONObject(i);
                    Log.d("xys333", String.valueOf(jsonObject));

                    //注意("icon")别写错
                    newsBean=new NewsBean();
                    newsBean.newsIconUrl=jsonObject.getString("icon");
                    newsBean.newsTitle=jsonObject.getString("title");
                    newsBean.newsContent=jsonObject.getString("summary");
                    //把newsBean放到newsBeenList

                    newsBeenList.add(newsBean);
                    Log.d("xys444", String.valueOf(newsBeenList));



                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return newsBeenList;
    }

    /**
     * 通过is解析网页返回的数据
     * 读取网络信息
     * @param is
     * @return
     */
    private String readStream(InputStream is){
        InputStreamReader isr;
        String result="";
        try {
            String line="";
            //通过InputStreamReader指定格式   字节流转化成字符流
            isr=new InputStreamReader(is,"utf-8");

            Log.d("啊啊啊啊", String.valueOf(isr));

            //通过BufferedReader把字符流以BufferedReader形式读取出来
            BufferedReader br=new BufferedReader(isr);
            //用while读取br

            try {
                while ((line=br.readLine())!=null){
                    //最终拼接到readLine里面
                    result+=line;

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 2.实现网络的异步访问
     * url传给getJsonData
     */
    class NewsAsyncTask extends AsyncTask<String,Void,List<NewsBean>> {
        @Override
        protected List<NewsBean> doInBackground(String... params) {

            return getJsonData(params[0]);//params[0]请求的网址

        }

        /**
         * 将数据传递给了 NewsAdapter
         * 通过 NewsAdapter构造了mListView数据源
         * 通过数据源显示我们需要的数据
         *
         * @param newsBeen
         */
        @Override
        protected void onPostExecute(List<NewsBean> newsBeen) {
            super.onPostExecute(newsBeen);
            NewsAdapter adapter=new NewsAdapter(MainActivity.this,newsBeen);
            mListView.setAdapter(adapter);

        }
    }

}
