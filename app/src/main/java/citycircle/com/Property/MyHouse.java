package citycircle.com.Property;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ScrollDirectionListener;

import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;
import citycircle.com.Utils.PreferencesUtils;

/**
 * Created by 飞侠 on 2016/2/18.
 */
public class MyHouse extends Activity {
    SwipeRefreshLayout Refresh;
    ListView houselist;
    ImageView back;
    String url, urlstr, updatrurl, updatestr, uid, username;
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> hashMap;
    HouseAdapter adapter;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myhouse);
        uid = PreferencesUtils.getString(MyHouse.this, "userid");
        username = PreferencesUtils.getString(MyHouse.this, "username");
        url = GlobalVariables.urlstr + "user.getHouseList&uid=" + uid + "&username=" + username;
        intview();
        setHouselist();
        gethouselist(0);
    }

    private void intview() {
        back = (ImageView) findViewById(R.id.back);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        Refresh = (SwipeRefreshLayout) findViewById(R.id.Refresh);
        houselist = (ListView) findViewById(R.id.houselist);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fab.attachToListView(houselist, new ScrollDirectionListener() {
            @Override
            public void onScrollDown() {

                fab.show();
            }

            @Override
            public void onScrollUp() {

                fab.hide();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MyHouse.this, AddHome.class);
                MyHouse.this.startActivity(intent);
            }
        });
        Refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                url = GlobalVariables.urlstr + "user.getHouseList&uid=" + uid + "&username=" + username;
                gethouselist(1);
            }
        });
    }

    private void gethouselist(final int type) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                HttpRequest httpRequest = new HttpRequest();
                urlstr = httpRequest.doGet(url);
                if (type == 0 || type == 1) {
                    if (urlstr.equals("网络超时")) {
                        handler.sendEmptyMessage(2);
                    } else {
                        if (type == 1) {
                            handler.sendEmptyMessage(4);
                        } else {
                            handler.sendEmptyMessage(1);
                        }
                    }
                } else if (type == 2 || type == 3||type==4) {
                    updatestr = httpRequest.doGet(updatrurl);
                    if (updatestr.equals("网络超时")) {
                        handler.sendEmptyMessage(2);
                    } else {
                        if (type == 3) {
                            array.remove(GlobalVariables.position);
                            String housid = PreferencesUtils.getString(MyHouse.this, "houseid");
                            PreferencesUtils.putString(MyHouse.this, "houseids", housid);
                            handler.sendEmptyMessage(6);
                        } else if(type==4){
                            handler.sendEmptyMessage(8);
                        }
                        else {
                            PreferencesUtils.putString(MyHouse.this, "houseid", array.get(GlobalVariables.position).get("houseid"));
                            PreferencesUtils.putString(MyHouse.this, "fanghaoid", array.get(GlobalVariables.position).get("fanghaoid"));
                            PreferencesUtils.putString(MyHouse.this, "houseids", array.get(GlobalVariables.position).get("houseid"));
                            PreferencesUtils.putString(MyHouse.this, "xiaoqu", array.get(GlobalVariables.position).get("xiaoqu"));
                            String hosename = array.get(GlobalVariables.position).get("xiaoqu") + array.get(GlobalVariables.position).get("louhao") + array.get(GlobalVariables.position).get("danyuan") + array.get(GlobalVariables.position).get("fanghao");
                            PreferencesUtils.putString(MyHouse.this, "housename", hosename);
                            handler.sendEmptyMessage(6);
                        }

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
                    Refresh.setRefreshing(false);
                    try {
                        setArray(urlstr);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case 2:
                    Refresh.setRefreshing(false);
                    Toast.makeText(MyHouse.this, R.string.intent_error, Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Refresh.setRefreshing(false);
                    Toast.makeText(MyHouse.this, R.string.nomore, Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Refresh.setRefreshing(false);
                    array.clear();
                    try {
                        setArray(urlstr);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case 5:
                    updatrurl = GlobalVariables.urlstr + "User.updateHouse&uid=" + uid + "&username=" + username + "&houseid=" + array.get(GlobalVariables.position).get("houseid") + "&fanghaoid=" + array.get(GlobalVariables.position).get("fanghaoid");
                    gethouselist(2);
                    break;
                case 6:
                    JSONObject jsonObject = JSON.parseObject(updatestr);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    int a = jsonObject1.getIntValue("code");
                    if (a != 0) {
                        Toast.makeText(MyHouse.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case 7:
                    updatrurl = GlobalVariables.urlstr + "User.delHouse&uid=" + uid + "&username=" + username + "&id=" + array.get(GlobalVariables.position).get("id");
                    gethouselist(3);
                    break;
                case 8:
                    jsonObject = JSON.parseObject(updatestr);
                    JSONObject jsonObject2 = jsonObject.getJSONObject("data");
                    a = jsonObject2.getIntValue("code");
                    if (a != 0) {
                        Toast.makeText(MyHouse.this, jsonObject2.getString("msg"), Toast.LENGTH_SHORT).show();
                    } else {
                        array.remove(GlobalVariables.position);
                        adapter.notifyDataSetChanged();
                        if (array.size() > 0) {
                            if (GlobalVariables.position == 0) {
                                updatrurl = GlobalVariables.urlstr + "User.updateHouse&uid=" + uid + "&username=" + username + "&houseid=" + array.get(GlobalVariables.position).get("houseid") + "&fanghaoid=" + array.get(GlobalVariables.position).get("fanghaoid");
                                gethouselist(2);
                            } else {
                                updatrurl = GlobalVariables.urlstr + "User.updateHouse&uid=" + uid + "&username=" + username + "&houseid=" + array.get(GlobalVariables.position - 1).get("houseid") + "&fanghaoid=" + array.get(GlobalVariables.position - 1).get("fanghaoid");
                                GlobalVariables.position = GlobalVariables.position - 1;
                                gethouselist(2);
                            }
                        } else {
                            PreferencesUtils.putString(MyHouse.this, "houseids", "0");
                            PreferencesUtils.putString(MyHouse.this, "houseid", "0");
                        }
                    }
                    break;
                case 9:
                    updatrurl = GlobalVariables.urlstr + "User.delHouse&uid=" + uid + "&username=" + username + "&id=" + array.get(GlobalVariables.position).get("id");
                    gethouselist(4);
                    break;
            }
        }
    };

    private void setArray(String str) throws Exception {
        JSONObject jsonObject = JSON.parseObject(str);
        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
        int a = jsonObject1.getIntValue("code");
        if (a == 0) {
            JSONArray jsonArray = jsonObject1.getJSONArray("info");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                hashMap = new HashMap<>();
                hashMap.put("id", jsonObject2.getString("id") == null ? "" : jsonObject2.getString("id"));
                hashMap.put("houseid", jsonObject2.getString("houseid") == null ? "" : jsonObject2.getString("houseid"));
                hashMap.put("xiaoqu", jsonObject2.getString("xiaoqu") == null ? "" : jsonObject2.getString("xiaoqu"));
                hashMap.put("louhao", jsonObject2.getString("louhao") == null ? "" : jsonObject2.getString("louhao"));
                hashMap.put("fanghao", jsonObject2.getString("fanghao") == null ? "" : jsonObject2.getString("fanghao"));
                hashMap.put("fanghaoid", jsonObject2.getString("fanghaoid") == null ? "" : jsonObject2.getString("fanghaoid"));
                hashMap.put("danyuan", jsonObject2.getString("danyuan") == null ? "" : jsonObject2.getString("danyuan"));
                array.add(hashMap);
            }
        } else {
            handler.sendEmptyMessage(3);
        }

    }

    private void setHouselist() {
        adapter = new HouseAdapter(array, MyHouse.this, handler);
        houselist.setAdapter(adapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Refresh.post(new Runnable() {
            @Override
            public void run() {
                Refresh.setRefreshing(true);
            }
        });
        onRefresh();
    }

    public void onRefresh() {
        Refresh.postDelayed(new Runnable() {

            @Override
            public void run() {
                // 更新数据
                array.clear();
                url = GlobalVariables.urlstr + "user.getHouseList&uid=" + uid + "&username=" + username;
                gethouselist(1);
            }
        }, 2000);

    }
}
