package yc.com.pinyin_study.study.model.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;

import rx.Observable;
import yc.com.base.BaseEngine;
import yc.com.pinyin_study.base.constant.UrlConfig;
import yc.com.pinyin_study.study.model.domain.VowelInfoWrapper;

/**
 * Created by wanglin  on 2018/11/1 09:33.
 */
public class StudyVowelEngine extends BaseEngine {
    public StudyVowelEngine(Context context) {
        super(context);
    }

    public Observable<ResultInfo<VowelInfoWrapper>> getVowelInfos() {

        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.vowel_all_url, new TypeReference<ResultInfo<VowelInfoWrapper>>() {
        }.getType(), null, true, true, true);

    }
}
