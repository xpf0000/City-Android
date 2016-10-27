package citycircle.com.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import citycircle.com.Adapter.News_Adapter;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;
import citycircle.com.Utils.PreferencesUtils;

/**
 * Created by admins on 2015/11/26.
 */
public class CityClassList extends Activity {
    ListView newlist;
    String url, urlstr,zanstr,uid,addcom, addcomstr;;
    int page = 1;
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> comarray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> parray = new ArrayList<HashMap<String, String>>();
    News_Adapter news_adapter;
    String zanurl,id;
    SwipeRefreshLayout lehuirefresh;
    RelativeLayout title;
    ImageView back,newsphoto;
    View headview,popView;
    PopupWindow popupWindow;
    EditText myviptxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newphoto);
        id=getIntent().getStringExtra("id");
        uid=PreferencesUtils.getString(CityClassList.this, "userid");
        url = GlobalVariables.urlstr + "Quan.getList&category_id="+id+"&page=" + page+"&uid="+uid;
        intview();
        setlist();
        getnews(0);
    }
    public void intview() {
        newsphoto=(ImageView)findViewById(R.id.newsphoto);

        back=(ImageView)findViewById(R.id.back);
        title=(RelativeLayout)findViewById(R.id.title);
        title.setVisibility(View.VISIBLE);
        lehuirefresh = (SwipeRefreshLayout) findViewById(R.id.Refresh);
        newlist = (ListView) findViewById(R.id.city_news);
        newsphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                int a= PreferencesUtils.getInt(CityClassList.this, "land");
                if (a==0){
                    intent.setClass(CityClassList.this,Logn.class);
                    CityClassList.this.startActivity(intent);
                }else {
                    intent.setClass(CityClassList.this, ReplyPhoto.class);
                    CityClassList.this.startActivity(intent);
                }
            }
        });
        newlist.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            page++;
                            url = GlobalVariables.urlstr + "Quan.getList&category_id="+id+"&page=" + page+"#uid="+uid;
                            getnews(0);
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
                intent.putExtra("id", array.get(position).get("id"));
                intent.setClass(CityClassList.this, Cityinfo.class);
                CityClassList.this.startActivity(intent);
            }
        });
        lehuirefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                array.clear();
                page = 1;
//                newsurl = "http://appapi.rexian.cn:8080/HKCityApi/news/newsFocusList?areaID=1&pageSize=10&pageIndex=" + page;
                url = GlobalVariables.urlstr + "Quan.getList&category_id=" + id + "&page=" + page + "&uid=" + uid;
                getnews(0);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void getnews(final int type) {
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
                    lehuirefresh.setRefreshing(false);
                    getArray(urlstr);
                    news_adapter.notifyDataSetChanged();
                    newlist.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    lehuirefresh.setRefreshing(false);
                    Toast.makeText(CityClassList.this, "网络似乎有问题了", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    lehuirefresh.setRefreshing(false);
                    Toast.makeText(CityClassList.this, "暂无更多内容", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    String did=array.get(GlobalVariables.position).get("id");
                    String username= PreferencesUtils.getString(CityClassList.this, "username");
                    zanurl = GlobalVariables.urlstr + "Quan.addZan&did=" + did + "&username=" + username+"&tuid="+array.get(GlobalVariables.position).get("uid")+"&dpic="+ GlobalVariables.imgurls;
                    getnews(2);
                    break;
                case 5:
                    JSONObject jsonObject = JSON.parseObject(urlstr);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    int a = jsonObject1.getIntValue("code");
                    if (a == 0) {
                        Toast.makeText(CityClassList.this, "赞", Toast.LENGTH_SHORT).show();
//                        addarray.clear();
                        uid=PreferencesUtils.getString(CityClassList.this,"userid");
                        int pagenumber = page * 10;
                        url = GlobalVariables.urlstr + "Quan.getList&category_id="+id+"&page=1&uid="+uid+"&perNumber="+pagenumber;
                        getnews(1);
                    } else {
                        Toast.makeText(CityClassList.this, "失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 6:
                    array.clear();
                    getArray(urlstr);
                    news_adapter.notifyDataSetChanged();
                    break;
                case 7:
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
                    JSONObject jsonObject3 = JSON.parseObject(addcomstr);
                    JSONObject jsonObject4 = jsonObject3.getJSONObject("data");
                    int b = jsonObject4.getIntValue("code");
                    if (b == 0) {
                        Toast.makeText(CityClassList.this, "评论成功", Toast.LENGTH_SHORT).show();
                        popupWindow.dismiss();
                        uid=PreferencesUtils.getString(CityClassList.this,"userid");
                        int pagenumber = page * 10;
                        url = GlobalVariables.urlstr + "Quan.getList&category_id="+id+"&page=1&uid="+uid+"&perNumber="+pagenumber;
                        getnews(1);
//                        handler.sendEmptyMessage(6);
                    } else {
                        Toast.makeText(CityClassList.this, "评论失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 9:
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

    public void setlist() {
        news_adapter = new News_Adapter(array, CityClassList.this, handler);
        newlist.setAdapter(news_adapter);
    }
    public void showpop(final int type) {
        popView = CityClassList.this.getLayoutInflater().inflate(
                R.layout.comment_pop, null);
        WindowManager windowManager =  CityClassList.this.getWindowManager();
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
                    Toast.makeText(CityClassList.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    String username = PreferencesUtils.getString(CityClassList.this, "username");
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
}
