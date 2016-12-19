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
import util.XNetUtil;

import static citycircle.com.MyAppService.LocationApplication.APPDataCache;
import static citycircle.com.MyAppService.LocationApplication.APPService;

/**
 * Created by X on 2016/11/7.
 */

public class YouhuiquanAdapter extends BaseAdapter {


    public List<YouhuiquanModel> dataArr = new ArrayList<>();
    private Context context;
    public int type = 0;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ListItemView listItemView;

        // 初始化item view
        if (convertView == null) {
            // 通过LayoutInflater将xml中定义的视图实例化到一个View中
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.myyouhuiquan_cell, null);

            // 实例化一个封装类ListItemView，并实例化它的两个域
            listItemView = new ListItemView();
            listItemView.name = (TextView) convertView
                    .findViewById(R.id.myyouhuiquan_cell_name);
            listItemView.time = (TextView) convertView
                    .findViewById(R.id.myyouhuiquan_cell_time);
            listItemView.price = (TextView) convertView
                    .findViewById(R.id.myyouhuiquan_cell_price);
            listItemView.dis = (TextView) convertView
                    .findViewById(R.id.myyouhuiquan_cell_dis);
            listItemView.state = (TextView) convertView
                    .findViewById(R.id.myyouhuiquan_cell_state);

            // 将ListItemView对象传递给convertView
            convertView.setTag(listItemView);
        } else {
            // 从converView中获取ListItemView对象
            listItemView = (ListItemView) convertView.getTag();
        }


//        // 获取到mList中指定索引位置的资源


        String time = dataArr.get(position).getS_time()+"至"+dataArr.get(position).getE_time();
        String price = "￥"+dataArr.get(position).getMoney();
        String dis = dataArr.get(position).getS_money();
        dis = dis.equals("0") ? "无门槛使用":"满"+dis+"使用";

        int state = dataArr.get(position).getOrlq();
        String str = "";

        if(type == 0)
        {
            str = dataArr.get(position).getOrlq() == 1? "已领取":"立即\r\n领取";

            if(dataArr.get(position).getOrlq() == 1)
            {
                listItemView.state.setOnClickListener(null);
            }
            else
            {
                listItemView.state.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        doLingqu(position);
                    }
                });
            }


        }
        else
        {
            str = "已领取";
        }


        listItemView.time.setText(time);
        listItemView.price.setText(price);
        listItemView.dis.setText(dis);
        listItemView.state.setText(str);

        // 返回convertView对象
        return convertView;
    }

    private void doLingqu(final int postion)
    {
        YouhuiquanModel model = dataArr.get(postion);

        String uid = APPDataCache.User.getUid();
        String uname = APPDataCache.User.getUsername();

        XNetUtil.Handle(APPService.jifenAddYHQ(uid, uname, model.getId()), "领取成功", "领取失败", new XNetUtil.OnHttpResult<Boolean>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(Boolean aBoolean) {

                if(aBoolean)
                {
                    dataArr.get(postion).setOrlq(1);
                    notifyDataSetChanged();
                }

            }
        });
    }


    class ListItemView {
        TextView price;
        TextView name;
        TextView dis;
        TextView time;
        TextView state;
    }

}



