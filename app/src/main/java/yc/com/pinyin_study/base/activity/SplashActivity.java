package yc.com.pinyin_study.base.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.qq.e.ads.nativ.NativeExpressADView;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import yc.com.base.BaseActivity;
import yc.com.pinyin_study.R;
import yc.com.pinyin_study.base.constant.Config;
import yc.com.pinyin_study.index.utils.UserInfoHelper;
import yc.com.tencent_adv.AdvDispatchManager;
import yc.com.tencent_adv.AdvType;
import yc.com.toutiao_adv.OnAdvStateListener;
import yc.com.toutiao_adv.TTAdDispatchManager;
import yc.com.toutiao_adv.TTAdType;

/**
 * Created by wanglin  on 2018/10/24 11:17.
 */
public class SplashActivity extends BaseActivity implements OnAdvStateListener, yc.com.tencent_adv.OnAdvStateListener {
    @BindView(R.id.iv_splash)
    ImageView ivSplash;
    @BindView(R.id.splash_container)
    FrameLayout splashContainer;
    @BindView(R.id.skip_view)
    TextView skipView;


    private Handler mHandler = new Handler();

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void init() {
        if (UserInfoHelper.isCloseAdv() || Build.BRAND.toUpperCase().equals("HUAWEI") || Build.BRAND.toUpperCase().equals("HONOR")) {
            splashContainer.setVisibility(View.GONE);
            switchMain(1000);
        } else {


            TTAdDispatchManager.getManager().init(this, TTAdType.SPLASH, splashContainer, Config.TOUTIAO_SPLASH_ID, 0, null, 0, null, 0, this);

        }
    }


    private void switchMain(long delayTime) {
        mHandler.postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, delayTime);


    }


    @Override
    public boolean isStatusBarMateria() {
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!UserInfoHelper.isCloseAdv()) {
            if (Build.BRAND.toUpperCase().equals("HUAWEI") || Build.BRAND.toUpperCase().equals("HONOR")) {
//                AdvDispatchManager.getManager().onResume();
            } else
                TTAdDispatchManager.getManager().onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!UserInfoHelper.isCloseAdv()) {
            if (Build.BRAND.toUpperCase().equals("HUAWEI") || Build.BRAND.toUpperCase().equals("HONOR")) {
//                AdvDispatchManager.getManager().onPause();
            } else
                TTAdDispatchManager.getManager().onStop();
        }
    }


    //防止用户返回键退出 APP
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void loadSuccess() {
        switchMain(0);
    }

    @Override
    public void loadFailed() {
        switchMain(0);
    }

    @Override
    public void clickAD() {
        switchMain(0);
    }

    @Override
    public void onTTNativeExpressed(List<TTNativeExpressAd> ads) {

    }

    @Override
    public void onShow() {
        ivSplash.setVisibility(View.GONE);
        skipView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDismiss(long delayTime) {
        switchMain(delayTime);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onNativeExpressDismiss(NativeExpressADView view) {

    }

    @Override
    public void onNativeExpressShow(Map<NativeExpressADView, Integer> mDatas) {

    }
}
