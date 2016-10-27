package citycircle.com.Property;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.Activity.Cityinfo;
import citycircle.com.Adapter.MyRecyclerAdapter;
import citycircle.com.MyViews.SpacesItemDecoration;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;
import citycircle.com.Utils.PreferencesUtils;

/**
 * Created by admins on 2016/4/12.
 */
public class ProHot  extends Fragment {
    View view;
    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter adapter;
    HashMap<String, String> hashMap;
    String url, urlstr;
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
    int page=1;
    SwipeRefreshLayout lehuirefresh;
    String houseid;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.hotphoto, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        houseid= PreferencesUtils.getString(getActivity(), "houseid");
        url= GlobalVariables.urlstr+"Quan.getListHot&page="+page+"&xiaoquid="+houseid;
        SpacesItemDecoration decoration=new SpacesItemDecoration(16);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        lehuirefresh = (SwipeRefreshLayout) view.findViewById(R.id.Refresh);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));//设置RecyclerView布局管理器为2列垂直排布
        adapter = new MyRecyclerAdapter(getActivity(),array);
        mRecyclerView.setAdapter(adapter);
        getnews(0);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                StaggeredGridLayoutManager lm = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
                int count = recyclerView.getAdapter().getItemCount();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取最后一个完全显示的ItemPosition
                    int[] lastItems = new int[2];
                    lm.findLastCompletelyVisibleItemPositions(lastItems);
                    int lastItem = Math.max(lastItems[0], lastItems[1]);
                    if (lastItem == count - 1) {
                        page++;
                        url = GlobalVariables.urlstr + "Quan.getListHot&page=" + page+"&xiaoquid="+houseid;
                        getnews(0);
                    }
                }
            }
        });
        lehuirefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page=1;
//                array.clear();
                url = GlobalVariables.urlstr + "Quan.getListHot&page=" + page+"&xiaoquid="+houseid;
                getnews(1);
            }
        });
        adapter.setOnClickListener(new MyRecyclerAdapter.OnItemClickListener() {
            @Override
            public void ItemClickListener(View view, int postion) {
//              Toast.makeText(getActivity(), , Toast.LENGTH_SHORT).show();
                Intent intent=new Intent();
                intent.putExtra("id",array.get(postion).get("id"));
                intent.setClass(getActivity(),Cityinfo.class);
                getActivity().startActivity(intent);
            }

            @Override
            public void ItemLongClickListener(View view, int postion) {

            }
        });
        return view;
    }
    public void getnews(final int type) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                HttpRequest httpRequest = new HttpRequest();
                urlstr = httpRequest.doGet(url);
                if (urlstr.equals("网络超时")) {
                    handler.sendEmptyMessage(2);
                } else {
                   if (type==1){
                       handler.sendEmptyMessage(4);
                   }else {
                       handler.sendEmptyMessage(1);
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
                    lehuirefresh.setRefreshing(false);

                    getArray(urlstr);
                    adapter.notifyDataSetChanged();
                    break;
                case 2:
                    lehuirefresh.setRefreshing(false);
                    Toast.makeText(getActivity(), "网络似乎有问题了", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    lehuirefresh.setRefreshing(false);
                    Toast.makeText(getActivity(), "暂无更多内容", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    lehuirefresh.setRefreshing(false);
                    array.clear();
                    getArray(urlstr);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    public void getArray(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
        int a = jsonObject1.getIntValue("code");
        if (a == 0) {
            JSONArray jsonArray = jsonObject1.getJSONArray("info");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                hashMap = new HashMap<>();
                hashMap.put("content", jsonObject2.getString("content") == null ? "" : jsonObject2.getString("content"));
                hashMap.put("id", jsonObject2.getString("id") == null ? "" : jsonObject2.getString("id"));
                hashMap.put("nickname", jsonObject2.getString("nickname") == null ? "" : jsonObject2.getString("nickname"));
                hashMap.put("create_time", jsonObject2.getString("create_time") == null ? "" : jsonObject2.getString("create_time"));
                hashMap.put("url", jsonObject2.getString("url") == null ? "" : jsonObject2.getString("url"));
                hashMap.put("width", jsonObject2.getString("width") == null ? "" : jsonObject2.getString("width"));
                hashMap.put("height", jsonObject2.getString("height") == null ? "" : jsonObject2.getString("height"));
                array.add(hashMap);
            }
        } else {
            if (page == 1) {

            } else {
                page--;
            }
            handler.sendEmptyMessage(3);
        }

    }
}
