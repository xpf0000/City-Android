package citycircle.com.OA;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import citycircle.com.MyViews.MyDialog;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.MyhttpRequest;
import citycircle.com.Utils.PreferencesUtils;

/**
 * Created by admins on 2016/1/5.
 */
public class LandActivity extends Activity {
    private EditText username, password;
    private Button login;
    ImageView loginback;
    Object object;
    String url, datastr;
    Dialog dialog;
    int type=0;
//    PushAgent mPushAgent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.land);
//        mPushAgent = PushAgent.getInstance(this);
//        mPushAgent.enable();
//        PushAgent.getInstance(this).onAppStart();
        type=getIntent().getIntExtra("type",0);
        dialog = MyDialog.createLoadingDialog(LandActivity.this, "正在登陆...");
        url = GlobalVariables.oaurlstr + "User.login";
        intview();
    }

    private void intview() {
        username = (EditText) this.findViewById(R.id.login_name);
        password = (EditText) this.findViewById(R.id.login_powd);
        login = (Button) this.findViewById(R.id.login);
        loginback = (ImageView) this.findViewById(R.id.loginback);
        loginback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().trim().length() == 0) {
                    Toast.makeText(LandActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                } else if (password.getText().toString().trim().length() == 0) {
                    Toast.makeText(LandActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.show();
                    String usernames = username.getText().toString().trim();
                    String passwords = password.getText().toString().trim();
                    datastr = "username=" + usernames + "&password=" + passwords;
                    getUserinfo();
                }
            }
        });
    }

    private void getUserinfo() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                MyhttpRequest myhttpRequest = new MyhttpRequest();
                object = myhttpRequest.request(url, datastr, "POST");
                if (object == null) {
                    handler.sendEmptyMessage(2);
                } else {
                    handler.sendEmptyMessage(1);
                }
            }
        }.start();
    }
//    void settages(final String[] tags) {
//        new Thread() {
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//                super.run();
//                TagManager.Result result = null;
//                try {
//                   String list= mPushAgent.getTagManager().reset().toString();
//                    result = mPushAgent.getTagManager().add(tags);
//                    System.out.println(result);
//                } catch (Exception e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//
//            }
//        }.start();
//
//    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    dialog.dismiss();
                    String str=object.toString();
                    JSONObject jsonObject= JSON.parseObject(str);
                    JSONObject jsonObject1=jsonObject.getJSONObject("data");
                    int a=jsonObject1.getIntValue("code");
                    if (a==0){
                        JSONArray jsonArray=jsonObject1.getJSONArray("info");
                        for (int i=0;i<jsonArray.size();i++){
                            JSONObject jsonObject2=jsonArray.getJSONObject(i);
                            PreferencesUtils.putString(LandActivity.this,"oauid",jsonObject2.getString("uid"));
                            PreferencesUtils.putString(LandActivity.this,"oatruename",jsonObject2.getString("truename"));
                            PreferencesUtils.putInt(LandActivity.this, "oasex", jsonObject2.getIntValue("sex"));
                            PreferencesUtils.putString(LandActivity.this, "oausername", jsonObject2.getString("username"));
                            PreferencesUtils.putString(LandActivity.this,"dwid",jsonObject2.getString("dwid"));
                            PreferencesUtils.putString(LandActivity.this,"bmid",jsonObject2.getString("bmid"));
                            PreferencesUtils.putString(LandActivity.this,"dw",jsonObject2.getString("dw"));
                            PreferencesUtils.putString(LandActivity.this,"bm",jsonObject2.getString("bm"));
                            PreferencesUtils.putString(LandActivity.this,"oatel",jsonObject2.getString("tel"));
                            PreferencesUtils.putString(LandActivity.this,"oamobile",jsonObject2.getString("mobile"));
                            PreferencesUtils.putString(LandActivity.this,"address",jsonObject2.getString("address"));
                            PreferencesUtils.putString(LandActivity.this,"qq",jsonObject2.getString("qq"));
                            PreferencesUtils.putString(LandActivity.this,"email",jsonObject2.getString("email"));
                            PreferencesUtils.putString(LandActivity.this,"jgid",jsonObject2.getString("jgid"));
                            final String[] tags;
                            tags = new String[] { jsonObject2.getString("jgid"), jsonObject2.getString("dwid"),
                                    jsonObject2.getString("bmid"),jsonObject2.getString("uid") };
//                            settages(tags);
                            GlobalVariables.tags=tags;
                        }
                        PreferencesUtils.putInt(LandActivity.this, "oaland", 1);
                        Toast.makeText(LandActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();


                        if (type==0){
                            finish();
                        }else {
                            Intent intent=new Intent();
                            intent.setClass(LandActivity.this,HomePageActivity.class);
                            LandActivity.this.startActivity(intent);
                            finish();
                        }
                    }else {
                        Toast.makeText(LandActivity.this,jsonObject1.getString("msg"),Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    dialog.dismiss();
                    Toast.makeText(LandActivity.this,"网络似乎有问题了",Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    dialog.dismiss();
                    Toast.makeText(LandActivity.this,"服务器出现问题",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
