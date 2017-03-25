package com.android.jerroldelayre.portfolio.mapgeo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jerrol on 3/15/2017.
 */

public class DBConnection extends SQLiteOpenHelper{



    public DBConnection(Context context) {
        super(context, null, null, DBEntry.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
