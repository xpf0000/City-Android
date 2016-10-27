package citycircle.com.Property.PropertyAdapter;

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
 * Created by admins on 2016/8/19.
 */
public class InfoMadapter  extends BaseAdapter {
    private ArrayList<HashMap<String, String>> array;
    Context context;

    public InfoMadapter(ArrayList<HashMap<String, String>> array, Context context) {
        this.array = array;
        this.context = context;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return array.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        getItemView getItemView = new getItemView();
        convertView = LayoutInflater.from(context).inflate(R.layout.infom_item, null);
        getItemView.title=(TextView)convertView.findViewById(R.id.title);
        getItemView.time=(TextView)convertView.findViewById(R.id.time);
        getItemView.title.setText(array.get(position).get("title"));
        String time=DateUtils.getDateToStringss(Long.parseLong(array.get(position).get("create_time")));
        getItemView.time.setText(time);
        return convertView;
    }
    private class getItemView {
        TextView title,time;
    }
}

