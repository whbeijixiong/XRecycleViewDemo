package com.bwei.xrecycleviewdemo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

/**
 * 作    者 ： 文欢
 * 时    间 ： 2017/2/23.
 * 描    述 ：
 * 修改时间 ：
 */

public class OtherActivity extends Activity{
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    //定位相关
    LocationClient mlocationClient;
    private MyBDLocationListenter myBDLocationListenter;
    //判断是否是用户第一次进入
    private boolean isFirstIn = true;
    //定义两个变量实现在拉远处之后记录我的位置的经纬度
    private double mLatitude;
    private double mLongtitude;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_other
        );
        this.context = this;
        //初始化资源ID
        initView();
        //初始化定位
        initLocation();
    }
    //初始化定位
    private void initLocation() {
        mlocationClient = new LocationClient(this);
        myBDLocationListenter = new MyBDLocationListenter();
        //让定位的类注册定位的监听
        mlocationClient.registerLocationListener(myBDLocationListenter);
        //需要给LocationClient定义一些选项
        LocationClientOption opentions = new LocationClientOption();
        //设置经纬度类型
        opentions.setCoorType("bd09ll");
        //设置是否需要获取地址
        opentions.setIsNeedAddress(true);
        //设置是否打开GPS
        opentions.setOpenGps(true);
        //设置每隔多少秒请求
        opentions.setScanSpan(1000);
        //设置完opentions之后去监听里面

        //将选项添加到LOcationClient上去
        mlocationClient.setLocOption(opentions);
    }

    //初始化资源id
    private void initView() {
        //创建地图对象
        mMapView = (MapView) findViewById(R.id.b_mapView);
        //设置地图初始时倍数的大小
        mBaiduMap = mMapView.getMap();
        //通过状态改变的工厂类设置缩放
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        //将对象设置到地图上
        mBaiduMap.setMapStatus(msu);

    }
    //实现让Activity的生命周期与地图的生命周期同步
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //默认需要开启定位
        mBaiduMap.setMyLocationEnabled(true);
        //判断如果没有开启地图的话 就让他开启
        if(!mlocationClient.isStarted()){
            mlocationClient.start();
        }
        //然后在onStop的方法里面停止定位
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBaiduMap.setMyLocationEnabled(false);
        mlocationClient.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }
    //定义一个上下文菜单 设置按菜单键的时候生成
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //根据点中的菜单键选择地图的类型
        switch(item.getItemId()){
            case R.id.id_map_common:
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                break;
            case R.id.id_map_site:
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.id_map_traffic:
                //选择交通地图的时候首先要判断是否打开
                if(!mBaiduMap.isTrafficEnabled()){
                    mBaiduMap.setTrafficEnabled(true);
                    item.setTitle("实时交通(off)");
                }else{
                    mBaiduMap.setTrafficEnabled(false);
                    item.setTitle("实时交通(on)");
                }
                break;
            case R.id.id_map_location:
                returnToMyLocation();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //拉远之后点击回到我的位置
    public void returnToMyLocation(){
        LatLng latLng = new LatLng(mLatitude,mLongtitude);
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        //将定位的效果通过动画传递到百度地图的对象上面
        mBaiduMap.animateMapStatus(msu);
    }
    //定位相关 BDLOcationListenter需要定义一个内部类去实现它
    public class MyBDLocationListenter implements BDLocationListener {
        //定位成功之后的回调
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            MyLocationData myLocationData = new MyLocationData.Builder()//
                    .accuracy(bdLocation.getRadius())//获取经纬度
                    .latitude(bdLocation.getLatitude())//
                    .longitude(bdLocation.getLongitude()).build();
            mBaiduMap.setMyLocationData(myLocationData);
            //定位后记录当前的经纬度
            mLatitude = bdLocation.getLatitude();
            mLongtitude = bdLocation.getLongitude();
            if(isFirstIn){
                //第一次进入的时候获取当前的经纬度
                LatLng latLng = new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                //将定位的效果通过动画传递到百度地图的对象上面
                mBaiduMap.animateMapStatus(msu);
                isFirstIn = false;
                //然后在onstart方法里面开启定位
                //定位成功之后将我的位置谈歘来
                Toast.makeText(context,bdLocation.getAddrStr(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}

