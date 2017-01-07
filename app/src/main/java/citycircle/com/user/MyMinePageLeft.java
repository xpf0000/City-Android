package citycircle.com.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import citycircle.com.Activity.Cityinfo;
import citycircle.com.Adapter.News_Adapter;
import citycircle.com.MyAppService.CityServices;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;
import citycircle.com.Utils.ImageUtils;

import static citycircle.com.MyAppService.LocationApplication.APPDataCache;

/**
 * Created by X on 2016/11/7.
 */

public class MyMinePageLeft extends Fragment {
    View view;
    ListView newlist;
    String url, urlstr, zanstr, getnewme, newmestr, username, addurl, addstr,addcom, addcomstr;
    int page = 1;
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> mesarray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> addarray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> comarray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> parray = new ArrayList<HashMap<String, String>>();
    News_Adapter news_adapter;
    String zanurl,uid;
    SwipeRefreshLayout lehuirefresh;

    View popView;

    PopupWindow popupWindow;

    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    citycircle.com.Utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;

    EditText myviptxt;
    int type=0;
    View footview;
    TextView mestst;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.newphoto, container, false);
        addurl = GlobalVariables.urlstr + "News.getGuanggao&typeid=84";
        int a = APPDataCache.land;
        username = APPDataCache.User.getUsername();
        uid = APPDataCache.User.getUid();
        if (a == 0) {
            getnewme = GlobalVariables.urlstr + "Quan.getNewsTop&username=0";
        } else {
            getnewme = GlobalVariables.urlstr + "Quan.getNewsTop&username=" + username;
        }

        url = GlobalVariables.urlstr + "quan.getMyList&page=" + page+"&uid="+uid;

        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
        options = ImageUtils.setOptions();
        Intent intent = new Intent(getActivity(), CityServices.class);
        getActivity().startService(intent);
        IntentFilter filter = new IntentFilter(CityServices.action);
        getActivity().registerReceiver(broadcastReceiver, filter);
        intview();
        setlist();
        getnews(0);
        return view;
    }

    public void intview() {
        footview=LayoutInflater.from(getActivity()).inflate(R.layout.footview,null);
        mestst=(TextView)footview.findViewById(R.id.mestst) ;
        mestst.setText("正在加载更多...");

        lehuirefresh = (SwipeRefreshLayout) view.findViewById(R.id.Refresh);
        newlist = (ListView) view.findViewById(R.id.city_news);
        newlist.addFooterView(footview,null,false);

        newlist.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            if (type==1){
                                page++;
                                url = GlobalVariables.urlstr + "quan.getMyList&page=" + page + "&uid=" + uid;
                                getnews(0);
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        newlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("id", array.get(position - newlist.getHeaderViewsCount()).get("id"));
                intent.setClass(getActivity(), Cityinfo.class);
                getActivity().startActivity(intent);
            }
        });
        lehuirefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                array.clear();
                page = 1;
                mesarray.clear();
                username = APPDataCache.User.getUsername();
                uid = APPDataCache.User.getUid();
                int a = APPDataCache.land;
                if (a == 0) {
                    getnewme = GlobalVariables.urlstr + "Quan.getNewsTop&username=0";
                } else {
                    getnewme = GlobalVariables.urlstr + "Quan.getNewsTop&username=" + username;
                }
//                newsurl = "http://appapi.rexian.cn:8080/HKCityApi/news/newsFocusList?areaID=1&pageSize=10&pageIndex=" + page;
                url = GlobalVariables.urlstr + "quan.getMyList&page=" + page + "&uid=" + uid;
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
                    addstr = httpRequest.doGet(addurl);
                    newmestr = httpRequest.doGet(getnewme);
                    urlstr = httpRequest.doGet(url);
                    if (urlstr.equals("网络超时")) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(1);
                    }
                }else if (type==1){
                    urlstr = httpRequest.doGet(url);
                    if (urlstr.equals("网络超时")) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(6);
                    }
                }else if (type==4){
                    addcomstr = httpRequest.doGet(addcom);
                    if (addcomstr.equals("网络超时")) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(8);

                    }
                }
                else {
                    zanstr = httpRequest.doGet(zanurl);
                    if (zanstr.equals("网络超时")) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(5);
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
                    type=1;
                    lehuirefresh.setRefreshing(false);
                    addarray.clear();
                    getArray(urlstr);
                    setMesarray(newmestr);
                    setAddarray(addstr);
                    news_adapter.notifyDataSetChanged();
                    newlist.setVisibility(View.VISIBLE);

                    break;
                case 2:
                    type=1;
                    lehuirefresh.setRefreshing(false);
                    Toast.makeText(getActivity(), "网络似乎有问题了", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    type=1;
                    lehuirefresh.setRefreshing(false);
                    Toast.makeText(getActivity(), "暂无更多内容", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    type=1;
                    String did = array.get(GlobalVariables.position).get("id");
                    String username = APPDataCache.User.getUsername();
                    zanurl = GlobalVariables.urlstr + "Quan.addZan&did=" + did + "&username=" + username + "&tuid=" + array.get(GlobalVariables.position).get("uid") + "&dpic=" + GlobalVariables.imgurls;
                    getnews(2);
                    break;
                case 5:
                    type=1;
                    JSONObject jsonObject = JSON.parseObject(zanstr);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    int a = jsonObject1.getIntValue("code");
                    if (a == 0) {
                        Toast.makeText(getActivity(), "赞", Toast.LENGTH_SHORT).show();
//                        addarray.clear();

                        uid=APPDataCache.User.getUid();
                        int pagenumber = page * 10;
                        url = GlobalVariables.urlstr + "quan.getMyList&page=" + 1 + "&perNumber=" + pagenumber+"&uid="+uid;
                        getnews(1);
                    } else {
                        Toast.makeText(getActivity(), "失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 6:
                    type=1;
                    array.clear();
                    getArray(urlstr);
                    news_adapter.notifyDataSetChanged();
                    break;
                case 7:
                    type=1;
                    getarray();
                    showpop(1);
                    popView.setVisibility(View.GONE);
                    myviptxt.setFocusable(true);
                    myviptxt.setFocusableInTouchMode(true);
                    myviptxt.requestFocus();
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                                       public void run() {
                                           InputMethodManager inputManager =
                                                   (InputMethodManager) myviptxt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                           inputManager.showSoftInput(myviptxt, 0);
                                       }
                                   },
                            100);
                    popView.setVisibility(View.VISIBLE);
                    break;
                case 8:
                    type=1;
                    JSONObject jsonObject3 = JSON.parseObject(addcomstr);
                    JSONObject jsonObject4 = jsonObject3.getJSONObject("data");
                    int b = jsonObject4.getIntValue("code");
                    if (b == 0) {
                        Toast.makeText(getActivity(), "评论成功", Toast.LENGTH_SHORT).show();
                        popupWindow.dismiss();
                        uid=APPDataCache.User.getUid();
                        int pagenumber = page * 10;
                        url = GlobalVariables.urlstr + "quan.getMyList&page=" + 1 + "&perNumber=" + pagenumber+"&uid="+uid;
                        getnews(1);
//                        handler.sendEmptyMessage(6);
                    } else {
                        Toast.makeText(getActivity(), "评论失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 9:
                    type=1;
                    getarray();
                    showpop(0);
                    popView.setVisibility(View.GONE);
                    myviptxt.setFocusable(true);
                    myviptxt.setFocusableInTouchMode(true);
                    myviptxt.requestFocus();
                    Timer timers = new Timer();
                    timers.schedule(new TimerTask() {
                                        public void run() {
                                            InputMethodManager inputManager =
                                                    (InputMethodManager) myviptxt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                            inputManager.showSoftInput(myviptxt, 0);
                                        }
                                    },
                            100);
                    popView.setVisibility(View.VISIBLE);
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
                hashMap.put("uid", jsonObject2.getString("uid") == null ? "" : jsonObject2.getString("uid"));
                hashMap.put("sex", jsonObject2.getString("sex") == null ? "" : jsonObject2.getString("sex"));
                hashMap.put("orzan", jsonObject2.getString("orzan") == null ? "" : jsonObject2.getString("orzan"));
                hashMap.put("headimage", jsonObject2.getString("headimage") == null ? "" : jsonObject2.getString("headimage"));
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

    public void setMesarray(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
        int a = jsonObject1.getIntValue("code");
        if (a == 0) {
            JSONObject jsonObject2 = jsonObject1.getJSONObject("info");
            hashMap = new HashMap<>();
            hashMap.put("count", jsonObject2.getString("count") == null ? "" : jsonObject2.getString("count"));
            hashMap.put("top", jsonObject2.getString("top") == null ? "" : jsonObject2.getString("top"));
            mesarray.add(hashMap);

        } else {
        }
    }

    public void setAddarray(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
        if (jsonObject1.getIntValue("code") == 0) {
            JSONArray jsonArray = jsonObject1.getJSONArray("info");

            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                hashMap = new HashMap<>();
                hashMap.put("picurl", jsonObject2.getString("picurl") == null ? "" : jsonObject2.getString("picurl"));
                hashMap.put("url", jsonObject2.getString("url") == null ? "" : jsonObject2.getString("url"));
                addarray.add(hashMap);
            }
        } else {

        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int a = intent.getExtras().getInt("meeage");
            if (a == 4) {
                lehuirefresh.post(new Runnable() {
                    @Override
                    public void run() {
                        lehuirefresh.setRefreshing(true);
                    }
                });
                onRefresh();
            }
        }

    };

    public void onRefresh() {
        lehuirefresh.postDelayed(new Runnable() {

            @Override
            public void run() {
                // 更新数据
                array.clear();
                mesarray.clear();
                addarray.clear();
                page = 1;
//                newsurl = "http://appapi.rexian.cn:8080/HKCityApi/news/newsFocusList?areaID=1&pageSize=10&pageIndex=" + page;
                url = GlobalVariables.urlstr + "quan.getMyList&page=" + page+"&uid="+uid;
                getnews(0);
            }
        }, 2000);

    }

    public void setlist() {
        news_adapter = new News_Adapter(array, getActivity(), handler);
        newlist.setAdapter(news_adapter);
    }


    //评论弹窗
    public void showpop(final int type) {
        popView = getActivity().getLayoutInflater().inflate(
                R.layout.comment_pop, null);
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        popupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        popupWindow.showAtLocation(popView,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        myviptxt= (EditText) popView.findViewById(R.id.popedttxt);
        Button back = (Button) popView.findViewById(R.id.back);
        Button update = (Button) popView.findViewById(R.id.update);
        if (type==0){
            myviptxt.setHint("回复"+comarray.get(GlobalVariables.positions).get("nickname"));
        }else {

        }
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (myviptxt.getText().toString().trim().length() == 0) {
                    Toast.makeText(getActivity(), "内容不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    String username = APPDataCache.User.getUsername();
                    try {
                        if (type==0){
                            addcom = GlobalVariables.urlstr + "Quan.addComment&did=" + array.get(GlobalVariables.position).get("id") + "&username=" + username + "&content=" + URLEncoder.encode(myviptxt.getText().toString(), "UTF-8")+"&tuid="+comarray.get(GlobalVariables.positions).get("uid")+"&dpic="+parray.get(0).get("path")+"&type=1";
                        }else {
                            addcom = GlobalVariables.urlstr + "Quan.addComment&did=" + array.get(GlobalVariables.position).get("id") + "&username=" + username + "&content=" + URLEncoder.encode(myviptxt.getText().toString(), "UTF-8")+"&tuid="+array.get(GlobalVariables.position).get("uid")+"&dpic="+parray.get(0).get("path")+"&type=0";
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    getnews(4);
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindow.dismiss();
            }
        });
    }
    public void getarray(){
        comarray.clear();
        parray.clear();
        String str=array.get(GlobalVariables.position).get("picList");
        JSONArray jsonArray=JSON.parseArray(str);
        for (int i=0;i<jsonArray.size();i++){
            hashMap=new HashMap<>();
            JSONObject jsonObject2=jsonArray.getJSONObject(i);
            hashMap.put("path",jsonObject2.getString("url")== null ? "" : jsonObject2.getString("url"));
            hashMap.put("width",jsonObject2.getString("width")== null ? "" : jsonObject2.getString("width"));
            hashMap.put("height",jsonObject2.getString("height")== null ? "" : jsonObject2.getString("height"));
            parray.add(hashMap);
        }
        String comstr=array.get(GlobalVariables.position).get("commentList");
        JSONArray jsonArray1=JSON.parseArray(comstr);
        for (int i=0;i<jsonArray1.size();i++){
            JSONObject jsonObject2=jsonArray1.getJSONObject(i);
            hashMap=new HashMap<>();
            String name = jsonObject2.getString("nickname") == null ? "" : jsonObject2.getString("nickname");
            String tnickname=jsonObject2.getString("tnickname") == null ? "" : jsonObject2.getString("tnickname");
            hashMap.put("content", jsonObject2.getString("content") == null ? "" : jsonObject2.getString("content"));
            hashMap.put("id", jsonObject2.getString("id") == null ? "" : jsonObject2.getString("id"));
            hashMap.put("uid", jsonObject2.getString("uid") == null ? "" : jsonObject2.getString("uid"));
            hashMap.put("tnickname",tnickname );
            hashMap.put("nickname", name);
            comarray.add(hashMap);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
    }
}
