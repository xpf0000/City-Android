package citycircle.com.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

import citycircle.com.R;
import citycircle.com.Utils.ImageUtils;

/**
 * Created by admins on 2016/7/15.
 */
public class ImgAdapter extends BaseAdapter {
    ArrayList<String> arrayList;
    Context context;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    citycircle.com.Utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    public ImgAdapter(ArrayList<String> arrayList, Context context) {
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
        convertView= LayoutInflater.from(context).inflate(R.layout.newsimg,null);
        getItem.shopimg=(ImageView)convertView.findViewById(R.id.shopimg);
        String url=arrayList.get(position);
        options=ImageUtils.setcenterOptions();
        ImageLoader.displayImage(url,getItem.shopimg,options,animateFirstListener);
        return convertView;
    }
    private class getItem{
        ImageView shopimg;
    }
}
