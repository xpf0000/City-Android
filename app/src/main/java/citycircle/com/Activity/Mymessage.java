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
import java.util.List;

import citycircle.com.Adapter.MyMessageAdapter;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.MyEventBus;
import model.MessageCountModel;
import okhttp3.Call;
import util.XActivityindicator;
import util.XNetUtil;

import static citycircle.com.MyAppService.LocationApplication.APPDataCache;
import static citycircle.com.MyAppService.LocationApplication.APPService;

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
        String username = APPDataCache.User.getUsername();
        String uid = APPDataCache.User.getUid();

        XNetUtil.Handle(APPService.userGetMessagesCount(uid, username), new XNetUtil.OnHttpResult<List<MessageCountModel>>() {
            @Override
            public void onError(Throwable e) {

                XNetUtil.APPPrintln("getMsgCount error !!!!!!!!!!");
                XNetUtil.APPPrintln(e);
            }

            @Override
            public void onSuccess(List<MessageCountModel> models) {

                try
                {
                    if(models.size() == 0)
                    {
                        EventBus.getDefault().post(
                                new MyEventBus("hidden"));
                        APPDataCache.msgshow = false;
                        return;
                    }

                    MessageCountModel model = models.get(0);

                    message.clear();
                    message.add(model.getCount1());
                    message.add(model.getCount2());
                    message.add(model.getCount3());

                    int c1 = Integer.parseInt(model.getCount1());
                    int c2 = Integer.parseInt(model.getCount2());
                    int c3 = Integer.parseInt(model.getCount3());

                    if(c1+c2+c3 > 0)
                    {
                        EventBus.getDefault().post(
                                new MyEventBus("show"));
                        APPDataCache.msgshow = true;
                    }
                    else
                    {
                        EventBus.getDefault().post(
                                new MyEventBus("hidden"));
                        APPDataCache.msgshow = false;
                    }

                    list.clear();
                    setMyListView();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

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
