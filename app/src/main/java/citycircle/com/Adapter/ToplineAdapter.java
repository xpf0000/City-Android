package citycircle.com.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.R;
import citycircle.com.Utils.ImageUtils;

/**
 * Created by admins on 2015/11/16.
 */
public class ToplineAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> abscure_list;
    Context context;
    ImageLoader ImageLoader;
    DisplayImageOptions options;
    ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    public ToplineAdapter(ArrayList<HashMap<String, String>> abscure_list,
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
        convertView = LayoutInflater.from(context).inflate(R.layout.topline_item, null);
        getItemView.content=(TextView)convertView.findViewById(R.id.content);
        getItemView.title=(TextView)convertView.findViewById(R.id.title);
        getItemView.views=(TextView)convertView.findViewById(R.id.views);
        getItemView.shopimg=(ImageView)convertView.findViewById(R.id.shopimg);
//        getItemView.content.setText(abscure_list.get(position).get("description"));
        getItemView.title.setText(abscure_list.get(position).get("title"));
        getItemView.views.setText(abscure_list.get(position).get("comment")+"评论");
        final String url= abscure_list.get(position).get("url");
        int color = context.getResources().getColor(R.color.aaaa);
        int colors = context.getResources().getColor(R.color.blackcolor);
        if (abscure_list.get(position).get("ordian").equals("0")){
            getItemView.title.setTextColor(colors);
        }else {
            getItemView.title.setTextColor(color);
        }
        options=ImageUtils.setnoOptions();
        ImageLoader.displayImage(url, getItemView.shopimg, options,
                animateFirstListener);
        return convertView;
    }
    private class getItemView {
        ImageView shopimg;
        TextView title, content,views;
    }
}
