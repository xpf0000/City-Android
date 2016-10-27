package citycircle.com.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.Adapter.MessageAdapter;
import citycircle.com.MyViews.MyPopwindows;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;
import citycircle.com.Utils.PreferencesUtils;

/**
 * Created by admins on 2015/12/7.
 */
public class Messagelist extends Activity {
    ListView messagelist;
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
    String message, messagestr;
    String username;
    MessageAdapter messageAdapter;
    View footview;
    int page = 1;
    int a = 0;
    TextView mestst;
    ImageView back;
    MyPopwindows myPopwindows;
    String deurl, deurlstr;
    int positions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messagelist);
        username = PreferencesUtils.getString(Messagelist.this, "username");
        message = GlobalVariables.urlstr + "Quan.getNews&username=" + username;
        footview = LayoutInflater.from(this).inflate(
                R.layout.footview, null);
        intview();
        setlist();
        getList(0);
    }

    public void intview() {
        myPopwindows = new MyPopwindows();
        back=(ImageView)findViewById(R.id.back);
        mestst = (TextView) footview.findViewById(R.id.mestst);
        messagelist = (ListView) findViewById(R.id.messagelist);
        messagelist.addFooterView(footview);
        myPopwindows.setMyPopwindowswListener(new MyPopwindows.MyPopwindowsListener() {
            @Override
            public void onRefresh() {
                String id = array.get(positions).get("did");
                deurl = GlobalVariables.urlstr + "Quan.commentDel&id="+id + "&username=" + username;
                getList(1);
            }
        });
//        messagelist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                positions=position;
//                myPopwindows.showpop(Messagelist.this, "确定删除吗");
//                return true;
//            }
//        });
        messagelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("id", array.get(position).get("did"));
                intent.setClass(Messagelist.this, Cityinfo.class);
                Messagelist.this.startActivity(intent);
            }
        });
        footview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a = 1;
                array.clear();
                message = GlobalVariables.urlstr + "Quan.getNewsMore&username=" + username + "&page=" + page;
                getList(0);
                footview.setClickable(false);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        messagelist.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            if (a == 1) {
                                page++;
                                message = GlobalVariables.urlstr + "Quan.getNewsMore&username=" + username + "&page=" + page;
                                getList(0);
                            } else {

                            }

                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }


    public void getList(final int type) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                HttpRequest httpRequest = new HttpRequest();
                if (type==0){
                    messagestr = httpRequest.doGet(message);
                    if (messagestr.equals("网络超时")) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(1);
                    }
                }else {
                    deurlstr = httpRequest.doGet(deurl);
                    if (deurlstr.equals("网络超时")) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(4);
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
                    setMessagelist(messagestr);
                    messageAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    Toast.makeText(Messagelist.this, "网络超时", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    mestst.setText("暂无更多");
//                    Toast.makeText(Messagelist.this, "暂无更多", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    JSONObject jsonObject = JSON.parseObject(deurlstr);
                    if (jsonObject.getIntValue("ret") == 200) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        if (jsonObject1.getIntValue("code") == 0) {
                            Toast.makeText(Messagelist.this, "删除成功", Toast.LENGTH_SHORT).show();
                            array.remove(positions);
                            messageAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(Messagelist.this, "删除失败", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Messagelist.this, "删除失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    public void setMessagelist(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
        int a = jsonObject1.getIntValue("code");
        if (a == 0) {
            JSONArray jsonArray = jsonObject1.getJSONArray("info");
            for (int i = 0; i < jsonArray.size(); i++) {
                hashMap = new HashMap<>();
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                hashMap.put("content", jsonObject2.getString("content") == null ? "" : jsonObject2.getString("content"));
                hashMap.put("dpic", jsonObject2.getString("dpic") == null ? "" : jsonObject2.getString("dpic"));
                hashMap.put("headimage", jsonObject2.getString("headimage") == null ? "" : jsonObject2.getString("headimage"));
                hashMap.put("did", jsonObject2.getString("did") == null ? "" : jsonObject2.getString("did"));
                hashMap.put("nickname", jsonObject2.getString("nickname") == null ? "" : jsonObject2.getString("nickname"));
                hashMap.put("create_time", jsonObject2.getString("create_time") == null ? "" : jsonObject2.getString("create_time"));
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
        messageAdapter = new MessageAdapter(array, Messagelist.this);
        messagelist.setAdapter(messageAdapter);
    }
}
