package util;

import java.util.List;

import model.BannerModel;
import model.GoodsModel;
import model.UserModel;
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




}


