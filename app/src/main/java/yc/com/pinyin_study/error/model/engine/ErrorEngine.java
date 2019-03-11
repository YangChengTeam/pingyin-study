package yc.com.pinyin_study.error.model.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import yc.com.base.BaseEngine;
import yc.com.pinyin_study.base.constant.UrlConfig;
import yc.com.pinyin_study.error.model.bean.ErrorInfo;
import yc.com.pinyin_study.error.model.bean.ErrorInfoList;

/**
 * Created by wanglin  on 2019/3/7 16:56.
 */
public class ErrorEngine extends BaseEngine {
    public ErrorEngine(Context context) {
        super(context);
    }


    public Observable<ResultInfo<ErrorInfoList>> getErrorInfoList() {
        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.error_list, new TypeReference<ResultInfo<ErrorInfoList>>() {
        }.getType(), null, true, true, true);
    }


    public Observable<ResultInfo<ErrorInfo>> getErrorInfo(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);

        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.error_detail_info, new TypeReference<ResultInfo<ErrorInfo>>() {
        }.getType(), params, true, true, true);
    }
}
