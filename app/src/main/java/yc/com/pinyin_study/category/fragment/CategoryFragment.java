package yc.com.pinyin_study.category.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.ScreenUtil;
import com.qq.e.ads.nativ.NativeExpressADView;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import yc.com.base.BaseActivity;
import yc.com.base.BaseFragment;
import yc.com.pinyin_study.R;
import yc.com.pinyin_study.base.activity.WebActivity;
import yc.com.pinyin_study.base.constant.BusAction;
import yc.com.pinyin_study.base.constant.Config;
import yc.com.pinyin_study.base.widget.MainToolBar;
import yc.com.pinyin_study.base.widget.StateView;
import yc.com.pinyin_study.category.activity.WeiKeDetailActivity;
import yc.com.pinyin_study.category.activity.WeikeUnitActivity;
import yc.com.pinyin_study.category.adapter.CategoryMainAdapter;
import yc.com.pinyin_study.category.adapter.WeiKeInfoItemAdapter;
import yc.com.pinyin_study.category.contract.CategoryMainContract;
import yc.com.pinyin_study.category.model.domain.WeiKeCategory;
import yc.com.pinyin_study.category.presenter.CategoryMainPresenter;
import yc.com.pinyin_study.category.utils.ItemDecorationHelper;
import yc.com.pinyin_study.index.utils.UserInfoHelper;
import yc.com.tencent_adv.AdvDispatchManager;
import yc.com.tencent_adv.AdvType;
import yc.com.tencent_adv.OnAdvStateListener;

/**
 * Created by wanglin  on 2018/10/24 17:21.
 */
public class CategoryFragment extends BaseFragment<CategoryMainPresenter> implements CategoryMainContract.View, OnAdvStateListener {
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
        if (UserInfoHelper.isCloseAdv()) {
            topContainer.setVisibility(View.GONE);
        } else {
            AdvDispatchManager.getManager().init(getActivity(), AdvType.BANNER, topContainer, null, Config.TENCENT_ADV, Config.BANNER_TOP_ADV, this);
        }
        getData(false);
        categoryRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        categoryRecyclerView.setHasFixedSize(true);

        categoryMainAdapter = new WeiKeInfoItemAdapter(null);

        categoryRecyclerView.setAdapter(categoryMainAdapter);

        itemDecoration = new ItemDecorationHelper(getActivity(), 6, 6);

        categoryRecyclerView.addItemDecoration(itemDecoration);


        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.app_selected_color));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getData(true);
            }
        });

        categoryMainAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getData(false);
            }
        }, categoryRecyclerView);


        categoryMainAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                Intent intent = new Intent(getActivity(), WeikeUnitActivity.class);
//                intent.putExtra("category_id", categoryMainAdapter.getItem(position).getId());
//                startActivity(intent);
                Intent intent = new Intent(getActivity(), WeiKeDetailActivity.class);

                intent.putExtra("pid", categoryMainAdapter.getItem(position).getId());
                startActivity(intent);

            }
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
        stateView.showNoNet(categoryRecyclerView, HttpConfig.NET_ERROR, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(false);
            }
        });
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
}
