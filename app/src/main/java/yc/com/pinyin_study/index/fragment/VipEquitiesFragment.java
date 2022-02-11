package yc.com.pinyin_study.index.fragment;

import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseDialogFragment;
import yc.com.pinyin_study.R;
import yc.com.rthttplibrary.util.ScreenUtil;

/**
 * Created by wanglin  on 2018/11/12 15:36.
 */
public class VipEquitiesFragment extends BaseDialogFragment {
    @BindView(R.id.tv_tint)
    TextView tvTint;
    @BindView(R.id.iv_close)
    ImageView ivClose;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_vip_equities;
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

    @Override
    protected float getWidth() {
        return 0.9f;
    }

    @Override
    public int getAnimationId() {
        return 0;
    }

    @Override
    public int getHeight() {
        return ScreenUtil.getHeight(getActivity()) * 9 / 10;
    }

}
