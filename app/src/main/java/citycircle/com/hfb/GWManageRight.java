package citycircle.com.hfb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import citycircle.com.Adapter.PaihangAdapter;
import citycircle.com.MyAppService.LocationApplication;
import citycircle.com.R;
import model.HFBModel;
import util.HttpResult;
import util.XAPPUtil;
import util.XHorizontalBaseFragment;
import util.XNetUtil;

import static citycircle.com.MyAppService.LocationApplication.APPDataCache;
import static citycircle.com.MyAppService.LocationApplication.APPService;


/**
 * Created by X on 16/9/3.
 */
public class GWManageRight extends XHorizontalBaseFragment
{
    private PullToRefreshListView list;
    private PaihangAdapter adapter;

    public List<Object> dataArr = new ArrayList<>();
    private int page = 1;
    private boolean end = false;

    private Context context;

    private TextView pm;
    private RoundedImageView img;
    private TextView name;
    private TextView num;

    private LinearLayout bg;

    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.mipmap.home_head)
            .showImageOnFail(R.mipmap.home_head)
            .build();

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
        View v = inflater.inflate(R.layout.caifupaihang, container, false);
        list = (PullToRefreshListView) v.findViewById(R.id.qiandaopaihang_list);

        bg = (LinearLayout) v.findViewById(R.id.qiandaopaihang_bg);

        img = (RoundedImageView) v
                .findViewById(R.id.qiandaopaihang_header);
        name = (TextView) v
                .findViewById(R.id.qiandaopaihang_name);
        num = (TextView) v
                .findViewById(R.id.qiandaopaihang_num);
        pm = (TextView) v
                .findViewById(R.id.qiandaopaihang_order);

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

        XNetUtil.HandleReturnAll(LocationApplication.APPService.jifenethfbpm(uid, page, 20), new XNetUtil.OnHttpResult<HttpResult<List<HFBModel>>>() {

            @Override
            public void onError(Throwable e) {
                XNetUtil.APPPrintln(e);
            }

            @Override
            public void onSuccess(HttpResult<List<HFBModel>> listHttpResult) {

                List<HFBModel> models = listHttpResult.getData().getInfo();

                if(page == 1)
                {
                    dataArr.clear();
                }

                if(models.size() > 0)
                {
                    if(page == 1)
                    {
                        if(models.size() > 3)
                        {
                            List<HFBModel> arr = new ArrayList<>();
                            arr.add(models.get(0));
                            arr.add(models.get(1));
                            arr.add(models.get(2));

                            dataArr.add(arr);

                            for(int i = 3;i<models.size();i++)
                            {
                                dataArr.add(models.get(i));
                            }

                        }
                        else
                        {
                            List<HFBModel> arr = new ArrayList<>();

                            for(int i = 0;i<models.size();i++)
                            {
                                arr.add(models.get(i));
                            }

                            dataArr.add(arr);
                        }
                    }
                    else
                    {
                        dataArr.addAll(models);
                    }
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

                HttpResult.Uinfo info = listHttpResult.getData().getUinfo();

                pm.setText(info.getPm());
                name.setText(info.getNick());
                ImageLoader.getInstance().displayImage(info.getHeadimage(),img,options);
                num.setText(info.getHfb() +"怀府币");

                adapter.dataArr = dataArr;
                adapter.notifyDataSetChanged();
                list.onRefreshComplete();

            }
        });


    }


}
