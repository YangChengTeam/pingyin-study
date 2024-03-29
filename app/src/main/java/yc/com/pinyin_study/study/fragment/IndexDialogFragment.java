package yc.com.pinyin_study.study.fragment;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import yc.com.blankj.utilcode.util.SPUtils;
import yc.com.pinyin_study.R;
import yc.com.pinyin_study.base.EnglishStudyApp;
import yc.com.pinyin_study.base.constant.SpConstant;
import yc.com.pinyin_study.index.fragment.IndexNoticeDialogFragment;
import yc.com.rthttplibrary.util.ScreenUtil;
import yc.com.rthttplibrary.util.ToastUtil;

/**
 * Created by wanglin  on 2019/4/12 14:40.
 */
public class IndexDialogFragment extends DialogFragment {

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Window window = Objects.requireNonNull(getDialog()).getWindow();


        if (rootView == null) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            rootView = inflater.inflate(R.layout.fragment_index_dialog, container, false);
//            window.setLayout((int) (RxDeviceTool.getScreenWidth(getActivity()) * getWidth()), getHeight());//这2行,和上面的一样,注意顺序就行;
            window.setWindowAnimations(getAnimationId());
        }
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);

        initView();


        return rootView;

    }


    public View getView(int resId) {
        return rootView.findViewById(resId);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//注意此处

            WindowManager.LayoutParams layoutParams = window.getAttributes();

            layoutParams.width = (int) (ScreenUtil.getWidth(getActivity()) * getWidth());
            layoutParams.height = getHeight();
            window.setAttributes(layoutParams);
        }

    }

    private boolean mChecked;

    private void initView() {


        CheckBox cb = (CheckBox) getView(R.id.cb_privacy);
        ((TextView) getView(R.id.tv_privacy)).setText(EnglishStudyApp.privacyPolicy);

        final TextView tvEnterApp = (TextView) getView(R.id.tv_enter_app);
        mChecked = cb.isChecked();

        cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mChecked = isChecked;
            if (isChecked) {
                tvEnterApp.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.app_selected_color));
            } else {
                tvEnterApp.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.gray_ddd));
            }
        });


        RxView.clicks(tvEnterApp).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            if (mChecked) {

//                SharePreferenceUtils.getInstance().putBoolean(Config.index_dialog, true);
                SPUtils.getInstance().put(SpConstant.INDEX_DIALOG, true);
//                    PromotionDialogFragment promotionDialogFragment = new PromotionDialogFragment();
//                    if (getActivity() != null) {
//                        promotionDialogFragment.show(getActivity().getSupportFragmentManager(), "");
//                    }
                IndexNoticeDialogFragment indexNoticeDialogFragment = new IndexNoticeDialogFragment();
                if (getActivity() != null)
                    indexNoticeDialogFragment.show(getActivity().getSupportFragmentManager(), "");

                dismiss();
            } else {
                ToastUtil.toast(getActivity(), "请先同意用户协议");
            }
        });


//        RxView.clicks(view).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> dismiss());
//
//        RxView.clicks(rootView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
//            String str = PreferenceUtil.getImpl(getActivity()).getString(Config.ADV_INFO, "");
//            final AdvInfo advInfo = JSON.parseObject(str, AdvInfo.class);
//            if (null != advInfo) {
//                Intent intent = new Intent(getActivity(), AdvInfoActivity.class);
//                intent.putExtra("url", advInfo.getUrl());
//                intent.putExtra("title", advInfo.getButton_txt());
//                startActivity(intent);
//            }
//            dismiss();
//        });
    }

    protected float getWidth() {
        return 0.8f;
    }


    protected int getAnimationId() {
        return R.style.share_anim;
    }

    public int getHeight() {
        return ScreenUtil.getHeight(getActivity()) * 3 / 5;
    }

    protected int getGravity() {
        return Gravity.CENTER;
    }
}
