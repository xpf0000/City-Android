package citycircle.com.Property;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;

import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;

/**
 * Created by admins on 2015/11/20.
 */
public class AddHome extends FragmentActivity implements View.OnClickListener {
    private ViewPager viewPager;
    private List<Fragment> lists;
    private FragmentAdapter fa;
    private TextView tv1;
    private TextView tv2;

    private ImageView iv1;
    private ImageView iv2;

    private ImageView iv01;
    private ImageView iv02;
    private ImageView mettingback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addhome);
        intview();
    }

    public void intview() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv1.setOnClickListener(this);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv2.setOnClickListener(this);
        iv1 = (ImageView) findViewById(R.id.iv1);
        iv2 = (ImageView) findViewById(R.id.iv2);
        mettingback = (ImageView) findViewById(R.id.back);
        mettingback.setOnClickListener(this);
        iv01 = (ImageView) findViewById(R.id.iv01);
        iv02 = (ImageView) findViewById(R.id.iv02);
        lists = new ArrayList<Fragment>();
        SerachHome MallFragment = new SerachHome();
        SelectHome WareFragment = new SelectHome();
        lists.add(WareFragment);
        lists.add(MallFragment);
        viewPager.setOffscreenPageLimit(3);
        fa = new FragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fa);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case 0:

                        ViewHelper.setAlpha(iv1, 1 - positionOffset);
                        ViewHelper.setAlpha(iv01, positionOffset);
                        ViewHelper.setAlpha(iv2, 1 - positionOffset);
                        ViewHelper.setAlpha(iv02, positionOffset);

                        iv01.setVisibility(View.VISIBLE);
                        iv02.setVisibility(View.VISIBLE);
                        break;
//                    case 1:
//
//                        ViewHelper.setAlpha(iv2, positionOffset);
//                        ViewHelper.setAlpha(iv02, 1 - positionOffset);
//                        ViewHelper.setAlpha(iv3, 1 - positionOffset);
//                        ViewHelper.setAlpha(iv03, positionOffset);
//                        iv02.setVisibility(View.VISIBLE);
//                        iv03.setVisibility(View.VISIBLE);
//                        break;
                    case 2:
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {
                int color = getResources().getColor(R.color.topbule);
                switch (position) {
                    case 0:
                        tv1.setTextColor(color);
                        tv2.setTextColor(Color.BLACK);
                        iv1.setVisibility(View.VISIBLE);
                        iv01.setVisibility(View.GONE);
                        iv2.setVisibility(View.VISIBLE);
                        iv02.setVisibility(View.GONE);
                        break;
                    case 1:
                        tv2.setTextColor(color);
                        tv1.setTextColor(Color.BLACK);

                        iv1.setVisibility(View.GONE);
                        iv01.setVisibility(View.VISIBLE);

                        iv2.setVisibility(View.GONE);
                        iv02.setVisibility(View.VISIBLE);


                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv1:
                viewPager.setCurrentItem(0);
                break;
            case R.id.tv2:
                viewPager.setCurrentItem(1);
                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        GlobalVariables.Propertyid="";
        GlobalVariables.doorid="";
        GlobalVariables.homenuid="";
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
