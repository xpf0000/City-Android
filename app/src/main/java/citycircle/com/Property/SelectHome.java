package citycircle.com.Property;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
public class SelectHome extends Fragment implements OnItemClickListener, View.OnClickListener {
    private View view;
    Animation translateAnimation;
    int types;
    Button calm, village, building, unit, floor, room, addhouse;
    String url, urlstr, calmid, villageid, buildingid, unitid, floorid, roomid, addurl, uid, username;
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> hashMap;
    String alretstr[];
    int type = 0, satatus = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.selecthome, container, false);
        intview();
        types = getActivity().getIntent().getIntExtra("types", 0);
        username = PreferencesUtils.getString(getActivity(), "username");
        ;
        uid = PreferencesUtils.getString(getActivity(), "userid");
        ;
//        Alertshow(new String[]{"其他按钮1", "其他按钮2", "其他按钮3"});
        return view;
    }

    private void intview() {
        translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(5));
        translateAnimation.setDuration(1000);
        calm = (Button) view.findViewById(R.id.calm);
        village = (Button) view.findViewById(R.id.village);
        building = (Button) view.findViewById(R.id.building);
        unit = (Button) view.findViewById(R.id.unit);
        floor = (Button) view.findViewById(R.id.floor);
        room = (Button) view.findViewById(R.id.room);
        addhouse = (Button) view.findViewById(R.id.addhouse);
        calm.setOnClickListener(this);
        village.setOnClickListener(this);
        building.setOnClickListener(this);
        unit.setOnClickListener(this);
        floor.setOnClickListener(this);
        room.setOnClickListener(this);
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
                            PreferencesUtils.putString(getActivity(), "fanghaoid", roomid);
                            addurl = GlobalVariables.urlstr + "User.updateHouse&uid=" + uid + "&username=" + username + "&houseid=" + villageid + "&fanghaoid=" + roomid;
                            getStr(1);
                        } else {
                            Toast.makeText(getActivity(), "添加成功", Toast.LENGTH_SHORT).show();
                        }
                        if (types==1){
                            Intent intent=new Intent();
                            intent.setClass(getActivity(),PropertyHome.class);
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

    //弹出框点击
    @Override
    public void onItemClick(Object o, int i) {
        if (array.size()!=0){
            if (type == 0) {
                if (calmid != null && !calmid.equals(array.get(i).get("id"))) {
                    villageid = null;
                    village.setText("点击选择小区");
                    buildingid = null;
                    building.setText("点击选择楼号");
                    unitid = null;
                    unit.setText("点击选择单元");
                    floorid = null;
                    floor.setText("点击选择楼层");
                    roomid = null;
                    room.setText("点击选择房号");
                    addhouse.setBackgroundResource(R.mipmap.btn_bg_g);
                    satatus = 0;
                }
                calmid = array.get(i).get("id");
                calm.setText(array.get(i).get("title"));
            } else if (type == 1) {
                if (villageid != null && !villageid.equals(array.get(i).get("id"))) {
                    buildingid = null;
                    building.setText("点击选择楼号");
                    unitid = null;
                    unit.setText("点击选择单元");
                    floorid = null;
                    floor.setText("点击选择楼层");
                    roomid = null;
                    room.setText("点击选择房号");
                    addhouse.setBackgroundResource(R.mipmap.btn_bg_g);
                    satatus = 0;
                }
                villageid = array.get(i).get("id");
                village.setText(array.get(i).get("title"));
            } else if (type == 2) {
                if (buildingid != null && !buildingid.equals(array.get(i).get("id"))) {
                    unitid = null;
                    unit.setText("点击选择单元");
                    floorid = null;
                    floor.setText("点击选择楼层");
                    roomid = null;
                    room.setText("点击选择房号");
                    addhouse.setBackgroundResource(R.mipmap.btn_bg_g);
                    satatus = 0;
                }
                buildingid = array.get(i).get("id");
                building.setText(array.get(i).get("title"));
            } else if (type == 3) {
                if (unitid != null && !unitid.equals(array.get(i).get("id"))) {
                    floorid = null;
                    floor.setText("点击选择楼层");
                    roomid = null;
                    room.setText("点击选择房号");
                    addhouse.setBackgroundResource(R.mipmap.btn_bg_g);
                    satatus = 0;
                }
                unitid = array.get(i).get("id");
                unit.setText(array.get(i).get("title"));
            } else if (type == 4) {
                if (floorid != null && !floorid.equals(array.get(i).get("id"))) {
                    roomid = null;
                    room.setText("点击选择房号");
                    addhouse.setBackgroundResource(R.mipmap.btn_bg_g);
                    satatus = 0;
                }
                floorid = array.get(i).get("id");
                floor.setText(array.get(i).get("title"));
            } else if (type == 5) {
                roomid = array.get(i).get("id");
                room.setText(array.get(i).get("title"));
                addhouse.setBackgroundResource(R.drawable.btn_bg);
                satatus = 1;
            }
        }
    }

    //按钮点击
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.calm:
                array.clear();
                type = 0;
                url = GlobalVariables.urlstr + "Common.getHouseList&pid=0&type=1";
                getStr(0);
                break;
            case R.id.village:
                type = 1;
                if (calmid == null) {
                    calm.startAnimation(translateAnimation);
                } else {
                    array.clear();
                    url = GlobalVariables.urlstr + "Common.getHouseList&type=1&pid=" + calmid;
                    getStr(0);
                }
                break;
            case R.id.building:
                type = 2;
                if (villageid == null) {
                    village.startAnimation(translateAnimation);
                } else {
                    array.clear();
                    url = GlobalVariables.urlstr + "Common.getHouseList&type=1&pid=" + villageid;
                    getStr(0);
                }
                break;
            case R.id.unit:
                type = 3;
                if (buildingid == null) {
                    building.startAnimation(translateAnimation);
                } else {
                    array.clear();
                    url = GlobalVariables.urlstr + "Common.getHouseList&type=1&pid=" + buildingid;
                    getStr(0);
                }
                break;
            case R.id.floor:
                type = 4;
                if (unitid == null) {
                    unit.startAnimation(translateAnimation);
                } else {
                    array.clear();
                    url = GlobalVariables.urlstr + "Common.getHouseList&type=1&pid=" + unitid;
                    getStr(0);
                }
                break;
            case R.id.room:
                type = 5;
                if (floorid == null) {
                    floor.startAnimation(translateAnimation);
                } else {
                    array.clear();
                    url = GlobalVariables.urlstr + "Common.getHouseList&type=1&pid=" + floorid;
                    getStr(0);
                }
                break;
            case R.id.addhouse:
                if (satatus != 0) {
                    addurl = GlobalVariables.urlstr + "User.addHouse&uid=" + uid + "&username=" + username + "&fanghaoid=" + roomid+"&houseid="+villageid;
                    getStr(1);
                }
                break;
        }
    }
}
