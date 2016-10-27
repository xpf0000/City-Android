package citycircle.com.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.R;

/**
 * Created by admins on 2015/11/28.
 */
public class CalssAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> abscure_list;
    Context context;
    public CalssAdapter(ArrayList<HashMap<String, String>> abscure_list,
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
        convertView = LayoutInflater.from(context).inflate(R.layout.calss_item, null);
        getItemView.shopguige=(TextView)convertView.findViewById(R.id.shopguige);
        getItemView.shopguige.setText(abscure_list.get(position).get("title"));
        if (abscure_list.get(position).get("select").equals("0")){
            getItemView.shopguige.setBackgroundResource(R.drawable.specibutton);
            getItemView.shopguige.setTextColor(Color.BLACK);
        }else {
            getItemView.shopguige.setBackgroundResource(R.drawable.speciselect);
            getItemView.shopguige.setTextColor(Color.WHITE);
        }
        return convertView;
    }
    private class getItemView {
        TextView shopguige;
    }
}
