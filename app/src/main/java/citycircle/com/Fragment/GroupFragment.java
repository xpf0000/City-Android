package citycircle.com.Fragment;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import citycircle.com.Adapter.GroupVCAdapter;
import citycircle.com.R;
import model.GroupModel;
import util.XNetUtil;
import util.XNotificationCenter;

import static citycircle.com.MyAppService.LocationApplication.APPService;

/**
 * Created by X on 2016/11/13.
 */

public class GroupFragment extends Fragment implements View.OnClickListener{

    private PullToRefreshListView list;
    private Context context;
    private int page = 1;
    private boolean end = false;
    private GroupVCAdapter adapter;
    private List<Object> dataArr = new ArrayList<>();
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.groupvc, container, false);

        dataArr.add(1);
        dataArr.add(1);
        dataArr.add(1);


        list = (PullToRefreshListView) view.findViewById(R.id.groupvc_list);

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

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //showAlert(position-1);
            }
        });

        adapter = new GroupVCAdapter(getActivity());

        list.setAdapter(adapter);

        getData();

        return view;

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

        XNetUtil.Handle(APPService.hykGetShopTJList(page, 20), new XNetUtil.OnHttpResult<List<GroupModel>>() {
            @Override
            public void onError(Throwable e) {
                list.onRefreshComplete();
                XNetUtil.APPPrintln(e);
            }

            @Override
            public void onSuccess(List<GroupModel> arrs) {

                if(page == 1)
                {
                    dataArr.clear();
                    dataArr.add(1);
                    dataArr.add(1);
                    dataArr.add(1);
                }

                if(arrs.size() > 0)
                {
                    dataArr.addAll(arrs);
                }

                if(arrs.size() == 20)
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
    public void onClick(View view) {

    }
}
