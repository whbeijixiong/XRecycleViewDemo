package com.bwei.xrecycleviewdemo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bwei.xrecycleviewdemo.bean.Data;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;


/**
 * 作    者 ： 文欢
 * 时    间 ： 2017/2/22.
 * 描    述 ：
 * 修改时间 ：
 */

public class MyAdapter extends XRecyclerView.Adapter<MyAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Data> list;

    public MyAdapter(Context context, ArrayList<Data> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 创建一个View，简单起见直接使用系统提供的布局，就是一个TextView

        View view = View.inflate(parent.getContext(), R.layout.item, null);

        // 创建一个ViewHolder

        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(list.get(position).introduction);
        holder.age.setText(list.get(position).userAge);
        holder.join.setText(list.get(position).occupation);
        holder.name.setText(list.get(position).userName);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(new ImageLoaderConfiguration.Builder(context).build());
        imageLoader.displayImage(list.get(position).img, holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends XRecyclerView.ViewHolder {

        public TextView join;
        public TextView name;
        public TextView mTextView;
        public TextView age;
        public PhotoView imageView;

        public ViewHolder(View itemView) {

            super(itemView);

            mTextView = (TextView) itemView.findViewById(R.id.item_tv);
            imageView = (PhotoView) itemView.findViewById(R.id.img);
            name = (TextView) itemView.findViewById(R.id.name);
            age = (TextView) itemView.findViewById(R.id.age);
            join = (TextView) itemView.findViewById(R.id.join);

        }

    }
}
