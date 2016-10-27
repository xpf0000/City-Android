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
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.ImageUtils;
import citycircle.com.Utils.PreferencesUtils;

/**
 * Created by admins on 2015/9/21.
 */
public class PhotoAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> abscure_list;
    Context context;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;

    public PhotoAdapter(ArrayList<HashMap<String, String>> abscure_list,
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
        convertView = LayoutInflater.from(context).inflate(R.layout.phtemlayout, null);
        getItemView.content=(TextView)convertView.findViewById(R.id.title);
        getItemView.lehuiimg=(ImageView)convertView.findViewById(R.id.newsimg);
        final String url= abscure_list.get(position).get("url");
        getItemView.content.setText(abscure_list.get(position).get("title"));
        options=ImageUtils.setnoOptions();
        int a= PreferencesUtils.getInt(context, "photo");
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
        ImageView lehuiimg;
        TextView content;
    }
}
