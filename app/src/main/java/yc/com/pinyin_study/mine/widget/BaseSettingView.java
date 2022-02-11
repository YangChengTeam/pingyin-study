package yc.com.pinyin_study.mine.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import yc.com.base.BaseView;
import yc.com.pinyin_study.R;

/**
 * Created by wanglin  on 2019/4/23 14:28.
 */
public class BaseSettingView extends BaseView {
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_centet_tile)
    TextView tvCentetTile;
    @BindView(R.id.iv_arrow)
    ImageView ivArrow;
    @BindView(R.id.divider)
    View divider;
    private String title;
    private Drawable mDrawable;
    private boolean mShowArrow;
    private boolean mShowDivider;

    public BaseSettingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BaseSettingView);
        try {
            title = ta.getString(R.styleable.BaseSettingView_settingTitle);
            mDrawable = ta.getDrawable(R.styleable.BaseSettingView_settingIcon);
            mShowArrow = ta.getBoolean(R.styleable.BaseSettingView_setting_show_arrow, true);
            mShowDivider = ta.getBoolean(R.styleable.BaseSettingView_setting_show_divider, true);

            if (!TextUtils.isEmpty(title)) tvTitle.setText(title);
            if (mDrawable != null) ivIcon.setImageDrawable(mDrawable);
            ivArrow.setVisibility(mShowArrow ? VISIBLE : GONE);
            divider.setVisibility(mShowDivider ? VISIBLE : GONE);

        } finally {
            ta.recycle();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.base_setting_view;
    }

    public void setCentTitle(CharSequence charSequence) {
        tvCentetTile.setText(charSequence);
    }
}
