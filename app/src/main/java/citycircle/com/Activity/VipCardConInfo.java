package citycircle.com.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import citycircle.com.Adapter.MyWallAdapter;
import citycircle.com.JsonMordel.WallJsonMo;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.Loadmore;
import okhttp3.Call;
import util.XActivityindicator;
import util.XNetUtil;

import static citycircle.com.MyAppService.LocationApplication.APPDataCache;

/**
 * Created by admins on 2016/6/30.
 */
public class VipCardConInfo extends Fragment {
    View view;
    SwipeRefreshLayout swipeRefreshLayout;
    ListView walletlist;
    WallJsonMo wallJsonMo;
    List<WallJsonMo.DataBean.InfoBean> list = new ArrayList<>();
    String url, username,id;
    MyWallAdapter adapter;
    int page=1;
    Loadmore loadmore;

    public void setId(String id) {
        this.id = id;

        init();
    }

    private void init()
    {
        if(view == null || id == null)
        {
            return;
        }

        username = APPDataCache.User.getUsername();
        url = GlobalVariables.urlstr + "Hyk.getUserMoneys&username=" + username+"&page="+page+"&id="+id;

        intview();
        setWalletlist();
        getJson(0);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.mywallet, null);
        init();

        return view;

    }

    private void intview() {
        loadmore=new Loadmore();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.Refresh);
        walletlist = (ListView) view.findViewById(R.id.walletlist);
        loadmore.loadmore(walletlist);
        loadmore.setMyPopwindowswListener(new Loadmore.LoadmoreList() {
            @Override
            public void loadmore() {
                page++;
                url = GlobalVariables.urlstr + "Hyk.getUserMoneys&username=" + username+"&page="+page+"&id="+id;
                getJson(0);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page=1;
                url = GlobalVariables.urlstr + "Hyk.getUserMoneys&username=" + username+"&page="+page+"&id="+id;
                getJson(1);
            }
        });

    }

    private void getJson(final int type) {

        XNetUtil.APPPrintln("url: "+url);

        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                swipeRefreshLayout.setRefreshing(false);
                XActivityindicator.showToast(getResources().getString(R.string.intent_error));
            }

            @Override
            public void onResponse(String response) {
                swipeRefreshLayout.setRefreshing(false);
                wallJsonMo = JSON.parseObject(response, WallJsonMo.class);
                if (wallJsonMo.getData().getCode()==0){
                    if (type==1){
                        list.clear();
                    }
                    list.addAll(wallJsonMo.getData().getInfo());
                    adapter.notifyDataSetChanged();
                }else {
                    if (page!=1){
                        page--;
                    }
                    Toast.makeText(getActivity(),R.string.nomore,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void setWalletlist(){
        adapter=new MyWallAdapter(list,getActivity());
        walletlist.setAdapter(adapter);
    }
}
