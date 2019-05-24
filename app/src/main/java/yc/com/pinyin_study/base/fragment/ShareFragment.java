package yc.com.pinyin_study.base.fragment;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.share.UMShareImpl;
import com.kk.utils.LogUtil;
import com.kk.utils.ToastUtil;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseDialogFragment;
import yc.com.blankj.utilcode.util.SPUtils;
import yc.com.pinyin_study.R;
import yc.com.pinyin_study.base.constant.BusAction;
import yc.com.pinyin_study.base.constant.SpConstant;
import yc.com.pinyin_study.index.model.domain.ShareInfo;
import yc.com.pinyin_study.index.utils.ShareInfoHelper;

/**
 * Created by wanglin  on 2019/4/10 15:40.
 */
public class ShareFragment extends BaseDialogFragment {
    @BindView(R.id.ll_wx)
    LinearLayout llWx;
    @BindView(R.id.ll_circle)
    LinearLayout llCircle;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    private ShareInfo mShareInfo;

    @Override
    protected float getWidth() {
        return 1.0f;
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
        RxView.clicks(tvCancel).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                dismiss();
            }
        });

        RxView.clicks(llWx).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                shareInfo(0);
            }
        });
        RxView.clicks(llCircle).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                shareInfo(1);
            }
        });
    }

    @Override
    public int getGravity() {
        return Gravity.BOTTOM;
    }


    private SHARE_MEDIA getShareMedia(String tag) {
        if (tag.equals("0")) {
            return SHARE_MEDIA.WEIXIN;
        }

        if (tag.equals("1")) {
            return SHARE_MEDIA.WEIXIN_CIRCLE;
        }

        if (tag.equals("2")) {
            return SHARE_MEDIA.QQ;
        }
        return SHARE_MEDIA.WEIXIN;

    }


    private void shareInfo(int tag) {
        ShareInfo shareInfo = ShareInfoHelper.getShareInfo();
        if (shareInfo != null) {
            String title = shareInfo.getTitle();
            String url = shareInfo.getUrl();
            String desc = shareInfo.getContent();
            Bitmap bitmap = null;

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

            dismiss();
//        if (bitmap != null) {
//            UMShareImpl.get().setCallback(mContext, umShareListener).shareImage("app", bitmap, bitmap, getShareMedia(tag.toString() + ""))
//        } else {
            UMShareImpl.get().setCallback(getActivity(), umShareListener).shareUrl(title, url, desc, R.mipmap.ic_launcher, getShareMedia(tag + ""));
//        }
        }
    }


    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

            loadingView.setMessage("正在分享...");
            loadingView.show();
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {

            loadingView.dismiss();
            ToastUtil.toast2(mContext, "分享成功");

            //            RxSPTool.putBoolean(mContext, SpConstant.SHARE_SUCCESS, true);

            SPUtils.getInstance().put(SpConstant.IS_SHARE, true);

            if (mShareInfo == null)
                mShareInfo = ShareInfoHelper.getShareInfo();

            RxBus.get().post(BusAction.SHARE_SUCCESS, "share_success");
//            if (mShareInfo != null) {
//
//            }

        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            loadingView.dismiss();
            LogUtil.msg("e: " + throwable.getMessage());
            ToastUtil.toast2(mContext, "分享有误");
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            loadingView.dismiss();
            ToastUtil.toast2(mContext, "取消分享");
        }
    };

    public void setShareInfo(ShareInfo shareInfo) {
        this.mShareInfo = shareInfo;
    }

}
