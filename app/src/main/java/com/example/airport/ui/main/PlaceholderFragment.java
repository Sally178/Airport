package com.example.airport.ui.main;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.airport.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment implements Runnable {
    String url=null;
    Document doc=null;
    Handler handler;
    int i;
    ListView listView;
    static int Index;
    SimpleAdapter listItemAdapter;
    //MyAdapter myAdapter;
    //private static final String ARG_SECTION_NUMBER = "section_number";

    //private PageViewModel pageViewModel;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        /*Index = 1;
        if (getArguments() != null) {
            Index = getArguments().getInt(ARG_SECTION_NUMBER);
        }*/
        listView=(ListView) getView().findViewById(R.id.list);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                DBManager dbManager =new DBManager(PlaceholderFragment.this.getActivity());
                String ID=((TextView)view.findViewById(R.id.show_flight)).getText().toString();
                String original=((TextView)view.findViewById(R.id.show_origin)).getText().toString();
                String destination=((TextView)view.findViewById(R.id.show_destination)).getText().toString();
                String pass=((TextView)view.findViewById(R.id.show_pass)).getText().toString();
                String planned=((TextView)view.findViewById(R.id.show_planned)).getText().toString();
                String terminal=((TextView)view.findViewById(R.id.show_terminal)).getText().toString();
                String status=((TextView)view.findViewById(R.id.show_status)).getText().toString();
                FlightItem item=new FlightItem(ID,original,destination,pass,planned,terminal,status);
                dbManager.add(item);
                Toast.makeText(PlaceholderFragment.this.getActivity(), "航班已添加至收藏夹", Toast.LENGTH_SHORT).show();
                Log.i("aaa", "collect");
                return true;
            }
        });
        listView.setEmptyView(getView().findViewById(R.id.inform));
        ArrayList<HashMap<String,String>> list1=new ArrayList<HashMap<String, String>>();
        getInfo();
        Log.i("aaa", "thread?");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        //final TextView textView = root.findViewById(R.id.section_label);
        //pageViewModel.getText().observe(this, new Observer<String>() {
           // @Override
           // public void onChanged(@Nullable String s) {
                //textView.setText(s);
           // }
       // });

        //pageViewModel.setIndex(index);
        return root;
    }

    @Override
    public void run() {
        Log.i("aaa", "run");
        try {
            url="http://www.cdairport.com/flightInfor.aspx?t=4&attribute=D&time=0&page=1";
            ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String, String>>();
            //while (true){
                doc= Jsoup.connect(url).get();
                if(doc==null) {
                    Log.i("Info", "logout");
                    Message msg= handler.obtainMessage(6);
                    msg.obj=0;
                    handler.sendMessage(msg);
                    //break;
                }
                else {
                    Log.i("info", doc.html());
                    Element table = doc.getElementsByTag("table").first();
                    Elements trs = table.getElementsByTag("tr");
                    for (int j = 0; j < trs.size() - 1; j++) {
                        int get = j + 1;
                        HashMap<String, String> map = new HashMap<String, String>();
                        Element tr = trs.get(get);
                        String flight = tr.getElementsByTag("td").get(0).text();
                        String origin = tr.getElementsByTag("td").get(1).text();
                        String destination = tr.getElementsByTag("td").get(2).text();
                        String pass = tr.getElementsByTag("td").get(3).text();
                        String planned = tr.getElementsByTag("td").get(4).text();
                        String terminal = tr.getElementsByTag("td").get(5).text();
                        String status = tr.getElementsByTag("td").get(6).text();
                        map.put("flight", flight);
                        map.put("origin", origin);
                        map.put("destination", destination);
                        map.put("pass", pass);
                        map.put("planned", planned);
                        map.put("terminal", terminal);
                        map.put("status", status);
                        list.add(map);
                    }
                    Message msg= handler.obtainMessage(5);
                    msg.obj=list;
                    handler.sendMessage(msg);
                }
           // }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getInfo(){
        final ArrayList<HashMap<String, String>>[] list = new ArrayList[]{new ArrayList<HashMap<String, String>>()};
        Log.i("aaa", "getInfo");
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 5) {
                    list[0] = (ArrayList<HashMap<String, String>>) msg.obj;
                    Log.i("aaa", "receive");
                    listItemAdapter =new SimpleAdapter(PlaceholderFragment.this.getActivity(),list[0],R.layout.list_item,
                            new String[]{"flight","origin","destination","pass","planned","terminal","status"},new int[]{R.id.show_flight,R.id.show_origin,R.id.show_destination,R.id.show_pass,R.id.show_planned,R.id.show_terminal,R.id.show_status});
                    listView.setAdapter(listItemAdapter);
                }
                super.handleMessage(msg);
            }
        };
        Thread t=new Thread(this);
        t.start();
    }

}