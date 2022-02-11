package yc.com.pinyin_study.base.presenter;

import android.content.Context;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;



import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BasePresenter;
import yc.com.blankj.utilcode.util.PhoneUtils;
import yc.com.blankj.utilcode.util.RegexUtils;
import yc.com.blankj.utilcode.util.SPUtils;
import yc.com.pinyin_study.base.constant.SpConstant;
import yc.com.pinyin_study.base.contract.BasePhoneContract;
import yc.com.pinyin_study.base.model.engine.BasePhoneEngine;
import yc.com.pinyin_study.base.observer.BaseCommonObserver;
import yc.com.rthttplibrary.util.ToastUtil;

/**
 * Created by wanglin  on 2018/11/1 15:00.
 */
public class BasePhonePresenter extends BasePresenter<BasePhoneEngine, BasePhoneContract.View> implements BasePhoneContract.Presenter {
    public BasePhonePresenter(Context context, BasePhoneContract.View view) {
        super(context, view);
        mEngine = new BasePhoneEngine(context);
    }

    @Override
    public void loadData(boolean isForceUI, boolean isLoadingUI) {

    }

    public void uploadPhone(final String phone) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.toast(mContext, "手机号码不能为空");
            return;
        }

        if (!RegexUtils.isMobileExact(phone)) {
            ToastUtil.toast(mContext, "请输入正确的手机号码");
            return;
        }
//        mView.showLoadingDialog("正在上传手机号,请稍候...");

        mEngine.uploadPhone(phone).subscribe(new BaseCommonObserver<String>(mContext, "上传手机号中,请稍候...") {
            @Override
            public void onSuccess(String data, String message) {

                mView.showUploadSuccess();
                SPUtils.getInstance().put(SpConstant.PHONE, phone);

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
