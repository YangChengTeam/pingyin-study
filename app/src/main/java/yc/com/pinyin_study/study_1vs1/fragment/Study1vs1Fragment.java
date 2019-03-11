package yc.com.pinyin_study.study_1vs1.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.alibaba.fastjson.JSON;
import com.hwangjr.rxbus.RxBus;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;
import yc.com.base.BaseFragment;
import yc.com.blankj.utilcode.util.SPUtils;
import yc.com.blankj.utilcode.util.ToastUtils;
import yc.com.blankj.utilcode.util.UIUitls;
import yc.com.pinyin_study.R;
import yc.com.pinyin_study.base.activity.WebActivity;
import yc.com.pinyin_study.base.constant.Config;
import yc.com.pinyin_study.base.constant.SpConstant;
import yc.com.pinyin_study.base.widget.CommonWebView;
import yc.com.pinyin_study.base.widget.MainToolBar;
import yc.com.pinyin_study.index.utils.UserInfoHelper;
import yc.com.pinyin_study.pay.alipay.IAliPay1Impl;
import yc.com.pinyin_study.pay.alipay.IPayCallback;
import yc.com.pinyin_study.pay.alipay.IWXPay1Impl;
import yc.com.pinyin_study.pay.alipay.LoadingDialog;
import yc.com.pinyin_study.pay.alipay.OrderInfo;
import yc.com.pinyin_study.study.utils.EngineUtils;
import yc.com.pinyin_study.study_1vs1.model.bean.SlideInfo;

/**
 * Created by wanglin  on 2018/10/24 17:21.
 */
public class Study1vs1Fragment extends BaseFragment {

    @BindView(R.id.main_toolbar)
    MainToolBar mainToolbar;
    @BindView(R.id.webView)
    CommonWebView webView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private String url = "https://vip.hfjy.com/zhuanzhu-m?adid=5b897ad136d8af77";
    //http://m.upkao.com/xiaomayb.html

    private Handler mHandler;
    private IAliPay1Impl iAliPay;
    private IWXPay1Impl iwxPay;
    private LoadingDialog loadingDialog;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_study1vs1;
    }

    @Override
    public void init() {

        SlideInfo slideInfo = JSON.parseObject(SPUtils.getInstance().getString(SpConstant.INDEX_MENU_STATICS), SlideInfo.class);
        if (null != slideInfo) {
            url = slideInfo.getUrl();
        }
        mHandler = new Handler();
        mainToolbar.init((BaseActivity) getActivity(), WebActivity.class);
        mainToolbar.setTvRightTitleAndIcon(getString(R.string.diandu), R.mipmap.diandu);
        iAliPay = new IAliPay1Impl(getActivity());
        iwxPay = new IWXPay1Impl(getActivity());
        loadingDialog = new LoadingDialog(getActivity());

        webView.addJavascriptInterface(new JavascriptInterface(), "study");
        webView.loadUrl(url);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, final int newProgress) {
                super.onProgressChanged(view, newProgress);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (progressBar.getVisibility() == View.GONE) {
                            progressBar.setVisibility(View.VISIBLE);
                        }
                        progressBar.setProgress(newProgress);
                        webView.postInvalidate();
                        if (newProgress == 100) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });


    }


    public class JavascriptInterface {

        @android.webkit.JavascriptInterface
        public void startQQChat(String qq) {
            try {
                String url3521 = "mqqwpa://im/chat?chat_type=wpa&uin=" + qq;
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url3521)));
            } catch (Exception e) {
                ToastUtils.showShort("你的手机还未安装qq,请先安装");
            }

        }

        @android.webkit.JavascriptInterface
        public void pay(final String title, final String money, String paywayname, String id) {


            if (TextUtils.isEmpty(paywayname)) {
                paywayname = "alipay";
            }


            showLoading();
            final String finalPaywayname = paywayname;
            EngineUtils.createOrder(getActivity(), 1, paywayname, money, id)
                    .subscribe(new Action1<ResultInfo<OrderInfo>>() {
                                   @Override
                                   public void call(ResultInfo<OrderInfo> orderInfoResultInfo) {
                                       dismissLoading();
                                       if (orderInfoResultInfo != null) {
                                           if (orderInfoResultInfo.code == HttpConfig.STATUS_OK && orderInfoResultInfo.data != null) {
                                               OrderInfo orderInfo = orderInfoResultInfo.data;
                                               orderInfo.setMoney(Float.parseFloat(money));
                                               orderInfo.setName(title);
                                               if (finalPaywayname.equals("alipay")) {
                                                   iAliPay.pay(orderInfo, payCallBack);
                                               } else {
                                                   iwxPay.pay(orderInfo, payCallBack);
                                               }
                                           } else {
                                               ToastUtil.toast2(getActivity(), orderInfoResultInfo.message);
                                           }
                                       }

                                   }
                               }
                    );


        }

        private IPayCallback payCallBack = new IPayCallback() {

            @Override
            public void onSuccess(OrderInfo orderInfo) {
                UserInfoHelper.saveVip(Config.SUPER_VIP + "");
                dismissPayDialog();

            }

            @Override
            public void onFailure(OrderInfo orderInfo) {
                dismissPayDialog();
            }
        };

        private void showLoading() {
            if (loadingDialog != null) {
                UIUitls.post(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.show("创建订单中，请稍候...");

                    }
                });
            }

        }

        private void dismissLoading() {
            UIUitls.post(new Runnable() {
                @Override
                public void run() {
                    loadingDialog.dismiss();
                }
            });
        }

        private void dismissPayDialog() {

            UIUitls.post(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("javascript:hidePay()");
                }
            });
        }


    }


}
