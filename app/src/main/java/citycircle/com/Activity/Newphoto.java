package citycircle.com.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import citycircle.com.Adapter.Addadpter;
import citycircle.com.Adapter.News_Adapter;
import citycircle.com.MyAppService.CityServices;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;
import citycircle.com.Utils.ImageUtils;
import citycircle.com.Utils.PreferencesUtils;

/**
 * Created by admins on 2015/11/23.
 */
public class Newphoto extends Fragment {
    View view, adddview;
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
    View headview,popView;
    PopupWindow popupWindow;
    TextView message;
    ImageView head;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    citycircle.com.Utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    ViewPager hviewpage;
    private List<View> listViews; // 图片组
    private View item;
    private LayoutInflater inflater;
    Addadpter myviewpageadapater;
    private ImageView[] indicator_imgs;
    int po = 0;
    final Handler handlers = new Handler();
     EditText myviptxt;
    int type=0;
    View footview;
    TextView mestst;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.newphoto, container, false);
        headview = LayoutInflater.from(getActivity()).inflate(
                R.layout.newmessage, null);
        adddview = LayoutInflater.from(getActivity()).inflate(
                R.layout.addviewpage, null);

        addurl = GlobalVariables.urlstr + "News.getGuanggao&typeid=84";
        int a = PreferencesUtils.getInt(getActivity(), "land");
        uid=PreferencesUtils.getString(getActivity(),"userid");
        if (a == 0) {
            getnewme = GlobalVariables.urlstr + "Quan.getNewsTop&username=0";
        } else {
            username = PreferencesUtils.getString(getActivity(), "username");
            getnewme = GlobalVariables.urlstr + "Quan.getNewsTop&username=" + username;
        }
        url = GlobalVariables.urlstr + "Quan.getListAll&page=" + page+"&uid="+uid;
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
        inflater = LayoutInflater.from(getActivity());
        hviewpage = (ViewPager) adddview.findViewById(R.id.hviewpage);
        head = (ImageView) headview.findViewById(R.id.head);
        message = (TextView) headview.findViewById(R.id.message);
        lehuirefresh = (SwipeRefreshLayout) view.findViewById(R.id.Refresh);
        newlist = (ListView) view.findViewById(R.id.city_news);
        newlist.addFooterView(footview,null,false);
        newlist.addHeaderView(adddview);
        newlist.addHeaderView(headview);
        headview.setVisibility(View.GONE);
        handler.postDelayed(task, 5000);
        headview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), Messagelist.class);
                getActivity().startActivity(intent);
                headview.setPadding(0, -headview.getHeight(), 0, 0);
                headview.setVisibility(View.GONE);
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
                    case MotionEvent.ACTION_CANCEL:
                        lehuirefresh.setEnabled(true);
                        break;
                }
                return false;
            }
        });
        newlist.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                          if (type==1){
                              page++;
                              url = GlobalVariables.urlstr + "Quan.getListAll&page=" + page + "&uid=" + uid;
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
                intent.putExtra("id", array.get(position - 2).get("id"));
//                intent.putExtra("orzan", array.get(position - 2).get("orzan"));
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
                uid=PreferencesUtils.getString(getActivity(),"userid");
                int a = PreferencesUtils.getInt(getActivity(), "land");
                if (a == 0) {
                    getnewme = GlobalVariables.urlstr + "Quan.getNewsTop&username=0";
                } else {
                    username = PreferencesUtils.getString(getActivity(), "username");
                    getnewme = GlobalVariables.urlstr + "Quan.getNewsTop&username=" + username;
                }
//                newsurl = "http://appapi.rexian.cn:8080/HKCityApi/news/newsFocusList?areaID=1&pageSize=10&pageIndex=" + page;
                url = GlobalVariables.urlstr + "Quan.getListAll&page=" + page + "&uid=" + uid;
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
                    if (addarray.size()==0){
                        hviewpage.setVisibility(View.GONE);
                        adddview.setVisibility(View.GONE);
                    }else {
                        hviewpage.setVisibility(View.VISIBLE);
                        adddview.setVisibility(View.VISIBLE);
                    }
                    newlist.setVisibility(View.VISIBLE);
                    setheadadd();
                    if(page==1){
                        initIndicator();
                    }else {

                    }
                    hviewpage.setOnPageChangeListener(new MyListener());
                    if (mesarray.size() == 0) {
                        headview.setPadding(0, -140, 0, 0);
                        headview.setVisibility(View.GONE);
                    } else {
                        String url = mesarray.get(0).get("top");
                        ImageLoader.displayImage(url, head, options,
                                animateFirstListener);
                        headview.setPadding(20, 20, 20, 20);
                        headview.setVisibility(View.VISIBLE);
                        message.setText(mesarray.get(0).get("count") + "条新消息");
                    }
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
                    String username = PreferencesUtils.getString(getActivity(), "username");
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
                        uid=PreferencesUtils.getString(getActivity(),"userid");
                        int pagenumber = page * 10;
                        url = GlobalVariables.urlstr + "Quan.getListAll&page=" + 1 + "&perNumber=" + pagenumber+"&uid="+uid;
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
                        uid=PreferencesUtils.getString(getActivity(),"userid");
                        int pagenumber = page * 10;
                        url = GlobalVariables.urlstr + "Quan.getListAll&page=" + 1 + "&perNumber=" + pagenumber+"&uid="+uid;
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
            indicator_imgs = new ImageView[jsonArray.size()];
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
                url = GlobalVariables.urlstr + "Quan.getListAll&page=" + page+"&uid="+uid;
                getnews(0);
            }
        }, 2000);

    }

    public void setlist() {
        news_adapter = new News_Adapter(array, getActivity(), handler);
        newlist.setAdapter(news_adapter);
    }

    public void setheadadd() {
        listViews = new ArrayList<View>();
        for (int i = 0; i < addarray.size(); i++) {
            item = inflater.inflate(R.layout.viewpage, null);
            ((TextView) item.findViewById(R.id.infor_title))
                    .setText("");
            listViews.add(item);
        }
        myviewpageadapater = new Addadpter(listViews,
                getActivity(), addarray);
        hviewpage.setAdapter(myviewpageadapater);
    }

    private void initIndicator() {

        ImageView imgView;
        View v = view.findViewById(R.id.dian);// 线性水平布局，负责动态调整导航图标
        ((ViewGroup) v).removeAllViews();
        for (int i = 0; i < addarray.size(); i++) {
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
        //
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
        public void onPageSelected(final int position) {
            try {
                // 改变所有导航的背景图片为：未选中

                for (int i = 0; i < indicator_imgs.length; i++) {

                    indicator_imgs[i]
                            .setBackgroundResource(R.mipmap.ic_indicator_off);

                }
                // 改变当前背景图片为：选中

                indicator_imgs[position]
                        .setBackgroundResource(R.mipmap.ic_indicator_on);

            } catch (Exception e) {
                // TODO: handle exception
            }

        }

    }

    //图片定时轮播


        Runnable task = new Runnable() {
            private boolean run = true;

            public void run() {
                // TODO Auto-generated method stub

                if (run) {
                    handlers.postDelayed(this, 5000);
                    if (po < addarray.size()) {
                        po++;
                    } else {
                        po = 0;
                    }
                }
                hviewpage.setCurrentItem(po);
            }
        };
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
                String username = PreferencesUtils.getString(getActivity(), "username");
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
