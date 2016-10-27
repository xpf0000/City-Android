package citycircle.com.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import citycircle.com.Adapter.MyVipcardAdapter;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.Loadmore;
import okhttp3.Call;

/**
 * Created by admins on 2016/6/28.
 */
public class SearchVip extends Activity implements View.OnClickListener{
    ListView list;
    EditText search;
    ImageView back;
    LinearLayout searchbt;
    String url, urlstr, key;
    int page = 1;
    Loadmore loadmore;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> hashMap;
    MyVipcardAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchsale);
        url = GlobalVariables.urlstr + "Hyk.search&keyword=" + key + "&page=" + page;
        intview();
        setAdapter();
    }
    private void intview(){
        loadmore = new Loadmore();
        list = (ListView) findViewById(R.id.list);
        search = (EditText) findViewById(R.id.search);
        searchbt = (LinearLayout) findViewById(R.id.searchbt);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        searchbt.setOnClickListener(this);
        loadmore.loadmore(list);
        list.setDivider(new ColorDrawable(Color.TRANSPARENT));
        list.setDividerHeight((int) getResources().getDimension(R.dimen.ui_10_dip));
        int a=(int) getResources().getDimension(R.dimen.ui_10_dip);
        list.setPadding(a,a,a,a);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("id", arrayList.get(position).get("id"));
                intent.setClass(SearchVip.this, VipcardInfo.class);
                SearchVip.this.startActivity(intent);
            }
        });
        loadmore.setMyPopwindowswListener(new Loadmore.LoadmoreList() {
            @Override
            public void loadmore() {
                page++;
                try {
                    url = GlobalVariables.urlstr + "Hyk.search&keyword=" + URLEncoder.encode(key, "UTF-8") + "&page=" + page;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                getjson(0);
            }
        });
    }
    private void getjson(final int type) {
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Toast.makeText(SearchVip.this, R.string.intent_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                if (type==1){
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
                arrayList.add(hashMap);
            }

        } else {
            if (page != 1) {
                page--;
            }
            Toast.makeText(SearchVip.this, R.string.nomore, Toast.LENGTH_SHORT).show();
        }
    }
    private void setAdapter() {
        adapter = new MyVipcardAdapter(arrayList, SearchVip.this);
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
                    Toast.makeText(SearchVip.this, "请输入内容", Toast.LENGTH_SHORT).show();
                } else {
                    page = 1;
                    key = search.getText().toString();
                    try {
                        url = GlobalVariables.urlstr + "Hyk.search&keyword=" + URLEncoder.encode(key, "UTF-8") + "&page=" + page;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    getjson(1);
                }
                break;
        }
    }
}
