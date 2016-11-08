package citycircle.com.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import citycircle.com.JsonMordel.WallJsonMo;
import citycircle.com.R;
import citycircle.com.Utils.DateUtils;

/**
 * Created by X on 2016/11/8.
 */

public class JifenRecordAdapter extends BaseAdapter {
    List<WallJsonMo.DataBean.InfoBean> list;
    Context context;

    public JifenRecordAdapter(List<WallJsonMo.DataBean.InfoBean> list, Context context) {
        this.context = context;
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
        convertView = LayoutInflater.from(context).inflate(R.layout.jifenrecord_cell, null);
        getItem getItem = new getItem();
        getItem.add = (TextView) convertView.findViewById(R.id.jifenrecord_cell_add);
        getItem.time = (TextView) convertView.findViewById(R.id.time);
        getItem.yu = (TextView) convertView.findViewById(R.id.jifenrecord_cell_yu);
        getItem.shopname = (TextView) convertView.findViewById(R.id.jifenrecord_cell_sname);

        getItem.add.setText("增加积分: "+list.get(position).getJf());
        getItem.yu.setText("剩余积分: "+list.get(position).getJfsy());
        getItem.shopname.setText("店铺名称:" + list.get(position).getShopname());
        getItem.time.setText(DateUtils.getDateToStringss(list.get(position).getCreate_time()));

        return convertView;
    }

    public class getItem {
        TextView time, add, yu, shopname;
    }
}
