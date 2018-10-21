package com.paul.t29ideagarden2.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import static com.paul.t29ideagarden2.util.Constants.CREATE_MEDITATION_HISTORY_TABLE;
import static com.paul.t29ideagarden2.util.Constants.CREATE_MONK_TABLE;

/**
 * Created by paul on 2018/6/26
 * last modified at 5:15 PM.
 * Desc: 2018-10-21 06:34:47 添加单例模式，懒汉的
 */

public class MonkDatabaseHelper extends SQLiteOpenHelper {
    private Context mContext;

    private static final MonkDatabaseHelper mMonkDatabaseHelper;

    public MonkDatabaseHelper getInstance(Context context) {
        if (mMonkDatabaseHelper == null) {
            mMonkDatabaseHelper = new MonkDatabaseHelper(context);
        }
    }
    private MonkDatabaseHelper(Context context) {
        this(context)
    }
    public MonkDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MONK_TABLE);
        db.execSQL(CREATE_MEDITATION_HISTORY_TABLE);
        Toast.makeText(mContext, "首次净心，已准备好", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
