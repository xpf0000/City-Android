package citycircle.com.Property;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.File;

import citycircle.com.MyViews.MyDialog;
import citycircle.com.MyViews.MyGridView;
import citycircle.com.Property.uitls.UpDatephotos;
import citycircle.com.R;
import citycircle.com.Utils.GetPhotos;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.PreferencesUtils;
import citycircle.com.getPhoto.AlbumActivity;
import citycircle.com.getPhoto.GalleryActivity;
import util.Bimp;
import util.FileUtils;
import util.ImageItem;
import util.PublicWay;
import util.Res;

/**
 * Created by admins on 2016/2/17.
 */
public class ReplyFee extends Activity implements View.OnClickListener {
    ImageView back;
    TextView activity_selectimg_send;
    EditText content;
    MyGridView noScrollgridview;
    RadioGroup check;
    RadioButton biaoyang, jianyi, fankuiwenti;
    private GridAdapter adapter;
    private View parentView;
    private PopupWindow pop = null;
    private LinearLayout ll_popup;
    public static Bitmap bimap;
    File tempFile;
    GetPhotos getPhotos;
    String url, urlstr;
    String type = "2";
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalVariables.Typr = 1;
        Res.init(this);
        getPhotos = new GetPhotos();
        File sd = Environment.getExternalStorageDirectory();
        String path = sd.getPath() + "/citycircle/Cache";
        File file = new File(path);
        if (!file.exists())
            file.mkdir();
        tempFile = new File(file, getPhotos.getPhotoFileName());
        bimap = BitmapFactory.decodeResource(getResources(),
                R.mipmap.icon_addpic_unfocused);
        PublicWay.activityList.add(this);
        parentView = getLayoutInflater().inflate(R.layout.replyfee,
                null);
        setContentView(parentView);
        url = GlobalVariables.urlstr + "Wuye.addfeed";
        dialog = MyDialog.createLoadingDialog(ReplyFee.this, "正在上传...");
        intview();
    }

    private void intview() {
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        activity_selectimg_send = (TextView) findViewById(R.id.activity_selectimg_send);
        activity_selectimg_send.setOnClickListener(this);
        content = (EditText) findViewById(R.id.content);
        noScrollgridview = (MyGridView) findViewById(R.id.calss);
        check = (RadioGroup) findViewById(R.id.check);
        biaoyang = (RadioButton) findViewById(R.id.biaoyang);
        jianyi = (RadioButton) findViewById(R.id.jianyi);
        fankuiwenti = (RadioButton) findViewById(R.id.fankuiwenti);
        pop = new PopupWindow(ReplyFee.this);
        View view = getLayoutInflater().inflate(R.layout.item_popupwindows,
                null);
        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
        Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
        Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
        parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                photo();
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ReplyFee.this,
                        AlbumActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_translate_in,
                        R.anim.activity_translate_out);
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridAdapter(this);
        adapter.update();
        noScrollgridview.setAdapter(adapter);
        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == Bimp.tempSelectBitmap.size()) {
                    Log.i("ddddddd", "----------");
                    ll_popup.startAnimation(AnimationUtils.loadAnimation(
                            ReplyFee.this, R.anim.activity_translate_in));
                    pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                } else {
                    Intent intent = new Intent(ReplyFee.this,
                            GalleryActivity.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", arg2);
                    startActivity(intent);
                }
            }
        });
        check.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.biaoyang:
                        biaoyang.setTextColor(Color.parseColor("#21ACFC"));
                        jianyi.setTextColor(Color.BLACK);
                        fankuiwenti.setTextColor(Color.BLACK);
                        type = "2";
                        break;
                    case R.id.jianyi:
                        jianyi.setTextColor(Color.parseColor("#21ACFC"));
                        biaoyang.setTextColor(Color.BLACK);
                        fankuiwenti.setTextColor(Color.BLACK);
                        type = "1";
                        break;
                    case R.id.fankuiwenti:
                        fankuiwenti.setTextColor(Color.parseColor("#21ACFC"));
                        biaoyang.setTextColor(Color.BLACK);
                        jianyi.setTextColor(Color.BLACK);
                        type = "0";
                        break;
                }
            }
        });
    }

    private void getPhoto() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                UpDatephotos upDatephotos = new UpDatephotos();
                String username = PreferencesUtils.getString(ReplyFee.this, "username");
                String uid = PreferencesUtils.getString(ReplyFee.this, "userid");
                String houseid = PreferencesUtils.getString(ReplyFee.this, "houseids");
                String contents = content.getText().toString();
                urlstr = upDatephotos.uploadFile(url, uid, username, contents, houseid, type);
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
                    JSONObject jsonObject = JSON.parseObject(urlstr);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    if (jsonObject1.getIntValue("code") == 0) {
                        Toast.makeText(ReplyFee.this, "发布成功", Toast.LENGTH_SHORT).show();
                        Bimp.tempSelectBitmap.clear();
                        Bimp.max = 0;
                        Intent intent = new Intent("data.broadcast.action");
                        sendBroadcast(intent);
                        for (int i = 0; i < PublicWay.activityList.size(); i++) {
                            if (null != PublicWay.activityList.get(i)) {
                                PublicWay.activityList.get(i).finish();
                            }
                        }
                        Intent intent1 = new Intent();
                        intent1.setAction("com.servicedemo4");
                        intent1.putExtra("getmeeage", "11");
                        ReplyFee.this.sendBroadcast(intent1);
                        finish();
                    } else {
                        Toast.makeText(ReplyFee.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                    }

                    break;
                case 2:
                    Toast.makeText(ReplyFee.this, R.string.intent_error, Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.activity_selectimg_send:

                if (content.getText().toString().trim().length() == 0) {
                    Toast.makeText(ReplyFee.this, "请输入内容", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.show();
                    getPhoto();
                }
                break;
        }
    }

    private static final int TAKE_PICTURE = 0x000001;

    public void photo() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (Bimp.tempSelectBitmap.size() < 9 && resultCode == RESULT_OK) {
//                    dataList = new ArrayList<ImageItem>();
                    String fileName = String.valueOf(System.currentTimeMillis());
//                    String a=tempFile.toString();
//                    System.out.println(a);
//                    Bitmap bm = (Bitmap) data.getExtras().get("data");
                    Bitmap bm = BitmapFactory.decodeFile(tempFile.toString());
                    File file = FileUtils.saveBitmap(bm, fileName, tempFile.toString());

                    ImageItem takePhoto = new ImageItem();
                    takePhoto.setImagePath(file.toString());
                    takePhoto.setBitmap(bm);
                    Bimp.tempSelectBitmap.add(takePhoto);
                }
                break;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            for (int i = 0; i < PublicWay.activityList.size(); i++) {
                if (null != PublicWay.activityList.get(i)) {
                    PublicWay.activityList.get(i).finish();
                }
            }
//            finish();
        }
        return true;
    }

    protected void onRestart() {
        adapter.update();
        super.onRestart();
    }

    @SuppressLint("HandlerLeak")
    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private int selectedPosition = -1;
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {
            loading();
        }

        public int getCount() {
            if (Bimp.tempSelectBitmap.size() == 9) {
                return 9;
            }
            return (Bimp.tempSelectBitmap.size() + 1);
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_published_grida,
                        parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.item_grida_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == Bimp.tempSelectBitmap.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.mipmap.icon_addpic_unfocused));
                if (position == 9) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position)
                        .getBitmap());
                System.out.println(Bimp.tempSelectBitmap.get(position)
                        .getImagePath());
            }

            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        adapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        public void loading() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (Bimp.max == Bimp.tempSelectBitmap.size()) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        } else {
                            Bimp.max += 1;
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }
                }
            }).start();
        }
    }
}
