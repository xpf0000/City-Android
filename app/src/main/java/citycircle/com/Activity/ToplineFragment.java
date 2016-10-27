package citycircle.com.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import citycircle.com.Adapter.Myviewpageadapater;
import citycircle.com.Adapter.PhotoAdapter;
import citycircle.com.Adapter.ToplineAdapter;
import citycircle.com.R;
import citycircle.com.Utils.DataString;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;
import citycircle.com.Utils.PreferencesUtils;
import citycircle.com.Utils.StringtoJson;

/**
 * Created by admins on 2015/11/16.
 */
public class ToplineFragment extends Fragment {
    View view, headview, footview;
    ListView toplist;
    ViewPager hviewpage;
    String url, infostr, newsurl, newinfo, addview;
    private ImageView[] indicator_imgs;
    ArrayList<HashMap<String, String>> addarray = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> addmap;
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, Object>> newsid = new ArrayList<HashMap<String, Object>>();
    private List<View> listViews; // 图片组
    Myviewpageadapater myviewpageadapater;
    private View item;
    private LayoutInflater inflater;
    ToplineAdapter adapter;
    //    TextView title;
    SwipeRefreshLayout lehuirefresh;
    int page = 1;
    String titles, classID;
    StringtoJson stringtoJson;
    PhotoAdapter photoAdapter;
    GridView lehuigrid;
    TextView foottxt,date;
    DataString dataString;
    String jsonstrs;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle mBundle = getArguments();
        titles = mBundle.getString("arg");
        classID = mBundle.getString("classID");
        String jsonstr = PreferencesUtils.getString(getActivity(), titles);
        jsonstrs  = PreferencesUtils.getString(getActivity(), titles + "img");
        if (titles.equals("图片")) {
            view = inflater.inflate(R.layout.photo, container, false);
            url = GlobalVariables.urlstr + "News.getBanner&category_id=" + classID;
            newsurl = GlobalVariables.urlstr + "News.getList&category_id=" + classID + "&page=" + page;
            photointview();
            if (jsonstr != null) {
                infostr = jsonstrs;
                newinfo = jsonstr;
                setphoto();
                handler.sendEmptyMessage(1);
            } else {
                setphoto();
                getinfostr(0);
            }
        } else {
            view = inflater.inflate(R.layout.topline, container, false);
            url = GlobalVariables.urlstr + "News.getBanner&category_id=" + classID;
            newsurl = GlobalVariables.urlstr + "News.getList&category_id=" + classID + "&page=" + page;
            intview();
            if (jsonstr != null) {
                infostr = jsonstrs;
                newinfo = jsonstr;
                setlist();
                handler.sendEmptyMessage(1);
            } else {
                setlist();
                getinfostr(0);
            }
        }
        return view;
    }

    //注册非图片新闻控件
    public void intview() {
        footview = LayoutInflater.from(getActivity()).inflate(
                R.layout.list_footview, null);
        foottxt = (TextView) footview.findViewById(R.id.foottxt);
        toplist = (ListView) view.findViewById(R.id.toplist);
        lehuirefresh = (SwipeRefreshLayout) view.findViewById(R.id.Refresh);
        inflater = LayoutInflater.from(getActivity());
        headview = LayoutInflater.from(getActivity()).inflate(
                R.layout.viewpagehead, null);
        hviewpage = (ViewPager) headview.findViewById(R.id.hviewpage);
        date=(TextView)headview.findViewById(R.id.date);
        TextPaint tp = date.getPaint();
        tp.setFakeBoldText(true);
        dataString=new DataString();
        date.setText(dataString.StringData());
//        title = (TextView) headview.findViewById(R.id.title);
        toplist.addHeaderView(headview);
        toplist.addFooterView(footview, null, false);
        toplist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("id", array.get(position - 1).get("id"));
                intent.putExtra("title", array.get(position - 1).get("title"));
                intent.putExtra("description", array.get(position - 1).get("description"));
                intent.putExtra("url", array.get(position - 1).get("url"));
                intent.setClass(getActivity(), NewsInfoActivity.class);
                getActivity().startActivity(intent);
                if (array.get(position - 1).get("ordian").equals("0")) {
                    setListViews(position);
                    addview = GlobalVariables.urlstr + "News.addView&id=" + array.get(position - 1).get("id");
                    getinfostr(1);
                } else {
System.out.println(array.get(position - 1).get("ordian").equals("0"));
                }
            }
        });
        lehuirefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                addarray.clear();
                url = GlobalVariables.urlstr + "News.getBanner&category_id=" + classID;
                array.clear();
                page = 1;
//                newsurl = "http://appapi.rexian.cn:8080/HKCityApi/news/newsFocusList?areaID=1&pageSize=10&pageIndex=" + page;
                newsurl = GlobalVariables.urlstr + "News.getList&category_id=" + classID + "&page=" + page;
                getinfostr(0);
            }
        });
        hviewpage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        lehuirefresh.setEnabled(false);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_DOWN:
                        lehuirefresh.setEnabled(false);
                    case MotionEvent.ACTION_CANCEL:
                        lehuirefresh.setEnabled(true);
                        break;
                }
                return false;
            }
        });
        toplist.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            page++;
                            newsurl = GlobalVariables.urlstr + "News.getList&category_id=" + classID + "&page=" + page;
                            getinfostr(0);
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    //注册图片新闻控件
    public void photointview() {
        lehuigrid = (GridView) view.findViewById(R.id.lehuigrid);
        lehuirefresh = (SwipeRefreshLayout) view.findViewById(R.id.lehuirefresh);
        lehuigrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("id", array.get(position).get("id"));
                intent.setClass(getActivity(), PhotoNewsIn.class);
                getActivity().startActivity(intent);
            }
        });
        lehuirefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                addarray.clear();
                url = GlobalVariables.urlstr + "News.getBanner&category_id=" + classID;
                array.clear();
                page = 1;
//                newsurl = "http://appapi.rexian.cn:8080/HKCityApi/news/newsFocusList?areaID=1&pageSize=10&pageIndex=" + page;
                newsurl = GlobalVariables.urlstr + "News.getList&category_id=" + classID + "&page=" + page;
                getinfostr(0);
            }
        });
        lehuigrid.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            page++;
                            newsurl = GlobalVariables.urlstr + "News.getList&category_id=" + classID + "&page=" + page;
                            getinfostr(0);
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    //注册设置图片新闻
    public void setphoto() {
        photoAdapter = new PhotoAdapter(array, getActivity());
        lehuigrid.setAdapter(photoAdapter);
    }

    public void getinfostr(final int type) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                HttpRequest httpRequest = new HttpRequest();
                if (type == 0) {
                    infostr = httpRequest.doGet(url);
                    newinfo = httpRequest.doGet(newsurl);
                    if (infostr.equals("网络超时")) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(1);
                    }
                } else {
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
                    lehuirefresh.setRefreshing(false);
                    if (titles.equals("图片")) {
                        setlistmap(newinfo);
                        photoAdapter.notifyDataSetChanged();
                    } else {
                        try {
                            getHomeadd(infostr);
                        }catch (Exception e){

                        }
                        listViews = new ArrayList<View>();
                        for (int i = 0; i < addarray.size(); i++) {
                            item = inflater.inflate(R.layout.viewpage, null);
                            ((TextView) item.findViewById(R.id.infor_title))
                                    .setText(addarray.get(i).get("title"));
                            listViews.add(item);
                        }
                        try {
//                            title.setText(addarray.get(0).get("title"));
                        } catch (Exception e) {

                        }

                        myviewpageadapater = new Myviewpageadapater(listViews,
                                getActivity(), addarray,handler);
                        hviewpage.setAdapter(myviewpageadapater);
                        hviewpage.setCurrentItem(1);
                        if (addarray.size() == 0) {
                            hviewpage.setVisibility(View.GONE);
                            date.setVisibility(View.GONE);
                        } else {
                            hviewpage.setVisibility(View.VISIBLE);
                            date.setVisibility(View.VISIBLE);
                        }
                    if (addarray.size()!=0){
                        initIndicator();
                    }else {

                    }

                        hviewpage.setOnPageChangeListener(new MyListener());
                        setlistmap(newinfo);
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 2:
                    lehuirefresh.setRefreshing(false);
                    try {

                        Toast.makeText(getActivity(), "网络似乎有问题了", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    lehuirefresh.setRefreshing(false);
                    try {
                        foottxt.setText("已无更多内容");
//                        Toast.makeText(getActivity(), "暂无更多内容", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    addview = GlobalVariables.urlstr + "News.addView&id=" + addarray.get(GlobalVariables.position).get("id");
                    getinfostr(1);
                    break;
            }
        }
    };

    public void setlist() {
        adapter = new ToplineAdapter(array, getActivity());
        toplist.setAdapter(adapter);
    }

    public void setlistmap(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
        int a = jsonObject1.getIntValue("code");
        if (a == 0) {
            String newsstr = PreferencesUtils.getString(getActivity(), "newid");
            if (newsstr == null) {

            } else {
                HashMap<String, Object> hashMaps = new HashMap<String, Object>();
                JSONObject jsonObject2 = JSON.parseObject(newsstr);
                JSONArray jsonArray = jsonObject2.getJSONArray("news");
                for (int i = 0; i < jsonArray.size(); i++) {
                    hashMaps = new HashMap<String, Object>();
                    JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                    hashMaps.put("id", jsonObject3.getString("id"));
                    newsid.add(hashMaps);
                }
            }
            JSONArray jsonArray = jsonObject1.getJSONArray("info");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                hashMap = new HashMap<>();
                if (newsid.size() == 0) {
                    hashMap.put("ordian", "0");
                } else {
                    for (int j = 0; j < newsid.size(); j++) {
                        if (newsid.get(j).get("id").toString().equals(jsonObject2.getString("id"))) {
                            hashMap.put("ordian", "1");
                            break;
                        } else {
                            hashMap.put("ordian", "0");
                        }
                    }
                }

                hashMap.put("description", jsonObject2.getString("description") == null ? "" : jsonObject2.getString("description"));
                hashMap.put("url", jsonObject2.getString("url") == null ? "" : jsonObject2.getString("url"));
                hashMap.put("id", jsonObject2.getString("id") == null ? "" : jsonObject2.getString("id"));
                hashMap.put("title", jsonObject2.getString("title") == null ? "" : jsonObject2.getString("title"));
                hashMap.put("comment", jsonObject2.getString("comment") == null ? "" : jsonObject2.getString("comment"));
                array.add(hashMap);
            }
            try {
                stringtoJson = new StringtoJson();
                String json = stringtoJson.getJson(array);
                PreferencesUtils.putString(getActivity(), titles, json);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            if (page == 1) {

            } else {
                page--;
            }
            handler.sendEmptyMessage(3);
        }

    }

    public void getHomeadd(String str) {
        JSONObject json = JSONObject.parseObject(str);
        addarray = new ArrayList<HashMap<String, String>>();
        JSONObject jsonObject = json.getJSONObject("data");
        int a = jsonObject.getIntValue("code");
        if (a == 0) {
            JSONArray JSONArray = jsonObject.getJSONArray("info");
            if (jsonstrs==null){
                addmap = new HashMap<String, String>();
                JSONObject JSONObjects = JSONArray.getJSONObject(JSONArray.size()-1);
                addmap.put("id", JSONObjects.getString("id") == null ? "" : JSONObjects.getString("id"));
                addmap.put("title", JSONObjects.getString("title") == null ? "" : JSONObjects.getString("title"));
                addmap.put("url", JSONObjects.getString("url") == null ? "" : JSONObjects.getString("url"));
                addarray.add(addmap);
            }
            for (int i = 0; i < JSONArray.size(); i++) {
                JSONObject JSONObject = JSONArray.getJSONObject(i);
                addmap = new HashMap<String, String>();
                addmap.put("id", JSONObject.getString("id") == null ? "" : JSONObject.getString("id"));
                addmap.put("title", JSONObject.getString("title") == null ? "" : JSONObject.getString("title"));
                addmap.put("url", JSONObject.getString("url") == null ? "" : JSONObject.getString("url"));
                addarray.add(addmap);
            }
            if (jsonstrs==null){
                addmap = new HashMap<String, String>();
                JSONObject JSONObjectss = JSONArray.getJSONObject(0);
                addmap.put("id", JSONObjectss.getString("id") == null ? "" : JSONObjectss.getString("id"));
                addmap.put("title", JSONObjectss.getString("title") == null ? "" : JSONObjectss.getString("title"));
                addmap.put("url", JSONObjectss.getString("url") == null ? "" : JSONObjectss.getString("url"));
                addarray.add(addmap);
            }
            indicator_imgs = new ImageView[addarray.size()-2];
            try {
                stringtoJson = new StringtoJson();
                String jsons = stringtoJson.getJson(addarray);
                PreferencesUtils.putString(getActivity(), titles + "img", jsons);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

        }
    }

    public void setListViews(int position) {
        String newsstr = PreferencesUtils.getString(getActivity(), "newid");
        HashMap<String, Object> hashMaps = new HashMap<String, Object>();
        newsid.clear();
        if (newsstr == null) {
            hashMaps = new HashMap<String, Object>();
            hashMaps.put("id", array.get(position - 1).get("id"));
            newsid.add(hashMaps);
            hashMaps = new HashMap<String, Object>();
            hashMaps.put("news", newsid);
            String str = JSON.toJSONString(hashMaps);
            PreferencesUtils.putString(getActivity(), "newid", str);
        } else {
            JSONObject jsonObject = JSON.parseObject(newsstr);
            JSONArray jsonArray = jsonObject.getJSONArray("news");
            for (int i = 0; i < jsonArray.size(); i++) {
                hashMaps = new HashMap<String, Object>();
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                hashMaps.put("id", jsonObject1.getString("id"));
                newsid.add(hashMaps);
            }
            hashMaps = new HashMap<String, Object>();
            hashMaps.put("id", array.get(position - 1).get("id"));
            newsid.add(hashMaps);
            hashMaps = new HashMap<String, Object>();
            hashMaps.put("news", newsid);
            String str = JSON.toJSONString(hashMaps);
            PreferencesUtils.putString(getActivity(), "newid", str);
        }
        hashMap = new HashMap<String, String>();
        hashMap.put("description", array.get(position - 1).get("description"));
        hashMap.put("url", array.get(position - 1).get("url"));
        hashMap.put("ordian", "1");
        hashMap.put("id", array.get(position - 1).get("id"));
        hashMap.put("title", array.get(position - 1).get("title"));
        hashMap.put("comment", array.get(position - 1).get("comment"));
        array.set(position - 1, hashMap);
        adapter.notifyDataSetChanged();
    }

    private void initIndicator() {
        ImageView imgView;
        View v = headview.findViewById(R.id.drablelayout);// 线性水平布局，负责动态调整导航图标
        ((ViewGroup) v).removeAllViews();
        for (int i = 0; i < indicator_imgs.length; i++) {
            imgView = new ImageView(getActivity());
            LinearLayout.LayoutParams params_linear = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params_linear.setMargins(7, 10, 20, 10);
            params_linear.gravity = Gravity.CENTER;
            imgView.setLayoutParams(params_linear);
            indicator_imgs[i] = imgView;
            if (i == 0) { // 初始化第一个为选中状态

                indicator_imgs[i]
                        .setBackgroundResource(R.mipmap.ic_indicator_on);
            } else {
                indicator_imgs[i].setBackgroundResource(R.mipmap.ic_indicator_off);
            }
            ((ViewGroup) v).addView(indicator_imgs[i]);

        }

    }

    public class MyListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int state) {
            // TODO Auto-generated method stub
            if (state == 0) {
                // new MyAdapter(null).notifyDataSetChanged();
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected( int position) {
            if ( addarray.size() > 1) { //多于1，才会循环跳转
                if (position < 1) { //首位之前，跳转到末尾（N）
                    position = addarray.size()-2;
                    hviewpage.setCurrentItem(position, false);
                } else if (position > (addarray.size()-2)) { //末位之后，跳转到首位（1）
                    hviewpage.setCurrentItem(1, false); //false:不显示跳转过程的动画
                    position = 1;
                }
            }
                try {
                // 改变所有导航的背景图片为：未选中

                for (int i = 0; i < indicator_imgs.length; i++) {

                    indicator_imgs[i]
                            .setBackgroundResource(R.mipmap.ic_indicator_off);

                }
                // 改变当前背景图片为：选中

                indicator_imgs[position-1]
                        .setBackgroundResource(R.mipmap.ic_indicator_on);

            } catch (Exception e) {
                // TODO: handle exception
            }

        }

    }
}
