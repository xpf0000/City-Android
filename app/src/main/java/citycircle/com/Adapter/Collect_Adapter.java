package citycircle.com.Adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
 * Created by admins on 2015/11/30.
 */
public class Collect_Adapter extends BaseAdapter {
    private ArrayList<HashMap<String, String>> list;
    private Context context;
    private LayoutInflater inflater = null;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    citycircle.com.Utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    Handler handler;
    public Collect_Adapter(ArrayList<HashMap<String, String>> list, Context context, Handler handler){
        this.context = context;
        this.list = list;
        this.handler=handler;
        inflater = LayoutInflater.from(context);
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(context));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.collect_item, null);
            holder.tv = (TextView) convertView.findViewById(R.id.content);
            holder.cb = (CheckBox) convertView.findViewById(R.id.xieyi);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.btn = (Button) convertView.findViewById(R.id.btn);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (list.get(position).get("show").equals("true")){
            holder.cb.setVisibility(View.VISIBLE);
            holder.btn.setVisibility(View.VISIBLE);
        }else {
            holder.cb.setVisibility(View.GONE);
            holder.btn.setVisibility(View.VISIBLE);
        }
        holder.tv.setText(list.get(position).get("title"));
        String url=list.get(position).get("url");
        options=ImageUtils.setOptions();
        int a = PreferencesUtils.getInt(context, "photo");
//        if (a==1){
        ImageLoader.displayImage(url, holder.img, options,
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
        holder.cb.setEnabled(false);
        holder.cb.setChecked(list.get(position).get("flag").equals("true"));
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalVariables.position=position;
                handler.sendEmptyMessage(5);
            }
        });
        return convertView;
    }
    public final class ViewHolder{
        TextView tv;
        public CheckBox cb;
        ImageView img;
        Button btn;
    }
}
