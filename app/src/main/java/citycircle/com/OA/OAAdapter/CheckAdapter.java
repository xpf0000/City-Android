package citycircle.com.OA.OAAdapter;

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
 * Created by admins on 2016/1/8.
 */
public class CheckAdapter extends BaseAdapter
{
    ArrayList<HashMap<String, String>> abscure_list;
    Context context;
    public CheckAdapter(ArrayList<HashMap<String, String>> abscure_list,
                        Context context) {
        this.abscure_list = abscure_list;
        this.context = context;

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
        convertView = LayoutInflater.from(context).inflate(R.layout.check_item, null);
        getItemView.title=(TextView)convertView.findViewById(R.id.title);
        getItemView.title.setText(abscure_list.get(position).get("name"));
        return convertView;
    }
    private class getItemView {
        TextView title,time,action;
    }
}
