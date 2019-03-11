package yc.com.pinyin_study.base.model.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import yc.com.base.BaseEngine;
import yc.com.pinyin_study.base.constant.UrlConfig;

/**
 * Created by wanglin  on 2018/11/1 15:01.
 */
public class BasePhoneEngine extends BaseEngine {
    public BasePhoneEngine(Context context) {
        super(context);
    }

    public Observable<ResultInfo<String>> uploadPhone(String mobile) {
        Map<String, String> params = new HashMap<>();
        params.put("mobile", mobile);

        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.upd_info_url, new TypeReference<ResultInfo<String>>() {
        }.getType(), params, true, true, true);

    }
}
