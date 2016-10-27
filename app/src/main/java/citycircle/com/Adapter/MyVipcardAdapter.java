package citycircle.com.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
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
 * Created by admins on 2016/6/17.
 */
public class MyVipcardAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> arrayList;
    Context context;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    citycircle.com.Utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    public MyVipcardAdapter(ArrayList<HashMap<String, String>> arrayList, Context context){
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
        convertView= LayoutInflater.from(context).inflate(R.layout.vip_item,parent,false);
        getItem getItem=new getItem();
        getItem.cardView=(CardView)convertView.findViewById(R.id.cardview);
        getItem.titile=(TextView)convertView.findViewById(R.id.titile);
        getItem.cardtype=(TextView)convertView.findViewById(R.id.cardtype);
        getItem.orlq=(TextView)convertView.findViewById(R.id.orlq);
        getItem.logo=(ImageView)convertView.findViewById(R.id.logo);
        try{
            getItem.cardView.setCardBackgroundColor(Color.parseColor(arrayList.get(position).get("color")));
        }catch (Exception e){
            getItem.cardView.setCardBackgroundColor(Color.parseColor("#232323"));
        }
        getItem.cardtype.setText(arrayList.get(position).get("type"));
        getItem.titile.setText(arrayList.get(position).get("shopname"));
        options=ImageUtils.setCirclelmageOptions();
        String url=arrayList.get(position).get("logo");
        ImageLoader.displayImage(url,getItem.logo,options,animateFirstListener);
        return convertView;
    }
    private class getItem{
        CardView cardView;
        ImageView logo;
        TextView orlq,cardtype,titile;
    }
}
