package com.igor.vetrov.artzonaevents.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class VkBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "tokenBase.db";

    public VkBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + VkDbSchema.TokenTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                VkDbSchema.TokenTable.Cols.ID + ", " +
                VkDbSchema.TokenTable.Cols.TOKEN + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
