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
 * Created by admins on 2016/3/22.
 */
public class MeeageAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
    Context context;
    public MeeageAdapter(ArrayList<HashMap<String, String>> array, Context context) {
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
        getItem getItem=new getItem();
        convertView = LayoutInflater.from(context).inflate(R.layout.estatemessagelist, null);
        getItem.title=(TextView)convertView.findViewById(R.id.title);
        getItem.time=(TextView)convertView.findViewById(R.id.time);
        getItem.username=(TextView)convertView.findViewById(R.id.username);
        getItem.title.setText(array.get(position).get("content"));
        getItem.time.setText(DateUtils.getDateToStringss(Long.parseLong(array.get(position).get("create_time"))));
        getItem.username.setText(array.get(position).get("xiaoqu"));
        if (array.get(position).get("dian").equals("1")){
            getItem.username.setTextColor(context.getResources().getColor(R.color.graly));
        }
        return convertView;
    }

    public class getItem {
        TextView title, time,username;
    }
}
