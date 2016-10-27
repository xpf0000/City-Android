package citycircle.com.Property;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import citycircle.com.Activity.CityClass;
import citycircle.com.Activity.Logn;
import citycircle.com.Adapter.MyFragmentPagerAdapter;
import citycircle.com.R;
import citycircle.com.Utils.PreferencesUtils;

/**
 * Created by admins on 2016/4/12.
 */
public class ProCircle extends FragmentActivity implements View.OnClickListener {
    private ArrayList<Fragment> fragmentsList;
    Fragment home1;
    Fragment home2;
    private ViewPager mPager;
    ImageView back,newsphoto;
    TextView hot, news;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_layout);
        intview();
    }
    public void intview() {
        newsphoto=(ImageView)findViewById(R.id.newsphoto);
        newsphoto.setOnClickListener(this);
        back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(this);
        back.setImageResource(R.mipmap.news_info_back_arrow01);
        mPager = (ViewPager)findViewById(R.id.vPager);
        hot = (TextView)findViewById(R.id.hot);
        news = (TextView)findViewById(R.id.news);
        hot.setOnClickListener(this);
        news.setOnClickListener(this);
        fragmentsList = new ArrayList<Fragment>();
        home1 = new ProNew();
        home2 = new ProHot();
        fragmentsList.add(home1);
        fragmentsList.add(home2);
        mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(),
                fragmentsList));
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int color = getResources().getColor(R.color.butbg);
                if (position == 1) {
                    hot.setBackgroundResource(R.mipmap.top_hot_bg);
                    hot.setTextColor(color);
                    news.setBackgroundResource(R.mipmap.top_new_bg);
                    news.setTextColor(Color.WHITE);
                } else {
                    hot.setBackgroundResource(R.mipmap.top_hot_bg2);
                    hot.setTextColor(Color.WHITE);
                    news.setBackgroundResource(R.mipmap.top_new_bg2);
                    news.setTextColor(color);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent();
        switch (v.getId()) {
            case R.id.news:
                mPager.setCurrentItem(1);
                break;
            case R.id.hot:
                mPager.setCurrentItem(0);
                break;
            case R.id.back:
//                intent.setClass(ProCircle.this, CityClass.class);
//                ProCircle.this.startActivity(intent);
                finish();
                break;
            case R.id.newsphoto:
                int a= PreferencesUtils.getInt(ProCircle.this, "land");
                if (a==0){
                    intent.setClass(ProCircle.this,Logn.class);
                    ProCircle.this.startActivity(intent);
                }else {
                    intent.setClass(ProCircle.this, AddProcircle.class);
                    ProCircle.this.startActivity(intent);
                }
                break;

        }
    }
}
