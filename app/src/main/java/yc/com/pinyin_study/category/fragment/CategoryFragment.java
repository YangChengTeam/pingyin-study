package yc.com.pinyin_study.category.fragment;

import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;

import com.qq.e.ads.nativ.NativeExpressADView;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;
import yc.com.base.BaseFragment;
import yc.com.blankj.utilcode.util.SPUtils;
import yc.com.pinyin_study.R;
import yc.com.pinyin_study.base.activity.WebActivity;
import yc.com.pinyin_study.base.constant.BusAction;
import yc.com.pinyin_study.base.constant.Config;
import yc.com.pinyin_study.base.constant.SpConstant;
import yc.com.pinyin_study.base.widget.MainToolBar;
import yc.com.pinyin_study.base.widget.StateView;
import yc.com.pinyin_study.category.activity.WeiKeDetailActivity;
import yc.com.pinyin_study.category.adapter.WeiKeInfoItemAdapter;
import yc.com.pinyin_study.category.contract.CategoryMainContract;
import yc.com.pinyin_study.category.model.domain.WeiKeCategory;
import yc.com.pinyin_study.category.presenter.CategoryMainPresenter;
import yc.com.pinyin_study.category.utils.ItemDecorationHelper;
import yc.com.pinyin_study.index.utils.UserInfoHelper;
import yc.com.rthttplibrary.config.HttpConfig;
import yc.com.rthttplibrary.util.ScreenUtil;
import yc.com.toutiao_adv.OnAdvStateListener;
import yc.com.toutiao_adv.TTAdDispatchManager;
import yc.com.toutiao_adv.TTAdType;

/**
 * Created by wanglin  on 2018/10/24 17:21.
 */
public class CategoryFragment extends BaseFragment<CategoryMainPresenter> implements CategoryMainContract.View, OnAdvStateListener, yc.com.tencent_adv.OnAdvStateListener {
    @BindView(R.id.category_recyclerView)
    RecyclerView categoryRecyclerView;
    @BindView(R.id.main_toolbar)
    MainToolBar mainToolbar;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.stateView)
    StateView stateView;
    @BindView(R.id.topContainer)
    FrameLayout topContainer;
    @BindView(R.id.iv_bottombanner_close)
    ImageView ivBottombannerClose;
    @BindView(R.id.rl_ad_container)
    RelativeLayout rlAdContainer;

    private int mPos;

    private WeiKeInfoItemAdapter categoryMainAdapter;
    private int page = 1;

    private int PAGE_SIZE = 20;
    private RecyclerView.ItemDecoration itemDecoration;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_category;
    }

    @Override
    public void init() {

        mPresenter = new CategoryMainPresenter(getActivity(), this);

        mainToolbar.init((BaseActivity) getActivity(), WebActivity.class);
        mainToolbar.setTvRightTitleAndIcon(getString(R.string.diandu), R.mipmap.diandu);
//        if (UserInfoHelper.isCloseAdv() || Build.BRAND.toUpperCase().equals("HUAWEI") || Build.BRAND.toUpperCase().equals("HONOR")) {
//            topContainer.setVisibility(View.GONE);
//        } else {
////                AdvDispatchManager.getManager().init(getActivity(), AdvType.BANNER, topContainer, null, Config.TENCENT_ADV, Config.BANNER_TOP_ADV, this);
        if (SPUtils.getInstance().getBoolean(SpConstant.INDEX_DIALOG))
            TTAdDispatchManager.getManager().init(getActivity(), TTAdType.BANNER, topContainer, Config.TOUTIAO_BANNER2_ID, 0, null, 0, null, 0, this);
//
//
//        }

        getData(false);
        categoryRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        categoryRecyclerView.setHasFixedSize(true);

        categoryMainAdapter = new WeiKeInfoItemAdapter(null);

        categoryRecyclerView.setAdapter(categoryMainAdapter);

        itemDecoration = new ItemDecorationHelper(getActivity(), 6, 6);

        categoryRecyclerView.addItemDecoration(itemDecoration);


        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.app_selected_color));
        swipeRefreshLayout.setOnRefreshListener(() -> {
            page = 1;
            getData(true);
        });

        categoryMainAdapter.setOnLoadMoreListener(() -> getData(false), categoryRecyclerView);


        categoryMainAdapter.setOnItemClickListener((adapter, view, position) -> {
//                Intent intent = new Intent(getActivity(), WeikeUnitActivity.class);
//                intent.putExtra("category_id", categoryMainAdapter.getItem(position).getId());
//                startActivity(intent);
            this.mPos = position;
//            if (UserInfoHelper.isCloseAdv() || Build.BRAND.toUpperCase().equals("HUAWEI") || Build.BRAND.toUpperCase().equals("HONOR")) {
//                gotoWeiKeDetail();
//            } else {
            TTAdDispatchManager.getManager().init(getActivity(), TTAdType.REWARD_VIDEO, null, Config.TOUTIAO_REWARD_ID, 0, "解锁微课视频", 12, UserInfoHelper.getUid(), TTAdConstant.VERTICAL, this);

//            }
//

        });

        categoryRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                View view = recyclerView.getChildAt(0);

                if (view.getTop() < 0) {
                    categoryRecyclerView.setPadding(ScreenUtil.dip2px(getActivity(), 6), 0, 0, 0);
                } else {
                    categoryRecyclerView.setPadding(ScreenUtil.dip2px(getActivity(), 6), ScreenUtil.dip2px(getActivity(), 6), 0, 0);
                }
            }
        });
        RxView.clicks(ivBottombannerClose).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                rlAdContainer.setVisibility(View.GONE);
            }
        });


    }


    @Override
    public void showWeiKeCategoryInfos(List<WeiKeCategory> weiKeCategoryList) {
        if (page == 1) {
            categoryMainAdapter.setNewData(weiKeCategoryList);
        } else {
            categoryMainAdapter.addData(weiKeCategoryList);
        }

        if (weiKeCategoryList.size() == PAGE_SIZE) {
            page++;
            categoryMainAdapter.loadMoreComplete();
        } else {
            categoryMainAdapter.loadMoreEnd();
        }
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void hide() {
        stateView.hide();
    }

    @Override
    public void showLoading() {
        stateView.showLoading(categoryRecyclerView);
    }

    @Override
    public void showNoData() {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        stateView.showNoData(categoryRecyclerView);

    }

    @Override
    public void showNoNet() {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        stateView.showNoNet(categoryRecyclerView, HttpConfig.NET_ERROR, v -> getData(false));
    }

    private void getData(boolean isRefresh) {
//        mPresenter.getCategoryInfos(page, PAGE_SIZE, "0", isRefresh);
        mPresenter.getSpellInfos(page, PAGE_SIZE, isRefresh);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (itemDecoration != null) {
            categoryRecyclerView.removeItemDecoration(itemDecoration);
        }


    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.PAY_SUCCESS)
            }
    )
    public void paySuccess(String info) {
        if (topContainer.getVisibility() == View.VISIBLE) {
            topContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void loadSuccess() {

    }

    @Override
    public void loadFailed() {

    }

    @Override
    public void clickAD() {
        gotoWeiKeDetail();
    }


    private void gotoWeiKeDetail() {
        Intent intent = new Intent(getActivity(), WeiKeDetailActivity.class);
//
        intent.putExtra("pid", categoryMainAdapter.getItem(mPos).getId());
        startActivity(intent);
    }

    @Override
    public void onTTNativeExpressed(List<TTNativeExpressAd> ads) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        TTAdDispatchManager.getManager().onDestroy();
    }

    @Override
    public void onShow() {

    }

    @Override
    public void onDismiss(long delayTime) {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onNativeExpressDismiss(NativeExpressADView view) {

    }

    @Override
    public void onNativeExpressShow(Map<NativeExpressADView, Integer> mDatas) {

    }
}
