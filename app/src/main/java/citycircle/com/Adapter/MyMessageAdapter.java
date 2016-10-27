package citycircle.com.Adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.readystatesoftware.viewbadger.BadgeView;

import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.R;

/**
 * Created by admins on 2016/6/27.
 */
public class MyMessageAdapter extends BaseAdapter {
    ArrayList<HashMap<String,Object>> arrayList;
    Context context;
    public MyMessageAdapter( ArrayList<HashMap<String,Object>> arrayList, Context context){
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        getItem getItem=new getItem();
        convertView= LayoutInflater.from(context).inflate(R.layout.mymessagelist,null);
        getItem.item=(TextView)convertView.findViewById(R.id.item);
        getItem.look=(TextView)convertView.findViewById(R.id.look);
        getItem.drable=(ImageView)convertView.findViewById(R.id.drable);
        getItem.item.setText(arrayList.get(position).get("item").toString());
        getItem.look.setText(arrayList.get(position).get("items").toString());
        Drawable image =context.getResources().getDrawable(Integer.parseInt(arrayList.get(position).get("drable").toString()));
        getItem.drable.setImageDrawable(image);
        BadgeView badge = new BadgeView(context, getItem.drable);
        badge.setText(arrayList.get(position).get("count").toString());
        badge.setTextSize(10);
        badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        badge.setBadgeMargin(getItem.drable.getWidth()/5);
        badge.show();
        if (arrayList.get(position).get("count").toString().equals("0")){
            badge.hide();
        }
        return convertView;
    }
    public class getItem{
        TextView item,look;
        ImageView drable;
    }
}
