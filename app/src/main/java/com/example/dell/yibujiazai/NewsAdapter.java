package com.example.dell.yibujiazai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by CYF on 2016/6/16.
 */
public class NewsAdapter extends BaseAdapter{

    private List<NewsBean>mList;
    //转化一个布局
    private LayoutInflater mInflater;
    //添加一个构造方法将参数传进去  上下文，数据
    public NewsAdapter(Context context,List<NewsBean>data){
        mList=data;
        //初始化对象
        mInflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView=mInflater.inflate(R.layout.item_layout,null);
            viewHolder.ivIcon= (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.tvTitle= (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tvContent= (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(viewHolder);
        }else {
            //viewHolder取出
            viewHolder= (ViewHolder) convertView.getTag();
        }


        //赋值  设置默认图片  异步加载在改变图标
        viewHolder.ivIcon.setImageResource(R.mipmap.ic_launcher);
        String url=mList.get(position).newsIconUrl;
        viewHolder.ivIcon.setTag(url);

        //通过调用showImageByThread方法把图片所对应的url对应的viewHolder传递给showImageByThread
        //从方法里将图片设置给viewHolder
        new ImageLoader().showImageByThread(viewHolder.ivIcon,url);//mList.get(position).newsIconUrl
        //直接从数据中取出数据 取出的数据都包含viewHolder中的三个数据
        viewHolder.tvTitle.setText(mList.get(position).newsTitle);

        viewHolder.tvContent.setText(mList.get(position).newsContent);

        return convertView;
    }
    class ViewHolder{
        public TextView tvTitle,tvContent;
        public ImageView ivIcon;
    }
}
