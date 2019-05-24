package yc.com.pinyin_study.study.utils;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.kk.securityhttp.net.contains.HttpConfig;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import yc.com.pinyin_study.base.constant.UrlConfig;
import yc.com.pinyin_study.base.model.domain.GoodInfoWrapper;
import yc.com.pinyin_study.base.widget.MainToolBar;
import yc.com.pinyin_study.index.utils.UserInfoHelper;
import yc.com.pinyin_study.pay.alipay.OrderInfo;
import yc.com.pinyin_study.study.model.domain.StudyPages;
import yc.com.pinyin_study.study_1vs1.model.bean.IndexDialogInfoWrapper;

/**
 * Created by wanglin  on 2018/11/5 16:20.
 */
public class EngineUtils {

    public static Observable<ResultInfo<OrderInfo>> createOrder(Context context, int goods_num, String payway_name, String money, String goods_id) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", UserInfoHelper.getUid());
        params.put("goods_num", goods_num + "");
        params.put("payway_name", payway_name);
        params.put("app_id", String.valueOf(7));
        params.put("money", money);
        params.put("goods_id", goods_id);
//        params.put("goods_list", JSON.toJSONString(goods_list));
        return HttpCoreEngin.get(context).rxpost(UrlConfig.pay_url, new TypeReference<ResultInfo<OrderInfo>>() {
        }.getType(), params, true, true, true);

    }


    public static Observable<ResultInfo<String>> isBindPhone(Context context) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", UserInfoHelper.getUid());

        return HttpCoreEngin.get(context).rxpost(UrlConfig.is_bind_mobile_url, new TypeReference<ResultInfo<String>>() {
        }.getType(), params, true, true, true);
    }


    public static Observable<ResultInfo<GoodInfoWrapper>> getVipInfoList(Context context) {

        return HttpCoreEngin.get(context).rxpost(UrlConfig.vip_info_url, new TypeReference<ResultInfo<GoodInfoWrapper>>() {
        }.getType(), null, true, true, true);

    }


    public static Observable<ResultInfo<IndexDialogInfoWrapper>> getIndexMenuInfo(Context context) {

        return HttpCoreEngin.get(context).rxpost(UrlConfig.index_menu_url, new TypeReference<ResultInfo<IndexDialogInfoWrapper>>() {
        }.getType(), null, true, true, true);
    }


    public static Observable<ResultInfo<StudyPages>> getStudyPages(Context context){
        return HttpCoreEngin.get(context).rxpost(UrlConfig.study_list_url, new TypeReference<ResultInfo<StudyPages>>() {
        }.getType(), null, true, true, true);
    }



}
