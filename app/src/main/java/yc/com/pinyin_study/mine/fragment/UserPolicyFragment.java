package yc.com.pinyin_study.mine.fragment;

import android.widget.ImageView;

import com.jakewharton.rxbinding.view.RxView;


import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseDialogFragment;
import yc.com.base.BasePresenter;
import yc.com.pinyin_study.R;
import yc.com.rthttplibrary.util.ScreenUtil;

/**
 * Created by suns  on 2020/4/17 16:28.
 */
public class UserPolicyFragment extends BaseDialogFragment<BasePresenter> {
    @BindView(R.id.iv_close)
    ImageView ivClose;

    @Override
    protected float getWidth() {
        return 0.7f;
    }

    @Override
    public int getAnimationId() {
        return R.style.share_anim;
    }

    @Override
    public int getHeight() {
        return ScreenUtil.getHeight(getActivity()) * 3 / 5;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_user_policy;
    }

    @Override
    public void init() {
        RxView.clicks(ivClose).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                dismiss();
            }
        });
    }
}
