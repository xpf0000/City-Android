package citycircle.com.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.Activity.NewsInfoActivity;
import citycircle.com.MyViews.MyGridView;
import citycircle.com.R;
import citycircle.com.Utils.ImageUtils;

/**
 * Created by admins on 2016/6/14.
 */
public class LocaAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> arrayList;
    Context context;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    citycircle.com.Utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    ArrayList<HashMap<String, String>> list;
    ArrayList<String> imgList=new ArrayList<String>();
    public LocaAdapter(ArrayList<HashMap<String, String>> arrayList, Context context, ArrayList<HashMap<String, String>> list) {
        this.arrayList = arrayList;
        this.context = context;
        this.list = list;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        getitem getitem = new getitem();
        String url = null;
        options=ImageUtils.setcenterOptions();
        JSONArray jsonArray = JSON.parseArray(arrayList.get(position).get("picList"));
        if (jsonArray.size() < 3) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                url = jsonObject.getString("url");
            }
            convertView = LayoutInflater.from(context).inflate(R.layout.renews_item, null);
            getitem.name = (TextView) convertView.findViewById(R.id.name);
            getitem.title = (TextView) convertView.findViewById(R.id.title);
            getitem.views = (TextView) convertView.findViewById(R.id.views);
            getitem.shopimg = (ImageView) convertView.findViewById(R.id.shopimg);
            if (!setlist(position)) {
                getitem.title.setTextColor(Color.parseColor("#8e8e8e"));
            }
            getitem.title.setText(arrayList.get(position).get("title"));
            getitem.views.setText(arrayList.get(position).get("view") + "阅读");
            getitem.name.setText(arrayList.get(position).get("name"));
            ImageLoader.displayImage(url, getitem.shopimg, options, animateFirstListener);
        }else {
            imgList=new ArrayList<>();
            convertView= LayoutInflater.from(context).inflate(R.layout.news_titem,null);
            getitem.title=(TextView)convertView.findViewById(R.id.title);
            getitem.nwsgrid=(MyGridView) convertView.findViewById(R.id.nwsgrid);
            getitem.views=(TextView)convertView.findViewById(R.id.views);
            getitem.name=(TextView)convertView.findViewById(R.id.name);
            if (!setlist(position)) {
                getitem.title.setTextColor(Color.parseColor("#8e8e8e"));
            }
            getitem.views.setText(arrayList.get(position).get("view")+"阅读");
            getitem.name.setText(arrayList.get(position).get("name"));
            getitem.title.setText(arrayList.get(position).get("title"));
            for (int i=0;i<jsonArray.size();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                imgList.add(jsonObject.getString("url"));
            }
            ImgAdapter newPhotoAdapter=new ImgAdapter(imgList,context);
            getitem.nwsgrid.setAdapter(newPhotoAdapter);
            getitem.nwsgrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int positions, long id) {
                    Intent intent=new Intent();
                    intent.putExtra("id", arrayList.get(position).get("id"));
                    intent.putExtra("title", arrayList.get(position).get("title"));
                    intent.putExtra("description", arrayList.get(position).get("description"));
                    intent.putExtra("url", arrayList.get(position).get("url"));
                    intent.setClass(context, NewsInfoActivity.class);
                    context.startActivity(intent);
                }
            });
        }
        return convertView;
    }

    private class getitem {
        TextView title, name, views;
        ImageView shopimg;
        MyGridView nwsgrid;
    }

    private boolean setlist(int position) {
        boolean a = true;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).get("id").equals(arrayList.get(position).get("id"))) {
                a = false;
                return a;
            }
        }
        return a;
    }
}
