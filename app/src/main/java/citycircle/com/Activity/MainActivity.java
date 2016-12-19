package citycircle.com.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.alertview.AlertView;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Timer;
import java.util.TimerTask;

import citycircle.com.Fragment.CityCircleFragment;
import citycircle.com.Fragment.FoundFragment;
import citycircle.com.Fragment.GroupFragment;
import citycircle.com.Fragment.HomeFragment;
import citycircle.com.Fragment.MineFragment;
import citycircle.com.Fragment.VipCardFragment;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.MyEventBus;
import citycircle.com.Utils.PreferencesUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import okhttp3.Call;
import util.XActivityindicator;
import util.XNotificationCenter;

import static citycircle.com.MyAppService.LocationApplication.APPDataCache;

public class MainActivity extends FragmentActivity implements CompoundButton.OnCheckedChangeListener {
    private RadioButton home, rb_lehui, rb_subscribe, rb_mall, rb_vipcard;
    public static FragmentTransaction transaction;
    String activityStyle = null;
    public HomeFragment HomeFragment;
    public CityCircleFragment LehuiFragment;
    public FoundFragment MallFragment;
    public MineFragment MemberFragment;
    public GroupFragment groupFragment;
    TextView badge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        EventBus.getDefault().register(this);
        if (activityStyle == null) {

            if (HomeFragment == null) {
                HomeFragment = new HomeFragment();
                transaction.add(R.id.all_content, HomeFragment);
                transaction.commit();
            }

        }
        int a = PreferencesUtils.getInt(MainActivity.this, "land");
        if (a != 0) {
            getJsom();
        } else {
            GlobalVariables.types = false;
        }

        XNotificationCenter.getInstance().addObserver("ShowAccountLogout", new XNotificationCenter.OnNoticeListener() {
            @Override
            public void OnNotice(Object obj) {
                showAccountLogout();
            }
        });

    }

    private void showAccountLogout()
    {
        AlertView Alert = new AlertView("提醒", "您的账户已在其他设备登录", null, null,
                new String[]{"确定"},
                this, AlertView.Style.Alert, new com.bigkoo.alertview.OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position == 0) {

                }
            }
        });

        XActivityindicator.setAlert(Alert);

        Alert.show();
        badge.setVisibility(View.GONE);
        home.setChecked(true);
        PreferencesUtils.putInt(this, "land", 0);
        PreferencesUtils.putString(this, "userid", null);
        APPDataCache.User.unRegistNotice();
        APPDataCache.User.reSet();

        Platform QQ = ShareSDK.getPlatform(QZone.NAME);
        QQ.SSOSetting(true);
        if(QQ.isAuthValid() ){
            QQ.removeAccount();
            ShareSDK.removeCookieOnAuthorize(true);

        }

        Platform WX = ShareSDK.getPlatform(Wechat.NAME);
        WX.SSOSetting(true);
        if(WX.isAuthValid() ){
            WX.removeAccount();
            ShareSDK.removeCookieOnAuthorize(true);
        }

        Platform SINA = ShareSDK.getPlatform(SinaWeibo.NAME);
        SINA.SSOSetting(true);
        if(SINA.isAuthValid() ){
            SINA.removeAccount();
            ShareSDK.removeCookieOnAuthorize(true);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void initComponents() {
        badge = (TextView) findViewById(R.id.bdage);
        badge.setVisibility(View.GONE);
        home = (RadioButton) findViewById(R.id.rb_home);
        rb_lehui = (RadioButton) findViewById(R.id.rb_lehui);
        rb_vipcard = (RadioButton) findViewById(R.id.rb_vipcard);
        rb_subscribe = (RadioButton) findViewById(R.id.rb_subscribe);
        rb_mall = (RadioButton) findViewById(R.id.rb_mall);
        home.setOnCheckedChangeListener(this);
        rb_lehui.setOnCheckedChangeListener(this);
        rb_subscribe.setOnCheckedChangeListener(this);
        rb_mall.setOnCheckedChangeListener(this);
        rb_vipcard.setOnCheckedChangeListener(this);
        transaction = getSupportFragmentManager().beginTransaction();

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            transaction = getSupportFragmentManager().beginTransaction();
            switch (buttonView.getId()) {
                case R.id.rb_home:

                    if (HomeFragment == null) {
                        HomeFragment = new HomeFragment();
                        transaction.add(R.id.all_content, HomeFragment);
                    }
                    if (LehuiFragment != null) {
                        transaction.hide(LehuiFragment);
                    }

                    if (MallFragment != null) {
                        transaction.hide(MallFragment);
                    }
                    if (MemberFragment != null) {
                        transaction.hide(MemberFragment);
                    }
                    if (groupFragment != null) {
                        transaction.hide(groupFragment);
                    }
                    transaction.show(HomeFragment);
                    break;
                case R.id.rb_lehui:
                    if (LehuiFragment == null) {
                        LehuiFragment = new CityCircleFragment();
                        transaction.add(R.id.all_content, LehuiFragment);
                    }
                    if (HomeFragment != null) {
                        transaction.hide(HomeFragment);
                    }

                    if (MallFragment != null) {
                        transaction.hide(MallFragment);
                    }
                    if (MemberFragment != null) {
                        transaction.hide(MemberFragment);
                    }
                    if (groupFragment != null) {
                        transaction.hide(groupFragment);
                    }
                    transaction.show(LehuiFragment);
                    break;
                case R.id.rb_subscribe:
                    if (MallFragment == null) {
                        MallFragment = new FoundFragment();
                        transaction.add(R.id.all_content, MallFragment);
                    }
                    if (HomeFragment != null) {
                        transaction.hide(HomeFragment);
                    }
                    if (MemberFragment != null) {
                        transaction.hide(MemberFragment);
                    }
                    if (LehuiFragment != null) {
                        transaction.hide(LehuiFragment);
                    }
                    if (groupFragment != null) {
                        transaction.hide(groupFragment);
                    }
                    transaction.show(MallFragment);
                    break;
                case R.id.rb_mall:

                    XNotificationCenter.getInstance().postNotice("MinePageShow",null);

                    if (MemberFragment == null) {
                        MemberFragment = new MineFragment();
                        transaction.add(R.id.all_content, MemberFragment);
                    }
                    if (HomeFragment != null) {
                        transaction.hide(HomeFragment);
                    }

                    if (MallFragment != null) {
                        transaction.hide(MallFragment);
                    }
                    if (LehuiFragment != null) {
                        transaction.hide(LehuiFragment);
                    }
                    if (groupFragment != null) {
                        transaction.hide(groupFragment);
                    }
                    transaction.show(MemberFragment);
                    break;
                case R.id.rb_vipcard:
                    if (groupFragment == null) {
                        groupFragment = new GroupFragment();
                        transaction.add(R.id.all_content, groupFragment);
                    }
                    if (HomeFragment != null) {
                        transaction.hide(HomeFragment);
                    }

                    if (MallFragment != null) {
                        transaction.hide(MallFragment);
                    }
                    if (LehuiFragment != null) {
                        transaction.hide(LehuiFragment);
                    }
                    if (MemberFragment != null) {
                        transaction.hide(MemberFragment);
                    }
                    transaction.show(groupFragment);
                    break;
            }
            transaction.commitAllowingStateLoss();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click(); // 调用双击退出函数
        }
        return false;
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            // 提示放屏幕中间
            // Toast toast;
            // toast = Toast.makeText(getApplicationContext(), "再按一次退出程序",
            // Toast.LENGTH_SHORT);
            // toast.setGravity(Gravity.CENTER, 0, 0);
            // toast.show();

            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            for (int i = 0; i < GlobalVariables.TITLE.length; i++) {
                PreferencesUtils.putString(MainActivity.this, GlobalVariables.TITLE[i], null);
                PreferencesUtils.putString(MainActivity.this, GlobalVariables.TITLE[i] + "img", null);
            }
            finish();
            System.exit(0);
        }
    }

    @Subscribe
    public void getEventmsg(MyEventBus myEventBus) {

        if (myEventBus.getMsg().equals("show")) {
            badge.setVisibility(View.VISIBLE);
        } else if(myEventBus.getMsg().equals("hidden")) {
            badge.setVisibility(View.GONE);
        }

    }

    public void getJsom() {
        String username = PreferencesUtils.getString(MainActivity.this, "username");
        String uid = PreferencesUtils.getString(MainActivity.this, "userid");
        String url = GlobalVariables.urlstr + "user.getMessagesCount&uid=" + uid + "&username=" + username;
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Toast.makeText(MainActivity.this, R.string.intent_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = JSON.parseObject(response);
                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                if (jsonObject1.getIntValue("code") == 0) {
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("info");
                    if (jsonObject2.getIntValue("count1")==0&&jsonObject2.getIntValue("count2")==0&&jsonObject2.getIntValue("count3")==0){
                        GlobalVariables.types = false;
                    }else {
                        GlobalVariables.types = true;
                    }

                } else {
                    EventBus.getDefault().post(
                            new MyEventBus("show"));
                    GlobalVariables.types = true;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册EventBus
    }
}
