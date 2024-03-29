package com.example.airport.ui.main;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final int VERSION=1;
    private static final String DB_NAME="flight.db";  //数据库名
    public static final String TB_NAME="tb_flight";   //表名
    public static final String TB_NAME2="tb_collect";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
    }

    public DBHelper(Context context){
        super(context,DB_NAME,null,VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TB_NAME+"(ID TEXT PRIMARY KEY, ORIGINAL TEXT, DESTINATION TEXT, PASS TEXT, PLANNED TEXT, TERMINAL TEXT, STATUS TEXT)");
        db.execSQL("CREATE TABLE "+TB_NAME2+"(ID TEXT PRIMARY KEY, ORIGINAL TEXT, DESTINATION TEXT, PASS TEXT, PLANNED TEXT, TERMINAL TEXT, STATUS TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}
