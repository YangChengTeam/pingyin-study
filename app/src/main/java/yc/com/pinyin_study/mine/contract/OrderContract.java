package yc.com.pinyin_study.mine.contract;

import java.util.List;

import yc.com.base.IHide;
import yc.com.base.ILoading;
import yc.com.base.INoData;
import yc.com.base.INoNet;
import yc.com.base.IPresenter;
import yc.com.base.IView;
import yc.com.pinyin_study.pay.alipay.OrderInfo;

/**
 * Created by wanglin  on 2019/4/25 16:49.
 */
public interface OrderContract {

    interface View extends IView, IHide, ILoading, INoData, INoNet {
        void showOrderInfos(List<OrderInfo> orderInfos);
    }

    interface Presenter extends IPresenter {
        void getOrderInfos();

        void getOrderInfoList(int page, int page_size, boolean isRefresh);
    }
}
