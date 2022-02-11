package yc.com.pinyin_study.mine.activity;

import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;
import yc.com.blankj.utilcode.util.SPUtils;
import yc.com.pinyin_study.R;
import yc.com.pinyin_study.base.constant.SpConstant;
import yc.com.pinyin_study.mine.contract.LoginContract;
import yc.com.pinyin_study.mine.fragment.UserPolicyFragment;
import yc.com.pinyin_study.mine.presenter.LoginPresenter;

/**
 * Created by suns  on 2020/4/14 17:02.
 */
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.tv_code_login)
    TextView tvCodeLogin;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.iv_login)
    ImageView ivLogin;
    @BindView(R.id.tv_policy)
    TextView tvPolicy;

    @Override
    public boolean isStatusBarMateria() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void init() {
        mPresenter = new LoginPresenter(this, this);
        tvPolicy.setText(Html.fromHtml("登录即代表同意<font color='#efc008'>《用户协议》</font>"));
        tvCodeLogin.setText(Html.fromHtml("忘记了？<font color='#efc008'>验证码登录</font>"));
        String phone = SPUtils.getInstance().getString(SpConstant.USER_PHONE);
        if (!TextUtils.isEmpty(phone)) {
            etPhone.setText(phone);
            etPhone.setSelection(phone.length());
        }

        initListener();
    }

    private void initListener() {
        RxView.clicks(ivLogin).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                String phone = etPhone.getText().toString().trim();
                String pwd = etPwd.getText().toString().trim();
                mPresenter.login(phone, pwd);

            }
        });
        RxView.clicks(tvCodeLogin).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.putExtra("isLogin", true);
                startActivity(intent);
                finish();
            }
        });
        RxView.clicks(tvRegister).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
        RxView.clicks(tvPolicy).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                UserPolicyFragment userPolicyFragment = new UserPolicyFragment();
                userPolicyFragment.show(getSupportFragmentManager(), "");
            }
        });
    }


    @Override
    public void showDisplayCode() {

    }


}
