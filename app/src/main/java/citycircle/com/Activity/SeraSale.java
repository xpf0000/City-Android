package citycircle.com.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.Adapter.SalseAdapter;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;

/**
 * Created by admins on 2015/11/30.
 */
public class SeraSale extends Activity implements View.OnClickListener{
    ListView list;
    EditText search;
    ImageView searchbt,back;
    String url,urlstr,key;
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> hashMap;
    SalseAdapter hotTelAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchsale);
        url= GlobalVariables.urlstr+"Discount.search&key="+key;
        intview();
        setHotlist();
    }
    public void intview(){
        list=(ListView)findViewById(R.id.list);
        search=(EditText)findViewById(R.id.search);
        searchbt=(ImageView)findViewById(R.id.searchbt);
        searchbt.setOnClickListener(this);
        back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(this);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                intent.putExtra("id",array.get(position).get("id"));
                intent.setClass(SeraSale.this, DiscountInfo.class);
                SeraSale.this.startActivity(intent);
            }
        });
    }
    public void geturlstr() {
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
                    array.clear();
                    setHashMap(urlstr);
                    hotTelAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    Toast.makeText(SeraSale.this, "网络似乎有问题了", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(SeraSale.this, "暂无内容", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    public  void  setHashMap(String str){
        JSONObject jsonObject = JSON.parseObject(str);
        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
        int a = jsonObject1.getIntValue("code");
        if (a == 0) {
            JSONArray jsonArray = jsonObject1.getJSONArray("info");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                hashMap = new HashMap<>();
                hashMap.put("id", jsonObject2.getString("id") == null ? "" : jsonObject2.getString("id"));
                hashMap.put("title", jsonObject2.getString("title") == null ? "" : jsonObject2.getString("title"));
                hashMap.put("path", jsonObject2.getString("url") == null ? "" : jsonObject2.getString("url"));
                hashMap.put("s_time", jsonObject2.getString("s_time") == null ? "" : jsonObject2.getString("s_time"));
                hashMap.put("e_time", jsonObject2.getString("e_time") == null ? "" : jsonObject2.getString("e_time"));
                hashMap.put("view", jsonObject2.getString("view") == null ? "" : jsonObject2.getString("view"));
                array.add(hashMap);
            }
        } else {
            handler.sendEmptyMessage(3);
        }
    }
    public void  setHotlist(){
        hotTelAdapter=new SalseAdapter(array,SeraSale.this,handler);
        list.setAdapter(hotTelAdapter);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.searchbt:
                if (search.getText().toString().length()==0){
                    Toast.makeText(SeraSale.this,"请输入内容",Toast.LENGTH_SHORT).show();
                }else {
                    key=search.getText().toString();
                    try {
                        url= GlobalVariables.urlstr+"Discount.search&key="+ URLEncoder.encode(key, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    geturlstr();
                }
                break;
        }
    }
}
