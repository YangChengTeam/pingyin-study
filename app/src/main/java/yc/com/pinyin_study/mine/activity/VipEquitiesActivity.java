package yc.com.pinyin_study.mine.activity;

import butterknife.BindView;
import yc.com.base.BaseActivity;
import yc.com.base.BasePresenter;
import yc.com.pinyin_study.R;
import yc.com.pinyin_study.base.widget.MainToolBar;

/**
 * Created by wanglin  on 2019/4/25 14:49.
 */
public class VipEquitiesActivity extends BaseActivity<BasePresenter> {
    @BindView(R.id.main_toolbar)
    MainToolBar mainToolbar;

    @Override
    public boolean isStatusBarMateria() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_vip_equities;
    }

    @Override
    public void init() {
        mainToolbar.showNavigationIcon();
        mainToolbar.setTitle(getString(R.string.vip_equities));
        mainToolbar.setRightContainerVisible(false);
        mainToolbar.init(this);
    }


}
