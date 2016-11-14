package citycircle.com.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import citycircle.com.Activity.Alrece;
import citycircle.com.Activity.SearchVip;
import citycircle.com.Activity.Yerec;
import citycircle.com.R;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by admins on 2016/5/31.
 */
public class VipCardFragment extends AppCompatActivity {
    ViewPager vippage;
    private List<Fragment> lists;
    Alrece alrece;
    Yerec yerec;
    private FragmentAdapter fa;
    ImageView search,back;
    SegmentedGroup segmentedGroup;
    RadioButton button1, button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vipcard);
        intview();
    }

    private void intview() {
        segmentedGroup = (SegmentedGroup) findViewById(R.id.segmented2);
        button1 = (RadioButton) findViewById(R.id.button1);
        button2 = (RadioButton) findViewById(R.id.button2);
        search = (ImageView) findViewById(R.id.search);
        back = (ImageView) findViewById(R.id.back);
        lists = new ArrayList<>();
        vippage = (ViewPager) findViewById(R.id.vippage);
        alrece = new Alrece();
        yerec = new Yerec();
        lists.add(alrece);
        lists.add(yerec);
        fa = new FragmentAdapter(getSupportFragmentManager());
        vippage.setAdapter(fa);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(VipCardFragment.this, SearchVip.class);
                VipCardFragment.this.startActivity(intent);
            }
        });
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







