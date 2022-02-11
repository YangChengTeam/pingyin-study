package yc.com.pinyin_study.category.model.engine;

import android.content.Context;


import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import yc.com.pinyin_study.base.model.engine.BaseEngine;
import yc.com.pinyin_study.category.model.domain.CourseInfo;
import yc.com.rthttplibrary.bean.ResultInfo;

/**
 * Created by zhangkai on 2017/9/6.
 */

public class WeiKeDetailEngine extends BaseEngine {


    public WeiKeDetailEngine(Context context) {
        super(context);

    }

    public Observable<ResultInfo<CourseInfo>> getWeikeCategoryInfo(String id, int page, int page_size) {

//        Map<String, String> params = new HashMap<>();
//        params.put("page", page + "");
//
//        params.put("page_size", page_size + "");
//        params.put("id", id);
//
//
//        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.weike_info_url, new TypeReference<ResultInfo<CourseInfo>>() {
//                }.getType(),
//                params,
//                true,
//                true, true);
        return request.getWeikeCategoryInfo(id, page, page_size).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }


}
