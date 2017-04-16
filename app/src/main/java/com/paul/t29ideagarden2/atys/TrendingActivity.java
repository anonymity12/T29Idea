package com.paul.t29ideagarden2.atys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.paul.t29ideagarden2.R;
import com.paul.t29ideagarden2.bean.Idea;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class TrendingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending);
        BmobQuery<Idea> query = new BmobQuery<>();
        query.addWhereGreaterThanOrEqualTo("shared",3);
        query.order("-shared");
        query.findObjects(new FindListener<Idea>() {
            @Override
            public void done(List<Idea> list, BmobException e) {
                if (e ==null){
                    for (Idea idea : list){
                        Log.d("TrendingActivity",">>>>>>>>>idea title is "+idea.getTitle());
                    }
                }else {
                    Log.d("Trending Activity",">>>>>>>>.failed to query trending");
                }
            }
        });
    }
}
