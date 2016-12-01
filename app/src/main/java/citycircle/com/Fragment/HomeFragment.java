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

import com.readystatesoftware.viewbadger.BadgeView;
import com.umeng.update.UmengUpdateAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import citycircle.com.Activity.AttaFragment;
import citycircle.com.Activity.CamFragment;
import citycircle.com.Activity.LocalFragment;
import citycircle.com.Activity.Logn;
import citycircle.com.Activity.Mymessage;
import citycircle.com.Activity.RecommFragment;
import citycircle.com.Activity.SearchNews;
import citycircle.com.MyAppService.LocationApplication;
import citycircle.com.R;
import citycircle.com.Utils.MyEventBus;
import util.DataCache;
import util.DensityUtil;
import util.XActivityindicator;
import util.XHtmlVC;
import util.XNetUtil;

import static citycircle.com.MyAppService.LocationApplication.APPDataCache;
import static citycircle.com.MyAppService.LocationApplication.APPService;
import static citycircle.com.MyAppService.LocationApplication.SW;
import static citycircle.com.MyAppService.LocationApplication.context;

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
    ImageView search,message;
    TextView qiandao;
    private TextView badge;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_layout, container, false);

        EventBus.getDefault().register(this);

        UmengUpdateAgent.update(getActivity());
//        PushAgent mPushAgent = PushAgent.getInstance(getActivity());
//        mPushAgent.enable();
        setView();

        APPDataCache.User.getMsgCount();

        return view;
    }
    private void setView(){

        qiandao = (TextView) view.findViewById(R.id.qiandao) ;
        search=(ImageView)view.findViewById(R.id.search) ;
        message=(ImageView)view.findViewById(R.id.message) ;
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

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(getActivity(), Mymessage.class);
                getActivity().startActivity(intent);
            }
        });

        qiandao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doQD(v);
            }
        });

        badge = (TextView) view.findViewById(R.id.badge) ;
        badge.setVisibility(View.INVISIBLE);

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

        if(APPDataCache.User.getOrqd() == 1)
        {
            Intent intent = new Intent();
            intent.setClass(getActivity(), XHtmlVC.class);
            intent.putExtra("url","file:///android_asset/index.html?uid="+uid+"&uname="+uname);
            intent.putExtra("title","每日签到");
            getActivity().startActivity(intent);

            return;
        }

        XActivityindicator.create(getActivity()).show();
        v.setEnabled(false);
        XNetUtil.Handle(APPService.jifenAddQiandao(uid,uname), "签到成功,获得1怀府币", "签到失败", new XNetUtil.OnHttpResult<Boolean>() {
            @Override
            public void onError(Throwable e) {
                XNetUtil.APPPrintln(e);
                v.setEnabled(true);
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                v.setEnabled(true);
                if(aBoolean)
                {
                    APPDataCache.User.setOrqd(1);
                }
            }
        });
    }

    @Subscribe
    public void getEventmsg(MyEventBus myEventBus) {
        if (myEventBus.getMsg().equals("show")) {
            badge.setVisibility(View.VISIBLE);
        } else {
            badge.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
