package citycircle.com.user;

import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import citycircle.com.Adapter.YouhuiquanAdapter;
import citycircle.com.R;
import citycircle.com.hfb.JifenDetail;
import model.HFBModel;
import model.YouhuiquanModel;
import util.BaseActivity;
import util.XNetUtil;

import static citycircle.com.MyAppService.LocationApplication.APPDataCache;
import static citycircle.com.MyAppService.LocationApplication.APPService;

/**
 * Created by X on 2016/11/7.
 */

public class MyYouhuiquan extends BaseActivity {

    private PullToRefreshListView list;
    private YouhuiquanAdapter adapter;
    private List<YouhuiquanModel> dataArr = new ArrayList<>();

    private int page = 1;
    private boolean end = false;
    private String uid = "";

    @Override
    protected void setupUi() {
        setContentView(R.layout.myyouhuiquan);
        setPageTitle("我的优惠券");

        uid = APPDataCache.User.getUid();

        list = (PullToRefreshListView) findViewById(R.id.myyouhuiquan_list);

        adapter = new YouhuiquanAdapter(mContext);
        adapter.type = 1;

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

        getData();
    }

    @Override
    protected void setupData() {

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
                    Toast.makeText(mContext, "没有更多了",
                            Toast.LENGTH_SHORT).show();

                    super.onPostExecute(aVoid);
                }
            }.execute();

            return;
        }

        XNetUtil.Handle(APPService.JifenGetUYHQList(uid, page, 20), new XNetUtil.OnHttpResult<List<YouhuiquanModel>>() {
            @Override
            public void onError(Throwable e) {

                XNetUtil.APPPrintln(e);
            }

            @Override
            public void onSuccess(List<YouhuiquanModel> models) {

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
                adapter.dataArr = dataArr;
                adapter.notifyDataSetChanged();
                list.onRefreshComplete();
            }
        });

    }
}
