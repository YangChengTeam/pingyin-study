package yc.com.pinyin_study.base.model.engine;

import android.content.Context;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import yc.com.rthttplibrary.bean.ResultInfo;

/**
 * Created by wanglin  on 2018/11/1 15:01.
 */
public class BasePhoneEngine extends BaseEngine {
    public BasePhoneEngine(Context context) {
        super(context);
    }

    public Observable<ResultInfo<String>> uploadPhone(String mobile) {

        return request.uploadPhone(mobile).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }
}
