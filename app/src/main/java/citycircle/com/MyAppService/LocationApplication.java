package citycircle.com.MyAppService;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.multidex.MultiDex;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import com.robin.lazy.cache.CacheLoaderManager;
import com.robin.lazy.cache.disk.naming.HashCodeFileNameGenerator;

import java.io.File;
import java.io.IOException;

import citycircle.com.R;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import util.DataCache;
import util.ServicesAPI;
import util.XNetUtil;

public class LocationApplication extends Application {
    public LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;
    public double longitude, latitude, a, conlongitude, conlatitude;
    public Vibrator mVibrator;
    private LocationMode tempMode = LocationMode.Battery_Saving;
    private String tempcoor = "bd09ll";
    LocationClientOption locationClientOption;
    public String adress,getStreet;
    public String city,District;
    private static final String TAG = "Init";


    public static int stateBarHeight = 0;
    public static int navBarHeight = 0;

    public static int SW = 0;
    public static int SH = 0;

    public static Context context;

    public static Retrofit retrofit;

    public static ServicesAPI APPService;

    static public DataCache APPDataCache;

    /**
     * 创建全局变量 全局变量一般都比较倾向于创建一个单独的数据类文件，并使用static静态变量
     *
     * 这里使用了在Application中添加数据的方法实现全局变量
     * 注意在AndroidManifest.xml中的Application节点添加android:name=".MyApplication"属性
     *
     */
    private WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();

    public WindowManager.LayoutParams getMywmParams() {
        return wmParams;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                XNetUtil.APPPrintln("onActivityCreated: "+activity);
                context = activity;
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                context = activity;
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });




        context = getApplicationContext();

        CacheLoaderManager.getInstance().init(this, new HashCodeFileNameGenerator(), 1024 * 1024 * 64, 200, 50);

        APPDataCache = new DataCache();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        SW = displayMetrics.widthPixels;
        SH = displayMetrics.heightPixels;

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request request = chain.request().newBuilder()
                        .addHeader("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36")
                        .addHeader("Content-Type","text/plain; charset=utf-8")
                        .addHeader("Accept","*/*")
                        .addHeader("Accept-Encoding","gzip, deflate, sdch")
                        .build();

                XNetUtil.APPPrintln("URL: "+request.url().toString());
                if(request.body() != null)
                {
                    XNetUtil.APPPrintln("Body: "+request.body().toString());
                }

                Response response = chain.proceed(request);

                return response;
            }
        }).build();


        retrofit = new Retrofit.Builder()
                .baseUrl(ServicesAPI.APPUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .callFactory(client)
                .build();

        APPService = retrofit.create(ServicesAPI.class);

        initCloudChannel(this);
        // System.out.println("getApplicationContext()"+getApplicationContext());
        SDKInitializer.initialize(getApplicationContext());
        mLocationClient = new LocationClient(this.getApplicationContext());

        //初始化imgload
        initImageLoader();

        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        locationClientOption = new LocationClientOption();
        locationClientOption.setLocationMode(tempMode);//设置定位模式
        locationClientOption.setCoorType(tempcoor);//返回的定位结果是百度经纬度，默认值gcj02
        locationClientOption.setIsNeedAddress(true);
        mLocationClient.setLocOption(locationClientOption);
        mVibrator = (Vibrator) getApplicationContext().getSystemService(
                Service.VIBRATOR_SERVICE);
        mLocationClient.start();

        System.out.println("================init============");
    }

    //初始化网络图片缓存库
    private void initImageLoader() {
        //网络图片例子,结合常用的图片缓存库UIL,你可以根据自己需求自己换其他网络图片库
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().
                showImageForEmptyUri(R.drawable.app_default)
                .cacheInMemory(true).cacheOnDisk(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext()).defaultDisplayImageOptions(defaultOptions)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);

    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            adress = location.getAddrStr();
            city = location.getCity();
            District=location.getDistrict();
            getStreet=location.getStreet();
            System.out.println("longitude:" + longitude + "latitude:"
                    + latitude);
            System.out.println("adressa:" + adress);
            if (adress!=null){
                mLocationClient.stop();
            }
        }

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.out.println("+++++++++++");
    }
    private void initCloudChannel(Context applicationContext) {
        PushServiceFactory.init(applicationContext);
        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "init cloudchannel success");
            }
            @Override
            public void onFailed(String errorCode, String errorMessage) {
                Log.d(TAG, "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });
    }

}
