package com.android.jerroldelayre.portfolio.taskwatcher.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Jerrol on 4/6/2017.
 */

public class Database extends SQLiteOpenHelper implements BaseColumns {

    private static final String DATABASE_NAME = "Database.db";
    private static final int DATABASE_VERSION = 1;

    public interface TABLE {
        String PROJECT = "PROJECT";
        String TASKS = "TASKS";
    }

    public interface TBL_PROJECT {
        String TITLE = "PROJECT_TITLE";
        String DESCRIPTION = "PROJECT_DESC";
    }
    public interface TBL_TASKS {
        String PROJECT_ID = "PROJECT_ID";
        String TITLE = "TASK_TITLE";
        String DESCRIPTION = "TASK_DESC";
        String DUE_DATE = "TASK_DUE_DATE";
    }

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE.PROJECT + "("
                + Database._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TBL_PROJECT.TITLE + " TEXT,"
                + TBL_PROJECT.DESCRIPTION + " TEXT)"
        );

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE.TASKS + "("
                + Database._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TBL_TASKS.TITLE + " TEXT,"
                + TBL_TASKS.DESCRIPTION + " TEXT,"
                + TBL_TASKS.DUE_DATE + " TEXT,"
                + TBL_TASKS.PROJECT_ID + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DELETE TABLE IF EXISTS " + TABLE.PROJECT);
        db.execSQL("DELETE TABLE IF EXISTS " + TABLE.TASKS);
    }

    private SQLiteDatabase dbWrite() { return this.getWritableDatabase(); }
    private SQLiteDatabase dbRead() { return this.getReadableDatabase(); }

    public long insertProject(String title, String description) {
        ContentValues values = new ContentValues();
        values.put(TBL_PROJECT.TITLE, title);
        values.put(TBL_PROJECT.DESCRIPTION, description);

        return dbWrite().insert(TABLE.PROJECT, null, values);
    }

    public long insertTask(String projectId, String title, String description, String due_date) {
        ContentValues values = new ContentValues();
        values.put(TBL_TASKS.PROJECT_ID, projectId);
        values.put(TBL_TASKS.TITLE, title);
        values.put(TBL_TASKS.DESCRIPTION, description);
        values.put(TBL_TASKS.DUE_DATE, due_date);

        return dbWrite().insert(TABLE.TASKS, null, values);
    }

    public Cursor getProjectList() {
        Cursor cursor = dbRead().query(TABLE.PROJECT, null, null, null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }
}
