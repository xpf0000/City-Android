package citycircle.com.Activity;

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
 * Created by admins on 2015/12/17.
 */
public class CityListAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> abscure_list;
    Context context;
    public CityListAdapter(ArrayList<HashMap<String, String>> abscure_list,
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
        convertView = LayoutInflater.from(context).inflate(R.layout.citylist_item, null);
        getItemView.item=(TextView)convertView.findViewById(R.id.item);
        getItemView.city=(TextView)convertView.findViewById(R.id.city);
        getItemView.item.setText(abscure_list.get(position).get("name"));
        if (abscure_list.get(position).get("city").length()==0){

                getItemView.city.setVisibility(View.GONE);

        }else {
            try{
                getItemView.city.setText(abscure_list.get(position).get("city"));
            }catch (Exception e){
//            getItemView.city.setText("a");
                getItemView.city.setVisibility(View.GONE);
            }
        }


        return convertView;
    }
    private class getItemView {
        TextView item,city;
    }
}
