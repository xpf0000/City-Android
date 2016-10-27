package citycircle.com.Property;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.readystatesoftware.viewbadger.BadgeView;

import net.simonvt.menudrawer.MenuDrawer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import citycircle.com.Adapter.Addadpter;
import citycircle.com.Property.PropertyAdapter.HomeListAdapter;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;
import citycircle.com.Utils.ImageUtils;
import citycircle.com.Utils.PreferencesUtils;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

/**
 * Created by admins on 2016/1/30.
 */
public class PropertyHome extends Activity implements View.OnClickListener {
    ImageView back;
    TextView messages, DOC, meeting, phonenumber, number, prophonenumber, prcitcle;
    private MenuDrawer mDrawer;
    ImageView head;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    citycircle.com.Utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    String url, urlstr, uid, username, adduel, addurlstr, houseurl, housestr, hosestatstr, fanghaoid;
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> addarray = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> hashMap;
    HomeListAdapter adapter;
    ListView houselist;
    AutoScrollViewPager hviewpage;
    private List<View> listViews; // 图片组
    private View item;
    private LayoutInflater inflater;
    Addadpter myviewpageadapater;
    private ImageView[] indicator_imgs;
    BadgeView badge;
    int hosestat = 0;
    Intent intent = new Intent();
    TextView wuyu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mDrawer = MenuDrawer.attach(this, MenuDrawer.Type.OVERLAY, Position.RIGHT);
//        mDrawer.setContentView(R.layout.pro_home);
//        mDrawer.setMenuView(R.layout.left_meun);
//        mDrawer.setMaxAnimationDuration(R.anim.woniu_list_item);
        setContentView(R.layout.pro_home);

        uid = PreferencesUtils.getString(PropertyHome.this, "userid");
        username = PreferencesUtils.getString(PropertyHome.this, "username");
        houseurl = GlobalVariables.urlstr + "user.getHouseList&uid=" + uid + "&username=" + username;
        url = GlobalVariables.urlstr + "Wuye.getUserNewsCount&uid=" + uid + "&username=" + username;
        adduel = GlobalVariables.urlstr + "News.getGuanggao&typeid=93";
        intview();
        getHuose();

    }

    private void intview() {
        wuyu = (TextView) findViewById(R.id.wuyu);
        prophonenumber = (TextView) findViewById(R.id.prophonenumber);
        prophonenumber.setOnClickListener(this);
//        usermessages = (TextView) findViewById(R.id.usermessages);
//        badge = new BadgeView(this, usermessages);
//        usermessages.post(new Runnable() {
//            @Override
//            public void run() {
//                badge.setBadgeMargin(0, (usermessages.getHeight() / 5));
//            }
//        });

//        badge.setBadgePosition(BadgeView.POSITION_CENTER);
        wuyu.setText(PreferencesUtils.getString(PropertyHome.this, "xiaoqu"));
//        usermessages.setOnClickListener(this);
        inflater = LayoutInflater.from(this);
        hviewpage = (AutoScrollViewPager) findViewById(R.id.view_pager);
        hviewpage.setOffscreenPageLimit(3);
        hviewpage.startAutoScroll();
        hviewpage.setInterval(3000);
//        houselist = (ListView) findViewById(R.id.houselist);
//        head = (ImageView) findViewById(R.id.head);
//        number=(TextView)findViewById(R.id.number);
        back = (ImageView) findViewById(R.id.loginback);
        back.setOnClickListener(this);
        messages = (TextView) findViewById(R.id.messages);
        DOC = (TextView) findViewById(R.id.DOC);
        phonenumber = (TextView) findViewById(R.id.phonenumber);
        meeting = (TextView) findViewById(R.id.meeting);
        prcitcle = (TextView) findViewById(R.id.prcitcle);
        messages.setOnClickListener(this);
        DOC.setOnClickListener(this);
        meeting.setOnClickListener(this);
        phonenumber.setOnClickListener(this);
        prcitcle.setOnClickListener(this);
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(this));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
        options = ImageUtils.setCirclelmageOptions();
//        String headurl = PreferencesUtils.getString(this, "headimage");
//        try {
//            String numbers=PreferencesUtils.getString(PropertyHome.this,"mobile");
//            number.setText(numbers.replace(numbers.substring(7,11),"****"));
//        }catch (Exception e){
//            e.printStackTrace();
//            number.setText("未绑定电话号码");
//        }

//        ImageLoader.displayImage(headurl, head, options,
//                animateFirstListener);
//        houselist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                mDrawer.closeMenu();
//                PreferencesUtils.putString(PropertyHome.this, "houseids", array.get(position).get("houseid"));
//                adapter.notifyDataSetChanged();
//            }
//        });
        hviewpage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                try {
                    // 改变所有导航的背景图片为：未选中

                    for (int i = 0; i < indicator_imgs.length; i++) {

                        indicator_imgs[i]
                                .setBackgroundResource(R.mipmap.ic_indicator_off);

                    }
                    // 改变当前背景图片为：选中

                    indicator_imgs[position]
                            .setBackgroundResource(R.mipmap.ic_indicator_on);

                } catch (Exception e) {
                    // TODO: handle exception
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        String houseid;
        try {
            houseid = PreferencesUtils.getString(PropertyHome.this, "houseid");
        } catch (Exception e) {
            houseid = "0";
        }
        switch (v.getId()) {
            case R.id.loginback:
                finish();
                break;
            case R.id.messages:
                if (houseid.equals("0")) {
                    intent.setClass(PropertyHome.this, AddHome.class);
                    PropertyHome.this.startActivity(intent);
                } else {
                    intent.setClass(PropertyHome.this, Information.class);
                    gethousestate();
                }
                break;
            case R.id.phonenumber:
                if (houseid.equals("0")) {
                    intent.setClass(PropertyHome.this, AddHome.class);
                    PropertyHome.this.startActivity(intent);
                } else {
                    intent.setClass(PropertyHome.this, Payment.class);
                    gethousestate();
                }
//                PropertyHome.this.startActivity(intent);
                break;
            case R.id.meeting:
                if (houseid.equals("0")) {
                    intent.setClass(PropertyHome.this, AddHome.class);
                    PropertyHome.this.startActivity(intent);
                } else {
                    intent.setClass(PropertyHome.this, MyHouse.class);
                    PropertyHome.this.startActivity(intent);
                }
                break;
            case R.id.DOC:
                if (houseid.equals("0")) {
                    intent.setClass(PropertyHome.this, AddHome.class);
                    PropertyHome.this.startActivity(intent);
                } else {
                    intent.setClass(PropertyHome.this, FeeBack.class);
                    gethousestate();
                }
//                PropertyHome.this.startActivity(intent);
                break;
//            case R.id.usermessages:
//
//                intent.setClass(PropertyHome.this, MessageList.class);
//                gethousestate();
////                PropertyHome.this.startActivity(intent);
//                break;
            case R.id.prophonenumber:
                if (houseid.equals("0")) {
                    intent.setClass(PropertyHome.this, AddHome.class);
                    PropertyHome.this.startActivity(intent);
                } else {
                    intent.setClass(PropertyHome.this, Pronumber.class);
                    gethousestate();
                }
//                PropertyHome.this.startActivity(intent);
                break;
            case R.id.prcitcle:
                if (houseid.equals("0")) {
                    intent.setClass(PropertyHome.this, AddHome.class);
                    PropertyHome.this.startActivity(intent);
                } else {
                    intent.setClass(PropertyHome.this, ProCircle.class);
                    gethousestate();
                }
//                PropertyHome.this.startActivity(intent);
                break;
        }
    }

    private void getHuose() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                HttpRequest httpRequest = new HttpRequest();
                urlstr = httpRequest.doGet(url);
                if (addurlstr == null) {
                    addurlstr = httpRequest.doGet(adduel);
                    housestr = httpRequest.doGet(houseurl);
                }
                if (addurlstr.equals("网络超时")) {
                    handler.sendEmptyMessage(2);
                } else {
                    handler.sendEmptyMessage(1);
                }
            }
        }.start();
    }

    private void gethousestate() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                HttpRequest httpRequest = new HttpRequest();
                fanghaoid = PreferencesUtils.getString(PropertyHome.this, "fanghaoid");
                String url = GlobalVariables.urlstr + "wuye.getshenhe&fanghaoid=" + fanghaoid + "&uid=" + uid;
                hosestatstr = httpRequest.doGet(url);
                if (hosestatstr.equals("网络超时")) {
//                    handler.sendEmptyMessage(3);
                } else {
                    handler.sendEmptyMessage(4);
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
                    int message = 0;
                    if (jsonObject1.getIntValue("code") == 0) {
                        JSONArray jsonArray = jsonObject1.getJSONArray("info");
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            message = jsonObject2.getIntValue("count");
                        }
                    }
//                    setHouselist();
//                    if (message != 0) {
//                        badge.setText(message + "");
//                        badge.show();
//                    } else {
//                        badge.hide();
//                    }
                    setAddarray(addurlstr);
                    setheadadd();
                    initIndicator();
                    try {
                        setArray(housestr);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    final String houseid = PreferencesUtils.getString(PropertyHome.this, "houseid");
                    final String fanghaoid = PreferencesUtils.getString(PropertyHome.this, "fanghaoid");
                    for (int i = 0; i < array.size(); i++) {
                        if (array.get(i).get("houseid").equals(houseid) && array.get(i).get("fanghaoid").equals(fanghaoid)) {
                            PreferencesUtils.putString(PropertyHome.this, "xiaoqu", array.get(i).get("xiaoqu"));
                            String hosename = array.get(i).get("xiaoqu") + array.get(i).get("louhao") + array.get(i).get("danyuan") + array.get(i).get("fanghao");
                            PreferencesUtils.putString(PropertyHome.this, "housename", hosename);
                        }
                    }
                    break;
                case 2:
                    Toast.makeText(PropertyHome.this, R.string.intent_error, Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(PropertyHome.this, "未审核通过", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    JSONObject jsonObject2 = JSON.parseObject(hosestatstr);
                    JSONObject jsonObject3 = jsonObject2.getJSONObject("data");
                    if (jsonObject3.getIntValue("code") == 0) {
                        JSONArray jsonArray = jsonObject3.getJSONArray("info");
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject jsonObject4 = jsonArray.getJSONObject(i);
                            hosestat = jsonObject4.getIntValue("status");
                        }
                    }
                    if (hosestat == 0) {
                        handler.sendEmptyMessage(3);
                    } else {
                        PropertyHome.this.startActivity(intent);
                    }
                    break;
            }
        }
    };

    private void setArray(String str) throws Exception {
        JSONObject jsonObject = JSON.parseObject(str);
        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
        int a = jsonObject1.getIntValue("code");
        if (a == 0) {
            JSONArray jsonArray = jsonObject1.getJSONArray("info");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                hashMap = new HashMap<>();
                hashMap.put("id", jsonObject2.getString("id") == null ? "" : jsonObject2.getString("id"));
                hashMap.put("houseid", jsonObject2.getString("houseid") == null ? "" : jsonObject2.getString("houseid"));
                hashMap.put("xiaoqu", jsonObject2.getString("xiaoqu") == null ? "" : jsonObject2.getString("xiaoqu"));
                hashMap.put("louhao", jsonObject2.getString("louhao") == null ? "" : jsonObject2.getString("louhao"));
                hashMap.put("fanghao", jsonObject2.getString("fanghao") == null ? "" : jsonObject2.getString("fanghao"));
                hashMap.put("fanghaoid", jsonObject2.getString("fanghaoid") == null ? "" : jsonObject2.getString("fanghaoid"));
                hashMap.put("danyuan", jsonObject2.getString("danyuan") == null ? "" : jsonObject2.getString("danyuan"));
                array.add(hashMap);
            }
        } else {
//            handler.sendEmptyMessage(3);
        }

    }

    public void setheadadd() {
        listViews = new ArrayList<View>();
        for (int i = 0; i < addarray.size(); i++) {
            item = inflater.inflate(R.layout.viewpage, null);
            ((TextView) item.findViewById(R.id.infor_title))
                    .setText("");
            listViews.add(item);
        }
        myviewpageadapater = new Addadpter(listViews,
                PropertyHome.this, addarray);
        hviewpage.setAdapter(myviewpageadapater);
    }

    private void setHouselist() {
        adapter = new HomeListAdapter(array, PropertyHome.this, handler);
        houselist.setAdapter(adapter);
    }

    public void setAddarray(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
        if (jsonObject1.getIntValue("code") == 0) {
            JSONArray jsonArray = jsonObject1.getJSONArray("info");
            indicator_imgs = new ImageView[jsonArray.size()];
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                hashMap = new HashMap<>();
                hashMap.put("picurl", jsonObject2.getString("picurl") == null ? "" : jsonObject2.getString("picurl"));
                hashMap.put("url", jsonObject2.getString("url") == null ? "" : jsonObject2.getString("url"));
                addarray.add(hashMap);
            }
        } else {

        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        array.clear();
        addarray.clear();
        getHuose();
        wuyu.setText(PreferencesUtils.getString(PropertyHome.this, "xiaoqu"));
    }

    //    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode==KeyEvent.KEYCODE_BACK){
//            if (mDrawer.isMenuVisible()){
//                mDrawer.closeMenu();
//            }else {
//                finish();
//            }
//        }
//        return false;
//    }
    private void initIndicator() {

        ImageView imgView;
        View v = findViewById(R.id.dian);// 线性水平布局，负责动态调整导航图标
        ((ViewGroup) v).removeAllViews();
        for (int i = 0; i < addarray.size(); i++) {
            imgView = new ImageView(this);
            LinearLayout.LayoutParams params_linear = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params_linear.setMargins(7, 10, 20, 10);
            params_linear.gravity = Gravity.CENTER;
            imgView.setLayoutParams(params_linear);
            indicator_imgs[i] = imgView;

            if (i == 0) { // 初始化第一个为选中状态

                indicator_imgs[i]
                        .setBackgroundResource(R.mipmap.ic_indicator_on);
            } else {
                indicator_imgs[i].setBackgroundResource(R.mipmap.ic_indicator_off);
            }
            ((ViewGroup) v).addView(indicator_imgs[i]);

        }

    }
}
