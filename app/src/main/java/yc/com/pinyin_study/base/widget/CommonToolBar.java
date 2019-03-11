package yc.com.pinyin_study.base.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;
import yc.com.base.BaseView;
import yc.com.base.StatusBarUtil;
import yc.com.pinyin_study.R;

/**
 * Created by wanglin  on 2019/3/7 10:20.
 */
public class CommonToolBar extends BaseView {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_share)
    ImageView ivShare;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.iv_left_back)
    ImageView ivLeftBack;
    private boolean isShowNavigationIcon;

    public CommonToolBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public int getLayoutId() {
        return R.layout.common_toolbar;
    }

    public void setTitle(CharSequence title) {
        tvTitle.setText(title);
    }

    public void showNavigation() {
//        toolBar.setNavigationIcon(R.mipmap.black_left_arrow);
        ivLeftBack.setVisibility(VISIBLE);
        isShowNavigationIcon = true;
    }

    public void init(final BaseActivity activity) {
//        toolBar.setTitle("");
        StatusBarUtil.setStatusTextColor1(true, activity);
//        activity.setSupportActionBar(toolBar);
//        if (isShowNavigationIcon) {
//            toolBar.setNavigationOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    activity.finish();
//                }
//            });
//        }

        RxView.clicks(ivShare).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //todo 分享
            }
        });
        RxView.clicks(ivLeftBack).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                activity.finish();
            }
        });

    }

    public void setRightVisable(boolean flag) {
        ivShare.setVisibility(flag ? VISIBLE : GONE);
    }
}
