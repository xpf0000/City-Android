package citycircle.com.Property;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ScrollDirectionListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import citycircle.com.Adapter.GroupSearchAdapter;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;
import citycircle.com.Utils.PreferencesUtils;
import model.GroupModel;
import model.HouseModel;
import util.DensityUtil;
import util.XActivityindicator;
import util.XNetUtil;
import util.XNotificationCenter;

import static citycircle.com.MyAppService.LocationApplication.APPDataCache;
import static citycircle.com.MyAppService.LocationApplication.APPService;

/**
 * Created by 飞侠 on 2016/2/18.
 */
public class MyHouse extends Activity {

    ImageView back;
    String uid, username;
    HouseAdapter adapter;
    FloatingActionButton fab;

    PullToRefreshListView list;
    private List<HouseModel> dataArr = new ArrayList<>();
    private int page = 1;
    private boolean end = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myhouse);
        uid = PreferencesUtils.getString(MyHouse.this, "userid");
        username = PreferencesUtils.getString(MyHouse.this, "username");
        intview();
        getData();

        XNotificationCenter.getInstance().addObserver("ADDHouseSuccess", new XNotificationCenter.OnNoticeListener() {
            @Override
            public void OnNotice(Object obj) {
                page = 1;
                end = false;
                getData();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        XNotificationCenter.getInstance().removeObserver("ADDHouseSuccess");
    }

    private void intview() {
        back = (ImageView) findViewById(R.id.back);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        list = (PullToRefreshListView) findViewById(R.id.list);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fab.attachToListView(list.getRefreshableView(), new ScrollDirectionListener() {
            @Override
            public void onScrollDown() {

                fab.show();
            }

            @Override
            public void onScrollUp() {

                fab.hide();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MyHouse.this, AddHome.class);
                MyHouse.this.startActivity(intent);
            }
        });

        adapter = new HouseAdapter(MyHouse.this);

        list.setAdapter(adapter);

        list.setMode(PullToRefreshBase.Mode.BOTH);

        list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                XNetUtil.APPPrintln("onPullDownToRefresh ~~~~~~~~~");

                //new FinishRefresh().execute();
                page = 1;
                end = false;
                getData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

                XNetUtil.APPPrintln("onPullUpToRefresh ~~~~~~~~~");
                getData();

            }
        });

        adapter.onHouseAdapterClick = new HouseAdapter.OnHouseAdapterClick() {
            @Override
            public void onBtnClick(int type, int position) {

                if(type == 0) //设定默认房屋
                {
                    setDefault(position);
                }
                else
                {
                    doDel(position);
                }

            }
        };


    }

    private void setDefault(int postion)
    {
        if(postion < dataArr.size())
        {
            final HouseModel model = dataArr.get(postion);

            XNetUtil.Handle(APPService.userUpdateHouse(uid, username, model.getHouseid(), model.getFanghaoid()), null, "设定默认房屋失败", new XNetUtil.OnHttpResult<Boolean>() {
                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onSuccess(Boolean aBoolean) {

                    if(aBoolean)
                    {
                        APPDataCache.User.setHouseid(model.getHouseid());
                        APPDataCache.User.setFanghaoid(model.getFanghaoid());
                        APPDataCache.User.save();

                        adapter.notifyDataSetChanged();
                    }
                }
            });

        }
    }

    private void doDel(int postion)
    {
        if(postion < dataArr.size())
        {
            final HouseModel model = dataArr.get(postion);

            XNetUtil.Handle(APPService.userDelHouse(uid, username, model.getId()), "删除成功", "删除失败", new XNetUtil.OnHttpResult<Boolean>() {
                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onSuccess(Boolean aBoolean) {

                    if(aBoolean)
                    {
                        if(APPDataCache.User.getHouseid().equals(model.getHouseid()) && APPDataCache.User.getFanghaoid().equals(model.getFanghaoid()))
                        {
                            APPDataCache.User.setHouseid("");
                            APPDataCache.User.setFanghaoid("");
                            APPDataCache.User.save();
                        }

                        page = 1;
                        end = false;
                        getData();

                    }
                }
            });

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

                    list.onRefreshComplete();
                    Toast.makeText(MyHouse.this, "没有更多了",
                            Toast.LENGTH_SHORT).show();

                    super.onPostExecute(aVoid);
                }
            }.execute();

            return;
        }

        XNetUtil.Handle(APPService.userGetHouseList(uid,username, page, 20), new XNetUtil.OnHttpResult<List<HouseModel>>() {
            @Override
            public void onError(Throwable e) {

                XNetUtil.APPPrintln(e);
            }

            @Override
            public void onSuccess(List<HouseModel> models) {

                if(page == 1)
                {
                    dataArr.clear();
                }

                if(models.size() > 0)
                {
                    dataArr.addAll(models);

                    if(APPDataCache.User.getHouseid().equals(""))
                    {
                        setDefault(0);
                    }

                }
                else
                {
                    APPDataCache.User.setHouseid("");
                    APPDataCache.User.setFanghaoid("");
                    APPDataCache.User.save();
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
                adapter.dataArr = dataArr;
                adapter.notifyDataSetChanged();
                list.onRefreshComplete();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
