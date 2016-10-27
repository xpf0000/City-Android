package citycircle.com.Property;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;
import citycircle.com.Utils.PreferencesUtils;

/**
 * Created by admins on 2016/1/30.
 */
public class SerachHome extends Fragment implements View.OnClickListener, OnItemClickListener {
    private View view;
    Animation translateAnimation;
    int types;
    Button calm, village, door, addhouse, villages;
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
    String url, urlstr, addurl, calmid, villageid, doord, username, uid, villagesid;
    HashMap<String, String> hashMap;
    String alretstr[];
    int type = 0, satatus = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.searhome, container, false);
        intview();
        types = getActivity().getIntent().getIntExtra("types", 0);
        username = PreferencesUtils.getString(getActivity(), "username");
        uid = PreferencesUtils.getString(getActivity(), "userid");
        return view;
    }

    private void intview() {
        translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(5));
        translateAnimation.setDuration(1000);
        calm = (Button) view.findViewById(R.id.calm);
        village = (Button) view.findViewById(R.id.village);
        door = (Button) view.findViewById(R.id.door);
        addhouse = (Button) view.findViewById(R.id.addhouse);
        villages = (Button) view.findViewById(R.id.villages);
        villages.setOnClickListener(this);
        calm.setOnClickListener(this);
        village.setOnClickListener(this);
        door.setOnClickListener(this);
        addhouse.setOnClickListener(this);
    }

    private void getStr(final int type) {
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
                    urlstr = httpRequest.doGet(addurl);
                    if (urlstr.equals("网络超时")) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(4);
                    }
                }

            }
        }.start();
    }

    public void setArray(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
        int a = jsonObject1.getIntValue("code");
        if (a == 0) {
            JSONArray jsonArray = jsonObject1.getJSONArray("info");
            alretstr = new String[jsonArray.size()];
            for (int i = 0; i < jsonArray.size(); i++) {
                hashMap = new HashMap<>();
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                hashMap.put("id", jsonObject2.getString("id") == null ? "" : jsonObject2.getString("id"));
                hashMap.put("title", jsonObject2.getString("title") == null ? "" : jsonObject2.getString("title"));
                alretstr[i] = jsonObject2.getString("title");
                array.add(hashMap);
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    setArray(urlstr);
                    Alertshow(alretstr);
                    break;
                case 2:
                    try {
                        Toast.makeText(getActivity(), R.string.intent_error, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {

                    }
                    break;
                case 3:
                    try {
                        Toast.makeText(getActivity(), R.string.nomore, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {

                    }
                    break;
                case 4:
                    JSONObject jsonObject = JSON.parseObject(urlstr);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    if (jsonObject1.getIntValue("code") == 0) {
                        if (types == 1) {
                            PreferencesUtils.putString(getActivity(), "houseid", villageid);
                            PreferencesUtils.putString(getActivity(), "houseids", villageid);
                            PreferencesUtils.putString(getActivity(), "fanghaoid", doord);
                            addurl = GlobalVariables.urlstr + "User.updateHouse&uid=" + uid + "&username=" + username + "&houseid=" + villagesid + "&fanghaoid=" + doord;
                            getStr(1);
                        } else {
                            Toast.makeText(getActivity(), "添加成功", Toast.LENGTH_SHORT).show();
                        }
                        if (types == 1) {
                            Intent intent = new Intent();
                            intent.setClass(getActivity(), PropertyHome.class);
                            getActivity().startActivity(intent);
                        }
                        getActivity().finish();

                    } else {
                        Toast.makeText(getActivity(), jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    private void Alertshow(String str[]) {
        new AlertView(null, null, null, null,
                str, getActivity(), AlertView.Style.Alert, this).show();
    }

    @Override
    public void onItemClick(Object o, int position) {
        if (array.size() != 0) {
            if (type == 0) {
                if (calmid != null && !calmid.equals(array.get(position).get("id"))) {
                    villagesid = null;
                    villages.setText("点击选择小区");
                    villageid = null;
                    village.setText("点击选择区域");
                    doord = null;
                    door.setText("点击选择门牌");
                    addhouse.setBackgroundResource(R.mipmap.btn_bg_g);
                    satatus = 0;
                }
                calmid = array.get(position).get("id");
                calm.setText(array.get(position).get("title"));
            } else if (type == 1) {
                if (villagesid != null && !villageid.equals(array.get(position).get("id"))) {
                    villageid = null;
                    village.setText("点击选择区域");
                    doord = null;
                    door.setText("点击选择门牌");
                    addhouse.setBackgroundResource(R.mipmap.btn_bg_g);
                    satatus = 0;
                }
                villagesid = array.get(position).get("id");
                villages.setText(array.get(position).get("title"));
            } else if (type == 2) {
                if (villageid != null && !villageid.equals(array.get(position).get("id"))) {

                    doord = null;
                    door.setText("点击选择门牌");
                    addhouse.setBackgroundResource(R.mipmap.btn_bg_g);
                    satatus = 0;
                }
                villageid = array.get(position).get("id");
                village.setText(array.get(position).get("title"));
            } else if (type == 3) {
                doord = array.get(position).get("id");
                door.setText(array.get(position).get("title"));
                addhouse.setBackgroundResource(R.drawable.btn_bg);
                satatus = 1;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.calm:
                array.clear();
                type = 0;
                url = GlobalVariables.urlstr + "Common.getHouseList&pid=0&type=2";
                getStr(0);
                break;
            case R.id.villages:
                type = 1;
                if (calmid == null) {
                    calm.startAnimation(translateAnimation);
                } else {
                    array.clear();
                    url = GlobalVariables.urlstr + "Common.getHouseList&type=2&pid=" + calmid;
                    getStr(0);
                }
                break;
            case R.id.door:
                type = 3;
                if (villageid == null) {
                    village.startAnimation(translateAnimation);
                } else {
                    array.clear();
                    url = GlobalVariables.urlstr + "Common.getHouseList&type=2&pid=" + villageid;
                    getStr(0);
                }
                break;
            case R.id.addhouse:
                if (satatus != 0) {
                    addurl = GlobalVariables.urlstr + "User.addHouse&uid=" + uid + "&username=" + username + "&fanghaoid=" + doord+"&houseid="+villagesid;
                    getStr(1);
                }
                break;
            case R.id.village:
                type = 2;
                if (villagesid == null) {
                    calm.startAnimation(translateAnimation);
                } else {
                    array.clear();
                    url = GlobalVariables.urlstr + "Common.getHouseList&type=2&pid=" + villagesid;
                    getStr(0);
                }
                break;
        }
    }
}
