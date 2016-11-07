package citycircle.com.user;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import citycircle.com.Activity.MyInfo;
import citycircle.com.Adapter.PaihangAdapter;
import citycircle.com.MyAppService.LocationApplication;
import citycircle.com.R;
import model.HFBModel;
import util.BaseActivity;
import util.DataCache;
import util.HttpResult;
import util.XHorizontalBaseFragment;
import util.XNetUtil;

import static citycircle.com.MyAppService.LocationApplication.APPDataCache;

/**
 * Created by X on 2016/11/7.
 */

public class MyMinePageRight extends XHorizontalBaseFragment
{

    private Context context;

    private TextView sex;
    private TextView birth;
    private TextView tel;

    private LinearLayout btn;


    @Override
    protected void lazyLoad() {

        System.out.println("RightFragment--->lazyLoad !!!");

        getData();

    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        System.out.println("RightFragment--->onCreate");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        System.out.println("RightFragment--->onCreateView");
        View v = inflater.inflate(R.layout.myminepage_right, container, false);

        btn = (LinearLayout) v.findViewById(R.id.myminepage_right_btn);
        sex = (TextView) v
                .findViewById(R.id.myminepage_right_sex);
        birth = (TextView) v
                .findViewById(R.id.myminepage_right_birth);
        tel = (TextView) v
                .findViewById(R.id.myminepage_right_tel);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toEdit(v);
            }
        });

        return v;
    }

    public void toEdit(View v)
    {
        v.setEnabled(false);

        ((BaseActivity)getActivity()).pushVC(MyInfo.class);

        v.setEnabled(true);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        System.out.println("RightFragment--->onResume");
    }

    private void getData() {
        XNetUtil.APPPrintln("do getData !!!!!!!!!!");

        if(APPDataCache.User.getSex().equals("0"))
        {
            sex.setText("女");
        }
        else
        {
            sex.setText("男");
        }

        birth.setText(APPDataCache.User.getBirthday());
        String m = APPDataCache.User.getMobile();
        if(m.length() > 7)
        {
            this.tel.setText(m.substring(0,3)+"****"+m.substring(7,11));
        }
        else
        {
            tel.setText("暂未绑定手机号");
        }




    }


}
