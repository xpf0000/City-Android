package citycircle.com.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.Adapter.HotTelAdapter;
import citycircle.com.MyViews.CallPhonePop;
import citycircle.com.MyViews.MyGridView;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;
import citycircle.com.Utils.Loadmore;
import okhttp3.Call;

/**
 * Created by admins on 2015/11/19.
 */
public class TelYelloePage extends Activity {
    MyGridView telclass;
    ListView hotlist;
    String calssurl, calssurlstr, hoturl, hoturlstr, id;
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> classhashMap;
    ArrayList<HashMap<String, String>> classarray = new ArrayList<HashMap<String, String>>();
    //    TelclassAdapter telclassAdapter;
    ImageView back;
    HotTelAdapter hotTelAdapter;
    CallPhonePop callPhonePop;
    EditText search;
    TabLayout mytab;
    int page = 1;
    Loadmore loadmore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.telpage);
        calssurl = GlobalVariables.urlstr + "Tel.getCategory";
        hoturl = GlobalVariables.urlstr + "Tel.getList&category_id=" + id + "&page=" + page;
        intview();
//        setTelclass();
        setHotlist();
        geturlstr();
    }

    public void intview() {
        loadmore=new Loadmore();
        mytab = (TabLayout) findViewById(R.id.mytab);
        search = (EditText) findViewById(R.id.search);
        telclass = (MyGridView) findViewById(R.id.telclass);
        hotlist = (ListView) findViewById(R.id.hotlist);
        back = (ImageView) findViewById(R.id.back);
        telclass.setVisibility(View.GONE);
        loadmore.loadmore(hotlist);
        loadmore.setMyPopwindowswListener(new Loadmore.LoadmoreList() {
            @Override
            public void loadmore() {
                page++;
                if (mytab.getSelectedTabPosition()==0){
                    hoturl = GlobalVariables.urlstr + "Tel.getHot&page=" + page;
                }else {
                    id=classarray.get(mytab.getSelectedTabPosition()).get("id");
                    hoturl = GlobalVariables.urlstr + "Tel.getList&category_id=" + id + "&page=" + page;
                }
                gettelstr(0);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(TelYelloePage.this, SearchTel.class);
                TelYelloePage.this.startActivity(intent);
            }
        });
        telclass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("id", classarray.get(position).get("id"));
                intent.putExtra("calssnames", classarray.get(position).get("title"));
                intent.setClass(TelYelloePage.this, TelClassList.class);
                TelYelloePage.this.startActivity(intent);
            }
        });
        hotlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("id", array.get(position).get("id"));
                intent.setClass(TelYelloePage.this, TelInfo.class);
                TelYelloePage.this.startActivity(intent);
            }
        });
    }

    public void geturlstr() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                HttpRequest httpRequest = new HttpRequest();
                calssurlstr = httpRequest.doGet(calssurl);
                if (calssurlstr.equals("网络超时")) {
                    handler.sendEmptyMessage(2);
                } else {
                    handler.sendEmptyMessage(1);
                }
            }
        }.start();
    }
private void gettelstr(final int type){
    OkHttpUtils.get().url(hoturl).build().execute(new StringCallback() {
        @Override
        public void onError(Call call, Exception e) {
            Toast.makeText(TelYelloePage.this,R.string.intent_error,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResponse(String response) {
            if (type==1){
                array.clear();
            }
            setHashMap(response);
            hotTelAdapter.notifyDataSetChanged();
        }
    });
}
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    setClasshashMap(calssurlstr);
//                    telclassAdapter.notifyDataSetChanged();

                    hoturl = GlobalVariables.urlstr + "Tel.getHot&page=" + page;
                    gettelstr(0);
                    mytab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {
                            page=1;
                          if (mytab.getSelectedTabPosition()==0){
                              hoturl = GlobalVariables.urlstr + "Tel.getHot&page=" + page;
                          }else {
                              id=classarray.get(mytab.getSelectedTabPosition()).get("id");
                              hoturl = GlobalVariables.urlstr + "Tel.getList&category_id=" + id + "&page=" + page;
                          }
                            gettelstr(1);
                        }

                        @Override
                        public void onTabUnselected(TabLayout.Tab tab) {

                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {

                        }
                    });
                    break;
                case 2:
                    Toast.makeText(TelYelloePage.this, "网络似乎有问题了", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(TelYelloePage.this, "暂无内容", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    callPhonePop = new CallPhonePop();
                    callPhonePop.showpop(TelYelloePage.this, array.get(GlobalVariables.position).get("tel"));
                    break;
            }
        }
    };

    //分类信息
    public void setClasshashMap(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
        int a = jsonObject1.getIntValue("code");
        if (a == 0) {
            JSONArray jsonArray = jsonObject1.getJSONArray("info");
            classhashMap = new HashMap<>();
            classhashMap.put("id", "0");
            classhashMap.put("title", "热门电话");
            classhashMap.put("path", "");
            mytab.addTab(mytab.newTab().setText("热门"));
            classarray.add(classhashMap);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                classhashMap = new HashMap<>();
                classhashMap.put("id", jsonObject2.getString("id") == null ? "" : jsonObject2.getString("id"));
                classhashMap.put("title", jsonObject2.getString("title") == null ? "" : jsonObject2.getString("title"));
                classhashMap.put("path", jsonObject2.getString("url") == null ? "" : jsonObject2.getString("url"));
                mytab.addTab(mytab.newTab().setText(jsonObject2.getString("title")));
                classarray.add(classhashMap);
            }
        } else {
            handler.sendEmptyMessage(3);
        }
    }

    //装填分类信息
//    public void setTelclass() {
//        telclassAdapter = new TelclassAdapter(classarray, TelYelloePage.this);
//        telclass.setAdapter(telclassAdapter);
//    }
    //热门信息
    public void setHashMap(String str) {
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
                hashMap.put("tel", jsonObject2.getString("tel") == null ? "" : jsonObject2.getString("tel"));
                array.add(hashMap);
            }
        } else {
            handler.sendEmptyMessage(3);
        }
    }

    //装填热门
    public void setHotlist() {
        hotTelAdapter = new HotTelAdapter(array, TelYelloePage.this, handler);
        hotlist.setAdapter(hotTelAdapter);
    }
}
