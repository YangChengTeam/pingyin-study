package yc.com.pinyin_study.index.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BasePresenter;
import yc.com.pinyin_study.base.model.domain.GoodInfo;
import yc.com.pinyin_study.base.model.domain.GoodInfoWrapper;
import yc.com.pinyin_study.base.utils.VipInfoHelper;
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
        Subscription subscription = mEngine.getIndexInfo().subscribe(new Subscriber<ResultInfo<IndexInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<IndexInfoWrapper> infoWrapperResultInfo) {
                if (infoWrapperResultInfo != null && infoWrapperResultInfo.code == HttpConfig.STATUS_OK && infoWrapperResultInfo.data != null) {
                    IndexInfoWrapper infoWrapper = infoWrapperResultInfo.data;
                    UserInfoHelper.saveUserInfo(infoWrapper.getUserInfo());
                    UserInfoHelper.setVipInfoList(infoWrapper.getUser_vip_list());
                    ShareInfoHelper.saveShareInfo(infoWrapper.getShare_info());
                    PayWayInfoHelper.setPayWayInfoList(infoWrapper.getPayway_list());

                    mView.showIndexInfo(infoWrapper);
                }
            }
        });
        mSubscriptions.add(subscription);
    }

}
