package yc.com.pinyin_study.mine.activity;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.RxBus;
import com.jakewharton.rxbinding.view.RxView;


import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;
import yc.com.pinyin_study.R;
import yc.com.pinyin_study.base.constant.BusAction;
import yc.com.pinyin_study.base.contract.BasePayContract;
import yc.com.pinyin_study.base.fragment.BasePhoneFragment;
import yc.com.pinyin_study.base.model.domain.GoodInfo;
import yc.com.pinyin_study.base.presenter.BasePayPresenter;
import yc.com.pinyin_study.base.utils.VipInfoHelper;
import yc.com.pinyin_study.category.utils.ItemDecorationHelper;
import yc.com.pinyin_study.index.utils.UserInfoHelper;
import yc.com.pinyin_study.mine.adapter.PayAdapter;
import yc.com.pinyin_study.pay.PayWayInfo;
import yc.com.pinyin_study.pay.PayWayInfoHelper;
import yc.com.pinyin_study.pay.alipay.IAliPay1Impl;
import yc.com.pinyin_study.pay.alipay.IPayCallback;
import yc.com.pinyin_study.pay.alipay.IWXPay1Impl;
import yc.com.pinyin_study.pay.alipay.OrderInfo;
import yc.com.rthttplibrary.util.ToastUtil;

/**
 * Created by wanglin  on 2019/4/23 15:42.
 */
public class PayActivity extends BaseActivity<BasePayPresenter> implements BasePayContract.View {

    @BindView(R.id.iv_left_icon)
    ImageView ivLeftIcon;
    @BindView(R.id.tv_tb_title)
    TextView tvTbTitle;
    @BindView(R.id.toolbar_sub)
    Toolbar toolbarSub;
    @BindView(R.id.payInfo_recyclerView)
    RecyclerView payInfoRecyclerView;
    @BindView(R.id.iv_ali)
    ImageView ivAli;
    @BindView(R.id.iv_ali_select)
    ImageView ivAliSelect;
    @BindView(R.id.rl_ali)
    RelativeLayout rlAli;
    @BindView(R.id.iv_wx)
    ImageView ivWx;
    @BindView(R.id.iv_wx_select)
    ImageView ivWxSelect;
    @BindView(R.id.rl_wx)
    RelativeLayout rlWx;
    @BindView(R.id.iv_pay_btn)
    ImageView ivPayBtn;
    private PayAdapter payAdapter;
    private ImageView preImageView;
    private GoodInfo currentGoodInfo;
    private int currentPos = 0;
    private String payway;

    private IAliPay1Impl aliPay;
    private IWXPay1Impl wxPay;
    private boolean isBind;
    private BasePhoneFragment basePhoneFragment;

    private List<GoodInfo> vipInfoList;

    @Override
    public boolean isStatusBarMateria() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_pay;
    }

    @Override
    public void init() {

        mPresenter = new BasePayPresenter(this, this);
        vipInfoList = VipInfoHelper.getVipInfoList();
        payInfoRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        payAdapter = new PayAdapter(vipInfoList);
        payInfoRecyclerView.setAdapter(payAdapter);
        payInfoRecyclerView.setHasFixedSize(true);
        payInfoRecyclerView.addItemDecoration(new ItemDecorationHelper(this, 10));

        aliPay = new IAliPay1Impl(this);
        wxPay = new IWXPay1Impl(this);

        ivAliSelect.setSelected(true);
        initListener();
    }

    private void initListener() {
        RxView.clicks(ivLeftIcon).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });
        payAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                clickItem(position);

            }
        });

        payAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                clickItem(position);

            }
        });


        RxView.clicks(rlAli).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                resetPaywayState();
                if (!ivAliSelect.isSelected()) {
                    ivAliSelect.setSelected(true);
                    currentPos = 0;
                }
            }
        });

        RxView.clicks(rlWx).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                resetPaywayState();
                if (!ivWxSelect.isSelected()) {
                    ivWxSelect.setSelected(true);
                    currentPos = 1;
                }
            }
        });

        RxView.clicks(ivPayBtn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                // todo  支付
                if (UserInfoHelper.isPhonogramOrPhonicsVip()) {//已购买所有项目
                    createRewardDialog();
                    return;
                }

                payway = getPaywayName(currentPos);
                if (currentGoodInfo != null) {
                    mPresenter.createOrder(1, payway, currentGoodInfo.getReal_price(), currentGoodInfo.getId(), currentGoodInfo.getTitle());

                } else {
                    ToastUtil.toast(PayActivity.this, "支付错误");
                }
            }
        });
    }

    private String getPaywayName(int position) {
        List<PayWayInfo> payWayInfoList = PayWayInfoHelper.getPayWayInfoList();
        if (payWayInfoList != null && payWayInfoList.size() > 0 && position < payWayInfoList.size()) {
            return payWayInfoList.get(position).getPay_way_name();
        }
        return "";
    }

    private void createRewardDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    private void resetPaywayState() {
        ivAliSelect.setSelected(false);
        ivWxSelect.setSelected(false);
    }

    private void clickItem(int position) {
        ImageView mIvSelect = payAdapter.getIv(position);
        boolean isBuy = (boolean) mIvSelect.getTag();
        if (isBuy) {
            return;
        }

        if (preImageView == null)
            preImageView = payAdapter.getIv(getPosition());

        if (preImageView != mIvSelect && !((boolean) preImageView.getTag())) {
            preImageView.setImageResource(R.mipmap.vip_info_unselect);
        }
        mIvSelect.setImageResource(R.mipmap.vip_info_selected);
        preImageView = mIvSelect;
        currentGoodInfo = payAdapter.getItem(position);
    }

    private int getPosition() {
        int pos = 0;
        if (UserInfoHelper.isPhonogramVip()) {
            pos = 1;
        }
        if (UserInfoHelper.isPhonicsVip() && UserInfoHelper.isPhonogramVip() || UserInfoHelper.isPhonogramOrPhonicsVip()) {
            if (vipInfoList != null) {
                pos = vipInfoList.size() - 1;
            }
        }

        return pos;
    }

    @Override
    public void showOrderInfo(OrderInfo orderInfo) {
        if (TextUtils.equals(payway, "alipay"))
            aliPay(orderInfo);
        else {
            wxPay(orderInfo);
        }
    }

    @Override
    public void showBindSuccess() {
        isBind = true;
    }

    @Override
    public void showVipInfoList(List<GoodInfo> vip_list) {
        vipInfoList = vip_list;
        currentGoodInfo = getGoodInfo(vip_list);
        payAdapter.setNewData(vip_list);
    }

    private GoodInfo getGoodInfo(List<GoodInfo> goodInfoList) {
        GoodInfo goodInfo = null;
        if (goodInfoList != null && goodInfoList.size() > 0) {
            goodInfo = goodInfoList.get(getPosition());
            if (UserInfoHelper.isPhonogramOrPhonicsVip()) {
                goodInfo = null;
            }

        }
        return goodInfo;
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


            if (!isBind) {
                if (basePhoneFragment == null)
                    basePhoneFragment = new BasePhoneFragment();
                if (!basePhoneFragment.isVisible()) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
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


}
