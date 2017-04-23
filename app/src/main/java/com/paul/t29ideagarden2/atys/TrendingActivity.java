package com.paul.t29ideagarden2.atys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.paul.t29ideagarden2.R;
import com.paul.t29ideagarden2.adapter.MyRecyclerAdapter;
import com.paul.t29ideagarden2.bean.Idea;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class TrendingActivity extends AppCompatActivity {

    List<Idea> ideasList;
    private MyRecyclerAdapter myRecyclerAdapter;
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
                    ideasList =list;
                    for (Idea idea : list){
                        Log.d("TrendingActivity",">>>>>>>>>idea title is "+idea.getTitle());

                    }
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerList);
                    LinearLayoutManager llm= new LinearLayoutManager(TrendingActivity.this);
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(llm);
                    myRecyclerAdapter = new MyRecyclerAdapter(ideasList);
                    recyclerView.setAdapter(myRecyclerAdapter);
                    initEvent();
                }else {
                    Log.d("Trending Activity",">>>>>>>>.failed to query trending");
                }
            }
        });
    }
    private void initEvent(){
        myRecyclerAdapter.setOnItemClickListener(new MyRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String objectId = ideasList.get(position).getObjectId();
                Intent intent = new Intent(TrendingActivity.this,SeeIdeaActivity.class);
                intent.putExtra("ideaObjectId",objectId);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(TrendingActivity.this,"长按功能还没开发，你希望添加什么功能呢？",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
