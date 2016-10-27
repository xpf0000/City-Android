package citycircle.com.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.R;

/**
 * Created by admins on 2016/6/15.
 */
public class ClassDadpter extends BaseAdapter {
    ArrayList<HashMap<String, Object>> arrayList;
    Context context;

    public ClassDadpter(ArrayList<HashMap<String, Object>> arrayList, Context context) {
        this.context = context;
        this.arrayList = arrayList;

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
        convertView= LayoutInflater.from(context).inflate(R.layout.class_item,null);
        getItem.classname=(TextView)convertView.findViewById(R.id.classname);
        getItem.icon=(ImageView) convertView.findViewById(R.id.icon);
        getItem.classname.setText(arrayList.get(position).get("classname").toString());
        getItem.icon.setImageResource((int)arrayList.get(position).get("id"));
        if ((boolean) arrayList.get(position).get("check")){
            getItem.classname.setTextColor(Color.parseColor("#21ADFD"));
        }else {
            getItem.classname.setTextColor(Color.parseColor("#333333"));
        }
        return convertView;
    }

    private class getItem {
        ImageView icon;
        TextView classname;
    }
}
