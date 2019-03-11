package yc.com.pinyin_study.error.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import yc.com.base.BaseFragment;
import yc.com.pinyin_study.R;
import yc.com.pinyin_study.base.widget.MainToolBar;
import yc.com.pinyin_study.base.widget.StateView;
import yc.com.pinyin_study.category.utils.ItemDecorationHelper;
import yc.com.pinyin_study.error.activity.ErrorDetailActivity;
import yc.com.pinyin_study.error.adapter.ErrorListAdapter;
import yc.com.pinyin_study.error.contract.ErrorContract;
import yc.com.pinyin_study.error.model.bean.ErrorInfo;
import yc.com.pinyin_study.error.presenter.ErrorPresenter;

/**
 * Created by wanglin  on 2019/3/6 16:41.
 */
public class ErrorFragment extends BaseFragment<ErrorPresenter> implements ErrorContract.View {

    @BindView(R.id.main_toolbar)
    MainToolBar mainToolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.stateView)
    StateView stateView;


    private ErrorListAdapter errorListAdapter;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_error;
    }

    @Override
    public void init() {
        mainToolbar.setTitle(getString(R.string.main_error));
        mainToolbar.setRightContainerVisible(false);
        mainToolbar.setLeftIconVisable(false);
        mainToolbar.setBackGround(R.color.transparant);

        mPresenter = new ErrorPresenter(getActivity(), this);
        mPresenter.getErrorInfoList();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        errorListAdapter = new ErrorListAdapter(null);
        recyclerView.setAdapter(errorListAdapter);
        recyclerView.addItemDecoration(new ItemDecorationHelper(getActivity(), 5));

        initListener();
    }

    private void initListener() {
        errorListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ErrorInfo errorInfo = errorListAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), ErrorDetailActivity.class);
                intent.putExtra("errorId", errorInfo.getId());
                intent.putExtra("title", errorInfo.getTitle());
                MobclickAgent.onEvent(getActivity(), "material_id", "易错点");
                startActivity(intent);

            }
        });
    }


    @Override
    public void showErrorList(List<ErrorInfo> errorInfos) {
        errorListAdapter.setNewData(errorInfos);
    }

    @Override
    public void showErrorDetail(ErrorInfo data) {

    }


    @Override
    public void hide() {
        stateView.hide();
    }

    @Override
    public void showLoading() {
        stateView.showLoading(recyclerView);
    }

    @Override
    public void showNoData() {
        stateView.showNoData(recyclerView);
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(recyclerView, HttpConfig.NET_ERROR, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getErrorInfoList();
            }
        });
    }


}
