package citycircle.com.hfb;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import citycircle.com.MyAppService.LocationApplication;
import citycircle.com.R;
import model.GoodsModel;
import util.BaseActivity;
import util.XGridView;
import util.XNetUtil;

import static citycircle.com.MyAppService.LocationApplication.APPService;

/**
 * Created by X on 2016/11/1.
 */

public class HfbCenter extends BaseActivity {

    private XGridView gview;
    private HFBGoodsAdapter adapter;
    private List<GoodsModel> dataArr = new ArrayList<>();

    public void setDataArr(List<GoodsModel> dataArr) {
        this.dataArr = dataArr;
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        getWindow().setAttributes(params);

        setContentView(R.layout.hfbcenter);

        doHideBackBtn();
        //setRightImg(R.drawable.user_head);

        gview = (XGridView) findViewById(R.id.hfbcenter_grid);
        gview.setScrollEnable(false);

        gview.setColumnWidth(LocationApplication.SW/2);

        gview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        adapter = new HFBGoodsAdapter();

        gview.setAdapter(adapter);

        getGoods();

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

    // 开始自动翻页
    @Override
    protected void onResume() {
        super.onResume();

    }

    // 停止自动翻页
    @Override
    protected void onPause() {
        super.onPause();
        //停止翻页

    }

    public void toBanKa(View v) {


    }

    public void toXiaoFei(View v) {


    }


    @Override
    protected void setupData() {

    }

    @Override
    protected void setupUi() {

    }

    @Override
    public void onCreateCustomToolBar(Toolbar toolbar) {
        super.onCreateCustomToolBar(toolbar);

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

            int w = layoutParams.width;
            int h = (int)(w*0.75);

            layoutParams.height = h;
            listItemView.img.setLayoutParams(layoutParams);

            String img = dataArr.get(position).getUrl();
            String name = dataArr.get(position).getName();
            String price = dataArr.get(position).getHfb();

            ImageLoader.getInstance().displayImage(img,listItemView.img);
            listItemView.name.setText(name);
            listItemView.price.setText(price);

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
