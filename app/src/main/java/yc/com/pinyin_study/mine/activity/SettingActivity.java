package yc.com.pinyin_study.mine.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.jakewharton.rxbinding.view.RxView;
import com.tencent.bugly.beta.Beta;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;
import yc.com.base.BasePresenter;
import yc.com.pinyin_study.R;
import yc.com.pinyin_study.base.constant.BusAction;
import yc.com.pinyin_study.base.utils.DataCleanManagerUtils;
import yc.com.pinyin_study.base.widget.MainToolBar;
import yc.com.pinyin_study.index.model.domain.UserInfo;
import yc.com.pinyin_study.index.utils.UserInfoHelper;

/**
 * Created by suns  on 2020/4/15 17:58.
 */
public class SettingActivity extends BaseActivity<BasePresenter> {
    @BindView(R.id.main_toolbar)
    MainToolBar mainToolbar;
    @BindView(R.id.tv_version_code)
    TextView tvVersionCode;
    @BindView(R.id.rl_version_code)
    RelativeLayout rlVersionCode;
    @BindView(R.id.tv_cache)
    TextView tvCache;
    @BindView(R.id.rl_cache)
    RelativeLayout rlCache;
    @BindView(R.id.tv_logout)
    TextView tvLogout;

    @Override
    public boolean isStatusBarMateria() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void init() {
        mainToolbar.showNavigationIcon();
        mainToolbar.init(this);
        mainToolbar.setTitle("系统设置");
        mainToolbar.setRightContainerVisible(false);

        tvVersionCode.setText(getCode());
        try {
            tvCache.setText(DataCleanManagerUtils.getTotalCacheSize(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(UserInfoHelper.getUid())) {
            tvLogout.setVisibility(View.GONE);
        } else {
            tvLogout.setVisibility(View.VISIBLE);
        }

        initListener();

    }

    private void initListener() {
        RxView.clicks(rlVersionCode).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Beta.checkUpgrade(true, false);
            }
        });

        RxView.clicks(rlCache).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                DataCleanManagerUtils.clearAllCache(SettingActivity.this);
                tvCache.setText("0M");
            }
        });

        RxView.clicks(tvLogout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                UserInfoHelper.logout();
                tvLogout.setVisibility(View.GONE);
                RxBus.get().post(BusAction.LOGIN, new UserInfo());
            }
        });
    }


    private String getCode() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";

    }


}
