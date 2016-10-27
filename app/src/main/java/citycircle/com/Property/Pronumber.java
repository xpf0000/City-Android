package citycircle.com.Property;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.Adapter.HotTelAdapter;
import citycircle.com.MyViews.CallPhonePop;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;
import citycircle.com.Utils.PreferencesUtils;

/**
 * Created by 飞侠 on 2016/3/18.
 */
public class Pronumber extends Activity {
    ListView numberlist;
    String url, urlstr, uid, username,houseid;
    SwipeRefreshLayout Refresh;
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> hashMap;
    HotTelAdapter hotTelAdapter;
    CallPhonePop callPhonePop;
    ImageView loginback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pronumber);
        uid = PreferencesUtils.getString(Pronumber.this, "userid");
        username = PreferencesUtils.getString(Pronumber.this, "username");
        houseid = PreferencesUtils.getString(Pronumber.this, "houseids");
        url = GlobalVariables.urlstr + "Wuye.getTelList&uid=" + uid + "&username=" + username+"&houseid="+houseid;
        intview();
        getmessagelist();
    }
    private void intview() {
        loginback=(ImageView)findViewById(R.id.loginback);
        numberlist = (ListView) findViewById(R.id.numberlists);
        Refresh=(SwipeRefreshLayout)findViewById(R.id.Refresh);
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
                    setArray(urlstr);
                    hotTelAdapter=new HotTelAdapter(array,Pronumber.this,handler);
                    numberlist.setAdapter(hotTelAdapter);
                    break;
                case 2:
                    Refresh.setRefreshing(false);
                    Toast.makeText(Pronumber.this, R.string.intent_error, Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Refresh.setRefreshing(false);
                    Toast.makeText(Pronumber.this, R.string.nomore, Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    callPhonePop=new CallPhonePop();
                    callPhonePop.showpop(Pronumber.this,array.get(GlobalVariables.position).get("tel"));
                    break;
            }
        }
    };
    private void setArray(String str){
        JSONObject jsonObject= JSON.parseObject(str);
        JSONObject jsonObject1=jsonObject.getJSONObject("data");
        int a=jsonObject1.getIntValue("code");
        if (a==0){
            JSONArray jsonArray=jsonObject1.getJSONArray("info");
            for (int i=0;i<jsonArray.size();i++){
                hashMap=new HashMap<>();
                JSONObject jsonObject2=jsonArray.getJSONObject(i);
                hashMap.put("id", jsonObject2.getString("id") == null ? "" : jsonObject2.getString("id"));
                hashMap.put("title", jsonObject2.getString("title") == null ? "" : jsonObject2.getString("title"));
                hashMap.put("name", jsonObject2.getString("name") == null ? "" : jsonObject2.getString("name"));
                hashMap.put("tel", jsonObject2.getString("tel") == null ? "" : jsonObject2.getString("tel"));
                hashMap.put("path", "http://android-artworks.25pp.com/fs01/2014/10/11/102_64afc48ec95440ae71f3b84f84f504ba.png");
                array.add(hashMap);
            }
        }else {

        }

    }
}
