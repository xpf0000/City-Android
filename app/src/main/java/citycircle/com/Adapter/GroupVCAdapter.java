package citycircle.com.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import citycircle.com.Activity.GroupSearchVC;
import citycircle.com.Activity.Logn;
import citycircle.com.R;
import model.GroupModel;

/**
 * Created by X on 2016/11/13.
 */

public class GroupVCAdapter extends BaseAdapter {

    public List<Object> dataArr = new ArrayList<>();

    private Context context;

    public GroupVCAdapter(Context context) {
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

        final SearchBar searchBar;
        Cell cell;

        if(getItemViewType(position) == 0)
        {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.groupsearchbar, null);
                searchBar = new SearchBar();
                searchBar.txt = (EditText) convertView.findViewById(R.id.txt);
                searchBar.btn = (TextView) convertView.findViewById(R.id.btn);

                convertView.setTag(searchBar);

            }
            else
            {
                searchBar = (SearchBar) convertView.getTag();
            }



            searchBar.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent();
                    intent.putExtra("key",searchBar.txt.getText().toString());

                    intent.setClass(context, GroupSearchVC.class);
                    context.startActivity(intent);

                }
            });

        }
        else if(getItemViewType(position) == 1)
        {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.groupcelltj, null);
            }
        }
        else
        {
            if (convertView == null) {

                convertView = LayoutInflater.from(context).inflate(
                        R.layout.groupsearchcell, null);

                cell = new Cell();
                cell.name = (TextView) convertView
                        .findViewById(R.id.name);
                cell.txt = (TextView) convertView
                        .findViewById(R.id.txt);
                cell.img = (ImageView) convertView
                        .findViewById(R.id.img);
                cell.icon = (ImageView) convertView
                        .findViewById(R.id.icon);

                convertView.setTag(cell);

            }
            else
            {
                cell = (Cell) convertView.getTag();
            }

            GroupModel model = (GroupModel) dataArr.get(position);

            // 获取到mList中指定索引位置的资源
            String name = model.getName();
            String txt = model.getJdinfo();
            String img = model.getUrl();

            cell.name.setText(name);
            cell.txt.setText(txt);
            ImageLoader.getInstance().displayImage(img,cell.img);

            cell.txt.setVisibility(View.VISIBLE);

            String level = model.getViplevel();

            if (level.equals("1"))
            {
                cell.icon.setImageResource(R.mipmap.renzheng_icon);
            }
            else if (level.equals("2"))
            {
                cell.icon.setImageResource(R.mipmap.vip_icon);
            }
            else if (level.equals("3"))
            {
                cell.icon.setImageResource(R.mipmap.svip_icon);
            }
            else
            {
                cell.txt.setVisibility(View.GONE);
            }

        }

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {

        if(position < 2)
        {
            return position;
        }

        return 2;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    /**
     * 封装两个视图组件的类
     */
    class SearchBar {
        EditText txt;
        TextView btn;
    }

    class Cell {
//        ImageView img;
//        TextView txt;

        TextView name;
        TextView txt;
        ImageView icon;
        ImageView img;
    }

}
