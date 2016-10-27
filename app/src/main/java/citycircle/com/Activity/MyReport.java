package citycircle.com.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.Adapter.News_Adapter;
import citycircle.com.Adapter.ReportAdapter;
import citycircle.com.MyViews.MyPopwindows;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;
import citycircle.com.Utils.PreferencesUtils;

/**
 * Created by admins on 2015/11/16.
 */
public class MyReport extends Fragment {
    View view;
    ListView list;
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
    String url, urlstr;
    ReportAdapter adapter;
    int page = 1;
    String uid;
    SwipeRefreshLayout lehuirefresh;
    MyPopwindows myPopwindows;
    int positions;
    String deurl, deurlstr, username;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mycity, container, false);
        uid = PreferencesUtils.getString(getActivity(), "userid");
        username = PreferencesUtils.getString(getActivity(), "username");
        url = GlobalVariables.urlstr + "Quan.getUserList&uid=" + uid + "&page=" + page;
        intview();
        setlist();
        getnews(0);
        return view;
    }

    public void intview() {
        myPopwindows = new MyPopwindows();
        lehuirefresh = (SwipeRefreshLayout) view.findViewById(R.id.Refresh);
        list = (ListView) view.findViewById(R.id.list);
        myPopwindows.setMyPopwindowswListener(new MyPopwindows.MyPopwindowsListener() {
            @Override
            public void onRefresh() {
                String id = array.get(positions).get("id");
                deurl = GlobalVariables.urlstr + "Quan.quanDel&id=" + id + "&username=" + username;
                getnews(1);
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                positions = position;
                myPopwindows.showpop(getActivity(), "确定删除吗");
                return true;
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("id", array.get(position).get("id"));
                intent.setClass(getActivity(), Cityinfo.class);
                getActivity().startActivity(intent);
            }
        });
        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            page++;
                            url = GlobalVariables.urlstr + "Quan.getUserList&uid=" + uid + "&page=" + page;
                            getnews(0);
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        lehuirefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                array.clear();
                page = 1;
//                newsurl = "http://appapi.rexian.cn:8080/HKCityApi/news/newsFocusList?areaID=1&pageSize=10&pageIndex=" + page;
                url = GlobalVariables.urlstr + "Quan.getUserList&uid=" + uid + "&page=" + page;
                getnews(0);
            }
        });
    }

    public void getnews(final int type) {
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
                    getArray(urlstr);
                    adapter.notifyDataSetChanged();
                    break;
                case 2:
                    lehuirefresh.setRefreshing(false);
                    Toast.makeText(getActivity(), "网络似乎有问题了", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    lehuirefresh.setRefreshing(false);
                    Toast.makeText(getActivity(), "暂无更多内容", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    JSONObject jsonObject = JSON.parseObject(deurlstr);
                    if (jsonObject.getIntValue("ret") == 200) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        if (jsonObject1.getIntValue("code") == 0) {
                            Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                            array.remove(positions);
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getActivity(), "删除失败", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "删除失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    public void getArray(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
        int a = jsonObject1.getIntValue("code");
        if (a == 0) {
            JSONArray jsonArray = jsonObject1.getJSONArray("info");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                hashMap = new HashMap<>();
                hashMap.put("content", jsonObject2.getString("content") == null ? "" : jsonObject2.getString("content"));
                hashMap.put("id", jsonObject2.getString("id") == null ? "" : jsonObject2.getString("id"));
                hashMap.put("comment", jsonObject2.getString("comment") == null ? "" : jsonObject2.getString("comment"));
                hashMap.put("zan", jsonObject2.getString("zan") == null ? "" : jsonObject2.getString("zan"));
                hashMap.put("create_time", jsonObject2.getString("create_time") == null ? "" : jsonObject2.getString("create_time"));
                hashMap.put("category_id", jsonObject2.getString("category_id") == null ? "" : jsonObject2.getString("category_id"));
                hashMap.put("title", jsonObject2.getString("title") == null ? "" : jsonObject2.getString("title"));
                hashMap.put("nickname", jsonObject2.getString("nickname") == null ? "" : jsonObject2.getString("nickname"));
                hashMap.put("picList", jsonObject2.getString("picList") == null ? "" : jsonObject2.getString("picList"));
                hashMap.put("zanList", jsonObject2.getString("zanList") == null ? "" : jsonObject2.getString("zanList"));
                hashMap.put("commentList", jsonObject2.getString("commentList") == null ? "" : jsonObject2.getString("commentList"));
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
        adapter = new ReportAdapter(array, getActivity());
        list.setAdapter(adapter);
    }
}
