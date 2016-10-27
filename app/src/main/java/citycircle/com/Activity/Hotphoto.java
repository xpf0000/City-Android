package citycircle.com.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;

import citycircle.com.Adapter.DataAdapter;
import citycircle.com.JsonMordel.HotPageMod;
import citycircle.com.MyViews.LoadingFooter;
import citycircle.com.MyViews.SpacesItemDecoration;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.RecyclerViewStateUtils;
import citycircle.com.headRecy.EndlessRecyclerOnScrollListener;
import citycircle.com.headRecy.ExStaggeredGridLayoutManager;
import citycircle.com.headRecy.HeaderAndFooterRecyclerViewAdapter;
import citycircle.com.headRecy.HeaderSpanSizeLookup;
import okhttp3.Call;

/**
 * Created by admins on 2015/11/23.
 */
public class Hotphoto extends Fragment {
    View view;
    private RecyclerView mRecyclerView;
    HotPageMod hotPageMod;
    ArrayList<HotPageMod.DataBean.InfoBean> arrayList = new ArrayList<>();
    String url;
    int page=1;
    SwipeRefreshLayout lehuirefresh;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;
    private DataAdapter mDataAdapter = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.hotphoto, container, false);
        url = GlobalVariables.urlstr + "Quan.getListHot&page=" + page;
        intview();
        getJson(0);
        return view;
    }

    private void intview() {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        lehuirefresh = (SwipeRefreshLayout) view.findViewById(R.id.Refresh);
        lehuirefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                url = GlobalVariables.urlstr + "Quan.getListHot&page=" + page;
                getJson(1);
            }
        });
        mDataAdapter = new DataAdapter(getActivity(),arrayList);
//        mDataAdapter.addAll(arrayList);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
        SpacesItemDecoration decoration=new SpacesItemDecoration(16);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        ExStaggeredGridLayoutManager manager = new ExStaggeredGridLayoutManager (2, StaggeredGridLayoutManager.VERTICAL);
        manager.setSpanSizeLookup(new HeaderSpanSizeLookup((HeaderAndFooterRecyclerViewAdapter) mRecyclerView.getAdapter(), manager.getSpanCount()));
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
    }

    private void getJson(final int type) {
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                lehuirefresh.setRefreshing(false);
                Toast.makeText(getActivity(), R.string.intent_error, Toast.LENGTH_SHORT).show();
                RecyclerViewStateUtils.setFooterViewState(getActivity(), mRecyclerView, 10, LoadingFooter.State.NetWorkError, null);
            }

            @Override
            public void onResponse(String response) {
                lehuirefresh.setRefreshing(false);
                RecyclerViewStateUtils.setFooterViewState(getActivity(), mRecyclerView, 10, LoadingFooter.State.TheEnd, null);
                hotPageMod = JSON.parseObject(response, HotPageMod.class);
                if (hotPageMod.getData().getCode() == 0) {
                    if (type == 1) {
                        arrayList.clear();
                    }
                    arrayList.addAll(hotPageMod.getData().getInfo());
//                    mDataAdapter.addAll(arrayList);
                    mDataAdapter.notifyDataSetChanged();
                    mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
                    RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.Normal);
                } else {
                    Toast.makeText(getActivity(), R.string.nomore, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {

        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);

            LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(mRecyclerView);
            if(state == LoadingFooter.State.Loading) {
                Log.d("@Cundong", "the state is Loading, just wait..");
                return;
            }
            RecyclerViewStateUtils.setFooterViewState(getActivity(), mRecyclerView, 10, LoadingFooter.State.Loading, null);
            page++;
            url = GlobalVariables.urlstr + "Quan.getListHot&page=" + page;
            getJson(0);
        }
    };
}
