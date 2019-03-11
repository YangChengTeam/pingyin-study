package yc.com.pinyin_study.index.fragment;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;
import yc.com.base.BaseFragment;
import yc.com.pinyin_study.R;
import yc.com.pinyin_study.base.widget.MainToolBar;
import yc.com.pinyin_study.index.contract.IndexContract;
import yc.com.pinyin_study.index.model.domain.ContactInfo;
import yc.com.pinyin_study.index.model.domain.IndexInfoWrapper;
import yc.com.pinyin_study.index.model.domain.UserInfo;
import yc.com.pinyin_study.index.presenter.IndexPresenter;

/**
 * Created by wanglin  on 2018/10/24 17:21.
 */
public class PhoneticFragment extends BaseFragment<IndexPresenter> implements IndexContract.View {


    @BindView(R.id.main_toolbar)
    MainToolBar mainToolbar;
    @BindView(R.id.tv_service_tel)
    TextView tvServiceTel;
    @BindView(R.id.tv_service_wechat)
    TextView tvServiceWechat;
    @BindView(R.id.iv_vip)
    ImageView ivVip;
    @BindView(R.id.tv_userid)
    TextView tvUserid;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_phonetic;
    }

    @Override
    public void init() {
        mPresenter = new IndexPresenter(getActivity(), this);

        mainToolbar.init(((BaseActivity) getActivity()), null);

        RxView.clicks(ivVip).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                MobclickAgent.onEvent(getActivity(), "vip_interest", "会员权益");
//                Intent intent = new Intent(getActivity(), VipEquitiesActivity.class);
//                startActivity(intent);
                VipEquitiesFragment vipEquitiesFragment = new VipEquitiesFragment();
                vipEquitiesFragment.show(getFragmentManager(), "");

            }
        });

        RxView.clicks(tvServiceWechat).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setPrimaryClip(ClipData.newPlainText("weixin", tvServiceWechat.getText().toString().trim()));
                gotoWeixin();
            }
        });

    }


    private void gotoWeixin() {

        try {
            ToastUtil.toast2(getActivity(), "复制成功，正在前往微信");
            Intent intent = new Intent(Intent.ACTION_MAIN);
            ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            startActivity(intent);

        } catch (ActivityNotFoundException e) {
            ToastUtil.toast2(getActivity(), "检查到您手机没有安装微信，请安装后使用该功能");
        }

    }

    @Override
    public void showIndexInfo(IndexInfoWrapper indexInfoWrapper) {
        if (indexInfoWrapper != null) {

            ContactInfo contactInfo = indexInfoWrapper.getContact_info();
            UserInfo userInfo = indexInfoWrapper.getUserInfo();
            if (!TextUtils.isEmpty(contactInfo.getTel()))
                tvServiceTel.setText(contactInfo.getTel());
            if (!TextUtils.isEmpty(contactInfo.getWeixin()))
                tvServiceWechat.setText(contactInfo.getWeixin());
            tvUserid.setText(String.format(getString(R.string.user_id), userInfo.getUid()));
        }
    }

}
