package yc.com.pinyin_study.index.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;


import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import yc.com.blankj.utilcode.util.LogUtils;
import yc.com.blankj.utilcode.util.SPUtils;
import yc.com.pinyin_study.base.constant.Config;
import yc.com.pinyin_study.base.constant.SpConstant;
import yc.com.pinyin_study.base.httpinterface.HttpRequestInterface;
import yc.com.pinyin_study.base.model.domain.VipInfo;
import yc.com.pinyin_study.base.observer.BaseCommonObserver;
import yc.com.pinyin_study.index.model.domain.ContactInfo;
import yc.com.pinyin_study.index.model.domain.UserInfo;
import yc.com.pinyin_study.index.model.domain.UserInfoWrapper;
import yc.com.pinyin_study.mine.activity.LoginActivity;
import yc.com.pinyin_study.mine.model.engine.LoginEngine;
import yc.com.pinyin_study.study.model.domain.StudyPages;
import yc.com.pinyin_study.study_1vs1.model.bean.IndexDialogInfoWrapper;
import yc.com.rthttplibrary.request.RetrofitHttpRequest;
import yc.com.rthttplibrary.util.LogUtil;

/**
 * Created by wanglin  on 2018/10/29 09:38.
 */
public class UserInfoHelper {

    private static UserInfo mUserInfo;

    private static List<VipInfo> mVipInfoList;
    private static ContactInfo mContactInfo;

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

    public static void setContactInfo(ContactInfo contactInfo) {
        UserInfoHelper.mContactInfo = contactInfo;
        try {
            String str = JSON.toJSONString(contactInfo);
            SPUtils.getInstance().put(SpConstant.CONTACT_INFO, str);
        } catch (Exception e) {
            LogUtil.msg("to json error->" + e.getMessage());
        }
    }

    public static ContactInfo getContactInfo() {
        if (mContactInfo != null) {
            return mContactInfo;
        }
        ContactInfo contactInfo = null;
        try {
            String str = SPUtils.getInstance().getString(SpConstant.CONTACT_INFO);
            contactInfo = JSON.parseObject(str, ContactInfo.class);

        } catch (Exception e) {
            LogUtils.e("json parse error->" + e.getMessage());
        }
        mContactInfo = contactInfo;

        return mContactInfo;
    }

    public static String getUid() {
        String userId = "";

        if (getUserInfo() != null) {
            userId = getUserInfo().getUser_id();
        }

        return userId;
    }


    public static boolean isLogin(Context context) {
        boolean isLogin = false;
        if (!TextUtils.isEmpty(getUid())) {
            isLogin = true;

        }

        if (!isLogin) {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        }
        return isLogin;
    }

    public static void logout() {
        UserInfo userInfo = new UserInfo();
        UserInfoHelper.saveUserInfo(userInfo);
        List<VipInfo> vipInfos = getVipInfoList();
        if (vipInfos != null && vipInfos.size() > 0) {
            vipInfos = null;
        }
        setVipInfoList(vipInfos);
    }


    public static void saveVip(String vip) {
        boolean flag = false;
        String vips = SPUtils.getInstance().getString(SpConstant.USER_VIP, "");
        String[] vipArr = vips.split(",");
        for (String tmp : vipArr) {
            if (tmp.equals(vip)) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            SPUtils.getInstance().put(SpConstant.USER_VIP, vips + "," + vip);
        }
    }

    public static boolean isVip(String vip) {
        boolean flag = false;
        String vips = SPUtils.getInstance().getString(SpConstant.USER_VIP, "");
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
        return isPhonogramVip() || isPhonicsVip() || isPhonogramOrPhonicsVip() || isSuperVip() || !Config.SHOW_ADV;
    }

    private static LoginEngine loginEngine;

    public static void login(Context context) {
        if (loginEngine == null) {
            loginEngine = new LoginEngine(context);
        }

        UserInfo userInfo = getUserInfo();
        if (userInfo != null) {
            String mobile = userInfo.getMobile();
            String pwd = userInfo.getPwd();
            if (!TextUtils.isEmpty(mobile) && !TextUtils.isEmpty(pwd)) {

                loginEngine.login(mobile,pwd).subscribe(new BaseCommonObserver<UserInfoWrapper>(context) {
                    @Override
                    public void onSuccess(UserInfoWrapper data, String message) {

                            if (data != null) {
                                UserInfo info = data.getUserInfo();
                                List<VipInfo> vipList = data.getVipList();
                                if (info != null) {
                                    info.setPwd(pwd);
                                }

                                saveUserInfo(info);
                                setVipInfoList(vipList);

                            }

                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {

                    }

                    @Override
                    public void onRequestComplete() {

                    }
                });


//                loginEngine.login(mobile, pwd).subscribe(new Subscriber<ResultInfo<UserInfoWrapper>>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(ResultInfo<UserInfoWrapper> userInfoWrapperResultInfo) {
//                        if (userInfoWrapperResultInfo != null) {
//                            if (userInfoWrapperResultInfo.code == HttpConfig.STATUS_OK && userInfoWrapperResultInfo.data != null) {
//                                UserInfo info = userInfoWrapperResultInfo.data.getUserInfo();
//                                List<VipInfo> vipList = userInfoWrapperResultInfo.data.getVipList();
//                                if (info != null) {
//                                    info.setPwd(pwd);
//                                }
//
//                                saveUserInfo(info);
//                                setVipInfoList(vipList);
//
//                            }
//                        }
//                    }
//                });
            }

        }

    }

    public static void getIndexMenuInfo(Context context) {
        RetrofitHttpRequest.get(context).create(HttpRequestInterface.class)
                .getIndexMenuInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseCommonObserver<IndexDialogInfoWrapper>(context) {
                    @Override
                    public void onSuccess(IndexDialogInfoWrapper data, String message) {

                        SPUtils.getInstance().put(SpConstant.INDEX_MENU_STATICS, JSON.toJSONString(data.info));
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {

                    }


                    @Override
                    public void onRequestComplete() {

                    }
                });
//        EngineUtils.getIndexMenuInfo(context).subscribe(new Subscriber<ResultInfo<IndexDialogInfoWrapper>>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//
//            }
//
//            @Override
//            public void onNext(ResultInfo<IndexDialogInfoWrapper> indexDialogInfoWrapperResultInfo) {
//
//                if (indexDialogInfoWrapperResultInfo != null && indexDialogInfoWrapperResultInfo.code == HttpConfig.STATUS_OK) {
//                    IndexDialogInfoWrapper infoWrapper = indexDialogInfoWrapperResultInfo.data;
//                    SPUtils.getInstance().put(SpConstant.INDEX_MENU_STATICS, JSON.toJSONString(infoWrapper.info));
//                }
//            }
//        });

    }

    public static void getStudyPages(Context context) {
        RetrofitHttpRequest.get(context).create(HttpRequestInterface.class)
                .getStudyPages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseCommonObserver<StudyPages>(context) {
                    @Override
                    public void onSuccess(StudyPages data, String message) {
                        if (data != null) {
                            SPUtils.getInstance().put(SpConstant.STUDY_PAGES, data.count);
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
