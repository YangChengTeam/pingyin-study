package yc.com.pinyin_study.mine.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import yc.com.base.BaseActivity;
import yc.com.pinyin_study.R;
import yc.com.pinyin_study.base.widget.MainToolBar;
import yc.com.pinyin_study.mine.contract.LoginContract;
import yc.com.pinyin_study.mine.presenter.LoginPresenter;

/**
 * Created by suns  on 2020/4/15 16:58.
 */
public class ModifyPWdActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {
    @BindView(R.id.main_toolbar)
    MainToolBar mainToolbar;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_new_pwd)
    EditText etNewPwd;

    @Override
    public boolean isStatusBarMateria() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_modify_pwd;
    }

    @Override
    public void init() {
        mPresenter = new LoginPresenter(this, this);
        mainToolbar.showNavigationIcon();
        mainToolbar.init(this);
        mainToolbar.setTitle("修改密码");
        TextView tvDone = new TextView(this);
        tvDone.setText("完成");
        tvDone.setTextColor(ContextCompat.getColor(this, R.color.white));

        mainToolbar.addRightView(tvDone);

        RxView.clicks(tvDone).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            String pwd = etPwd.getText().toString().trim();
            String newPwd = etNewPwd.getText().toString().trim();
            mPresenter.modifyPwd(pwd, newPwd);
        });
    }

    @Override
    public void showDisplayCode() {

    }


}
