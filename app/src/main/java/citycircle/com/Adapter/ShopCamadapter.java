package citycircle.com.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.R;
import citycircle.com.Utils.DateUtils;

/**
 * Created by admins on 2016/6/18.
 */
public class ShopCamadapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> arrayList;
    Context context;

    public ShopCamadapter(ArrayList<HashMap<String, String>> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
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
        getItem getItem = new getItem();
        convertView = LayoutInflater.from(context).inflate(R.layout.shopcam_item, null);
        getItem.titile = (TextView) convertView.findViewById(R.id.title);
        getItem.time = (TextView) convertView.findViewById(R.id.time);
        getItem.titile.setText(arrayList.get(position).get("title"));
        DateUtils dateUtils = new DateUtils();
        String time = dateUtils.getDateToString(Long.parseLong(arrayList.get(position).get("s_time")));
        getItem.time.setText(time);
        return convertView;
    }

    private class getItem {
        TextView titile, time;
    }
}
