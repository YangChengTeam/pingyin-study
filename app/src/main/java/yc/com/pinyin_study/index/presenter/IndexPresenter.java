package yc.com.pinyin_study.index.presenter;

import android.content.Context;

import yc.com.base.BasePresenter;
import yc.com.pinyin_study.base.observer.BaseCommonObserver;
import yc.com.pinyin_study.index.contract.IndexContract;
import yc.com.pinyin_study.index.model.domain.IndexInfoWrapper;
import yc.com.pinyin_study.index.model.engine.IndexEngine;
import yc.com.pinyin_study.index.utils.ShareInfoHelper;
import yc.com.pinyin_study.index.utils.UserInfoHelper;
import yc.com.pinyin_study.pay.PayWayInfoHelper;

/**
 * Created by wanglin  on 2018/10/29 08:49.
 */
public class IndexPresenter extends BasePresenter<IndexEngine, IndexContract.View> implements IndexContract.Presenter {
    public IndexPresenter(Context context, IndexContract.View view) {
        super(context, view);
        mEngine = new IndexEngine(mContext);
    }

    @Override
    public void loadData(boolean isForceUI, boolean isLoadingUI) {
        if (!isForceUI) return;
        getIndexInfo();
    }


    @Override
    public void getIndexInfo() {

        mEngine.getIndexInfo().subscribe(new BaseCommonObserver<IndexInfoWrapper>(mContext) {
            @Override
            public void onSuccess(IndexInfoWrapper infoWrapper, String message) {
                if (infoWrapper != null) {
//                    IndexInfoWrapper infoWrapper = infoWrapperResultInfo.data;
//                    UserInfoHelper.saveUserInfo(infoWrapper.getUserInfo());
//                    UserInfoHelper.setVipInfoList(infoWrapper.getUser_vip_list());
                    UserInfoHelper.setContactInfo(infoWrapper.getContact_info());
                    ShareInfoHelper.saveShareInfo(infoWrapper.getShare_info());
                    PayWayInfoHelper.setPayWayInfoList(infoWrapper.getPayway_list());

                    mView.showIndexInfo(infoWrapper);
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {

            }


            @Override
            public void onRequestComplete() {

            }

            @Override
            protected boolean isShow() {
                return false;
            }
        });


//        Subscription subscription = mEngine.getIndexInfo().subscribe(new BaseCommonObserver<ResultInfo<IndexInfoWrapper>>(mContext) {
//
//
//            @Override
//            public void onSuccess(ResultInfo<IndexInfoWrapper> infoWrapperResultInfo, String message) {
//                if (infoWrapperResultInfo != null && infoWrapperResultInfo.code == HttpConfig.STATUS_OK && infoWrapperResultInfo.data != null) {
//                    IndexInfoWrapper infoWrapper = infoWrapperResultInfo.data;
////                    UserInfoHelper.saveUserInfo(infoWrapper.getUserInfo());
////                    UserInfoHelper.setVipInfoList(infoWrapper.getUser_vip_list());
//                    UserInfoHelper.setContactInfo(infoWrapper.getContact_info());
//                    ShareInfoHelper.saveShareInfo(infoWrapper.getShare_info());
//                    PayWayInfoHelper.setPayWayInfoList(infoWrapper.getPayway_list());
//
//                    mView.showIndexInfo(infoWrapper);
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable e, String errorMsg) {
//
//            }
//
//            @Override
//            public void onRequestComplete() {
//
//            }
//
//
//        });
//        mSubscriptions.add(subscription);
    }

}
