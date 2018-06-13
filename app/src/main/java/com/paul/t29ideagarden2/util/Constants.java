package com.paul.t29ideagarden2.util;

import android.Manifest;

/**
 * Created by paul on 2018/6/13
 * last modified at 3:00 PM.
 * Desc:
 */

public class Constants {
    public static final String[] permissions = {Manifest.permission.INTERNET,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION};
    public static final int PERMISSION_REQUEST_CODE = 321;
    public static final int ACTIVITY_REQUEST_FOR_SETTING_CODE = 322;
    public static final int ACTIVITY_REQUEST_FOR_REGISTER_USER = 323;

}
