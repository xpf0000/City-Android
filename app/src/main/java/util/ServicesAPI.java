package util;

import java.util.List;

import model.APPVersionModel;
import model.ActityModel;
import model.BannerModel;
import model.ChongzhiModel;
import model.GoodsModel;
import model.GroupModel;
import model.HFBModel;
import model.HouseModel;
import model.NewsModel;
import model.QuanModel;
import model.RenzhengModel;
import model.UserModel;
import model.YouhuiquanModel;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by X on 2016/10/1.
 */

public interface ServicesAPI {

 String APPUrl = "http://182.92.70.85/hfapi/Public/Found/";

    @POST("?service=jifen.getproductList")
    Observable<HttpResult<List<GoodsModel>>> jifenGetproductList(
            @Query("page") int page,
            @Query("perNumber") int perNumber
    );

    @POST("?service=jifen.getUinfo")
    Observable<HttpResult<List<UserModel>>> jifenGetUinfo(
                    @Query("uid") String uid,
                    @Query("username") String username
            );

    @POST("?service=jifen.addQiandao")
    Observable<HttpResult<Object>> jifenAddQiandao(
            @Query("uid") String uid,
            @Query("username") String username
    );

    @POST("?service=jifen.addDH")
    Observable<HttpResult<Object>> jifenAddDH(
            @Query("uid") String uid,
            @Query("username") String username,
            @Query("id") String id
    );

   @GET("?service=news.getGuanggao&typeid=111")
   Observable<HttpResult<List<BannerModel>>> getBanner();

    @GET("?service=Jifen.getDHList")
    Observable<HttpResult<List<HFBModel>>> jifenGetDHList(
            @Query("uid") String uid,
            @Query("username") String username,
            @Query("page") int page,
            @Query("perNumber") int perNumber
    );

    @GET("?service=jifen.gethfblist")
    Observable<HttpResult<List<HFBModel>>> jifenGethfblist(
            @Query("uid") String uid,
            @Query("username") String username,
            @Query("page") int page,
            @Query("perNumber") int perNumber
    );

    @GET("?service=Jifen.getQDPM")  //签到排行
    Observable<HttpResult<List<HFBModel>>> JifenGetQDPM(
            @Query("uid") String uid,
            @Query("page") int page,
            @Query("perNumber") int perNumber
    );

    @GET("?service=jifen.gethfbpm")  //财富排行
    Observable<HttpResult<List<HFBModel>>> jifenethfbpm(
            @Query("uid") String uid,
            @Query("page") int page,
            @Query("perNumber") int perNumber
    );


    @GET("?service=Jifen.getUYHQList")  //我的优惠券
    Observable<HttpResult<List<YouhuiquanModel>>> JifenGetUYHQList(
            @Query("uid") String uid,
            @Query("page") int page,
            @Query("perNumber") int perNumber
    );

    @GET("?service=jifen.getYHQList")  //商家优惠券
    Observable<HttpResult<List<YouhuiquanModel>>> jifenGetYHQList(
            @Query("shopid") String shopid,
            @Query("uid") String uid,
            @Query("page") int page,
            @Query("perNumber") int perNumber
    );

    @GET("?service=hyk.getShopVIPInfo")  //商家认证信息
    Observable<HttpResult<List<RenzhengModel>>> hykGetShopVIPInfo(
            @Query("id") String id
    );

    @POST("?service=user.getOrLine")//检测token
    Observable<HttpResult<Object>> userGetOrLine(@Query("token") String token);


    @GET("?service=hyk.getShopTJList")  //商圈推荐商家
    Observable<HttpResult<List<GroupModel>>> hykGetShopTJList(
            @Query("page") int page,
            @Query("perNumber") int perNumber
    );

    @GET("?service=Hyk.getShopSearch")  //商圈商家搜索
    Observable<HttpResult<List<GroupModel>>> hykGetShopSearch(
            @Query("keyword") String keyword,
            @Query("page") int page,
            @Query("perNumber") int perNumber
    );

    @GET("?service=Hyk.getCardProduct")  //会员卡充值金额列表
    Observable<HttpResult<List<ChongzhiModel>>> hykGetCardProduct(
            @Query("id") String id
    );

    @GET("?service=Jifen.getUYHQList")  //用户领取的商家优惠列表
    Observable<HttpResult<List<YouhuiquanModel>>> jifenGetUYHQList(
            @Query("shopid") String shopid,
            @Query("uid") String uid,
            @Query("page") int page,
            @Query("perNumber") int perNumber
    );

    @POST("?service=Hyk.paySign")  //支付
    Observable<HttpResult<Object>> hykPaySign(
            @Query("uid") String uid,
            @Query("username") String username,
            @Query("mcardid") String mcardid,
            @Query("cpid") String cpid,
            @Query("yhqid") String yhqid
    );

    @GET("?service=News.getArticle&id=6892")  //怀府币规则
    Observable<HttpResult<List<NewsModel>>> newsGetArticle();


    @GET("?service=Quan.getMyList")  //用户领取的商家优惠列表
    Observable<HttpResult<List<QuanModel>>> quanGetMyList(
            @Query("uid") String uid,
            @Query("page") int page,
            @Query("perNumber") int perNumber
    );

    @GET("?service=User.getUser")  //获取用户信息
    Observable<HttpResult<List<UserModel>>> userGetUser(
            @Query("username") String username
    );

    @POST("?service=User.updateHouse")  //设置默认房屋
    Observable<HttpResult<Object>> userUpdateHouse(
            @Query("uid") String uid,
            @Query("username") String username,
            @Query("houseid") String houseid,
            @Query("fanghaoid") String fanghaoid
    );

    @POST("?service=User.delHouse")  //删除房屋
    Observable<HttpResult<Object>> userDelHouse(
            @Query("uid") String uid,
            @Query("username") String username,
            @Query("id") String id
    );

    @GET("?service=user.getHouseList")  //用户房屋列表
    Observable<HttpResult<List<HouseModel>>> userGetHouseList(
            @Query("uid") String uid,
            @Query("username") String username,
            @Query("page") int page,
            @Query("perNumber") int perNumber
    );

    @GET("?service=Discount.getArticle")  //商家活动详情
    Observable<HttpResult<List<ActityModel>>> discountGetArticle(
            @Query("id") String id
    );

 @GET("?service=Jifen.addYHQ")  //领取优惠券
 Observable<HttpResult<Object>> jifenAddYHQ(
         @Query("uid") String uid,
         @Query("username") String username,
         @Query("yhqid") String yhqid
 );

 @GET("?service=User.openLogin")  //领取优惠券
 Observable<HttpResult<List<UserModel>>> userOpenLogin(
         @Query("openid") String openid,
         @Query("type") String type
 );


 @POST("?service=User.smsSend")  //发送验证码
 Observable<HttpResult<Object>> userSmsSend(
         @Query("mobile") String mobile,
         @Query("type") String type
 );

 @POST("?service=User.openRegister")  //第三方登录注册
 Observable<HttpResult<List<UserModel>>> userOpenRegister(
         @Query("openid") String openid,
         @Query("type") String type,
         @Query("nickname") String nickname,
         @Query("sex") String sex,
         @Query("headimage") String headimage,
         @Query("mobile") String mobile,
         @Query("password") String password,
         @Query("code") String code
 );

 @POST("?service=User.openBD")  //第三方登录绑定现有帐号
 Observable<HttpResult<List<UserModel>>> userOpenBD(
         @Query("openid") String openid,
         @Query("type") String type,
         @Query("mobile") String mobile,
         @Query("password") String password
 );

 @GET("?service=Common.getAppVersion")  //获取APP最新版本信息
 Observable<HttpResult<List<APPVersionModel>>> commonGetAppVersion();



}


