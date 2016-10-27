package citycircle.com.OA;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import citycircle.com.MyViews.MyDialog;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;
import citycircle.com.Utils.PreferencesUtils;

/**
 * Created by admins on 2016/1/8.
 */
public class UodatePassword extends Activity {
    private EditText updatepowd_powd, updatepowd_newpowd, updatepowd_sureword;
    ImageView addmessaget,messagetback;
    String url, urlstr;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updapass);
        dialog = MyDialog.createLoadingDialog(this, "正在提交..");
        intview();
    }

    private void intview() {
        messagetback=(ImageView)findViewById(R.id.messagetback);
        addmessaget = (ImageView) findViewById(R.id.addmessaget);
        updatepowd_powd = (EditText) this.findViewById(R.id.updatepowd_powd);
        updatepowd_newpowd = (EditText) this
                .findViewById(R.id.updatepowd_newpowd);
        updatepowd_sureword = (EditText) this
                .findViewById(R.id.updatepowd_sureword);
        messagetback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addmessaget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!updatepowd_newpowd.getText().toString().trim()
                        .equals(updatepowd_sureword.getText().toString().trim())) {
                    Toast.makeText(UodatePassword.this, "两次密码不统一",
                            Toast.LENGTH_SHORT).show();
                } else if (updatepowd_newpowd.getText().toString().trim().equals("")
                        || updatepowd_newpowd.getText().toString().trim() == "null") {
                    Toast.makeText(UodatePassword.this, "请输入新密码",
                            Toast.LENGTH_SHORT).show();

                } else {
                    dialog.show();
                    String mypowd = updatepowd_powd.getText().toString();
                    String mynewpowd = updatepowd_newpowd.getText().toString();
                    String username = PreferencesUtils.getString(UodatePassword.this, "oausername");
                    url = GlobalVariables.oaurlstr + "User.updatePass&username=" + username + "&password=" + mypowd + "&newpassword=" + mynewpowd;
                    getstr();

                }
            }
        });

    }

    private void getstr() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                HttpRequest httpRequest = new HttpRequest();
                urlstr = httpRequest.doGet(url);
                if (urlstr.equals("网络超时")) {
                    handler.sendEmptyMessage(2);
                } else {
                    handler.sendEmptyMessage(1);
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
                    if (jsonObject1.getIntValue("code") == 0) {
                        Toast.makeText(UodatePassword.this, "修改成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(UodatePassword.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    dialog.dismiss();
                    Toast.makeText(UodatePassword.this, "网络似乎有问题了", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    break;
            }
        }
    };
}
