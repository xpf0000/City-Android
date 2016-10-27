package citycircle.com.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.R;
import citycircle.com.Utils.ImageUtils;
import citycircle.com.Utils.PreferencesUtils;

/**
 * Created by admins on 2015/11/23.
 */
public class newPhotoAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> abscure_list;
    Context context;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    citycircle.com.Utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    DisplayMetrics dm;
    public newPhotoAdapter(ArrayList<HashMap<String, String>> abscure_list,
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
        convertView = LayoutInflater.from(context).inflate(R.layout.newphoto_item, null);
        getItemView.lehuiimg=(ImageView)convertView.findViewById(R.id.newsimg);
        final String url= abscure_list.get(position).get("path");

        double w;
        int weight;
        double b;
        int height;
        RelativeLayout.LayoutParams params;
        if (abscure_list.size()==1){
            double we=Double.parseDouble(abscure_list.get(position).get("width"));
            double he=Double.parseDouble(abscure_list.get(position).get("height"));
            double x=he/we;
            if (x>3.0){
                weight=dm.widthPixels/2;
            }else if (x<0.5){
                weight=dm.widthPixels;
            }
            else
            {
                weight=dm.widthPixels/2;
            }
            height= (int) (weight*x);
            params=new RelativeLayout.LayoutParams(weight,height);
            getItemView.lehuiimg.setLayoutParams(params);
        }else if (abscure_list.size()==2){
            w=(5.0)/(10.0);
            weight= (int)(dm.widthPixels*w);
            b=(3.0)/(4.0);
            height=(int)(weight*b);
            params=new RelativeLayout.LayoutParams(weight,height);
            getItemView.lehuiimg.setLayoutParams(params);
        }else if (abscure_list.size()>=3){

        }
        options=ImageUtils.setcenterOptions();
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
