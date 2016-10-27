package citycircle.com.Property;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.Property.PropertyAdapter.MeeageAdapter;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;
import citycircle.com.Utils.PreferencesUtils;

/**
 * Created by 飞侠 on 2016/3/18.
 */
public class MessageList extends Activity {
    ListView messagelist;
    String url, urlstr, uid, username;
    SwipeRefreshLayout Refresh;
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> idarray = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> hashMap;
    HashMap<String, Object> hashMaps;
    MeeageAdapter meeageAdapter;
    ImageView loginback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.promessagelist);
        uid = PreferencesUtils.getString(MessageList.this, "userid");
        username = PreferencesUtils.getString(MessageList.this, "username");
        url = GlobalVariables.urlstr + "Wuye.getUserNewsList&uid=" + uid + "&username=" + username;
        intview();
        getmessagelist();
    }

    private void intview() {
        try {
            setIdarray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        loginback = (ImageView) findViewById(R.id.loginback);
        messagelist = (ListView) findViewById(R.id.messagelist);
        Refresh = (SwipeRefreshLayout) findViewById(R.id.Refresh);
        Refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getmessagelist();
            }
        });
        loginback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        messagelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("content", array.get(position).get("content"));
                intent.setClass(MessageList.this, Messagecontent.class);
                MessageList.this.startActivity(intent);
                if (array.get(position).get("dian").equals("0")) {
                    hashMap = new HashMap<String, String>();
                    hashMap.put("id", array.get(position).get("id"));
                    idarray.add(hashMap);
                    hashMaps = new HashMap<String, Object>();
                    hashMaps.put("list", idarray);
                    String json = JSON.toJSONString(hashMaps);
                    PreferencesUtils.putString(MessageList.this, "proid", json);
                    try {
                        setIdarray();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    array.clear();
                    handler.sendEmptyMessage(1);
//                    meeageAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void getmessagelist() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                HttpRequest httpRequest = new HttpRequest();
                urlstr = httpRequest.doGet(url);
                if (urlstr.equals("网络超时")) {
                    handler.sendEmptyMessage(2);
                } else {
                    handler.sendEmptyMessage(1);
                }
            }
        }.start();
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Refresh.setRefreshing(false);
                    array.clear();
                    setarray(urlstr);
                    setMessagelist();
                    break;
                case 2:
                    Refresh.setRefreshing(false);
                    Toast.makeText(MessageList.this, R.string.intent_error, Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Refresh.setRefreshing(false);
                    Toast.makeText(MessageList.this, R.string.nomore, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void setarray(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
        int a = jsonObject1.getIntValue("code");
        if (a == 0) {
            JSONArray jsonArray = jsonObject1.getJSONArray("info");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                hashMap = new HashMap<>();
                hashMap.put("id", jsonObject2.getString("id") == null ? "" : jsonObject2.getString("id"));
               if (idarray.size()!=0){
                   for (int j = 0; j < idarray.size(); j++) {
                       if (jsonObject2.getString("id").equals(idarray.get(j).get("id"))) {
                           hashMap.put("dian", "1");
                           break;
                       } else {
                           hashMap.put("dian", "0");
                       }
                   }
               }else {
                   hashMap.put("dian", "0");
               }
                hashMap.put("xiaoqu", jsonObject2.getString("xiaoqu") == null ? "" : jsonObject2.getString("xiaoqu"));
                hashMap.put("content", jsonObject2.getString("content") == null ? "" : jsonObject2.getString("content"));
                hashMap.put("create_time", jsonObject2.getString("create_time") == null ? "" : jsonObject2.getString("create_time"));
                array.add(hashMap);
            }
        } else {
        }
    }

    private void setMessagelist() {
        meeageAdapter = new MeeageAdapter(array, MessageList.this);
        messagelist.setAdapter(meeageAdapter);
    }

    private void setIdarray() throws Exception {
        idarray.clear();
        String idjson = PreferencesUtils.getString(MessageList.this, "proid");
        JSONObject jsonObject = JSON.parseObject(idjson);
        JSONArray jsonArray = jsonObject.getJSONArray("list");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            hashMap = new HashMap<>();
            hashMap.put("id", jsonObject1.getString("id"));
            idarray.add(hashMap);
        }
    }
}
