package util;
import com.robin.lazy.cache.CacheLoaderManager;

import model.UserModel;

/**
 * Created by X on 2016/10/2.
 */

public class DataCache {

    public UserModel User = new UserModel();

    public DataCache() {

        UserModel model= CacheLoaderManager.getInstance().loadSerializable("UserModel");

        if(model != null)
        {
            User = model;
        }
        else
        {
            ModelUtil.reSet(User);
        }

        XNetUtil.APPPrintln("User: "+User.toString());

    }
}
