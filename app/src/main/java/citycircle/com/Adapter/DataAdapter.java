package citycircle.com.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

import citycircle.com.Activity.Cityinfo;
import citycircle.com.JsonMordel.HotPageMod;
import citycircle.com.R;
import citycircle.com.Utils.DateUtils;
import citycircle.com.Utils.ImageUtils;
import citycircle.com.Utils.Timechange;

/**
 * Created by admins on 2016/8/18.
 */
public class DataAdapter  extends RecyclerView.Adapter {

    private LayoutInflater mLayoutInflater;
    private ArrayList<HotPageMod.DataBean.InfoBean> mDataList = new ArrayList<>();
    Context context;
//    private int largeCardHeight, smallCardHeight;
com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    citycircle.com.Utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    public DataAdapter(Context context,ArrayList<HotPageMod.DataBean.InfoBean> list) {
        mLayoutInflater = LayoutInflater.from(context);
        this.context=context;
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(context));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
        this.mDataList=list;
//        largeCardHeight = (int)context.getResources().getDisplayMetrics().density * 300;
//        smallCardHeight = (int)context.getResources().getDisplayMetrics().density * 200;
    }

//    public void addAll(ArrayList<HotPageMod.DataBean.InfoBean> list) {
//        int lastIndex = this.mDataList.size();
//        if (this.mDataList.addAll(list)) {
//            notifyItemRangeInserted(lastIndex, list.size());
//        }
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.hot_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        WindowManager wm = (WindowManager) context.
                getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        double w=Double.parseDouble(mDataList.get(position).getWidth());
        double h=Double.parseDouble(mDataList.get(position).getHeight());
        double z=h/w;
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(params.width,(int)((width/2)*z));
        viewHolder.photo.setLayoutParams(layoutParams);
        Timechange timechange=new Timechange();
        String time= timechange.Time(DateUtils.getDateToStringss(Long.parseLong(mDataList.get(position).getCreate_time())));
        viewHolder.mTv.setText(mDataList.get(position).getContent());
        viewHolder.name.setText(mDataList.get(position).getNickname());
        viewHolder.time.setText(time);
        options=ImageUtils.setnoOptions();
        String url=mDataList.get(position).getUrl();
        ImageLoader.displayImage(url, viewHolder.photo, options,
                animateFirstListener);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("id",mDataList.get(position).getId());
                intent.setClass(context,Cityinfo.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTv, name, time;
        ImageView photo;

        public ViewHolder(View itemView) {
            super(itemView);
            mTv = (TextView) itemView.findViewById(R.id.content);
            name = (TextView) itemView.findViewById(R.id.name);
            time = (TextView) itemView.findViewById(R.id.time);
            photo=(ImageView)itemView.findViewById(R.id.photo);
        }
    }
}
