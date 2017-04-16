package com.paul.t29ideagarden2.atys;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.Toast;

import com.paul.t29ideagarden2.R;
import com.paul.t29ideagarden2.bean.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by paul on 4/15/17.
 */

public class LoginActivity extends Activity {
    public static String TAG = "bmob";
    Context context = getApplication();
    private TableLayout tableLayout;
    private ImageView loginImage;

    private EditText account, pwd;
    private CompositeSubscription mCompositeSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
         /* adapt the image to the size of the display */
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Bitmap bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.bakc),size.x,size.y,true);
        loginImage = (ImageView) findViewById(R.id.login_image);
        loginImage.setImageBitmap(bmp);
        account = (EditText) findViewById(R.id.account);
        pwd = (EditText) findViewById(R.id.pwd);

    }

    public void register(View view) {
        final User myUser = new User();
        myUser.setUsername(account.getText().toString());
        myUser.setPassword(pwd.getText().toString());
        addSubscription(myUser.signUp(new SaveListener<User>() {
            @Override
            public void done(User s, BmobException e) {
                if(e==null){
                    toast("注册成功:" +s.toString());
                }else{
                    loge(e);
                }
            }
        }));
    }

    public void login(View view) {
        final User myUser2 = new User();
        myUser2.setUsername(account.getText().toString());
        myUser2.setPassword(pwd.getText().toString());
        myUser2.loginObservable(User.class).subscribe(new Subscriber<User>() {
            @Override
            public void onCompleted() {
                Log.d(TAG,"----onCompleted----");
            }

            @Override
            public void onError(Throwable throwable) {
                Log.d(TAG,"----onError----");
                toast("登录失败,请检查网络");
            }

            @Override
            public void onNext(User user) {
                toast(user.getUsername() + "登陆成功");
                setResult(1);
                finish();
              /*  String username = (String) User.getObjectByKey("username");
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                Config.saveUserName(context,username);
                startActivity(intent);
                finish();*/
            }
        });

    }

    public void logout(View view) {
        User.logOut();
        toast("当前用户退出成功");
    }

    protected void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(s);
    }
    public void toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public static void loge(Throwable e) {
        Log.i(TAG,"===============================================================================");
        if(e instanceof BmobException){
            Log.e(TAG, "错误码："+((BmobException)e).getErrorCode()+",错误描述："+((BmobException)e).getMessage());
        }else{
            Log.e(TAG, "错误描述："+e.getMessage());
        }
    }
}
