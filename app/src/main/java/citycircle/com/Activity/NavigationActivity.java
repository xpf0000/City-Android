package citycircle.com.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import citycircle.com.Adapter.NaPageadapter;
import citycircle.com.R;
import citycircle.com.Utils.ImageUtils;
import citycircle.com.Utils.PreferencesUtils;

/**
 * Created by admins on 2015/11/2.
 */
public class NavigationActivity extends Activity {
    ViewPager hviewpage; // 图片容器
    NaPageadapter myviewpageadapater;
    private List<View> listViews; // 图片组
    private View item;
    private LayoutInflater inflater;
    ArrayList<HashMap<String, String>> addarray = new ArrayList<HashMap<String, String>>();
    private int[] resIds = new int[]{R.mipmap.app,
            R.mipmap.app2, R.mipmap.app3};
    private int lastX = 0;
    int currentIndex;
    int frist;
    int a = 0;
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation);
        frist = PreferencesUtils.getInt(NavigationActivity.this, "frist");
        inflater = LayoutInflater.from(this);
        hviewpage = (ViewPager) findViewById(R.id.hviewpage);
        listViews = new ArrayList<View>();
        if (frist == -1) {
            setHviewpage();
        } else {
            setnewHviewpage();
        }
        hviewpage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        lastX = (int) event.getX();

                        break;
                    case MotionEvent.ACTION_MOVE:
                        if ((lastX - event.getX()) > 100
                                && (currentIndex == listViews.size() - 1)) {
                            if (a == 0) {
//                                if (frist == -1) {
//                                    Intent intent = new Intent(NavigationActivity.this,
//                                            CityList.class);
//                                    NavigationActivity.this.startActivity(intent);
//
//                                } else {
                                PreferencesUtils.putInt(NavigationActivity.this, "frist", 0);
                                PreferencesUtils.putInt(NavigationActivity.this, "ornew", 0);
                                Intent intent = new Intent(NavigationActivity.this, MainActivity.class);
                                startActivity(intent);
//                                }
                                a++;
                            }

                            finish();
                        }
                        break;

                }
                return false;
            }
        });
        hviewpage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                currentIndex = position;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setHviewpage() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        for (int i = 0; i < resIds.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(layoutParams);
            iv.setBackgroundResource(resIds[i]);
            listViews.add(iv);
        }
        MyPageAdapter adapter = new MyPageAdapter();
        hviewpage.setAdapter(adapter);
    }

    public void setnewHviewpage() {
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(NavigationActivity.this));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
        options = ImageUtils.setqidongOptions();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        String json = PreferencesUtils.getString(NavigationActivity.this, "picimg");
        if (json.equals("网络超时")) {
            PreferencesUtils.putInt(NavigationActivity.this, "newImage", 0);
            Intent intent = new Intent(NavigationActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {

            JSONObject jsonObject = JSONObject.parseObject(json);
            JSONObject jsonObject1=jsonObject.getJSONObject("data");
            if (jsonObject1.getIntValue("code") == 0) {
                JSONArray jsonArray = jsonObject1.getJSONArray("info");
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                    hashMap = new HashMap<String, String>();
                    hashMap.put("PicUrl", jsonObject2.getString("url"));
                    addarray.add(hashMap);
                }
                setViewpage();
//                for (int i = 0; i < array.size(); i++) {
//                    ImageView iv = new ImageView(this);
//                    iv.setLayoutParams(layoutParams);
//                    ImageLoader.displayImage(array.get(i).get("PicUrl"), iv, options,
//                            animateFirstListener);
//                    listViews.add(iv);
//                }
//                MyPageAdapter adapter = new MyPageAdapter();
//                hviewpage.setAdapter(adapter);
            } else {
                PreferencesUtils.putInt(NavigationActivity.this, "newImage", 0);
                Intent intent = new Intent(NavigationActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

        }
    }

    public void setViewpage() {
        for (int i = 0; i < addarray.size(); i++) {
            item = inflater.inflate(R.layout.naviewpageimg, null);
            listViews.add(item);
        }
        myviewpageadapater = new NaPageadapter(listViews,
                NavigationActivity.this, addarray);
        hviewpage.setAdapter(myviewpageadapater);
    }

    class MyPageAdapter extends PagerAdapter {

        @Override
        public int getCount() {

            return listViews.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // currentIndex = position;
            container.removeView(listViews.get(position));

        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            container.addView(listViews.get(position));

            return listViews.get(position);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {

            return arg0 == arg1;
        }

    }
}
