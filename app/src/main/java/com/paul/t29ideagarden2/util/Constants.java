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
    public static final int ACTIVITY_REQUEST_FOR_CHANGE_USER_IMG = 324;
    public static final int DEFAULT_SIZE = 150;
    public static final int DEFAULT_ANIM_TIME = 1000;
    public static final int DEFAULT_VALUE_SIZE = 15;
    public static final int DEFAULT_HINT_SIZE = 15;
    public static final int DEFAULT_UNIT_SIZE = 30;
    public static final int DEFAULT_ARC_WIDTH = 15;
    public static final int DEFAULT_WAVE_HEIGHT = 40;




    public static String img_path = "";
    public static final String CREATE_MONK_TABLE = "create table Monk("
            + "monk_name text,"
            + "monk_img_path text,"
            + "monk_level integer,"
            + "monk_dan_count integer)";
}
