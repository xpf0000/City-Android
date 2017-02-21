package citycircle.com.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import citycircle.com.Activity.CityPhotos;
import citycircle.com.MyViews.MyGridView;
import citycircle.com.R;
import citycircle.com.Utils.DateUtils;
import citycircle.com.Utils.ImageUtils;
import citycircle.com.Utils.Timechange;
import model.QuanModel;
import model.UserModel;
import util.XAPPUtil;
import util.XInterface;
import util.XNetUtil;

import static citycircle.com.MyAppService.LocationApplication.APPDataCache;

/**
 * Created by X on 2016/12/8.
 */

public class MyMineAdapter extends RecyclerView.Adapter implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.mipmap.home_head)
            .showImageOnFail(R.mipmap.home_head)
            .considerExifParams(true)
            .build();

    citycircle.com.Utils.ImageUtils ImageUtils = new ImageUtils();

    DisplayImageOptions options1 = ImageUtils.setCirclelmageOptions();

    ImageLoadingListener animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();

    private List<QuanModel> dataArr = new ArrayList<>();

    private UserModel user;

    public void setUser(UserModel user) {
        this.user = user;
    }

    public UserModel getUser() {
        return user;
    }

    public void setDataArr(List<QuanModel> dataArr) {
        this.dataArr = dataArr;
    }

    private XInterface mOnItemClickLitener;

    public void setOnItemClickLitener(XInterface mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public int checkClick(MotionEvent ev)
    {
        View left = header.left;
        View right = header.right;

        if(XAPPUtil.inRangeOfView(left,ev))
        {
            return 0;
        }
        else if(XAPPUtil.inRangeOfView(right,ev))
        {
            return 1;
        }

        return  -1;

    }

    private  class ViewHolder1 extends RecyclerView.ViewHolder
    {
        public ViewHolder1(View arg0)
        {
            super(arg0);
        }
        ImageView img;
        TextView name;
        TextView tel;
    }

    private  class ViewHolder2 extends RecyclerView.ViewHolder
    {
        public ViewHolder2(View arg0)
        {
            super(arg0);
        }
        public LinearLayout left;
        public LinearLayout right;

        public TextView leftTxt;
        public TextView rightTxt;

        public LinearLayout leftLine;
        public LinearLayout rightLine;

    }

    private  class ViewHolder3 extends RecyclerView.ViewHolder
    {
        public ViewHolder3(View arg0)
        {
            super(arg0);
        }
        ImageView uesrhead;
        TextView usename,time,review,content;
        MyGridView myGridView;

    }

    private  class ViewHolder4 extends RecyclerView.ViewHolder
    {
        public ViewHolder4(View arg0)
        {
            super(arg0);
        }
        TextView sex,birthday;
    }

    private  class ViewHolder5 extends RecyclerView.ViewHolder
    {
        public ViewHolder5(View arg0)
        {
            super(arg0);
        }
        TextView txt;
    }

    private LayoutInflater mInflater;
    private Context context;

    public MyMineAdapter(Context context)
    {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        XNetUtil.APPPrintln("0000000000000");

        if(viewType == 0)
        {
            View view = mInflater.inflate(R.layout.minecell1,
                    parent, false);
            ViewHolder1 viewHolder = new ViewHolder1(view);

            viewHolder.img = (ImageView) view.findViewById(R.id.myminepage_head);
            viewHolder.name = (TextView) view.findViewById(R.id.myminepage_name);

            return viewHolder;
        }
        else if(viewType == 1)
        {
            View view = mInflater.inflate(R.layout.minecell5,
                    parent, false);
            ViewHolder5 viewHolder = new ViewHolder5(view);
            viewHolder.txt = (TextView) view.findViewById(R.id.txt);
            return viewHolder;
        }
        else
        {

            if(flag == 0)
            {
                XNetUtil.APPPrintln("11111111111111111");
                View view = mInflater.inflate(R.layout.minecell3,
                        parent, false);
                ViewHolder3 viewHolder = new ViewHolder3(view);

                viewHolder.usename = (TextView) view.findViewById(R.id.usename);
                viewHolder.time = (TextView) view.findViewById(R.id.time);
                viewHolder.content = (TextView) view.findViewById(R.id.content);
                viewHolder.uesrhead=(ImageView)view.findViewById(R.id.uesrhead);
                viewHolder.myGridView = (MyGridView) view.findViewById(R.id.photogrid);
                viewHolder.review = (TextView) view.findViewById(R.id.review);

                return viewHolder;
            }
            else
            {
                XNetUtil.APPPrintln("2222222222222222222");
                View view = mInflater.inflate(R.layout.minecell4,
                        parent, false);
                ViewHolder4 viewHolder = new ViewHolder4(view);

                viewHolder.sex = (TextView) view.findViewById(R.id.myminepage_right_sex);
                viewHolder.birthday = (TextView) view.findViewById(R.id.myminepage_right_birth);

                return viewHolder;
            }



        }


    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder,final int i) {

        if(getItemViewType(i) == 0)
        {
            if(user == null)
            {
                return;
            }
            ViewHolder1 viewHolder = ((ViewHolder1)holder);
            ImageLoader.getInstance().displayImage(user.getHeadimage(),viewHolder.img,options);
            viewHolder.name.setText(user.getNickname());
        }
        else if(getItemViewType(i) == 1)
        {
            ViewHolder5 viewHolder = ((ViewHolder5)holder);

            if(flag == 1 || dataArr.size() > 0)
            {
                viewHolder.txt.setVisibility(View.GONE);
            }
            else
            {
                viewHolder.txt.setVisibility(View.VISIBLE);
            }


        }
        else
        {
            if(flag == 0)
            {
                ViewHolder3 viewHolder = ((ViewHolder3)holder);

                final QuanModel model = dataArr.get(i-2);
                citycircle.com.Utils.DateUtils dateUtils = new DateUtils();

                Timechange timechange=new Timechange();
                String time= timechange.Time(dateUtils.getDateToStringss(Long.parseLong(model.getCreate_time())));

                viewHolder.usename.setText(model.getNickname());
                viewHolder.time.setText(time);
                viewHolder.review.setText("");
                viewHolder.content.setText(model.getContent());
                ImageLoader.getInstance().displayImage(model.getHeadimage(), viewHolder.uesrhead, options1,
                        animateFirstListener);

                ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();

                for (int k=0;k<model.getPicList().size();k++){
                    HashMap<String, String> hashMap=new HashMap<>();
                    hashMap.put("path",model.getPicList().get(k).getUrl());
                    hashMap.put("width",model.getPicList().get(k).getWidth());
                    hashMap.put("height",model.getPicList().get(k).getHeight());
                    array.add(hashMap);
                }
                if (array.size()==2){
                    viewHolder.myGridView.setNumColumns(2);
                }else if (array.size()==1){
                    viewHolder.myGridView.setNumColumns(1);
                } if (array.size()>=3){
                viewHolder.myGridView.setNumColumns(3);
            }
                newPhotoAdapter newPhotoAdapter=new newPhotoAdapter(array,context);
                viewHolder.myGridView.setAdapter(newPhotoAdapter);
                viewHolder.myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int positions, long id) {
                        Intent intent=new Intent();
                        intent.putExtra("id",model.getId());
                        intent.putExtra("pos",positions);
                        intent.setClass(context, CityPhotos.class);
                        context.startActivity(intent);
                    }
                });

                //如果设置了回调，则设置点击事件
                if (mOnItemClickLitener != null)
                {
                    holder.itemView.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            mOnItemClickLitener.onItemClick(holder.itemView, i-2);
                        }
                    });

                }
            }
            else
            {
                if(user == null)
                {
                    return;
                }

                if(holder instanceof ViewHolder4)
                {
                    ViewHolder4 viewHolder = ((ViewHolder4)holder);

                    if(user.getSex().equals("0"))
                    {
                        viewHolder.sex.setText("女");
                    }
                    else
                    {
                        viewHolder.sex.setText("男");
                    }

                    viewHolder.birthday.setText(user.getBirthday());

                }

                //viewHolder.name.setText(user.getNickname());
            }


        }

    }

    @Override
    public int getItemViewType(int position) {

        if(position < 2)
        {
            return position;
        }

        return 2;

    }

    /**
     * 返回item的id
     */
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getHeaderId(int position) {

        if(position > 0)
        {
            return 1;
        }

        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {

        View view = mInflater.inflate(R.layout.minecell2,
                parent, false);
        view.setClickable(true);
        ViewHolder2 viewHolder = new ViewHolder2(view);

        viewHolder.left = (LinearLayout) view.findViewById(R.id.left);
        viewHolder.right = (LinearLayout) view.findViewById(R.id.right);

        viewHolder.leftTxt = (TextView) view.findViewById(R.id.left_txt);
        viewHolder.rightTxt = (TextView) view.findViewById(R.id.right_txt);

        viewHolder.leftLine = (LinearLayout) view.findViewById(R.id.left_line);
        viewHolder.rightLine = (LinearLayout) view.findViewById(R.id.right_line);

        return viewHolder;

    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

        ViewHolder2 viewHolder = ((ViewHolder2)holder);

        header = viewHolder;

        flagChange();
    }

    public ViewHolder2 header;

    @Override
    public int getItemCount() {

        if(flag == 0)
        {
            return dataArr.size()+2;
        }
        else
        {
            return 3;
        }


    }

    public int flag = 0;

    private void flagChange()
    {
        if(flag == 0)
        {
            header.leftTxt.setTextColor(Color.parseColor("#21adfd"));
            header.leftLine.setBackgroundColor(Color.parseColor("#21adfd"));

            header.rightTxt.setTextColor(Color.parseColor("#333333"));
            header.rightLine.setBackgroundColor(Color.parseColor("#f2f2f2"));
        }
        else
        {
            header.rightTxt.setTextColor(Color.parseColor("#21adfd"));
            header.rightLine.setBackgroundColor(Color.parseColor("#21adfd"));

            header.leftTxt.setTextColor(Color.parseColor("#333333"));
            header.leftLine.setBackgroundColor(Color.parseColor("#f2f2f2"));
        }
    }



}
