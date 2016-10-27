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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import citycircle.com.MyViews.MyDialog;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;
import citycircle.com.Utils.MyhttpRequest;
import citycircle.com.Utils.PreferencesUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;

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
        ShareSDK.initSDK(this);
        setContentView(R.layout.logn);
        pushService = PushServiceFactory.getCloudPushService();
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        dialog = MyDialog.createLoadingDialog(Logn.this, "正在登陆...");
        url = GlobalVariables.urlstr + "User.login";
        thredurls = GlobalVariables.urlstr + "User.openLogin&openid=" + userid;
        openRegister = GlobalVariables.urlstr + "User.openRegister";
//                + "&openid=" + userid + "&nickname=" + nickname + "&sex=" + sex + "&headimage=" + headimage;
        intview();
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
                            PreferencesUtils.putString(Logn.this, "openid", jsonObject2.getString("openid"));
                            PreferencesUtils.putString(Logn.this, "userid", jsonObject2.getString("uid"));
                            PreferencesUtils.putString(Logn.this, "username", jsonObject2.getString("username"));
                            setAccount(jsonObject2.getString("uid"));
                            PreferencesUtils.putString(Logn.this, "nickname", jsonObject2.getString("nickname"));
                            PreferencesUtils.putString(Logn.this, "headimage", jsonObject2.getString("headimage"));
                            PreferencesUtils.putString(Logn.this, "mobile", jsonObject2.getString("mobile"));
                            PreferencesUtils.putInt(Logn.this, "sex", jsonObject2.getIntValue("sex"));
                            PreferencesUtils.putString(Logn.this, "houseid", jsonObject2.getString("houseid"));
                            PreferencesUtils.putString(Logn.this, "houseids", jsonObject2.getString("houseid"));
                            PreferencesUtils.putString(Logn.this, "fanghaoid", jsonObject2.getString("fanghaoid"));
                            PreferencesUtils.putString(Logn.this, "truename", jsonObject2.getString("truename"));
                            PreferencesUtils.putString(Logn.this, "birthday", jsonObject2.getString("birthday"));
                            PreferencesUtils.putString(Logn.this, "address", jsonObject2.getString("address"));
                        }
                        PreferencesUtils.putInt(Logn.this, "land", 1);
                        Intent intent = new Intent();
                        intent.setAction("com.servicedemo4");
                        intent.putExtra("getmeeage", "0");
                        Logn.this.sendBroadcast(intent);
                        Toast.makeText(Logn.this, "登陆成功", Toast.LENGTH_SHORT).show();

                        finish();
                    } else {
                        handler.sendEmptyMessage(3);
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
                    if (jsonObject3.getIntValue("code") == 0) {
                        urlstr = therdstr;
                        handler.sendEmptyMessage(1);

                    } else {
                        mAlertViewExt.show();
//                        openRegister = GlobalVariables.urlstr + "User.openRegister&openid=" + userid + "&nickname=" + nickname + "&sex=" + sex + "&headimage=" + headimage;

                    }
                    break;
                case 5:
                    String str = openRegisterstr.toString();
                    JSONObject jsonObject4 = JSON.parseObject(str);
                    JSONObject jsonObject5 = jsonObject4.getJSONObject("data");
                    int b = jsonObject5.getIntValue("code");
                    if (b == 0) {
                        JSONArray jsonArray = jsonObject5.getJSONArray("info");
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject jsonObject6 = jsonArray.getJSONObject(i);
                            PreferencesUtils.putString(Logn.this, "userid", jsonObject6.getString("uid"));
                            PreferencesUtils.putString(Logn.this, "openid", jsonObject6.getString("openid"));
                            PreferencesUtils.putString(Logn.this, "username", jsonObject6.getString("username"));
                            setAccount(jsonObject6.getString("uid"));
                            PreferencesUtils.putString(Logn.this, "nickname", jsonObject6.getString("nickname"));
                            PreferencesUtils.putString(Logn.this, "headimage", jsonObject6.getString("headimage"));
                            PreferencesUtils.putString(Logn.this, "mobile", jsonObject6.getString("mobile"));
                            PreferencesUtils.putInt(Logn.this, "sex", jsonObject6.getIntValue("sex"));
                            PreferencesUtils.putInt(Logn.this, "houseid", jsonObject6.getIntValue("houseid"));
                            PreferencesUtils.putString(Logn.this, "houseids", jsonObject6.getString("houseid"));
                            PreferencesUtils.putString(Logn.this, "fanghaoid", jsonObject6.getString("fanghaoid"));
                            PreferencesUtils.putString(Logn.this, "truename", jsonObject6.getString("truename"));
                            PreferencesUtils.putString(Logn.this, "birthday", jsonObject6.getString("birthday"));
                            PreferencesUtils.putString(Logn.this, "address", jsonObject6.getString("address"));
                        }
                        PreferencesUtils.putInt(Logn.this, "land", 1);
                        Intent intent = new Intent();
                        intent.setAction("com.servicedemo4");
                        intent.putExtra("getmeeage", "0");
                        Logn.this.sendBroadcast(intent);
                        Toast.makeText(Logn.this, "登陆成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        handler.sendEmptyMessage(3);
                    }
                    break;
            }
        }
    };

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
                a = 1;
                Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
                sina.setPlatformActionListener(this);
                sina.showUser(null);//执行登录，登录后在回调里面获取用户资料
//                authorize(sina);
                break;
            case R.id.qq_login_icon:
                a = 2;
                Platform qzone = ShareSDK.getPlatform(QZone.NAME);
//                authorize(qzone);
                qzone.setPlatformActionListener(this);
                qzone.showUser(null);//执行登录，登录后在回调里面获取用户资料
                break;
            case R.id.my_login_wx3x:
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
            userid = platform.getDb().getUserId();
            nickname = platform.getDb().getUserName();
            if (platform.getDb().getUserGender().equals("m")) {
                sex = "0";
            } else {
                sex = "1";
            }
            headimage = platform.getDb().getUserIcon();
            Message msg = new Message();
            msg.what = MSG_AUTH_COMPLETE;
            msg.obj = new Object[]{platform.getName(), hashMap};
            UIHandler.sendEmptyMessage(MSG_AUTH_COMPLETE, this);
        }
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        if (i == Platform.ACTION_USER_INFOR) {
//            handler.sendEmptyMessage(MSG_AUTH_ERROR);
            UIHandler.sendEmptyMessage(MSG_AUTH_ERROR, this);
        }
        throwable.printStackTrace();
    }

    @Override
    public void onCancel(Platform platform, int i) {
        if (i == Platform.ACTION_USER_INFOR) {
//            handler.sendEmptyMessage(MSG_AUTH_CANCEL);
            UIHandler.sendEmptyMessage(MSG_AUTH_CANCEL, this);
        }
    }

    @Override
    public void onItemClick(Object o, int position) {
        if (o == mAlertViewExt && position != AlertView.CANCELPOSITION) {
            String name = etName.getText().toString();
            if (name.isEmpty()) {
                Toast.makeText(this, "昵称不能为空", Toast.LENGTH_SHORT).show();
            } else {
                nickname = name;
                try {
                    dastr = "&openid=" + userid + "&nickname=" + URLEncoder.encode(nickname, "UTF-8") + "&sex=" + sex + "&headimage=" + headimage;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                getUser(2);
            }
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
}
