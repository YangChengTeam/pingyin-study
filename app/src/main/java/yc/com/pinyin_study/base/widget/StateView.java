package yc.com.pinyin_study.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseView;
import yc.com.blankj.utilcode.util.SizeUtils;
import yc.com.pinyin_study.R;

/**
 * Created by zhangkai on 2017/8/7.
 */

public class StateView extends BaseView {
    @BindView(R.id.iv_loading)
    ImageView mLoadingImageView;

    @BindView(R.id.tv_message)
    TextView mMessageTextView;

    @BindView(R.id.btn_refresh)
    Button mRefreshButton;

    private View mContextView;
    private Context mContext;

    public StateView(Context context) {
        super(context);
        this.mContext = context;
    }

    public StateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.base_view_state;
    }

    public void hide() {
        Glide.with(mContext).clear(mLoadingImageView);
        setVisibility(View.GONE);
        if (mContextView != null) {
            mContextView.setVisibility(View.VISIBLE);
        }
    }

    public void showLoading(View contextView, int type) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mLoadingImageView.getLayoutParams();
        layoutParams.width = SizeUtils.dp2px(540 / 3);
        layoutParams.height = SizeUtils.dp2px(960 / 3);
        mLoadingImageView.setLayoutParams(layoutParams);
        mLoadingImageView.invalidate();
        mContextView = contextView;
        mMessageTextView.setText("");
        setVisibility(View.VISIBLE);
        mRefreshButton.setVisibility(View.GONE);
        mContextView.setVisibility(View.GONE);
//        RequestOptions requestOptions = new RequestOptions();
//        requestOptions.override(SizeUtils.dp2px(1080 / 3), SizeUtils.dp2px(408 / 3));
//        if (type == 1) {

        Glide.with(mContext).asGif().apply(new RequestOptions().override(SizeUtils.dp2px(540 / 3), SizeUtils.dp2px(960 / 3))).load(R.mipmap.base_loading).into(mLoadingImageView);
//        } else {
//            Glide.with(mContext).load(R.mipmap.base_loading2).apply(requestOptions).into(mLoadingImageView);
//        }
    }


    public void showLoading(View contextView) {
        showLoading(contextView, 1);
    }

    public void showNoData(View contextView, String message) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mLoadingImageView.getLayoutParams();
        layoutParams.width = SizeUtils.dp2px(425 / 3);
        layoutParams.height = SizeUtils.dp2px(405 / 3);
        mLoadingImageView.setLayoutParams(layoutParams);
        mContextView = contextView;
        setVisibility(View.VISIBLE);
        mRefreshButton.setVisibility(View.GONE);
        mContextView.setVisibility(View.GONE);

        mMessageTextView.setText(message);
        Glide.with(mContext).clear(mLoadingImageView);
//        RequestOptions requestOptions = new RequestOptions();
//        requestOptions.override(SizeUtils.dp2px(312 / 3), SizeUtils.dp2px(370 / 3));
        Glide.with(mContext).asBitmap().
                apply(new RequestOptions().override(SizeUtils.dp2px(425 / 3), SizeUtils.dp2px(405 / 3))).load(R.mipmap.base_no_data).into(mLoadingImageView);
    }

    public void showNoData(View contextView) {
        showNoData(contextView, "暂无数据");
    }

    public void showNoNet(View contextView, String message, final View.OnClickListener onClickListener) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mLoadingImageView.getLayoutParams();
        layoutParams.width = SizeUtils.dp2px(490 / 3);
        layoutParams.height = SizeUtils.dp2px(485 / 3);
        mLoadingImageView.setLayoutParams(layoutParams);
        mContextView = contextView;
        setVisibility(View.VISIBLE);
        mContextView.setVisibility(View.GONE);
        mRefreshButton.setVisibility(View.VISIBLE);
        mMessageTextView.setText(message);
        Glide.with(mContext).clear(mLoadingImageView);
//        RequestOptions requestOptions = new RequestOptions();
//        requestOptions.override(SizeUtils.dp2px(312 / 3), SizeUtils.dp2px(370 / 3));
//        if (ActivityUtils.isValidContext(getContext())) {
        Glide.with(mContext).asBitmap().apply(new RequestOptions()
                .override(SizeUtils.dp2px(490 / 3), SizeUtils.dp2px(485 / 3))).load(R.mipmap.base_no_wifi).into(mLoadingImageView);
//        }
        RxView.clicks(mRefreshButton).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                onClickListener.onClick(mRefreshButton);
            }
        });
    }
}
