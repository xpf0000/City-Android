package citycircle.com.card;

import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import citycircle.com.Adapter.YouhuiquanAdapter;
import citycircle.com.R;
import model.ChongzhiModel;
import model.YouhuiquanModel;
import util.BaseActivity;
import util.XAPPUtil;
import util.XNetUtil;
import util.XNotificationCenter;

import static citycircle.com.MyAppService.LocationApplication.APPDataCache;
import static citycircle.com.MyAppService.LocationApplication.APPService;

/**
 * Created by X on 2016/11/19.
 */

public class ChooseYouhuiquan extends BaseActivity {

    private PullToRefreshListView list;
    private YouhuiquanAdapter adapter;
    private List<YouhuiquanModel> dataArr = new ArrayList<>();

    private int page = 1;
    private boolean end = false;
    private String uid = "";

    private String shopid="";

    private ChongzhiModel model;

    @Override
    protected void setupUi() {
        setContentView(R.layout.myyouhuiquan);
        setPageTitle("优惠券");

        uid = getIntent().getStringExtra("id");
        model = (ChongzhiModel) getIntent().getSerializableExtra("ChongzhiModel");
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

        list.getRefreshableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                doChoose(position-1);

            }
        });

        getData();
    }

    private void doChoose(int p)
    {
        YouhuiquanModel m = dataArr.get(p);

        XNotificationCenter.getInstance().postNotice("ChoosedYouhuiquan",m);

        doPop();
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

        XNetUtil.Handle(APPService.jifenGetUYHQList(shopid,uid, page, 20), new XNetUtil.OnHttpResult<List<YouhuiquanModel>>() {
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

                List<YouhuiquanModel> arr = new ArrayList<YouhuiquanModel>();

                for(YouhuiquanModel m : models)
                {
                    try
                    {
                        Double sm = Double.parseDouble(m.getS_money());
                        Double cm = Double.parseDouble(model.getMoney());

                        long now = XAPPUtil.serverUnixSecond();

                        XNetUtil.APPPrintln("now: "+now);
                        XNetUtil.APPPrintln("S_time_unix: "+m.getS_time_unix());
                        XNetUtil.APPPrintln("E_time_unix: "+m.getE_time_unix());

                        if(sm <= cm && now >= m.getS_time_unix() && now <= m.getE_time_unix())
                        {
                            arr.add(m);
                        }

                    }
                    catch (Exception e)
                    {

                    }
                }

                adapter.dataArr = arr;
                adapter.notifyDataSetChanged();
                list.onRefreshComplete();

                if(arr.size() == 0)
                {
                    doShowToast("暂无可用优惠券");
                }
            }
        });

    }
}
