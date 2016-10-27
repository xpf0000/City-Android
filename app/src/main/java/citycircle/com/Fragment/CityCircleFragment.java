package citycircle.com.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import citycircle.com.Activity.CityClass;
import citycircle.com.Activity.Hotphoto;
import citycircle.com.Activity.Logn;
import citycircle.com.Activity.Newphoto;
import citycircle.com.Activity.ReplyPhoto;
import citycircle.com.Adapter.MyFragmentPagerAdapter;
import citycircle.com.R;
import citycircle.com.Utils.PreferencesUtils;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by admins on 2015/11/14.
 */
public class CityCircleFragment extends Fragment implements View.OnClickListener {
    View view;
    private ArrayList<Fragment> fragmentsList;
    Fragment home1;
    Fragment home2;
    private ViewPager mPager;
    ImageView back,newsphoto;
    TextView hot, news;
    SegmentedGroup segmentedGroup;
    RadioButton button1, button2;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.city_layout, container, false);
        intview();
        return view;
    }

    public void intview() {
        segmentedGroup = (SegmentedGroup) view.findViewById(R.id.segmented2);
        button1 = (RadioButton) view.findViewById(R.id.button1);
        button2 = (RadioButton) view.findViewById(R.id.button2);
        newsphoto=(ImageView)view.findViewById(R.id.newsphoto);
        newsphoto.setOnClickListener(this);
        back=(ImageView)view.findViewById(R.id.back);
        back.setOnClickListener(this);
        mPager = (ViewPager) view.findViewById(R.id.vPager);
        hot = (TextView) view.findViewById(R.id.hot);
        news = (TextView) view.findViewById(R.id.news);
        hot.setOnClickListener(this);
        news.setOnClickListener(this);
        fragmentsList = new ArrayList<Fragment>();
        home1 = new Newphoto();
        home2 = new Hotphoto();
        fragmentsList.add(home1);
        fragmentsList.add(home2);
        mPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(),
                fragmentsList));
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int color = getResources().getColor(R.color.butbg);
                if (position == 1) {
                    button2.setChecked(true);
                    hot.setBackgroundResource(R.mipmap.top_hot_bg2);
                    hot.setTextColor(Color.WHITE);
                    news.setBackgroundResource(R.mipmap.top_new_bg2);
                    news.setTextColor(color);
                } else {
                    button1.setChecked(true);
                    hot.setBackgroundResource(R.mipmap.top_hot_bg);
                    hot.setTextColor(color);
                    news.setBackgroundResource(R.mipmap.top_new_bg);
                    news.setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        segmentedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.button1:
                        mPager.setCurrentItem(0);
                        break;
                    case R.id.button2:
                        mPager.setCurrentItem(1);
                        break;
                }
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
                intent.setClass(getActivity(), CityClass.class);
                getActivity().startActivity(intent);
                break;
            case R.id.newsphoto:
                int a= PreferencesUtils.getInt(getActivity(), "land");
                if (a==0){
                    intent.setClass(getActivity(),Logn.class);
                    getActivity().startActivity(intent);
                }else {
                    intent.setClass(getActivity(), ReplyPhoto.class);
                    getActivity().startActivity(intent);
                }
                break;

        }
    }
}
