package com.skopincev.testtask.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import javax.inject.Inject;

/**
 * Created by skopi on 16.06.2017.
 */


public class DBHelper extends SQLiteOpenHelper{

    // Database name
    public static final String DATABASE_NAME = "LocalDB";

    // Database version
    private static final int DATABASE_VERSION = 1;

    @Inject
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBContract.ContactContract.CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DBContract.ContactContract.DROP);
        onCreate(db);
    }
}
