package yc.com.pinyin_study.base.presenter;

import android.content.Context;

import java.util.List;

import yc.com.base.BasePresenter;
import yc.com.pinyin_study.base.contract.BasePayContract;
import yc.com.pinyin_study.base.model.domain.GoodInfo;
import yc.com.pinyin_study.base.model.domain.GoodInfoWrapper;
import yc.com.pinyin_study.base.observer.BaseCommonObserver;
import yc.com.pinyin_study.base.utils.VipInfoHelper;
import yc.com.pinyin_study.mine.model.engine.VipEngine;
import yc.com.pinyin_study.pay.alipay.OrderInfo;

/**
 * Created by wanglin  on 2018/10/30 14:47.
 */
public class BasePayPresenter extends BasePresenter<VipEngine, BasePayContract.View> implements BasePayContract.Presenter {
    public BasePayPresenter(Context context, BasePayContract.View view) {
        super(context, view);
        mEngine = new VipEngine(context);
    }

    @Override
    public void loadData(boolean isForceUI, boolean isLoadingUI) {
        if (!isForceUI) return;
        getVipInfos();
    }

    @Override
    public void createOrder(int goods_num, final String payway_name, final String money, final String goods_id, final String title) {
//        mView.showLoadingDialog("正在创建订单，请稍候...");
        mEngine.createOrder(goods_num, payway_name, money, goods_id).subscribe(new BaseCommonObserver<OrderInfo>(mContext, "创建订单中，请稍候...") {
            @Override
            public void onSuccess(OrderInfo orderInfo, String message) {
                if (orderInfo != null) {
                    orderInfo.setMoney(Float.parseFloat(money));
                    orderInfo.setName(title);
                    orderInfo.setGoodId(goods_id);
                    mView.showOrderInfo(orderInfo);
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
                return true;
            }
        });


    }

    public void isBindPhone() {
        mEngine.isBindPhone().subscribe(new BaseCommonObserver<String>(mContext){
            @Override
            public void onSuccess(String data, String message) {
                    //绑定手机号
                    mView.showBindSuccess();
            }

            @Override
            public void onFailure(int code, String errorMsg) {

            }


            @Override
            public void onRequestComplete() {

            }
        });

//
    }


    private void getVipInfos() {

        mEngine.getVipInfoList().subscribe(new BaseCommonObserver<GoodInfoWrapper>(mContext) {
            @Override
            public void onSuccess(GoodInfoWrapper goodInfoWrapper, String message) {
                if (goodInfoWrapper != null) {
                    List<GoodInfo> vip_list = goodInfoWrapper.getVip_list();
                    mView.showVipInfoList(vip_list);
                    VipInfoHelper.setVipInfoList(vip_list);
                }
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
