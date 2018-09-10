package com.paul.t29ideagarden2.atys;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.paul.t29ideagarden2.MainActivity;
import com.paul.t29ideagarden2.R;
import com.paul.t29ideagarden2.bean.FlowerCard;
import com.paul.t29ideagarden2.bean.Monk;
import com.paul.t29ideagarden2.helper.MonkDatabaseHelper;
import com.paul.t29ideagarden2.presenter.MeditationPresenter;
import com.paul.t29ideagarden2.view.IMonkMeditationView;
import com.paul.t29ideagarden2.views.WaveProgress;

import static com.paul.t29ideagarden2.util.Constants.ACTIVITY_REQUEST_FOR_CHANGE_USER_IMG;

/**
 * Created by paul on 2018/6/22
 * last modified at 8:48 PM.
 * Desc:
 */
// TODO: 2018/8/8 在数据库内保存完成的丹信息，以及对应的UI更新考虑 
public class MeditationActivity extends AppCompatActivity implements IMonkMeditationView{
    private RecyclerView mRecyclerView;
    private WaveProgress mWaveProgress;
    private ImageView headImg;
    private TextView tv_dan_count;
    private ProgressBar mProgressBar;
    private FloatingActionButton fab;
    private List<String> mDatas;
    private Monk mMonk;
    private MonkDatabaseHelper monkDatabaseHelper;
    private MeditationPresenter meditationPresenter = new MeditationPresenter(this);
    static Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation);

        initData();
        initViews();
    }

    protected void initData()
    {
        monkDatabaseHelper = new MonkDatabaseHelper(this,"MonkRecord.db",null,1);
        mDatas = new ArrayList<String>();
        mDatas.addAll(Arrays.asList(FlowerCard.recyclerViewStrings));
        insertDatabaseData();
        mMonk = getMonk();
    }

    void initViews(){
        tv_dan_count = findViewById(R.id.dan_count);
        tv_dan_count.setText(mMonk.getDanCount()+"");
        headImg = findViewById(R.id.user_profile);
        if (getUserProfile(mMonk.getImgPath())!= null){
            headImg.setImageBitmap(getUserProfile(mMonk.getImgPath()));
        }
        headImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPicIntent = new Intent("android.intent.action.PICK");
                pickPicIntent.setType("image/*");
                startActivityForResult(pickPicIntent,ACTIVITY_REQUEST_FOR_CHANGE_USER_IMG);
            }
        });
        mProgressBar = findViewById(R.id.progressBar);
        mWaveProgress = findViewById(R.id.wave_progress_bar);
        fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meditationPresenter.meditation();
            }
        });
        handler =  new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        mProgressBar.setProgress((int)(100 - ((msg.arg1 / 60.0) * 100)));//tt: now we use 1 min, so 60
                        mWaveProgress.setValue((int)(100 - ((msg.arg1 / 60.0) * 100)));
                        break;
                    default:
                        Toast.makeText(getBaseContext(),"handle error",Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    Bitmap getUserProfile(String filePath){
        Bitmap userProfile = BitmapFactory.decodeFile(filePath);
        if (userProfile != null){
            return userProfile;
        }else {
            return null;
        }

    }
    @Override
    public Handler getHandler(){
        return MeditationActivity.handler;
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
            cursor.close();
            return monk;
        }
        cursor.close();
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
            mMonk.setDanCount(danCount+1);
        }
        ContentValues cv = new ContentValues();
        cv.put("monk_dan_count",mMonk.getDanCount());
        db.update("Monk",cv,"monk_name = ?",new String[]{mMonk.getName()});
        Toast.makeText(this, "顺利完成本次修行,丹数量："+(mMonk.getDanCount()), Toast.LENGTH_SHORT).show();
        tv_dan_count.setText(mMonk.getDanCount()+"");
        cursor.close();
    }

    public void interruptMeditation(){
        Toast.makeText(this, "未完成本次修行", Toast.LENGTH_SHORT).show();
    }


    private void insertDatabaseData(){
        SQLiteDatabase db = monkDatabaseHelper.getWritableDatabase();
        Cursor cursor = db.query("Monk",null,null,null,null,null,null);
        //tt: only find no result, we execute db.insert();
        if (!cursor.moveToFirst()) {
            ContentValues cv = new ContentValues();
            cv.put("monk_name", "Paul");
            cv.put("monk_img_path", "/sdcard/Picture");
            cv.put("monk_level", 12);
            cv.put("monk_dan_count", 34);
            db.insert("Monk", null, cv);
        }
        cursor.close();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SQLiteDatabase db = monkDatabaseHelper.getWritableDatabase();
        if(requestCode == ACTIVITY_REQUEST_FOR_CHANGE_USER_IMG && resultCode == RESULT_OK && data != null){
            Uri selectedImg = data.getData();
            String [] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImg,filePathColumn,null,null,null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Toast.makeText(this,"find Img at" + picturePath,Toast.LENGTH_SHORT).show();
            ContentValues cv = new ContentValues();
            cv.put("monk_img_path",picturePath);
            db.update("Monk",cv,"monk_name = ?",new String[]{mMonk.getName()});//tt: we use getMonk().getName() later.
            headImg.setImageBitmap(getUserProfile(mMonk.getImgPath()));
        }
    }

}

