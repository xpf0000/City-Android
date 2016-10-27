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
 * Created by admins on 2015/12/10.
 */
public class AboutAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> abscure_list;
    Context context;
    DateUtils dateUtils;
    public AboutAdapter(ArrayList<HashMap<String, String>> abscure_list,
                        Context context) {
        this.abscure_list = abscure_list;
        this.context = context;
        dateUtils=new DateUtils();
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
        getItemView getItemView = new getItemView();
        convertView = LayoutInflater.from(context).inflate(R.layout.aboutnews, null);
        getItemView.title=(TextView)convertView.findViewById(R.id.title);
        getItemView.time=(TextView)convertView.findViewById(R.id.time);
        getItemView.action=(TextView)convertView.findViewById(R.id.action);
        getItemView.title.setText(abscure_list.get(position).get("title"));
        getItemView.action.setText(abscure_list.get(position).get("source"));
       String time= dateUtils.getDateToString(Long.parseLong(abscure_list.get(position).get("create_time")) * 1000);
        getItemView.time.setText(time);
        return convertView;
    }
    private class getItemView {
        TextView title,time,action;
    }
}
