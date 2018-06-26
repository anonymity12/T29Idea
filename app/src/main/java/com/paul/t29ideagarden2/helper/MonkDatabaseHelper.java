package com.paul.t29ideagarden2.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import static com.paul.t29ideagarden2.util.Constants.CREATE_MONK_TABLE;

/**
 * Created by paul on 2018/6/26
 * last modified at 5:15 PM.
 * Desc:
 */

public class MonkDatabaseHelper extends SQLiteOpenHelper {
    private Context mContext;

    public MonkDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MONK_TABLE);
        Toast.makeText(mContext, "首次精心，已准备好", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
