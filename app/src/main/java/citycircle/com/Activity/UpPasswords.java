package citycircle.com.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import citycircle.com.R;
import citycircle.com.Utils.Emailtest;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;

/**
 * Created by admins on 2015/12/4.
 */
public class UpPasswords extends Activity implements View.OnClickListener {
    EditText password, number, code, surepassword;
    String url, urlstr, numbers, smurl, smurlstr;
    Button submit, getcode;
    ImageView back;
    Emailtest emtest;
    private int recLen = 60;
    Timer timer = new Timer();
    TimerTask task;
    int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uppass);
        url = GlobalVariables.urlstr + "User.updatePass";
        emtest = new Emailtest();
        intview();
    }

    public void intview() {
        back = (ImageView) findViewById(R.id.back);
        password = (EditText) findViewById(R.id.password);
        number = (EditText) findViewById(R.id.number);
        code = (EditText) findViewById(R.id.code);
        surepassword = (EditText) findViewById(R.id.surepassword);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);
        getcode = (Button) findViewById(R.id.getcode);
        getcode.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    public void getstr(final int type) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                HttpRequest httpRequest = new HttpRequest();
                if (type == 0) {
                    smurlstr = httpRequest.doGet(smurl);
                    if (smurlstr.equals("网络超时")) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(3);
                    }
                } else {
                    urlstr = httpRequest.doGet(url);
                    if (urlstr.equals("网络超时")) {
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
                    JSONObject jsonObject = JSON.parseObject(urlstr);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    if (jsonObject1.getIntValue("code") == 0) {
                        Toast.makeText(UpPasswords.this, "修改成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(UpPasswords.this, "修改失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    Toast.makeText(UpPasswords.this, "网络似乎有问题了", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    JSONObject jsonObject3 = JSON.parseObject(smurlstr);
                    JSONObject jsonObject2 = jsonObject3.getJSONObject("data");
                    int b = jsonObject2.getIntValue("code");
                    if (b == 0) {
                        Toast.makeText(UpPasswords.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(UpPasswords.this, "验证码发送失败", Toast.LENGTH_SHORT).show();
                    }
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.submit:
                if (!(numbers.equals(number.getText().toString().trim()))) {
                    Toast.makeText(UpPasswords.this, "与验证手机不符", Toast.LENGTH_SHORT).show();
                } else if (code.getText().toString().trim().length() == 0) {
                    Toast.makeText(UpPasswords.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                } else if (password.getText().toString().trim().length() == 0) {
                    Toast.makeText(UpPasswords.this, "请输入新密码", Toast.LENGTH_SHORT).show();
                } else if (!password.getText().toString().trim().equals(surepassword.getText().toString().trim())) {
                    Toast.makeText(UpPasswords.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                } else {
                    url = GlobalVariables.urlstr + "User.updatePass&mobile=" + number.getText().toString().trim() + "&password=" + password.getText().toString() + "&code=" + code.getText().toString();
                    getstr(1);
                }
                break;
            case R.id.getcode:
                if (number.getText().toString().length() == 0) {
                    Toast.makeText(UpPasswords.this, "请输入手机号", Toast.LENGTH_SHORT).show();
                } else if (!emtest.checkphone(number.getText().toString().trim())) {
                    Toast.makeText(UpPasswords.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                } else {
                    numbers = number.getText().toString().trim();
                    smurl = GlobalVariables.urlstr + "User.smsSend&mobile=" + number.getText().toString().trim() + "&type=2";
                    getstr(0);
                    gettime();
                }
                break;
        }
    }
}
