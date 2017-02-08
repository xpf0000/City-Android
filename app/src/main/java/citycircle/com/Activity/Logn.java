package citycircle.com.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnDismissListener;
import com.bigkoo.alertview.OnItemClickListener;
import com.mob.tools.utils.UIHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import citycircle.com.MyViews.MyDialog;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;
import citycircle.com.Utils.MyEventBus;
import citycircle.com.Utils.MyhttpRequest;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import model.UserModel;
import util.HttpResult;
import util.XActivityindicator;
import util.XHtmlVC;
import util.XNetUtil;
import util.XNotificationCenter;

import static citycircle.com.MyAppService.LocationApplication.APPDataCache;
import static citycircle.com.MyAppService.LocationApplication.APPService;

//import static cn.smssdk.SMSSDK.getVerificationCode;

/**
 * Created by admins on 2015/11/24.
 */
public class Logn extends Activity implements View.OnClickListener, Handler.Callback, PlatformActionListener, OnDismissListener, OnItemClickListener {
    ImageView back, qq_login_icon, sina_login_icon, my_login_wx3x;
    EditText name, password;
    Button submit;
    String url, urlstr, urldate, thredurls, therdstr, openRegister;
    Object object;
    Dialog dialog;
    TextView reg, newpa;
    String userid, nickname, sex, headimage, dastr;
    private static final int MSG_USERID_FOUND = 1;
    private static final int MSG_LOGIN = 2;
    private static final int MSG_AUTH_CANCEL = 3;
    private static final int MSG_AUTH_ERROR = 4;
    private static final int MSG_AUTH_COMPLETE = 5;
    int a = 1;
    private AlertView mAlertViewExt;
    Object openRegisterstr;
    private EditText etName;//拓展View内容
    private InputMethodManager imm;
    CloudPushService pushService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logn);

        ShareSDK.initSDK(this,"ccae6a09a59e");
        EventBus.getDefault().register(this);

        pushService = PushServiceFactory.getCloudPushService();
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        dialog = MyDialog.createLoadingDialog(Logn.this, "正在登陆...");
        url = GlobalVariables.urlstr + "User.login";
        thredurls = GlobalVariables.urlstr + "User.openLogin&openid=" + userid;
        openRegister = GlobalVariables.urlstr + "User.openRegister";
//                + "&openid=" + userid + "&nickname=" + nickname + "&sex=" + sex + "&headimage=" + headimage;
        intview();

        XNotificationCenter.getInstance().addObserver("BindPhoneSuccess", new XNotificationCenter.OnNoticeListener() {
            @Override
            public void OnNotice(Object obj) {
                bindPhoneSuccess();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        XNotificationCenter.getInstance().removeObserver("BindPhoneSuccess");
        EventBus.getDefault().unregister(this);
    }

    private void intview() {
        reg = (TextView) findViewById(R.id.reg);
        reg.setOnClickListener(this);
        newpa = (TextView) findViewById(R.id.newpa);
        newpa.setOnClickListener(this);
        name = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.password);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        qq_login_icon = (ImageView) findViewById(R.id.qq_login_icon);
        qq_login_icon.setOnClickListener(this);
        sina_login_icon = (ImageView) findViewById(R.id.sina_login_icon);
        sina_login_icon.setOnClickListener(this);
        my_login_wx3x = (ImageView) findViewById(R.id.my_login_wx3x);
        my_login_wx3x.setOnClickListener(this);
        mAlertViewExt = new AlertView("提示", "请完善你的个人资料！", "取消", null, new String[]{"完成"}, this, AlertView.Style.Alert, this);
        ViewGroup extView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.edlay, null);
        etName = (EditText) extView.findViewById(R.id.etName);
        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                //输入框出来则往上移动
                boolean isOpen = imm.isActive();
                mAlertViewExt.setMarginBottom(isOpen && focus ? 120 : 0);
                System.out.println(isOpen);
            }
        });
        mAlertViewExt.addExtView(extView);
    }

    private void getUser(final int type) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                if (type == 0) {
                    MyhttpRequest myhttpRequest = new MyhttpRequest();
                    object = myhttpRequest.request(url, urldate, "POST");
                    if (object == null) {
                        handler.sendEmptyMessage(2);
                    } else {
                        urlstr = object.toString();
                        handler.sendEmptyMessage(1);

                    }
                } else if (type == 1) {
                    HttpRequest httpRequest = new HttpRequest();
                    therdstr = httpRequest.doGet(thredurls);
                    if (therdstr.equals("网络超时")) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(4);
                    }

                } else {
                    MyhttpRequest myhttpRequest = new MyhttpRequest();
                    openRegisterstr = myhttpRequest.request(openRegister, dastr, "POST");
                    if (openRegisterstr == null) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(5);
                    }
                }

            }
        }.start();
    }

    private boolean first = false;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    dialog.dismiss();
                    JSONObject jsonObject = JSON.parseObject(urlstr);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    int a = jsonObject1.getIntValue("code");
                    if (a == 0) {
                        JSONArray jsonArray = jsonObject1.getJSONArray("info");
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            setAccount(jsonObject2.getString("uid"));

                            // JSON串转用户组对象
                            UserModel user = JSON.parseObject(jsonObject2.toJSONString(), UserModel.class);

                            APPDataCache.User = user;
                            APPDataCache.User.save();
                            APPDataCache.User.registNotice();
                            APPDataCache.User.getUser();
                            APPDataCache.User.getMsgCount();
                        }

                        XNotificationCenter.getInstance().postNotice("UserChanged",null);
                        APPDataCache.land = 1;

                        Intent intent = new Intent();
                        intent.setAction("com.servicedemo4");
                        intent.putExtra("getmeeage", "0");
                        Logn.this.sendBroadcast(intent);
                        Toast.makeText(Logn.this, "登陆成功", Toast.LENGTH_SHORT).show();

                        finish();
                    } else {
                        String msgStr = jsonObject1.getString("msg");
                        msgStr = msgStr == null ? "用户名或密码错误" : msgStr;
                        dialog.dismiss();
                        Toast.makeText(Logn.this, msgStr, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    dialog.dismiss();
                    Toast.makeText(Logn.this, "网络似乎有问题了", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    dialog.dismiss();
                    Toast.makeText(Logn.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    JSONObject jsonObject2 = JSON.parseObject(therdstr);
                    JSONObject jsonObject3 = jsonObject2.getJSONObject("data");

                    XNetUtil.APPPrintln("login str: "+therdstr);

                    if (jsonObject3.getIntValue("code") == 0) {

                        JSONObject obj = jsonObject3.getJSONArray("info").getJSONObject(0);
                        String mobil = obj.getString("mobile");

                        if(mobil.equals(""))
                        {
                            String uname = obj.getString("username");
                            Intent intent1 = new Intent();
                            intent1.putExtra("uname",uname);
                            intent1.setClass(Logn.this, GetPhone.class);
                            Logn.this.startActivity(intent1);

                            first = false;
                        }
                        else
                        {
                            urlstr = therdstr;
                            handler.sendEmptyMessage(1);
                        }

                    }
                    else {
                        mAlertViewExt.show();
                    }
                    break;
                case 5:
                    String str = openRegisterstr.toString();
                    JSONObject jsonObject4 = JSON.parseObject(str);
                    JSONObject jsonObject5 = jsonObject4.getJSONObject("data");
                    int b = jsonObject5.getIntValue("code");
                    if (b == 0) {

                        JSONArray jsonArray = jsonObject5.getJSONArray("info");
                        JSONObject jsonObject6 = jsonArray.getJSONObject(0);
                        String uname = jsonObject6.getString("username");
                        Intent intent1 = new Intent();
                        intent1.putExtra("uname",uname);
                        intent1.setClass(Logn.this, GetPhone.class);
                        Logn.this.startActivity(intent1);

                        first = true;

                    } else {
                        handler.sendEmptyMessage(3);
                    }
                    break;
            }
        }
    };

    private void bindPhoneSuccess() {

        if(!first)
        {
            urlstr = therdstr;
            handler.sendEmptyMessage(1);

            return;
        }

        String str = openRegisterstr.toString();
        JSONObject jsonObject4 = JSON.parseObject(str);
        JSONObject jsonObject5 = jsonObject4.getJSONObject("data");
        int b = jsonObject5.getIntValue("code");
        if (b == 0) {
            JSONArray jsonArray = jsonObject5.getJSONArray("info");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject6 = jsonArray.getJSONObject(i);

                setAccount(jsonObject6.getString("uid"));

                // JSON串转用户组对象
                UserModel user = JSON.parseObject(jsonObject6.toJSONString(), UserModel.class);

                APPDataCache.User = user;
                APPDataCache.User.save();
                APPDataCache.User.registNotice();
                APPDataCache.User.getUser();
                APPDataCache.User.getMsgCount();
            }

            XNotificationCenter.getInstance().postNotice("UserChanged",null);

            APPDataCache.land = 1;
            Intent intent = new Intent();
            intent.setAction("com.servicedemo4");
            intent.putExtra("getmeeage", "0");
            Logn.this.sendBroadcast(intent);
            Toast.makeText(Logn.this, "登陆成功", Toast.LENGTH_SHORT).show();

            finish();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.submit:
//                getVerificationCode("46", "15638743223");
                if (name.getText().toString().trim().length() == 0) {
                    Toast.makeText(Logn.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                } else if (password.getText().toString().trim().length() == 0) {
                    Toast.makeText(Logn.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    String names = name.getText().toString().trim();
                    String pass = password.getText().toString().trim();
                    urldate = "mobile=" + names + "&password=" + pass;
                    getUser(0);
                    dialog.show();
                }
                break;
            case R.id.newpa:
                intent.setClass(Logn.this, UpPasswords.class);
                Logn.this.startActivity(intent);
                break;
            case R.id.reg:
                intent.putExtra("type", 1);
                intent.setClass(Logn.this, ForgetPass.class);
                Logn.this.startActivity(intent);
                finish();
                break;
            case R.id.sina_login_icon:
                XActivityindicator.create(Logn.this).show();
                a = 1;
                type = "1";
                Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
                sina.setPlatformActionListener(this);
                sina.showUser(null);//执行登录，登录后在回调里面获取用户资料
//                authorize(sina);
                break;
            case R.id.qq_login_icon:
                XActivityindicator.create(Logn.this).show();
                a = 2;
                type = "3";
                Platform qzone = ShareSDK.getPlatform(QZone.NAME);
//                authorize(qzone);
                qzone.setPlatformActionListener(this);
                qzone.showUser(null);//执行登录，登录后在回调里面获取用户资料
                break;
            case R.id.my_login_wx3x:
                XActivityindicator.create(Logn.this).show();
                type = "2";
                Platform wx = ShareSDK.getPlatform(Wechat.NAME);
//                authorize(qzone);
                wx.setPlatformActionListener(this);
                wx.showUser(null);//执行登录，登录后在回调里面获取用户资料
                break;
        }
    }

    private void authorize(Platform plat) {
        if (plat == null) {
            return;
        }
        if (plat.isAuthValid()) {
            String userId = plat.getDb().getUserId();
            if (userId != null) {
                String name = plat.getName();
                UIHandler.sendEmptyMessage(MSG_USERID_FOUND, this);
//                login(plat.getName(), userId, null);
                return;
            }
        }
        plat.setPlatformActionListener(this);
        //关闭SSO授权
        plat.SSOSetting(true);
        plat.showUser(null);
    }

//新浪 1  微信  2  QQ 3
    private String type = "";
    public static PlatformDb otheruser;

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_USERID_FOUND: {
                Toast.makeText(this, R.string.userid_found, Toast.LENGTH_SHORT)
                        .show();
            }
            break;
            case MSG_LOGIN: {
                String text = getString(R.string.logining, msg.obj);
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
//                intent.setClass(LoginActivity.this, Myinfo.class);
//                LoginActivity.this.startActivity(intent);
                finish();
            }
            break;
            case MSG_AUTH_CANCEL: {
                Toast.makeText(this, R.string.auth_cancel, Toast.LENGTH_SHORT)
                        .show();
            }
            break;
            case MSG_AUTH_ERROR: {
                Toast.makeText(this, R.string.auth_error, Toast.LENGTH_SHORT)
                        .show();
            }
            break;
            case MSG_AUTH_COMPLETE: {
                thredurls = GlobalVariables.urlstr + "User.openLogin&openid=" + userid;
                getUser(1);
//                Toast.makeText(this, R.string.auth_complete, Toast.LENGTH_SHORT)
//                        .show();
            }
            break;
        }
        return false;
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        if (i == Platform.ACTION_USER_INFOR) {
            otheruser = platform.getDb();
            userOpenLogin();
        }
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        XActivityindicator.hide();
        if (i == Platform.ACTION_USER_INFOR) {
//            handler.sendEmptyMessage(MSG_AUTH_ERROR);
            UIHandler.sendEmptyMessage(MSG_AUTH_ERROR, this);
        }
        throwable.printStackTrace();
    }

    @Override
    public void onCancel(Platform platform, int i) {
        XActivityindicator.hide();
        if (i == Platform.ACTION_USER_INFOR) {
//            handler.sendEmptyMessage(MSG_AUTH_CANCEL);
            UIHandler.sendEmptyMessage(MSG_AUTH_CANCEL, this);
        }
    }

    private void userOpenLogin()
    {
        XNetUtil.HandleReturnAll(APPService.userOpenLogin(otheruser.getUserId(), type), new XNetUtil.OnHttpResult<HttpResult<List<UserModel>>>() {
            @Override
            public void onError(Throwable e) {
                XActivityindicator.hide();
            }

            @Override
            public void onSuccess(HttpResult<List<UserModel>> listHttpResult) {
                XActivityindicator.hide();
                if(listHttpResult.getData().getCode() == 1)
                {
                    String header = otheruser.getUserIcon();
                    String nick = otheruser.getUserName();
                    String openid = otheruser.getUserId();
                    String ptype = "";
                    switch (type)
                    {
                        case "1":
                            ptype = "新浪微博";
                            break;
                        case "2":
                            ptype = "微信";
                            break;
                        case "3":
                            ptype = "QQ";
                            break;
                        default:
                            break;
                    }

                    Intent intent = new Intent();
                    intent.setClass(Logn.this, XHtmlVC.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("url","file:///android_asset/unitLogin.html?header="+header+"&type="+ptype+"&nick="+nick+"&openid="+openid);
                    bundle.putString("title","联合登录");
                    intent.putExtras(bundle);
                    Logn.this.startActivity(intent);

                }
                else
                {
                    UserModel user = listHttpResult.getData().getInfo().get(0);

                    APPDataCache.User = user;
                    APPDataCache.User.save();

                    int sex = 0;
                    int houseid = 0;

                    try
                    {
                        sex = Integer.parseInt(user.getSex());

                    }
                    catch (Exception e)
                    {
                        sex = 0;
                    }

                    try
                    {
                        houseid = Integer.parseInt(user.getHouseid());

                    }
                    catch (Exception e)
                    {
                        houseid = 0;
                    }

                    setAccount(user.getUid());

                    APPDataCache.User.registNotice();
                    APPDataCache.User.getUser();
                    APPDataCache.User.getMsgCount();
                    APPDataCache.land = 1;

                    Intent intent = new Intent();
                    intent.setAction("com.servicedemo4");
                    intent.putExtra("getmeeage", "0");
                    Logn.this.sendBroadcast(intent);
                    Toast.makeText(Logn.this, "登陆成功", Toast.LENGTH_SHORT).show();
                    XNotificationCenter.getInstance().postNotice("UserChanged",null);
                    finish();
                }

            }
        });


    }





    @Override
    public void onItemClick(Object o, int position) {
        if (o == mAlertViewExt && position != AlertView.CANCELPOSITION) {
            String name = etName.getText().toString();
            String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(name);
            if( m.find()){
                Toast.makeText(this, "昵称不允许输入特殊符号！", Toast.LENGTH_SHORT).show();
                return;
            }

            if (name.isEmpty()) {
                Toast.makeText(this, "昵称不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            if(name.length() < 3 || name.length() > 12)
            {
                Toast.makeText(this, "昵称长度为3-12位!", Toast.LENGTH_SHORT).show();
                return;
            }

            nickname = name;
            try {
                dastr = "&openid=" + userid + "&nickname=" + URLEncoder.encode(nickname, "UTF-8") + "&sex=" + sex + "&headimage=" + headimage;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            getUser(2);

            return;
        }

    }

    @Override
    public void onDismiss(Object o) {
        finish();
//        Toast.makeText()
    }

    private void setAccount(String username) {
        pushService.bindAccount(username, new CommonCallback() {
            @Override
            public void onSuccess(String s) {
                System.out.println("setAccount:onSuccess");
            }

            @Override
            public void onFailed(String s, String s1) {
                System.out.println("setAccount:onFailed");
            }
        });
    }


    @Subscribe
    public void getEventmsg(MyEventBus myEventBus) {
        if (myEventBus.getMsg().equals("LoginSuccess")) {
            finish();
        }
    }


}
