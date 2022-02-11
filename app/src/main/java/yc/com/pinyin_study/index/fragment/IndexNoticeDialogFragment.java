package yc.com.pinyin_study.index.fragment;

import android.text.Html;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseDialogFragment;
import yc.com.base.BasePresenter;
import yc.com.pinyin_study.R;

/**
 * Created by suns  on 2020/4/15 20:16.
 */
public class IndexNoticeDialogFragment extends BaseDialogFragment<BasePresenter> {
    @BindView(R.id.tv_notice_content)
    TextView tvNoticeContent;
    @BindView(R.id.tv_know)
    TextView tvKnow;

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
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_index_notice;
    }

    @Override
    public void init() {
        tvNoticeContent.setText(Html.fromHtml("系统升级，更新用户登录注册后退出重启即可正常使用！如遇问题，请加客服微信<font color='#2bd88f'>nuanjiguiren886</font>"));
        RxView.clicks(tvKnow).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                dismiss();
            }
        });
    }
}
