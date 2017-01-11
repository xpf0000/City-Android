package util;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import com.robin.lazy.cache.CacheLoaderManager;

import java.io.Serializable;
import java.util.Date;

import citycircle.com.MyAppService.LocationApplication;
import citycircle.com.R;

public class XAPPUtil {

    public static boolean inRangeOfView(View view, MotionEvent ev){
        int[] location = new int[2];
        view.getLocationOnScreen(location);

        int x = location[0];
        int y = location[1];

        XNetUtil.APPPrintln("location x: "+x+" y: "+y);
        XNetUtil.APPPrintln("width: "+view.getWidth()+" height: "+view.getHeight());

        if(ev.getX() < x || ev.getX() > (x + view.getWidth()) || ev.getY() < y || ev.getY() > (y + view.getHeight())){
            return false;
        }
        return true;
    }

    public static Date serverTime()
    {
        Date now = new Date();
        return  new Date(now.getTime()+LocationApplication.serverTimeInterval);
    }

    public static long serverUnix()
    {
        Date now = new Date();
        return  now.getTime()+LocationApplication.serverTimeInterval;
    }

    public static long serverUnixSecond()
    {
        Date now = new Date();
        return  (now.getTime()+LocationApplication.serverTimeInterval)/1000;
    }

    final static public boolean APPCheckIsLogin()
    {
        return !LocationApplication.APPDataCache.User.getUid().equals("");
    }

    public static boolean isPhone(String str)
    {
        String telRegex = "[1][3578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(str)) return false;
        else return str.matches(telRegex);
    }

    public static boolean isPhone(EditText et)
    {
        if(et == null)
        {
            return false;
        }
        boolean b = false;
        if(et.getText() == null)
        {
            XAPPUtil.nope(et);
            return false;
        }

        b = isPhone(et.getText().toString());

        if(!b)
        {
            XAPPUtil.nope(et);
        }

        return b;

    }

    public static boolean isNull(EditText et)
    {
        if(et == null)
        {
            return false;
        }
        boolean b = false;
        if(et.getText() == null)
        {
            XAPPUtil.nope(et);
            return false;
        }

        b = et.getText().toString().trim().length() > 0;

        if(!b)
        {
            XAPPUtil.nope(et);
        }

        return b;

    }



    public static void nope(View view) {
        int delta = view.getResources().getDimensionPixelOffset(R.dimen.spacing_medium);

        PropertyValuesHolder pvhTranslateX = PropertyValuesHolder.ofKeyframe(View.TRANSLATION_X,
                Keyframe.ofFloat(0f, 0),
                Keyframe.ofFloat(.10f, -delta),
                Keyframe.ofFloat(.26f, delta),
                Keyframe.ofFloat(.42f, -delta),
                Keyframe.ofFloat(.58f, delta),
                Keyframe.ofFloat(.74f, -delta),
                Keyframe.ofFloat(.90f, delta),
                Keyframe.ofFloat(1f, 0f)
        );

        ObjectAnimator.ofPropertyValuesHolder(view, pvhTranslateX).
                setDuration(500).start();
    }

    public static <V extends Serializable> boolean SaveAPPCache(String key, V values){

        if(values instanceof String)
        {
            return CacheLoaderManager.getInstance().saveString(key, (String) values,60*24*30*12*5*10);
        }

        if(values instanceof Serializable)
        {
            return CacheLoaderManager.getInstance().saveSerializable(key,values,60*24*30*12*5*10);
        }

        XNetUtil.APPPrintln("key: "+key+" | values: "+values);

        return  false;
    }

}