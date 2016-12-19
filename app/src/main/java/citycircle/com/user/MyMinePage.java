package citycircle.com.user;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.extras.recyclerview.PullToRefreshRecyclerView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersTouchListener;

import java.util.ArrayList;
import java.util.List;

import citycircle.com.Activity.Cityinfo;
import citycircle.com.Activity.MyInfo;
import citycircle.com.Activity.Newphoto;
import citycircle.com.Adapter.MyMineAdapter;
import citycircle.com.R;
import citycircle.com.hfb.GWManageRight;
import citycircle.com.hfb.GoodsCenter;
import citycircle.com.hfb.YGManageLeft;
import model.GoodsModel;
import model.HFBModel;
import model.QuanModel;
import model.UserModel;
import util.BaseActivity;
import util.XHorizontalMain;
import util.XHorizontalMenu;
import util.XInterface;
import util.XNetUtil;

import static citycircle.com.MyAppService.LocationApplication.APPDataCache;
import static citycircle.com.MyAppService.LocationApplication.APPService;

/**
 * Created by X on 2016/11/7.
 */

public class MyMinePage extends BaseActivity {

    private PullToRefreshRecyclerView recyclerView;
    private RecyclerView mRecyclerView;
    private MyMineAdapter adapter;
    private MyMineAdapter adapter1;
    private int page = 1;
    private boolean end = false;
    private String uid="";
    private String uname = "";
    private List<QuanModel> dataArr = new ArrayList<>();
    private LinearLayout btn;

    public void toEditInfo(View v) {

        Intent intent = new Intent();
        intent.setClass(mContext, MyInfo.class);
        startActivity(intent);

    }

    @Override
    protected void setupUi() {
        setContentView(R.layout.myminepage);
        setPageTitle("我的主页");
        uid = getIntent().getStringExtra("uid");
        uname = getIntent().getStringExtra("uname");

        btn = (LinearLayout) findViewById(R.id.myminepage_right_btn);
        recyclerView = (PullToRefreshRecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView = recyclerView.getRefreshableView();

        recyclerView.setMode(PullToRefreshBase.Mode.BOTH);
        recyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                page = 1;
                end = false;
                getData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                getData();
            }
        });



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        adapter = new MyMineAdapter(mContext);
        adapter.flag = 0;

        adapter1 = new MyMineAdapter(mContext);
        adapter1.flag = 1;

        if(uid.equals(APPDataCache.User.getUid()))
        {
            adapter.setUser(APPDataCache.User);
            adapter1.setUser(APPDataCache.User);
        }
        else
        {
            btn.setVisibility(View.GONE);
        }

        adapter.setOnItemClickLitener(new XInterface()
        {
            @Override
            public void onItemClick(View view, int position)
            {

                XNetUtil.APPPrintln("position: "+position);
                Intent intent = new Intent();
                intent.putExtra("id", dataArr.get(position).getId());
                intent.setClass(mContext, Cityinfo.class);
                startActivity(intent);

            }
        });

        mRecyclerView.setAdapter(adapter);


        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(adapter); //绑定之前的adapter
        final StickyRecyclerHeadersDecoration headersDecor1 = new StickyRecyclerHeadersDecoration(adapter1);

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headersDecor.invalidateHeaders();
            }
        });  //刷新数据的时候回刷新头部

        adapter1.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headersDecor1.invalidateHeaders();
            }
        });  //刷新数据的时候回刷新头部

        mRecyclerView.addItemDecoration(headersDecor);


        MineHeaderTouchListener touchListener =
                new MineHeaderTouchListener(mRecyclerView, headersDecor);
        touchListener.setOnHeaderClickListener(
                new MineHeaderTouchListener.OnHeaderClickListener() {
                    @Override
                    public void onHeaderClick(int flag) {

                        mRecyclerView.removeItemDecoration(headersDecor);
                        mRecyclerView.removeItemDecoration(headersDecor1);
                        if(flag == 0)
                        {
                            mRecyclerView.addItemDecoration(headersDecor);
                            mRecyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        }
                        else
                        {
                            mRecyclerView.addItemDecoration(headersDecor1);
                            mRecyclerView.setAdapter(adapter1);
                            adapter1.notifyDataSetChanged();
                        }

                        XNetUtil.APPPrintln("flag: "+flag);

                    }
                });
        mRecyclerView.addOnItemTouchListener(touchListener);


        MineHeaderTouchListener touchListener1 =
                new MineHeaderTouchListener(mRecyclerView, headersDecor1);
        touchListener1.setOnHeaderClickListener(
                new MineHeaderTouchListener.OnHeaderClickListener() {
                    @Override
                    public void onHeaderClick(int flag) {

                        mRecyclerView.removeItemDecoration(headersDecor);
                        mRecyclerView.removeItemDecoration(headersDecor1);
                        if(flag == 0)
                        {
                            mRecyclerView.addItemDecoration(headersDecor);
                            mRecyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        }
                        else
                        {
                            mRecyclerView.addItemDecoration(headersDecor1);
                            mRecyclerView.setAdapter(adapter1);
                            adapter1.notifyDataSetChanged();
                        }
                        XNetUtil.APPPrintln("flag: "+flag);

                    }
                });
        mRecyclerView.addOnItemTouchListener(touchListener1);

        getData();
        if(!uid.equals(APPDataCache.User.getUid()))
        {
            getUser();
        }

    }

    private void getData() {


        XNetUtil.APPPrintln("do getData !!!!!!!!!!");

        if(end)
        {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {

                    recyclerView.onRefreshComplete();
                    Toast.makeText(mContext, "没有更多了",
                            Toast.LENGTH_SHORT).show();

                    super.onPostExecute(aVoid);
                }
            }.execute();

            return;
        }

        XNetUtil.Handle(APPService.quanGetMyList(uid, page, 20), new XNetUtil.OnHttpResult<List<QuanModel>>() {
            @Override
            public void onError(Throwable e) {

                XNetUtil.APPPrintln(e);
            }

            @Override
            public void onSuccess(List<QuanModel> models) {

                if(page == 1)
                {
                    dataArr.clear();
                }

                if(models.size() > 0)
                {
                    dataArr.addAll(models);
                }

                if(models.size() == 20)
                {
                    end = false;
                    page += 1;
                }
                else
                {
                    end = true;
                }

                adapter.setDataArr(dataArr);

                adapter.notifyDataSetChanged();
                recyclerView.onRefreshComplete();
            }
        });

    }

    private void getUser()
    {

        XNetUtil.APPPrintln("uname: "+uname);

        XNetUtil.Handle(APPService.userGetUser(uname), new XNetUtil.OnHttpResult<List<UserModel>>() {
            @Override
            public void onError(Throwable e) {

                XNetUtil.APPPrintln(e);
            }

            @Override
            public void onSuccess(List<UserModel> models) {

                if(models.size() > 0)
                {
                    adapter.setUser(models.get(0));
                    adapter1.setUser(models.get(0));
                    adapter.notifyDataSetChanged();
                    adapter1.notifyDataSetChanged();
                }

            }
        });
    }

    @Override
    protected void setupData() {

    }



}
