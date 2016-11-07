package model;

import java.io.Serializable;

import util.ModelUtil;
import util.XAPPUtil;
import util.XNetUtil;

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
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadimage() {
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
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUsername() {
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
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
