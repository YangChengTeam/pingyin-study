package yc.com.pinyin_study.pay.alipay;

/**
 * Created by zhangkai on 2017/3/17.
 */

public interface IPayCallback {
    void onSuccess(OrderInfo orderInfo);

    void onFailure(OrderInfo orderInfo);
}
