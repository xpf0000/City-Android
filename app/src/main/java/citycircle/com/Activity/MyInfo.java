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
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bigkoo.svprogresshud.listener.OnDismissListener;
import com.google.gson.Gson;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import citycircle.com.MyViews.MyDialog;
import citycircle.com.R;
import citycircle.com.Utils.GetPhotos;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;
import citycircle.com.Utils.ImageUtils;
import citycircle.com.Utils.MyEventBus;
import citycircle.com.Utils.UpUserHead;
import citycircle.com.Utils.mDateUtil;
import model.NewsModel;
import okhttp3.RequestBody;
import util.FileUtils;
import util.HttpResult;
import util.XAPPUtil;
import util.XActivityindicator;
import util.XNetUtil;

import static citycircle.com.MyAppService.LocationApplication.APPDataCache;
import static citycircle.com.MyAppService.LocationApplication.APPService;

/**
 * Created by admins on 2015/11/21.
 */
public class MyInfo extends TakePhotoActivity implements View.OnClickListener {
    TextView name, sex, tell_phone, truename,birthday,address,aihao,qianming;
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
    String url, urlstr, upinfourl, upinfostr,sexs;
    String username, nickname, headimage, true_name,birth_day,addr_ess,aihaostr,qianmingstr;
    UpUserHead upUserHead;
    Dialog dialog;
    View popView;
    PopupWindow popupWindow;
    LinearLayout namelay, sexlay, truenamelay,birthdaylay,addresslay,aihaolay,qianminglay;
    View CheckView = null;
    private LayoutInflater inflater = null;
    PopupWindow menuWindow;
    String mobile;
    int type = 0;
    int types=0;
    TimePickerView pvTime;
    AlertView alertView;

    private int position = -1;

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
        username = APPDataCache.User.getUsername();
        File sd = Environment.getExternalStorageDirectory();
        String path = sd.getPath() + "/citycircle/Cache";
        upinfourl = GlobalVariables.urlstr + "User.userEdit&username=" + username + "&nickname=" + nickname + "&sex=" + sex;
        mobile = APPDataCache.User.getMobile();
        File file = new File(path);
        if (!file.exists())
            file.mkdir();
        boolean a=file.exists();
        tempFile = new File(file, getPhotos.getPhotoFileName());
        url = GlobalVariables.urlstr + "User.headEdit";
        intview();


        alertView = new AlertView("选择图片", null, "取消", null,
                new String[]{"拍照", "从相册中选择"},
                this, AlertView.Style.ActionSheet, new OnItemClickListener(){
            public void onItemClick(Object o,int p){

                position = p;

            }
        });

        File f = new File(getExternalFilesDir(""), "temp.jpg");
        final Uri uri = Uri.fromFile(f);

        alertView.setOnDismissListener(new com.bigkoo.alertview.OnDismissListener() {
            @Override
            public void onDismiss(Object o) {
                if(position == 0)
                {
                    getTakePhoto().onPickFromCapture(uri);
                }
                else if(position == 1)
                {
                    getTakePhoto().onPickFromGallery();

                }
            }
        });


    }

    public void intview() {
        pvTime = new TimePickerView(MyInfo.this, TimePickerView.Type.YEAR_MONTH_DAY);
        truename = (TextView) findViewById(R.id.truename);
        truenamelay = (LinearLayout) findViewById(R.id.truenamelay);
        truenamelay.setOnClickListener(this);
        inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

        aihaolay = (LinearLayout) findViewById(R.id.aihaolay);
        aihaolay.setOnClickListener(this);
        qianminglay = (LinearLayout) findViewById(R.id.qianminglay);
        qianminglay.setOnClickListener(this);

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

        aihao = (TextView) findViewById(R.id.aihao);
        qianming = (TextView) findViewById(R.id.qianming);

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
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:

                    true_name = APPDataCache.User.getTruename();
                    nickname = APPDataCache.User.getNickname();
                    headimage = APPDataCache.User.getHeadimage();
                    sexs = APPDataCache.User.getSex();
                    birth_day=APPDataCache.User.getBirthday();
                    addr_ess=APPDataCache.User.getAddress();
                    aihaostr = APPDataCache.User.getAihao();
                    qianmingstr = APPDataCache.User.getQianming();

                    if (true_name==null){
                        true_name="";
                    }
                    birthday.setText(birth_day);
                    address.setText(addr_ess);
                    name.setText(nickname);
                    tell_phone.setText(mobile);
                    truename.setText(true_name);
                    aihao.setText(aihaostr);
                    qianming.setText(qianmingstr);

                    if (sexs.equals("1")) {
                        sex.setText("男");
                    } else if(sexs.equals("0")) {
                        sex.setText("女");
                    }
                    else
                    {
                        sex.setText("未选择");
                    }


                    options = ImageUtils.setCirclelmageOptions();
                    ImageLoader.displayImage(headimage, uesrhead, options,
                            animateFirstListener);
                    break;

            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PHOTO_REQUEST_TAKEPHOTO:

                try
                {
                    Bitmap bm = BitmapFactory.decodeFile(tempFile.toString());
                    uploadHeadImg(bm);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                break;

            case PHOTO_REQUEST_GALLERY:
                if (data != null)
                {
                    try {
                        File files = getPhotos.Getalbum(data, MyInfo.this);
                        tempFile = files;
                        Uri uri = Uri.fromFile(files);
                        ContentResolver cr = this.getContentResolver();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(cr, uri);
                        uploadHeadImg(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case PHOTO_REQUEST_CUT:

                try {
                    Bitmap bitmap = BitmapFactory.decodeFile(tempFile.toString());
                    uploadHeadImg(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                    Uri uri = Uri.fromFile(tempFile);
                    ContentResolver cr = this.getContentResolver();
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(cr, uri);
                        uploadHeadImg(bitmap);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }


                }

                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.uesrhead:
                //getPhotos.showDialog(MyInfo.this, tempFile);
                alertView.show();
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
            case R.id.aihaolay:
                showpop("请输入个人爱好", R.id.aihaolay);
                break;
            case R.id.qianminglay:
                showpop("请输入个性签名", R.id.qianminglay);
                break;
        }

    }


    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);

        String path = result.getImages().get(0).getOriginalPath();
        Bitmap bitmap= BitmapFactory.decodeFile(path);
        uploadHeadImg(bitmap);

    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        XNetUtil.APPPrintln(msg);
    }

    @Override
    public void takeCancel() {
        super.takeCancel();

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
                            name.setText(nickname);
                            popupWindow.dismiss();
                        }
                        break;
                    case R.id.truenamelay:
                        if (myviptxt.getText().toString().trim().length() == 0) {
                            Toast.makeText(MyInfo.this, "真实姓名不能为空", Toast.LENGTH_SHORT).show();
                        } else {
                            true_name = myviptxt.getText().toString();
                            truename.setText(true_name);
                            popupWindow.dismiss();
                        }
                        break;
                    case R.id.addresslay:
                        if (myviptxt.getText().toString().trim().length() == 0) {
                            popupWindow.dismiss();
                        } else {
                            addr_ess = myviptxt.getText().toString();
                            address.setText(addr_ess);
                            popupWindow.dismiss();
                        }
                        break;

                    case R.id.aihaolay:
                        if (myviptxt.getText().toString().trim().length() == 0) {
                            popupWindow.dismiss();
                        } else {
                            aihaostr = myviptxt.getText().toString();
                            aihao.setText(aihaostr);
                            popupWindow.dismiss();
                        }
                        break;

                    case R.id.qianminglay:
                        if (myviptxt.getText().toString().trim().length() == 0) {
                            popupWindow.dismiss();
                        } else {
                            qianmingstr = myviptxt.getText().toString();
                            qianming.setText(qianmingstr);
                            popupWindow.dismiss();
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
                        sexs = "1";
                        sex.setText("男");
                        break;
                    case R.id.woman:
                        sexs = "0";
                        sex.setText("女");
                        break;
                }
                menuWindow.dismiss();
            }
        });
        return CheckView;
    }

    private void uploadHeadImg(Bitmap bitmap)
    {
        if(bitmap == null)
        {
            return;
        }

        uesrhead.setImageBitmap(bitmap);
        Map<String , RequestBody> params = new HashMap<>();
        params.put("username", XAPPUtil.createBody(APPDataCache.User.getUsername()));
        params.put("file\"; filename=\"xtest.jpg",XAPPUtil.createBody(bitmap));

        XNetUtil.HandleReturnAll(APPService.userHeadEdit(params), new XNetUtil.OnHttpResult<HttpResult<Object>>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(HttpResult<Object> res) {
                if(res.getData().getCode() == 0)
                {
                    APPDataCache.User.setHeadimage(res.getData().getMsg());
                    APPDataCache.User.save();
                    options = ImageUtils.setCirclelmageOptions();
                    ImageLoader.displayImage(res.getData().getMsg(), uesrhead, options,
                            animateFirstListener);

                    EventBus.getDefault().post(
                            new MyEventBus("UserInfoUpdated"));

                    return;
                }

                String msg = res.getData().getMsg();
                msg = msg == null ? "头像上传失败" : msg;
                Toast.makeText(MyInfo.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void doSubmit(View v)
    {
       if(APPDataCache.User.getHeadimage().equals(""))
       {
           Toast.makeText(MyInfo.this, "请设置头像", Toast.LENGTH_SHORT).show();
           return;
       }

        if(sexs.equals(""))
        {
            Toast.makeText(MyInfo.this, "请设置性别", Toast.LENGTH_SHORT).show();
            return;
        }

        if(true_name.equals(""))
        {
            Toast.makeText(MyInfo.this, "请设置真实姓名", Toast.LENGTH_SHORT).show();
            return;
        }

        if(birth_day.equals(""))
        {
            Toast.makeText(MyInfo.this, "请设置出生年月", Toast.LENGTH_SHORT).show();
            return;
        }

        XActivityindicator.create(this).show();

        XNetUtil.Handle(APPService.userUserEdit(APPDataCache.User.getUsername(),
                nickname, sexs, true_name, birth_day, addr_ess, aihaostr,
                qianmingstr), "修改成功！", "修改失败！", new XNetUtil.OnHttpResult<Boolean>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(Boolean aBoolean) {

                if(aBoolean)
                {
                    APPDataCache.User.setNickname(nickname);
                    APPDataCache.User.setSex(sexs);
                    APPDataCache.User.setTruename(true_name);
                    APPDataCache.User.setBirthday(birth_day);
                    APPDataCache.User.setAddress(addr_ess);
                    APPDataCache.User.setAihao(aihaostr);
                    APPDataCache.User.setQianming(qianmingstr);
                    APPDataCache.User.save();

                    Intent intent = new Intent();
                    intent.setAction("com.servicedemo4");
                    intent.putExtra("getmeeage", "0");
                    MyInfo.this.sendBroadcast(intent);
                    EventBus.getDefault().post(
                            new MyEventBus("UserInfoUpdated"));
                    try {
                        popupWindow.dismiss();
                    } catch (Exception e) {

                    }

                    XActivityindicator.getHud().setOnDismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss(SVProgressHUD hud) {

                            finish();

                        }
                    });




                }
            }
        });




    }

}
