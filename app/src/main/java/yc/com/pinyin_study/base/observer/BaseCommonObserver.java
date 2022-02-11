package yc.com.pinyin_study.base.observer;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.util.Log;

import yc.com.pinyin_study.pay.alipay.LoadingDialog;
import yc.com.rthttplibrary.converter.BaseObserver;

/**
 * Created by suns  on 2020/7/25 10:35.
 */
public abstract class BaseCommonObserver<T> extends BaseObserver<T, LoadingDialog> {

    public BaseCommonObserver(LoadingDialog view) {
        super(view);
    }

    public BaseCommonObserver(Context context) {
        this(context, "加载中...");
    }

    public BaseCommonObserver(Context context, String mess) {
        super(null);
        Log.e("TAG", "BaseCommonObserver: " + Thread.currentThread().getName() + "--" + Looper.getMainLooper());
//        if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
//            Looper.prepare();
//
//            Looper.loop();
//        }
        if (Looper.myLooper() == null) return;
        LoadingDialog loadDialog = new LoadingDialog(context);
        loadDialog.setText(mess);
        this.view = loadDialog;
    }

    @Override
    protected boolean isShow() {
        return false;
    }
}
