package citycircle.com.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import citycircle.com.R;
import citycircle.com.hfb.GWManageRight;
import citycircle.com.user.MyMinePage;
import model.HFBModel;
import util.XNetUtil;

import static citycircle.com.MyAppService.LocationApplication.APPDataCache;

/**
 * Created by X on 2016/11/6.
 */

public class PaihangAdapter extends BaseAdapter {

    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.mipmap.home_head)
            .showImageOnFail(R.mipmap.home_head)
            .build();

    public List<Object> dataArr = new ArrayList<>();

    private Context context;
    public String dw = "";

    public PaihangAdapter(Context context) {
        this.context = context;
    }


    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(dataArr.size() == 0)
            {
                return;
            }
            XNetUtil.APPPrintln("tag: "+view.getTag());
            int tag = Integer.parseInt(view.getTag().toString());

            String uname = "";
            String uid = "";

            if(tag < 3)
            {
                List<HFBModel> arr = (List<HFBModel>) dataArr.get(0);

                if(tag < arr.size())
                {
                    HFBModel m = arr.get(tag);
                    uname = m.getUsername();
                    uid = m.getUid();
                }

            }
            else
            {
                HFBModel m = (HFBModel) dataArr.get(tag-2);
                uname = m.getUsername();
                uid = m.getUid();
            }


            Intent intent = new Intent();

            intent.putExtra("uid",uid);
            intent.putExtra("uname",uname);
            intent.setClass(context, MyMinePage.class);
            context.startActivity(intent);

        }
    };

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

        ListItemView1 listItemView1;
        ListItemView2 listItemView2;

        View cell1 = null;
        View cell2 = null;

        if(getItemViewType(position) == 0)
        {
            if (convertView == null) {
                cell1 = LayoutInflater.from(context).inflate(
                        R.layout.paihangcell1, null);
                listItemView1 = new ListItemView1();

                listItemView1.img1 = (RoundedImageView) cell1
                        .findViewById(R.id.paihangcell1_img1);
                listItemView1.img2 = (RoundedImageView) cell1
                        .findViewById(R.id.paihangcell1_img2);
                listItemView1.img3 = (RoundedImageView) cell1
                        .findViewById(R.id.paihangcell1_img3);

                listItemView1.name1 = (TextView) cell1
                        .findViewById(R.id.paihangcell1_name1);
                listItemView1.name2 = (TextView) cell1
                        .findViewById(R.id.paihangcell1_name2);
                listItemView1.name3 = (TextView) cell1
                        .findViewById(R.id.paihangcell1_name3);

                listItemView1.day1 = (TextView) cell1
                        .findViewById(R.id.paihangcell1_day1);
                listItemView1.day2 = (TextView) cell1
                        .findViewById(R.id.paihangcell1_day2);
                listItemView1.day3 = (TextView) cell1
                        .findViewById(R.id.paihangcell1_day3);

                cell1.setTag(listItemView1);

                convertView = cell1;

            } else {
                listItemView1 = (ListItemView1) convertView.getTag();
            }

            List<HFBModel> arr = (List<HFBModel>) dataArr.get(0);

            if(arr.size() > 0)
            {
                HFBModel model = arr.get(0);

                listItemView1.name1.setText(model.getNickname());
                ImageLoader.getInstance().displayImage(model.getHeadimage(),listItemView1.img1,options);

                if(dw.equals("天"))
                {
                    listItemView1.day1.setText(model.getQdday() +"天");
                }
                else
                {
                    listItemView1.day1.setText(model.getHfb()+"怀府币");
                }

            }

            if(arr.size() > 1)
            {
                HFBModel model = arr.get(1);

                listItemView1.name2.setText(model.getNickname());
                ImageLoader.getInstance().displayImage(model.getHeadimage(),listItemView1.img2,options);

                if(dw.equals("天"))
                {
                    listItemView1.day2.setText(model.getQdday() +"天");
                }
                else
                {
                    listItemView1.day2.setText(model.getHfb()+"怀府币");
                }

            }

            if(arr.size() > 2)
            {
                HFBModel model = arr.get(2);

                listItemView1.name3.setText(model.getNickname());
                ImageLoader.getInstance().displayImage(model.getHeadimage(),listItemView1.img3,options);

                if(dw.equals("天"))
                {
                    listItemView1.day3.setText(model.getQdday() +"天");
                }
                else
                {
                    listItemView1.day3.setText(model.getHfb()+"怀府币");
                }

            }

            listItemView1.img1.setOnClickListener(clickListener);
            listItemView1.img2.setOnClickListener(clickListener);
            listItemView1.img3.setOnClickListener(clickListener);

        }
        else
        {
            if (convertView == null) {
                cell2 = LayoutInflater.from(context).inflate(
                        R.layout.paihangcell2, null);
                listItemView2 = new ListItemView2();

                listItemView2.img = (RoundedImageView) cell2
                        .findViewById(R.id.paihangcell2_img);
                listItemView2.name = (TextView) cell2
                        .findViewById(R.id.paihangcell2_name);
                listItemView2.num = (TextView) cell2
                        .findViewById(R.id.paihangcell2_num);
                listItemView2.pm = (TextView) cell2
                        .findViewById(R.id.paihangcell2_pm);

                cell2.setTag(listItemView2);

                convertView = cell2;

            } else {
                listItemView2 = (ListItemView2) convertView.getTag();
            }


            HFBModel model = (HFBModel) dataArr.get(position);

            if(position+3 > 100)
            {
                listItemView2.pm.setText("100+");
            }
            else
            {
                listItemView2.pm.setText((position+3)+"");
            }

            listItemView2.name.setText(model.getNickname());

            ImageLoader.getInstance().displayImage(model.getHeadimage(),listItemView2.img,options);

            if(dw.equals("天"))
            {
                listItemView2.num.setText(model.getQdday() +"天");
            }
            else
            {
                listItemView2.num.setText(model.getHfb()+"怀府币");
            }

            listItemView2.img.setTag(""+(position+2));
            listItemView2.img.setOnClickListener(clickListener);

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
    class ListItemView1 {
        RoundedImageView img1;
        RoundedImageView img2;
        RoundedImageView img3;

        TextView name1;
        TextView name2;
        TextView name3;

        TextView day1;
        TextView day2;
        TextView day3;
    }

    class ListItemView2 {
        TextView pm;
        RoundedImageView img;
        TextView name;
        TextView num;

    }





}



