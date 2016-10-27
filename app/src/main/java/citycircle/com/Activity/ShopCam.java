package citycircle.com.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
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

import citycircle.com.Adapter.Camadapter;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import okhttp3.Call;

/**
 * Created by admins on 2016/6/17.
 */
public class ShopCam extends Activity {
    ImageView back;
    SwipeRefreshLayout Refresh;
    ListView viplist;
    String url, shopid;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> hashMap;
    int page = 1;
    Camadapter camadapter;
    ArrayList<HashMap<String, String>> list=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopcam);
        shopid = getIntent().getStringExtra("id");
        url = GlobalVariables.urlstr + "Hyk.getShopHD&id=" + shopid;
        intview();
        setCamadapter();
        getJson();
    }

    private void intview() {
        Refresh = (SwipeRefreshLayout) findViewById(R.id.Refresh);
        viplist = (ListView) findViewById(R.id.viplist);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viplist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("id", arrayList.get(position).get("id"));
                intent.putExtra("type", 1);
                intent.setClass(ShopCam.this, DiscountInfo.class);
                ShopCam.this.startActivity(intent);
            }
        });
    }

    private void getJson() {
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Toast.makeText(ShopCam.this, R.string.intent_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                setarray(response);
                camadapter.notifyDataSetChanged();
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
                hashMap.put("title", jsonObject2.getString("title") == null ? "" : jsonObject2.getString("title"));
                hashMap.put("description", jsonObject2.getString("description") == null ? "" : jsonObject2.getString("description"));
                hashMap.put("url", jsonObject2.getString("url") == null ? "" : jsonObject2.getString("url"));
                hashMap.put("create_time", jsonObject2.getString("create_time") == null ? "" : jsonObject2.getString("create_time"));
                hashMap.put("s_time", jsonObject2.getString("s_time") == null ? "" : jsonObject2.getString("s_time"));
                hashMap.put("e_time", jsonObject2.getString("e_time") == null ? "" : jsonObject2.getString("e_time"));
                arrayList.add(hashMap);
            }

        } else {
            if (page != 1) {
                page--;
            }
            Toast.makeText(ShopCam.this, R.string.nomore, Toast.LENGTH_SHORT).show();
        }
    }

    private void setCamadapter() {
        camadapter = new Camadapter(arrayList, ShopCam.this,list);
        viplist.setAdapter(camadapter);
    }
}
