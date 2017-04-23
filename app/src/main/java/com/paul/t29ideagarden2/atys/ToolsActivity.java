package com.paul.t29ideagarden2.atys;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.paul.t29ideagarden2.R;
import com.paul.t29ideagarden2.views.LoginDialogFragment;

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

    public void btn2Clicked(View view) {
        LoginDialogFragment loginDialogFragment = new LoginDialogFragment();
        loginDialogFragment.show(getFragmentManager(),"showMapChooser");

    }


    public void btn3Clicked(View view) {
        Intent intent = new Intent(this,MyFieldActivity.class);
        intent.putExtra("flag",true);
        startActivity(intent);
    }
}
