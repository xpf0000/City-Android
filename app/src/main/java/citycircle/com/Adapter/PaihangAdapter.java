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
import citycircle.com.hfb.GWManageRight;
import model.HFBModel;

/**
 * Created by X on 2016/11/6.
 */

public class PaihangAdapter extends BaseAdapter {

    public List<HFBModel> dataArr = new ArrayList<>();

    private Context context;

    public PaihangAdapter(Context context) {
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
        View cell1 = null;
        View cell2 = null;

        if(getItemViewType(position) == 0)
        {
            if (convertView == null) {
                cell1 = LayoutInflater.from(context).inflate(
                        R.layout.paihangcell1, null);
                listItemView = new ListItemView();
                cell1.setTag(listItemView);

                convertView = cell1;

            } else {
                listItemView = (ListItemView) convertView.getTag();
            }

        }
        else
        {
            if (convertView == null) {
                cell2 = LayoutInflater.from(context).inflate(
                        R.layout.paihangcell2, null);
                listItemView = new ListItemView();
                cell2.setTag(listItemView);

                convertView = cell2;

            } else {
                listItemView = (ListItemView) convertView.getTag();
            }

        }

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {

        if(position == 0)
        {
            return 0;
        }

        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    /**
     * 封装两个视图组件的类
     */
    class ListItemView {
        TextView title;
    }

}



