package citycircle.com.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.Adapter.SalseAdapter;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;

/**
 * Created by admins on 2015/11/30.
 */
public class SaleList extends Activity {
    ListView hotlist;
    String url,urlstr,id,calssnames,addview;
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> hashMap;
    SalseAdapter hotTelAdapter;
    int page=1;
    ImageView back;
    TextView calssname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.telclass);
        id=getIntent().getStringExtra("id");
        calssnames=getIntent().getStringExtra("calssnames");
        url= GlobalVariables.urlstr+"Discount.getList&category_id="+id+"&page="+page;
        intview();
        setHotlist();
        geturlstr(0);
    }
    public void intview(){
        calssname=(TextView)findViewById(R.id.calssname);
        calssname.setText(calssnames);
        back=(ImageView)findViewById(R.id.back);
        hotlist=(ListView)findViewById(R.id.hotlist);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        hotlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addview = GlobalVariables.urlstr + "News.addView&id=" + array.get(position).get("id");
                Intent intent = new Intent();
                intent.putExtra("id", array.get(position).get("id"));
                intent.setClass(SaleList.this, DiscountInfo.class);
                SaleList.this.startActivity(intent);
                geturlstr(1);
            }
        });
        hotlist.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            page++;
                            url = GlobalVariables.urlstr + "Discount.getList&category_id=" + id + "&page=" + page;
                            geturlstr(0);
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

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
                    urlstr = httpRequest.doGet(url);
                    if (urlstr.equals("网络超时")) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(1);
                    }
                }else {

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
                    setHashMap(urlstr);
                    hotTelAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    Toast.makeText(SaleList.this, "网络似乎有问题了", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(SaleList.this, "暂无内容", Toast.LENGTH_SHORT).show();
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
        hotTelAdapter=new SalseAdapter(array,SaleList.this,handler);
        hotlist.setAdapter(hotTelAdapter);
    }
}
