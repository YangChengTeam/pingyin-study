package yc.com.pinyin_study.error.model.engine;

import android.content.Context;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import yc.com.pinyin_study.base.model.engine.BaseEngine;
import yc.com.pinyin_study.error.model.bean.ErrorInfo;
import yc.com.pinyin_study.error.model.bean.ErrorInfoList;
import yc.com.rthttplibrary.bean.ResultInfo;

/**
 * Created by wanglin  on 2019/3/7 16:56.
 */
public class ErrorEngine extends BaseEngine {
    public ErrorEngine(Context context) {
        super(context);
    }


    public Observable<ResultInfo<ErrorInfoList>> getErrorInfoList() {


        return request.getErrorInfoList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<ResultInfo<ErrorInfo>> getErrorInfo(String id) {

        return request.getErrorInfo(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
