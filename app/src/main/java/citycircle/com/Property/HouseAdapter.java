package citycircle.com.Property;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.PreferencesUtils;
import model.HouseModel;

import static citycircle.com.MyAppService.LocationApplication.APPDataCache;

/**
 * Created by admins on 2016/2/19.
 */
public class HouseAdapter extends BaseAdapter {

    Context context;
    private Animation animation;
    private Map<Integer, Boolean> isFrist;
    public OnHouseAdapterClick onHouseAdapterClick;
    public List<HouseModel> dataArr = new ArrayList<>();

    public interface OnHouseAdapterClick{
        public void onBtnClick(int type,int position);
    }


    public HouseAdapter(Context context) {
        this.context = context;
        animation = AnimationUtils.loadAnimation(context, R.anim.woniu_list_item);
        isFrist = new HashMap<Integer, Boolean>();
    }

    @Override
    public int getCount() {
        return dataArr.size();
    }

    @Override
    public Object getItem(int position) {
        return dataArr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final getItemView getItemView=new getItemView();
        convertView = LayoutInflater.from(context).inflate(R.layout.houseitem, null);
        if (isFrist.get(position) == null || isFrist.get(position)) {
            convertView.startAnimation(animation);
            isFrist.put(position, false);
        }
        getItemView.adress=(TextView)convertView.findViewById(R.id.adress);
        getItemView.title=(TextView)convertView.findViewById(R.id.title);
        getItemView.yanzheng=(TextView)convertView.findViewById(R.id.yanzheng);
        getItemView.del=(TextView)convertView.findViewById(R.id.del);
        getItemView.defaults=(TextView)convertView.findViewById(R.id.defaults);
        getItemView.adress.setText(dataArr.get(position).getXiaoqu()+dataArr.get(position).getLouhao()+dataArr.get(position).getDanyuan()+dataArr.get(position).getFanghao());
        getItemView.title.setText(dataArr.get(position).getXiaoqu());
        final String houseid= APPDataCache.User.getHouseid();
        final String fanghaoid=APPDataCache.User.getFanghaoid();

        String truename=PreferencesUtils.getString(context,"truename");
        getItemView.yanzheng.setText("业主:"+truename);
        if (dataArr.get(position).getHouseid().equals(houseid)&&dataArr.get(position).getFanghaoid().equals(fanghaoid)){
            Drawable drawable=context.getResources().getDrawable(R.mipmap.checkbox_blue_selected);
            drawable.setBounds(0, 0,drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            getItemView.defaults.setCompoundDrawables(drawable,null,null,null);
        }
        getItemView.defaults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dataArr.size() <= position)
                {
                    return;
                }

                if(onHouseAdapterClick != null)
                {
                    onHouseAdapterClick.onBtnClick(0,position);
                }

            }
        });
        getItemView.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(dataArr.size() <= position)
                {
                    return;
                }

                if(onHouseAdapterClick != null)
                {
                    onHouseAdapterClick.onBtnClick(1,position);
                }
            }
        });

        return convertView;
    }

    private class getItemView {
        TextView title,adress,yanzheng,del;
        TextView defaults;
    }
}
