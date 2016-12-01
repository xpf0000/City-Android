package citycircle.com.hfb;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import citycircle.com.R;
import model.HFBModel;
import util.BaseActivity;
import util.XHtmlVC;
import util.XNetUtil;

import static citycircle.com.MyAppService.LocationApplication.APPDataCache;
import static citycircle.com.MyAppService.LocationApplication.APPService;

/**
 * Created by X on 2016/11/6.
 */

public class DHRecord extends BaseActivity {

    private PullToRefreshListView list;
    private DHRecordAdapter adapter;
    private List<HFBModel> dataArr = new ArrayList<>();

    private int page = 1;
    private boolean end = false;
    private String uid = "";
    private String uname = "";

    @Override
    protected void setupUi() {
        setContentView(R.layout.dhrecard);
        setPageTitle("兑换记录");

        uid = APPDataCache.User.getUid();
        uname = APPDataCache.User.getUsername();

        list = (PullToRefreshListView) findViewById(R.id.dhrecard_list);

        adapter = new DHRecordAdapter();

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
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                toinfo(i);
            }
        });

        getData();
    }

    private void toinfo(int postion)
    {
        HFBModel model = dataArr.get(postion-1);
        Bundle bundle = new Bundle();
        bundle.putString("url","file:///android_asset/duihuansuccess.html?id="+model.getId());
        bundle.putString("title","兑换详情");
        pushVC(XHtmlVC.class,bundle);
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

        XNetUtil.Handle(APPService.jifenGetDHList(uid,uname, page, 20), new XNetUtil.OnHttpResult<List<HFBModel>>() {
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

                adapter.notifyDataSetChanged();
                list.onRefreshComplete();
            }
        });

    }


    /**
     * 定义ListView适配器MainListViewAdapter
     */
    class DHRecordAdapter extends BaseAdapter {

        /**
         * 返回item的个数
         */
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return dataArr.size();
        }

        /**
         * 返回item的内容
         */
        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return dataArr.get(position);
        }

        /**
         * 返回item的id
         */
        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        /**
         * 返回item的视图
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            DHRecord.ListItemView listItemView;

            // 初始化item view
            if (convertView == null) {
                // 通过LayoutInflater将xml中定义的视图实例化到一个View中
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.dhrecord_cell, null);

                // 实例化一个封装类ListItemView，并实例化它的两个域
                listItemView = new DHRecord.ListItemView();
                listItemView.name = (TextView) convertView
                        .findViewById(R.id.dhrecard_cell_name);
                listItemView.time = (TextView) convertView
                        .findViewById(R.id.dhrecard_cell_time);
                listItemView.yu = (TextView) convertView
                        .findViewById(R.id.dhrecard_cell_yu);
                listItemView.num = (TextView) convertView
                        .findViewById(R.id.dhrecard_cell_num);


                // 将ListItemView对象传递给convertView
                convertView.setTag(listItemView);
            } else {
                // 从converView中获取ListItemView对象
                listItemView = (DHRecord.ListItemView) convertView.getTag();
            }


            // 获取到mList中指定索引位置的资源
            String name = dataArr.get(position).getName();
            String time = dataArr.get(position).getCreate_time();
            String yu = "余额:"+dataArr.get(position).getHfbsy()+"个";
            String num = "-"+dataArr.get(position).getHfb();

            listItemView.name.setText(name);
            listItemView.time.setText(time);
            listItemView.yu.setText(yu);
            listItemView.num.setText(num);

            // 返回convertView对象
            return convertView;
        }

    }

    /**
     * 封装两个视图组件的类
     */
    class ListItemView {
        TextView name;
        TextView time;
        TextView yu;
        TextView num;
    }
}
