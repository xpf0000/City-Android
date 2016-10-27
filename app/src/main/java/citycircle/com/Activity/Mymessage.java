package citycircle.com.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.Adapter.MyMessageAdapter;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.MyEventBus;
import citycircle.com.Utils.PreferencesUtils;
import okhttp3.Call;

/**
 * Created by admins on 2016/6/3.
 */
public class Mymessage extends Activity implements View.OnClickListener {
    ListView mylist;
    String[] item = new String[]{"系统消息", "小区消息", "会员卡消息"};
    String[] items = new String[]{"点击查看系统消息", "点击查看小区消息", "点击查看会员卡消息"};
    int[] drable = new int[]{R.mipmap.my_system3x, R.mipmap.my_msgx, R.mipmap.my_vipx};
    ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
    ArrayList<String> message = new ArrayList<>();
    HashMap<String, Object> map;
    MyMessageAdapter adapter;
    ImageView back;
    String url, urlstr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mymessage);
        intview();
        getJsom();
    }

    private void intview() {
        mylist = (ListView) findViewById(R.id.mylist);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventBus.getDefault().post(
                        new MyEventBus("dis"));
                Intent intent=new Intent();
                intent.putExtra("type",(position+1));
                intent.setClass(Mymessage.this, MyMessageList.class);
                Mymessage.this.startActivity(intent);
            }
        });
    }

    public void setMyListView() {
        for (int i = 0; i < drable.length; i++) {
            map = new HashMap<String, Object>();
            map.put("drable", drable[i]);
            map.put("item", item[i]);
            map.put("items", items[i]);
            map.put("count", message.get(i));
            list.add(map);
        }
        adapter = new MyMessageAdapter(list, Mymessage.this);
        mylist.setAdapter(adapter);
    }

    public void getJsom() {
        String username = PreferencesUtils.getString(Mymessage.this, "username");
        String uid = PreferencesUtils.getString(Mymessage.this, "userid");
        url = GlobalVariables.urlstr + "user.getMessagesCount&uid=" + uid + "&username=" + username;
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Toast.makeText(Mymessage.this, R.string.intent_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = JSON.parseObject(response);
                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                if (jsonObject1.getIntValue("code") == 0) {
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("info");
                    message.clear();
                    message.add(jsonObject2.getString("count1"));
                    message.add(jsonObject2.getString("count2"));
                    message.add(jsonObject2.getString("count3"));

                }
                list.clear();
                setMyListView();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getJsom();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }
}
