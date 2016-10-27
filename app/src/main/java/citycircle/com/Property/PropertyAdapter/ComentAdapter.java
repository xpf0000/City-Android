package citycircle.com.Property.PropertyAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
public class ComentAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> abscure_list;
    Context context;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    citycircle.com.Utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    public ComentAdapter(ArrayList<HashMap<String, String>> abscure_list,
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
        convertView = LayoutInflater.from(context).inflate(R.layout.coment_item, null);
        getItemView.lehead=(ImageView)convertView.findViewById(R.id.lehead);
        getItemView.rihead=(ImageView)convertView.findViewById(R.id.rihead);
        getItemView.lecontent=(TextView)convertView.findViewById(R.id.lecontent);
        getItemView.ricontent=(TextView)convertView.findViewById(R.id.ricontent);
        getItemView.right=(RelativeLayout)convertView.findViewById(R.id.right);
        getItemView.left=(RelativeLayout)convertView.findViewById(R.id.left);

        options=ImageUtils.setCirclelmageOptions();
        if (abscure_list.get(position).get("uid").equals("5")){
            String url= PreferencesUtils.getString(context,"headimage");
            getItemView.left.setVisibility(View.GONE);
            getItemView.ricontent.setText(abscure_list.get(position).get("content"));
            ImageLoader.displayImage(url, getItemView.rihead, options,
                    animateFirstListener);
        }else {
            String url="";
            getItemView.right.setVisibility(View.GONE);
            getItemView.lecontent.setText(abscure_list.get(position).get("content"));
            ImageLoader.displayImage(url, getItemView.lehead, options,
                    animateFirstListener);
        }
        return convertView;
    }
    private class getItemView {
        ImageView lehead,rihead;
        TextView lecontent,ricontent;
        RelativeLayout right,left;
    }
}
