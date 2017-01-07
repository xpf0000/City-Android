package citycircle.com.MyAppService;

import android.content.Context;

import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.CPushMessage;

import org.greenrobot.eventbus.EventBus;

import citycircle.com.Utils.MyEventBus;
import util.XNotificationCenter;

import static citycircle.com.MyAppService.LocationApplication.APPDataCache;

/**
 * Created by admins on 2016/8/9.
 */
public class Message extends MessageReceiver {
    @Override
    protected void onMessage(Context context, CPushMessage cPushMessage) {
        super.onMessage(context, cPushMessage);
        System.out.println("cPushMessage:"+cPushMessage.getTitle());
        int a = APPDataCache.land;
        if (a != 0) {
            EventBus.getDefault().post(
                new MyEventBus("show"));
            APPDataCache.msgshow = true;
        }

        if(cPushMessage.getTitle().equals("账号在其它设备已登陆"))
        {
            if(!APPDataCache.User.getUid().equals(""))
            {
                XNotificationCenter.getInstance().postNotice("AccountLogout",null);
                EventBus.getDefault().post(
                        new MyEventBus("hidden"));
                APPDataCache.msgshow = false;
            }

        }


    }
}
