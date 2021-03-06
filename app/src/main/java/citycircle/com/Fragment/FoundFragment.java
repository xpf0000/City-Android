package citycircle.com.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnDismissListener;
import com.bigkoo.alertview.OnItemClickListener;
import com.robin.lazy.cache.CacheLoaderManager;

import citycircle.com.Activity.GetPhone;
import citycircle.com.Activity.Logn;
import citycircle.com.Activity.MyInfo;
import citycircle.com.Activity.TelYelloePage;
import citycircle.com.OA.HomePageActivity;
import citycircle.com.OA.LandActivity;
import citycircle.com.Property.AddHome;
import citycircle.com.Property.PropertyHome;
import citycircle.com.R;

import static citycircle.com.MyAppService.LocationApplication.APPDataCache;

/**
 * Created by admins on 2015/11/14.
 */
public class FoundFragment extends Fragment implements View.OnClickListener, OnItemClickListener, OnDismissListener {
    View view;
    LinearLayout tYellowPages, sales, Property,card;
    private AlertView mAlertView;
    int type=0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.found_layout, container, false);
        intview();
        return view;
    }

    public void intview() {
        tYellowPages = (LinearLayout) view.findViewById(R.id.YellowPages);
        tYellowPages.setOnClickListener(this);
        sales = (LinearLayout) view.findViewById(R.id.sales);
        sales.setOnClickListener(this);
        Property = (LinearLayout) view.findViewById(R.id.Property);
        Property.setOnClickListener(this);

        card = (LinearLayout) view.findViewById(R.id.card);
        card.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();

        Integer o = CacheLoaderManager.getInstance().loadSerializable("oaland");
        if(o == null)
        {
            o = new Integer(0);
        }

        int oaland = o.intValue();
        int land = APPDataCache.land;
        switch (v.getId()) {
            case R.id.card:
                intent.setClass(getActivity(), VipCardFragment.class);
                getActivity().startActivity(intent);
                break;
            case R.id.YellowPages:
                intent.setClass(getActivity(), TelYelloePage.class);
                getActivity().startActivity(intent);
                break;

            case R.id.sales:
//                try {
//                    Intent i = new Intent();
//                    i.setClassName("com.example.hkoa", "com.hkoa.activity.StartoverActivity");
//                    getActivity(). startActivity(i);
//                }catch (Exception e){
//                    Toast.makeText(getActivity(),"应用位下载",Toast.LENGTH_SHORT).show();
//                }

                if (oaland == 1) {
                    intent.setClass(getActivity(), HomePageActivity.class);
                    getActivity().startActivity(intent);
                } else {
                    intent.putExtra("type", 1);
                    intent.setClass(getActivity(), LandActivity.class);
                    getActivity().startActivity(intent);
                }
                break;
            case R.id.Property:
                if (land == 1) {

                    String houseid = APPDataCache.User.getHouseid();
                    String truename = APPDataCache.User.getTruename();
                   String tel = APPDataCache.User.getMobile();
                    if (truename.length() == 0) {
                         type=0;
                        mAlertView = new AlertView("提示", "您未添加真实姓名，是否前往个人中心添加真实姓名？", "取消", new String[]{"确定"}, null, getActivity(), AlertView.Style.Alert, this).setCancelable(true).setOnDismissListener(this);
                        mAlertView.show();
                    } else if (tel.length()==0){
                         type=1;
                        mAlertView = new AlertView("提示", "您未绑定手机号，是否前往绑定手机号？", "取消", new String[]{"确定"}, null, getActivity(), AlertView.Style.Alert, this).setCancelable(true).setOnDismissListener(this);
                        mAlertView.show();
                    }
                    else {
                        if (houseid.equals("")) {
                            intent.putExtra("types", 1);
                            intent.setClass(getActivity(), AddHome.class);
                            getActivity().startActivity(intent);
                        } else {

                            intent.setClass(getActivity(), PropertyHome.class);
                            getActivity().startActivity(intent);
                        }
                    }
                } else {
                    intent.putExtra("type", 1);
                    intent.setClass(getActivity(), Logn.class);
                    getActivity().startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onItemClick(Object o, int i) {
       if (i!=-1){
           if(type==0){
               Intent intent = new Intent();
               intent.setClass(getActivity(), MyInfo.class);
//           intent.putExtra("type",1);
               getActivity().startActivity(intent);
           }else {
               Intent intent = new Intent();
               intent.setClass(getActivity(), GetPhone.class);
               getActivity().startActivity(intent);
           }

       }

    }

    @Override
    public void onDismiss(Object o) {

    }
}
