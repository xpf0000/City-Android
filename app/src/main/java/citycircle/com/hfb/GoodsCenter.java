package citycircle.com.hfb;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.handmark.pulltorefresh.library.extras.recyclerview.PullToRefreshRecyclerView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import citycircle.com.R;
import model.BannerModel;
import model.GoodsModel;
import util.BaseActivity;
import util.NetworkImageHolderView;
import util.XNetUtil;

import static citycircle.com.MyAppService.LocationApplication.APPService;
import static citycircle.com.MyAppService.LocationApplication.SW;

/**
 * Created by X on 2016/11/4.
 */

public class GoodsCenter extends BaseActivity {

    private PullToRefreshRecyclerView list;
    private RecyclerView mRecyclerView;
    private GoodsAdapter adapter;
    private List<GoodsModel> dataArr = new ArrayList<>();
    private List<BannerModel> bannerArr = new ArrayList<>();

    @Override
    protected void setupUi() {
        setContentView(R.layout.goodscenter);
        setPageTitle("兑换商城");

        list = (PullToRefreshRecyclerView)findViewById(R.id.goodscenger_list);
        mRecyclerView = list.getRefreshableView();



        //设置布局管理器
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int spansize=1;

                switch (adapter.getItemViewType(position)) {
                    case 0:
                        spansize=2;
                        break;
                }

                return spansize;
            }
        });

        //设置布局管理器
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mRecyclerView.setHasFixedSize(false);

        adapter = new GoodsAdapter(mContext);
        mRecyclerView.setAdapter(adapter);

        getBanner();
        getGoods();

    }

    @Override
    protected void setupData() {

    }

    public void setDataArr(List<GoodsModel> dataArr) {
        this.dataArr = dataArr;
        adapter.notifyDataSetChanged();
    }

    private void getBanner() {

        XNetUtil.Handle(APPService.getBanner(), new XNetUtil.OnHttpResult<List<BannerModel>>() {
            @Override
            public void onError(Throwable e) {

                XNetUtil.APPPrintln(e);

            }

            @Override
            public void onSuccess(List<BannerModel> bannerModels) {
                bannerArr = bannerModels;
                adapter.notifyDataSetChanged();
            }
        });




    }

    private void getGoods() {

        XNetUtil.Handle(APPService.jifenGetproductList(1,20), new XNetUtil.OnHttpResult<List<GoodsModel>>() {
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



    /**
     * 定义ListView适配器MainListViewAdapter
     */
    private class GoodsAdapter extends RecyclerView.Adapter {

        private  class ViewHolder extends RecyclerView.ViewHolder
        {
            public ViewHolder(View arg0)
            {
                super(arg0);
            }
            ImageView img;
            TextView name;
            TextView price;
            ConvenientBanner banner;
        }

        private LayoutInflater mInflater;

        public GoodsAdapter(Context context)
        {
            mInflater = LayoutInflater.from(context);
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            if(viewType == 0)
            {
                View view = mInflater.inflate(R.layout.goodscenter_banner_cell,
                        parent, false);
                ViewHolder viewHolder = new ViewHolder(view);

                viewHolder.banner = (ConvenientBanner) view
                        .findViewById(R.id.goodscenter_banner);

                return viewHolder;
            }
            else
            {
                View view = mInflater.inflate(R.layout.hfbcenter_cell,
                        parent, false);
                ViewHolder viewHolder = new ViewHolder(view);

                viewHolder.img = (ImageView) view
                        .findViewById(R.id.hfbcenter_cell_img);
                viewHolder.name = (TextView) view
                        .findViewById(R.id.hfbcenter_cell_name);
                viewHolder.price = (TextView) view
                        .findViewById(R.id.hfbcenter_cell_price);

                return viewHolder;
            }


        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder,final int i) {

            if(getItemViewType(i) == 0)
            {
                ViewHolder viewHolder = ((ViewHolder)holder);
                if(viewHolder.banner == null){return;}

                ViewGroup.LayoutParams layoutParams = viewHolder.banner.getLayoutParams();

                int w = SW;
                int h = (int)(w*9.0/16.0);

                layoutParams.height = h;
                viewHolder.banner.setLayoutParams(layoutParams);

                viewHolder.banner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
                    @Override
                    public NetworkImageHolderView createHolder() {
                        return new NetworkImageHolderView();
                    }
                }, bannerArr);

            }
            else
            {
                ViewHolder viewHolder = ((ViewHolder)holder);
                if(viewHolder.img == null){return;}

                ViewGroup.LayoutParams layoutParams = viewHolder.img.getLayoutParams();

                int w = SW/2;
                int h = (int)(w*0.75);

                layoutParams.height = h;
                viewHolder.img.setLayoutParams(layoutParams);

                String img = dataArr.get(i-1).getUrl();
                String name = dataArr.get(i-1).getName();
                String price = dataArr.get(i-1).getHfb();

                ImageLoader.getInstance().displayImage(img,viewHolder.img);
                viewHolder.name.setText(name);
                viewHolder.price.setText(price+"怀府币");
            }

        }

        @Override
        public int getItemViewType(int position) {

            if(position == 0)
            {
                return 0;
            }

            return 1;

        }

        /**
         * 返回item的id
         */
        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public int getItemCount() {
            return dataArr.size()+1;
        }

    }
}
