package citycircle.com.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.update.UmengUpdateAgent;

import java.util.ArrayList;

import citycircle.com.Activity.AttaFragment;
import citycircle.com.Activity.CamFragment;
import citycircle.com.Activity.LocalFragment;
import citycircle.com.Activity.Logn;
import citycircle.com.Activity.RecommFragment;
import citycircle.com.Activity.SearchNews;
import citycircle.com.MyAppService.LocationApplication;
import citycircle.com.R;
import util.XActivityindicator;
import util.XNetUtil;

import static citycircle.com.MyAppService.LocationApplication.APPDataCache;
import static citycircle.com.MyAppService.LocationApplication.APPService;

/**
 * Created by admins on 2015/11/14.
 */
public class HomeFragment extends Fragment {
    View view;
    ArrayList<Fragment> arrayList=new ArrayList<>();
    ViewPager viewPager;
    SectionsPagerAdapter adapter;
    TabLayout tabLayout;
    AttaFragment attaFragment;
    LocalFragment localFragment;
    RecommFragment recommFragment;
    CamFragment camFragment;
    ImageView search;
    TextView qiandao;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_layout, container, false);
        UmengUpdateAgent.update(getActivity());
//        PushAgent mPushAgent = PushAgent.getInstance(getActivity());
//        mPushAgent.enable();
        setView();
        return view;
    }
    private void setView(){

        qiandao = (TextView) view.findViewById(R.id.qiandao) ;
        search=(ImageView)view.findViewById(R.id.search) ;
        viewPager=(ViewPager)view.findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(4);
        adapter=new SectionsPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout = (TabLayout)view. findViewById(R.id.mytabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(getActivity(), SearchNews.class);
                getActivity().startActivity(intent);
            }
        });

        qiandao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doQD(v);
            }
        });
    }

    private void doQD(final View v)
    {
        String uid = APPDataCache.User.getUid();
        String uname = APPDataCache.User.getUsername();

        if(uid.equals(""))
        {
            Intent intent = new Intent();
            intent.setClass(getActivity(), Logn.class);
            getActivity().startActivity(intent);
            return;
        }

        LocationApplication.context = getActivity();
        XActivityindicator.create(getActivity()).show();
        v.setEnabled(false);
        XNetUtil.Handle(APPService.jifenAddQiandao(uid,uname), "签到成功", "签到失败", new XNetUtil.OnHttpResult<Boolean>() {
            @Override
            public void onError(Throwable e) {
                XNetUtil.APPPrintln(e);
                v.setEnabled(true);
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                v.setEnabled(!aBoolean);
            }
        });
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {
//        ArrayList<Fragment> arrayList=new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
//            this.arrayList=arrayList;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    if (null == recommFragment)
                        recommFragment = recommFragment.instance();
                    return recommFragment;

                case 1:
                    if (null == localFragment)
                        localFragment = localFragment.instance();
                    return localFragment;

                case 2:
                    if (null == attaFragment)
                        attaFragment = attaFragment.instance();
                    return attaFragment;

                case 3:
                    if (null == camFragment)
                        camFragment = camFragment.instance();
                    return camFragment;
                default:
                    break;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "推荐";
                case 1:
                    return "本地";
                case 2:
                    return "关注";
                case 3:
                    return "活动";
            }
            return null;
        }
    }
}
