package com.example.airport;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
//import android.content.DialogInterface;
import android.content.DialogInterface;

import com.example.airport.ui.main.DBManager;
import com.example.airport.ui.main.FlightItem;
import com.example.airport.ui.main.SecondPage;

import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;



public class Collect extends AppCompatActivity implements AdapterView.OnItemLongClickListener {
    ArrayList<HashMap<String,String>> list;
    SimpleAdapter listItemAdapter;
    DBManager dbManager;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        listView=(ListView)findViewById(R.id.list2);
        TextView textView=(TextView)findViewById(R.id.aaa);
        listView.setEmptyView(textView);
        listView.setOnItemLongClickListener(this);
        dbManager=new DBManager(this);

        list=new ArrayList<HashMap<String,String>>();
        ArrayList<FlightItem> items=new ArrayList<FlightItem>();
        items=dbManager.listAll();
        for(FlightItem item:items){
            HashMap<String,String> map=new HashMap<String, String>();
            map.put("flight", item.getId());
            map.put("origin",item.getOriginal());
            map.put("destination", item.getDestination());
            map.put("pass",item.getPass());
            map.put("planned", item.getPlanned());
            map.put("terminal", item.getTerminal());
            map.put("status", item.getStatus());
            list.add(map);
        }
        listItemAdapter =new SimpleAdapter(this,list,R.layout.list_item,
                new String[]{"flight","origin","destination","pass","planned","terminal","status"},new int[]{R.id.show_flight,R.id.show_origin,R.id.show_destination,R.id.show_pass,R.id.show_planned,R.id.show_terminal,R.id.show_status});
        listView.setAdapter(listItemAdapter);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, final View view, final int i, long l) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示")
                .setMessage("您确定要移出收藏夹？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int j) {
                        list.remove(i);
                        listItemAdapter.notifyDataSetChanged();
                        dbManager.delete_collect(((TextView)view.findViewById(R.id.show_flight)).getText().toString());
                    }
                }).setNegativeButton("否", null);
        builder.create().show();
        return false;
    }
}
