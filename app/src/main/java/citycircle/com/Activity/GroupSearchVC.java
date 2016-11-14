package citycircle.com.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import citycircle.com.Adapter.GroupSearchAdapter;
import citycircle.com.R;
import citycircle.com.hfb.DHRecord;
import model.GroupModel;
import model.HFBModel;
import util.BaseActivity;
import util.XNetUtil;

import static citycircle.com.MyAppService.LocationApplication.APPDataCache;
import static citycircle.com.MyAppService.LocationApplication.APPService;

/**
 * Created by X on 2016/11/14.
 */

public class GroupSearchVC extends BaseActivity {

    private PullToRefreshListView list;
    private GroupSearchAdapter adapter;
    private List<GroupModel> dataArr = new ArrayList<>();

    private int page = 1;
    private boolean end = false;

    private EditText search;
    private TextView btn;

    @Override
    protected void setupUi() {
        setContentView(R.layout.groupsearch);
        setPageTitle("商家搜索");

        search = (EditText) findViewById(R.id.search_txt);
        btn = (TextView) findViewById(R.id.search_btn);
        list = (PullToRefreshListView) findViewById(R.id.groupseach_list);

        adapter = new GroupSearchAdapter(mContext);

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
                toinfo(position);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm != null) {
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),
                            0);
                }
                page = 1;
                end = false;
                    getData();
            }
        });

        String key = getIntent().getStringExtra("key");
        search.setText(key);
        getData();

    }

    @Override
    protected void setupData() {

    }

    private void toinfo(int p)
    {

        GroupModel m = dataArr.get(p-1);

        Intent intent = new Intent();
        intent.putExtra("id", m.getId());
        intent.putExtra("shopname", m.getName());
        intent.setClass(this, ShopInfo.class);
        startActivity(intent);

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

        String key = search.getText().toString();

        XNetUtil.Handle(APPService.hykGetShopSearch(key, page, 20), new XNetUtil.OnHttpResult<List<GroupModel>>() {
            @Override
            public void onError(Throwable e) {

                XNetUtil.APPPrintln(e);
            }

            @Override
            public void onSuccess(List<GroupModel> models) {

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
