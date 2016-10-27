package citycircle.com.OA;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.OA.OAAdapter.SubscrAdapter;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;
import citycircle.com.Utils.PreferencesUtils;

/**
 * Created by admins on 2016/1/6.
 */
public class BumenFragment extends Fragment {
    private View view;
    ExpandableListView expandableListView;
    ArrayList<HashMap<String, String>> groupArray = new ArrayList<HashMap<String, String>>();
    ArrayList<ArrayList<HashMap<String, String>>> childArray;
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> childMap;
    HashMap<String, String> mychildMap;
    String url, urlstr,username,dwid,uid;
    SubscrAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bumenlayout, container, false);
        username = PreferencesUtils.getString(getActivity(), "oausername");
        dwid = PreferencesUtils.getString(getActivity(), "dwid");
        uid = PreferencesUtils.getString(getActivity(), "oauid");
        url= GlobalVariables.oaurlstr+"tel.getlistbybm&username="+username+"&uid="+uid+"&dwid="+dwid;
        intview();
        getstr();
        return view;
    }

    private void intview() {
        expandableListView = (ExpandableListView) view.findViewById(R.id.tellist);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent=new Intent();
                intent.putExtra("getAddress",childArray.get(groupPosition).get(childPosition).get("address"));
                intent.putExtra("getEmail",childArray.get(groupPosition).get(childPosition).get("email"));
                intent.putExtra("getId",childArray.get(groupPosition).get(childPosition).get("id"));
                intent.putExtra("getMobile",childArray.get(groupPosition).get(childPosition).get("mobile"));
                intent.putExtra("getQq",childArray.get(groupPosition).get(childPosition).get("qq"));
                intent.putExtra("getTel",childArray.get(groupPosition).get(childPosition).get("tel"));
                intent.putExtra("getTruename",childArray.get(groupPosition).get(childPosition).get("truename"));
                intent.setClass(getActivity(),TelMessage.class);
                getActivity().startActivity(intent);
                return false;
            }
        });
    }

    private void getstr() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                HttpRequest httpRequest = new HttpRequest();
                urlstr = httpRequest.doGet(url);
                if (urlstr.equals("网络超时")) {
                    handler.sendEmptyMessage(2);
                } else {
                    handler.sendEmptyMessage(1);
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
                    setClassstr(urlstr);
                    setChildArray(urlstr);
                    setclass();
                    break;
                case 2:
                    break;
                case 3:
                    break;
            }
        }
    };
    public void setClassstr(String classstr) {
        JSONObject jsonObject = JSONObject.parseObject(classstr);
        JSONObject jsonObject1=jsonObject.getJSONObject("data");
        int a = jsonObject1.getIntValue("code");
        if (a == 0) {
            JSONArray jsonArray = jsonObject1.getJSONArray("info");
            groupArray = new ArrayList<HashMap<String, String>>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                hashMap = new HashMap<String, String>();
                hashMap.put("title", jsonObject2.getString("title") == null ? "" : jsonObject2.getString("title"));
                hashMap.put("id", jsonObject2.getString("id") == null ? "" : jsonObject2.getString("id"));
                groupArray.add(hashMap);
            }
        } else {
            handler.sendEmptyMessage(3);
        }

    }
    public void setChildArray(String classstr) {
        JSONObject jsonObject = JSONObject.parseObject(classstr);
        JSONObject jsonObject1=jsonObject.getJSONObject("data");
        int a = jsonObject1.getIntValue("code");
        if (a == 0) {
            JSONArray jsonArray = jsonObject1.getJSONArray("info");
            childArray = new ArrayList<ArrayList<HashMap<String, String>>>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject2 = (JSONObject) jsonArray.get(i);
                JSONArray jsonArray3 = jsonObject2.getJSONArray("memberList");
                childMap = new ArrayList<HashMap<String, String>>();
                for (int j = 0; j < jsonArray3.size(); j++) {
                    mychildMap = new HashMap<String, String>();
                    JSONObject jsonObject3=jsonArray3.getJSONObject(j);
                    mychildMap.put("id", jsonObject3.getString("id"));
                    mychildMap.put("truename", jsonObject3.getString("truename"));
                    mychildMap.put("tel", jsonObject3.getString("tel"));
                    mychildMap.put("mobile", jsonObject3.getString("mobile"));
                    mychildMap.put("email", jsonObject3.getString("email"));
                    mychildMap.put("qq", jsonObject3.getString("qq"));
                    mychildMap.put("address", jsonObject3.getString("address"));
                    childMap.add(mychildMap);
                }
                childArray.add(childMap);
            }
        } else {
            handler.sendEmptyMessage(3);
        }

    }
    public void setclass() {
        expandableListView.setGroupIndicator(null);
        adapter = new SubscrAdapter(getActivity(), groupArray, childArray, getActivity(), handler);
        expandableListView.setAdapter(adapter);
    }
}
