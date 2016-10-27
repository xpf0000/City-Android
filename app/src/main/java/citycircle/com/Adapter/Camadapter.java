package citycircle.com.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.R;
import citycircle.com.Utils.DateUtils;
import citycircle.com.Utils.ImageUtils;

/**
 * Created by admins on 2016/6/14.
 */
public class Camadapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> arrayList;
    Context context;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    citycircle.com.Utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    ArrayList<HashMap<String, String>> list;
    public Camadapter(ArrayList<HashMap<String, String>> arrayList, Context context,ArrayList<HashMap<String, String>> list) {
        this.arrayList = arrayList;
        this.context = context;
        this.list = list;
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(context));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        getitem getitem=new getitem();
        convertView= LayoutInflater.from(context).inflate(R.layout.cam_item,null);
        getitem.title=(TextView)convertView.findViewById(R.id.title);
        getitem.time=(TextView)convertView.findViewById(R.id.time);
        getitem.banner=(ImageView) convertView.findViewById(R.id.banner);
        getitem.statimg=(ImageView) convertView.findViewById(R.id.statimg);
        if (!setlist(position)) {
            getitem.title.setTextColor(Color.parseColor("#8e8e8e"));
        }
        getitem.title.setText(arrayList.get(position).get("title"));
        String sTime=DateUtils.getDateToString(Long.parseLong(arrayList.get(position).get("s_time")));
        String eTime=DateUtils.getDateToString(Long.parseLong(arrayList.get(position).get("e_time")));
        getitem.time.setText("活动时间:"+sTime+"-"+eTime);
        options=ImageUtils.setcenterOptions();
        String url=arrayList.get(position).get("url");
        ImageLoader.displayImage(url,getitem.banner,options,animateFirstListener);
        long time=System.currentTimeMillis();
        if (time>Long.parseLong(arrayList.get(position).get("e_time"))){
            getitem.statimg.setImageResource(R.mipmap.indexstateing);
        }else {
            getitem.statimg.setImageResource(R.mipmap.indexstateend);
        }
        return convertView;
    }
    private class getitem{
        TextView title,time;
        ImageView banner,statimg;
    }
    private boolean setlist(int position) {
        boolean a = true;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).get("id").equals(arrayList.get(position).get("id"))) {
                a = false;
                return a;
            }
        }
        return a;
    }
}
