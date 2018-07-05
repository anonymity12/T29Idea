package com.paul.t29ideagarden2.atys;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.paul.t29ideagarden2.R;
import com.paul.t29ideagarden2.bean.FlowerCard;
import com.paul.t29ideagarden2.bean.Monk;
import com.paul.t29ideagarden2.helper.MonkDatabaseHelper;
import com.paul.t29ideagarden2.presenter.MeditationPresenter;
import com.paul.t29ideagarden2.view.IMonkMeditationView;

/**
 * Created by paul on 2018/6/22
 * last modified at 8:48 PM.
 * Desc:
 */

public class MeditationActivity extends AppCompatActivity implements IMonkMeditationView{
    private RecyclerView mRecyclerView;
    private FloatingActionButton fab;
    private List<String> mDatas;
    private HomeAdapter mAdapter;
    private Monk mMonk;
    private MonkDatabaseHelper monkDatabaseHelper;
    private MeditationPresenter meditationPresenter = new MeditationPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation);
        

        initData();
        initViews();


    }
    void initViews(){
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,1));
        mRecyclerView.setAdapter(mAdapter = new HomeAdapter());
        fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meditationPresenter.meditation();
            }
        });

    }

    protected void initData()
    {
        monkDatabaseHelper = new MonkDatabaseHelper(this,"MonkRecord.db",null,1);
        mDatas = new ArrayList<String>();
        mDatas.addAll(Arrays.asList(FlowerCard.recyclerViewStrings));
        insertDatabaseData();
        mMonk = getMonk();
    }

    @Override
    public Monk getMonk() {
        Monk monk = new Monk();
        SQLiteDatabase db = monkDatabaseHelper.getWritableDatabase();
        Cursor cursor = db.query("Monk",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            String name = cursor.getString(cursor.getColumnIndex("monk_name"));
            String monk_img_path = cursor.getString(cursor.getColumnIndex("monk_img_path"));
            int level = cursor.getInt(cursor.getColumnIndex("monk_level"));
            int danCount = cursor.getInt(cursor.getColumnIndex("monk_dan_count"));
            monk.setDanCount(danCount);
            monk.setLevel(level);
            monk.setImgPath(monk_img_path);
            monk.setName(name);
            return monk;
        }
        return null;
    }

    @Override
    public void beginMeditation() {
        Toast.makeText(this,"开始修行",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finishMeditation() {
        SQLiteDatabase db = monkDatabaseHelper.getWritableDatabase();
        Cursor cursor = db.query("Monk",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            int danCount = cursor.getInt(cursor.getColumnIndex("monk_dan_count"));
            mMonk.setDanCount(danCount);
        }
        ContentValues cv = new ContentValues();
        cv.put("monk_dan_count",mMonk.getDanCount()+1);
        db.update("Monk",cv,"monk_name = ?",new String[]{mMonk.getName()});
        Toast.makeText(this, "顺利完成本次修行,丹数量："+(mMonk.getDanCount()+1), Toast.LENGTH_SHORT).show();
    }

    public void interruptMeditation(){
        Toast.makeText(this, "未完成本次修行", Toast.LENGTH_SHORT).show();
    }


    private void insertDatabaseData(){
        SQLiteDatabase db = monkDatabaseHelper.getWritableDatabase();
        Cursor cursor = db.query("Monk",null,null,null,null,null,null);
        if (!cursor.moveToFirst()) {
            ContentValues cv = new ContentValues();
            cv.put("monk_name", "Paul");
            cv.put("monk_img_path", "/sdcard/Picture");
            cv.put("monk_level", 12);
            cv.put("monk_dan_count", 34);
            db.insert("Monk", null, cv);
        }

    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>
    {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    MeditationActivity.this).inflate(R.layout.item_home, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position)
        {
            holder.tv.setText(mDatas.get(position));
        }

        @Override
        public int getItemCount()
        {
            return mDatas.size();
        }

        class MyViewHolder extends ViewHolder
        {

            TextView tv;

            public MyViewHolder(View view)
            {
                super(view);
                tv = (TextView) view.findViewById(R.id.id_num);
            }
        }
    }

}

