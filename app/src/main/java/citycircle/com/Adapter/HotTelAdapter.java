package citycircle.com.Adapter;

import android.content.Context;
import android.os.Handler;
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
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.ImageUtils;
import citycircle.com.Utils.PreferencesUtils;

/**
 * Created by admins on 2015/11/19.
 */
public class HotTelAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> abscure_list;
    Context context;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    citycircle.com.Utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    Handler handler = new Handler();

    public HotTelAdapter(ArrayList<HashMap<String, String>> abscure_list,
                         Context context, Handler handler) {
        this.abscure_list = abscure_list;
        this.context = context;
        ImageUtils = new ImageUtils();
        this.handler = handler;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final getItemView getItemView = new getItemView();
        convertView = LayoutInflater.from(context).inflate(R.layout.hottel_item, null);
        getItemView.content = (TextView) convertView.findViewById(R.id.hottitle);
        getItemView.hottel = (TextView) convertView.findViewById(R.id.hottel);
        getItemView.lehuiimg = (ImageView) convertView.findViewById(R.id.hotimg);
        getItemView.call=(ImageView)convertView.findViewById(R.id.call);
        final String url = abscure_list.get(position).get("path");
        getItemView.content.setText(abscure_list.get(position).get("title"));
        getItemView.hottel.setText(abscure_list.get(position).get("tel"));
        getItemView.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalVariables.position=position;
                handler.sendEmptyMessage(4);
            }
        });
        options = ImageUtils.setCirclelmageOptions();
        int a = PreferencesUtils.getInt(context, "photo");
//        if (a==1){
        ImageLoader.displayImage(url, getItemView.lehuiimg, options,
                animateFirstListener);
//        }else {
//           String urls=  ImageLoader.getDiscCache().get(url).getPath();
//           boolean bloo= ImageUtils.fileIsExists(urls);
//           if (bloo){
//               ImageLoader.displayImage(url, getItemView.lehuiimg, options,
//                       animateFirstListener);
//           }else {
//               getItemView.lehuiimg.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ImageLoader.displayImage(url, getItemView.lehuiimg, options,
//                            animateFirstListener);
//                    getItemView.lehuiimg.setClickable(false);
//                }
//            });
//           }
//        }

        return convertView;
    }

    private class getItemView {
        ImageView lehuiimg, call;
        TextView content, hottel;
    }
}
