package citycircle.com.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import citycircle.com.R;
import citycircle.com.Utils.DateUtils;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.ImageUtils;

/**
 * Created by admins on 2015/11/25.
 */
public class ReportAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> abscure_list;
    Context context;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    citycircle.com.Utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    DateUtils dateUtils=new DateUtils();
    public ReportAdapter(ArrayList<HashMap<String, String>> abscure_list,
                         Context context) {
        this.abscure_list = abscure_list;
        this.context = context;
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(context));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
    }

    @Override
    public int getCount() {
        return abscure_list.size();
    }

    @Override
    public Object getItem(int position) {
        return abscure_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final getItemView getItemView = new getItemView();
        convertView = LayoutInflater.from(context).inflate(R.layout.repprt_item, null);
        getItemView.title = (TextView) convertView.findViewById(R.id.title);
        getItemView.day = (TextView) convertView.findViewById(R.id.day);
        getItemView.month = (TextView) convertView.findViewById(R.id.month);
        getItemView.collect = (TextView) convertView.findViewById(R.id.collect);
        getItemView.zan = (TextView) convertView.findViewById(R.id.zan);
        getItemView.img = (ImageView) convertView.findViewById(R.id.img);
        String str = abscure_list.get(position).get("picList");
        getItemView.title.setText(abscure_list.get(position).get("content"));
        getItemView.zan.setText(abscure_list.get(position).get("zan"));
        getItemView.collect.setText(abscure_list.get(position).get("comment"));
        String time=abscure_list.get(position).get("create_time");
       String tody= dateUtils.getCurrentDate();
        Long sec=Long.parseLong(time);
       String create_time= dateUtils.getDateToStrings(sec);
        if (create_time.equals(tody)){
            getItemView.day.setText("今天");
            getItemView.month.setVisibility(View.INVISIBLE);
        }else {
            getItemView.month.setVisibility(View.VISIBLE);
            Date d = new Date(sec*1000);
            SimpleDateFormat sf = new SimpleDateFormat("dd日");
            String day=sf.format(d);
            getItemView.day.setText(day);
            SimpleDateFormat sfs = new SimpleDateFormat("MM月");
            String month=sfs.format(d);
            getItemView.month.setText(month);
        }
        options=ImageUtils.setnoOptions();
        JSONArray jsonArray = JSON.parseArray(str);
        if (jsonArray.size() == 0) {
            getItemView.img.setVisibility(View.GONE);
        } else {

            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String url = jsonObject.getString("url");
            ImageLoader.displayImage(url, getItemView.img, options,
                    animateFirstListener);
        }
        return convertView;
    }

    private class getItemView {
        ImageView img;
        TextView title, day, month, collect, zan;
    }
}
