package yc.com.pinyin_study.study.fragment;

import android.text.method.LinkMovementMethod;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.jakewharton.rxbinding.view.RxView;
import com.umeng.commonsdk.UMConfigure;
import com.vondear.rxtools.RxSPTool;

import butterknife.BindView;
import yc.com.base.BaseDialogFragment;
import yc.com.base.BasePresenter;
import yc.com.base.IView;
import yc.com.blankj.utilcode.util.SPUtils;
import yc.com.blankj.utilcode.util.SpanUtils;
import yc.com.pinyin_study.R;
import yc.com.pinyin_study.base.constant.Config;
import yc.com.pinyin_study.base.constant.SpConstant;
import yc.com.pinyin_study.base.model.engine.BaseEngine;

/**
 * Created by suns  on 2021/1/28 14:02.
 */
public class PolicyTintFragment extends BaseDialogFragment<BasePresenter<BaseEngine, IView>> {


    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_agree_btn)
    TextView tvAgreeBtn;
    @BindView(R.id.tv_exit_btn)
    TextView tvExitBtn;

    @Override
    protected float getWidth() {
        return 0.75f;
    }

    @Override
    public int getAnimationId() {
        return 0;
    }

    @Override
    public int getHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_policy_tint;
    }

    @Override
    public void init() {
        setCancelable(false);
        String link = "为了加强对您个人信息的保护,根据最新法律法规要求,更新了隐私政策,请您仔细阅读并确认\"<a href=\"https://answer.bshu.com/v1/treaty\">隐私权相关政策</a>\"(特别是加粗或下划线标注的内容),同时我们使用了友盟SDK来进行应用统计、应用分享，相关的友盟隐私政策如下:\n" +
                "使用SDK名称：友盟SDK\n" +
                "服务类型：友盟统计、分享\n" +
                "收集个人信息类型：设备信息（IMEI/Mac/android ID/IDFA/OPENUDID/GUID/SIM卡IMSI/地理位置信息）\n" +
                "如有疑问，请仔细阅读\"<a href=\"https://www.umeng.com/page/policy\">友盟隐私政策</a>\",\n" +
                "我们严格按照政策内容使用和保存您的个人信息,为您提供更好的服务,感谢您的信任。";

        tvContent.setLinkTextColor(ContextCompat.getColor(requireActivity(), R.color.colorPrimary));
        tvContent.setMovementMethod(LinkMovementMethod.getInstance());

        tvContent.setText(SpanUtils.getStringBuilder(getActivity(), link, getString(R.string.privacy_policy)));

        RxView.clicks(tvAgreeBtn).subscribe(aVoid -> {
//            RxSPTool.putBoolean(requireActivity(), SpConstant.INDEX_DIALOG, true);
            SPUtils.getInstance().put(SpConstant.INDEX_DIALOG, true);
            UMConfigure.init(requireActivity(), Config.UMENG_KEY, "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
            if (onGrantListener != null) {
                onGrantListener.onAgree();
            }
            dismiss();
        });
        RxView.clicks(tvExitBtn).subscribe(aVoid -> {
            dismiss();
            android.os.Process.killProcess(android.os.Process.myPid());
        });
    }

    private OnGrantListener onGrantListener;

    public void setOnGrantListener(OnGrantListener onGrantListener) {
        this.onGrantListener = onGrantListener;
    }

    public interface OnGrantListener {
        void onAgree();
    }
}
