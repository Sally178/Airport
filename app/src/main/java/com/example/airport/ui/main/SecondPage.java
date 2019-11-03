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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.widget.Toast;

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
public class SecondPage extends Fragment implements Runnable{
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
        Button bt=(Button)getView().findViewById(R.id.button3);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FlightItem flightItem=new FlightItem();
                DBManager dbManager=new DBManager(SecondPage.this.getActivity());
                ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String, String>>();
                EditText editText1=getView().findViewById(R.id.editText2);
                EditText editText2=getView().findViewById(R.id.editText3);
                EditText editText3=getView().findViewById(R.id.editText4);
                Log.i("aaa", "onclick");
                String flightId=null;
                flightId=editText1.getText().toString();
                Log.i("aaa", flightId);
                String flightDest=editText2.getText().toString();
                String flightTerm=editText3.getText().toString();
                if(!flightId.equals("")){
                    flightItem=dbManager.findById(flightId);
                    HashMap<String,String> map=new HashMap<String, String>();
                    map.put("flight", flightItem.getId());
                    map.put("origin", flightItem.getOriginal());
                    map.put("destination", flightItem.getDestination());
                    map.put("pass", flightItem.getPass());
                    map.put("planned", flightItem.getPlanned());
                    map.put("terminal", flightItem.getTerminal());
                    map.put("status", flightItem.getStatus());
                    list.add(map);
                }
                else{
                    if(flightDest.equals("")&&flightTerm.equals("")){
                        Toast.makeText(SecondPage.this.getActivity(), "请输入查询内容", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else{
                        ArrayList<FlightItem> list1=new ArrayList<FlightItem>();
                        list1=dbManager.findByCondition(flightDest, flightTerm);
                        for(FlightItem flight: list1){
                            HashMap<String,String> map=new HashMap<String, String>();
                            map.put("flight", flight.getId());
                            Log.i("aaa", flight.getId());
                            map.put("origin", flight.getOriginal());
                            map.put("destination", flight.getDestination());
                            map.put("pass", flight.getPass());
                            map.put("planned", flight.getPlanned());
                            map.put("terminal", flight.getTerminal());
                            map.put("status", flight.getStatus());
                            list.add(map);
                        }
                    }
                }

                listItemAdapter =new SimpleAdapter(SecondPage.this.getActivity(),list,R.layout.list_item,
                        new String[]{"flight","origin","destination","pass","planned","terminal","status"},new int[]{R.id.show_flight,R.id.show_origin,R.id.show_destination,R.id.show_pass,R.id.show_planned,R.id.show_terminal,R.id.show_status});
                listView.setAdapter(listItemAdapter);
            }
        });
        listView=(ListView) getView().findViewById(R.id.list2);
        listView.setEmptyView(getView().findViewById(R.id.inform2));
        ArrayList<HashMap<String,String>> list1=new ArrayList<HashMap<String, String>>();
        getInfo();
        Log.i("aaa", "thread?");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.second_fragment, container, false);
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
            ArrayList<FlightItem> list=new ArrayList<FlightItem>();
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
                    FlightItem map = new FlightItem();
                    Element tr = trs.get(get);
                    String flight = tr.getElementsByTag("td").get(0).text();
                    String origin = tr.getElementsByTag("td").get(1).text();
                    String destination = tr.getElementsByTag("td").get(2).text();
                    String pass = tr.getElementsByTag("td").get(3).text();
                    String planned = tr.getElementsByTag("td").get(4).text();
                    String terminal = tr.getElementsByTag("td").get(5).text();
                    String status = tr.getElementsByTag("td").get(6).text();
                    map.setId(flight);
                    map.setOriginal(origin);
                    map.setDestination(destination);
                    map.setPass(pass);
                    map.setPlanned(planned);
                    map.setTerminal(terminal);
                    map.setStatus(status);
                    list.add(map);
                }
                Message msg= handler.obtainMessage(5);
                DBManager dbManager =new DBManager(this.getActivity());
                dbManager.deleteAll();
                dbManager.addAll(list);
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
               /* if (msg.what == 5) {
                    list[0] = (ArrayList<HashMap<String, String>>) msg.obj;
                    Log.i("aaa", "receive");
                    listItemAdapter =new SimpleAdapter(SecondPage.this.getActivity(),list[0],R.layout.list_item,
                            new String[]{"flight","origin","destination","pass","planned","terminal","status"},new int[]{R.id.show_flight,R.id.show_origin,R.id.show_destination,R.id.show_pass,R.id.show_planned,R.id.show_terminal,R.id.show_status});
                    listView.setAdapter(listItemAdapter);
                }*/
                super.handleMessage(msg);
            }
        };
        Thread t=new Thread(this);
        t.start();
    }
}