package yc.com.pinyin_study.mine.activity;

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
public class RegisterActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {

    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    @BindView(R.id.tv_count_down)
    TextView tvCountDown;
    @BindView(R.id.iv_register)
    ImageView ivRegister;
    @BindView(R.id.tv_policy)
    TextView tvPolicy;
    private boolean isLogin = false;


    @Override
    public boolean isStatusBarMateria() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void init() {
        mPresenter = new LoginPresenter(this, this);
        if (getIntent() != null) {
            this.isLogin = getIntent().getBooleanExtra("isLogin", false);
        }
        tvPolicy.setText(Html.fromHtml("注册即代表同意<font color='#efc008'>《用户协议》</font>"));
        String phone = SPUtils.getInstance().getString(SpConstant.USER_PHONE);
        if (!TextUtils.isEmpty(phone)) {
            etPhone.setText(phone);
            etPhone.setSelection(phone.length());
        }

        initListener();
    }

    private void initListener() {
        RxView.clicks(ivRegister).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                String phone = etPhone.getText().toString().trim();
                String code = etCode.getText().toString().trim();
                mPresenter.codeLogin(phone, code, isLogin);

            }
        });
        RxView.clicks(tvGetCode).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                String phone = etPhone.getText().toString().trim();
                mPresenter.sendCode(phone);
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
        showGetCodeDisplay(tvGetCode, tvCountDown);
    }


}
