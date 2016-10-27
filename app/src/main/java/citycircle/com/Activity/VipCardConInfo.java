package citycircle.com.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
import citycircle.com.Utils.PreferencesUtils;
import okhttp3.Call;

/**
 * Created by admins on 2016/6/30.
 */
public class VipCardConInfo extends Activity {
    SwipeRefreshLayout swipeRefreshLayout;
    ListView walletlist;
    ImageView back;
    WallJsonMo wallJsonMo;
    List<WallJsonMo.DataBean.InfoBean> list = new ArrayList<>();
    String url, username,id;
    MyWallAdapter adapter;
    int page=1;
    Loadmore loadmore;
    TextView titile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mywallet);
        username = PreferencesUtils.getString(VipCardConInfo.this, "username");
        id=getIntent().getStringExtra("id");
        url = GlobalVariables.urlstr + "Hyk.getUserMoneys&username=" + username+"&page="+page+"&id="+id;
        intview();
        setWalletlist();
        getJson(0);
    }
    private void intview() {
        loadmore=new Loadmore();
        titile=(TextView)findViewById(R.id.titile);
        titile.setText("消费明细");
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.Refresh);
        walletlist = (ListView) findViewById(R.id.walletlist);
        back = (ImageView) findViewById(R.id.back);
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
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getJson(final int type) {
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(VipCardConInfo.this, R.string.intent_error, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(VipCardConInfo.this,R.string.nomore,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void setWalletlist(){
        adapter=new MyWallAdapter(list,VipCardConInfo.this);
        walletlist.setAdapter(adapter);
    }
}
