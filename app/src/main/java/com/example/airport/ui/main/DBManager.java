package com.example.airport.ui.main;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private DBHelper dbHelper;
    private String TBNAME;
    private String TBNAME2;

    public DBManager(Context context){
        dbHelper=new DBHelper(context);
        TBNAME=DBHelper.TB_NAME;
        TBNAME2=DBHelper.TB_NAME2;
    }


    public void addAll(List<FlightItem> list){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        for (FlightItem item:list){
            ContentValues values=new ContentValues();
            values.put("ID", item.getId());
            values.put("original", item.getOriginal());
            values.put("destination", item.getDestination());
            values.put("pass", item.getPass());
            values.put("planned", item.getPlanned());
            values.put("terminal", item.getTerminal());
            values.put("status", item.getStatus());
            db.insert(TBNAME, null, values);
        }
        db.close();
    }

    public void add(FlightItem item){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("ID", item.getId());
        values.put("original", item.getOriginal());
        values.put("destination", item.getDestination());
        values.put("pass", item.getPass());
        values.put("planned", item.getPlanned());
        values.put("terminal", item.getTerminal());
        values.put("status", item.getStatus());
        db.insert(TBNAME2, null, values);
        db.close();
    }

    public void deleteAll(){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.delete(TBNAME, null, null);
        db.close();
    }

    public void delete(int id){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.delete(TBNAME, "ID=?",new String[]{String.valueOf(id)});
        db.close();
    }

    public void delete_collect(String id){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.delete(TBNAME2, "ID=?",new String[]{String.valueOf(id)});
        db.close();
    }



    public FlightItem findById(String id){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor cursor=db.query(TBNAME, null, "ID=?", new String[]{id}, null, null, null);
        FlightItem flightItem=null;
        if (cursor!=null&&cursor.moveToFirst()){
            flightItem =new FlightItem();
            flightItem.setId(cursor.getString(cursor.getColumnIndex("ID")));
            flightItem.setOriginal(cursor.getString(cursor.getColumnIndex("ORIGINAL")));
            flightItem.setDestination(cursor.getString(cursor.getColumnIndex("DESTINATION")));
            flightItem.setPass(cursor.getString(cursor.getColumnIndex("PASS")));
            flightItem.setPlanned(cursor.getString(cursor.getColumnIndex("PLANNED")));
            flightItem.setTerminal(cursor.getString(cursor.getColumnIndex("TERMINAL")));
            flightItem.setStatus(cursor.getString(cursor.getColumnIndex("STATUS")));
            cursor.close();
        }
        db.close();
        return flightItem;
    }

    public ArrayList<FlightItem> listAll(){
        ArrayList<FlightItem> itemList =null;
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor cursor=db.query(TBNAME2, null, null, null, null, null, null);
        if (cursor!=null){
            itemList=new ArrayList<FlightItem>();
            while (cursor.moveToNext()){
                FlightItem item =new FlightItem();
                item.setId(cursor.getString(cursor.getColumnIndex("ID")));
                item.setOriginal(cursor.getString(cursor.getColumnIndex("ORIGINAL")));
                item.setDestination(cursor.getString(cursor.getColumnIndex("DESTINATION")));
                item.setPass(cursor.getString(cursor.getColumnIndex("PASS")));
                item.setPlanned(cursor.getString(cursor.getColumnIndex("PLANNED")));
                item.setTerminal(cursor.getString(cursor.getColumnIndex("TERMINAL")));
                item.setStatus(cursor.getString(cursor.getColumnIndex("STATUS")));
                itemList.add(item);
            }
            cursor.close();
        }
        db.close();
        return itemList;
    }

    public ArrayList<FlightItem> findByCondition(String destination, String terminal){
        ArrayList<FlightItem> list=new ArrayList<FlightItem>();
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor cursor;
        if(!destination.equals("")){
            if(terminal.equals("")){
                cursor=db.query(TBNAME, null, "DESTINATION=?", new String[]{destination}, null, null, null);
            }
            else {
                cursor=db.query(TBNAME, null, "DESTINATION=? AND TERMINAL=?", new String[]{destination,terminal}, null, null, null);
            }
        }
        else {
            cursor=db.query(TBNAME, null, "TERMINAL=?", new String[]{terminal}, null, null, null);
        }
        FlightItem flightItem=null;
        if(cursor!=null&&cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                flightItem = new FlightItem();
                flightItem.setId(cursor.getString(cursor.getColumnIndex("ID")));
                flightItem.setOriginal(cursor.getString(cursor.getColumnIndex("ORIGINAL")));
                flightItem.setDestination(cursor.getString(cursor.getColumnIndex("DESTINATION")));
                flightItem.setPass(cursor.getString(cursor.getColumnIndex("PASS")));
                flightItem.setPlanned(cursor.getString(cursor.getColumnIndex("PLANNED")));
                flightItem.setTerminal(cursor.getString(cursor.getColumnIndex("TERMINAL")));
                flightItem.setStatus(cursor.getString(cursor.getColumnIndex("STATUS")));
                list.add(flightItem);
            }
        }
        cursor.close();
        db.close();
        return list;
    }
}

