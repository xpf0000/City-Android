package citycircle.com.user;

import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import citycircle.com.Activity.Logn;
import citycircle.com.R;
import citycircle.com.Utils.Emailtest;
import citycircle.com.Utils.MyEventBus;
import model.UserModel;
import util.BaseActivity;
import util.XAPPUtil;
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

        nickET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkNickName();
            }
        });
    }

    private boolean nickhased = false;
    private void checkNickName()
    {
        if(nickET.getText().toString().length() < 2 || nickET.getText().toString().length() > 12)
        {
            nickET.setTextColor(Color.BLACK);
            nickhased = false;
            return;
        }

        nickhased = true;
        XNetUtil.Handle(APPService.userGetOrNickname(nickET.getText().toString()), null, "昵称已存在,请更换", new XNetUtil.OnHttpResult<Boolean>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                nickhased = !aBoolean;
                if(nickhased)
                {
                    Toast.makeText(AuthBandPhoneVC.this, "昵称已存在,请更换", Toast.LENGTH_SHORT).show();
                    nickET.setTextColor(Color.RED);
                }
                else
                {
                    nickET.setTextColor(Color.BLACK);
                }

            }
        });
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
        if(nickhased)
        {
            Toast.makeText(AuthBandPhoneVC.this, "昵称已存在,请更换", Toast.LENGTH_SHORT).show();
            return;
        }

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
            Toast.makeText(this, "用户信息已过期,请重新获取", Toast.LENGTH_SHORT).show();
            return;
        }

        if(nickhased)
        {
            Toast.makeText(this, "昵称已存在,请更换", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "昵称不允许输入特殊符号！", Toast.LENGTH_SHORT).show();
            return;
        }

        if (nickname.isEmpty() || tel.isEmpty() || code.isEmpty() || pass1.isEmpty() || pass2.isEmpty()) {
            Toast.makeText(this, "请完善注册信息", Toast.LENGTH_SHORT).show();
            return;
        }

        if(nickname.length() < 2 || nickname.length() > 12)
        {
            Toast.makeText(this, "昵称长度为2-12位!", Toast.LENGTH_SHORT).show();
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

                if(userModels.size() > 0)
                {
                    XActivityindicator.hide();
                    UserModel user = userModels.get(0);

                    setAccount(user.getUid());

                    APPDataCache.User = user;
                    APPDataCache.User.save();
                    APPDataCache.land = 1;
                    APPDataCache.User.registNotice();
                    APPDataCache.User.getUser();
                    APPDataCache.User.getMsgCount();

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
