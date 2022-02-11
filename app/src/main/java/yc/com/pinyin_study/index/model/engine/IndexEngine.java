package yc.com.pinyin_study.index.model.engine;

import android.content.Context;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import yc.com.pinyin_study.base.httpinterface.HttpRequestInterface;
import yc.com.pinyin_study.base.model.engine.BaseEngine;
import yc.com.pinyin_study.index.model.domain.IndexInfoWrapper;
import yc.com.rthttplibrary.bean.ResultInfo;
import yc.com.rthttplibrary.request.RetrofitHttpRequest;

/**
 * Created by wanglin  on 2018/10/29 08:50.
 */
public class IndexEngine extends BaseEngine {

    private final HttpRequestInterface request;

    public IndexEngine(Context context) {
        super(context);
        request = RetrofitHttpRequest.get(context).create(HttpRequestInterface.class);
    }

    public Observable<ResultInfo<IndexInfoWrapper>> getIndexInfo() {

        return request.getIndexInfo().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }


}
