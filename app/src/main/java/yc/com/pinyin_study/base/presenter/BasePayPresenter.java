package yc.com.pinyin_study.base.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BaseEngine;
import yc.com.base.BasePresenter;
import yc.com.pinyin_study.base.contract.BasePayContract;
import yc.com.pinyin_study.base.model.domain.GoodInfo;
import yc.com.pinyin_study.base.model.domain.GoodInfoWrapper;
import yc.com.pinyin_study.base.utils.VipInfoHelper;
import yc.com.pinyin_study.pay.alipay.OrderInfo;
import yc.com.pinyin_study.study.utils.EngineUtils;

/**
 * Created by wanglin  on 2018/10/30 14:47.
 */
public class BasePayPresenter extends BasePresenter<BaseEngine, BasePayContract.View> implements BasePayContract.Presenter {
    public BasePayPresenter(Context context, BasePayContract.View view) {
        super(context, view);

    }

    @Override
    public void loadData(boolean isForceUI, boolean isLoadingUI) {
        if (!isForceUI) return;
        getVipInfos();
    }

    @Override
    public void createOrder(int goods_num, final String payway_name, final String money, final String goods_id, final String title) {
        mView.showLoadingDialog("正在创建订单，请稍候...");
        Subscription subscription = EngineUtils.createOrder(mContext, goods_num, payway_name, money, goods_id).subscribe(new Subscriber<ResultInfo<OrderInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.dismissDialog();
            }

            @Override
            public void onNext(ResultInfo<OrderInfo> orderInfoResultInfo) {
                mView.dismissDialog();
                if (orderInfoResultInfo != null && orderInfoResultInfo.code == HttpConfig.STATUS_OK && orderInfoResultInfo.data != null) {
                    OrderInfo orderInfo = orderInfoResultInfo.data;
                    orderInfo.setMoney(Float.parseFloat(money));
                    orderInfo.setName(title);
                    orderInfo.setGoodId(goods_id);
                    mView.showOrderInfo(orderInfo);
                }

            }
        });
        mSubscriptions.add(subscription);
    }

    public void isBindPhone() {
        Subscription subscription = EngineUtils.isBindPhone(mContext).subscribe(new Subscriber<ResultInfo<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<String> stringResultInfo) {
                if (stringResultInfo != null && stringResultInfo.code == HttpConfig.STATUS_OK) {
                    //绑定手机号
                    mView.showBindSuccess();
                }
            }
        });
        mSubscriptions.add(subscription);
    }


    private void getVipInfos() {

        Subscription subscription = EngineUtils.getVipInfoList(mContext).subscribe(new Subscriber<ResultInfo<GoodInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<GoodInfoWrapper> vipInfoWrapperResultInfo) {
                if (vipInfoWrapperResultInfo != null && vipInfoWrapperResultInfo.code == HttpConfig.STATUS_OK && vipInfoWrapperResultInfo.data != null) {
                    GoodInfoWrapper infoWrapper = vipInfoWrapperResultInfo.data;
                    List<GoodInfo> vip_list = infoWrapper.getVip_list();
                    mView.showVipInfoList(vip_list);
                    VipInfoHelper.setVipInfoList(vip_list);
                }

            }
        });

        mSubscriptions.add(subscription);


    }


}
