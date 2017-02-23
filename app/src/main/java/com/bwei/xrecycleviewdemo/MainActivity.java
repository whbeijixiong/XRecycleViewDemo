package com.bwei.xrecycleviewdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;

import com.bwei.xrecycleviewdemo.bean.Bean;
import com.bwei.xrecycleviewdemo.bean.Data;
import com.bwei.xrecycleviewdemo.okhttp.OkHttpUtil;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;

import static android.os.Environment.getExternalStorageDirectory;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public String url1 = "http://www.yulin520.com/a2a/impressApi/news/mergeList?sign=C7548DE604BCB8A17592EFB9006F9265&pageSize=10&gender=2&ts=1871746850&page=1";
    private ArrayList<Data> data1;
    private XRecyclerView recyclerview;
    private Button qc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerview = (XRecyclerView) findViewById(R.id.recyclerview);
        Button dw = (Button) findViewById(R.id.dw);
        qc = (Button) findViewById(R.id.qc);
        dw.setOnClickListener(this);
        qc.setOnClickListener(this);
        OkHttpUtil okHttpUtil = new OkHttpUtil();
        okHttpUtil.getJson(url1, new HttpData());


    }

    private void getData(ArrayList<Data> data1) {
        // 创建一个线性布局管理器

        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);

        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);

        recyclerview.setLoadingListener(new XRecyclerView.LoadingListener(){


            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {

            }
        });

        MyAdapter myAdapter = new MyAdapter(MainActivity.this, data1);
        recyclerview.setAdapter(myAdapter);


        File file = getExternalStorageDirectory();

        long length = file.length();
        String s = Formatter.formatFileSize(MainActivity.this, Long.valueOf(length));
        qc.setText("清除缓存" + s);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dw:
                Intent intent = new Intent(MainActivity.this,OtherActivity.class);
                startActivity(intent);
                break;
                case R.id.qc:
                    ImageLoader.getInstance().clearDiskCache();
                    ImageLoader.getInstance().clearMemoryCache();
                    qc.setText("清除缓存0KB");
                break;
        }
    }

    class HttpData extends OkHttpUtil.HttpCallBack {

        @Override
        public void onSusscess(String data) {
            Gson gson = new Gson();
            Bean bean = gson.fromJson(data, Bean.class);
            data1 = bean.data;
            getData(data1);
        }

    }
}
