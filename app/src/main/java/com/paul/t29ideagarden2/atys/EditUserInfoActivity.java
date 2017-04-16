package com.paul.t29ideagarden2.atys;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.paul.t29ideagarden2.R;
import com.paul.t29ideagarden2.bean.Idea;
import com.paul.t29ideagarden2.bean.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/*
* ShowUserInfoActivity, actually.
* 实际上这还没有支持编辑用户资料的功能，只是漂亮的查看和展示之用，
* 这里面的代码，特别是onTouchEvent（）需要好好查看。
* */
public class EditUserInfoActivity extends Activity {
    LinearLayout myscrollLinearlayout;
    LinearLayout mainheadview; //顶部个人资料视图
    RelativeLayout mainactionbar; //顶部菜单栏
    private ProgressDialog progressDialog;
    private TextView userName;
    private TextView userIdeaCount,userGoodIdeaCount;
    private MyStatics myStatics = new MyStatics();


    BmobUser bmobUser = BmobUser.getCurrentUser(User.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit_user_info);
        userName = (TextView) findViewById(R.id.user_name);
        userIdeaCount = (TextView) findViewById(R.id.user_idea_count);
        userGoodIdeaCount = (TextView) findViewById(R.id.user_good_idea_count);
        Log.d("EditUserInfo",">>>>>>>>>TextView userIdeaCount is"+userIdeaCount);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("正在找你的信息");
        progressDialog.setMessage("拼命加载中。。。");
        progressDialog.show();
//        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(bmobUser != null){
            // 允许用户使用应用
            //查询用户的Idea表，并开始下一步的initView（）；
            BmobQuery<Idea> bmobQuery = new BmobQuery<>();
            bmobQuery.addWhereEqualTo("author", bmobUser);
            bmobQuery.order("-updateAt");
            bmobQuery.findObjects(new FindListener<Idea>() {
                @Override
                public void done(List<Idea> list, BmobException e) {
                    if (e == null) {
//                        Log.d("EditUserActivity", ">>>>>>>>>>>>list size is:" + list.size());
                        myStatics.setIdeaCount(list.size());
                        Integer tempSharedCount=0;
                        for (Idea idea:list){
                            tempSharedCount =+ idea.getShared();
                        }
                        myStatics.setIdeaSharedCount(tempSharedCount);

                        Log.d("EditUserActivity", ">>>>>>>>>>>>temp Shared Idea Count is :" + myStatics.getIdeaSharedCount());
                        progressDialog.dismiss();
                        initView();
                    } else {
                        Log.d("EditUserActivity", "查idea失败:" + e);
                        Toast.makeText(EditUserInfoActivity.this, "查idea失败:" + e, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });
        }else{
            progressDialog.dismiss();
// 缓存用户对象为空时, 可打开用户注册界面...
            Toast.makeText(this,"你可能没有登录吧？来登录一下吧",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public void goToLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    int Y;
    int position = 0; //拖动Linearlayout的距离Y轴的距离
    int scrollviewdistancetotop = 0; //headView的高
    int menubarHeight = 0;
    int chufaHeight = 0; //需要触发动画的高
    float scale; //像素密度
    int headViewPosition = 0;
    ImageView userinfo_topbar;
    static boolean flag = true;
    static boolean topmenuflag = true;


    /*
    * UI initialize
    * */
    private void initView() {
        userinfo_topbar = (ImageView) findViewById(R.id.userinfo_topbar);
//获得像素密度
        scale = this.getResources().getDisplayMetrics().density;
        mainheadview = (LinearLayout) findViewById(R.id.mainheadview);
        mainactionbar = (RelativeLayout) findViewById(R.id.mainactionbar);
        menubarHeight = (int) (55 * scale);
        chufaHeight = (int) (110 * scale);
        scrollviewdistancetotop = (int) ((260 )*scale);
        position = scrollviewdistancetotop;
        userName.setText(bmobUser.getUsername());
        try {
            String string =myStatics.getIdeaCount().toString();
            String stringGoodIdeaCount = myStatics.getIdeaSharedCount()+"";
            Log.d("EditUserActivity", ">>>>>>>>>>>>temp Shared Idea Count is :" + myStatics.getIdeaSharedCount());
            userIdeaCount.setText(string);
            userGoodIdeaCount.setText(stringGoodIdeaCount);


        }catch (Exception e){
            e.printStackTrace();
        }

        //// TODO: 4/14/17 补充点击回调事件，仿照T25RecyclerView的


        myscrollLinearlayout = (LinearLayout) findViewById(R.id.myscrollLinearlayout);
        myscrollLinearlayout.setY( scrollviewdistancetotop); //要减去Absolote布局距离顶部的高度
        myscrollLinearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        myscrollLinearlayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
//按下的Y的位置
                        Y = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int nowY = (int) myscrollLinearlayout.getY(); //拖动界面的Y轴位置
                        int tempY = (int) (event.getRawY() - Y); //手移动的偏移量
                        Y = (int) event.getRawY();
                        if ((nowY + tempY >= 0) && (nowY + tempY <= scrollviewdistancetotop)) {
                            if ((nowY + tempY <= menubarHeight)&& (topmenuflag == true) ){
                                userinfo_topbar.setVisibility(View.VISIBLE);
                                topmenuflag = false;
                            } else if ((nowY + tempY > menubarHeight) && (topmenuflag == flag)) {
                                userinfo_topbar.setVisibility(View.INVISIBLE);
                                topmenuflag = true;
                            }
                            int temp = position += tempY;
                            myscrollLinearlayout.setY(temp);
                            int headviewtemp = headViewPosition += (tempY/5);
                            mainheadview.setY(headviewtemp);
                        }
//顶部的动画效果
                        if ((myscrollLinearlayout.getY() <= chufaHeight) && (flag == true)) {
                            ObjectAnimator anim = ObjectAnimator.ofFloat(mainheadview, "alpha", 1, 0.0f);
                            anim.setDuration(500);
                            anim.start();
                            flag = false;
                        } else if ((myscrollLinearlayout.getY() > chufaHeight + 40) && (flag == false)) {
                            ObjectAnimator anim = ObjectAnimator.ofFloat(mainheadview, "alpha", 0.0f, 1f);
                            anim.setDuration(500);
                            anim.start();
                            flag = true;
                        }
                        break;
                }
                return false;
            }
        });
    }

    public void finishWatch(View view) {
        finish();
    }

    class MyStatics{
        public Integer getIdeaCount() {
            return ideaCount;
        }
        public void setIdeaCount(Integer ideaCount) {
            this.ideaCount = ideaCount;
        }
        Integer ideaCount=0;

        public Integer getIdeaSharedCount() {
            return ideaSharedCount;
        }

        public void setIdeaSharedCount(Integer ideaSharedCount) {
            this.ideaSharedCount = ideaSharedCount;
        }

        Integer ideaSharedCount = 0;
    }
}
