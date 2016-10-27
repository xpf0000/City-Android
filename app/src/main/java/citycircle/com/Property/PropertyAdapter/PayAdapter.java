package citycircle.com.Property.PropertyAdapter;

import android.content.Context;
import android.text.Layout;
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
 * Created by 飞侠 on 2016/2/23.
 */
public class PayAdapter extends BaseAdapter {
    ArrayList<HashMap<String, Object>> arrayList = new ArrayList<>();
    Context context;
    public PayAdapter(ArrayList<HashMap<String, Object>> arrayList ,Context context){
        this.arrayList=arrayList;
        this.context=context;

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
        getItemView getItemView=new getItemView();
        convertView= LayoutInflater.from(context).inflate(R.layout.payitem,null);
        getItemView.title=(TextView)convertView.findViewById(R.id.title);
        getItemView.icon=(ImageView)convertView.findViewById(R.id.icon);
        getItemView.title.setText(arrayList.get(position).get("title").toString());
        getItemView.icon.setImageResource((int)arrayList.get(position).get("icon"));
        return convertView;
    }
    private class getItemView{
        TextView title;
        ImageView icon;
    }
}
