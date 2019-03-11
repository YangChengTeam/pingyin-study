package yc.com.pinyin_study.category.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kk.utils.ScreenUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import yc.com.base.BaseActivity;
import yc.com.pinyin_study.R;
import yc.com.pinyin_study.base.widget.MainToolBar;
import yc.com.pinyin_study.base.widget.StateView;
import yc.com.pinyin_study.category.adapter.WeiKeInfoItemAdapter;
import yc.com.pinyin_study.category.contract.CategoryMainContract;
import yc.com.pinyin_study.category.model.domain.WeiKeCategory;
import yc.com.pinyin_study.category.presenter.CategoryMainPresenter;
import yc.com.pinyin_study.category.utils.ItemDecorationHelper;

/**
 * 微课单元列表
 */

public class WeikeUnitActivity extends BaseActivity<CategoryMainPresenter> implements CategoryMainContract.View {


    @BindView(R.id.main_toolbar)
    MainToolBar mainToolbar;
    @BindView(R.id.category_recyclerView)
    RecyclerView categoryRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.stateView)
    StateView stateView;

    private WeiKeInfoItemAdapter mWeiKeInfoItemAdapter;

    private String type = "";

    private String pid = "";

    private int page = 1;

    private int pageSize = 20;

    @Override
    public void init() {

        mainToolbar.showNavigationIcon();
        mainToolbar.init(this);
        mPresenter = new CategoryMainPresenter(this, this);
        final Intent intent = getIntent();
        if (intent != null) {
            pid = intent.getStringExtra("category_id");
        }

        mainToolbar.showNavigationIcon();
        mainToolbar.setTitle("微课学习");
        mainToolbar.setRightContainerVisible(false);

        categoryRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mWeiKeInfoItemAdapter = new WeiKeInfoItemAdapter(null, type);
        categoryRecyclerView.setAdapter(mWeiKeInfoItemAdapter);
        categoryRecyclerView.addItemDecoration(new ItemDecorationHelper(this, 6, 6));
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.app_selected_color));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(true);
            }
        });


        mWeiKeInfoItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(WeikeUnitActivity.this, WeiKeDetailActivity.class);

                intent.putExtra("pid", mWeiKeInfoItemAdapter.getItem(position).getId());
                startActivity(intent);
            }
        });

        mWeiKeInfoItemAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getData(false);
            }
        }, categoryRecyclerView);

        categoryRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                View view = categoryRecyclerView.getChildAt(0);
                if (view.getTop() < 0) {
                    categoryRecyclerView.setPadding(ScreenUtil.dip2px(WeikeUnitActivity.this, 6), 0, 0, 0);
                } else {
                    categoryRecyclerView.setPadding(ScreenUtil.dip2px(WeikeUnitActivity.this, 6), ScreenUtil.dip2px(WeikeUnitActivity.this, 6), 0, 0);
                }
            }
        });

        getData(false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_category;
    }


    @Override
    public void showNoNet() {

        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        stateView.showNoNet(categoryRecyclerView, "网络不给力", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(false);
            }
        });
    }

    @Override
    public void showNoData() {
        stateView.showNoData(categoryRecyclerView);
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showLoading() {
        stateView.showLoading(categoryRecyclerView);
    }


    private void getData(boolean isRefresh) {
        mPresenter.getCategoryInfos(page, pageSize, pid, isRefresh);
    }

    @Override
    public void showWeiKeCategoryInfos(List<WeiKeCategory> weiKeCategoryList) {
        if (page == 1) {

            mWeiKeInfoItemAdapter.setNewData(weiKeCategoryList);
        } else {
            mWeiKeInfoItemAdapter.addData(weiKeCategoryList);
        }
        if (pageSize == weiKeCategoryList.size()) {
            page++;
            mWeiKeInfoItemAdapter.loadMoreComplete();
        } else {
            mWeiKeInfoItemAdapter.loadMoreEnd();
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
    public boolean isStatusBarMateria() {
        return true;
    }

}
