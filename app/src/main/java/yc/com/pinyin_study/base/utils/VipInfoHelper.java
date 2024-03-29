package yc.com.pinyin_study.base.utils;

import com.alibaba.fastjson.JSON;


import java.util.List;

import yc.com.blankj.utilcode.util.SPUtils;
import yc.com.pinyin_study.base.constant.SpConstant;
import yc.com.pinyin_study.base.model.domain.GoodInfo;
import yc.com.rthttplibrary.util.LogUtil;

/**
 * Created by wanglin  on 2018/10/30 11:19.
 */
public class VipInfoHelper {

    private static List<GoodInfo> mVipInfoList;

    public static List<GoodInfo> getVipInfoList() {
        if (mVipInfoList != null) {
            return mVipInfoList;
        }
        List<GoodInfo> vipInfos = null;
        try {
            String str = SPUtils.getInstance().getString(SpConstant.VIP_INFO);
            vipInfos = JSON.parseArray(str, GoodInfo.class);
        } catch (Exception e) {
            LogUtil.msg("json parse error->" + e.getMessage());
        }
        mVipInfoList = vipInfos;

        return mVipInfoList;
    }

    public static void setVipInfoList(List<GoodInfo> vipInfoList) {
        mVipInfoList = vipInfoList;
        try {
            String str = JSON.toJSONString(vipInfoList);
            SPUtils.getInstance().put(SpConstant.VIP_INFO, str);
        } catch (Exception e) {
            LogUtil.msg("to json error->" + e.getMessage());
        }

    }


}
