package citycircle.com.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.Adapter.MyVipcardAdapter;
import citycircle.com.MyViews.MyClassPopwd;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.Loadmore;
import citycircle.com.Utils.PreferencesUtils;
import okhttp3.Call;

/**
 * Created by admins on 2016/6/3.
 */
public class MyVipcard extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {
    TextView allclass, vipclass;
    ImageView search,back;
    ListView listView;
    String url, allclassurl;
    String category_id = "0", typeid = "0", username;
    int page = 1;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> hashMap;
    MyVipcardAdapter adapter;
    Loadmore loadmore;
    MyClassPopwd myClassPopwd;
    LinearLayout classlay;
    ArrayList<HashMap<String, Object>> poparrayList = new ArrayList<>();
    ArrayList<HashMap<String, String>> classarrayList = new ArrayList<HashMap<String, String>>();
    SwipeRefreshLayout Refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myvipcard);
        allclassurl = GlobalVariables.urlstr + "hyk.getCategory ";
        username = PreferencesUtils.getString(MyVipcard.this, "username");
        url = GlobalVariables.urlstr + "hyk.getuserlist&category_id=" + category_id + "&typeid=" + typeid + "&page=" + page + "&username=" + username;
        intview();
        setadapter();
        getjson(0);
        getAllclass();
    }

    private void intview() {
        loadmore = new Loadmore();
        myClassPopwd = new MyClassPopwd();
        search = (ImageView) findViewById(R.id.search);
        back = (ImageView) findViewById(R.id.back);
        search.setOnClickListener(this);
        search.setVisibility(View.GONE);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(this);
        Refresh = (SwipeRefreshLayout) findViewById(R.id.Refresh);
        classlay = (LinearLayout) findViewById(R.id.classlay);
        allclass = (TextView) findViewById(R.id.allclass);
        allclass.setOnClickListener(this);
        vipclass = (TextView) findViewById(R.id.vipclass);
        vipclass.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.viplist);
        listView.setOnItemClickListener(this);
        loadmore.loadmore(listView);
        loadmore.setMyPopwindowswListener(new Loadmore.LoadmoreList() {
            @Override
            public void loadmore() {
                page++;
                url = GlobalVariables.urlstr + "hyk.getuserlist&category_id=" + category_id + "&typeid=" + typeid + "&page=" + page + "&username=" + username;
                getjson(0);
            }
        });
        Refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                url = GlobalVariables.urlstr + "hyk.getuserlist&category_id=" + category_id + "&typeid=" + typeid + "&page=" + page + "&username=" + username;
                getjson(1);
            }
        });
    }

    private void getAllclass() {
        OkHttpUtils.get().url(allclassurl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = JSON.parseObject(response);
                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                hashMap = new HashMap<String, String>();
                hashMap.put("id", "0");
                hashMap.put("title", "全部分类");
                hashMap.put("url", "http://imgsrc.baidu.com/forum/pic/item/142ae7cec3fdfc030645e798dc3f8794a4c22623.jpg");
                hashMap.put("check", "false");
                classarrayList.add(hashMap);
                if (jsonObject1.getIntValue("code") == 0) {
                    JSONArray jsonArray = jsonObject1.getJSONArray("info");
                    for (int i = 0; i < jsonArray.size(); i++) {
                        hashMap = new HashMap<String, String>();
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                        hashMap.put("id", jsonObject2.getString("id") == null ? "" : jsonObject2.getString("id"));
                        hashMap.put("title", jsonObject2.getString("title") == null ? "" : jsonObject2.getString("title"));
                        hashMap.put("url", jsonObject2.getString("url") == null ? "" : jsonObject2.getString("url"));
                        hashMap.put("check", "false");
                        classarrayList.add(hashMap);
                    }
                }
            }
        });
    }

    private void getjson(final int type) {
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Refresh.setRefreshing(false);
                Toast.makeText(MyVipcard.this, R.string.intent_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                Refresh.setRefreshing(false);
                if (type == 1) {
                    arrayList.clear();
                }
                setarray(response);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void setarray(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
        if (jsonObject1.getIntValue("code") == 0) {
            JSONArray jsonArray = jsonObject1.getJSONArray("info");
            for (int i = 0; i < jsonArray.size(); i++) {
                hashMap = new HashMap<>();
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                hashMap.put("id", jsonObject2.getString("id") == null ? "" : jsonObject2.getString("id"));
                hashMap.put("color", jsonObject2.getString("color") == null ? "" : jsonObject2.getString("color"));
                hashMap.put("logo", jsonObject2.getString("logo") == null ? "" : jsonObject2.getString("logo"));
                hashMap.put("shopname", jsonObject2.getString("shopname") == null ? "" : jsonObject2.getString("shopname"));
                hashMap.put("type", jsonObject2.getString("type") == null ? "" : jsonObject2.getString("type"));
                hashMap.put("orlq", jsonObject2.getString("orlq") == null ? "" : jsonObject2.getString("orlq"));
                arrayList.add(hashMap);
            }

        } else {
            if (page != 1) {
                page--;
            }
            Toast.makeText(MyVipcard.this, R.string.nomore, Toast.LENGTH_SHORT).show();
        }
    }

    private void setadapter() {
        adapter = new MyVipcardAdapter (arrayList, MyVipcard.this);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.allclass:
                vipclass.setTextColor(Color.parseColor("#333333"));
                allclass.setTextColor(Color.parseColor("#21ADFD"));
                setrpop();
                break;
            case R.id.vipclass:
                vipclass.setTextColor(Color.parseColor("#21ADFD"));
                allclass.setTextColor(Color.parseColor("#333333"));
                setpop();
                break;
            case R.id.search:
                Intent intent = new Intent();
                intent.setClass(MyVipcard.this, SearchVip.class);
                MyVipcard.this.startActivity(intent);
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.viplist:
                Intent intent = new Intent();
                intent.putExtra("id", arrayList.get(position).get("id"));
                intent.putExtra("orlq", 1);
                intent.setClass(MyVipcard.this, VipcardInfo.class);
                MyVipcard.this.startActivity(intent);
                break;

        }
    }

    private void setpop() {
        HashMap<String, Object> hashMap;
        if (poparrayList.size() == 0) {
            poparrayList = new ArrayList<>();
            String[] text = new String[]{"会员卡分类", "打折卡", "计次卡", "充值卡", "积分卡"};
            int[] typeid = new int[]{0, 3, 1, 2, 4};
            int[] imageId = new int[]{R.mipmap.left_type_0, R.mipmap.right_type_03x, R.mipmap.right_type_13x, R.mipmap.right_type_23x, R.mipmap.right_type_33x};
            for (int i = 0; i < text.length; i++) {
                hashMap = new HashMap<>();
                hashMap.put("classname", text[i]);
                hashMap.put("id", imageId[i]);
                hashMap.put("check", false);
                hashMap.put("typeid", typeid[i]);
                poparrayList.add(hashMap);
            }
        }

        myClassPopwd.showpopdrable(MyVipcard.this, poparrayList, classlay);
        myClassPopwd.setMyPopwindowswListener(new MyClassPopwd.MyPopwindowsListener() {
            @Override
            public void onRefresh(int position) {
                vipclass.setText(poparrayList.get(position).get("classname").toString());
                vipclass.setTextColor(Color.parseColor("#21ADFD"));
                typeid = poparrayList.get(position).get("typeid").toString();
                url = GlobalVariables.urlstr + "hyk.getuserlist&category_id=" + category_id + "&typeid=" + typeid + "&page=" + page + "&username=" + username;
                getjson(1);
            }
        });
    }

    private void setrpop() {
        myClassPopwd.showpop(MyVipcard.this, classarrayList, classlay);
        myClassPopwd.setMyPopwindowswListener(new MyClassPopwd.MyPopwindowsListener() {
            @Override
            public void onRefresh(int position) {
                allclass.setText(classarrayList.get(position).get("title"));
                allclass.setTextColor(Color.parseColor("#21ADFD"));
                category_id = classarrayList.get(position).get("id");
                url = GlobalVariables.urlstr + "hyk.getuserlist&category_id=" + category_id + "&typeid=" + typeid + "&page=" + page + "&username=" + username;
                getjson(1);
            }
        });
    }
}
