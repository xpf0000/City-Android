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
 * Created by admins on 2015/12/6.
 */
public class CommAdapter extends BaseAdapter {
    private ArrayList<HashMap<String, String>> list;
    private Context context;
    private LayoutInflater inflater = null;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    citycircle.com.Utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    DateUtils dateUtils;
    public CommAdapter(ArrayList<HashMap<String, String>> list, Context context){
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(context));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
        dateUtils=new DateUtils();
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        convertView = LayoutInflater.from(context).inflate(R.layout.com_item, null);
        holder.username=(TextView)convertView.findViewById(R.id.username);
        holder.time=(TextView)convertView.findViewById(R.id.time);
        holder.comtent=(TextView)convertView.findViewById(R.id.comtent);
        holder.userhead=(ImageView)convertView.findViewById(R.id.userhead);
        Timechange timechange=new Timechange();
        String time= timechange.Time(dateUtils.getDateToStringss(Long.parseLong(list.get(position).get("create_time"))));
        holder.time.setText(time);
        holder.username.setText(list.get(position).get("nickname"));
        holder.comtent.setText(list.get(position).get("content"));
        String url=list.get(position).get("headimage");
        options=ImageUtils.setCirclelmageOptions();
        ImageLoader.displayImage(url, holder.userhead, options,
                animateFirstListener);
        return convertView;
    }
    public final class ViewHolder{
        TextView username,time,comtent;
        ImageView userhead;
    }
}
