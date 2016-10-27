package citycircle.com.Property.PropertyAdapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.R;
import citycircle.com.Utils.ImageUtils;
import citycircle.com.Utils.PreferencesUtils;

/**
 * Created by admins on 2016/2/17.
 */
public class ProImgAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> abscure_list;
    Context context;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    citycircle.com.Utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    DisplayMetrics dm;
    public ProImgAdapter(ArrayList<HashMap<String, String>> abscure_list,
                           Context context) {
        this.abscure_list = abscure_list;
        this.context = context;
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(context));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
        dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
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
        convertView = LayoutInflater.from(context).inflate(R.layout.pro_img_item, null);
        getItemView.lehuiimg=(ImageView)convertView.findViewById(R.id.newsimg);
        final String url= abscure_list.get(position).get("path");
        options=ImageUtils.setnoOptions();
        int a= PreferencesUtils.getInt(context, "photo");
//        if (a==1){


//        }else {
//           String urls=  ImageLoader.getDiscCache().get(url).getPath();
//           boolean bloo= ImageUtils.fileIsExists(urls);
//           if (bloo){
        ImageLoader.displayImage(url, getItemView.lehuiimg, options,
                animateFirstListener);
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
        ImageView lehuiimg;
    }
}
