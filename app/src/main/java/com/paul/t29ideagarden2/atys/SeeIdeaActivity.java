package com.paul.t29ideagarden2.atys;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.paul.t29ideagarden2.R;
import com.paul.t29ideagarden2.bean.Idea;
import com.paul.t29ideagarden2.bean.User;
import com.zzhoujay.richtext.RichText;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class SeeIdeaActivity extends AppCompatActivity {

    private TextView textView,ideaDetails,ideaShared;
    //注意，之后应该理解并使用StringBuilder来打造Idea.content这个String；
    private String ideaObjectId,content,title,ideaOwner="owner",location="location",ideaTime="2017-4-15";
    private Integer shared,hated;

    private User ownerUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Bmob.initialize(this, "6b5c101fa6f907f9f68e35d24b2344d4");

        ideaObjectId = getIntent().getExtras().getString("ideaObjectId",null);
        Log.d("SeeIdeaActivity","the idea id is: "+ideaObjectId);

        RichText.initCacheDir(this);

        BmobQuery<Idea> query = new BmobQuery<>();
        query.include("author");
        query.getObject(ideaObjectId, new QueryListener<Idea>() {
            @Override
            public void done(Idea idea, BmobException e) {
                if (e == null) {
                    content = idea.getContent();
                    Log.d("SeeIdeaActivity","the content is: "+content);
                    title = idea.getTitle();
                    shared = idea.getShared();
                    ownerUser = idea.getAuthor();
                    Log.d("SeeIdeaActivity","the ideaOwner is: "+ownerUser);
                    ideaOwner = ownerUser.getObjectId();
                    Log.d("SeeIdeaActivity","the ideaOwnerId is: "+ideaOwner);
                    shared = idea.getShared();

                    location = idea.getGps().getLatitude()+","+idea.getGps().getLongitude();
                    ideaTime = idea.getCreatedAt();

                    ideaDetails.setText("@" + location + "(" + ideaTime + ")");
                    final String sharedCountString = "+"+shared;
                    BmobQuery<User> userBmobQuery =new BmobQuery<User>();
                    userBmobQuery.getObject(ideaOwner, new QueryListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            String countAndUser = user.getUsername();
                            countAndUser+=" "+sharedCountString;
                            ideaShared.setText(countAndUser);
                        }
                    });

                    setTitle(title);
                    RichText.fromMarkdown(content).into(textView);
                    Log.d("SeeIdeaActivity",">>>>>>>>>>>>idea Shared is : "+idea.getShared());
                    Log.d("SeeIdeaActivity",">>>>>>>>>>>>TextView Share is found as : "+ideaShared);

                }else {
                    e.printStackTrace();
                    Log.d("SeeIdeaActivity","the error is: "+e.getMessage()+e.getErrorCode());

                }
            }

        });
        setContentView(R.layout.activity_see_idea);
        textView = (TextView) findViewById(R.id.text_markdown);
        ideaDetails = (TextView) findViewById(R.id.idea_details);
        ideaShared = (TextView) findViewById(R.id.idea_shared_count);
        Log.d("SeeIdeaActivity",">>>>>>>>>>>>TextView Share is found as(outside) : "+ideaShared);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.see_idea, menu);
        return true;
    }

    public void toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_shared){
            Idea idea = new Idea();
            idea.setShared(++shared);
            idea.update(ideaObjectId, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null){
                        toast("你认可了这个思想，\n 对方思想花园繁荣度+1");
                    }else {
                        toast("认可这个思想失败，请稍后再试，或反馈给作者bug");
                    }

                }
            });
        }
        return super.onOptionsItemSelected(item);
    }
}
