package yc.com.pinyin_study.base.model.engine;

import android.content.Context;

import yc.com.pinyin_study.base.httpinterface.HttpRequestInterface;
import yc.com.rthttplibrary.request.RetrofitHttpRequest;

/**
 * Created by wanglin  on 2018/10/25 13:54.
 */
public class BaseEngine {

    protected Context mContext;
    protected HttpRequestInterface request;

    public BaseEngine(Context context) {
        this.mContext = context;
        request = RetrofitHttpRequest.get(context).create(HttpRequestInterface.class);
    }
}
