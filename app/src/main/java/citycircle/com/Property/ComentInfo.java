package citycircle.com.Property;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.Activity.PhotoLook;
import citycircle.com.MyViews.MyGridView;
import citycircle.com.Property.PropertyAdapter.ComentAdapter;
import citycircle.com.Property.PropertyAdapter.ProImgAdapter;
import citycircle.com.R;
import citycircle.com.Utils.DateUtils;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;

/**
 * Created by admins on 2016/2/17.
 */
public class ComentInfo extends Activity {
    ListView comentlist;
    View headview;
    TextView type, time, content;
    Button submit;
    EditText collected;
    MyGridView photogrid;
    String url, urlstr, id, contents, TYPE, picList, create_time;
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> comarray = new ArrayList<HashMap<String, String>>();
    ProImgAdapter newPhotoAdapter;
    ComentAdapter adapter;
    ImageView back;
    DateUtils dateUtils;
    String feurl, feurlstr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comentinfo);
//       getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        headview = LayoutInflater.from(this).inflate(
                R.layout.comenthead, null);
        id = getIntent().getStringExtra("id");
        contents = getIntent().getStringExtra("content");
        TYPE = getIntent().getStringExtra("type");
        picList = getIntent().getStringExtra("picList");
        create_time = getIntent().getStringExtra("time");
        url = GlobalVariables.urlstr + "Wuye.getFeedBackList&fid=" + id;

        intview();
        setComentlist();
        getComentstr(0);
    }

    private void intview() {
        submit = (Button) findViewById(R.id.submit);
        collected = (EditText) findViewById(R.id.collected);
        back = (ImageView) findViewById(R.id.back);
        comentlist = (ListView) findViewById(R.id.comentlist);
        type = (TextView) headview.findViewById(R.id.type);
        time = (TextView) headview.findViewById(R.id.time);
        content = (TextView) headview.findViewById(R.id.content);
        photogrid = (MyGridView) headview.findViewById(R.id.photogrid);
        comentlist.addHeaderView(headview);
        content.setText(contents);
        dateUtils = new DateUtils();
        time.setText(dateUtils.getDateToStrings(Long.parseLong(create_time)));
        if (TYPE.equals("0")) {
            type.setText("问题");
        } else if (TYPE.equals("1")) {
            type.setText("建议");
        } else {
            type.setText("表扬");
        }
        getImgArray();
        newPhotoAdapter = new ProImgAdapter(array, ComentInfo.this);
        photogrid.setAdapter(newPhotoAdapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (collected.getText().toString().trim().length() != 0) {
                    try {
                        feurl = GlobalVariables.urlstr + "Wuye.addFeedBack&fid=" + id + "&uid=5&username=cheng&content=" + URLEncoder.encode(collected.getText().toString(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    getComentstr(1);
                }
            }
        });
        photogrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GlobalVariables.parrays=array;
                Intent intent = new Intent();
                intent.putExtra("pos", position);
                intent.setClass(ComentInfo.this, PhotoLook.class);
                ComentInfo.this.startActivity(intent);
            }
        });
    }

    private void getComentstr(final int type) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                HttpRequest httpRequest = new HttpRequest();
                if (type == 0) {
                    urlstr = httpRequest.doGet(url);
                    if (urlstr.equals("网络超时")) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(1);
                    }
                } else {
                    feurlstr = httpRequest.doGet(feurl);
                    if (feurlstr.equals("网络超时")){
                        handler.sendEmptyMessage(2);
                    }else {
                        handler.sendEmptyMessage(3);
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
                    getarray(urlstr);
                    adapter.notifyDataSetChanged();
//                    comentlist.setSelection(array.size());
                    break;
                case 2:
                    Toast.makeText(ComentInfo.this, R.string.intent_error, Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    JSONObject jsonObject=JSON.parseObject(feurlstr);
                    JSONObject jsonObject1=jsonObject.getJSONObject("data");
                    if (jsonObject1.getIntValue("code")==0){
                        JSONObject jsonObject2=jsonObject1.getJSONObject("info");
                        hashMap = new HashMap<>();
                        hashMap.put("id", jsonObject2.getString("id") == null ? "" : jsonObject2.getString("id"));
                        hashMap.put("content", jsonObject2.getString("content") == null ? "" : jsonObject2.getString("content"));
                        hashMap.put("uid", jsonObject2.getString("uid") == null ? "" : jsonObject2.getString("uid"));
                        hashMap.put("create_time", jsonObject2.getString("create_time") == null ? "" : jsonObject2.getString("create_time"));
                        comarray.add(hashMap);
                        adapter.notifyDataSetChanged();
                        View view = getWindow().peekDecorView();
                        if (view != null) {
                            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputmanger.hideSoftInputFromWindow(
                                    view.getWindowToken(), 0);
                        }
                        collected.setText("");
                        comentlist.setSelection(ListView.FOCUS_DOWN);//刷新到底部
                    }else {
                        Toast.makeText(ComentInfo.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    private void getImgArray() {
        JSONArray jsonArray = JSON.parseArray(picList);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            hashMap = new HashMap<>();
            hashMap.put("path", jsonObject.getString("url") == null ? "" : jsonObject.getString("url"));
            array.add(hashMap);
        }
    }

    private void getarray(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
        int a = jsonObject1.getIntValue("code");
        if (a == 0) {
            JSONArray jsonArray = jsonObject1.getJSONArray("info");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                hashMap = new HashMap<>();
                hashMap.put("id", jsonObject2.getString("id") == null ? "" : jsonObject2.getString("id"));
                hashMap.put("content", jsonObject2.getString("content") == null ? "" : jsonObject2.getString("content"));
                hashMap.put("uid", jsonObject2.getString("uid") == null ? "" : jsonObject2.getString("uid"));
                hashMap.put("create_time", jsonObject2.getString("create_time") == null ? "" : jsonObject2.getString("create_time"));
                comarray.add(hashMap);
            }
        } else {

        }

    }

    private void setComentlist() {
        adapter = new ComentAdapter(comarray, ComentInfo.this);
        comentlist.setAdapter(adapter);
    }
}
