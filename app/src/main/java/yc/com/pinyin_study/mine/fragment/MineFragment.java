package yc.com.pinyin_study.mine.fragment;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.TextView;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;


import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;
import yc.com.base.BaseFragment;
import yc.com.pinyin_study.R;
import yc.com.pinyin_study.base.constant.BusAction;
import yc.com.pinyin_study.base.fragment.BasePayFragment;
import yc.com.pinyin_study.base.widget.MainToolBar;
import yc.com.pinyin_study.index.fragment.VipEquitiesFragment;
import yc.com.pinyin_study.index.model.domain.ContactInfo;
import yc.com.pinyin_study.index.model.domain.UserInfo;
import yc.com.pinyin_study.index.utils.UserInfoHelper;
import yc.com.pinyin_study.mine.activity.LoginActivity;
import yc.com.pinyin_study.mine.activity.OrderActivity;
import yc.com.pinyin_study.mine.activity.PersonInfoActivity;
import yc.com.pinyin_study.mine.activity.PhoneticActivity;
import yc.com.pinyin_study.mine.activity.PrivacyStatementActivity;
import yc.com.pinyin_study.mine.activity.SettingActivity;
import yc.com.pinyin_study.mine.widget.BaseSettingView;
import yc.com.rthttplibrary.util.ToastUtil;

/**
 * Created by wanglin  on 2019/4/23 11:13.
 */
public class MineFragment extends BaseFragment {
    @BindView(R.id.main_toolbar)
    MainToolBar mainToolbar;
    @BindView(R.id.tv_user_id)
    TextView tvUserId;
    @BindView(R.id.baseSettingView_person)
    BaseSettingView baseSettingViewPerson;
    @BindView(R.id.baseSettingView_dredge)
    BaseSettingView baseSettingViewDredge;
    @BindView(R.id.baseSettingView_equities)
    BaseSettingView baseSettingViewEquities;
    @BindView(R.id.baseSettingView_order)
    BaseSettingView baseSettingViewOrder;
    @BindView(R.id.baseSettingView_share)
    BaseSettingView baseSettingViewShare;
    @BindView(R.id.baseSettingView_service)
    BaseSettingView baseSettingViewService;
    @BindView(R.id.baseSettingView_setting)
    BaseSettingView baseSettingViewSetting;
    @BindView(R.id.baseSettingView_privacy)
    BaseSettingView baseSettingViewPrivacy;
    @BindView(R.id.baseSettingView_service_time)
    BaseSettingView baseSettingViewServiceTime;
    private boolean isLogin = false;//是否登录

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void init() {
        mainToolbar.hideLeftIcon();
        mainToolbar.setTvRightTitleAndIcon(getString(R.string.phonetic_introduce), R.mipmap.index_phogetic_introduce);
        mainToolbar.init(((BaseActivity) getActivity()), PhoneticActivity.class);
        mainToolbar.setRightContainerVisible(false);

        String uid = UserInfoHelper.getUid();
        if (!TextUtils.isEmpty(uid)) {
            isLogin = true;
        }

        tvUserId.setText(isLogin ? "用户ID：" + uid : "登录/注册");

        ContactInfo contactInfo = UserInfoHelper.getContactInfo();
        if (null != contactInfo && !TextUtils.isEmpty(contactInfo.getWeixin())) {
            baseSettingViewService.setCentTitle(contactInfo.getWeixin());
        }

        initListener();
    }

    private void initListener() {
        RxView.clicks(baseSettingViewDredge).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (UserInfoHelper.isLogin(getActivity())) {
                    BasePayFragment basePayFragment = new BasePayFragment();
                    basePayFragment.show(getFragmentManager(), "");
                }
            }
        });
        RxView.clicks(baseSettingViewEquities).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            VipEquitiesFragment vipEquitiesFragment = new VipEquitiesFragment();
            vipEquitiesFragment.show(getFragmentManager(), "");
        });

        RxView.clicks(baseSettingViewOrder).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            if (UserInfoHelper.isLogin(getActivity()))
                startActivity(new Intent(getActivity(), OrderActivity.class));
        });

        RxView.clicks(baseSettingViewShare).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            ShareFragment shareFragment = new ShareFragment();
            shareFragment.show(getChildFragmentManager(), "");
        });
        RxView.clicks(baseSettingViewService).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            if (UserInfoHelper.getContactInfo() != null) {
                cm.setPrimaryClip(ClipData.newPlainText("weixin", UserInfoHelper.getContactInfo().getWeixin()));
                gotoWeixin();
            }
        });
        RxView.clicks(baseSettingViewPrivacy).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> startActivity(new Intent(getActivity(), PrivacyStatementActivity.class)));

        RxView.clicks(tvUserId).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                String uid = UserInfoHelper.getUid();
                if (TextUtils.isEmpty(uid)) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        RxView.clicks(baseSettingViewPerson).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (UserInfoHelper.isLogin(getActivity())) {
                    Intent intent = new Intent(getActivity(), PersonInfoActivity.class);
                    startActivity(intent);
                }

            }
        });
        RxView.clicks(baseSettingViewSetting).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });

        RxView.clicks(tvUserId).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                UserInfoHelper.isLogin(getActivity());
            }
        });
    }

    private void gotoWeixin() {

        try {
            ToastUtil.toast(getActivity(), "复制成功，正在前往微信");
            Intent intent = new Intent(Intent.ACTION_MAIN);
            ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            startActivity(intent);

        } catch (ActivityNotFoundException e) {
            ToastUtil.toast(getActivity(), "检查到您手机没有安装微信，请安装后使用该功能");
        }

    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.LOGIN)
            }
    )
    public void loginSuccess(UserInfo info) {
        if (info != null && !TextUtils.isEmpty(info.getUser_id()))
            tvUserId.setText(String.format("用户ID：%s", info.getUser_id()));
        else {
            tvUserId.setText("登录/注册");
        }
    }
}
