package util;

import java.util.List;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by X on 2016/10/1.
 */

public interface ServicesAPI {

 String APPUrl = "http://182.92.70.85/hfshopapi/Public/Found/";

    @POST("?service=User.login")
    Observable<HttpResult<List<UserModel>>> doLogin(@Query("mobile") String mobile, @Query
            ("password") String password);

    @POST("?service=User.register")
    Observable<HttpResult<Object>> userRegister(@Query("mobile") String mobile, @Query
            ("truename") String truename);


 @POST("?service=User.getUserInfoM")//根据手机号获取会员信息
    Observable<HttpResult<List<UserModel>>> userGetUserInfoM(@Query("mobile") String mobile);

    @POST("?service=User.smsSend")//发送验证码  type: 1 注册 2 找回密码
    Observable<HttpResult<Object>> userSmsSend(@Query("mobile") String mobile, @Query("type") String type);

   @POST("?service=User.updateMobile")//修改用户手机号
   Observable<HttpResult<Object>> userUpdateMobile(@Query("mobile") String mobile
           , @Query("newmobile") String newmobile
           , @Query("code") String code
   );

    @POST("?service=User.updatePass2")//修改用户密码
    Observable<HttpResult<Object>> userUpdatePass2(@Query("mobile") String mobile
            , @Query("oldpass") String oldpass
            , @Query("newpass") String newpass
    );

    @POST("?service=User.smsVerify")//短信验证
    Observable<HttpResult<Object>> userSmsVerify(@Query("mobile") String mobile
            , @Query("code") String code
    );

    @POST("?service=User.updatePass")//重置密码
    Observable<HttpResult<Object>> userUpdatePass(@Query("mobile") String mobile
            , @Query("code") String code
            , @Query("password") String password
    );

    @POST("?service=Shopd.updateShopInfo")//修改店铺资料
    Observable<HttpResult<Object>> shopdUpdateShopInfo(@Query("id") String sid
            , @Query("address") String address
            , @Query("tel") String tel
    );

    @POST("?service=Shopd.getShopInfo")//获取商家详情
    Observable<HttpResult<List<UserModel>>> shopdGetShopInfo(@Query("id") String sid);

    @POST("?service=Power.addShopJob")//添加岗位
    Observable<HttpResult<Object>> powerAddShopJob(@Query("shopid") String shopid
            , @Query("name") String name
    );

    @POST("?service=Power.updateShopJob")//修改岗位
    Observable<HttpResult<Object>> powerUpdateShopJob(@Query("id") String id
            , @Query("name") String name
    );

}


