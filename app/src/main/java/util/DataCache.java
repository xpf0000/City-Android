package util;
import com.robin.lazy.cache.CacheLoaderManager;

import model.UserModel;

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
        }

        XNetUtil.APPPrintln("User: "+User.toString());

    }
}
