package citycircle.com.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import citycircle.com.R;
import citycircle.com.Utils.Emailtest;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;
import citycircle.com.Utils.MyEventBus;
import citycircle.com.Utils.MyhttpRequest;
import model.UserModel;

import static citycircle.com.MyAppService.LocationApplication.APPDataCache;

/**
 * Created by admins on 2015/11/27.
 */
public class Register extends Activity implements View.OnClickListener {
    Button submit, getcode;
    ImageView back;
    EditText surepassword, password, name;
    String numbesr, datastr, url, urlstr;
    private int recLen = 60;
    Timer timer = new Timer();
    CheckBox xieyi;
    Emailtest emtest;
    TimerTask task;
    String smurl, smurlstr, codes, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        emtest = new Emailtest();
        codes = getIntent().getStringExtra("code");
        username = getIntent().getStringExtra("number");
        intview();
        url = GlobalVariables.urlstr + "User.register";
        smurl = GlobalVariables.urlstr + "User.smsSend";
    }

    public void intview() {
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        password = (EditText) findViewById(R.id.password);
        surepassword = (EditText) findViewById(R.id.surepassword);
        name = (EditText) findViewById(R.id.name);
        xieyi = (CheckBox) findViewById(R.id.xieyi);
    }

    public void getStr(final int type) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                if (type == 0) {
                    MyhttpRequest myhttp = new MyhttpRequest();
                    Object object = myhttp.request(url, datastr, "POST");
                    if (object == null) {
                        handler.sendEmptyMessage(2);
                    } else {
                        urlstr = object.toString();
                        handler.sendEmptyMessage(4);
                    }
                } else {
                    HttpRequest httpRequest = new HttpRequest();
                    smurlstr = httpRequest.doGet(smurl);
                    if (smurlstr.equals("网络超时")) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(1);
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
                    JSONObject jsonObject3 = JSON.parseObject(smurlstr);
                    JSONObject jsonObject1 = jsonObject3.getJSONObject("data");
                    int b = jsonObject1.getIntValue("code");
                    if (b == 0) {
                        Toast.makeText(Register.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Register.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    Toast.makeText(Register.this, "网络超时", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(Register.this, "验证成功", Toast.LENGTH_SHORT).show();

                    break;
                case 4:
                    JSONObject json = JSON.parseObject(urlstr);
                    JSONObject jsonob = json.getJSONObject("data");
                    int a = jsonob.getIntValue("code");
                    if (a == 0) {
                        JSONArray jsonobj = jsonob.getJSONArray("info");
                        for (int i = 0; i < jsonobj.size(); i++) {
                            JSONObject jsonObject = jsonobj.getJSONObject(i);

                            UserModel user = JSON.parseObject(jsonObject.toJSONString(), UserModel.class);

                            APPDataCache.User = user;
                            APPDataCache.User.save();
                            APPDataCache.User.registNotice();
                            APPDataCache.User.getUser();
                            APPDataCache.User.getMsgCount();
                            APPDataCache.land = 1;

                        }
                        Intent intent = new Intent();
                        intent.setAction("com.servicedemo4");
                        intent.putExtra("getmeeage", "0");
                        Register.this.sendBroadcast(intent);
                        Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();

                        EventBus.getDefault().post(
                                new MyEventBus("LoginSuccess"));

                        finish();
                    } else {
                        Toast.makeText(Register.this, jsonob.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 5:
                    Toast.makeText(Register.this, "验证码错误", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

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
    protected void onDestroy() {
        super.onDestroy();
//        unregisterEventHandler(eh);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.submit:

                String nick = name.getText().toString();

                if(nick.length() < 2 || nick.length() > 12)
                {
                    Toast.makeText(this, "昵称长度为2-12位!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(nick);
                if( m.find()){
                    Toast.makeText(this, "昵称不允许输入特殊符号！", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (name.getText().toString().trim().length() == 0) {
                    Toast.makeText(Register.this, "请输入昵称！", Toast.LENGTH_SHORT).show();
                } else if (password.getText().toString().trim().length() == 0) {
                    Toast.makeText(Register.this, "请输入密码！", Toast.LENGTH_SHORT).show();
                } else if (!password.getText().toString().trim().equals(surepassword.getText().toString().trim())) {
                    Toast.makeText(Register.this, "密码与缺人密码不一致！", Toast.LENGTH_SHORT).show();
                } else {
                    String nickname = name.getText().toString();
                    String passwords = password.getText().toString();
                    try {
                        datastr = "mobile=" + username + "&nickname=" + URLEncoder.encode(nickname, "UTF-8") + "&password=" + passwords + "&code=" + codes;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    getStr(0);
                }

                break;
        }
    }


}
