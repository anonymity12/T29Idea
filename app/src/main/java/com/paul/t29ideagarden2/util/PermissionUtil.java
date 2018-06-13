package com.paul.t29ideagarden2.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.ActivityCompat;

import static com.paul.t29ideagarden2.util.Constants.PERMISSION_REQUEST_CODE;
import static com.paul.t29ideagarden2.util.Constants.permissions;

/**
 * Created by paul on 2018/6/13
 * last modified at 2:57 PM.
 * Desc:
 */

public class PermissionUtil {
    public static void showDialogTipUserRequestPermission(final Activity activityContext){
        new AlertDialog.Builder(activityContext)
                .setTitle("希望得到一些权限")
                .setMessage("应用需要存储和位置权限来进行记录")
                .setPositiveButton("去开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(activityContext, permissions, PERMISSION_REQUEST_CODE);
                    }
                })
                .setNegativeButton("不开启,退出应用", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activityContext.finish();
                    }
                })
                .setCancelable(false).show();
    }
}
