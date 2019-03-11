package yc.com.pinyin_study.base.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.qq.e.ads.nativ.NativeExpressADView;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import yc.com.base.BaseActivity;
import yc.com.pinyin_study.R;
import yc.com.pinyin_study.base.constant.Config;
import yc.com.pinyin_study.index.utils.UserInfoHelper;
import yc.com.tencent_adv.AdvDispatchManager;
import yc.com.tencent_adv.AdvType;
import yc.com.tencent_adv.OnAdvStateListener;

/**
 * Created by wanglin  on 2018/10/24 11:17.
 */
public class SplashActivity extends BaseActivity implements OnAdvStateListener {
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
        if (UserInfoHelper.isCloseAdv()) {
            splashContainer.setVisibility(View.GONE);
            switchMain(1000);
        } else {
            AdvDispatchManager.getManager().init(this, AdvType.SPLASH, splashContainer, skipView, Config.TENCENT_ADV, Config.SPLASH_ADV, this);
        }
    }


    private void switchMain(long delayTime) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, delayTime);


    }


    @Override
    public boolean isStatusBarMateria() {
        return true;
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

    @Override
    protected void onResume() {
        super.onResume();
        if (!UserInfoHelper.isCloseAdv())
            AdvDispatchManager.getManager().onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!UserInfoHelper.isCloseAdv())
            AdvDispatchManager.getManager().onPause();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!UserInfoHelper.isCloseAdv())
            AdvDispatchManager.getManager().onRequestPermissionsResult(requestCode, permissions, grantResults);
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
}
