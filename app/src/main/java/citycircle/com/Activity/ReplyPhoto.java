package citycircle.com.Activity;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.LocationClient;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.Adapter.CalssAdapter;
import citycircle.com.MyAppService.LocationApplication;
import citycircle.com.MyViews.MyDialog;
import citycircle.com.MyViews.MyGridView;
import citycircle.com.R;
import citycircle.com.Utils.GetPhotos;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;
import citycircle.com.Utils.PreferencesUtils;
import citycircle.com.Utils.UpdatePhotos;
import citycircle.com.getPhoto.AlbumActivity;
import citycircle.com.getPhoto.GalleryActivity;
import util.Bimp;
import util.FileUtils;
import util.ImageItem;
import util.PublicWay;
import util.Res;

/**
 * Created by admins on 2015/11/28.
 */
public class ReplyPhoto extends Activity {

    private MyGridView noScrollgridview, calss;
    private GridAdapter adapter;
    private View parentView;
    private PopupWindow pop = null;
    private LinearLayout ll_popup;
    public static Bitmap bimap;
    ImageView back;
    String url, urlstr;
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> myguigearray = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> hashMap;
    CalssAdapter calssAdapter;
    String classid, contents;
    TextView city, activity_selectimg_send;
    private LocationClient mLocClient;
    LocationApplication location = null;
    String City;
    EditText content;
    Dialog dialog;
    String str;
    GetPhotos getPhotos;
    File tempFile;

    //    private ArrayList<ImageItem> dataList;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalVariables.Typr = 0;
        Res.init(this);
        getPhotos = new GetPhotos();
        File sd = Environment.getExternalStorageDirectory();
        String path = sd.getPath() + "/citycircle/Cache";
        File file = new File(path);
        if (!file.exists())
            file.mkdir();

        tempFile = new File(file, getPhotos.getPhotoFileName());
        dialog = MyDialog.createLoadingDialog(ReplyPhoto.this, "正在上传...");
        bimap = BitmapFactory.decodeResource(getResources(),
                R.mipmap.icon_addpic_unfocused);
        PublicWay.activityList.add(this);
        parentView = getLayoutInflater().inflate(R.layout.activity_selectimg,
                null);
        setContentView(parentView);
        location = (LocationApplication) getApplication();
        mLocClient = ((LocationApplication) getApplication()).mLocationClient;
        url = GlobalVariables.urlstr + "Quan.getCategory";
        Init();
        getclass(0);
    }

    public void setmap(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
        int a = jsonObject1.getIntValue("code");
        if (a == 0) {
            JSONArray jsonArray = jsonObject1.getJSONArray("info");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                hashMap = new HashMap<>();
                hashMap.put("id", jsonObject2.getString("id") == null ? "" : jsonObject2.getString("id"));
                hashMap.put("title", jsonObject2.getString("title") == null ? "" : jsonObject2.getString("title"));
                hashMap.put("path", jsonObject2.getString("path") == null ? "" : jsonObject2.getString("path"));
                hashMap.put("select", "0");
                array.add(hashMap);
            }
        } else {
            handlers.sendEmptyMessage(3);
        }
    }

    public void Init() {

        pop = new PopupWindow(ReplyPhoto.this);

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
                Intent intent = new Intent(ReplyPhoto.this,
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
        content = (EditText) findViewById(R.id.content);
        activity_selectimg_send = (TextView) findViewById(R.id.activity_selectimg_send);
        city = (TextView) findViewById(R.id.city);
        City = ((LocationApplication) getApplication()).city + " " + ((LocationApplication) getApplication()).getStreet;
        city.setText(City);
        back = (ImageView) findViewById(R.id.back);
        calss = (MyGridView) findViewById(R.id.calss);
        noScrollgridview = (MyGridView) findViewById(R.id.noScrollgridview);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridAdapter(this);
        adapter.update();
        noScrollgridview.setAdapter(adapter);
        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ReplyPhoto.this, CityList.class);
                ReplyPhoto.this.startActivity(intent);

            }
        });
        activity_selectimg_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contents = content.getText().toString();
                if (contents.length() == 0) {
                    Toast.makeText(ReplyPhoto.this, "请输入内容", Toast.LENGTH_SHORT).show();
                } else if (classid == null) {
                    Toast.makeText(ReplyPhoto.this, "请选择标签", Toast.LENGTH_SHORT).show();
                } else if (Bimp.tempSelectBitmap.size() == 0) {
                    Toast.makeText(ReplyPhoto.this, "请至少选择一张图片", Toast.LENGTH_SHORT).show();
                } else {
                    getclass(1);
                    dialog.show();
                }

            }
        });
        calss.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                classid = array.get(position).get("id");
                myguigearray.clear();
                for (int i = 0; i < array.size(); i++) {
                    hashMap = new HashMap<String, String>();
                    hashMap.put("id", array.get(i).get("id"));
                    hashMap.put("title", array.get(i).get("title"));
                    hashMap.put("path", array.get(i).get("path"));
                    hashMap.put("select", array.get(i).get("select"));
                    myguigearray.add(hashMap);
                }
                array.clear();
                for (int i = 0; i < myguigearray.size(); i++) {
                    hashMap = new HashMap<String, String>();
                    hashMap.put("id", myguigearray.get(i).get("id"));
                    hashMap.put("title", myguigearray.get(i).get("title"));
                    hashMap.put("path", myguigearray.get(i).get("path"));
                    if (i == position) {
                        hashMap.put("select", "1");
                    } else {
                        hashMap.put("select", "0");
                    }
                    array.add(hashMap);
                }
                calssAdapter.notifyDataSetChanged();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == Bimp.tempSelectBitmap.size()) {
                    Log.i("ddddddd", "----------");
                    ll_popup.startAnimation(AnimationUtils.loadAnimation(
                            ReplyPhoto.this, R.anim.activity_translate_in));
                    pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                } else {
                    Intent intent = new Intent(ReplyPhoto.this,
                            GalleryActivity.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", arg2);
                    startActivity(intent);
                }
            }
        });

    }

    public void getclass(final int type) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                if (type == 0) {
                    HttpRequest httpRequest = new HttpRequest();
                    urlstr = httpRequest.doGet(url);
                    if (urlstr.equals("网络超时")) {
                        handlers.sendEmptyMessage(2);
                    } else {
                        handlers.sendEmptyMessage(1);
                    }
                } else {
                    UpdatePhotos updatePhotos = new UpdatePhotos();
                    String url = GlobalVariables.urlstr + "Quan.addQuan";
                    String username = PreferencesUtils.getString(ReplyPhoto.this, "username");
                    str = updatePhotos.uploadFile(url, classid, username, contents, City);
                    if (str.equals("失败")) {
                        handlers.sendEmptyMessage(4);
                    } else {
                        handlers.sendEmptyMessage(5);
                    }
                }
            }
        }.start();
    }

    Handler handlers = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    setmap(urlstr);
                    calssAdapter = new CalssAdapter(array, ReplyPhoto.this);
                    calss.setAdapter(calssAdapter);
                    break;
                case 2:
                    Toast.makeText(ReplyPhoto.this, "获取分类失败，请检查网络", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(ReplyPhoto.this, "暂无分类", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    dialog.dismiss();
                    Toast.makeText(ReplyPhoto.this, "失败", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    dialog.dismiss();
                    JSONObject jsonObject = JSON.parseObject(str);
                    int a = jsonObject.getIntValue("ret");
                    if (a == 200) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        int b = jsonObject1.getIntValue("code");
                        if (b == 0) {
//                            Bimp.tempSelectBitmap.clear();
                            Bimp.tempSelectBitmap.clear();
                            Bimp.max = 0;
                            Intent intent = new Intent("data.broadcast.action");
                            sendBroadcast(intent);
                            for (int i = 0; i < PublicWay.activityList.size(); i++) {
                                if (null != PublicWay.activityList.get(i)) {
                                    PublicWay.activityList.get(i).finish();
                                }
                            }
//                            finish();
                            Intent intent1 = new Intent();
                            intent1.setAction("com.servicedemo4");
                            intent1.putExtra("getmeeage", "4");
                            ReplyPhoto.this.sendBroadcast(intent1);
                        } else {
                            handlers.sendEmptyMessage(4);
                        }
                    } else {
                        handlers.sendEmptyMessage(4);
                    }
                    break;
            }
        }
    };

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

    public String getString(String s) {
        String path = null;
        if (s == null)
            return "";
        for (int i = s.length() - 1; i > 0; i++) {
            s.charAt(i);
        }
        return path;
    }

    protected void onRestart() {
        adapter.update();
        City = GlobalVariables.city;
        city.setText(City);
        super.onRestart();
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
                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    opts.inJustDecodeBounds = true;
                    Bitmap bm = BitmapFactory.decodeFile(tempFile.toString(), opts);
                    try {
                        File file = FileUtils.saveBitmap(bm, fileName, tempFile.toString());
                        ImageItem takePhoto = new ImageItem();
                        takePhoto.setImagePath(file.toString());
                        takePhoto.setBitmap(bm);
                        Bimp.tempSelectBitmap.add(takePhoto);
                    } catch (Exception e) {
                        Toast.makeText(ReplyPhoto.this, "手机可运行内存不足，照片保存失败，请清理后操作", Toast.LENGTH_SHORT).show();
                    }

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
}
