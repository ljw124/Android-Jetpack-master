package com.ljw.selfmvvm.net;

import com.ljw.selfmvvm.bean.BannerBean;
import com.ljw.selfmvvm.bean.HomeCollectionBean;
import com.ljw.selfmvvm.bean.UserBean;
import com.ljw.selfmvvm.bean.basebean.ResponModel;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Create by Ljw on 2019/12/13 10:44
 * Retrofit 接口请求配置都在这
 */
public interface RetrofitApiService {

    //轮播banner的接口
    @GET("banner/json")
    Observable<ResponModel<List<BannerBean>>> getBanner();

    //首页文章,curPage拼接。从0开始
    @GET("article/list/{curPage}/json")
    Observable<ResponModel<HomeCollectionBean>> getHomeArticles(@Path("curPage") int curPage);

    //收藏文章列表
    @GET("lg/collect/list/{curPage}/json")
    Observable<ResponModel<HomeCollectionBean>> getCollectArticles(@Path("curPage") int curPage);

    //收藏站内文章
    @POST("lg/collect/{id}/json")
    Observable<ResponModel<String>> collectArticle(@Path("id") int id);

    //收藏站外文章
    @FormUrlEncoded
    @POST("lg/collect/add/json")
    Observable<ResponModel<String>> collectOutArticle(@Field("title") String title, @Field("author") String author, @Field("link") String link);

    //取消收藏 -- 首页文章列表
    @POST("lg/uncollect_originId/{id}/json")
    Observable<ResponModel<String>> unCollectByHome(@Path("id") int id);

    //取消收藏 -- 我的收藏列表
    @FormUrlEncoded
    @POST("lg/uncollect/{id}/json")
    Observable<ResponModel<String>> unCollectByMe(@Path("id") int id,@Field("originId")int originId);

    //退出登录
    @GET("user/logout/json")
    Observable<ResponModel<String>> loginOut();

    @POST("user/login")
    @FormUrlEncoded
    Observable<ResponModel<UserBean>> login(@FieldMap HashMap<String, Object> map);

    //Retrofit get请求
    @GET("xiandu/category/wow")
    Observable<String> getGank(@Query("en_name") String en_name);

    //Retrofit post请求
    @POST("add2gank")
    @FormUrlEncoded
    Observable<ResponseBody> postAddGank(@FieldMap HashMap<String, String> map);

    //Retrofit 上传文件,前面的sequence是单表单@Part("sequence") RequestBody sequence
    //Observable<ResponseBody> uploadPic(@Url String url,@Part("sequence") RequestBody sequence , @Part MultipartBody.Part file);
    // 多表单 @FieldMap Map<String, String> usermaps
    @POST("upload/pic")
    @Multipart
    Observable<ResponseBody> uploadPic(@FieldMap HashMap<String, String> map, @Part MultipartBody.Part file);

    //Retrofit下载文件
    @GET
    @Streaming
    //10以上用@streaming。不会造成oom，反正用就是了
    Observable<ResponseBody> downloadFile(@Url String url);

    @GET
    @Streaming
    Observable<ResponseBody> downloadFile(@Url String url, @Header("RANGE") String range);

}
