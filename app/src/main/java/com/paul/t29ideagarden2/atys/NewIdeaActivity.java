package com.paul.t29ideagarden2.atys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.paul.t29ideagarden2.R;
import com.paul.t29ideagarden2.bean.Idea;
import com.paul.t29ideagarden2.bean.User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class NewIdeaActivity extends AppCompatActivity {

    double latitude, longitude;
    private EditText editText;
    private TextView tv_location;
    User user = BmobUser.getCurrentUser(User.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_idea);
        tv_location = (TextView) findViewById(R.id.tv_location);
        editText = (EditText) findViewById(R.id.et_text);
        latitude = getIntent().getExtras().getDouble("latitude",39.8965);
        longitude = getIntent().getExtras().getDouble("longitude",116.4074);
        tv_location.setText(user.getUsername()+"@"+latitude+","+longitude);//// TODO: 4/20/17 逆地理编码

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.idea_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.finish_idea){
            String originalString = editText.getText().toString();
            if (originalString.length()<40){
                toast("你输入的字数太少");
            }else {
                String title = originalString.substring(0, 14);
                Idea idea = new Idea();
                idea.setContent(originalString);
                idea.setTitle(title);
                idea.setAuthor(user);
                Log.d("NewIdeaActivity", ">>>>>>>User Id is:  " + user.getObjectId());
                idea.setGps(new BmobGeoPoint(longitude, latitude));
                idea.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            toast("成功种下一个思想在" + "\n" + longitude + "\n" + latitude);
                            finish();
                        } else {
                            toast("失败了，错误代码是：" + e);
                        }
                    }
                });

            }
        }
        return super.onOptionsItemSelected(item);
    }
    public void toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
