package yc.com.pinyin_study.mine.presenter;

import android.content.Context;

import java.util.List;

import io.reactivex.observers.DisposableObserver;
import yc.com.base.BasePresenter;
import yc.com.pinyin_study.base.observer.BaseCommonObserver;
import yc.com.pinyin_study.mine.contract.OrderContract;
import yc.com.pinyin_study.mine.model.bean.OrderInfoWrapper;
import yc.com.pinyin_study.mine.model.engine.OrderEngine;
import yc.com.pinyin_study.pay.alipay.OrderInfo;

/**
 * Created by wanglin  on 2019/4/25 16:42.
 */
public class OrderPresenter extends BasePresenter<OrderEngine, OrderContract.View> implements OrderContract.Presenter {
    public OrderPresenter(Context context, OrderContract.View view) {
        super(context, view);
        mEngine = new OrderEngine(context);
    }

    @Override
    public void loadData(boolean isForceUI, boolean isLoadingUI) {
        if (!isForceUI) return;
        getOrderInfos();
//        getOrderInfoList();
    }

    @Override
    public void getOrderInfos() {
        mEngine.getOrderInfos().subscribe(new DisposableObserver<List<OrderInfo>>() {
            @Override
            public void onNext(List<OrderInfo> orderInfos) {
                mView.showOrderInfos(orderInfos);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


    }

    @Override
    public void getOrderInfoList(final int page, int page_size, boolean isRefresh) {
        if (page == 1 && !isRefresh)
            mView.showLoading();

        mEngine.getOrderInfoList(page, page_size).subscribe(new BaseCommonObserver<OrderInfoWrapper>(mContext) {
            @Override
            public void onSuccess(OrderInfoWrapper data, String message) {

                if (data != null) {
                    mView.hide();
                    mView.showOrderInfos(data.getList());
                } else {
                    if (page == 1)
                        mView.showNoData();
                }

            }

            @Override
            public void onFailure(int code, String errorMsg) {
                if (page == 1)
                    mView.showNoNet();
            }

            @Override
            public void onRequestComplete() {

            }
        });
    }


}
