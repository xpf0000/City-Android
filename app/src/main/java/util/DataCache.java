package util;
import com.robin.lazy.cache.CacheLoaderManager;

import citycircle.com.MyAppService.LocationApplication;
import citycircle.com.Utils.PreferencesUtils;
import model.UserModel;

import static citycircle.com.MyAppService.LocationApplication.context;

/**
 * Created by X on 2016/10/2.
 */

public class DataCache {

    public UserModel User = new UserModel();

    public boolean msgshow = false;

    public DataCache() {

        UserModel model= CacheLoaderManager.getInstance().loadSerializable("UserModel");

        if(model != null)
        {
            XNetUtil.APPPrintln("UserModel readed!!!!!!!!!!!!!");
            User = model;
            User.getUinfo();
            User.getUser();
        }
        else
        {
            ModelUtil.reSet(User);

            String username = PreferencesUtils.getString(context, "username");
            String uid = PreferencesUtils.getString(context, "userid");
            String hid = PreferencesUtils.getString(context, "houseid");
            String fid = PreferencesUtils.getString(context, "fanghaoid");

            if(username != null && uid != null)
            {
                User.setUid(uid);
                User.setUsername(username);
                User.setHouseid(hid);
                User.setFanghaoid(fid);

                User.getUinfo();
                User.getUser();
            }



        }

        XNetUtil.APPPrintln("User: "+User.toString());

    }
}
