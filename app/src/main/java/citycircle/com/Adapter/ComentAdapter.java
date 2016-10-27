package citycircle.com.Adapter;

import android.content.Context;
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
import citycircle.com.Utils.Timechange;

/**
 * Created by admins on 2015/12/15.
 */
public class ComentAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> abscure_list;
    Context context;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    citycircle.com.Utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    DateUtils dateUtils;
    public ComentAdapter(ArrayList<HashMap<String, String>> abscure_list,
                      Context context) {
        this.abscure_list = abscure_list;
        this.context = context;
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(context));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
        dateUtils =new DateUtils();
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
        getItemView getItemView = new getItemView();
        convertView = LayoutInflater.from(context).inflate(R.layout.comment_items, null);
        getItemView.name=(TextView)convertView.findViewById(R.id.name);
        getItemView.hui=(TextView)convertView.findViewById(R.id.hui);
        getItemView.tname=(TextView)convertView.findViewById(R.id.tname);
        getItemView.comment=(TextView)convertView.findViewById(R.id.comment);
        getItemView.userhead=(ImageView)convertView.findViewById(R.id.userhead);
        getItemView.time=(TextView)convertView.findViewById(R.id.time);
        getItemView.name.setText(abscure_list.get(position).get("nickname"));
        getItemView.tname.setText(abscure_list.get(position).get("tnickname"));
        getItemView.comment.setText(abscure_list.get(position).get("content"));
        String url=abscure_list.get(position).get("headimage");
        options=ImageUtils.setCirclelmageOptions();
        ImageLoader.displayImage(url, getItemView.userhead, options,
                animateFirstListener);
        Timechange timechange=new Timechange();
        String time= timechange.Time(dateUtils.getDateToStringss(Long.parseLong(abscure_list.get(position).get("create_time"))));
        getItemView.time.setText(time);
        if (abscure_list.get(position).get("tnickname").length()==0){
            getItemView.hui.setVisibility(View.GONE);
        }else {
            getItemView.hui.setVisibility(View.VISIBLE);
        }
        return convertView;
    }
    private class getItemView {
        TextView name,hui,tname,comment,time;
        ImageView userhead;
    }
}

