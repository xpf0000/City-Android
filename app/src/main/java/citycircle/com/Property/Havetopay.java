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
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.Property.PropertyAdapter.PayWuAdapter;
import citycircle.com.Property.PropertyAdapter.PaysAdapter;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;
import citycircle.com.Utils.PreferencesUtils;

/**
 * Created by 飞侠 on 2016/2/23.
 */
public class Havetopay extends Fragment {
    View view;
    //    SwipeRefreshLayout swipeRefreshLayout;
    ListView list;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    HashMap<String, String> hashMap;
    String url, urlstr, uid, username, houseid, type, fangid;
    PaysAdapter paysAdapter;
    LinearLayout dian,wuye;
    PayWuAdapter payWuAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.money, container, false);
        uid = PreferencesUtils.getString(getActivity(), "userid");
        username = PreferencesUtils.getString(getActivity(), "username");
//        houseid = PreferencesUtils.getString(getActivity(), "houseids");
        fangid = PreferencesUtils.getString(getActivity(), "fanghaoid");
        Bundle mBundle = getArguments();
        type=mBundle.getString("type");
        intview();
        setArrayList();
        url = GlobalVariables.urlstr+"wuye.getPayList&uid=" + uid + "&username=" + username + "&fanghaoid=" + fangid + "&type=" + type + "&status=1";
        getList();
        return view;
    }

    private void intview() {
        dian=(LinearLayout)view.findViewById(R.id.dian);
        wuye=(LinearLayout)view.findViewById(R.id.wuye);
        if (type.equals("1")||type.equals("4")){
            wuye.setVisibility(View.VISIBLE);
            dian.setVisibility(View.GONE);
        }else {
            dian.setVisibility(View.VISIBLE);
            wuye.setVisibility(View.GONE);
        }
//        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.Refresh);
        list = (ListView) view.findViewById(R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), PayMoney.class);
                intent.putExtra("type", type);
                intent.putExtra("yumoney", arrayList.get(position).get("yumoney"));
                intent.putExtra("snumber", arrayList.get(position).get("snumber"));
                intent.putExtra("bnumber", arrayList.get(position).get("bnumber"));
                intent.putExtra("unumber", arrayList.get(position).get("unumber"));
                intent.putExtra("ymoney", arrayList.get(position).get("ymoney"));
                intent.putExtra("smoney", arrayList.get(position).get("smoney"));
                intent.putExtra("create_time", arrayList.get(position).get("create_time"));
                intent.putExtra("status", "1");
                getActivity().startActivity(intent);
            }
        });
    }

    private void getList() {
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
                    getarray(urlstr);
                    try {
                        paysAdapter.notifyDataSetChanged();
                    }catch (Exception e){
                        payWuAdapter.notifyDataSetChanged();
                    }
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
            }
        }
    };

    private void getarray(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
        int a = jsonObject1.getIntValue("code");
        if (a == 0) {
            JSONArray jsonArray = jsonObject1.getJSONArray("info");
            for (int i = 0; i < jsonArray.size(); i++) {
                hashMap = new HashMap<>();
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                hashMap.put("id", jsonObject2.getString("id") == null ? "" : jsonObject2.getString("id"));
                hashMap.put("yumoney", jsonObject2.getString("yumoney") == null ? "" : jsonObject2.getString("yumoney"));
                hashMap.put("snumber", jsonObject2.getString("snumber") == null ? "" : jsonObject2.getString("snumber"));
                hashMap.put("bnumber", jsonObject2.getString("bnumber") == null ? "" : jsonObject2.getString("bnumber"));
                hashMap.put("unumber", jsonObject2.getString("unumber") == null ? "" : jsonObject2.getString("unumber"));
                hashMap.put("ymoney", jsonObject2.getString("ymoney") == null ? "" : jsonObject2.getString("ymoney"));
                hashMap.put("smoney", jsonObject2.getString("smoney") == null ? "" : jsonObject2.getString("smoney"));
                hashMap.put("create_time", jsonObject2.getString("create_time") == null ? "" : jsonObject2.getString("create_time"));
                hashMap.put("bak", jsonObject2.getString("bak") == null ? "" : jsonObject2.getString("bak"));
                arrayList.add(hashMap);
            }
        } else {

        }

    }

    private void setArrayList() {
        if (type.equals("1")||type.equals("4")){
            payWuAdapter=new PayWuAdapter(arrayList, getActivity());
            list.setAdapter(payWuAdapter);
        }else {
            paysAdapter = new PaysAdapter(arrayList, getActivity());
            list.setAdapter(paysAdapter);
        }

    }
}
