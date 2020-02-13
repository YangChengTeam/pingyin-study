package yc.com.pinyin_study.base.fragment;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.RxBus;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.ScreenUtil;
import com.kk.utils.ToastUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;
import yc.com.base.BaseDialogFragment;
import yc.com.blankj.utilcode.util.SPUtils;
import yc.com.pinyin_study.R;
import yc.com.pinyin_study.base.adapter.BasePayAdapter;
import yc.com.pinyin_study.base.constant.BusAction;
import yc.com.pinyin_study.base.constant.SpConstant;
import yc.com.pinyin_study.base.contract.BasePayContract;
import yc.com.pinyin_study.base.model.domain.GoodInfo;
import yc.com.pinyin_study.base.presenter.BasePayPresenter;
import yc.com.pinyin_study.base.utils.VipInfoHelper;
import yc.com.pinyin_study.category.utils.ItemDecorationHelper;
import yc.com.pinyin_study.index.utils.UserInfoHelper;
import yc.com.pinyin_study.pay.PayWayInfo;
import yc.com.pinyin_study.pay.PayWayInfoHelper;
import yc.com.pinyin_study.pay.alipay.IAliPay1Impl;
import yc.com.pinyin_study.pay.alipay.IPayCallback;
import yc.com.pinyin_study.pay.alipay.IWXPay1Impl;
import yc.com.pinyin_study.pay.alipay.OrderInfo;

/**
 * Created by wanglin  on 2018/10/29 17:18.
 */
public class BasePayFragment extends BaseDialogFragment<BasePayPresenter> implements BasePayContract.View {
    @BindView(R.id.payway_recyclerView)
    RecyclerView paywayRecyclerView;
    @BindView(R.id.iv_payway_ali)
    ImageView ivPaywayAli;
    @BindView(R.id.ll_ali)
    LinearLayout llAli;
    @BindView(R.id.iv_payway_wx)
    ImageView ivPaywayWx;
    @BindView(R.id.ll_wx)
    LinearLayout llWx;
    @BindView(R.id.iv_pay_btn)
    ImageView ivPayBtn;
    @BindView(R.id.iv_close)
    ImageView ivClose;


    private IAliPay1Impl aliPay;
    private IWXPay1Impl wxPay;
    private GoodInfo currentGoodInfo;

    private String payway;
    private int currentPos = 0;
    private BasePhoneFragment basePhoneFragment;
    private ImageView preImageView;
    private BasePayAdapter basePayAdapter;

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
        return ScreenUtil.getHeight(getActivity()) * 19 / 20;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_base_pay;
    }

    @Override
    public void init() {
        aliPay = new IAliPay1Impl(getActivity());
        wxPay = new IWXPay1Impl(getActivity());
        mPresenter = new BasePayPresenter(getActivity(), this);

        List<GoodInfo> vipInfoList = VipInfoHelper.getVipInfoList();
        currentGoodInfo = getGoodInfo(vipInfoList);
//        mPresenter.isBindPhone();


        paywayRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        basePayAdapter = new BasePayAdapter(vipInfoList);
        paywayRecyclerView.setAdapter(basePayAdapter);
        paywayRecyclerView.setHasFixedSize(true);
        paywayRecyclerView.addItemDecoration(new ItemDecorationHelper(getActivity(), 15));


        basePayAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                clickItem(position);

            }
        });

        basePayAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public boolean onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                clickItem(position);
                return false;
            }
        });

        ivPaywayAli.setSelected(true);
        initListener();

//        String phone = SPUtils.getInstance().getString(SpConstant.PHONE);
//        if (!TextUtils.isEmpty(phone))
//            isBind = true;
        isBind = !TextUtils.isEmpty(SPUtils.getInstance().getString(SpConstant.PHONE));
    }


    private void clickItem(int position) {
        ImageView mIvSelect = basePayAdapter.getIv(position);
        boolean isBuy = (boolean) mIvSelect.getTag();
        if (isBuy) {
            return;
        }

        if (preImageView == null)
            preImageView = basePayAdapter.getIv(getPosition());

        if (preImageView != mIvSelect && !((boolean) preImageView.getTag())) {
            preImageView.setImageResource(R.mipmap.vip_info_unselect);
        }
        mIvSelect.setImageResource(R.mipmap.vip_info_selected);
        preImageView = mIvSelect;
        currentGoodInfo = basePayAdapter.getItem(position);
    }

    private void initListener() {
        RxView.clicks(llAli).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                resetPaywayState();
                if (!ivPaywayAli.isSelected()) {
                    ivPaywayAli.setSelected(true);
                    currentPos = 0;
                }
            }
        });

        RxView.clicks(llWx).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                resetPaywayState();
                if (!ivPaywayWx.isSelected()) {
                    ivPaywayWx.setSelected(true);
                    currentPos = 1;
                }
            }
        });
        RxView.clicks(ivPayBtn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                // todo  支付
                if (UserInfoHelper.isSuperVip()) {//已购买所有项目
                    createRewardDialog();
                    return;
                }

                payway = getPaywayName(currentPos);
                if (currentGoodInfo != null) {
                    mPresenter.createOrder(1, payway, currentGoodInfo.getReal_price(), currentGoodInfo.getId(), currentGoodInfo.getTitle());
                } else {
                    ToastUtil.toast2(getActivity(), "支付错误");
                }

            }
        });

        RxView.clicks(ivClose).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                dismiss();
            }
        });

    }

    private void resetPaywayState() {
        ivPaywayAli.setSelected(false);
        ivPaywayWx.setSelected(false);
    }


    private void aliPay(OrderInfo orderInfo) {
        aliPay.pay(orderInfo, iPayCallback);
    }

    private void wxPay(OrderInfo orderInfo) {
        wxPay.pay(orderInfo, iPayCallback);
    }

    private IPayCallback iPayCallback = new IPayCallback() {
        @Override
        public void onSuccess(OrderInfo orderInfo) {
            //保存vip
            UserInfoHelper.saveVip(orderInfo.getGoodId());
            //支付弹窗消失
            dismissAllowingStateLoss();

            if (!isBind) {
                if (basePhoneFragment == null)
                    basePhoneFragment = new BasePhoneFragment();
                if (!basePhoneFragment.isVisible()) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.add(basePhoneFragment, null);
                    ft.commitAllowingStateLoss();
                }
            }
            RxBus.get().post(BusAction.PAY_SUCCESS, "pay_success");

        }

        @Override
        public void onFailure(OrderInfo orderInfo) {

        }
    };


    private String getPaywayName(int position) {
        List<PayWayInfo> payWayInfoList = PayWayInfoHelper.getPayWayInfoList();
        if (payWayInfoList != null && payWayInfoList.size() > 0 && position < payWayInfoList.size()) {
            return payWayInfoList.get(position).getPay_way_name();
        }
        return "";
    }

    @Override
    public void showOrderInfo(OrderInfo orderInfo) {

        if (TextUtils.equals(payway, "alipay"))
            aliPay(orderInfo);
        else {
            wxPay(orderInfo);
        }
    }

    private boolean isBind;

    @Override
    public void showBindSuccess() {
//        isBind = true;
    }

    @Override
    public void showVipInfoList(List<GoodInfo> goodInfoList) {
        currentGoodInfo = getGoodInfo(goodInfoList);
        basePayAdapter.setNewData(goodInfoList);
    }

    @Override
    public void showLoadingDialog(String mess) {
        ((BaseActivity) getActivity()).showLoadingDialog(mess);
    }

    @Override
    public void dismissDialog() {
        ((BaseActivity) getActivity()).dismissDialog();
    }


    private void createRewardDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("提示");
        builder.setMessage("你已经购买了所有项目");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        builder.show();
    }

    private GoodInfo getGoodInfo(List<GoodInfo> goodInfoList) {
        GoodInfo goodInfo = null;
        if (goodInfoList != null && goodInfoList.size() > 0) {
            goodInfo = goodInfoList.get(getPosition());
            if (UserInfoHelper.isPhonogramVip()) {
                goodInfo = null;
            }

        }
        return goodInfo;
    }


    private int getPosition() {
        int pos = 0;
//        if (UserInfoHelper.isPhonogramVip()) {
//            pos = 1;
//        }
//        if (UserInfoHelper.isPhonicsVip() && UserInfoHelper.isPhonogramVip() || UserInfoHelper.isPhonogramOrPhonicsVip()) {
//            pos = 3;
//        }

        return pos;
    }

}
