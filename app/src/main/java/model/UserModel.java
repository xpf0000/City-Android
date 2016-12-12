package model;

import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.List;

import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.MyEventBus;
import okhttp3.Call;
import util.HttpResult;
import util.ModelUtil;
import util.XAPPUtil;
import util.XNetUtil;
import util.XNotificationCenter;

import static citycircle.com.MyAppService.LocationApplication.APPDataCache;
import static citycircle.com.MyAppService.LocationApplication.APPService;
import static citycircle.com.MyAppService.LocationApplication.context;

/**
 * Created by X on 2016/11/4.
 */

public class UserModel implements Serializable {


    /**
     * uid : 5
     * nickname : cheng
     * headimage : http://7xotdy.com2.z0.glb.qiniucdn.com/2016-06-27_5770e17be8054.jpg
     * qdday : 7
     * hfb : 9
     * wqd : 0
     * orqd : 0
     */



    private String uid;
    private String nickname;
    private String headimage;
    private String qdday;
    private String hfb;
    private String wqd;
    private int orqd;
    /**
     * sex : 0
     * username : 1451106370128
     * openid :
     * mobile : 17719226070
     * houseid : 158
     * fanghaoid : 172
     * truename : Lou
     * louhaoid : 159
     * danyuanid : 169
     * birthday : 1994-04-21
     * address :
     */

    private String sex;
    private String username;
    private String openid;
    private String mobile;
    private String houseid;
    private String fanghaoid;
    private String truename;
    private String louhaoid;
    private String danyuanid;
    private String birthday;
    private String address;
    private String token;

    private String orwsinfo;
    /**
     * id : 5
     * aihao : null
     * qianming : null
     */

    private String id;
    private String aihao;
    private String qianming;

    public String getAihao() {
        aihao = aihao == null ? "" : aihao;
        return aihao;
    }

    public void setAihao(String aihao) {
        this.aihao = aihao;
    }

    public String getQianming() {
        qianming = qianming == null ? "" : qianming;
        return qianming;
    }

    public void setQianming(String qianming) {
        this.qianming = qianming;
    }

    public String getOrwsinfo() {
        orwsinfo = orwsinfo == null ? "0" : orwsinfo;
        return orwsinfo;
    }

    public void setOrwsinfo(String orwsinfo) {
        this.orwsinfo = orwsinfo;
    }

    public String getToken() {
        token = token == null ? "" : token;
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    @Override
    public String toString() {
        return "UserModel{" +
                "uid='" + uid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", headimage='" + headimage + '\'' +
                ", qdday='" + qdday + '\'' +
                ", hfb='" + hfb + '\'' +
                ", wqd='" + wqd + '\'' +
                ", orqd=" + orqd +
                ", sex='" + sex + '\'' +
                ", username='" + username + '\'' +
                ", openid='" + openid + '\'' +
                ", mobile='" + mobile + '\'' +
                ", houseid='" + houseid + '\'' +
                ", fanghaoid='" + fanghaoid + '\'' +
                ", truename='" + truename + '\'' +
                ", louhaoid='" + louhaoid + '\'' +
                ", danyuanid='" + danyuanid + '\'' +
                ", birthday='" + birthday + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public void reSet()
    {
        ModelUtil.reSet(this);
        save();
    }

    public void copy(UserModel u)
    {
        uid = u.uid;
        nickname = u.nickname;
        headimage = u.headimage;
        qdday = u.qdday;
        hfb = u.hfb;
        wqd = u.wqd;
        orqd = u.orqd;
        sex = u.sex;
        username = u.username;
        openid = u.openid;
        mobile = u.mobile;
        houseid = u.houseid;
        fanghaoid = u.fanghaoid;
        truename = u.truename;
        louhaoid = u.louhaoid;
        danyuanid = u.danyuanid;
        birthday = u.birthday;
        address = u.address;
        token = u.token;

        save();
    }

    public void save()
    {
        XNetUtil.APPPrintln(this.toString());
        boolean b = XAPPUtil.SaveAPPCache("UserModel",this);
        XNetUtil.APPPrintln("保存Model: "+b);
    }

    public String getUid() {
        uid = uid == null? "" : uid;
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        nickname = nickname == null? "" : nickname;
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadimage() {
        headimage = headimage == null? "" : headimage;
        return headimage;
    }

    public void setHeadimage(String headimage) {
        this.headimage = headimage;
    }

    public String getQdday() {
        return qdday;
    }

    public void setQdday(String qdday) {
        this.qdday = qdday;
    }

    public String getHfb() {
        return hfb;
    }

    public void setHfb(String hfb) {
        this.hfb = hfb;
    }

    public String getWqd() {
        return wqd;
    }

    public void setWqd(String wqd) {
        this.wqd = wqd;
    }

    public int getOrqd() {
        return orqd;
    }

    public void setOrqd(int orqd) {
        this.orqd = orqd;
    }

    public String getSex() {
        sex = sex == null? "" : sex;
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUsername() {
        username = username == null? "" : username;
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getHouseid() {
        return houseid;
    }

    public void setHouseid(String houseid) {
        this.houseid = houseid;
    }

    public String getFanghaoid() {
        return fanghaoid;
    }

    public void setFanghaoid(String fanghaoid) {
        this.fanghaoid = fanghaoid;
    }

    public String getTruename() {
        truename = truename == null? "" : truename;
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public String getLouhaoid() {
        return louhaoid;
    }

    public void setLouhaoid(String louhaoid) {
        this.louhaoid = louhaoid;
    }

    public String getDanyuanid() {
        return danyuanid;
    }

    public void setDanyuanid(String danyuanid) {
        this.danyuanid = danyuanid;
    }

    public String getBirthday() {
        birthday = birthday == null? "" : birthday;
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        address = address == null? "" : address;
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public void unRegistNotice()
    {
        PushServiceFactory.getCloudPushService().removeAlias(getToken(),new CommonCallback() {
            @Override
            public void onSuccess(String s) {
                XNetUtil.APPPrintln("removeAlias success!!!!!!");
            }

            @Override
            public void onFailed(String s, String s1) {
                XNetUtil.APPPrintln("removeAlias fail!!!!!! "+s+" | "+s1);
            }
        });
    }

    public void registNotice()
    {
        if(getToken().equals(""))
        {
            return;
        }

        PushServiceFactory.getCloudPushService().addAlias(getToken(), new CommonCallback() {
            @Override
            public void onSuccess(String s) {
                XNetUtil.APPPrintln("addAlias success!!!!!!");
            }

            @Override
            public void onFailed(String s, String s1) {
                XNetUtil.APPPrintln("addAlias fail!!!!!! "+s+" | "+s1);
            }
        });

        getMsgCount();

    }

    public void checkToken()
    {
        if(getToken().equals(""))
        {
            return;
        }

        XNetUtil.HandleReturnAll(APPService.userGetOrLine(getToken()), new XNetUtil.OnHttpResult<HttpResult<Object>>() {

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(HttpResult<Object> res) {

                if(res.getData().getCode() == 1)
                {
                    XNotificationCenter.getInstance().postNotice("AccountLogout",null);
                }
            }
        });


    }



    public void getUinfo()
    {
        String uid = getUid();
        String uname = getUsername();

        if(uid.equals("") || uname.equals(""))
        {
            return;
        }

        XNetUtil.Handle(APPService.jifenGetUinfo(uid,uname), new XNetUtil.OnHttpResult<List<UserModel>>() {
            @Override
            public void onError(Throwable e) {

                XNetUtil.APPPrintln("!!!!!! jifenGetUinfo error: "+e);

            }

            @Override
            public void onSuccess(List<UserModel> arrs) {

                XNetUtil.APPPrintln(arrs.toString());

                if(arrs.size() > 0)
                {
                    UserModel u = arrs.get(0);
                    setHfb(u.getHfb());
                    setQdday(u.getQdday());
                    setWqd(u.getWqd());
                    setOrqd(u.getOrqd());
                }

            }
        });
    }

    public void getUser()
    {
        String uname = getUsername();

        if(uname.equals(""))
        {
            return;
        }

        XNetUtil.Handle(APPService.userGetUser(uname), new XNetUtil.OnHttpResult<List<UserModel>>() {
            @Override
            public void onError(Throwable e) {

                XNetUtil.APPPrintln(e);
            }

            @Override
            public void onSuccess(List<UserModel> models) {

                if(models.size() > 0)
                {
                    setHeadimage(models.get(0).getHeadimage());
                    setSex(models.get(0).getSex());
                    setBirthday(models.get(0).getBirthday());
                    setAihao(models.get(0).getAihao());
                    setQianming(models.get(0).getQianming());
                    setMobile(models.get(0).getMobile());
                    setNickname(models.get(0).getNickname());
                    setAddress(models.get(0).getAddress());
                    setTruename(models.get(0).getTruename());
                    save();
                }

            }
        });
    }



    public void getMsgCount() {

        String uid = getUid();
        String uname = getUsername();

        XNetUtil.APPPrintln("getMsgCount uid: "+uid+" | uname: "+uname);

        if(uid.equals("") || uname.equals(""))
        {
            return;
        }

        String url = GlobalVariables.urlstr + "user.getMessagesCount&uid=" + uid + "&username=" + uname;

        XNetUtil.APPPrintln("url: "+url);

        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Toast.makeText(context, R.string.intent_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = JSON.parseObject(response);
                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                if (jsonObject1.getIntValue("code") == 0) {
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("info");

                    try
                    {
                        int c1 = Integer.parseInt(jsonObject2.getString("count1"));
                        int c2 = Integer.parseInt(jsonObject2.getString("count2"));
                        int c3 = Integer.parseInt(jsonObject2.getString("count3"));

                        if(c1+c2+c3 > 0)
                        {
                            EventBus.getDefault().post(
                                    new MyEventBus("show"));
                            APPDataCache.msgshow = true;
                        }
                        else
                        {
                            APPDataCache.msgshow = false;
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }

            }
        });
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
