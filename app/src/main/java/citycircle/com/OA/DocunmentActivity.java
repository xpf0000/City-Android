package citycircle.com.OA;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import citycircle.com.OA.uitls.StringtoJsontoo;
import citycircle.com.R;
import citycircle.com.Utils.DateUtils;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;
import citycircle.com.Utils.PreferencesUtils;

public class DocunmentActivity extends Activity {
    AutoCompleteTextView messageslistserach;
    SwipeMenuListView messageslist;
    SimpleAdapter adapter;
    List<Map<String, String>> items = new ArrayList<Map<String, String>>();
    Map<String, String> map;
    List<Map<String, String>> myitems = new ArrayList<Map<String, String>>();
    Map<String, String> mymap;
    List<Map<String, String>> searchitems = new ArrayList<Map<String, String>>();
    ImageView messagetback;
    String messagestr, url,username;
    DateUtils date;
    String dwid, bmid , userid;
    StringtoJsontoo strtojson;
    int b = 0;
    ImageView addmessaget;
    int a = 0;
    int root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.document);
        strtojson = new StringtoJsontoo();
        username=PreferencesUtils.getString(DocunmentActivity.this, "oausername");
        userid = PreferencesUtils.getString(DocunmentActivity.this, "oauid");
        dwid = PreferencesUtils.getString(DocunmentActivity.this, "dwid");
        bmid=PreferencesUtils.getString(DocunmentActivity.this, "bmid");
        url= GlobalVariables.oaurlstr+"Document.getList&dwid="+dwid+"&bmid="+bmid+"&username="+username+"&uid="+userid;
        String mymessage = PreferencesUtils.getString(DocunmentActivity.this,
                userid + "Document");

        try {
            if (mymessage == null) {
            } else {
                setmymessagelist(mymessage);
            }

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        intview();
        getmessagestr(url, 1);
        setmessage();
        SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // 设置背景
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // 设置宽度
                deleteItem.setWidth(dp2px(90));
                // 设置一个图标
                deleteItem.setIcon(R.drawable.ic_delete);
                // 添加到菜单
                menu.addMenuItem(deleteItem);

            }
        };
        messageslist.setMenuCreator(swipeMenuCreator);
        messageslist.setOnMenuItemClickListener(new OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu,
                                           int index) {
                switch (index) {
                    case 0:
                        if (a == 1) {
                            for (int i = 0; i < items.size(); i++) {
                                if (searchitems.get(position).get("id")
                                        .equals(items.get(i).get("id"))) {
                                    items.remove(i);
                                    searchitems.remove(position);
                                    adapter.notifyDataSetChanged();
                                    String myjson = strtojson.getJson(items);
                                    PreferencesUtils.putString(
                                            DocunmentActivity.this, userid
                                                    + "Document", myjson);
                                    break;
                                }

                            }
                        } else {
                            items.remove(position);
                            adapter.notifyDataSetChanged();
                            String myjson = strtojson.getJson(items);
                            PreferencesUtils.putString(DocunmentActivity.this, userid
                                    + "Document", myjson);
                        }
                        break;

                    default:
                        break;
                }
                return false;
            }
        });
        messageslist.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent();
                if (a == 1) {
                    intent.putExtra("id", searchitems.get(position).get("id"));
                    intent.setClass(DocunmentActivity.this, DocumentContent.class);
                    DocunmentActivity.this.startActivity(intent);
                } else {
                    intent.putExtra("id", items.get(position).get("id"));
                    intent.setClass(DocunmentActivity.this, DocumentContent.class);
                    DocunmentActivity.this.startActivity(intent);
                }

            }
        });
        addmessaget.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//				getmessagestr(getuser, 5);
                int a=PreferencesUtils.getInt(DocunmentActivity.this, "新增日程");
                if(a==0){
                    Toast.makeText(DocunmentActivity.this, "暂无权限", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent2 = new Intent();
                    intent2.setClass(DocunmentActivity.this, AddMessage.class);
                    DocunmentActivity.this.startActivity(intent2);
                }
            }
        });

    }

    private void getmessagestr(final String url, final int what) {
        new Thread() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                super.run();
                HttpRequest httpRequest = new HttpRequest();
                if (what == 1) {
                    messagestr = httpRequest.doGet(url);
                    if (messagestr.equals("网络超时")) {
                        handler.sendEmptyMessage(3);
                    } else {
                        handler.sendEmptyMessage(what);
                    }
                } else if (what == 5) {

                }

            }
        }.start();

    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        setmessagelist(messagestr);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case 2:
//                    if (root==1){
//                        addmessaget.setVisibility(View.VISIBLE);
//                    }else {
//                        addmessaget.setVisibility(View.GONE);
//                    }
                    Toast.makeText(DocunmentActivity.this, b + "条新消息",
                            Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(DocunmentActivity.this, "网络超时", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case 4:
//                    if (root==1){
//                        addmessaget.setVisibility(View.VISIBLE);
//                    }else {
//                        addmessaget.setVisibility(View.INVISIBLE);
//                    }
                    items.addAll(myitems);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(DocunmentActivity.this, "暂无更多新消息",
                            Toast.LENGTH_SHORT).show();
                    break;
                case 5:
//				JSONObject jsonObject = JSONObject.parseObject(userroot);
//				if (jsonObject.getIntValue("status") == 0) {
//					if (jsonObject.getBooleanValue("addMessages")) {
//						Intent intent2 = new Intent();
//						intent2.setClass(MessageActivity.this, AddMessage.class);
//						MessageActivity.this.startActivity(intent2);
//					} else {
//						Toast.makeText(MessageActivity.this, "没有权限",
//								Toast.LENGTH_SHORT).show();
//					}
//				} else {
//					Toast.makeText(MessageActivity.this, "验证权限失败！！",
//							Toast.LENGTH_SHORT).show();
//				}
                    break;
                case 6:
                    Toast.makeText(DocunmentActivity.this, "验证权限失败！！",
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        };
    };

    void setmessagelist(String str) throws ParseException {
        JSONObject jsonObject = JSONObject.parseObject(str);
        JSONObject jsonObject1=jsonObject.getJSONObject("data");
        int a = jsonObject1.getIntValue("code");
        root=jsonObject1.getIntValue("add");
        date = new DateUtils();
        if (a == 0) {
            JSONArray array = jsonObject1.getJSONArray("info");
            b = array.size();
            for (int i = 0; i < array.size(); i++) {
                JSONObject jsonObject2 = array.getJSONObject(i);
                map = new HashMap<String, String>();
                map.put("id", jsonObject2.getString("id") == null ? ""
                        : jsonObject2.getString("id"));
                map.put("truename", jsonObject2.getString("truename") == null ? ""
                        : jsonObject2.getString("truename"));
                long time = jsonObject2.getLongValue("create_time");
                map.put("create_time", date.getDateToStringsss(time));
                map.put("title", jsonObject2.getString("title") == null ? ""
                        : jsonObject2.getString("title"));
                map.put("content", jsonObject2.getString("content") == null ? ""
                        : jsonObject2.getString("content"));
                items.add(map);
            }
            items.addAll(myitems);
            handler.sendEmptyMessage(2);
            String myjson = strtojson.getJson(items);
            PreferencesUtils.putString(DocunmentActivity.this,
                    userid + "Document", myjson);
        } else {
            handler.sendEmptyMessage(4);
        }

    }

    void intview() {
        messageslistserach = (AutoCompleteTextView) findViewById(R.id.messageslistserach);
        addmessaget = (ImageView) findViewById(R.id.addmessaget);
        messageslistserach = (AutoCompleteTextView) findViewById(R.id.messageslistserach);
        messageslist = (SwipeMenuListView) findViewById(R.id.messageslist);
        messagetback = (ImageView) findViewById(R.id.messagetback);
        messagetback.setVisibility(View.VISIBLE);
        messagetback.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });
        messageslistserach.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (messageslistserach.getText().toString().trim().equals("")) {
                    a = 0;
                    adapter = new SimpleAdapter(DocunmentActivity.this, items,
                            R.layout.messagelist_item, new String[]{"title",
                            "create_time", "truename"}, new int[]{
                            R.id.message_title, R.id.message_aut,
                            R.id.message_time});
                    messageslist.setAdapter(adapter);
                } else {
                    a = 1;
                    searchitems.clear();
                    adapter.notifyDataSetChanged();
                    for (int i = 0; i < items.size(); i++) {
                        if (items.get(i).get("title")
                                .contains(messageslistserach.getText())
                                || items.get(i).get("truename")
                                .contains(messageslistserach.getText())
                                || items.get(i).get("create_time")
                                .contains(messageslistserach.getText())) {
                            searchitems.add(items.get(i));
                            adapter = new SimpleAdapter(
                                    DocunmentActivity.this,
                                    searchitems,
                                    R.layout.messagelist_item,
                                    new String[]{"title", "create_time",
                                            "truename"},
                                    new int[]{R.id.message_title,
                                            R.id.message_aut, R.id.message_time});
                            messageslist.setAdapter(adapter);
                        }
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

    }

    void setmessage() {
        adapter = new SimpleAdapter(DocunmentActivity.this, items,
                R.layout.messagelist_item, new String[] { "title", "create_time",
                "truename" }, new int[] { R.id.message_title,
                R.id.message_aut, R.id.message_time });
        messageslist.setAdapter(adapter);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    void setmymessagelist(String str) throws ParseException {
        JSONObject jsonObject = JSONObject.parseObject(str);
        date = new DateUtils();
        JSONArray array = jsonObject.getJSONArray("list");
        for (int i = 0; i < array.size(); i++) {
            JSONObject jsonObject2 = array.getJSONObject(i);

            mymap = new HashMap<String, String>();
            mymap.put("id", jsonObject2.getString("id") == null ? ""
                    : jsonObject2.getString("id"));
            mymap.put("truename", jsonObject2.getString("truename") == null ? ""
                    : jsonObject2.getString("truename"));
//			long time = jsonObject2.getLongValue("create_time");
            mymap.put("create_time", jsonObject2.getString("create_time"));
            mymap.put("title", jsonObject2.getString("title") == null ? ""
                    : jsonObject2.getString("title"));
            mymap.put("content", jsonObject2.getString("content") == null ? ""
                    : jsonObject2.getString("content"));
            myitems.add(mymap);
        }
    }

}
