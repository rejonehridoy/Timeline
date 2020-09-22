package com.example.timeline;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.timeline.TimelineDB.Activity;
import com.example.timeline.TimelineDB.PasswordManager;

import androidx.annotation.Nullable;

public class TimelineDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Timeline.db";
    public static final int DATABASE_VERSION = 1;
    public TimelineDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // create activity table
        final String SQL_CREATE_ACTIVITY_TABLE = "CREATE TABLE "+ Activity.TABLE_NAME + " ( "+Activity._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                Activity.COLUMN_USERNAME + " VARCHAR(16) NOT NULL, "+
                Activity.COLUMN_ACTIVITY_TYPE + " VARCHAR(16) NOT NULL, "+
                Activity.COLUMN_NAME + " TEXT NOT NULL, "+
                Activity.COLUMN_CATEGORY + " TEXT, "+
                Activity.COLUMN_PLACE + " TEXT, "+
                Activity.COLUMN_PRICE + " VARCHAR(16), "+
                Activity.COLUMN_DURATION + " VARCHAR(16), "+
                Activity.COLUMN_YEAR + " VARCHAR(8), "+
                Activity.COLUMN_YOUR_TEAM + " TEXT, "+
                Activity.COLUMN_OPPONENT_TEAM + " TEXT, "+
                Activity.COLUMN_LANGUAGE + " VARCHAR(32), "+
                Activity.COLUMN_REVIEW + " VARCHAR(8), "+
                Activity.COLUMN_STATUS + " VARCHAR(32), "+
                Activity.COLUMN_RESULT + " TEXT, "+
                Activity.COLUMN_DATE + " VARCHAR(32), "+
                Activity.COLUMN_TIME + " VARCHAR(16), "+
                Activity.COLUMN_DAYOFWEEK + " VARCHAR(16), "+
                Activity.COLUMN_NOTES + " TEXT, "+
                Activity.COLUMN_MODIFIED_DATE + " VARCHAR(64), "+
                Activity.COLUMN_BACKUP + " VARCHAR(4) );";
        sqLiteDatabase.execSQL(SQL_CREATE_ACTIVITY_TABLE);

        //create Password Manager table
        final String SQL_CREATE_PASSWORD_MANAGER_TABLE = "CREATE TABLE "+PasswordManager.TABLE_NAME+ " ( "+PasswordManager._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                PasswordManager.COLUMN_USERNAME + " VARCHAR(16) NOT NULL, "+
                PasswordManager.COLUMN_ACCOUNT_NAME + " VARCHAR(30), "+
                PasswordManager.COLUMN_ACCOUNT_LOGO + " VARCHAR(25), "+
                PasswordManager.COLUMN_ACCOUNT_USERNAME + " VARCHAR(50), "+
                PasswordManager.COLUMN_ACCOUNT_EMAIL + " VARCHAR(64), "+
                PasswordManager.COLUMN_ACCOUNT_PHONE + " VARCHAR(32), "+
                PasswordManager.COLUMN_ACCOUNT_PASSWORD + " TEXT, "+
                PasswordManager.COLUMN_ACCOUNT_PRIORITY + " VARCHAR(16), "+
                PasswordManager.COLUMN_ACCOUNT_NOTES + " TEXT, "+
                PasswordManager.COLUMN_ACCOUNT_CREATED_DATE + " VARCHAR(32), "+
                PasswordManager.COLUMN_ACCOUNT_MODIFIED_DATE + " VARCHAR(32) );";
        sqLiteDatabase.execSQL(SQL_CREATE_PASSWORD_MANAGER_TABLE);

        final String SQL_CREATE_NOTES_TABLE = "CREATE TABLE "+ TimelineDB.Notes.TABLE_NAME + " ( "+ TimelineDB.Notes._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                TimelineDB.Notes.COLUMN_USERNAME + " VARCHAR(16) NOT NULL, "+
                TimelineDB.Notes.COLUMN_SUBJECT + " TEXT NOT NULL, "+
                TimelineDB.Notes.COLUMN_DESCRIPTION + " TEXT, "+
                TimelineDB.Notes.COLUMN_DATE + " VARCHAR(15), "+
                TimelineDB.Notes.COLUMN_TIME + " VARCHAR(10), "+
                TimelineDB.Notes.COLUMN_DAYOFWEEK+ " VARCHAR(10), "+
                TimelineDB.Notes.COLUMN_MODIFIED_DATE + " VARCHAR(10), "+
                TimelineDB.Notes.COLUMN_FAVORITE + " VARCHAR(5), "+
                TimelineDB.Notes.COLUMN_BACKUP + " VARCHAR(5) );";
        sqLiteDatabase.execSQL(SQL_CREATE_NOTES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Activity.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+PasswordManager.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
