package citycircle.com.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import java.io.File;
import java.text.DecimalFormat;

import citycircle.com.MyViews.CheckSwitchButton;
import citycircle.com.R;
import citycircle.com.Utils.PreferencesUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by admins on 2015/11/20.
 */
public class SetActivity extends Activity {
    CheckSwitchButton mPush;
    Button logout;
    ImageView back;
    LinearLayout update,cleaner,about,free;
    File cacheDir;
    String dd;
    TextView shuliang;
    CloudPushService pushService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setlayout);
        ShareSDK.initSDK(this);
        pushService = PushServiceFactory.getCloudPushService();
        File sd = Environment.getExternalStorageDirectory();
        String path = sd.getPath() + "/citycircle/Cache";
        cacheDir = new File(path);
        try {
            getFolderSize(cacheDir);
            DecimalFormat fnum = new DecimalFormat("##0.0");
            dd = fnum.format(getFolderSize(cacheDir));

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        intview();

    }

    public void intview() {
        shuliang=(TextView)findViewById(R.id.shuliang);
        free=(LinearLayout)findViewById(R.id.free);
        about=(LinearLayout)findViewById(R.id.about);
        cleaner=(LinearLayout)findViewById(R.id.cleaner);
        update=(LinearLayout)findViewById(R.id.update);
        back=(ImageView)findViewById(R.id.back);
        logout=(Button)findViewById(R.id.logout);
        shuliang.setText(dd+"M");
        free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(SetActivity.this,FreeBack.class);
                SetActivity.this.startActivity(intent);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(SetActivity.this,About.class);
                SetActivity.this.startActivity(intent);
            }
        });
        cleaner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getFolderSize(cacheDir);
                    DecimalFormat fnum = new DecimalFormat("##0.0");
                    dd = fnum.format(getFolderSize(cacheDir));

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                delete(cacheDir);
                shuliang.setText(0.0+"M");
                Toast.makeText(SetActivity.this, "清理成功", Toast.LENGTH_SHORT)
                        .show();
            }
        });
//        mCheckSwithcButton = (CheckSwitchButton) findViewById(R.id.mCheckSwithcButton);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UmengUpdateAgent.setUpdateOnlyWifi(false);
                UmengUpdateAgent.setUpdateAutoPopup(false);
                UmengUpdateAgent
                        .setUpdateListener(new UmengUpdateListener() {

                            @Override
                            public void onUpdateReturned(int arg0,
                                                         UpdateResponse arg1) {
                                switch (arg0) {
                                    case UpdateStatus.Yes: // has update
                                        UmengUpdateAgent.showUpdateDialog(
                                                SetActivity.this, arg1);
                                        break;
                                    case UpdateStatus.No: // has no update
                                        Toast.makeText(SetActivity.this, "没有更新",
                                                Toast.LENGTH_SHORT).show();
                                        break;
                                    case UpdateStatus.NoneWifi: // none wifi
                                        Toast.makeText(SetActivity.this,
                                                "没有wifi连接， 只在wifi下更新",
                                                Toast.LENGTH_SHORT).show();
                                        break;
                                    case UpdateStatus.Timeout: // time out
                                        Toast.makeText(SetActivity.this, "超时",
                                                Toast.LENGTH_SHORT).show();
                                        break;
                                }

                            }
                        });
                UmengUpdateAgent.update(SetActivity.this);
            }
        });
        mPush = (CheckSwitchButton) findViewById(R.id.mPush);
        int b = PreferencesUtils.getInt(SetActivity.this, "photo");
        if (b == 2) {
            mPush.setChecked(false);
        } else {
            mPush.setChecked(true);
        }
        int a = PreferencesUtils.getInt(SetActivity.this, "land");
        Intent intent = new Intent();
        if (a == 0) {
            logout.setVisibility(View.GONE);
        } else {
            logout.setVisibility(View.VISIBLE);
        }
        mPush.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    PreferencesUtils.putInt(SetActivity.this, "photo", 1);
//                    Toast.makeText(SetActivity.this, "推送已关闭", Toast.LENGTH_SHORT)
//                            .show();
                    ;
                } else {
                    PreferencesUtils.putInt(SetActivity.this, "photo", 2);
//                    Toast.makeText(SetActivity.this, "推送打开", Toast.LENGTH_SHORT)
//                            .show();
                    ;
                }
            }
        });
//        mCheckSwithcButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    PreferencesUtils.putInt(SetActivity.this, "photo", 1);
//                    Toast.makeText(SetActivity.this, "无图模式已关闭", Toast.LENGTH_SHORT)
//                            .show();
//                    ;
//                } else {
//                    PreferencesUtils.putInt(SetActivity.this, "photo", 2);
//                    Toast.makeText(SetActivity.this, "无图模式已打开", Toast.LENGTH_SHORT)
//                            .show();
//                    ;
//                }
//            }
//        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                PreferencesUtils.putInt(SetActivity.this, "land", 0);
                PreferencesUtils.putString(SetActivity.this, "userid", null);
                Intent intent = new Intent();
                intent.setAction("com.servicedemo4");
                intent.putExtra("getmeeage", "1");
                SetActivity.this.sendBroadcast(intent);
                Toast.makeText(SetActivity.this, "退出登录", Toast.LENGTH_SHORT).show();
                finish();
                pushService.unbindAccount(new CommonCallback() {
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
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public static float getFolderSize(File file) throws Exception {
        float size = 0;
        File[] fileList = file.listFiles();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory()) {
                size = size + getFolderSize(fileList[i]);
            } else {
                size = size + fileList[i].length();
            }
        }
        return size / 1048576;
    }
    public static void delete(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            file.delete();
        }
    }
}
