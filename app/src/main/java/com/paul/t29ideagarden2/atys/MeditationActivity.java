package com.paul.t29ideagarden2.atys;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import android.util.Log;
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
import com.paul.t29ideagarden2.fragment.CustomBottomSheetDialogFragment;
import com.paul.t29ideagarden2.helper.MonkDatabaseHelper;
import com.paul.t29ideagarden2.presenter.MeditationPresenter;
import com.paul.t29ideagarden2.util.DBUtil;
import com.paul.t29ideagarden2.util.NotificationUtil;
import com.paul.t29ideagarden2.view.IMonkMeditationView;
import com.paul.t29ideagarden2.views.WaveProgress;

import static com.paul.t29ideagarden2.util.Constants.ACTIVITY_REQUEST_FOR_CHANGE_USER_IMG;
import static com.paul.t29ideagarden2.util.Constants.MSG_WHAT_UPDATE_TICK;
import static com.paul.t29ideagarden2.util.Constants.SP_USER_IMG_PATH;
import static com.paul.t29ideagarden2.util.Constants.SP_USER_IMG_PATH_KEY;
import static com.paul.t29ideagarden2.util.Constants.TIME_UP_LIMIT;
import static com.paul.t29ideagarden2.util.DBUtil.dbName;

/**
 * Created by paul on 2018/6/22
 * last modified at 8:48 PM.
 * Desc:
 * 2018-10-21 09:06:14 现在使用sharedPreference来保存图片的路径。
 */
// TODO: 2018/8/8 在数据库内保存完成的丹信息，以及对应的UI更新考虑
// TODO: 2018/10/21 让用户自定义名字，by sharedPreference
public class MeditationActivity extends AppCompatActivity implements IMonkMeditationView{
    private static final String TAG = "tt1";
    private RecyclerView mRecyclerView;
    private WaveProgress mWaveProgress;
    private ImageView headImg;
    private TextView tv_dan_count;
    private ProgressBar mProgressBar;
    private FloatingActionButton fab;
    private List<String> mDatas;
    private Monk mMonk;
    private MeditationPresenter meditationPresenter = new MeditationPresenter(this);
    static Handler handler;
    private NotificationUtil notificationUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation);
        initDatabase();
        Log.e(TAG, "onCreate: database done");

        initData();
        initViews();
    }

    protected void initData()
    {
        mDatas = new ArrayList<String>();
        mDatas.addAll(Arrays.asList(FlowerCard.recyclerViewStrings));
        insertDatabaseData();
        mMonk = getMonk();
    }

    void initViews(){
        SharedPreferences sp = getSharedPreferences(SP_USER_IMG_PATH,MODE_PRIVATE);
        String imgPath = sp.getString("img_path",null);
        tv_dan_count = findViewById(R.id.dan_count);
        tv_dan_count.setText(mMonk.getDanCount()+"");
        headImg = findViewById(R.id.user_profile);
        if (getUserProfile(imgPath)!= null){
            headImg.setImageBitmap(getUserProfile(imgPath));
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
                    case MSG_WHAT_UPDATE_TICK:
                        mProgressBar.setProgress((int)(((msg.arg1 / (TIME_UP_LIMIT * 1f)) * 100)));//tt: now we use 10 min, so 600
                        mWaveProgress.setValue((int)(((msg.arg1 / (TIME_UP_LIMIT * 1f)) * 100)));
                        notificationUtil.updateProgress(100, (int)(((msg.arg1 / (TIME_UP_LIMIT * 1f)) * 100)));
                        break;
                    default:
                        Toast.makeText(getBaseContext(),"handle error",Toast.LENGTH_SHORT).show();
                }
            }
        };
    }


    @Override
    public Handler getHandler(){
        return MeditationActivity.handler;
    }

    //tt ： todo 考虑 我们不再通过数据库获取 monk信息了，希望是通过sharedPreference
    @Override
    public Monk getMonk() {
        Monk monk = new Monk();
        SQLiteDatabase db = new DBUtil().getDatabase(this);
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
        Toast.makeText(this,"静心时间开始",Toast.LENGTH_SHORT).show();
        notificationUtil = new NotificationUtil(this);
        notificationUtil.showNotification(100);
    }

    @Override
    public void finishMeditation() {
        new CustomBottomSheetDialogFragment().show(getSupportFragmentManager(), "Dialog");
        notificationUtil.cancel(100);
        SQLiteDatabase db = new DBUtil().getDatabase(this);
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

    @Override
    public void interruptMeditation(){
        Toast.makeText(this, "未完成本次修行", Toast.LENGTH_SHORT).show();
    }


    private void insertDatabaseData(){
        SQLiteDatabase db = new DBUtil().getDatabase(this);
        if(db == null) {
            initDatabase();
        }
        Log.e(TAG, "insertDatabaseData: here db is" + db );
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
    void initDatabase() {
        SQLiteDatabase db = new MonkDatabaseHelper(MeditationActivity.this, dbName, null, 1).getWritableDatabase();
        Log.e(TAG, "at very fisrt: here db is" + db );

    }

    //tt: 处理用户选择照片作为头像，aty返回时
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ACTIVITY_REQUEST_FOR_CHANGE_USER_IMG && resultCode == RESULT_OK && data != null){
            //天天：获得绝对路径的逻辑，不懂。开始
            Uri selectedImg = data.getData();
            String [] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImg,filePathColumn,null,null,null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            //天天：获得绝对路径的逻辑，不懂。结束。下面存储到sp内
            Toast.makeText(this,"find Img at" + picturePath,Toast.LENGTH_SHORT).show();
            SharedPreferences sp = getSharedPreferences(SP_USER_IMG_PATH, MODE_PRIVATE);
            //tt: apply() 在后台执行
            sp.edit().putString(SP_USER_IMG_PATH_KEY, picturePath).apply();
            headImg.setImageBitmap(getUserProfile(picturePath));
        }
    }
    //便利方法：返回Bitmap
    Bitmap getUserProfile(String filePath){
        Bitmap userProfile = BitmapFactory.decodeFile(filePath);
        if (userProfile != null){
            return userProfile;
        }else {
            return null;
        }

    }


}

