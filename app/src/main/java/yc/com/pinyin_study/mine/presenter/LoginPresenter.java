package yc.com.pinyin_study.mine.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.hwangjr.rxbus.RxBus;

import java.util.List;
import java.util.concurrent.TimeUnit;

import yc.com.base.BasePresenter;
import yc.com.base.UIUtils;
import yc.com.blankj.utilcode.util.SPUtils;
import yc.com.pinyin_study.base.constant.BusAction;
import yc.com.pinyin_study.base.constant.SpConstant;
import yc.com.pinyin_study.base.model.domain.VipInfo;
import yc.com.pinyin_study.base.observer.BaseCommonObserver;
import yc.com.pinyin_study.index.model.domain.UserInfo;
import yc.com.pinyin_study.index.model.domain.UserInfoWrapper;
import yc.com.pinyin_study.index.utils.UserInfoHelper;
import yc.com.pinyin_study.mine.activity.RegisterActivity;
import yc.com.pinyin_study.mine.activity.SetPwdActivity;
import yc.com.pinyin_study.mine.contract.LoginContract;
import yc.com.pinyin_study.mine.model.engine.LoginEngine;
import yc.com.rthttplibrary.util.ToastUtil;

/**
 * Created by suns  on 2020/4/16 15:18.
 */
public class LoginPresenter extends BasePresenter<LoginEngine, LoginContract.View> implements LoginContract.Presenter {
    public LoginPresenter(Context context, LoginContract.View view) {
        super(context, view);
        mEngine = new LoginEngine(context);
    }

    @Override
    public void loadData(boolean isForceUI, boolean isLoadingUI) {

    }

    public void login(String phone, String password) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.toast(mContext, "手机号不能为空");
            return;
        }
        if (!UIUtils.isPhoneNumber(phone)) {
            ToastUtil.toast(mContext, "手机号格式不正确");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            ToastUtil.toast(mContext, "密码不能为空");
            return;
        }
        mView.showLoadingDialog("登录中...");

        mEngine.login(phone, password).subscribe(new BaseCommonObserver<UserInfoWrapper>(mContext) {
            @Override
            public void onSuccess(UserInfoWrapper userInfoWrapper, String message) {
                mView.dismissDialog();
                if (userInfoWrapper != null) {
                    UserInfo userInfo = userInfoWrapper.getUserInfo();
                    List<VipInfo> vipList = userInfoWrapper.getVipList();
                    if (null != userInfo) {
                        userInfo.setPwd(password);
                        SPUtils.getInstance().put(SpConstant.USER_PHONE, userInfo.getMobile());
                    }
                    UserInfoHelper.saveUserInfo(userInfo);
                    RxBus.get().post(BusAction.LOGIN, userInfo);
                    UserInfoHelper.setVipInfoList(vipList);
                    if (mContext instanceof Activity) {
                        ((Activity) mContext).finish();
                    }
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                mView.dismissDialog();
                if (code == 2 || errorMsg.contains("账号不存在")) {

                    Intent intent = new Intent(mContext, RegisterActivity.class);
                    mContext.startActivity(intent);
                    if (mContext instanceof Activity) {
                        ((Activity) mContext).finish();
                    }
                } else {
                    ToastUtil.toast(mContext, errorMsg);
                }
            }


            @Override
            public void onRequestComplete() {

            }
        });



    }


    public void sendCode(String phone) {

        mEngine.sendCode(phone).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new BaseCommonObserver<String>(mContext) {
            @Override
            public void onSuccess(String data, String message) {

                mView.showDisplayCode();
                ToastUtil.toast(mContext, message);
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                ToastUtil.toast(mContext, errorMsg);
            }

            @Override
            public void onRequestComplete() {

            }
        });


    }


    public void codeLogin(String phone, String code, boolean isLogin) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.toast(mContext, "手机号不能为空");
            return;
        }
        if (!UIUtils.isPhoneNumber(phone)) {
            ToastUtil.toast(mContext, "手机号格式不正确");
            return;
        }

        if (TextUtils.isEmpty(code)) {
            ToastUtil.toast(mContext, "验证码不能为空");
            return;
        }
        mView.showLoadingDialog("登录中...");

        mEngine.codeLogin(phone, code).subscribe(new BaseCommonObserver<UserInfoWrapper>(mContext) {
            @Override
            public void onSuccess(UserInfoWrapper userInfoWrapper, String message) {
                mView.dismissDialog();
                if (userInfoWrapper != null) {
                    UserInfo userInfo = userInfoWrapper.getUserInfo();
                    List<VipInfo> vipList = userInfoWrapper.getVipList();
                    if (null != userInfo) {
                        userInfo.setPwd("");
                        SPUtils.getInstance().put(SpConstant.USER_PHONE, userInfo.getMobile());
                    }
                    UserInfoHelper.saveUserInfo(userInfo);
                    RxBus.get().post(BusAction.LOGIN, userInfo);

                    if (!isLogin) {

                        Intent intent = new Intent(mContext, SetPwdActivity.class);
                        mContext.startActivity(intent);
                    } else {
                        UserInfoHelper.setVipInfoList(vipList);
                    }
                    if (mContext instanceof Activity) {
                        ((Activity) mContext).finish();
                    }

                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                mView.dismissDialog();
                ToastUtil.toast(mContext, errorMsg);
            }

            @Override
            public void onRequestComplete() {

            }
        });



    }

    public void setPwd(String pwd) {
        if (TextUtils.isEmpty(pwd)) {
            ToastUtil.toast(mContext, "密码不能为空");
            return;
        }
        mView.showLoadingDialog("设置密码中...");

        mEngine.sendCode(pwd).subscribe(new BaseCommonObserver<String>(mContext) {
            @Override
            public void onSuccess(String data, String message) {
                mView.dismissDialog();
                UserInfo userInfo = UserInfoHelper.getUserInfo();
                if (null != userInfo) {
                    userInfo.setPwd(pwd);
                }
                UserInfoHelper.saveUserInfo(userInfo);

                ToastUtil.toast(mContext, message);
                RxBus.get().post(BusAction.SET_PWD, "success");
                if (mContext instanceof Activity) {
                    ((Activity) mContext).finish();
                }


            }

            @Override
            public void onFailure(int code, String errorMsg) {
                mView.dismissDialog();
            }

            @Override
            public void onRequestComplete() {

            }
        });

    }

    public void modifyPwd(String pwd, String newPwd) {
        mView.showLoadingDialog("修改密码中...");
        mEngine.modifyPwd(pwd, newPwd).subscribe(new BaseCommonObserver<UserInfo>(mContext) {
            @Override
            public void onSuccess(UserInfo data, String message) {
                mView.dismissDialog();
                if (data != null) {
                    UserInfo userInfo = UserInfoHelper.getUserInfo();
                    if (null != userInfo) {
                        userInfo.setPwd(newPwd);
                    }
                    UserInfoHelper.saveUserInfo(userInfo);
                    if (mContext instanceof Activity) {
                        ((Activity) mContext).finish();
                    }

                }

            }

            @Override
            public void onFailure(int code, String errorMsg) {
                mView.dismissDialog();
                ToastUtil.toast(mContext, errorMsg);
            }

            @Override
            public void onRequestComplete() {

            }
        });


    }

}
