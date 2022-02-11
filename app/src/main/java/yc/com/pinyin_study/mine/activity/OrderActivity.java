package yc.com.pinyin_study.mine.activity;

import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;


import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import yc.com.base.BaseActivity;
import yc.com.pinyin_study.R;
import yc.com.pinyin_study.base.widget.MainToolBar;
import yc.com.pinyin_study.base.widget.StateView;
import yc.com.pinyin_study.category.utils.ItemDecorationHelper;
import yc.com.pinyin_study.mine.adapter.OrderAdapter;
import yc.com.pinyin_study.mine.contract.OrderContract;
import yc.com.pinyin_study.mine.presenter.OrderPresenter;
import yc.com.pinyin_study.pay.alipay.OrderInfo;
import yc.com.rthttplibrary.config.HttpConfig;
import yc.com.rthttplibrary.util.ScreenUtil;

/**
 * Created by wanglin  on 2019/4/25 15:11.
 */
public class OrderActivity extends BaseActivity<OrderPresenter> implements OrderContract.View {

    @BindView(R.id.main_toolbar)
    MainToolBar mainToolbar;
    @BindView(R.id.stateView)
    StateView stateView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.rl_container)
    RelativeLayout rlContainer;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private OrderAdapter orderAdapter;

    private int page = 1;
    private int pageSize = 20;

    @Override
    public boolean isStatusBarMateria() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_order;
    }

    @Override
    public void init() {
        mPresenter = new OrderPresenter(this, this);
        mainToolbar.showNavigationIcon();
        mainToolbar.setTitle(getString(R.string.my_order));
        mainToolbar.init(this);
        mainToolbar.setRightContainerVisible(false);
        getData(false);
        initAdapter();
        initListener();

    }


    private void initListener() {
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.app_selected_color));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                View view = recyclerView.getChildAt(0);

                if (view.getTop() < 0) {

                    recyclerView.setPadding(recyclerView.getPaddingLeft(), 0, recyclerView.getPaddingRight(), 0);

                } else {

                    recyclerView.setPadding(recyclerView.getPaddingLeft(), ScreenUtil.dip2px(OrderActivity.this, 46f), recyclerView.getPaddingRight(), 0);
                }

            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            page = 1;
            getData(true);
        });
        orderAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getData(false);
            }
        }, recyclerView);
    }

    private void getData(boolean isRefresh) {
        mPresenter.getOrderInfoList(page, pageSize, isRefresh);
    }

    private void initAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderAdapter = new OrderAdapter(null);
        recyclerView.setAdapter(orderAdapter);
        recyclerView.addItemDecoration(new ItemDecorationHelper(this, 10));
    }

    @Override
    public void showOrderInfos(List<OrderInfo> orderInfos) {
        if (page == 1) {
            orderAdapter.setNewData(orderInfos);
        } else {
            orderAdapter.addData(orderInfos);
        }
        if (orderInfos.size() == pageSize) {
            page++;
            orderAdapter.loadMoreComplete();
        } else {
            orderAdapter.loadMoreEnd();
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
        stateView.showLoading(rlContainer);
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showNoData() {
        stateView.showNoData(rlContainer);
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(rlContainer, HttpConfig.NET_ERROR, v -> getData(false));
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }


}
