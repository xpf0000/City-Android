package citycircle.com.Property.PropertyAdapter;

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

import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.R;
import citycircle.com.Utils.DateUtils;
import citycircle.com.Utils.ImageUtils;

/**
 * Created by admins on 2016/2/15.
 */
public class FeeAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> abscure_list;
    Context context;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    citycircle.com.Utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    DateUtils dateUtils;
    public FeeAdapter(ArrayList<HashMap<String, String>> abscure_list,
                       Context context) {
        this.abscure_list = abscure_list;
        this.context = context;
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(context));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
        dateUtils=new DateUtils();
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
        convertView = LayoutInflater.from(context).inflate(R.layout.pro_feeback_item, null);
        getItemView.img=(ImageView)convertView.findViewById(R.id.img);
        getItemView.type=(TextView)convertView.findViewById(R.id.type);
        getItemView.content=(TextView)convertView.findViewById(R.id.content);
        getItemView.time=(TextView)convertView.findViewById(R.id.time);
        String json=abscure_list.get(position).get("picList");
        String url="";
        JSONArray jsonArray= JSON.parseArray(json);
        for (int i=0;i<jsonArray.size();i++){
            JSONObject jsonObject=jsonArray.getJSONObject(i);
            url=jsonObject.getString("url");
        }
        options=ImageUtils.setcenterOptions();
        ImageLoader.displayImage(url, getItemView.img, options,
                animateFirstListener);
        getItemView.content.setText(abscure_list.get(position).get("content"));
        String time=dateUtils.getDateToStrings(Long.parseLong(abscure_list.get(position).get("create_time")));
        getItemView.time.setText(time);
        String tyoe=abscure_list.get(position).get("type");
        if(tyoe.equals("0")){
            getItemView.type.setText("问题");
        }else if (tyoe.equals("1")){
            getItemView.type.setText("建议");
        }else {
            getItemView.type.setText("表扬");
        }
        return convertView;
    }
    private class getItemView {
        ImageView img;
        TextView type,content,time;
    }
}
