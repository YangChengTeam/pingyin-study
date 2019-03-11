package yc.com.pinyin_study.error.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BasePresenter;
import yc.com.pinyin_study.error.contract.ErrorContract;
import yc.com.pinyin_study.error.model.bean.ErrorInfo;
import yc.com.pinyin_study.error.model.bean.ErrorInfoList;
import yc.com.pinyin_study.error.model.engine.ErrorEngine;

/**
 * Created by wanglin  on 2019/3/7 17:18.
 */
public class ErrorPresenter extends BasePresenter<ErrorEngine, ErrorContract.View> implements ErrorContract.Presenter {
    public ErrorPresenter(Context context, ErrorContract.View view) {
        super(context, view);
        mEngine = new ErrorEngine(context);
    }

    @Override
    public void loadData(boolean isForceUI, boolean isLoadingUI) {

    }

    @Override
    public void getErrorInfoList() {
        mView.showLoading();
        Subscription subscription = mEngine.getErrorInfoList().subscribe(new Subscriber<ResultInfo<ErrorInfoList>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showNoNet();
            }

            @Override
            public void onNext(ResultInfo<ErrorInfoList> errorInfoListResultInfo) {
                if (errorInfoListResultInfo != null && errorInfoListResultInfo.code == HttpConfig.STATUS_OK && errorInfoListResultInfo.data != null && errorInfoListResultInfo.data.getList() != null) {
                    mView.showErrorList(errorInfoListResultInfo.data.getList());
                    mView.hide();
                } else {
                    mView.showNoNet();
                }
            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void getErrorInfo(String id) {
        mView.showLoading();
        Subscription subscription = mEngine.getErrorInfo(id).subscribe(new Subscriber<ResultInfo<ErrorInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showNoNet();
            }

            @Override
            public void onNext(ResultInfo<ErrorInfo> errorInfoResultInfo) {
                if (errorInfoResultInfo != null) {
                    if (errorInfoResultInfo.code == HttpConfig.STATUS_OK && errorInfoResultInfo.data != null) {
                        mView.hide();
                        mView.showErrorDetail(errorInfoResultInfo.data);
                    } else {
                        mView.showNoData();
                    }
                } else {
                    mView.showNoNet();
                }
            }
        });
        mSubscriptions.add(subscription);
    }
}
