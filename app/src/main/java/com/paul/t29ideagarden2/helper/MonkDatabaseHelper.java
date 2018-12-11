package com.paul.t29ideagarden2.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import static com.paul.t29ideagarden2.util.Constants.CREATE_MONK_TABLE;

/**
 * Created by paul on 2018/6/26
 * last modified at 5:15 PM.
 * Desc: 2018-10-21 06:34:47 添加单例模式，懒汉的
 */

public class MonkDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "tt1";
    private Context mContext;

    public MonkDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MONK_TABLE);
        ContentValues cv = new ContentValues();
        cv.put("monk_name", "Paul");
        cv.put("monk_img_path", "/sdcard/Picture");
        cv.put("monk_level", 12);
        cv.put("monk_dan_count", 34);
        db.insert("Monk", null, cv);
        Log.e(TAG, "in helper: here db is" + db );

        Toast.makeText(mContext, "首次净心，已准备好", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
