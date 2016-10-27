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

import citycircle.com.Property.PropertyAdapter.InfoMadapter;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;
import citycircle.com.Utils.PreferencesUtils;

/**
 * Created by admins on 2016/1/29.
 */
public class Information extends Activity implements View.OnClickListener{
    SwipeRefreshLayout Refresh;
    ListView info_list;
    ImageView back;
    String url, urlstr, uid, username, houseid;
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
    InfoMadapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information);
//        username = PreferencesUtils.getString(Information.this, "username");
//        uid = PreferencesUtils.getString(Information.this, "userid");
        houseid = PreferencesUtils.getString(Information.this, "houseids");
        username= PreferencesUtils.getString(Information.this, "username");;
        uid=PreferencesUtils.getString(Information.this, "userid");;
        url = GlobalVariables.urlstr + "Wuye.getNewsList&uid=" + uid + "&username=" + username + "&houseid=" + houseid;
        intview();
        setInfo_list();
        getList();
    }

    private void intview() {
//        Refresh = (SwipeRefreshLayout) findViewById(R.id.Refresh);
        info_list = (ListView) findViewById(R.id.info_list);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        info_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                intent.putExtra("id",array.get(position).get("id"));
                intent.setClass(Information.this, Info_info.class);
                Information.this.startActivity(intent);
            }
        });
    }

    private void getList() {
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
                    setArray(urlstr);
                    adapter.notifyDataSetChanged();
                    break;
                case 2:
                    Toast.makeText(Information.this,R.string.intent_error,Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(Information.this,R.string.nomore,Toast.LENGTH_SHORT).show();
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
                JSONObject jsonObject2=jsonArray.getJSONObject(i);
                hashMap=new HashMap<>();
                hashMap.put("id",jsonObject2.getString("id") == null ? "" : jsonObject2.getString("id"));
                hashMap.put("title",jsonObject2.getString("title") == null ? "" : jsonObject2.getString("title"));
                hashMap.put("create_time",jsonObject2.getString("create_time") == null ? "" : jsonObject2.getString("create_time"));
                array.add(hashMap);
            }
        }else {

        }

    }
    private void setInfo_list(){
        adapter=new InfoMadapter(array,Information.this);
        info_list.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
        }
    }
}
