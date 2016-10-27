package citycircle.com.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
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
import java.util.Iterator;
import java.util.List;

import citycircle.com.Adapter.Collect_Adapter;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;
import citycircle.com.Utils.PreferencesUtils;
import citycircle.com.Adapter.Collect_Adapter.ViewHolder;

/**
 * Created by admins on 2015/11/29.
 */
public class MyCollect extends Activity implements View.OnClickListener {
    ImageView back;
    String url, urlstr, uid, deurl, deurlstr, did, username;
    int page = 1;
    TextView bianji;
    ListView collectlist;
    SwipeRefreshLayout lehuirefresh;
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
    List<String> list = new ArrayList<>();
    Collect_Adapter collect_adapter;
    int a = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mycollects);
        uid = PreferencesUtils.getString(MyCollect.this, "userid");
        username = PreferencesUtils.getString(MyCollect.this, "username");
        url = GlobalVariables.urlstr + "News.getCollectList&uid=" + uid + "&page=" + page;
        deurl = GlobalVariables.urlstr + "News.collectDel&id=" + did + "&username=" + username;
        intview();
        setlist();
        getcollect(0);
    }

    public void intview() {
        back = (ImageView) findViewById(R.id.back);
        bianji = (TextView) findViewById(R.id.bianji);
        bianji.setOnClickListener(this);
        back.setOnClickListener(this);
        collectlist = (ListView) findViewById(R.id.mycollect);
        lehuirefresh = (SwipeRefreshLayout) findViewById(R.id.lehuirefresh);
        lehuirefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                array.clear();
                page = 1;
//                newsurl = "http://appapi.rexian.cn:8080/HKCityApi/news/newsFocusList?areaID=1&pageSize=10&pageIndex=" + page;
                url = GlobalVariables.urlstr + "News.getCollectList&uid=" + uid + "&page=" + page;
                getcollect(0);
            }
        });
        collectlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (a == 2) {
                    ViewHolder holder = (ViewHolder) view.getTag();
                    holder.cb.toggle();
                    if (holder.cb.isChecked() == true) {
                        array.get(position).put("flag", "true");
                        collect_adapter.notifyDataSetChanged();
                        list.add(array.get(position).get("id"));
                    } else {
                        array.get(position).put("flag", "false");
                        list.remove(array.get(position).get("id"));
                        collect_adapter.notifyDataSetChanged();
                    }
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("id", array.get(position).get("id"));
                    intent.putExtra("title", array.get(position).get("title"));
                    intent.putExtra("description", "");
                    intent.putExtra("url", array.get(position).get("url"));
                    intent.setClass(MyCollect.this, NewsInfoActivity.class);
                    MyCollect.this.startActivity(intent);
                }
            }
        });
        collectlist.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            if (a == 2) {

                            } else {
                                page++;
                                url = GlobalVariables.urlstr + "News.getCollectList&uid=" + uid + "&page=" + page;
                                getcollect(0);
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    public void getcollect(final int type) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                HttpRequest httpRequest = new HttpRequest();
                if (type == 0) {
                    urlstr = httpRequest.doGet(url);
                    if (urlstr.equals("网络超时")) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(1);
                    }
                } else {
                    deurlstr = httpRequest.doGet(deurl);
                    if (deurlstr.equals("网络超时")) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(4);
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
                    lehuirefresh.setRefreshing(false);
                    a=1;
                    bianji.setText("编辑");
                    setHashMap(urlstr);
                    collect_adapter.notifyDataSetChanged();
                    break;
                case 2:
                    lehuirefresh.setRefreshing(false);
                    Toast.makeText(MyCollect.this, "网络似乎出问题了", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    lehuirefresh.setRefreshing(false);
                    Toast.makeText(MyCollect.this, "暂无更多收藏", Toast.LENGTH_SHORT).show();
                    Snackbar.make(lehuirefresh, "暂无更多收藏", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    break;
                case 4:
                    JSONObject jsonObject = JSON.parseObject(deurlstr);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    if (jsonObject1.getIntValue("code") == 0) {
                        Toast.makeText(MyCollect.this, "删除成功", Toast.LENGTH_SHORT).show();
                        Iterator<HashMap<String, String>> iterator = array.iterator();
                        while (iterator.hasNext()) {
                            HashMap<String, String> temp = iterator.next();
                            if (temp.get("flag").equals("true")) {
                                iterator.remove();
                            }
                        }
                        a = 1;
                        for (int i = 0; i < array.size(); i++) {
                            array.get(i).put("show", "false");
                        }
                        collect_adapter.notifyDataSetChanged();
                        try {
                            setcollect();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(MyCollect.this, "删除失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 5:
                    if (array.get(GlobalVariables.position).get("flag").equals("false")) {
                        array.get(GlobalVariables.position).put("flag", "true");
                        collect_adapter.notifyDataSetChanged();
                        list.add(array.get(GlobalVariables.position).get("id"));
                    } else {
                        array.get(GlobalVariables.position).put("flag", "false");
                        list.remove(array.get(GlobalVariables.position).get("id"));
                        collect_adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };

    public void setHashMap(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
        int a = jsonObject1.getIntValue("code");
        if (a == 0) {
            JSONArray jsonArray = jsonObject1.getJSONArray("info");
            for (int i = 0; i < jsonArray.size(); i++) {
                hashMap = new HashMap<>();
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                hashMap.put("id", jsonObject2.getString("id") == null ? "" : jsonObject2.getString("id"));
                hashMap.put("title", jsonObject2.getString("title") == null ? "" : jsonObject2.getString("title"));
                hashMap.put("url", jsonObject2.getString("url") == null ? "" : jsonObject2.getString("url"));
                hashMap.put("flag", "false");
                hashMap.put("show", "false");
                array.add(hashMap);
            }
        } else {
            if (page == 1) {

            } else {
                page--;
            }
            handler.sendEmptyMessage(3);
        }
    }

    public void setlist() {
        collect_adapter = new Collect_Adapter(array, MyCollect.this,handler);
        collectlist.setAdapter(collect_adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.bianji:
                if (a == 1) {
                    for (int i = 0; i < array.size(); i++) {
                        array.get(i).put("show", "true");
                    }
                    collect_adapter.notifyDataSetChanged();
                    bianji.setText("删除");
                    a = 2;
                } else {
                    bianji.setText("编辑");
                    if (list.size() == 0) {
                        for (int i = 0; i < array.size(); i++) {
                            array.get(i).put("show", "false");
                        }
                        collect_adapter.notifyDataSetChanged();
                        a = 1;
                    } else {
                        String cid = "";
                        for (int i = 0; i < list.size(); i++) {
                            if (i == 0) {
                                cid = list.get(i);
                            } else {
                                cid = cid + "," + list.get(i);
                            }
                        }
                        did = cid;
                        deurl = GlobalVariables.urlstr + "News.collectDel&id=" + did + "&username=" + username;
                        getcollect(1);
//                        System.out.println(cid);
                    }
                }

                break;
        }
    }

    private void setcollect() {
        String str = PreferencesUtils.getString(MyCollect.this, "collelist");
        JSONObject jsonObject = JSON.parseObject(str);
        JSONArray jsonArray = jsonObject.getJSONArray("list");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            String ID = jsonObject1.getString("id") == null ? "" : jsonObject1.getString("id");
            for (int j = 0; j < list.size(); j++) {
                if (ID.equals(list.get(j))) {
                    jsonArray.remove(i);
                    i = i - 1;
                }
            }
        }
        PreferencesUtils.putString(MyCollect.this, "collelist", jsonObject.toJSONString());
    }
}
