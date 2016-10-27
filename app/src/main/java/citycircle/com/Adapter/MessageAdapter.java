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
 * Created by admins on 2015/12/7.
 */
public class MessageAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> abscure_list;
    Context context;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options,optiona;
    citycircle.com.Utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    DateUtils dateUtils;

    public MessageAdapter(ArrayList<HashMap<String, String>> abscure_list,
                          Context context) {
        this.abscure_list = abscure_list;
        this.context = context;
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(context));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
        dateUtils = new DateUtils();
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
        convertView = LayoutInflater.from(context).inflate(R.layout.messagelistlay, null);
        getItemView.head = (ImageView) convertView.findViewById(R.id.head);
        getItemView.name = (TextView) convertView.findViewById(R.id.name);
        getItemView.content = (TextView) convertView.findViewById(R.id.content);
        getItemView.time = (TextView) convertView.findViewById(R.id.time);
        getItemView.img = (ImageView) convertView.findViewById(R.id.img);
        final String url = abscure_list.get(position).get("dpic");
        final String headimage = abscure_list.get(position).get("headimage");
        getItemView.name.setText(abscure_list.get(position).get("nickname"));
        if (abscure_list.get(position).get("content").equals("0")){
            getItemView.content.setText("赞了你");
        }else {
            getItemView.content.setText(abscure_list.get(position).get("content"));
        }

        Timechange timechange = new Timechange();
        String time = timechange.Time(dateUtils.getDateToStringss(Long.parseLong(abscure_list.get(position).get("create_time"))));
        getItemView.time.setText(time);
        options = ImageUtils.setCirclelmageOptions();
        optiona=ImageUtils.setnoOptions();
        ImageLoader.displayImage(url, getItemView.img, optiona,
                animateFirstListener);
        ImageLoader.displayImage(headimage, getItemView.head, options,
                animateFirstListener);
        return convertView;
    }

    private class getItemView {
        ImageView head, img;
        TextView name, content, time;
    }
}
