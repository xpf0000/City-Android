package citycircle.com.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import citycircle.com.R;
import model.GroupModel;

/**
 * Created by X on 2016/11/14.
 */

public class GroupSearchAdapter extends BaseAdapter {

    public List<GroupModel> dataArr = new ArrayList<>();

    private Context context;

    public GroupSearchAdapter(Context context) {
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
                    R.layout.groupsearchcell, null);

            // 实例化一个封装类ListItemView，并实例化它的两个域
            listItemView = new ListItemView();
            listItemView.name = (TextView) convertView
                    .findViewById(R.id.name);
            listItemView.txt = (TextView) convertView
                    .findViewById(R.id.txt);
            listItemView.img = (ImageView) convertView
                    .findViewById(R.id.img);
            listItemView.icon = (ImageView) convertView
                    .findViewById(R.id.icon);


            // 将ListItemView对象传递给convertView
            convertView.setTag(listItemView);
        } else {
            // 从converView中获取ListItemView对象
            listItemView = (ListItemView) convertView.getTag();
        }


        // 获取到mList中指定索引位置的资源
        String name = dataArr.get(position).getName();
        String txt = dataArr.get(position).getJdinfo();
        String img = dataArr.get(position).getUrl();

        listItemView.name.setText(name);
        listItemView.txt.setText(txt);
        ImageLoader.getInstance().displayImage(img,listItemView.img);

        listItemView.txt.setVisibility(View.VISIBLE);

        String level = dataArr.get(position).getViplevel();

        if (level.equals("1"))
        {
            listItemView.icon.setImageResource(R.mipmap.renzheng_icon);
        }
        else if (level.equals("2"))
        {
            listItemView.icon.setImageResource(R.mipmap.vip_icon);
        }
        else if (level.equals("3"))
        {
            listItemView.icon.setImageResource(R.mipmap.svip_icon);
        }
        else
        {
            listItemView.txt.setVisibility(View.GONE);
        }

        // 返回convertView对象
        return convertView;
    }

    class ListItemView {
        TextView name;
        TextView txt;
        ImageView icon;
        ImageView img;
    }

}
