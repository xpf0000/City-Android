package citycircle.com.Property;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.MyAppService.CityServices;
import citycircle.com.Property.PropertyAdapter.FeeAdapter;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;
import citycircle.com.Utils.PreferencesUtils;

/**
 * Created by admins on 2016/2/15.
 */
public class FeeBack extends Activity implements View.OnClickListener {
    ImageView back, newsphoto;
    ListView comentlist;
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> hashMap;
    String url, urlstr;
    FeeAdapter adapter;
    SwipeRefreshLayout Refresh;
    String uid, username, houseid;
    int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pro_feeback);
        Intent intent = new Intent(FeeBack.this, CityServices.class);
        FeeBack.this.startService(intent);
        IntentFilter filter = new IntentFilter(CityServices.action);
        registerReceiver(broadcastReceiver, filter);
        username = PreferencesUtils.getString(FeeBack.this, "username");
        uid = PreferencesUtils.getString(FeeBack.this, "userid");
        houseid = PreferencesUtils.getString(FeeBack.this, "houseids");
        url = GlobalVariables.urlstr + "wuye.getFeedList&uid=" + uid + "&username=" + username + "&page=" + page + "&houseid=" + houseid;
        intview();
        setComentlist();
        getList(0);
    }

    private void intview() {
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        newsphoto = (ImageView) findViewById(R.id.newsphoto);
        newsphoto.setOnClickListener(this);
        comentlist = (ListView) findViewById(R.id.comentlist);
        Refresh = (SwipeRefreshLayout) findViewById(R.id.Refresh);
        comentlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(FeeBack.this, ComentInfo.class);
                intent.putExtra("id", array.get(position).get("id"));
                intent.putExtra("content", array.get(position).get("content"));
                intent.putExtra("type", array.get(position).get("type"));
                intent.putExtra("picList", array.get(position).get("picList"));
                intent.putExtra("time", array.get(position).get("create_time"));
                FeeBack.this.startActivity(intent);
            }
        });
        comentlist.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            page++;
                            url = GlobalVariables.urlstr + "wuye.getFeedList&uid=" + uid + "&username=" + username + "&page=" + page + "&houseid=" + houseid;
                            getList(0);
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        Refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                url = GlobalVariables.urlstr + "wuye.getFeedList&uid=" + uid + "&username=" + username + "&page=" + page + "&houseid=" + houseid;
                getList(1);
            }
        });
    }

    private void getList(final int type) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                HttpRequest httpRequest = new HttpRequest();
                urlstr = httpRequest.doGet(url);
                if (urlstr.equals("网络超时")) {
                    handler.sendEmptyMessage(2);
                } else {
                    if (type == 0) {
                        handler.sendEmptyMessage(1);
                    } else {
                        handler.sendEmptyMessage(3);
                    }

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
                    Toast.makeText(FeeBack.this, R.string.intent_error, Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Refresh.setRefreshing(false);
                    array.clear();
                    setArray(urlstr);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    private void setArray(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
        int a = jsonObject1.getIntValue("code");
        if (a == 0) {
            JSONArray jsonArray = jsonObject1.getJSONArray("info");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                hashMap = new HashMap<>();
                hashMap.put("id", jsonObject2.getString("id") == null ? "" : jsonObject2.getString("id"));
                hashMap.put("content", jsonObject2.getString("content") == null ? "" : jsonObject2.getString("content"));
                hashMap.put("type", jsonObject2.getString("type") == null ? "" : jsonObject2.getString("type"));
                hashMap.put("picList", jsonObject2.getString("picList") == null ? "" : jsonObject2.getString("picList"));
                hashMap.put("create_time", jsonObject2.getString("create_time") == null ? "" : jsonObject2.getString("create_time"));
                array.add(hashMap);
            }
        } else {
            if (page == 1) {

            } else {
                page--;
            }
        }
    }

    private void setComentlist() {
        adapter = new FeeAdapter(array, FeeBack.this);
        comentlist.setAdapter(adapter);
    }
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int a = intent.getExtras().getInt("meeage");
            if (a == 11) {
                Refresh.post(new Runnable() {
                    @Override
                    public void run() {
                        Refresh.setRefreshing(true);
                    }
                });
                onRefresh();
            }
        }

    };
    public void onRefresh() {
        Refresh.postDelayed(new Runnable() {

            @Override
            public void run() {
                // 更新数据
                page = 1;
                url = GlobalVariables.urlstr + "wuye.getFeedList&uid=" + uid + "&username=" + username + "&page=" + page + "&houseid=" + houseid;
                getList(1);
            }
        }, 2000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newsphoto:
                Intent intent = new Intent();
                intent.setClass(FeeBack.this, ReplyFee.class);
                FeeBack.this.startActivity(intent);
                break;
            case R.id.back:
                finish();
                break;
        }
    }
}
