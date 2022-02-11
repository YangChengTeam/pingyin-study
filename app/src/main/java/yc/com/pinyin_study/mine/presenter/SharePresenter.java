package yc.com.pinyin_study.mine.presenter;

import android.content.Context;

import com.hwangjr.rxbus.RxBus;

import yc.com.base.BasePresenter;
import yc.com.blankj.utilcode.util.SPUtils;
import yc.com.pinyin_study.base.constant.BusAction;
import yc.com.pinyin_study.base.observer.BaseCommonObserver;
import yc.com.pinyin_study.mine.contract.ShareContract;
import yc.com.pinyin_study.mine.model.engine.ShareEngine;
import yc.com.rthttplibrary.config.GoagalInfo;

/**
 * Created by wanglin  on 2019/5/10 10:35.
 */
public class SharePresenter extends BasePresenter<ShareEngine, ShareContract.View> implements ShareContract.Presenter {
    public SharePresenter(Context context, ShareContract.View view) {
        super(context, view);
        mEngine = new ShareEngine(context);
    }

    @Override
    public void loadData(boolean isForceUI, boolean isLoadingUI) {

    }


    public void share() {
        mEngine.share().subscribe(new BaseCommonObserver<String>(mContext) {


            @Override
            public void onSuccess(String data, String message) {
                RxBus.get().post(BusAction.SHARE_SUCCESS, "success");
                SPUtils.getInstance().put(GoagalInfo.get().uuid, true);
            }

            @Override
            public void onFailure(int code, String errorMsg) {

            }

            @Override
            public void onRequestComplete() {

            }


        });

    }
}
