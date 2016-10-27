package citycircle.com.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.Adapter.RecomAdapter;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.Loadmore;
import okhttp3.Call;

/**
 * Created by admins on 2016/6/28.
 */
public class SearchNews extends Activity implements View.OnClickListener {
    ListView list;
    EditText search;
    ImageView back;
    LinearLayout searchbt;
    String url, urlstr, key;
    int page = 1;
    RecomAdapter adapter;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> hashMap;
    Loadmore loadmore;
    private ArrayList<HashMap<String, String>> newsid=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchsale);
        url = GlobalVariables.urlstr + "News.search&keyword=" + key + "&page=" + page;
        intview();
        setAdapter();
    }

    private void intview() {
        loadmore = new Loadmore();
        list = (ListView) findViewById(R.id.list);
        search = (EditText) findViewById(R.id.search);
        searchbt = (LinearLayout) findViewById(R.id.searchbt);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        searchbt.setOnClickListener(this);
        loadmore.loadmore(list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("id", arrayList.get(position ).get("id"));
                intent.putExtra("title", arrayList.get(position ).get("title"));
                intent.putExtra("description", arrayList.get(position ).get("description"));
                intent.putExtra("url", arrayList.get(position).get("url"));
                intent.setClass(SearchNews.this, NewsInfoActivity.class);
                SearchNews.this.startActivity(intent);
            }
        });
        loadmore.setMyPopwindowswListener(new Loadmore.LoadmoreList() {
            @Override
            public void loadmore() {
                page++;
                try {
                    url = GlobalVariables.urlstr + "News.search&keyword=" + URLEncoder.encode(key, "UTF-8") + "&page=" + page;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                getJson(0);

            }
        });
    }

    private void getJson(final int type) {
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {

                Toast.makeText(SearchNews.this, R.string.intent_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                if (type == 1) {
                    arrayList.clear();
                }
                setArray(response);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void setArray(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
        if (jsonObject1.getIntValue("code") == 0) {
            JSONArray jsonArray = jsonObject1.getJSONArray("info");
            for (int i = 0; i < jsonArray.size(); i++) {
                hashMap = new HashMap<>();
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                hashMap.put("id", jsonObject2.getString("id") == null ? "" : jsonObject2.getString("id"));
                hashMap.put("title", jsonObject2.getString("title") == null ? "" : jsonObject2.getString("title"));
                hashMap.put("description", jsonObject2.getString("description") == null ? "" : jsonObject2.getString("description"));
                hashMap.put("view", jsonObject2.getString("view") == null ? "" : jsonObject2.getString("view"));
                hashMap.put("picList", jsonObject2.getString("picList") == null ? "" : jsonObject2.getString("picList"));
                hashMap.put("name", jsonObject2.getString("name") == null ? "" : jsonObject2.getString("name"));
                hashMap.put("category_id", jsonObject2.getString("category_id") == null ? "" : jsonObject2.getString("category_id"));
                arrayList.add(hashMap);
            }

        } else {
            if (page != 1) {
                page--;
            }
            Toast.makeText(SearchNews.this, R.string.nomore, Toast.LENGTH_SHORT).show();
        }
    }

    private void setAdapter() {
        adapter = new RecomAdapter(arrayList, SearchNews.this,newsid);
        list.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.searchbt:
                if (search.getText().toString().length() == 0) {
                    Toast.makeText(SearchNews.this, "请输入内容", Toast.LENGTH_SHORT).show();
                } else {
                    page = 1;
                    key = search.getText().toString();
                    try {
                        url = GlobalVariables.urlstr + "News.search&keyword=" + URLEncoder.encode(key, "UTF-8") + "&page=" + page;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    getJson(1);
                }
                break;
        }
    }
}
