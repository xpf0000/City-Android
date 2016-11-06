package citycircle.com.hfb;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import citycircle.com.Adapter.PaihangAdapter;
import citycircle.com.MyAppService.LocationApplication;
import citycircle.com.R;
import model.HFBModel;
import util.XHorizontalBaseFragment;
import util.XNetUtil;

import static citycircle.com.MyAppService.LocationApplication.APPDataCache;

/**
 * Created by X on 16/9/2.
 */
public class YGManageLeft extends XHorizontalBaseFragment
{
    private PullToRefreshListView list;
    private PaihangAdapter adapter;

    public List<HFBModel> dataArr = new ArrayList<>();
    private int page = 1;
    private boolean end = false;

    private Context context;

    public int selectIndex = 0;

    public void refresh()
    {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void lazyLoad() {

        System.out.println("RightFragment--->lazyLoad !!!");

        // 获取MainListAdapter对象
        adapter = new PaihangAdapter(getActivity());
        // 将MainListAdapter对象传递给ListView视图

        if(list != null)
        {
            list.setAdapter(adapter);
        }

        getData();

    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        System.out.println("RightFragment--->onCreate");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        System.out.println("RightFragment--->onCreateView");
        View v = inflater.inflate(R.layout.qiandaopaihang, container, false);
        list = (PullToRefreshListView) v.findViewById(R.id.qiandaopaihang_list);

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


        return v;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        System.out.println("RightFragment--->onResume");
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
                    Toast.makeText(getActivity(), "没有更多了",
                            Toast.LENGTH_SHORT).show();

                    super.onPostExecute(aVoid);
                }
            }.execute();

            return;
        }

        String uid = APPDataCache.User.getUid();

        XNetUtil.Handle(LocationApplication.APPService.JifenGetQDPM(uid, page, 20), new XNetUtil.OnHttpResult<List<HFBModel>>() {
            @Override
            public void onError(Throwable e) {

                XNetUtil.APPPrintln(e);
            }

            @Override
            public void onSuccess(List<HFBModel> models) {

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
