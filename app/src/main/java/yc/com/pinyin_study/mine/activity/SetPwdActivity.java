package yc.com.pinyin_study.mine.activity;

import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import yc.com.base.BaseActivity;
import yc.com.pinyin_study.R;
import yc.com.pinyin_study.base.widget.MainToolBar;
import yc.com.pinyin_study.mine.contract.LoginContract;
import yc.com.pinyin_study.mine.presenter.LoginPresenter;

/**
 * Created by suns  on 2020/4/15 16:58.
 */
public class SetPwdActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {
    @BindView(R.id.main_toolbar)
    MainToolBar mainToolbar;
    @BindView(R.id.tv_pwd)
    EditText tvPwd;

    @Override
    public boolean isStatusBarMateria() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_set_pwd;
    }

    @Override
    public void init() {
        mPresenter = new LoginPresenter(this, this);
        mainToolbar.showNavigationIcon();
        mainToolbar.init(this);
        mainToolbar.setTitle("设置密码");
        TextView tvDone = new TextView(this);
        tvDone.setText("完成");
        tvDone.setTextColor(ContextCompat.getColor(this, R.color.white));

        mainToolbar.addRightView(tvDone);

        RxView.clicks(tvDone).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            String pwd = tvPwd.getText().toString().trim();

            mPresenter.setPwd(pwd);
        });
    }

    @Override
    public void showDisplayCode() {

    }


}
