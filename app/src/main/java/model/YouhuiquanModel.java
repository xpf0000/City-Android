package model;

import util.DateUtils;

/**
 * Created by X on 2016/11/7.
 */

public class YouhuiquanModel {

    private String id="";
    private String money="";
    private String s_time="";
    private String e_time="";
    private String s_money="";
    private int orlq=0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getS_time() {

        if(!s_time.contains("-"))
        {
            s_time = DateUtils.unixToStr(s_time,"yyyy-MM-dd");
        }

        return s_time;
    }

    public void setS_time(String s_time) {
        this.s_time = s_time;
    }

    public String getE_time() {

        if(!e_time.contains("-"))
        {
            e_time = DateUtils.unixToStr(e_time,"yyyy-MM-dd");
        }

        return e_time;
    }

    public void setE_time(String e_time) {
        this.e_time = e_time;
    }

    public String getS_money() {
        return s_money;
    }

    public void setS_money(String s_money) {
        this.s_money = s_money;
    }

    public int getOrlq() {
        return orlq;
    }

    public void setOrlq(int orlq) {
        this.orlq = orlq;
    }
}
