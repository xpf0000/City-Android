package citycircle.com.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.TimePickerView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;

import citycircle.com.MyViews.MyDialog;
import citycircle.com.R;
import citycircle.com.Utils.GetPhotos;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;
import citycircle.com.Utils.ImageUtils;
import citycircle.com.Utils.PreferencesUtils;
import citycircle.com.Utils.UpUserHead;
import citycircle.com.Utils.mDateUtil;
import util.FileUtils;

/**
 * Created by admins on 2015/11/21.
 */
public class MyInfo extends Activity implements View.OnClickListener {
    TextView name, sex, tell_phone, truename,birthday,address;
    ImageView uesrhead, back;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    GetPhotos getPhotos;
    File tempFile, file;
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    String url, urlstr, upinfourl, upinfostr;
    int sexs;
    String username, nickname, headimage, true_name,birth_day,addr_ess;
    UpUserHead upUserHead;
    Dialog dialog;
    View popView;
    PopupWindow popupWindow;
    LinearLayout namelay, sexlay, truenamelay,birthdaylay,addresslay;
    View CheckView = null;
    private LayoutInflater inflater = null;
    PopupWindow menuWindow;
    String mobile;
    int type = 0;
    int types=0;
    TimePickerView pvTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);
        dialog = MyDialog.createLoadingDialog(MyInfo.this, "正在登陆...");
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(MyInfo.this));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
        getPhotos = new GetPhotos();
        types=getIntent().getIntExtra("type",0);
        username = PreferencesUtils.getString(MyInfo.this, "username");
        File sd = Environment.getExternalStorageDirectory();
        String path = sd.getPath() + "/citycircle/Cache";
        upinfourl = GlobalVariables.urlstr + "User.userEdit&username=" + username + "&nickname=" + nickname + "&sex=" + sex;
        mobile = PreferencesUtils.getString(MyInfo.this, "mobile");
        File file = new File(path);
        if (!file.exists())
            file.mkdir();
        boolean a=file.exists();
        tempFile = new File(file, getPhotos.getPhotoFileName());
        url = GlobalVariables.urlstr + "User.headEdit";
        intview();
    }

    public void intview() {
        pvTime = new TimePickerView(MyInfo.this, TimePickerView.Type.YEAR_MONTH_DAY);
        truename = (TextView) findViewById(R.id.truename);
        truenamelay = (LinearLayout) findViewById(R.id.truenamelay);
        truenamelay.setOnClickListener(this);
        inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        namelay = (LinearLayout) findViewById(R.id.namelay);
        namelay.setOnClickListener(this);
        birthdaylay = (LinearLayout) findViewById(R.id.birthdaylay);
        birthdaylay.setOnClickListener(this);
        addresslay = (LinearLayout) findViewById(R.id.addresslay);
        addresslay.setOnClickListener(this);
        sexlay = (LinearLayout) findViewById(R.id.sexlay);
        sexlay.setOnClickListener(this);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        uesrhead = (ImageView) findViewById(R.id.uesrhead);
        uesrhead.setOnClickListener(this);
        name = (TextView) findViewById(R.id.name);
        birthday = (TextView) findViewById(R.id.birthday);
        address = (TextView) findViewById(R.id.address);
//        name.setOnClickListener(this);
        sex = (TextView) findViewById(R.id.sex);
//        sex.setOnClickListener(this);
        tell_phone = (TextView) findViewById(R.id.tell_phone);
        tell_phone.setOnClickListener(this);
        handler.sendEmptyMessage(1);
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(MyInfo.this));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR) - 70, calendar.get(Calendar.YEAR));
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                birthday.setText(mDateUtil.getTime(date));
                birth_day=birthday.getText().toString();
                try {
                    upinfourl = GlobalVariables.urlstr + "User.userEdit&username=" + username + "&nickname=" + URLEncoder.encode(nickname, "UTF-8") + "&sex=" + sexs + "&truename=" + URLEncoder.encode(true_name, "UTF-8")+"&birthday="+birth_day+"&address="+addr_ess;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                getuserinfo(1);
            }
        });
    }

    public void getuserinfo(final int type) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                if (type == 0) {
                    upUserHead = new UpUserHead();
                    String username = PreferencesUtils.getString(MyInfo.this, "username");
                    urlstr = upUserHead.uploadFile(url, tempFile, username);
                    if (urlstr.equals("失败")) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(3);
                    }
                } else {
                    HttpRequest httpRequest = new HttpRequest();
                    upinfostr = httpRequest.doGet(upinfourl);
                    if (upinfostr.equals("网络超时")) {
                        handler.sendEmptyMessage(4);
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
                    true_name = PreferencesUtils.getString(MyInfo.this, "truename");
                    nickname = PreferencesUtils.getString(MyInfo.this, "nickname");
                    headimage = PreferencesUtils.getString(MyInfo.this, "headimage");
                    sexs = PreferencesUtils.getInt(MyInfo.this, "sex");
                    headimage = PreferencesUtils.getString(MyInfo.this, "headimage");
                    birth_day=PreferencesUtils.getString(MyInfo.this, "birthday");
                    addr_ess=PreferencesUtils.getString(MyInfo.this, "address");
                    if (true_name==null){
                        true_name="";
                    }
                    birthday.setText(birth_day);
                    address.setText(addr_ess);
                    name.setText(nickname);
                    tell_phone.setText(mobile);
                    truename.setText(true_name);
                    if (sexs == 1) {
                        sex.setText("男");
                    } else {
                        sex.setText("女");
                    }
                    options = ImageUtils.setCirclelmageOptions();
                    ImageLoader.displayImage(headimage, uesrhead, options,
                            animateFirstListener);
                    break;
                case 2:
                    dialog.dismiss();
                    Toast.makeText(MyInfo.this, "上传头像失败！", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    JSONObject jsonObject2 = JSON.parseObject(urlstr);
                    JSONObject jsonObject3 = jsonObject2.getJSONObject("data");
                    if (jsonObject3.getIntValue("code") == 0) {
                        String hedimg = jsonObject3.getString("msg");
                        PreferencesUtils.putString(MyInfo.this, "headimage", hedimg);
                        handler.sendEmptyMessage(1);
                        Toast.makeText(MyInfo.this, "上传头像成功！", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setAction("com.servicedemo4");
                        intent.putExtra("getmeeage", "0");
                        MyInfo.this.sendBroadcast(intent);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(MyInfo.this, "上传头像失败！", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                    break;
                case 4:
                    Toast.makeText(MyInfo.this, "网络似乎有问题了！", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    JSONObject jsonObject = JSON.parseObject(upinfostr);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    if (jsonObject1.getIntValue("code") == 0) {
                        Toast.makeText(MyInfo.this, "修改成功！", Toast.LENGTH_SHORT).show();
                        PreferencesUtils.putString(MyInfo.this, "nickname", nickname);
                        PreferencesUtils.putInt(MyInfo.this, "sex", sexs);
                        PreferencesUtils.putString(MyInfo.this, "truename", true_name);
                        PreferencesUtils.putString(MyInfo.this, "birthday", birth_day);
                        PreferencesUtils.putString(MyInfo.this, "address", addr_ess);
                        Intent intent = new Intent();
                        intent.setAction("com.servicedemo4");
                        intent.putExtra("getmeeage", "0");
                        MyInfo.this.sendBroadcast(intent);
                        handler.sendEmptyMessage(1);
                        try {
                            popupWindow.dismiss();
                        } catch (Exception e) {

                        }
//                       if (types==1){
//                           if (type == 1) {
//                               Intent intent1 = new Intent();
//                               intent1.setClass(MyInfo.this, AddHome.class);
//                               MyInfo.this.startActivity(intent);
//                           }
//                       }
                    } else {
                        Toast.makeText(MyInfo.this, "修改失败！", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PHOTO_REQUEST_TAKEPHOTO:
//                getPhotos.startPhotoZoom(Uri.fromFile(tempFile), 150, MyInfo.this);
//                file = tempFile;
                String fileName = String.valueOf(System.currentTimeMillis());
//                    String a=tempFile.toString();
//                    System.out.println(a);
//                    Bitmap bm = (Bitmap) data.getExtras().get("data");
                Bitmap bm = BitmapFactory.decodeFile(tempFile.toString());
                File file = FileUtils.saveBitmap(bm, fileName, tempFile.toString());
                tempFile = file;
                getuserinfo(0);
                break;

            case PHOTO_REQUEST_GALLERY:
                if (data != null)
//                    getPhotos.startPhotoZoom(data.getData(), 150, MyInfo.this);
                {
                    File files = getPhotos.Getalbum(data, MyInfo.this);
                    tempFile = files;
                    Uri uri = Uri.fromFile(files);
                    ContentResolver cr = this.getContentResolver();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(cr, uri);
                        uesrhead.setImageBitmap(bitmap);
//                        getUserinfo(2);
                        getuserinfo(0);
                        dialog.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ;
                }
                break;

            case PHOTO_REQUEST_CUT:
                if (data != null)
//                    getUserinfo(2);
                    getuserinfo(0);
                try {
                    uesrhead.setImageDrawable(getPhotos.setPicToView(data));
                } catch (Exception e) {
                    e.printStackTrace();
                    Uri uri = Uri.fromFile(tempFile);
                    ContentResolver cr = this.getContentResolver();
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(cr, uri);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
//                    getUserinfo(2)
                    dialog.show();
                    getuserinfo(0);
                    uesrhead.setImageBitmap(bitmap);
                }

                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.uesrhead:
                getPhotos.showDialog(MyInfo.this, tempFile);
                break;
            case R.id.back:
                finish();
                break;
            case R.id.sexlay:
                showPopwindow(getSexCheck());
                break;
            case R.id.namelay:
//                showpop("请输入昵称", R.id.namelay);
                break;
            case R.id.truenamelay:
                if (true_name == null||true_name.equals("")) {
                    showpop("请输入姓名", R.id.truenamelay);
                }
                break;
            case R.id.birthdaylay:
                pvTime.show();
                break;
            case R.id.addresslay:
                showpop("请输入地址", R.id.addresslay);
                break;
        }

    }

    private void showPopwindow(View view) {
        menuWindow = new PopupWindow(view, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        menuWindow.setFocusable(true);
        menuWindow.setBackgroundDrawable(new BitmapDrawable());
        menuWindow.setOutsideTouchable(true);
        menuWindow.update();
        menuWindow.setAnimationStyle(R.style.PopupAnimation);
        menuWindow.showAtLocation(CheckView,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        menuWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                menuWindow = null;
            }
        });
    }

    public void showpop(String title, final int id) {
        popView = getLayoutInflater().inflate(
                R.layout.editextpop, null);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        popupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        popupWindow.showAtLocation(popView,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        final EditText myviptxt = (EditText) popView.findViewById(R.id.popedttxt);
        TextView poptitle = (TextView) popView.findViewById(R.id.title);
        poptitle.setText(title);
        Button back = (Button) popView.findViewById(R.id.back);
        Button update = (Button) popView.findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (id) {
                    case R.id.namelay:
                        if (myviptxt.getText().toString().trim().length() == 0) {
                            Toast.makeText(MyInfo.this, "昵称不能为空", Toast.LENGTH_SHORT).show();
                        } else {
                            nickname = myviptxt.getText().toString();
                            try {
                                upinfourl = GlobalVariables.urlstr + "User.userEdit&username=" + username + "&nickname=" + URLEncoder.encode(nickname, "UTF-8") + "&sex=" + sexs + "&truename=" + URLEncoder.encode(true_name, "UTF-8")+"&birthday="+birth_day+"&address="+addr_ess;
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            getuserinfo(1);
                        }
                        break;
                    case R.id.truenamelay:
                        if (myviptxt.getText().toString().trim().length() == 0) {
                            popupWindow.dismiss();
                        } else {
                            true_name = myviptxt.getText().toString();
                            try {
                                upinfourl = GlobalVariables.urlstr + "User.userEdit&username=" + username + "&nickname=" + URLEncoder.encode(nickname, "UTF-8") + "&sex=" + sexs + "&truename=" + URLEncoder.encode(true_name, "UTF-8")+"&birthday="+birth_day+"&address="+addr_ess;
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            type = 1;
                            getuserinfo(1);
                        }
                        break;
                    case R.id.addresslay:
                        if (myviptxt.getText().toString().trim().length() == 0) {
                            popupWindow.dismiss();
                        } else {
                            addr_ess = myviptxt.getText().toString();
                            try {
                                upinfourl = GlobalVariables.urlstr + "User.userEdit&username=" + username + "&nickname=" + URLEncoder.encode(nickname, "UTF-8") + "&sex=" + sexs + "&truename=" + URLEncoder.encode(true_name, "UTF-8")+"&birthday="+birth_day+"&address="+addr_ess;
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            type = 1;
                            getuserinfo(1);
                        }
                        break;
                }

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nickname = name.getText().toString();
                popupWindow.dismiss();
            }
        });
    }

    private View getSexCheck() {
        CheckView = inflater.inflate(R.layout.sexcheck, null);
        RadioGroup RadioGroup = (RadioGroup) CheckView.findViewById(R.id.check);
        RadioButton man = (RadioButton) CheckView.findViewById(R.id.man);
        RadioButton woman = (RadioButton) CheckView.findViewById(R.id.woman);
        if (sex.getText().equals("男")) {
            man.setChecked(true);
        } else {
            woman.setChecked(true);
        }
        RadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.man:
                        sexs = 1;
                        try {
                            upinfourl = GlobalVariables.urlstr + "User.userEdit&username=" + username + "&nickname=" + URLEncoder.encode(nickname, "UTF-8") + "&sex=" + sexs + "&truename=" + URLEncoder.encode(true_name, "UTF-8")+"&birthday="+birth_day+"&address="+addr_ess;
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        getuserinfo(1);
                        break;
                    case R.id.woman:
                        sexs = 0;
                        try {
                            upinfourl = GlobalVariables.urlstr + "User.userEdit&username=" + username + "&nickname=" + URLEncoder.encode(nickname, "UTF-8") + "&sex=" + sexs + "&truename=" + URLEncoder.encode(true_name, "UTF-8")+"&birthday="+birth_day+"&address="+addr_ess;
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        getuserinfo(1);
                        break;
                }
                menuWindow.dismiss();
            }
        });
        return CheckView;
    }
}
