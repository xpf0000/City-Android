package citycircle.com.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import citycircle.com.Activity.Logn;
import citycircle.com.Activity.MyCity;
import citycircle.com.Activity.MyCollect;
import citycircle.com.Activity.MyInfo;
import citycircle.com.Activity.MyVipcard;
import citycircle.com.Activity.Mymessage;
import citycircle.com.Activity.SetActivity;
import citycircle.com.MyAppService.LocationApplication;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.ImageUtils;
import citycircle.com.Utils.MyEventBus;
import citycircle.com.Utils.PreferencesUtils;
import citycircle.com.Utils.XAlertView;
import citycircle.com.hfb.GoodsCenter;
import citycircle.com.hfb.HfbCenter;
import citycircle.com.user.MyMinePage;
import citycircle.com.user.MyYouhuiquan;
import model.UserModel;
import okhttp3.Call;
import util.XActivityindicator;
import util.XHtmlVC;
import util.XNetUtil;
import util.XNotificationCenter;

import static citycircle.com.MyAppService.LocationApplication.APPDataCache;
import static citycircle.com.MyAppService.LocationApplication.APPService;

/**
 * Created by admins on 2015/11/14.
 */
public class MineFragment extends Fragment implements View.OnClickListener {
    View view;
    LinearLayout mine;
    LinearLayout left;
    LinearLayout right;

    TextView name, vip,leftnum,rightnum;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    citycircle.com.Utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    ImageView head,messageicon;

    String opid;
    String tel;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mine_layout, container, false);

        EventBus.getDefault().register(this);

        XNotificationCenter.getInstance().addObserver("MinePageShow", new XNotificationCenter.OnNoticeListener() {
            @Override
            public void OnNotice(Object obj) {
                show();
                getHFB();
            }
        });
        XNetUtil.APPPrintln("onCreateView &&&&&&&&&&&&&&");

        intview();

        XNotificationCenter.getInstance().addObserver("UserChanged", new XNotificationCenter.OnNoticeListener() {
            @Override
            public void OnNotice(Object obj) {
                show();
                getHFB();
                APPDataCache.User.getUser();
            }
        });

        return view;

    }

    private void show()
    {
        XNetUtil.APPPrintln(APPDataCache.User.toString());

        if(APPDataCache.User.getUid().equals(""))
        {
            vip.setText("登录后查看更多");
            name.setText("点击登录");
            head.setImageResource(R.mipmap.my_face_icon);
            messageicon.setImageResource(R.mipmap.my_icon3);
        }
        else
        {
            options = ImageUtils.setCirclelmageOptions();
            ImageLoader.displayImage(APPDataCache.User.getHeadimage(), head, options,
                    animateFirstListener);

            String n = APPDataCache.User.getNickname();
            String tel = APPDataCache.User.getMobile().equals("") ? "尚未绑定手机号" : APPDataCache.User.getMobile();

            vip.setText(n);
            name.setText(tel);

            XNetUtil.APPPrintln("APPDataCache.msgshow: "+APPDataCache.msgshow);

            if (APPDataCache.msgshow) {
                messageicon.setImageResource(R.mipmap.my_icon3_1);
            } else {
                messageicon.setImageResource(R.mipmap.my_icon3);
            }
        }

    }

    public void intview() {
        mine = (LinearLayout) view.findViewById(R.id.mine_layout);
        left = (LinearLayout) view.findViewById(R.id.mine_left);
        right = (LinearLayout) view.findViewById(R.id.mine_right);

        leftnum = (TextView) view.findViewById(R.id.mine_leftnum);
        rightnum = (TextView) view.findViewById(R.id.mine_rightnum);


        left.setOnClickListener(this);
        right.setOnClickListener(this);

        int count = mine.getChildCount();

        for(int i=0;i<count;i++)
        {
            if(mine.getChildAt(i).getTag() != null)
            {
                System.out.println(mine.getChildAt(i)) ;
                mine.getChildAt(i).setClickable(true);
                mine.getChildAt(i).setOnClickListener(this);
            }

        }

        vip = (TextView) view.findViewById(R.id.vip);

        head = (ImageView) view.findViewById(R.id.head);
        messageicon = (ImageView) view.findViewById(R.id.messageicon);
        name = (TextView) view.findViewById(R.id.name);

        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();

        show();
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent();

        String tag = (String)v.getTag();

        if(!tag.equals("8"))
        {
            if(APPDataCache.User.getUid().equals(""))
            {
                intent.setClass(getActivity(), Logn.class);
                getActivity().startActivity(intent);

                return;
            }
        }

        if(v.getTag() instanceof  String)
        {
            switch ((String)v.getTag()) {
                case "0":
                    intent.setClass(getActivity(), MyInfo.class);
                    getActivity().startActivity(intent);
                    break;

                case "1":
                    intent.putExtra("uid",APPDataCache.User.getUid());
                    intent.putExtra("uname",APPDataCache.User.getUsername());
                    intent.setClass(getActivity(), MyMinePage.class);
                    getActivity().startActivity(intent);
                break;

                case "2":
                    intent.setClass(getActivity(), MyCity.class);
                    getActivity().startActivity(intent);
                    break;

                case "3":
                    intent.setClass(getActivity(), Mymessage.class);
                    getActivity().startActivity(intent);
                    break;

                case "4":
                    intent.setClass(getActivity(), MyCollect.class);
                    getActivity().startActivity(intent);
                    break;

                case "5":
                    intent.setClass(getActivity(), GoodsCenter.class);
                    getActivity().startActivity(intent);
                    break;

                case "6":
                    intent.setClass(getActivity(), MyVipcard.class);
                    getActivity().startActivity(intent);
                    break;

                case "7":
                    intent.setClass(getActivity(), MyYouhuiquan.class);
                    getActivity().startActivity(intent);
                    break;

                case "8":
                    intent.setClass(getActivity(), SetActivity.class);
                    getActivity().startActivity(intent);
                    break;

                case "seting":
                    intent.setClass(getActivity(), SetActivity.class);
                    getActivity().startActivity(intent);
                    break;

                case "left":
                    intent.setClass(getActivity(), HfbCenter.class);
                    getActivity().startActivity(intent);
                    break;

                case "right":
                    doQD(v);
                    break;

                default:
                    System.out.println("click "+v.getTag()+" !!!!!!!");
                    break;
            }
        }

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册EventBus
    }

    @Override
    public void onStart() {
        super.onStart();
        getHFB();
        APPDataCache.User.getUser();
        XNetUtil.APPPrintln("onStart %%%%%%%%%%");
    }

    @Override
    public void onResume() {
        super.onResume();

        XNetUtil.APPPrintln("onResume *********");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        XNetUtil.APPPrintln("onAttach #########");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {

            XNetUtil.APPPrintln("isVisibleToUser ~~~~~~~~~~~~~~~");

        }
    }


    public void doQD(final View v) {
        String uid = APPDataCache.User.getUid();
        String uname = APPDataCache.User.getUsername();

        if(APPDataCache.User.getOrqd() == 1)
        {
            Intent intent = new Intent();
            intent.setClass(getActivity(), XHtmlVC.class);
            intent.putExtra("url","file:///android_asset/index.html?uid="+uid+"&uname="+uname);
            intent.putExtra("title","每日签到");
            getActivity().startActivity(intent);

            return;
        }

        XActivityindicator.create(getActivity()).show();
        v.setEnabled(false);
        XNetUtil.Handle(APPService.jifenAddQiandao(uid,uname), null, "签到失败", new XNetUtil.OnHttpResult<Boolean>() {
            @Override
            public void onError(Throwable e) {
                XNetUtil.APPPrintln(e);
                v.setEnabled(true);
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                v.setEnabled(true);
                if(aBoolean)
                {
                    qdSuccess();
                    getHFB();
                }
            }
        });

    }

    private void qdSuccess()
    {
        XAlertView alert = new XAlertView(getActivity());
        alert.show();
    }

    private void getHFB()
    {
        String uid = APPDataCache.User.getUid();
        String uname = APPDataCache.User.getUsername();

        if(uid.equals("") || uname.equals(""))
        {
            leftnum.setText("0");
            rightnum.setText("0/7");

            return;
        }

        XNetUtil.Handle(APPService.jifenGetUinfo(uid,uname), new XNetUtil.OnHttpResult<List<UserModel>>() {
            @Override
            public void onError(Throwable e) {

                XNetUtil.APPPrintln("!!!!!! jifenGetUinfo error: "+e);

            }

            @Override
            public void onSuccess(List<UserModel> arrs) {

                XNetUtil.APPPrintln(arrs.toString());

                if(arrs.size() > 0)
                {
                    UserModel u = arrs.get(0);
                    leftnum.setText(u.getHfb());
                    rightnum.setText(u.getWqd()+"/7");
                    APPDataCache.User.setOrqd(u.getOrqd());
                }

            }
        });
    }

    @Subscribe
    public void getEventmsg(MyEventBus myEventBus) {
        if (myEventBus.getMsg().equals("show")) {
            messageicon.setImageResource(R.mipmap.my_icon3_1);
        }
        else if(myEventBus.getMsg().equals("hidden"))
        {
            messageicon.setImageResource(R.mipmap.my_icon3);
        }
        else if(myEventBus.getMsg().equals("LoginSuccess"))
        {
            show();
            getHFB();
        }

    }



}
