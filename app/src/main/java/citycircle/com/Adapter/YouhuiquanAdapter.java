package citycircle.com.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import citycircle.com.R;
import citycircle.com.hfb.JifenDetail;
import model.YouhuiquanModel;

/**
 * Created by X on 2016/11/7.
 */

public class YouhuiquanAdapter extends BaseAdapter {


    public List<YouhuiquanModel> dataArr = new ArrayList<>();
    private Context context;

    public YouhuiquanAdapter(Context context) {
        this.context = context;
    }

    /**
     * 返回item的个数
     */
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return dataArr.size();
    }

    /**
     * 返回item的内容
     */
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return dataArr.get(position);
    }

    /**
     * 返回item的id
     */
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    /**
     * 返回item的视图
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItemView listItemView;

        // 初始化item view
        if (convertView == null) {
            // 通过LayoutInflater将xml中定义的视图实例化到一个View中
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.myyouhuiquan_cell, null);

            // 实例化一个封装类ListItemView，并实例化它的两个域
            listItemView = new ListItemView();
            listItemView.name = (TextView) convertView
                    .findViewById(R.id.dhrecard_cell_name);
            listItemView.time = (TextView) convertView
                    .findViewById(R.id.dhrecard_cell_time);
            listItemView.yu = (TextView) convertView
                    .findViewById(R.id.dhrecard_cell_yu);
            listItemView.num = (TextView) convertView
                    .findViewById(R.id.dhrecard_cell_num);


            // 将ListItemView对象传递给convertView
            convertView.setTag(listItemView);
        } else {
            // 从converView中获取ListItemView对象
            listItemView = (ListItemView) convertView.getTag();
        }


//        // 获取到mList中指定索引位置的资源
//        String name = dataArr.get(position).getName();
//        String time = dataArr.get(position).getCreate_time();
//        String yu = "余额:"+dataArr.get(position).getHfbsy()+"个";
//        String num = "+"+dataArr.get(position).getHfb();
//
//        listItemView.name.setText(name);
//        listItemView.time.setText(time);
//        listItemView.yu.setText(yu);
//        listItemView.num.setText(num);

        // 返回convertView对象
        return convertView;
    }


    class ListItemView {
        TextView name;
        TextView time;
        TextView yu;
        TextView num;
    }

}



