package yc.com.pinyin_study.mine.activity;

import android.widget.TextView;

import butterknife.BindView;
import yc.com.base.BaseActivity;
import yc.com.base.BasePresenter;
import yc.com.pinyin_study.R;
import yc.com.pinyin_study.base.EnglishStudyApp;
import yc.com.pinyin_study.base.widget.MainToolBar;

public class PrivacyStatementActivity extends BaseActivity<BasePresenter> {


    @BindView(R.id.main_toolbar)
    MainToolBar mainToolbar;
    @BindView(R.id.tv_privacy)
    TextView tvPrivacy;

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
        mainToolbar.setTitle("隐私声明");
        mainToolbar.init(this);
        mainToolbar.setRightContainerVisible(false);
        tvPrivacy.setText(EnglishStudyApp.privacyPolicy);
    }


}
