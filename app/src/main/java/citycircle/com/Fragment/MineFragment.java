package citycircle.com.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.Activity.GetPhone;
import citycircle.com.Activity.Logn;
import citycircle.com.Activity.MyCity;
import citycircle.com.Activity.MyCollect;
import citycircle.com.Activity.MyInfo;
import citycircle.com.Activity.MyVipcard;
import citycircle.com.Activity.MyWallet;
import citycircle.com.Activity.Mymessage;
import citycircle.com.Activity.SetActivity;
import citycircle.com.Activity.UpPassword;
import citycircle.com.Adapter.MineAdapter;
import citycircle.com.MyAppService.CityServices;
import citycircle.com.MyViews.MyListView;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.ImageUtils;
import citycircle.com.Utils.MyEventBus;
import citycircle.com.Utils.PreferencesUtils;
import citycircle.com.hfb.HfbCenter;
import okhttp3.Call;

/**
 * Created by admins on 2015/11/14.
 */
public class MineFragment extends Fragment implements View.OnClickListener {
    View view;
    LinearLayout mine;
    LinearLayout left;
    LinearLayout right;

    TextView name, vip,password,seting;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    citycircle.com.Utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    ImageView head;

    String opid;
    String tel;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mine_layout, container, false);
        //Intent intent = new Intent(getActivity(), CityServices.class);
        //getActivity().startService(intent);
        //IntentFilter filter = new IntentFilter(CityServices.action);
        //getActivity().registerReceiver(broadcastReceiver, filter);
        intview();
        //EventBus.getDefault().register(this);

        int a = PreferencesUtils.getInt(getActivity(), "land");
        if (a == 0) {
            handler.sendEmptyMessage(2);
        } else {
            handler.sendEmptyMessage(1);
        }

        return view;
    }

    public void intview() {
        mine = (LinearLayout) view.findViewById(R.id.mine_layout);
        left = (LinearLayout) view.findViewById(R.id.mine_left);
        right = (LinearLayout) view.findViewById(R.id.mine_right);

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
        seting=(TextView)view.findViewById(R.id.seting);
        password=(TextView)view.findViewById(R.id.password) ;
        head = (ImageView) view.findViewById(R.id.head);
        name = (TextView) view.findViewById(R.id.name);

        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    getJsom();
                    String names = PreferencesUtils.getString(getActivity(), "username");
                    vip.setText(PreferencesUtils.getString(getActivity(), "nickname"));
                    String url = PreferencesUtils.getString(getActivity(), "headimage");
                    options = ImageUtils.setCirclelmageOptions();
                    ImageLoader.displayImage(url, head, options,
                            animateFirstListener);
                    opid = PreferencesUtils.getString(getActivity(), "openid");
                    if(opid==null){
                        opid="";
                    }
                    tel =PreferencesUtils.getString(getActivity(),"mobile");

                    int a = PreferencesUtils.getInt(getActivity(), "land");

                        if (tel.length()!=0) {
                            name.setText("账号:" + tel);
//                        list.clear();
//                        item = new String[]{"收藏", "怀府圈", "修改密码"};
                            password.setText("修改密码");
                        } else {
                            name.setText("账号:" + names);
//                        list.clear();
//                        item = new String[]{"收藏", "怀府圈", "绑定手机号"};
//                        setMyListView();
//                        name.setText("账号:" + names);
                            password.setText("绑定手机号");
                        }


                    break;
                case 2:
                    //EventBus.getDefault().post(new MyEventBus("dis"));
                    name.setText("请登录");
                    vip.setText("请登录");

                    head.setImageResource(R.mipmap.my_face_icon);
                    break;
                case 3:
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();

        if(v.getTag() instanceof  String)
        {
            switch ((String)v.getTag()) {
                case "0":
                    int a = PreferencesUtils.getInt(getActivity(), "land");
                    if (a == 0) {
                        intent.setClass(getActivity(), Logn.class);
                        getActivity().startActivity(intent);
                    } else {
                        intent.setClass(getActivity(), MyInfo.class);
                        getActivity().startActivity(intent);
                    }
                    break;

//            case "1":
//                System.out.println("click "+v.getTag()+" !!!!!!!");
//                break;

                case "8":
                    intent.setClass(getActivity(), SetActivity.class);
                    getActivity().startActivity(intent);
                    break;

                case "left":
                    intent.setClass(getActivity(), HfbCenter.class);
                    getActivity().startActivity(intent);
                    break;

                case "right":
                    intent.setClass(getActivity(), SetActivity.class);
                    getActivity().startActivity(intent);
                    break;

                default:
                    System.out.println("click "+v.getTag()+" !!!!!!!");
                    break;
            }
        }

    }

//    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            int a = intent.getExtras().getInt("meeage");
//            if (a == 0) {
//                handler.sendEmptyMessage(1);
//            } else if (a == 1) {
//                handler.sendEmptyMessage(2);
//            }
//        }
//    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        //getActivity().unregisterReceiver(broadcastReceiver);
        EventBus.getDefault().unregister(this);//反注册EventBus
    }

    public void getJsom() {
        String username = PreferencesUtils.getString(getActivity(), "username");
        String uid = PreferencesUtils.getString(getActivity(), "userid");
        String url = GlobalVariables.urlstr + "user.getMessagesCount&uid=" + uid + "&username=" + username;
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Toast.makeText(getActivity(), R.string.intent_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = JSON.parseObject(response);
                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                if (jsonObject1.getIntValue("code") == 0) {
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("info");
                    if (jsonObject2.getIntValue("count1")==0&&jsonObject2.getIntValue("count2")==0&&jsonObject2.getIntValue("count3")==0){

                    }else {
                        EventBus.getDefault().post(
                                new MyEventBus("show"));
                    }

                } else {

                }
            }
        });
    }
}
