package citycircle.com.hfb;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bigkoo.svprogresshud.listener.OnDismissListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import citycircle.com.Activity.MyInfo;
import citycircle.com.MyAppService.LocationApplication;
import citycircle.com.R;
import model.GoodsModel;
import model.UserModel;
import util.BaseActivity;
import util.XAPPUtil;
import util.XActivityindicator;
import util.XGridView;
import util.XHtmlVC;
import util.XNetUtil;

import static citycircle.com.MyAppService.LocationApplication.APPDataCache;
import static citycircle.com.MyAppService.LocationApplication.APPService;
import static citycircle.com.MyAppService.LocationApplication.SW;

/**
 * Created by X on 2016/11/1.
 */

public class HfbCenter extends BaseActivity {

    private ScrollView scroll;
    private XGridView gview;
    private HFBGoodsAdapter adapter;
    private List<GoodsModel> dataArr = new ArrayList<>();

    private TextView tatle;
    private TextView donetxt;
    private ImageView doneicon;

    private String uid = "";
    private String uname = "";

    @Override
    protected void setupData() {

    }

    @Override
    protected void setupUi() {
        setContentView(R.layout.hfbcenter);
        setPageTitle("怀府币中心");

        uid = APPDataCache.User.getUid();
        uname = APPDataCache.User.getUsername();

        tatle = (TextView) findViewById(R.id.hfbcenter_tatle);
        donetxt = (TextView) findViewById(R.id.hfbcenter_donetxt);
        doneicon = (ImageView) findViewById(R.id.hfbcenter_doneicon);
        scroll = (ScrollView) findViewById(R.id.hfbcenter_scroll);
        gview = (XGridView) findViewById(R.id.hfbcenter_grid);
        gview.setScrollEnable(false);

        gview.setColumnWidth(LocationApplication.SW/2);

        gview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                GoodsModel m = dataArr.get(i);
                doDH(view,m);
            }
        });

        adapter = new HFBGoodsAdapter();
        gview.setAdapter(adapter);
        getGoods();

    }


    public void setDataArr(List<GoodsModel> dataArr) {
        this.dataArr = dataArr;
        adapter.notifyDataSetChanged();
    }

    private void getGoods() {

        XNetUtil.Handle(APPService.jifenGetproductList(1,4), new XNetUtil.OnHttpResult<List<GoodsModel>>() {
            @Override
            public void onError(Throwable e) {

                XNetUtil.APPPrintln(e);

            }

            @Override
            public void onSuccess(List<GoodsModel> arrs) {
                setDataArr(arrs);
            }
        });

    }

    public void doQD(final View v) {

        if(APPDataCache.User.getOrqd() == 1)
        {
            Bundle bundle = new Bundle();
            bundle.putString("url","file:///android_asset/index.html?uid="+uid+"&uname="+uname);
            bundle.putString("title","每日签到");
            pushVC(XHtmlVC.class,bundle);
            return;
        }

        XActivityindicator.create(this).show();
        v.setEnabled(false);
        XNetUtil.Handle(APPService.jifenAddQiandao(uid,uname), "签到成功,获得1怀府币", "签到失败", new XNetUtil.OnHttpResult<Boolean>() {
            @Override
            public void onError(Throwable e) {
                XNetUtil.APPPrintln(e);
                v.setEnabled(true);
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                v.setEnabled(!aBoolean);
                APPDataCache.User.setOrqd(1);
                if(aBoolean)
                {
                    doneicon.setVisibility(View.VISIBLE);
                    donetxt.setText("已完成");
                    int c = Color.parseColor("#21adfd");
                    donetxt.setTextColor(c);
                }
            }
        });

    }

    private void getHFB()
    {
        XNetUtil.Handle(APPService.jifenGetUinfo(uid,uname), new XNetUtil.OnHttpResult<List<UserModel>>() {
            @Override
            public void onError(Throwable e) {

                XNetUtil.APPPrintln("!!!!!! jifenGetUinfo error: "+e);

            }

            @Override
            public void onSuccess(List<UserModel> arrs) {

                XNetUtil.APPPrintln(arrs.toString());

                if(arrs.size() > 0)
                {
                    UserModel u = arrs.get(0);

                    XNetUtil.APPPrintln(u.toString());

                    tatle.setText(u.getHfb()+"个");
                    APPDataCache.User.setOrqd(u.getOrqd());
                    if(u.getOrqd() != 1)
                    {
                        doneicon.setVisibility(View.INVISIBLE);
                        donetxt.setText("还未签到");
                        int c = Color.parseColor("#333333");
                        donetxt.setTextColor(c);
                    }


                }

            }
        });
    }

    public void doDH(final View v,final GoodsModel model) {

        String id = model.getId();
        Bundle bundle = new Bundle();
        bundle.putString("url","file:///android_asset/duihuaninfo.html?id="+id+"&uid="+uid+"&uname="+uname);
        bundle.putString("title","兑换详情");
        pushVC(XHtmlVC.class,bundle);

    }

    // 开始自动翻页
    @Override
    protected void onResume() {
        super.onResume();
        getHFB();
    }

    // 停止自动翻页
    @Override
    protected void onPause() {
        super.onPause();
        //停止翻页

    }

    public void leftClick(View v) {

        pushVC(GoodsCenter.class);

    }

    public void rightClick(View v) {

        pushVC(YGManageMainVC.class);

    }

    public void toDetail(View v) {
        pushVC(JifenDetail.class);

    }

    public void toGuize(View v) {

        Bundle bundle = new Bundle();
        bundle.putString("url","file:///android_asset/hfbguize.html?id=6892");
        bundle.putString("title","怀府币规则");
        pushVC(XHtmlVC.class,bundle);

    }

    public void toEditInfo(View v) {

        Intent intent = new Intent();
        intent.setClass(mContext, MyInfo.class);
        startActivity(intent);

    }







    /**
     * 定义ListView适配器MainListViewAdapter
     */
    class HFBGoodsAdapter extends BaseAdapter {

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
            ListItemView listItemView;

            // 初始化item view
            if (convertView == null) {
                // 通过LayoutInflater将xml中定义的视图实例化到一个View中
                convertView = LayoutInflater.from(HfbCenter.this).inflate(
                        R.layout.hfbcenter_cell, null);

                // 实例化一个封装类ListItemView，并实例化它的两个域
                listItemView = new ListItemView();
                listItemView.img = (ImageView) convertView
                        .findViewById(R.id.hfbcenter_cell_img);
                listItemView.name = (TextView) convertView
                        .findViewById(R.id.hfbcenter_cell_name);
                listItemView.price = (TextView) convertView
                        .findViewById(R.id.hfbcenter_cell_price);

                // 将ListItemView对象传递给convertView
                convertView.setTag(listItemView);
            } else {
                // 从converView中获取ListItemView对象
                listItemView = (ListItemView) convertView.getTag();
            }

            ViewGroup.LayoutParams layoutParams = listItemView.img.getLayoutParams();

            int w = SW/2;
            int h = (int)(w*0.75);

            layoutParams.height = h;
            listItemView.img.setLayoutParams(layoutParams);

            String img = dataArr.get(position).getUrl();
            String name = dataArr.get(position).getName();
            String price = dataArr.get(position).getHfb();

            ImageLoader.getInstance().displayImage(img,listItemView.img);
            listItemView.name.setText(name);
            listItemView.price.setText(price+"怀府币");

            // 返回convertView对象
            return convertView;
        }

    }

    /**
     * 封装两个视图组件的类
     */
    class ListItemView {
        ImageView img;
        TextView name;
        TextView price;
    }

}
