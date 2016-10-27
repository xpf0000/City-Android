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
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.Adapter.SalseAdapter;
import citycircle.com.Adapter.TelclassAdapter;
import citycircle.com.MyViews.MyGridView;
import citycircle.com.MyViews.MyListView;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;

/**
 * Created by admins on 2015/11/20.
 */
public class SaleActivity extends Activity {
    MyGridView telclass;
    MyListView hotlist;
    String calssurl, calssurlstr, hoturl, hoturlstr;
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> classhashMap;
    ArrayList<HashMap<String, String>> classarray = new ArrayList<HashMap<String, String>>();
    TelclassAdapter telclassAdapter;
    ImageView back;
    SalseAdapter hotTelAdapter;
   EditText search;
    String addview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sale);
        calssurl = GlobalVariables.urlstr + "Discount.getCategory";
        hoturl = GlobalVariables.urlstr + "Discount.getHot&perNumber=20";
        intview();
        setTelclass();
        setHotlist();
        geturlstr(0);
    }

    public void intview() {
        search=(EditText)findViewById(R.id.search);
        telclass = (MyGridView) findViewById(R.id.telclass);
        hotlist = (MyListView) findViewById(R.id.hotlist);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SaleActivity.this, SeraSale.class);
                SaleActivity.this.startActivity(intent);
            }
        });
        hotlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addview = GlobalVariables.urlstr + "News.addView&id=" + array.get(position).get("id");
                Intent intent = new Intent();
                intent.putExtra("id", array.get(position).get("id"));
                intent.setClass(SaleActivity.this, DiscountInfo.class);
                SaleActivity.this.startActivity(intent);
                geturlstr(1);
            }
        });
        telclass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("id", classarray.get(position).get("id"));
                intent.putExtra("calssnames", classarray.get(position).get("title"));
                intent.setClass(SaleActivity.this, SaleList.class);
                SaleActivity.this.startActivity(intent);
            }
        });
    }
    public void geturlstr(final int type) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                HttpRequest httpRequest = new HttpRequest();
                if (type==0){
                    calssurlstr = httpRequest.doGet(calssurl);
                    hoturlstr = httpRequest.doGet(hoturl);
                    if (calssurlstr.equals("网络超时")) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(1);
                    }
                }else {
                    httpRequest.doGet(addview);
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
                    setClasshashMap(calssurlstr);
                    telclassAdapter.notifyDataSetChanged();
                    setHashMap(hoturlstr);
                    hotTelAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    Toast.makeText(SaleActivity.this, "网络似乎有问题了", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(SaleActivity.this, "暂无内容", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(SaleActivity.this, "拨打提示", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    //分类信息
    public void setClasshashMap(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
        int a = jsonObject1.getIntValue("code");
        if (a == 0) {
            JSONArray jsonArray = jsonObject1.getJSONArray("info");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                classhashMap = new HashMap<>();
                classhashMap.put("id", jsonObject2.getString("id") == null ? "" : jsonObject2.getString("id"));
                classhashMap.put("title", jsonObject2.getString("title") == null ? "" : jsonObject2.getString("title"));
                classhashMap.put("path", jsonObject2.getString("url") == null ? "" : jsonObject2.getString("url"));
                classarray.add(classhashMap);
            }
        } else {
            handler.sendEmptyMessage(3);
        }
    }

    //装填分类信息
    public void setTelclass() {
        telclassAdapter = new TelclassAdapter(classarray, SaleActivity.this);
        telclass.setAdapter(telclassAdapter);
    }
    //热门信息
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
    //装填热门
    public void  setHotlist(){
        hotTelAdapter=new SalseAdapter(array,SaleActivity.this,handler);
        hotlist.setAdapter(hotTelAdapter);
    }
}
