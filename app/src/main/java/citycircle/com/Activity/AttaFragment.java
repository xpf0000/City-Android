package citycircle.com.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import citycircle.com.Adapter.Camadapter;
import citycircle.com.Adapter.NetworkImageHolderView;
import citycircle.com.MyAppService.CityServices;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.Loadmore;
import citycircle.com.Utils.PreferencesUtils;
import okhttp3.Call;

/**
 * Created by admins on 2016/5/31.
 */
public class AttaFragment extends Fragment {
    View view, headview;
    LinearLayout nostr;
    Button logo;
    SwipeRefreshLayout Refresh;
    ListView mylist;
    int page = 1, a;
    String url, username, bannerurl;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> hashMap;
    Camadapter camadapter;
    Loadmore loadmore;
    ConvenientBanner fristbannerbanner;
    private List<String> networkImages;
    private ArrayList<HashMap<String, String>> newsid;
    HashMap<String, Object> hashMaps;
    String addview;
    private List<String> networkurl;

    public static AttaFragment instance() {
        AttaFragment view = new AttaFragment();
        return view;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.attlayout, null);
        a = PreferencesUtils.getInt(getActivity(), "land");
        Intent intent = new Intent(getActivity(), CityServices.class);
        getActivity().startService(intent);
        IntentFilter filter = new IntentFilter(CityServices.action);
        getActivity().registerReceiver(broadcastReceiver, filter);
        intview();
        bannerurl = GlobalVariables.urlstr + "News.getGuanggao&typeid=103";
        if (a == 0) {
            nostr.setVisibility(View.VISIBLE);
        } else {
            username = PreferencesUtils.getString(getActivity(), "username");
            url = GlobalVariables.urlstr + "News.getListGZ&username=" + username + "&page=" + page + "&perNumber=20";
            setCamadapter();
            getJson(0);
        }
        getbannerjson();
        return view;
    }

    private void intview() {
        getnewsid();
        loadmore = new Loadmore();
        headview = LayoutInflater.from(getActivity()).inflate(R.layout.headbanner, null);
        fristbannerbanner = (ConvenientBanner) headview.findViewById(R.id.fristbannerbanner);
        Refresh = (SwipeRefreshLayout) view.findViewById(R.id.Refresh);
        nostr = (LinearLayout) view.findViewById(R.id.nostr);
        logo = (Button) view.findViewById(R.id.logo);
        mylist = (ListView) view.findViewById(R.id.mylist);
        mylist.addHeaderView(headview);
        nostr.setVisibility(View.GONE);
        loadmore.loadmore(mylist);
        fristbannerbanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (networkurl.get(position).length() != 0) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), WebViews.class);
                    intent.putExtra("url", networkurl.get(position));
                    getActivity().startActivity(intent);
                }
            }
        });
        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (setidlist(arrayList.get(position - mylist.getHeaderViewsCount()).get("id"))) {
                    addview = GlobalVariables.urlstr + "News.addView&id=" + arrayList.get(position - mylist.getHeaderViewsCount()).get("id");
                    hashMap = new HashMap<String, String>();
                    hashMap.put("id", arrayList.get(position - mylist.getHeaderViewsCount()).get("id"));
                    newsid.add(hashMap);
                    hashMaps = new HashMap<String, Object>();
                    hashMaps.put("idlist", newsid);
                    String string = JSON.toJSONString(hashMaps);
                    PreferencesUtils.putString(getActivity(), "idstr", string);
                    camadapter.notifyDataSetChanged();
                    addview();
                }
                Intent intent = new Intent();
                intent.putExtra("id", arrayList.get(position - mylist.getHeaderViewsCount()).get("id"));
                intent.putExtra("type", 1);
                intent.setClass(getActivity(), DiscountInfo.class);
                getActivity().startActivity(intent);
            }
        });
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), Logn.class);
                getActivity().startActivity(intent);
            }
        });
        loadmore.setMyPopwindowswListener(new Loadmore.LoadmoreList() {
            @Override
            public void loadmore() {
                page++;
                url = GlobalVariables.urlstr + "News.getListGZ&username=" + username + "&page=" + page + "&perNumber=20";
                getJson(0);
            }
        });
        Refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                url = GlobalVariables.urlstr + "News.getListGZ&username=" + username + "&page=" + page + "&perNumber=20";
                getJson(1);
                getbannerjson();
            }
        });
    }

    private void getJson(final int type) {
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Refresh.setRefreshing(false);
                Toast.makeText(getActivity(), R.string.intent_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                Refresh.setRefreshing(false);
                if (type == 1) {
                    arrayList.clear();
                }
                setarray(response);
                camadapter.notifyDataSetChanged();
            }
        });
    }

    private void setarray(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
        if (jsonObject1.getIntValue("code") == 0) {
            JSONArray jsonArray = jsonObject1.getJSONArray("info");
            for (int i = 0; i < jsonArray.size(); i++) {
                hashMap = new HashMap<>();
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                hashMap.put("id", jsonObject2.getString("id") == null ? "" : jsonObject2.getString("id"));
                hashMap.put("title", jsonObject2.getString("title") == null ? "" : jsonObject2.getString("title"));
                hashMap.put("description", jsonObject2.getString("description") == null ? "" : jsonObject2.getString("description"));
                hashMap.put("url", jsonObject2.getString("url") == null ? "" : jsonObject2.getString("url"));
                hashMap.put("create_time", jsonObject2.getString("create_time") == null ? "" : jsonObject2.getString("create_time"));
                hashMap.put("s_time", jsonObject2.getString("s_time") == null ? "" : jsonObject2.getString("s_time"));
                hashMap.put("e_time", jsonObject2.getString("e_time") == null ? "" : jsonObject2.getString("e_time"));
                arrayList.add(hashMap);
            }

        } else {
            if (page != 1) {
                page--;
            }
            if (arrayList.size()!=0){
                Toast.makeText(getActivity(), R.string.nomore, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getbannerjson() {
        OkHttpUtils.get().url(bannerurl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                headview.setVisibility(View.GONE);
            }

            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = JSON.parseObject(response);
                JSONObject jsonObject1 = jsonObject.getJSONObject("data");

                if (jsonObject1.getIntValue("code") == 0) {
                    networkImages = new ArrayList<String>();
                    networkurl = new ArrayList<String>();
                    JSONArray jsonArray = jsonObject1.getJSONArray("info");
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                        networkImages.add(jsonObject2.getString("picurl"));
                        networkurl.add(jsonObject2.getString("url"));
                    }
                    fristbannerbanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
                        @Override
                        public NetworkImageHolderView createHolder() {
                            return new NetworkImageHolderView();
                        }
                    }, networkImages);
                    headview.setVisibility(View.VISIBLE);
                } else {
                    headview.setVisibility(View.GONE);
                }
            }
        });
    }

    private void addview() {
        OkHttpUtils.get().url(addview).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {

            }
        });
    }

    private void setCamadapter() {
        camadapter = new Camadapter(arrayList, getActivity(), newsid);
        mylist.setAdapter(camadapter);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            a = PreferencesUtils.getInt(getActivity(), "land");
            if (a == 0) {
                mylist.setVisibility(View.GONE);
                nostr.setVisibility(View.VISIBLE);
            } else {
                mylist.setVisibility(View.VISIBLE);
                nostr.setVisibility(View.GONE);
                arrayList.clear();
                ;
                username = PreferencesUtils.getString(getActivity(), "username");
                url = GlobalVariables.urlstr + "News.getListGZ&username=" + username + "&page=" + page + "&perNumber=20";
                setCamadapter();
                getJson(1);

            }

        }
    };

    private void getnewsid() {
        newsid = new ArrayList<>();
        String idstr = PreferencesUtils.getString(getActivity(), "idstr");
        if (idstr != null) {
            JSONObject jsonObject = JSON.parseObject(idstr);
            JSONArray jsonArray = jsonObject.getJSONArray("idlist");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                hashMap = new HashMap<>();
                hashMap.put("id", jsonObject1.getString("id"));
                newsid.add(hashMap);
            }
        }
    }

    private boolean setidlist(String id) {
        boolean a = true;
        for (int i = 0; i < newsid.size(); i++) {
            if (newsid.get(i).get("id").equals(id)) {
                a = false;
                return a;
            }
        }
        return a;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
    }
}
