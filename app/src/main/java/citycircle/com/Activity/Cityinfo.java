package citycircle.com.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
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

import citycircle.com.Adapter.ComentAdapter;
import citycircle.com.Adapter.newPhotoAdfapters;
import citycircle.com.MyViews.MyGridView;
import citycircle.com.R;
import citycircle.com.Utils.DateUtils;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;
import citycircle.com.Utils.ImageUtils;
import citycircle.com.Utils.PreferencesUtils;
import citycircle.com.Utils.Timechange;

/**
 * Created by admins on 2015/11/25.
 */
public class Cityinfo extends Activity implements View.OnClickListener {
    TextView title, view, usename, time, content, post, zan, zans, collect,number,stat;
    MyGridView photogrid;
    ListView commentlist;
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> comhashMap;
    ArrayList<HashMap<String, String>> comarray = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> phashMap;
    ArrayList<HashMap<String, String>> parray = new ArrayList<HashMap<String, String>>();
    String url, urlstr, id, comrnturl, comentstr;
    newPhotoAdfapters newPhotoAdapter;
    ComentAdapter adapter;
    int page = 1;
    View headview;
    ImageView back,uesrhead;
    String zanstr = "", zanurl, zanstrs, addcom, addcomstr,orzan,uid;
    View popView,footview;
    PopupWindow popupWindow;
    DateUtils dateUtils;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    citycircle.com.Utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
     EditText myviptxt;
    LinearLayout zanlay;
    int po;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cityinfo);
        id = getIntent().getStringExtra("id");
        orzan=getIntent().getStringExtra("orzan");
        dateUtils =new DateUtils();
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(Cityinfo.this));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
        options=ImageUtils.setCirclelmageOptions();
        uid=PreferencesUtils.getString(Cityinfo.this,"userid");
        url = GlobalVariables.urlstr + "Quan.getArticle&id=" + id+"&uid="+uid;
        comrnturl = GlobalVariables.urlstr + "Quan.getComment&id=" + id + "&page=" + page;
        intview();
        setlist();
        getnews(0);
    }

    public void intview() {
        footview=LayoutInflater.from(Cityinfo.this).inflate(
                R.layout.cityfootview, null);
        stat=(TextView)footview.findViewById(R.id.stat);
        zans = (TextView) findViewById(R.id.zans);
        zans.setOnClickListener(this);
        collect = (TextView) findViewById(R.id.collect);
        collect.setOnClickListener(this);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        commentlist = (ListView) findViewById(R.id.commentlist);
        commentlist.addFooterView(footview, null, false);
        headview = LayoutInflater.from(Cityinfo.this).inflate(
                R.layout.headview, null);
        uesrhead=(ImageView)headview.findViewById(R.id.uesrhead);
        title = (TextView) headview.findViewById(R.id.title);
        title.setOnClickListener(this);
        usename = (TextView) headview.findViewById(R.id.usename);
        time = (TextView) headview.findViewById(R.id.time);
        zanlay=(LinearLayout)headview.findViewById(R.id.zanlay);
        content = (TextView) headview.findViewById(R.id.content);
        post = (TextView) headview.findViewById(R.id.post);
        zan = (TextView) headview.findViewById(R.id.zan);
        view = (TextView) headview.findViewById(R.id.view);
        number=(TextView)headview.findViewById(R.id.number);
        photogrid = (MyGridView) headview.findViewById(R.id.photogrid);
        commentlist.addHeaderView(headview);
        photogrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("pos", position);
                intent.setClass(Cityinfo.this, PhotoLook.class);
                Cityinfo.this.startActivity(intent);
            }
        });
        commentlist.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            zanstr = "";
                            page++;
                            comrnturl = GlobalVariables.urlstr + "Quan.getComment&id=" + id + "&page=" + page;
                            getnews(0);
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        commentlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){

                }else {
                    int a = PreferencesUtils.getInt(Cityinfo.this, "land");
                    if (a==0){
                        Intent intent = new Intent();
                        intent.setClass(Cityinfo.this, Logn.class);
                        Cityinfo.this.startActivity(intent);
                    }else {
                        String nickname=PreferencesUtils.getString(Cityinfo.this,"nickname");
                        if (comarray.get(position-1).get("nickname").equals(nickname)){

                        }else {
                            po=position;
                            showpop(0);
                            popView.setVisibility(View.GONE);
                            myviptxt.setFocusable(true);
                            myviptxt.setFocusableInTouchMode(true);
                            myviptxt.requestFocus();
                            Timer timer = new Timer();
                            timer.schedule(new TimerTask()
                                           {
                                               public void run()
                                               {
                                                   InputMethodManager inputManager =
                                                           (InputMethodManager) myviptxt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                                   inputManager.showSoftInput(myviptxt, 0);
                                               }
                                           },
                                    100);
                            popView.setVisibility(View.VISIBLE);
                        }
                    }
                }
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
                    comentstr = httpRequest.doGet(comrnturl);
                    urlstr = httpRequest.doGet(url);
                    if (urlstr.equals("网络超时")) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(1);

                    }
                } else if (type == 1) {
                    zanstrs = httpRequest.doGet(zanurl);
                    if (zanstrs.equals("网络超时")) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(4);

                    }
                } else {
                    addcomstr = httpRequest.doGet(addcom);
                    if (addcomstr.equals("网络超时")) {
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
                    try {
                        getcoment(comentstr);
                        getArray(urlstr);

                        title.setText(array.get(0).get("title"));
                        usename.setText(array.get(0).get("nickname"));
                        content.setText(array.get(0).get("content"));
//                    post.setText(array.get(0).get("title"));
                        view.setText(array.get(0).get("view"));
                        if (array.get(0).get("location").equals("null")){
                            post.setVisibility(View.INVISIBLE);
                        }else {
                            post.setVisibility(View.VISIBLE);
                            post.setText(array.get(0).get("location"));
                        }

                        number.setText(array.get(0).get("zan"));
                        if (zanstr.length()==0){
                            zanlay.setVisibility(View.GONE);
                        }else {
                            zanlay.setVisibility(View.VISIBLE);
                        }

                        zan.setText(zanstr);
//                        zans.setText(array.get(0).get("zan"));
                        collect.setText(array.get(0).get("comment"));
                        orzan=array.get(0).get("orzan");
                        if (array.get(0).get("sex").equals("0")){
                            Drawable drawable= getResources().getDrawable(R.mipmap.woman);
                            usename.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable,null);
                        }else {
                            Drawable drawable= getResources().getDrawable(R.mipmap.man);
                            usename.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable,null);
                        }
                        if (orzan.equals("0")){
                            Drawable drawable= getResources().getDrawable(R.mipmap.ic_pin_count_);
                            zans.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
                        }else {
                            Drawable drawable= getResources().getDrawable(R.mipmap.ic_pin_count_2);
                            zans.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
                        }
                        ImageLoader.displayImage(array.get(0).get("headimage"), uesrhead, options,
                                animateFirstListener);
                    adapter.notifyDataSetChanged();
                        commentlist.setVisibility(View.VISIBLE);
                    if (comarray.size()==0){

                    }else if (!(comarray.size()%10==0)){
                        stat.setText("没有更多内容了");
                    }else {
                        stat.setText("正在加载...");
                    }
                    } catch (Exception e) {
                        handler.sendEmptyMessage(2);
                    }
                    setgrid();
                    Timechange timechange=new Timechange();
                    String times= timechange.Time(dateUtils.getDateToStringss(Long.parseLong(array.get(0).get("create_time"))));
                    time.setText(times);
                    break;
                case 2:
                    Toast.makeText(Cityinfo.this, "网络似乎有问题了", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
//                    Toast.makeText(Cityinfo.this, "暂无更多内容", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    JSONObject jsonObject = JSON.parseObject(urlstr);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    int a = jsonObject1.getIntValue("code");
                    if (a == 0) {
                        Toast.makeText(Cityinfo.this, "赞", Toast.LENGTH_SHORT).show();
                        orzan="1";
                        Drawable drawable= getResources().getDrawable(R.mipmap.ic_pin_count_2);
                        zans.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
                        handler.sendEmptyMessage(6);
                    } else {
                        Toast.makeText(Cityinfo.this, "失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 5:
                    JSONObject jsonObject3 = JSON.parseObject(urlstr);
                    JSONObject jsonObject4 = jsonObject3.getJSONObject("data");
                    int b = jsonObject4.getIntValue("code");
                    if (b == 0) {
                        Toast.makeText(Cityinfo.this, "评论成功", Toast.LENGTH_SHORT).show();
                        popupWindow.dismiss();
                        handler.sendEmptyMessage(6);
                    } else {
                        Toast.makeText(Cityinfo.this, "评论失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 6:
                    zanstr = "";
                    array.clear();
                    parray.clear();
                    comarray.clear();
                    page = 1;
                    url = GlobalVariables.urlstr + "Quan.getArticle&id=" + id+"&uid="+uid;
                    comrnturl = GlobalVariables.urlstr + "Quan.getComment&id=" + id + "&page=" + page;
                    getnews(0);
                    break;
            }
        }
    };

    public void getArray(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
        int a = jsonObject1.getIntValue("code");
        if (a == 0) {
            array = new ArrayList<HashMap<String, String>>();
//            JSONObject jsonObject2 = jsonObject1.getJSONObject("info");
            JSONArray jsonArray = jsonObject1.getJSONArray("info");
            JSONObject jsonObject3 = jsonArray.getJSONObject(0);
            hashMap = new HashMap<>();
            hashMap.put("view", jsonObject3.getString("view") == null ? "" : jsonObject3.getString("view"));
            hashMap.put("headimage", jsonObject3.getString("headimage") == null ? "" : jsonObject3.getString("headimage"));
            hashMap.put("sex", jsonObject3.getString("sex") == null ? "" : jsonObject3.getString("sex"));
            hashMap.put("orzan", jsonObject3.getString("orzan") == null ? "" : jsonObject3.getString("orzan"));
            hashMap.put("content", jsonObject3.getString("content") == null ? "" : jsonObject3.getString("content"));
            hashMap.put("location", jsonObject3.getString("location") == null ? "" : jsonObject3.getString("location"));
            hashMap.put("id", jsonObject3.getString("id") == null ? "" : jsonObject3.getString("id"));
            hashMap.put("uid", jsonObject3.getString("uid") == null ? "" : jsonObject3.getString("uid"));
            hashMap.put("comment", jsonObject3.getString("comment") == null ? "" : jsonObject3.getString("comment"));
            hashMap.put("zan", jsonObject3.getString("zan") == null ? "" : jsonObject3.getString("zan"));
            hashMap.put("create_time", jsonObject3.getString("create_time") == null ? "" : jsonObject3.getString("create_time"));
            hashMap.put("category_id", jsonObject3.getString("category_id") == null ? "" : jsonObject3.getString("category_id"));
            hashMap.put("title", jsonObject3.getString("title") == null ? "" : jsonObject3.getString("title"));
            hashMap.put("nickname", jsonObject3.getString("nickname") == null ? "" : jsonObject3.getString("nickname"));
//            hashMap.put("zanList", jsonObject3.getString("zanList") == null ? "" : jsonObject3.getString("zanList"));
            array.add(hashMap);
            JSONArray jsonArray1 = jsonObject3.getJSONArray("picList");
            parray = new ArrayList<HashMap<String, String>>();
            for (int j = 0; j < jsonArray1.size(); j++) {
                JSONObject jsonObject4 = jsonArray1.getJSONObject(j);
                phashMap = new HashMap<>();
                phashMap.put("path", jsonObject4.getString("url") == null ? "" : jsonObject4.getString("url"));
                phashMap.put("width",jsonObject4.getString("width")== null ? "" : jsonObject4.getString("width"));
                phashMap.put("height",jsonObject4.getString("height")== null ? "" : jsonObject4.getString("height"));
                parray.add(phashMap);
            }
            GlobalVariables.parrays=parray;
            JSONArray jsonArray2 = jsonObject3.getJSONArray("zanList");
            if (jsonArray2.size() == 0) {
                zan.setVisibility(View.GONE);
            } else {
                for (int i = 0; i < jsonArray2.size(); i++) {
                    JSONObject jsonObject2 = jsonArray2.getJSONObject(i);
                    if (i==0){
                        zanstr= jsonObject2.getString("nickname");
                    }else {
                        zanstr = zanstr +","+ jsonObject2.getString("nickname");
                    }

                }
                zan.setVisibility(View.VISIBLE);
            }


        } else {
            handler.sendEmptyMessage(3);
        }

    }

    public void getcoment(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
        int a = jsonObject1.getIntValue("code");
        if (a == 0) {
            JSONArray jsonArray = jsonObject1.getJSONArray("info");
            for (int z = 0; z < jsonArray.size(); z++) {
                JSONObject jsonObject3 = jsonArray.getJSONObject(z);
                comhashMap = new HashMap<>();
                String name = jsonObject3.getString("nickname") == null ? "" : jsonObject3.getString("nickname");
                String tnickname=jsonObject3.getString("tnickname") == null ? "" : jsonObject3.getString("tnickname");
                comhashMap.put("content", jsonObject3.getString("content") == null ? "" : jsonObject3.getString("content"));
                comhashMap.put("id", jsonObject3.getString("id") == null ? "" : jsonObject3.getString("id"));
                comhashMap.put("uid", jsonObject3.getString("uid") == null ? "" : jsonObject3.getString("uid"));
                comhashMap.put("headimage", jsonObject3.getString("headimage") == null ? "" : jsonObject3.getString("headimage"));
                comhashMap.put("create_time", jsonObject3.getString("create_time") == null ? "" : jsonObject3.getString("create_time"));
                comhashMap.put("tnickname",tnickname );
                comhashMap.put("nickname", name);
                comarray.add(comhashMap);
            }
        } else {
            handler.sendEmptyMessage(3);
        }
    }

    public void setgrid() {
        if (parray.size() == 1) {
            photogrid.setNumColumns(1);
        } else if (parray.size() == 2) {
            photogrid.setNumColumns(2);
        } else {

        }
        newPhotoAdapter = new newPhotoAdfapters(parray, Cityinfo.this);
        photogrid.setAdapter(newPhotoAdapter);
    }

    public void setlist() {
        adapter=new ComentAdapter(comarray,Cityinfo.this);
//        adapter = new SimpleAdapter(Cityinfo.this, comarray, R.layout.comment_item, new String[]{"content","tnickname", "nickname"}, new int[]{R.id.comment, R.id.tname,R.id.name});
        commentlist.setAdapter(adapter);
    }

    public void showpop(final int type) {
        popView = getLayoutInflater().inflate(
                R.layout.comment_pop, null);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        popupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        popupWindow.showAtLocation(popView,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        myviptxt= (EditText) popView.findViewById(R.id.popedttxt);
        Button back = (Button) popView.findViewById(R.id.back);
        Button update = (Button) popView.findViewById(R.id.update);
        if (type==0){
            myviptxt.setHint("回复"+comarray.get(po-1).get("nickname"));
        }else {

        }
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (myviptxt.getText().toString().trim().length() == 0) {
                    Toast.makeText(Cityinfo.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    String username = PreferencesUtils.getString(Cityinfo.this, "username");
                    try {
                        if (type==0){
                            addcom = GlobalVariables.urlstr + "Quan.addComment&did=" + id + "&username=" + username + "&content=" + URLEncoder.encode(myviptxt.getText().toString(), "UTF-8")+"&tuid="+comarray.get(po-1).get("uid")+"&dpic="+parray.get(0).get("path")+"&type=1";
                        }else {
                        addcom = GlobalVariables.urlstr + "Quan.addComment&did=" + id + "&username=" + username + "&content=" + URLEncoder.encode(myviptxt.getText().toString(), "UTF-8")+"&tuid="+array.get(0).get("uid")+"&dpic="+parray.get(0).get("path")+"&type=0";
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    getnews(2);
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

    @Override
    public void onClick(View v) {
        int a = PreferencesUtils.getInt(Cityinfo.this, "land");
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.zans:
                if (a == 0) {
                    Intent intent = new Intent();
                    intent.setClass(Cityinfo.this, Logn.class);
                    Cityinfo.this.startActivity(intent);
                } else {
                    if (orzan.equals("0")){
                        String username = PreferencesUtils.getString(Cityinfo.this, "username");
                        zanurl = GlobalVariables.urlstr + "Quan.addZan&did=" + id + "&username=" + username+"&tuid="+array.get(0).get("uid")+"&dpic="+parray.get(0).get("path");
                        getnews(1);
                    }else {
                      Toast.makeText(Cityinfo.this,"已经赞过了",Toast.LENGTH_SHORT).show();
                    }

                }
                break;
            case R.id.collect:
                if (a == 0) {
                    Intent intent = new Intent();
                    intent.setClass(Cityinfo.this, Logn.class);
                    Cityinfo.this.startActivity(intent);
                } else {
                    showpop(1);
                    popView.setVisibility(View.GONE);
                    myviptxt.setFocusable(true);
                    myviptxt.setFocusableInTouchMode(true);
                    myviptxt.requestFocus();
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask()
                                   {
                                       public void run()
                                       {
                                           InputMethodManager inputManager =
                                                   (InputMethodManager) myviptxt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                           inputManager.showSoftInput(myviptxt, 0);
                                       }
                                   },
                            100);
                    popView.setVisibility(View.VISIBLE);
//                    Toast.makeText(Cityinfo.this, "评", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.title:
                Intent intent=new Intent();
                intent.putExtra("id",array.get(0).get("category_id"));
                intent.setClass(Cityinfo.this, CityClassList.class);
                Cityinfo.this.startActivity(intent);
                break;
        }
    }
}
