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
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import citycircle.com.OA.OAAdapter.CityNameMod;
import citycircle.com.OA.OAAdapter.SortAdapter;
import citycircle.com.OA.uitls.CharacterParser;
import citycircle.com.OA.uitls.PinyinComparator;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;
import citycircle.com.Utils.PreferencesUtils;

/**
 * Created by admins on 2016/1/6.
 */
public class Allfragment extends Fragment {
    private ListView sortListView;
    private View view;
    private PinyinComparator pinyinComparator;
    private CharacterParser characterParser;
    String url, username, dwid, uid, urlstr;
    private SortAdapter adapter;
    private List<CityNameMod> SourceDateList=new ArrayList<CityNameMod>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.alllist, container, false);
        username = PreferencesUtils.getString(getActivity(), "oausername");
        dwid = PreferencesUtils.getString(getActivity(), "dwid");
        uid = PreferencesUtils.getString(getActivity(), "oauid");
        url = GlobalVariables.oaurlstr + "Tel.getList&dwid=130&uid=1&username=test";
        intview();
        getStr();
        return view;
    }

    private void intview() {
        pinyinComparator = new PinyinComparator();
        sortListView = (ListView) view.findViewById(R.id.country_lvcountry);
        characterParser = CharacterParser.getInstance();
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                intent.putExtra("getAddress",((CityNameMod) adapter.getItem(position)).getAddress());
                intent.putExtra("getEmail",((CityNameMod) adapter.getItem(position)).getEmail());
                intent.putExtra("getId",((CityNameMod) adapter.getItem(position)).getId());
                intent.putExtra("getMobile",((CityNameMod) adapter.getItem(position)).getMobile());
                intent.putExtra("getQq",((CityNameMod) adapter.getItem(position)).getQq());
                intent.putExtra("getTel",((CityNameMod) adapter.getItem(position)).getTel());
                intent.putExtra("getTruename",((CityNameMod) adapter.getItem(position)).getTruename());
                intent.setClass(getActivity(), TelMessage.class);
                getActivity().startActivity(intent);
            }
        });
    }

    private void getStr() {
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
                    setSortListView(urlstr);
                    adapter = new SortAdapter(getActivity(), SourceDateList);
                    sortListView.setAdapter(adapter);
                    break;
                case 2:
                    break;
                case 3:
                    break;
            }
        }
    };
    private void setSortListView(String str){
        JSONObject jsonObject= JSON.parseObject(str);
        JSONObject jsonObject1=jsonObject.getJSONObject("data");
        int a=jsonObject1.getIntValue("code");
        if (a==0){
            JSONArray jsonArray=jsonObject1.getJSONArray("info");
            for (int i=0;i<jsonArray.size();i++){
                JSONObject jsonCity = jsonArray.getJSONObject(i);
                CityNameMod city = new CityNameMod();
                city.setId(jsonCity.getString("id"));
                city.setTruename(jsonCity.getString("truename"));
                city.setSortLetters(characterParser.getSelling(jsonCity.getString("truename")).substring(0,1).toUpperCase());
                city.setAddress(jsonCity.getString("address"));
                city.setEmail(jsonCity.getString("email"));
                city.setMobile(jsonCity.getString("mobile"));
                city.setQq(jsonCity.getString("qq"));
                city.setTel(jsonCity.getString("tel"));
                SourceDateList.add(city);
            }
            GlobalVariables.SourceDateList=SourceDateList;
        }
    }

}
