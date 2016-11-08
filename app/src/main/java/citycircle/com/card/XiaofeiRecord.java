package citycircle.com.card;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import citycircle.com.Activity.SearchVip;
import citycircle.com.Activity.VipCardConInfo;
import citycircle.com.R;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by X on 2016/11/8.
 */

public class XiaofeiRecord extends FragmentActivity {
    ViewPager vippage;
    private List<Fragment> lists;
    VipCardConInfo alrece;
    JifenRecord yerec;
    private FragmentAdapter fa;
    SegmentedGroup segmentedGroup;
    RadioButton button1, button2;
    String id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xiaofeirecord);
        id=getIntent().getStringExtra("id");
        intview();
    }

    public void back(View v)
    {
        finish();
    }

    private void intview() {
        segmentedGroup = (SegmentedGroup) findViewById(R.id.segmented2);
        button1 = (RadioButton) findViewById(R.id.button1);
        button2 = (RadioButton) findViewById(R.id.button2);
        lists = new ArrayList<>();
        vippage = (ViewPager) findViewById(R.id.vippage);
        alrece = new VipCardConInfo();
        yerec = new JifenRecord();

        alrece.setId(id);
        yerec.setId(id);

        lists.add(alrece);
        lists.add(yerec);

        fa = new FragmentAdapter(getSupportFragmentManager());
        vippage.setAdapter(fa);

        segmentedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.button1:
                        vippage.setCurrentItem(0);
                        break;
                    case R.id.button2:
                        vippage.setCurrentItem(1);
                        break;
                }
            }
        });
        vippage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    button1.setChecked(true);
                } else {
                    button2.setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class FragmentAdapter extends FragmentPagerAdapter {

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public Fragment getItem(int arg0) {
            return lists.get(arg0);
        }
    }
}
