package com.paul.t29ideagarden2.atys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.paul.t29ideagarden2.R;

public class ToolsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);
    }

    public void btn1Clicked(View view) {
        Intent intent = new Intent(this,EditUserInfoActivity.class);
        startActivity(intent);
    }
}
