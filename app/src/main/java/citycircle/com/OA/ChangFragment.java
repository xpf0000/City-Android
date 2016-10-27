package citycircle.com.OA;

import android.content.Intent;
import android.os.Bundle;
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

import citycircle.com.OA.OAAdapter.ChangAdapter;
import citycircle.com.OA.OAAdapter.CityNameMod;
import citycircle.com.R;
import citycircle.com.Utils.PreferencesUtils;

/**
 * Created by admins on 2016/1/6.
 */
public class ChangFragment extends Fragment {
    private View view;
    String collectstr;
    ListView country_lvcountry;
    ArrayList<CityNameMod> arrayList = new ArrayList<CityNameMod>();
    CityNameMod cityNameMod;
    ChangAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.changactivity, container, false);
        collectstr = PreferencesUtils.getString(getActivity(), "collectphone");
        country_lvcountry = (ListView) view.findViewById(R.id.country_lvcountry);
        getString(collectstr);
        adapter=new ChangAdapter(getActivity(),arrayList);
        country_lvcountry.setAdapter(adapter);
        country_lvcountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                intent.putExtra("getAddress", arrayList.get(position).getAddress());
                intent.putExtra("getEmail",arrayList.get(position).getEmail());
                intent.putExtra("getId",arrayList.get(position).getId());
                intent.putExtra("getMobile",arrayList.get(position).getMobile());
                intent.putExtra("getQq",arrayList.get(position).getQq());
                intent.putExtra("getTel",arrayList.get(position).getTel());
                intent.putExtra("getTruename",arrayList.get(position).getTruename());
                intent.setClass(getActivity(), TelMessage.class);
                getActivity().startActivity(intent);
            }
        });
        return view;
    }

    private void getString(String str) {
        if (str==null){

        }else {
            JSONObject jsonObject = JSON.parseObject(str);
            JSONArray jsonArray = jsonObject.getJSONArray("colllist");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                cityNameMod=new CityNameMod();
                cityNameMod.setMobile(jsonObject1.getString("mobile"));
                cityNameMod.setEmail(jsonObject1.getString("email"));
                cityNameMod.setQq(jsonObject1.getString("qq"));
                cityNameMod.setTel(jsonObject1.getString("tel"));
                cityNameMod.setId(jsonObject1.getString("id"));
                cityNameMod.setTruename(jsonObject1.getString("truename"));
                cityNameMod.setAddress(jsonObject1.getString("address"));
                arrayList.add(cityNameMod);
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        arrayList.clear();
        collectstr = PreferencesUtils.getString(getActivity(), "collectphone");
        getString(collectstr);
        adapter.notifyDataSetChanged();
    }
}
