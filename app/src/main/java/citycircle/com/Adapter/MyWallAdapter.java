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
 * Created by admins on 2016/6/21.
 */
public class MyWallAdapter extends BaseAdapter {
    List<WallJsonMo.DataBean.InfoBean> list;
    Context context;

    public MyWallAdapter(List<WallJsonMo.DataBean.InfoBean> list, Context context) {
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
        convertView = LayoutInflater.from(context).inflate(R.layout.wall_item, null);
        getItem getItem = new getItem();
        getItem.jifen = (TextView) convertView.findViewById(R.id.jifen);
        getItem.time = (TextView) convertView.findViewById(R.id.time);
        getItem.money = (TextView) convertView.findViewById(R.id.money);
        getItem.shopname = (TextView) convertView.findViewById(R.id.shopname);
        switch (list.get(position).getCardtype()) {
            case 1:
                if (list.get(position).getXftype() == 1) {
                    getItem.money.setTextColor(Color.parseColor("#239400"));
                    getItem.money.setText("充次:+" + list.get(position).getValue() + "次");
                    getItem.jifen.setText("现金:"+list.get(position).getMoney()+"元");
                } else {
                    getItem.money.setTextColor(Color.parseColor("#BD0000"));
                    getItem.money.setText("消费: -" + list.get(position).getValue() + "次");
                    getItem.jifen.setVisibility(View.GONE);
                }

                break;
            case 2:
                if (list.get(position).getXftype() == 1) {
                    getItem.money.setTextColor(Color.parseColor("#239400"));
                    getItem.money.setText("充值:+" + list.get(position).getValue() + "元");
                    getItem.jifen.setText("现金:"+list.get(position).getMoney()+"元");
                } else {
                    getItem.money.setTextColor(Color.parseColor("#BD0000"));
                    getItem.money.setText("消费: -" + list.get(position).getValue() + "元");
                    getItem.jifen.setVisibility(View.GONE);
                }

                break;
            case 3:
//               if (list.get(position).getXftype()==1){
//                   getItem.money.setTextColor(Color.parseColor("#239400"));
//                   getItem.money.setText("充值:"+list.get(position).getMoney()+"元");
//               }else {
                getItem.money.setTextColor(Color.parseColor("#BD0000"));
                getItem.money.setText("消费:" + list.get(position).getMoney() + "元");
                getItem.jifen.setText("折扣后金额:" + list.get(position).getValue()+"元");
//               }
                break;
            case 4:
//                if (list.get(position).getXftype() == 1) {
//                    getItem.money.setTextColor(Color.parseColor("#239400"));
//                    getItem.money.setText("充值:" + list.get(position).getMoney() + "元");
//                } else {
                getItem.money.setTextColor(Color.parseColor("#BD0000"));
                getItem.money.setText("消费:" + list.get(position).getMoney() + "元");
                getItem.jifen.setText("获得积分:" + list.get(position).getValue());
//                }
                break;
        }

        getItem.shopname.setText("店铺名称:" + list.get(position).getShopname());
//        getItem.jifen.setText("获得积分:"+list.get(position).getValue());
        getItem.time.setText(DateUtils.getDateToStringss(list.get(position).getCreate_time()));
        return convertView;
    }

    public class getItem {
        TextView time, money, jifen, shopname;
    }
}
