package yc.com.pinyin_study.study.activity;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import butterknife.BindView;
import yc.com.base.BaseActivity;
import yc.com.pinyin_study.R;
import yc.com.pinyin_study.base.widget.CommonWebView;
import yc.com.pinyin_study.base.widget.MainToolBar;

/**
 * Created by wanglin  on 2018/11/8 15:03.
 */
public class PrivacyPolicyActivity extends BaseActivity {
    @BindView(R.id.main_toolbar)
    MainToolBar mainToolbar;




    @Override
    public boolean isStatusBarMateria() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_privacy_statement;
    }

    @Override
    public void init() {
        mainToolbar.showNavigationIcon();
        mainToolbar.init(this);
        mainToolbar.setRightContainerVisible(false);
        mainToolbar.setTitle(getString(R.string.privacy_policy));




    }


}
