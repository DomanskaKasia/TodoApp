package com.example.todoapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class AppDatabase extends SQLiteOpenHelper {
    private static final String TAG = "Database";

    private static final String DB_NAME = "todo.db";
    private static final int DB_VERSION = 1;

    //singleton implementation
    private static AppDatabase instance = null;

    private AppDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(TAG, "AppDatabase: constructor");
    }

    //get an instance of singleton database implementation
    static AppDatabase getInstance(Context context) {
        if(instance == null) {
            Log.d(TAG, "getInstance: creating new instance");
            instance = new AppDatabase(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: starts");

        String sql;
        sql = "CREATE TABLE " + TasksTable.TABLE_NAME + " (" +
                TasksTable.Column._ID + " INTEGER PRIMARY KEY NOT NULL, " +
                TasksTable.Column.NAME + " TEXT NOT NULL, " +
                TasksTable.Column.END_DATE + " TEXT NOT NULL, " +
                TasksTable.Column.CATEGORY + " TEXT NOT NULL);";
        Log.d(TAG, sql);
        db.execSQL(sql);

        Log.d(TAG, "onCreate: ends");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: starts");
        switch (oldVersion) {
            case 1:
                break;
            default:
                throw new IllegalStateException("Unknown new version " + newVersion);
        }
        Log.d(TAG, "onUpgrade: ends");
    }
}
