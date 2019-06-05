package yc.com.pinyin_study.base.activity;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.kk.share.UMShareImpl;
import com.kk.utils.LogUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.vondear.rxtools.RxPermissionsTool;
import com.xinqu.videoplayer.XinQuVideoPlayer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import yc.com.base.BaseActivity;
import yc.com.pinyin_study.R;
import yc.com.pinyin_study.base.adapter.MainAdapter;
import yc.com.pinyin_study.base.fragment.ExitFragment;
import yc.com.pinyin_study.base.utils.BottomNavigationViewHelper;
import yc.com.pinyin_study.base.utils.UIUtils;
import yc.com.pinyin_study.category.fragment.CategoryFragment;
import yc.com.pinyin_study.error.fragment.ErrorFragment;
import yc.com.pinyin_study.index.fragment.PhoneticFragment;
import yc.com.pinyin_study.study.fragment.StudyFragment;
import yc.com.pinyin_study.study.utils.AVMediaManager;
import yc.com.pinyin_study.study_1vs1.fragment.Study1vs1Fragment;
import yc.com.tencent_adv.AdvDispatchManager;

public class MainActivity extends BaseActivity {

    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    @BindView(R.id.viewPager)
    ViewPager viewPager;


    private List<Fragment> fragments = new ArrayList<>();


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {

        fragments.add(new PhoneticFragment());
        fragments.add(new StudyFragment());
        fragments.add(new CategoryFragment());
        fragments.add(new ErrorFragment());
        fragments.add(new Study1vs1Fragment());
        initNavigation();
        initViewPager();
        applyPermission();
        UIUtils.getInstance(this).measureBottomBarHeight(navigation);
    }


    /**
     * 初始化viewPager
     */
    private void initViewPager() {
        viewPager.setOffscreenPageLimit(4);
        MainAdapter mainAdapter = new MainAdapter(getSupportFragmentManager(), fragments);

        viewPager.setAdapter(mainAdapter);
        viewPager.setCurrentItem(1);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                navigation.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    /**
     * 初始化navigation
     */

    private void initNavigation() {
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setItemIconTintList(null);
        navigation.getMenu().getItem(1).setChecked(true);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    MobclickAgent.onEvent(MainActivity.this, "first_page", "首页");
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_study:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_category:
                    MobclickAgent.onEvent(MainActivity.this, "mini_class", "微课");
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_error:
                    viewPager.setCurrentItem(3);
                    return true;
                case R.id.navigation_1vs1:
                    viewPager.setCurrentItem(4);

                    MobclickAgent.onEvent(MainActivity.this, "one_to_one_click", "1对1");
                    return true;

            }
            return false;
        }
    };


    @Override
    public void onBackPressed() {
        if (XinQuVideoPlayer.backPress()) {
            return;
        }
        final ExitFragment exitFragment = new ExitFragment();
        exitFragment.show(getSupportFragmentManager(), "");
        exitFragment.setOnConfirmListener(new ExitFragment.onConfirmListener() {
            @Override
            public void onConfirm() {
                exitFragment.dismiss();
                AVMediaManager.getInstance().releaseAudioManager();
                finish();
                System.exit(0);
            }
        });

    }


    @Override
    public boolean isStatusBarMateria() {
        return true;
    }


    /**
     * Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,
     * Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE,
     * Manifest.permission.SET_DEBUG_APP,
     * Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.GET_ACCOUNTS,Manifest.permission.WRITE_APN_SETTINGS
     */
    private void applyPermission() {
        RxPermissionsTool.
                with(this).
                addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).
                addPermission(Manifest.permission.RECORD_AUDIO).
                addPermission(Manifest.permission.ACCESS_COARSE_LOCATION).
                addPermission(Manifest.permission.ACCESS_FINE_LOCATION).
                initPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            for (String permission : permissions) {
                LogUtil.msg("TAG: " + permission);
            }

        }
        AdvDispatchManager.getManager().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, requestCode, data);
    }
}
