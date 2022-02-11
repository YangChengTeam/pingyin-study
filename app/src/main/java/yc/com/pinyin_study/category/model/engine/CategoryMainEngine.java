package yc.com.pinyin_study.category.model.engine;

import android.content.Context;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import yc.com.pinyin_study.base.model.engine.BaseEngine;
import yc.com.pinyin_study.category.model.domain.WeiKeCategoryWrapper;
import yc.com.rthttplibrary.bean.ResultInfo;

/**
 * Created by wanglin  on 2018/10/25 14:01.
 */
public class CategoryMainEngine extends BaseEngine {



    public CategoryMainEngine(Context context) {
        super(context);

    }


    public Observable<ResultInfo<WeiKeCategoryWrapper>> getCategoryInfos(int page, int page_size, String pid) {


        return request.getCategoryInfos(pid, page, page_size).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());


    }

    public Observable<ResultInfo<WeiKeCategoryWrapper>> getSpellInfos(int page, int page_size) {

        return request.getSpellInfos(page,page_size).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
