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
import citycircle.com.Utils.ImageUtils;

/**
 * Created by admins on 2016/6/16.
 */
public class AllClassdadpter extends BaseAdapter {
    ArrayList<HashMap<String, String>> arrayList;
    Context context;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    citycircle.com.Utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    public AllClassdadpter(ArrayList<HashMap<String, String>> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
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
        getItem getItem=new getItem();
        convertView= LayoutInflater.from(context).inflate(R.layout.class_item,null);
        getItem.classname=(TextView)convertView.findViewById(R.id.classname);
        getItem.icon=(ImageView) convertView.findViewById(R.id.icon);
        getItem.classname.setText(arrayList.get(position).get("title").toString());
        String url=arrayList.get(position).get("url");
        options=ImageUtils.setnoOptions();
        ImageLoader.displayImage(url,getItem.icon,options,animateFirstListener);
        if (arrayList.get(position).get("check").equals("true")){
            getItem.classname.setTextColor(Color.parseColor("#21ADFD"));
        }else {
            getItem.classname.setTextColor(Color.parseColor("#333333"));
        }
        return convertView;
    }
    private class getItem{
        ImageView icon;
        TextView classname;
    }
}
