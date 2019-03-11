package yc.com.pinyin_study.pay;

import com.alibaba.fastjson.JSON;

import java.util.List;

import yc.com.blankj.utilcode.util.LogUtils;
import yc.com.blankj.utilcode.util.SPUtils;
import yc.com.pinyin_study.base.constant.SpConstant;

/**
 * Created by wanglin  on 2017/11/8 14:12.
 */

public class PayWayInfoHelper {

    private static List<PayWayInfo> mPayWayInfoList;

    public static List<PayWayInfo> getPayWayInfoList() {
        if (mPayWayInfoList != null) {
            return mPayWayInfoList;
        }
        String result = SPUtils.getInstance().getString(SpConstant.PAYWAY_INFO);

        try {
            mPayWayInfoList = JSON.parseArray(result, PayWayInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("error:->>" + e.getMessage());
        }
        return mPayWayInfoList;
    }

    public static void setPayWayInfoList(List<PayWayInfo> payWayInfoList) {

        try {
            String json = JSON.toJSONString(payWayInfoList);

            SPUtils.getInstance().put(SpConstant.PAYWAY_INFO, json);

        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("error:->>" + e.getMessage());
        }
        mPayWayInfoList = payWayInfoList;

    }


}
