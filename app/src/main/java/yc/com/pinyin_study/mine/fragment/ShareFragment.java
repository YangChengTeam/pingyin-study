package yc.com.pinyin_study.mine.fragment;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.kk.share.UMShareImpl;

import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseDialogFragment;
import yc.com.pinyin_study.R;
import yc.com.pinyin_study.index.model.domain.ShareInfo;
import yc.com.pinyin_study.index.utils.ShareInfoHelper;
import yc.com.pinyin_study.mine.contract.ShareContract;
import yc.com.pinyin_study.mine.presenter.SharePresenter;
import yc.com.rthttplibrary.util.LogUtil;
import yc.com.rthttplibrary.util.ToastUtil;

/**
 * Created by wanglin  on 2019/4/28 09:12.
 */
public class ShareFragment extends BaseDialogFragment<SharePresenter> implements ShareContract.View {
    @BindView(R.id.ll_wx)
    LinearLayout llWx;
    @BindView(R.id.ll_circle)
    LinearLayout llCircle;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;

    @Override
    protected float getWidth() {
        return 1f;
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
        return R.layout.fragment_share;
    }

    @Override
    public void init() {
        mPresenter = new SharePresenter(getActivity(), this);
        RxView.clicks(tvCancel).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                dismiss();
            }
        });
        List<View> shareItemViews = new ArrayList<>();
        shareItemViews.add(llWx);
        shareItemViews.add(llCircle);

        for (int i = 0; i < shareItemViews.size(); i++) {
            View view = shareItemViews.get(i);
            view.setTag(i);
            final int finalI = i;
            RxView.clicks(view).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
                    shareInfo(finalI);

                }
            });
        }


    }


    private ShareInfo mShareInfo;
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            loadingView.setMessage("正在分享...");
            loadingView.show();
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            loadingView.dismiss();
            ToastUtil.toast(mContext, "发送成功");
            //            RxSPTool.putBoolean(mContext, SpConstant.SHARE_SUCCESS, true);

            if (mShareInfo == null)
                mShareInfo = ShareInfoHelper.getShareInfo();
            mPresenter.share();


        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            loadingView.dismiss();
            ToastUtil.toast(mContext, "分享有误");
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            loadingView.dismiss();
            ToastUtil.toast(mContext, "取消发送");
        }
    };

    private SHARE_MEDIA getShareMedia(String tag) {
        if (tag.equals("0")) {
            return SHARE_MEDIA.WEIXIN;
        }
        if (tag.equals("1")) {
            return SHARE_MEDIA.WEIXIN_CIRCLE;
        } else return SHARE_MEDIA.WEIXIN;

    }


    private void shareInfo(int tag) {
        ShareInfo shareInfo = ShareInfoHelper.getShareInfo();

        String title = shareInfo.getTitle();
        String url = shareInfo.getUrl();
        String desc = shareInfo.getContent();

        LogUtil.msg("url: " + url);

        if (mShareInfo != null) {
            if (!TextUtils.isEmpty(mShareInfo.getUrl())) {
                url = mShareInfo.getUrl();
            }
            if (!TextUtils.isEmpty(mShareInfo.getTitle())) {
                title = mShareInfo.getTitle();
            }
            if (!TextUtils.isEmpty(mShareInfo.getContent())) {
                desc = mShareInfo.getContent();
            }
        }

        UMShareImpl.get().setCallback(mContext, umShareListener).shareUrl(title, url, desc, R.mipmap.ic_launcher, getShareMedia(tag + ""));
        dismiss();

    }

    @Override
    public int getGravity() {
        return Gravity.BOTTOM;
    }
}
