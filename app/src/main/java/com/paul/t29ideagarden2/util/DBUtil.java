package com.paul.t29ideagarden2.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.paul.t29ideagarden2.helper.MonkDatabaseHelper;

import static com.paul.t29ideagarden2.MyApplication.getContext;

/**
 * Created by paul on 2018/10/21
 * last modified at 17:33.
 * Desc: 垃圾单例模式
 * u can get a database just by call: DBUtil.getDatabase();
 */

public class DBUtil {
    public static String dbName = "MonkRecord.db";
    private static DBUtil mDBUtil;
    private static SQLiteDatabase mDatabase;
    public DBUtil() {}

    public SQLiteDatabase getDatabase(Context atyContext) {
        if (mDBUtil == null) {
            mDBUtil = new DBUtil(atyContext);//tt: 数据库取application的上下文，故生命周期很长。
        }
        return mDatabase;
    }

    private DBUtil(Context atyContext) {
        MonkDatabaseHelper dbHelper = new MonkDatabaseHelper(atyContext, dbName, null, 1);
        mDatabase = dbHelper.getWritableDatabase();
    }
}
