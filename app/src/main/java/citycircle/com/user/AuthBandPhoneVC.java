package citycircle.com.user;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import citycircle.com.Activity.Logn;
import citycircle.com.Activity.UpPasswords;
import citycircle.com.R;
import citycircle.com.Utils.Emailtest;
import citycircle.com.Utils.MyEventBus;
import citycircle.com.Utils.PreferencesUtils;
import model.UserModel;
import util.BaseActivity;
import util.XActivityindicator;
import util.XNetUtil;

import static citycircle.com.MyAppService.LocationApplication.APPDataCache;
import static citycircle.com.MyAppService.LocationApplication.APPService;

/**
 * Created by X on 2016/12/19.
 */

public class AuthBandPhoneVC extends BaseActivity {

    EditText nickET,phoneET,codeET,pass1ET,pass2ET;
    Button  getcode;
    TimerTask task;
    private int recLen = 60;
    Timer timer = new Timer();

    private String type;

    @Override
    protected void setupUi() {
        setContentView(R.layout.authbandphone);
        setPageTitle("快速注册");

        nickET = (EditText)findViewById(R.id.nickname);
        phoneET = (EditText)findViewById(R.id.number);
        codeET = (EditText)findViewById(R.id.code);
        pass1ET = (EditText)findViewById(R.id.pass1);
        pass2ET = (EditText)findViewById(R.id.pass2);
        getcode = (Button)findViewById(R.id.getcode);

        type = getIntent().getStringExtra("type");
    }

    public void gettime() {
        task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recLen--;
                        getcode.setText(recLen + "后再获取");
                        if (recLen < 0) {
                            getcode.setText("获取验证码");
                            recLen = 60;
                            task.cancel();
                            getcode.setClickable(true);
                        }
                    }
                });
            }
        };
        timer.schedule(task, 1000, 1000);
    }

    @Override
    protected void setupData() {

    }


    public void getCode(View v)
    {
        String tel = phoneET.getText().toString().trim();
        if (tel.isEmpty()) {
            Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return;
        }

        Emailtest emtest = new Emailtest();

        if (!emtest.checkphone(tel)) {
            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();

            return;
        }


        XActivityindicator.create(mContext).show();
        XNetUtil.Handle(APPService.userSmsSend(tel, "1"), "验证码发送成功", "验证码发送失败", new XNetUtil.OnHttpResult<Boolean>() {
            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                if(aBoolean)
                {
                    getcode.setClickable(false);
                    gettime();
                }
            }
        });
    }

    public void submit(View v)
    {
        if(Logn.otheruser == null)
        {
            Toast.makeText(this, "用户信息已过期,请重新获取", Toast.LENGTH_LONG).show();
            return;
        }

        String sex = "";
        if (Logn.otheruser.getUserGender().equals("m")) {
                sex = "0";
            } else {
                sex = "1";
            }

        String openid = Logn.otheruser.getUserId();
        String headimage = Logn.otheruser.getUserIcon();

        String nickname = nickET.getText().toString().trim();
        String tel = phoneET.getText().toString().trim();
        String code = codeET.getText().toString().trim();
        String pass1 = pass1ET.getText().toString().trim();
        String pass2 = pass1ET.getText().toString().trim();

        String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(nickname);
        if( m.find()){
            Toast.makeText(this, "昵称不允许输入特殊符号！", Toast.LENGTH_LONG).show();
            return;
        }

        if (nickname.isEmpty() || tel.isEmpty() || code.isEmpty() || pass1.isEmpty() || pass2.isEmpty()) {
            Toast.makeText(this, "请完善注册信息", Toast.LENGTH_SHORT).show();
            return;
        }

        if(nickname.length() < 3 || nickname.length() > 12)
        {
            Toast.makeText(this, "昵称长度为3-12位!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!pass1.equals(pass2))
        {
            Toast.makeText(this, "密码和确认密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }

        XActivityindicator.create(mContext).show();

        XNetUtil.Handle(APPService.userOpenRegister(openid,type,nickname,sex,headimage,tel,pass1,code), new XNetUtil.OnHttpResult<List<UserModel>>() {
            @Override
            public void onError(Throwable e) {
                XActivityindicator.hide();
            }

            @Override
            public void onSuccess(List<UserModel> userModels) {
                XActivityindicator.hide();
                if(userModels.size() > 0)
                {
                    UserModel user = userModels.get(0);
                    APPDataCache.User.copy(user);

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

                    PreferencesUtils.putString(AuthBandPhoneVC.this, "userid", user.getUid());
                    PreferencesUtils.putString(AuthBandPhoneVC.this, "username", user.getUsername());
                    setAccount(user.getUid());
                    PreferencesUtils.putString(AuthBandPhoneVC.this, "nickname", user.getNickname());
                    PreferencesUtils.putString(AuthBandPhoneVC.this, "headimage", user.getHeadimage());
                    PreferencesUtils.putString(AuthBandPhoneVC.this, "mobile", user.getMobile());
                    PreferencesUtils.putInt(AuthBandPhoneVC.this, "sex", sex);
                    PreferencesUtils.putInt(AuthBandPhoneVC.this, "houseid", houseid);
                    PreferencesUtils.putString(AuthBandPhoneVC.this, "houseids", user.getHouseid());
                    PreferencesUtils.putString(AuthBandPhoneVC.this, "fanghaoid", user.getFanghaoid());
                    PreferencesUtils.putString(AuthBandPhoneVC.this, "truename", user.getTruename());
                    PreferencesUtils.putString(AuthBandPhoneVC.this, "birthday", user.getBirthday());
                    PreferencesUtils.putString(AuthBandPhoneVC.this, "address", user.getAddress());

                    APPDataCache.User.registNotice();
                    APPDataCache.User.getUser();
                    APPDataCache.User.getMsgCount();

                    PreferencesUtils.putInt(AuthBandPhoneVC.this, "land", 1);
                    Intent intent = new Intent();
                    intent.setAction("com.servicedemo4");
                    intent.putExtra("getmeeage", "0");
                    AuthBandPhoneVC.this.sendBroadcast(intent);
                    Toast.makeText(AuthBandPhoneVC.this, "登陆成功", Toast.LENGTH_SHORT).show();

                    EventBus.getDefault().post(
                            new MyEventBus("LoginSuccess"));

                    finish();
                }

            }
        });

    }


    private void setAccount(String username) {
        PushServiceFactory.getCloudPushService().bindAccount(username, new CommonCallback() {
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
