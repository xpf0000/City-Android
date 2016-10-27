package citycircle.com.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import citycircle.com.Adapter.CommAdapter;
import citycircle.com.MyViews.MyPopwindows;
import citycircle.com.R;
import citycircle.com.Utils.DateUtils;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;
import citycircle.com.Utils.PreferencesUtils;

/**
 * Created by admins on 2015/12/6.
 */
public class CommentList extends Activity {
    ListView comlist;
    String url, uristr, id, newsurl, newsstr, urlinfo, comurl, delurl, delstr;
    int page = 1;
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> newsarray = new ArrayList<HashMap<String, String>>();
    CommAdapter commAdapter;
    ImageView back;
    TextView title, content, foottxt;
    DateUtils dateUtils;
    View headview, footview;
    Button collected, submit;
    PopupWindow popupWindow;
    View popView;
    EditText myviptxt;
    MyPopwindows myPopwindows;
    int positions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commentlist);
        id = getIntent().getStringExtra("id");
        url = GlobalVariables.urlstr + "Comment.getList&did=" + id + "&page=" + page;
        newsurl = GlobalVariables.urlstr + "News.getArticle&id=" + id;

        intview();
        setComlist();
        getstr(1);
    }

    public void intview() {
        dateUtils = new DateUtils();
        submit = (Button) findViewById(R.id.submit);
        collected = (Button) findViewById(R.id.collected);
        headview = LayoutInflater.from(CommentList.this).inflate(
                R.layout.comment_hedaciew, null);
        footview = LayoutInflater.from(CommentList.this).inflate(
                R.layout.list_footview, null);
        foottxt = (TextView) footview.findViewById(R.id.foottxt);
        title = (TextView) headview.findViewById(R.id.title);
        content = (TextView) headview.findViewById(R.id.content);
        back = (ImageView) findViewById(R.id.back);
        comlist = (ListView) findViewById(R.id.comlist);
        comlist.addHeaderView(headview);
        comlist.addFooterView(footview, null, false);
        myPopwindows = new MyPopwindows();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        comlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int land = PreferencesUtils.getInt(CommentList.this, "land");
                if (land != 1) {
                    Intent intent = new Intent();
                    intent.setClass(CommentList.this, Logn.class);
                    CommentList.this.startActivity(intent);
                } else {
                    positions = position-1;
                    if (array.get(positions).get("uid").equals(PreferencesUtils.getString(CommentList.this, "userid"))) {
                        myPopwindows.showpop(CommentList.this, "确定删除");
                    }
                }
            }
        });
        myPopwindows.setMyPopwindowswListener(new MyPopwindows.MyPopwindowsListener() {
            @Override
            public void onRefresh() {
                delurl = GlobalVariables.urlstr + "Comment.delComment&id=" + array.get(positions).get("id") + "&username=" + PreferencesUtils.getString(CommentList.this, "username");
                getstr(2);
            }
        });
        comlist.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            page++;
                            url = GlobalVariables.urlstr + "Comment.getList&did=" + id + "&page=" + page;
                            getstr(1);
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        collected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = PreferencesUtils.getInt(CommentList.this, "land");
                if (a == 0) {
                    Intent intent = new Intent();
                    intent.setClass(CommentList.this, Logn.class);
                    CommentList.this.startActivity(intent);
                } else {
                    showpop();
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
//                    myviptxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                        @Override
//                        public void onFocusChange(View v, boolean hasFocus) {
//                            if (hasFocus){
//
//                            }else {
//                              popupWindow.dismiss();
//                            }
//                        }
//                    });
                }
            }
        });
    }

    public void getstr(final int type) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                HttpRequest httpRequest = new HttpRequest();
                if (type == 0) {
                    urlinfo = httpRequest.doGet(comurl);
                    if (urlinfo.equals("网络超时")) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(4);
                    }
                } else if (type == 2) {
                    delstr = httpRequest.doGet(delurl);
                    if (delstr.equals("网络超时")) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(5);
                    }
                } else {
                    uristr = httpRequest.doGet(url);
                    newsstr = httpRequest.doGet(newsurl);
                    if (uristr.equals("网络超时")) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(1);
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
                    setHashMap(uristr);
                    setNewsstr(newsstr);
                    commAdapter.notifyDataSetChanged();
                    String time = dateUtils.getDateToStringsss(Long.parseLong(newsarray.get(0).get("update_time")));
                    title.setText(newsarray.get(0).get("title"));
                    content.setText(newsarray.get(0).get("source") + " " + time + " " + newsarray.get(0).get("comment") + "评论");
                    if (array.size() < 10) {
                        foottxt.setText("暂无更多评论");
                    }
                    break;
                case 2:
                    Toast.makeText(CommentList.this, "网络似乎有问题了", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    foottxt.setText("暂无更多评论");
//                    Toast.makeText(CommentList.this, "暂无更多评论", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    JSONObject jsonObject3 = JSON.parseObject(urlinfo);
                    JSONObject jsonObject4 = jsonObject3.getJSONObject("data");
                    int b = jsonObject4.getIntValue("code");
                    if (b == 0) {
                        Toast.makeText(CommentList.this, "评论成功", Toast.LENGTH_SHORT).show();
                        popupWindow.dismiss();
                        View view = getWindow().peekDecorView();
                        if (view != null) {
                            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputmanger.hideSoftInputFromWindow(
                                    view.getWindowToken(), 0);
                        }
                        int pagenum = page * 10;
                        page = 1;
                        array.clear();
                        url = GlobalVariables.urlstr + "Comment.getList&did=" + id + "&page=" + page + "&perNumber=" + pagenum;
                        getstr(1);

//                        collected.setText("");
                    } else {
                        Toast.makeText(CommentList.this, "评论失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 5:
                    JSONObject jsonObject5 = JSON.parseObject(delstr);
                    JSONObject jsonObject6 = jsonObject5.getJSONObject("data");
                    int c = jsonObject6.getIntValue("code");
                    if (c == 0) {
                        array.remove(positions);
                        commAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(CommentList.this, jsonObject6.getString("msg"), Toast.LENGTH_SHORT).show();
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
                hashMap.put("uid", jsonObject2.getString("uid") == null ? "" : jsonObject2.getString("uid"));
                hashMap.put("nickname", jsonObject2.getString("nickname") == null ? "" : jsonObject2.getString("nickname"));
                hashMap.put("content", jsonObject2.getString("content") == null ? "" : jsonObject2.getString("content"));
                hashMap.put("headimage", jsonObject2.getString("headimage") == null ? "" : jsonObject2.getString("headimage"));
                hashMap.put("create_time", jsonObject2.getString("create_time") == null ? "" : jsonObject2.getString("create_time"));
                array.add(hashMap);
            }
        } else {
            handler.sendEmptyMessage(3);
        }
    }

    public void setNewsstr(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
        int a = jsonObject1.getIntValue("code");
        if (a == 0) {
            JSONArray jsonArray = jsonObject1.getJSONArray("info");
            for (int i = 0; i < jsonArray.size(); i++) {
                hashMap = new HashMap<>();
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                hashMap.put("title", jsonObject2.getString("title") == null ? "" : jsonObject2.getString("title"));
                hashMap.put("comment", jsonObject2.getString("comment") == null ? "" : jsonObject2.getString("comment"));
                hashMap.put("update_time", jsonObject2.getString("update_time") == null ? "" : jsonObject2.getString("update_time"));
                hashMap.put("source", jsonObject2.getString("source") == null ? "" : jsonObject2.getString("source"));
                newsarray.add(hashMap);
            }
        } else {
            handler.sendEmptyMessage(3);
        }
    }

    public void setComlist() {
        commAdapter = new CommAdapter(array, CommentList.this);
        comlist.setAdapter(commAdapter);
    }

    public void showpop() {
        popView = getLayoutInflater().inflate(
                R.layout.newcommentpop, null);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        popupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        popupWindow.showAtLocation(popView,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        myviptxt = (EditText) popView.findViewById(R.id.popedttxt);
        Button back = (Button) popView.findViewById(R.id.back);
        Button update = (Button) popView.findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (myviptxt.getText().toString().length() == 0) {
                    Toast.makeText(CommentList.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    String username = PreferencesUtils.getString(CommentList.this, "username");
                    try {
                        comurl = GlobalVariables.urlstr + "Comment.insert&did=" + id + "&username=" + username + "&content=" + URLEncoder.encode(myviptxt.getText().toString().trim(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    getstr(0);
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
}
