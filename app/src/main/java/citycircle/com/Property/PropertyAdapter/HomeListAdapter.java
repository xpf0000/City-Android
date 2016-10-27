package citycircle.com.Property.PropertyAdapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.PreferencesUtils;

/**
 * Created by admins on 2016/2/23.
 */
public class HomeListAdapter extends BaseAdapter {
    private ArrayList<HashMap<String, String>> array;
    Context context;
    public HomeListAdapter(ArrayList<HashMap<String, String>> array, Context context,Handler handler) {
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
        final getItemView getItemView=new getItemView();
        convertView = LayoutInflater.from(context).inflate(R.layout.homelist, null);
        getItemView.linearLayout=(LinearLayout)convertView.findViewById(R.id.linearLayout);
        getItemView.title=(TextView)convertView.findViewById(R.id.title);
        final String houseid= PreferencesUtils.getString(context, "houseids");
        if (array.get(position).get("houseid").equals(houseid)){
            getItemView.linearLayout.setBackgroundResource(R.color.wh);
            getItemView.title.setTextColor(context.getResources().getColor(R.color.blackcolor));
            GlobalVariables.housename=array.get(position).get("xiaoqu")+array.get(position).get("louhao")+array.get(position).get("fanghao");
            GlobalVariables.proname=array.get(position).get("xiaoqu");
        }
        getItemView.title.setText(array.get(position).get("xiaoqu")+array.get(position).get("louhao")+array.get(position).get("fanghao"));
        return convertView;
    }
    private class getItemView {
        TextView title;
        LinearLayout linearLayout;
    }
}
