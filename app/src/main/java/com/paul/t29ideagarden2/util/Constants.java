package com.paul.t29ideagarden2.util;

import android.Manifest;

/**
 * Created by paul on 2018/6/13
 * last modified at 3:00 PM.
 * Desc:
 */

public class Constants {
    public static final String[] permissions = {Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION};
    public static final String SP_USER_IMG_PATH = "user_img_path";
    public static final String SP_USER_IMG_PATH_KEY = "user_img_path_key";
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
    public static final int TIME_UP_LIMIT = (1 * 60);//tt: 10 min will work for us!!
    public static final int MSG_WHAT_UPDATE_TICK = 1;




    public static String img_path = "";//tt？？del？
    public static final String CREATE_MONK_TABLE = "create table if not exists Dan("
            + "monk_name text,"
            + "type integer,"
            + "desc text,"
            + "img_addon text,"
            + "time integer)";

}
