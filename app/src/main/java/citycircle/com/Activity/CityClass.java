package citycircle.com.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import citycircle.com.Adapter.CityAdapter;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;

/**
 * Created by admins on 2015/11/24.
 */
public class CityClass extends Activity {
    ListView hotlist;
    String url, urlstr;
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> hashMap;
    CityAdapter adapter;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.telclass);
        url = GlobalVariables.urlstr + "Quan.getCategory";
        intview();
        getstr();
    }

    public void intview() {
        back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        hotlist = (ListView) findViewById(R.id.hotlist);
        setAdapter();
        hotlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                intent.putExtra("id",array.get(position).get("id"));
                intent.setClass(CityClass.this, CityClassList.class);
                CityClass.this.startActivity(intent);
            }
        });
    }

    public void getstr() {
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
            {
                switch (msg.what) {
                    case 1:
                        setmap(urlstr);
                        adapter.notifyDataSetChanged();
                        break;
                    case 2:
                        Toast.makeText(CityClass.this, "网络似乎有问题了", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(CityClass.this, "暂无内容", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    };
    public void setmap(String str){
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
                array.add(hashMap);
            }
        }else {

        }
    }

    public void setAdapter() {
       adapter=new CityAdapter(array,CityClass.this);
        hotlist.setAdapter(adapter);
    }
}
