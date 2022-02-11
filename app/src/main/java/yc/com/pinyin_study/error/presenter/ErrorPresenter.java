package yc.com.pinyin_study.error.presenter;

import android.content.Context;

import yc.com.base.BasePresenter;
import yc.com.pinyin_study.base.observer.BaseCommonObserver;
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

        mEngine.getErrorInfoList().subscribe(new BaseCommonObserver<ErrorInfoList>(mContext) {
            @Override
            public void onSuccess(ErrorInfoList errorInfoListResultInfo, String message) {
                if (errorInfoListResultInfo != null && errorInfoListResultInfo.getList() != null) {
                    mView.showErrorList(errorInfoListResultInfo.getList());
                    mView.hide();
                } else {
                    mView.showNoNet();
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                mView.showNoNet();
            }



            @Override
            public void onRequestComplete() {

            }
        });


    }

    @Override
    public void getErrorInfo(String id) {
        mView.showLoading();
        mEngine.getErrorInfo(id).subscribe(new BaseCommonObserver<ErrorInfo>(mContext) {
            @Override
            public void onSuccess(ErrorInfo errorInfoResultInfo, String message) {
                if (errorInfoResultInfo != null) {
                    mView.hide();
                    mView.showErrorDetail(errorInfoResultInfo);
                } else {
                    mView.showNoData();
                }

            }

            @Override
            public void onFailure(int code, String errorMsg) {
                mView.showNoNet();
            }


            @Override
            public void onRequestComplete() {

            }
        });

    }
}
