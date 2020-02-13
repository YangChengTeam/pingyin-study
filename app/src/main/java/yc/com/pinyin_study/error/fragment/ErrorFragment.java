package yc.com.pinyin_study.error.fragment;

import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import yc.com.base.BaseFragment;
import yc.com.blankj.utilcode.util.SPUtils;
import yc.com.pinyin_study.R;
import yc.com.pinyin_study.base.constant.BusAction;
import yc.com.pinyin_study.base.constant.SpConstant;
import yc.com.pinyin_study.base.fragment.ShareFragment;
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
                boolean isShare = SPUtils.getInstance().getBoolean(SpConstant.IS_SHARE, false);
                if (position < 2 || isShare) {
                    Intent intent = new Intent(getActivity(), ErrorDetailActivity.class);
                    intent.putExtra("errorId", errorInfo.getId());
                    intent.putExtra("title", errorInfo.getTitle());
                    MobclickAgent.onEvent(getActivity(), "material_id", "易错点");
                    startActivity(intent);
                } else {
                    ShareFragment shareFragment = new ShareFragment();
                    shareFragment.show(getChildFragmentManager(), "");
                }

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

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.SHARE_SUCCESS)
            }
    )
    public void shareSuccess(String success) {
        if (success.equals("share_success")) {
            errorListAdapter.notifyDataSetChanged();
        }
    }


}
