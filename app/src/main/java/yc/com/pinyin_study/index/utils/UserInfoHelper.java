package yc.com.pinyin_study.index.utils;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.LogUtil;
import com.kk.utils.PreferenceUtil;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import yc.com.blankj.utilcode.util.LogUtils;
import yc.com.blankj.utilcode.util.SPUtils;
import yc.com.pinyin_study.base.constant.Config;
import yc.com.pinyin_study.base.constant.SpConstant;
import yc.com.pinyin_study.base.model.domain.VipInfo;
import yc.com.pinyin_study.index.model.domain.UserInfo;
import yc.com.pinyin_study.study.utils.EngineUtils;
import yc.com.pinyin_study.study_1vs1.model.bean.IndexDialogInfoWrapper;

/**
 * Created by wanglin  on 2018/10/29 09:38.
 */
public class UserInfoHelper {

    private static UserInfo mUserInfo;

    private static List<VipInfo> mVipInfoList;

    public static UserInfo getUserInfo() {
        if (mUserInfo != null) {
            return mUserInfo;
        }
        UserInfo userInfo = null;
        try {
            String str = SPUtils.getInstance().getString(SpConstant.USER_INFO);
            userInfo = JSON.parseObject(str, UserInfo.class);

        } catch (Exception e) {
            LogUtils.e("json parse error->" + e.getMessage());
        }
        mUserInfo = userInfo;

        return mUserInfo;
    }

    public static void saveUserInfo(UserInfo userInfo) {
        UserInfoHelper.mUserInfo = userInfo;
        try {
            String str = JSON.toJSONString(userInfo);
            SPUtils.getInstance().put(SpConstant.USER_INFO, str);

        } catch (Exception e) {
            LogUtil.msg("to json error->" + e.getMessage());
        }

    }

    public static List<VipInfo> getVipInfoList() {
        if (mVipInfoList != null) {
            return mVipInfoList;
        }
        try {
            String str = SPUtils.getInstance().getString(SpConstant.VIP_INFO_LIST);

            mVipInfoList = JSON.parseArray(str, VipInfo.class);


        } catch (Exception e) {
            LogUtil.msg("to json error->" + e.getMessage());
        }


        return mVipInfoList;
    }

    public static void setVipInfoList(List<VipInfo> vipInfoList) {
        UserInfoHelper.mVipInfoList = vipInfoList;
        try {
            String str = JSON.toJSONString(vipInfoList);
            SPUtils.getInstance().put(SpConstant.VIP_INFO_LIST, str);
        } catch (Exception e) {
            LogUtil.msg("to json error->" + e.getMessage());
        }

    }

    public static String getUid() {
        String userId = "";

        if (mUserInfo != null) {
            userId = mUserInfo.getUid();
        }

        return userId;
    }


    public static void saveVip(String vip) {
        boolean flag = false;
        String vips = SPUtils.getInstance().getString("vip", "");
        String[] vipArr = vips.split(",");
        for (String tmp : vipArr) {
            if (tmp.equals(vip)) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            SPUtils.getInstance().put("vip", vips + "," + vip);
        }
    }

    public static boolean isVip(String vip) {
        boolean flag = false;
        String vips = SPUtils.getInstance().getString("vip", "");
        String[] vipArr = vips.split(",");

        for (String tmp : vipArr) {
            if (tmp.equals(vip)) {
                flag = true;
                break;
            }
        }

        if (!flag) {
            List<VipInfo> vipInfoList = getVipInfoList();
            if (vipInfoList != null) {
                for (VipInfo vipInfo : vipInfoList) {
                    if (vip.equals(vipInfo.getType() + "")) {
                        flag = true;
                        break;
                    }
                }
            }

        }
        return flag;
    }

    //音标点读
    public static boolean isPhonogramVip() {
        return isVip(Config.PHONOGRAM_VIP + "") || isPhonogramOrPhonicsVip() || isSuperVip();
    }

    //微课
    public static boolean isPhonicsVip() {
        return isVip(Config.PHONICS_VIP + "") || isPhonogramOrPhonicsVip() || isSuperVip();
    }

    //音标点读+微课
    public static boolean isPhonogramOrPhonicsVip() {
        return isVip(Config.PHONOGRAMORPHONICS_VIP + "") || isSuperVip() || (isVip(Config.PHONOGRAM_VIP + "") && isVip(Config.PHONICS_VIP + ""));
    }

    //音标点读+微课  超级vip
    public static boolean isSuperVip() {
        return isVip(Config.SUPER_VIP + "");
    }

    //是否开会员去广告
    public static boolean isCloseAdv() {
        return isPhonogramVip() || isPhonicsVip() || isPhonogramOrPhonicsVip() || isSuperVip();
    }


    public static void getIndexMenuInfo(Context context) {
        EngineUtils.getIndexMenuInfo(context).subscribe(new Subscriber<ResultInfo<IndexDialogInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {


            }

            @Override
            public void onNext(ResultInfo<IndexDialogInfoWrapper> indexDialogInfoWrapperResultInfo) {

                if (indexDialogInfoWrapperResultInfo != null && indexDialogInfoWrapperResultInfo.code == HttpConfig.STATUS_OK) {
                    IndexDialogInfoWrapper infoWrapper = indexDialogInfoWrapperResultInfo.data;
                    SPUtils.getInstance().put(SpConstant.INDEX_MENU_STATICS, JSON.toJSONString(infoWrapper.info));
                }
            }
        });

    }


}
