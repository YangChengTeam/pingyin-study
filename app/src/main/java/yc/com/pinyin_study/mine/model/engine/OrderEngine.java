package yc.com.pinyin_study.mine.model.engine;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import yc.com.pinyin_study.base.model.engine.BaseEngine;
import yc.com.pinyin_study.index.utils.UserInfoHelper;
import yc.com.pinyin_study.mine.model.bean.OrderInfoWrapper;
import yc.com.pinyin_study.pay.alipay.OrderInfo;
import yc.com.rthttplibrary.bean.ResultInfo;

/**
 * Created by wanglin  on 2019/4/25 16:27.
 */
public class OrderEngine extends BaseEngine {
    public OrderEngine(Context context) {
        super(context);
    }

    public Observable<List<OrderInfo>> getOrderInfos() {

        return Observable.just("").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).map(s -> {
            List<OrderInfo> orderInfos = new ArrayList<>();

            OrderInfo orderInfo1 = new OrderInfo();
            orderInfo1.setName("充值");
            orderInfo1.setMoney(265.00f);
            orderInfo1.setOrder_sn("201563214566123");

            orderInfo1.setTime("2019-03-23 12:23:45");
            orderInfo1.setState(0);

            OrderInfo orderInfo2 = new OrderInfo();
            orderInfo2.setName("充值");
            orderInfo2.setMoney(65.00f);
            orderInfo2.setOrder_sn("201563214566345");

            orderInfo2.setTime("2019-03-22 11:45:45");
            orderInfo2.setState(1);

            OrderInfo orderInfo3 = new OrderInfo();
            orderInfo3.setName("充值");
            orderInfo3.setMoney(102.00f);
            orderInfo3.setOrder_sn("201563214566458");

            orderInfo3.setTime("2019-03-16 09:15:45");
            orderInfo3.setState(0);

            OrderInfo orderInfo4 = new OrderInfo();
            orderInfo4.setName("充值");
            orderInfo4.setMoney(98.00f);
            orderInfo4.setOrder_sn("201563214566987");

            orderInfo4.setTime("2019-03-14 09:45:26");
            orderInfo4.setState(1);

            OrderInfo orderInfo5 = new OrderInfo();
            orderInfo5.setName("充值");
            orderInfo5.setMoney(98.00f);
            orderInfo5.setOrder_sn("201563214566987");
            orderInfo5.setTime("2019-03-14 09:45:26");
            orderInfo5.setState(1);

            OrderInfo orderInfo6 = new OrderInfo();
            orderInfo6.setName("充值");
            orderInfo6.setMoney(98.00f);
            orderInfo6.setOrder_sn("201563214566987");
            orderInfo6.setTime("2019-03-14 09:45:26");
            orderInfo6.setState(1);

            OrderInfo orderInfo7 = new OrderInfo();
            orderInfo7.setName("充值");
            orderInfo7.setMoney(98.00f);
            orderInfo7.setOrder_sn("201563214566987");
            orderInfo7.setTime("2019-03-14 09:45:26");
            orderInfo7.setState(1);

            orderInfos.add(orderInfo1);
            orderInfos.add(orderInfo2);
            orderInfos.add(orderInfo3);
            orderInfos.add(orderInfo4);
            orderInfos.add(orderInfo5);
            orderInfos.add(orderInfo6);
            orderInfos.add(orderInfo7);

            return orderInfos;
        });


    }

    public Observable<ResultInfo<OrderInfoWrapper>> getOrderInfoList(int page, int page_size) {
//        Map<String, String> params = new HashMap<>();
//        params.put("user_id", UserInfoHelper.getUid());
//        params.put("page", page + "");
//        params.put("page_size", page_size + "");
//        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.ORDER_LIST, new TypeReference<ResultInfo<OrderInfoWrapper>>() {
//        }.getType(), params, true, true, true);

        return request.getOrderInfoList(UserInfoHelper.getUid(), page, page_size).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


}
