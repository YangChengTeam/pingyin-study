package yc.com.pinyin_study.base.httpinterface;


import android.content.Context;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;
import yc.com.pinyin_study.base.model.domain.GoodInfoWrapper;
import yc.com.pinyin_study.category.model.domain.CourseInfo;
import yc.com.pinyin_study.category.model.domain.WeiKeCategoryWrapper;
import yc.com.pinyin_study.error.model.bean.ErrorInfo;
import yc.com.pinyin_study.error.model.bean.ErrorInfoList;
import yc.com.pinyin_study.index.model.domain.IndexInfoWrapper;
import yc.com.pinyin_study.index.model.domain.UserInfo;
import yc.com.pinyin_study.index.model.domain.UserInfoWrapper;
import yc.com.pinyin_study.mine.model.bean.OrderInfoWrapper;
import yc.com.pinyin_study.pay.alipay.OrderInfo;
import yc.com.pinyin_study.study.model.domain.StudyInfoWrapper;
import yc.com.pinyin_study.study.model.domain.StudyPages;
import yc.com.pinyin_study.study.model.domain.VowelInfoWrapper;
import yc.com.pinyin_study.study_1vs1.model.bean.IndexDialogInfoWrapper;
import yc.com.rthttplibrary.bean.ResultInfo;


/**
 * Created by suns  on 2020/7/10 10:43.
 */
public interface HttpRequestInterface {

    //    "https://www.easy-mock.com/mock/5c36b5d2be0a1c39bfd57589/img/beauty/page=1"
    @GET
    Observable<ResponseBody> getCall(@Url String url);

    /**
     * 首页
     *
     * @return
     */
    @POST("index/init")
    Observable<ResultInfo<IndexInfoWrapper>> getIndexInfo();

    /**
     * 学习详情页
     */
    @FormUrlEncoded
    @POST("index/vowel_detail")
    Observable<ResultInfo<StudyInfoWrapper>> getStudyDetail(@Field("page") int page);

    /**
     * 学习列表
     */
    @POST("index/vowel_lists")
    Observable<ResultInfo<StudyPages>> getStudyPages();

    /**
     * 微课主页
     */
    @FormUrlEncoded
    @POST("index/weike_list")
    Observable<ResultInfo<WeiKeCategoryWrapper>> getCategoryInfos(@Field("pid") String pid, @Field("page") int page, @Field("page_size") int pageSize);

    /**
     * 微课列表
     */
    @FormUrlEncoded
    @POST("weike/weike_list")
    Observable<ResultInfo<WeiKeCategoryWrapper>> getSpellInfos(@Field("page") int page, @Field("page_size") int page_size);

    /**
     * 微课详情
     */
    @FormUrlEncoded
    @POST("index/weike_info")
    Observable<ResultInfo<CourseInfo>> getWeikeCategoryInfo(@Field("id") String id, @Field("page") int page, @Field("page_size") int page_size);

    /**
     * 易错知识点
     */
    @POST("pinyin/lists")
    Observable<ResultInfo<ErrorInfoList>> getErrorInfoList();

    /**
     * 易错知识点详情
     */
    @FormUrlEncoded
    @POST("pinyin/detail")
    Observable<ResultInfo<ErrorInfo>> getErrorInfo(@Field("id") String id);

    /**
     * vip信息
     */
    @POST("index/vip_list")
    Observable<ResultInfo<GoodInfoWrapper>> getVipInfoList();

    /**
     * 创建订单
     */
    @FormUrlEncoded
    @POST("orders/pay")
    Observable<ResultInfo<OrderInfo>> createOrder(@Field("goods_num") int goods_num, @Field("payway_name") String payway_name, @Field("money") String money, @Field("goods_id") String goods_id, @Field("user_id") String user_id);

    /**
     * 是否绑定手机号
     */
    @FormUrlEncoded
    @POST("index/is_bind_mobile")
    Observable<ResultInfo<String>> isBindPhone(@Field("user_id") String user_id);

    /**
     * 所有的元音数据
     */
    @POST("index/vowel_all")
    Observable<ResultInfo<VowelInfoWrapper>> getVowelInfos();

    /**
     * 上传手机号码
     */
    @FormUrlEncoded
    @POST("index/upd_user_info")
    Observable<ResultInfo<String>> uploadPhone(@Field("mobile") String mobile);

    /**
     * 首页广告
     */
    @POST("index/menu_adv")
    Observable<ResultInfo<IndexDialogInfoWrapper>> getIndexMenuInfo();

    /**
     * 手机号密码登录
     */
    @FormUrlEncoded
    @POST("user/login")
    Observable<ResultInfo<UserInfoWrapper>> login(@Field("username") String username, @Field("pwd") String password);

    /**
     * 发送验证码
     */
    @FormUrlEncoded
    @POST("user/send_code")
    Observable<ResultInfo<String>> sendCode(@Field("mobile") String phone);

    /**
     * 手机号注册
     */
    @FormUrlEncoded
    @POST("user/reg")
    Observable<ResultInfo<UserInfoWrapper>> register(@Field("mobile") String mobile, @Field("code") String code, @Field("pwd") String pwd);

    /**
     * 修改密码
     *
     * @param pwd    原密码
     * @param newPwd 新密码
     * @return
     */
    @FormUrlEncoded
    @POST("user/upd_pwd")
    Observable<ResultInfo<UserInfo>> modifyPwd(@Field("user_id") String user_id, @Field("pwd") String pwd, @Field("new_pwd") String newPwd);

    /**
     * 验证码登录
     */
    @FormUrlEncoded
    @POST("user/code_login")
    Observable<ResultInfo<UserInfoWrapper>> codeLogin(@Field("mobile") String mobile, @Field("code") String code);

    /**
     * 设置密码
     */
    @FormUrlEncoded
    @POST("user/set_pwd")
    Observable<ResultInfo<String>> setPwd(@Field("user_id") String user_id, @Field("new_pwd") String pwd);

    /**
     * 订单列表
     */
    @FormUrlEncoded
    @POST("orders/lists")
    Observable<ResultInfo<OrderInfoWrapper>> getOrderInfoList(@Field("user_id") String user_id, @Field("page") int page, @Field("page_size") int page_size);

    /**
     * 分享解锁接口
     */
    @FormUrlEncoded
    @POST("share/reward")
    Observable<ResultInfo<String>> share();

    //    @GET("mock/{id}/img/beauty/page={page}")
//    Observable<ResultInfo<Demo>> getCall1(@Path("id") String id, @Path("page") int page);
//
//    @FormUrlEncoded
//    @POST("login")
//    Observable<ResultInfo<Demo>> postCall(@FieldMap Map<String, String> params);


//    @POST("index/index")
//    Observable<ResultInfo<IndexInfo>> getIndexInfo();
//
//
//    @POST("index/index")
//    Observable<ResultInfo<IndexInfo>> getIndexInfo1(@Body RequestBody body);
//
//    // params["category_id"] = id
//    //        params["page"] = "$page"
//    //        params["page_size"] = "$pageSize"
//    @FormUrlEncoded
//    @POST("article.example/ts_lists")
//    Observable<ResultInfo<ExampDataBean>> getExampleList(@Field("category_id") String category_id, @Field("page") String page, @Field("page_size") int page_size);
//
//
//    /**
//     * 获取导师详情
//     *
//     * @param tutor_id
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("tutor.tutor/desp")
//    Observable<ResultInfo<TutorDetailInfo>> getTutorDetailInfo(@Field("tutor_id") String tutor_id);
//
//    /**
//     * 获取导师服务列表
//     *
//     * @param tutor_id
//     * @param page
//     * @param page_sie
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("tutor.tutor/service")
//    Observable<ResultInfo<List<TutorServiceInfo>>> getTutorServiceList(@Field("tutor_id") String tutor_id, @Field("page") int page, @Field("page_sie") int page_sie);
//
//    @FormUrlEncoded
//    @POST("article.article/detail")
//    Observable<ResultInfo<LoveByStagesDetailsBean>> getArticleDetail(@Field("article_id") String article_id, @Field("user_id") String user_id, @Field("isrsa") boolean isrsa, @Field("encrypt_response") boolean encrypt_response);
//
//    @Multipart
//    @POST("user.upload/upimg")
//    Observable<ResultInfo<UploadPhotoBean>> uploadImg(@Part MultipartBody.Part file);
}
