package citycircle.com.Property.PropertyAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.R;
import citycircle.com.Utils.DateUtils;

/**
 * Created by admins on 2016/2/24.
 */
public class PayWuAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    Context context;
    DateUtils dateUtils;
    public PayWuAdapter(ArrayList<HashMap<String, String>> arrayList ,Context context){
        this.arrayList=arrayList;
        this.context=context;
        dateUtils=new DateUtils();
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
//        number上月抄表数
//                bnumber本月抄表数
//        unumber使用数量
//                ymoney应缴金额
//        smoney实缴金额
//                yumoney余额
        convertView= LayoutInflater.from(context).inflate(R.layout.paysitem,null);
        getItemView.title=(TextView)convertView.findViewById(R.id.title);
        getItemView.money=(TextView)convertView.findViewById(R.id.money);
        getItemView.time=(TextView)convertView.findViewById(R.id.time);
        getItemView.moremoney=(TextView)convertView.findViewById(R.id.moremoney);
        String time=dateUtils.getDateToStringssss(Long.parseLong(arrayList.get(position).get("create_time")));
        getItemView.title.setText(arrayList.get(position).get("smoney"));
        getItemView.money.setText(arrayList.get(position).get("ymoney"));
        getItemView.moremoney.setText(arrayList.get(position).get("yumoney"));
        getItemView.time.setText(time);
        return convertView;
    }
    private class getItemView{
        TextView title,money,time,moremoney;
    }
}
