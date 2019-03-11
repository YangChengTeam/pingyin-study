package yc.com.pinyin_study.base.contract;

import java.util.List;

import yc.com.base.IDialog;
import yc.com.base.IHide;
import yc.com.base.ILoading;
import yc.com.base.IPresenter;
import yc.com.base.IView;
import yc.com.pinyin_study.base.model.domain.GoodInfo;
import yc.com.pinyin_study.pay.alipay.OrderInfo;

/**
 * Created by wanglin  on 2018/10/30 14:58.
 */
public interface BasePayContract {

    interface View extends IView,IDialog {
        void showOrderInfo(OrderInfo orderInfo);

        void showBindSuccess();

        void showVipInfoList(List<GoodInfo> vip_list);
    }

    interface Presenter extends IPresenter {
        void createOrder(int goods_num,String payway_name,String money,String goods_id,String title);
    }
}
