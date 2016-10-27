package citycircle.com.OA;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;

import citycircle.com.R;

/**
 * Created by admins on 2016/1/6.
 */
public class ContactsActivity extends FragmentActivity implements View.OnClickListener{
    private ViewPager viewPager;
    private List<Fragment> lists;
    private FragmentAdapter fa;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private ImageView iv1;
    private ImageView iv2;
    private ImageView iv3;
    private ImageView iv01;
    private ImageView iv02;
    private ImageView iv03,mettingback,addmetting;
    Button earch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.cintacts);
        initView();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    private void initView() {
        earch=(Button)findViewById(R.id.earch);
        earch.setOnClickListener(this);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv1.setOnClickListener(this);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv2.setOnClickListener(this);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv3.setOnClickListener(this);
        iv1 = (ImageView) findViewById(R.id.iv1);
        iv2 = (ImageView) findViewById(R.id.iv2);
        iv3 = (ImageView) findViewById(R.id.iv3);
        mettingback=(ImageView)findViewById(R.id.mettingback);
        mettingback.setOnClickListener(this);
        addmetting=(ImageView)findViewById(R.id.addmetting);
        addmetting.setOnClickListener(this);
        iv01 = (ImageView) findViewById(R.id.iv01);
        iv02 = (ImageView) findViewById(R.id.iv02);
        iv03 = (ImageView) findViewById(R.id.iv03);

        lists = new ArrayList<Fragment>();
        Allfragment chart = new Allfragment();
        BumenFragment friend = new BumenFragment();
        ChangFragment contact = new ChangFragment();
        lists.add(chart);
        lists.add(friend);
        lists.add(contact);
        viewPager.setOffscreenPageLimit(3);
        fa = new FragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fa);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {



            @Override
            public void onPageSelected(int arg0) {
                switch (arg0) {
                    case 0:
                        tv1.setTextColor(Color.BLUE);
                        tv2.setTextColor(Color.BLACK);
                        tv3.setTextColor(Color.BLACK);

                        iv1.setVisibility(View.VISIBLE);
                        iv01.setVisibility(View.GONE);

                        iv2.setVisibility(View.VISIBLE);
                        iv02.setVisibility(View.GONE);

                        iv3.setVisibility(View.VISIBLE);
                        iv03.setVisibility(View.GONE);
                        break;
                    case 1:
                        tv2.setTextColor(Color.BLUE);
                        tv1.setTextColor(Color.BLACK);
                        tv3.setTextColor(Color.BLACK);
                        iv1.setVisibility(View.GONE);
                        iv01.setVisibility(View.VISIBLE);

                        iv2.setVisibility(View.GONE);
                        iv02.setVisibility(View.VISIBLE);

                        iv3.setVisibility(View.VISIBLE);
                        iv03.setVisibility(View.GONE);
                        break;
                    case 2:
                        tv3.setTextColor(Color.BLUE);
                        tv2.setTextColor(Color.BLACK);
                        tv1.setTextColor(Color.BLACK);

                        iv1.setVisibility(View.GONE);
                        iv01.setVisibility(View.VISIBLE);

                        iv2.setVisibility(View.VISIBLE);
                        iv02.setVisibility(View.GONE);

                        iv3.setVisibility(View.GONE);
                        iv03.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // arg1 0-1
                switch (arg0) {
                    case 0:

                        ViewHelper.setAlpha(iv1, 1 - arg1);
                        ViewHelper.setAlpha(iv01, arg1);
                        ViewHelper.setAlpha(iv2, 1 - arg1);
                        ViewHelper.setAlpha(iv02, arg1);

                        iv01.setVisibility(View.VISIBLE);
                        iv02.setVisibility(View.VISIBLE);
                        break;
                    case 1:

                        ViewHelper.setAlpha(iv2, arg1);
                        ViewHelper.setAlpha(iv02, 1 - arg1);
                        ViewHelper.setAlpha(iv3, 1 - arg1);
                        ViewHelper.setAlpha(iv03, arg1);

                        iv02.setVisibility(View.VISIBLE);
                        iv03.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mettingback:
                finish();
                break;
            case R.id.tv1:
                viewPager.setCurrentItem(0);
                break;
            case R.id.tv2:
                viewPager.setCurrentItem(1);
                break;
            case R.id.tv3:
                viewPager.setCurrentItem(2);
                break;
            default:
                break;
            case R.id.earch:
                Intent intent=new Intent();
                intent.setClass(ContactsActivity.this,ConSearch.class);
                ContactsActivity.this.startActivity(intent);
                break;
        }
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
