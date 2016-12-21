package model;

import java.io.Serializable;

/**
 * Created by X on 2016/12/21.
 */

public class APPVersionModel implements Serializable {


    /**
     * id : 1
     * num : 3.5
     * content : 1.第三方登录账号绑定手机号<br />
     2.商圈VIP类型添加<br />
     3.其它相关内容优化<br />
     * create_time : 1482309505
     * status : 0
     * url : https://www.pgyer.com/Mt7K
     */

    private String id;
    private String num;
    private String content;
    private String create_time;
    private String status;
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
