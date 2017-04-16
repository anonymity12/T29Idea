package com.paul.t29ideagarden2.atys;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.paul.t29ideagarden2.R;
import com.paul.t29ideagarden2.bean.Idea;
import com.paul.t29ideagarden2.bean.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MyFieldActivity extends Activity {

    private AMap aMap;
    MapView mMapView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_field);
        mMapView = (MapView) findViewById(R.id.field_map);
        mMapView.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        if (aMap == null){
            aMap = mMapView.getMap();
            setUpMap();
        }
    }

    private void setUpMap() {
        User user = BmobUser.getCurrentUser(User.class);
        BmobQuery<Idea> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("author",user);
        bmobQuery.order("-updatedAt");
        bmobQuery.include("author");

        bmobQuery.setLimit(20);
        Log.d("MyFiledActivity", ">>>>>>>>>>>>>>>>>ready for search");
        bmobQuery.findObjects(new FindListener<Idea>() {
            @Override
            public void done(List<Idea> list, BmobException e) {
                if (e == null){
                    double tempLatitude, tempLongitude;
                    LatLng latLng;
                    AMap aMap = mMapView.getMap();
                    Idea tempIdea;
                    for(int i = 0; i<list.size();i++){
                        tempIdea = list.get(i);
                        Log.d("MyFieldActivity",">>>>>>>>>>>>>>>each tempIdea Id is:"+tempIdea.getObjectId());
                        tempLatitude = list.get(i).getGps().getLatitude();
                        tempLongitude = list.get(i).getGps().getLongitude();
                        latLng = new LatLng(tempLatitude,tempLongitude);
                        aMap.addMarker(new MarkerOptions().position(latLng).title(list.get(i).getTitle()).snippet("ID:"+list.get(i).getObjectId()));
                    }
                }
            }
        });


    }
    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
}
