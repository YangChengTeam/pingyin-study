package yc.com.pinyin_study.base.fragment;

import android.widget.EditText;
import android.widget.ImageView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;
import yc.com.base.BaseDialogFragment;
import yc.com.pinyin_study.R;
import yc.com.pinyin_study.base.contract.BasePhoneContract;
import yc.com.pinyin_study.base.presenter.BasePhonePresenter;
import yc.com.rthttplibrary.util.ScreenUtil;

/**
 * Created by wanglin  on 2018/11/1 11:11.
 */
public class BasePhoneFragment extends BaseDialogFragment<BasePhonePresenter> implements BasePhoneContract.View {
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.iv_submit)
    ImageView ivSubmit;

    @Override
    protected float getWidth() {
        return 0.8f;
    }

    @Override
    public int getAnimationId() {
        return 0;
    }

    @Override
    public int getHeight() {
        return ScreenUtil.getHeight(getActivity()) * 3 / 10;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_base_phone;
    }

    @Override
    public void init() {

        mPresenter = new BasePhonePresenter(getActivity(), this);
        RxView.clicks(ivSubmit).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                String phone = etPhone.getText().toString().trim();
                mPresenter.uploadPhone(phone);

            }
        });

    }

    @Override
    public void showLoadingDialog(String mess) {
        ((BaseActivity) getActivity()).showLoadingDialog(mess);
    }

    @Override
    public void dismissDialog() {
        ((BaseActivity) getActivity()).dismissDialog();
    }


    @Override
    public void showUploadSuccess() {
        dismiss();
    }
}
