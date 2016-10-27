package citycircle.com.OA.OAAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import citycircle.com.R;

/**
 * Created by admins on 2016/1/8.
 */
public class ChangAdapter extends BaseAdapter {
    private List<CityNameMod> list = null;
    private Context mContext;
    public ChangAdapter(Context mContext, List<CityNameMod> list) {
        this.mContext = mContext;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=new ViewHolder();
        convertView = LayoutInflater.from(mContext).inflate(R.layout.chang_item, null);
        viewHolder.title=(TextView)convertView.findViewById(R.id.title);
        viewHolder.fristw=(TextView)convertView.findViewById(R.id.fristw);
        viewHolder.title.setText(list.get(position).getTruename());
        viewHolder.fristw.setText(list.get(position).getTruename().substring(0,1));
        return convertView;
    }
    final static class ViewHolder {
        TextView title,fristw;
    }
}
