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

/**
 * Created by admins on 2016/1/29.
 */
public class InfoAdapter extends BaseAdapter {
    private ArrayList<HashMap<String, String>> array;
    Context context;

    public InfoAdapter(ArrayList<HashMap<String, String>> array, Context context) {
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
        convertView = LayoutInflater.from(context).inflate(R.layout.info_item, null);
        getItemView.title=(TextView)convertView.findViewById(R.id.title);
        getItemView.title.setText(array.get(position).get("title"));
        return convertView;
    }
    private class getItemView {
        TextView title;
    }
}
