package yc.com.pinyin_study.error.activity;

import android.view.View;

import com.kk.securityhttp.net.contains.HttpConfig;

import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import butterknife.BindView;
import yc.com.base.BaseActivity;
import yc.com.pinyin_study.R;
import yc.com.pinyin_study.base.widget.CommonToolBar;
import yc.com.pinyin_study.base.widget.CommonWebView;
import yc.com.pinyin_study.base.widget.StateView;
import yc.com.pinyin_study.error.contract.ErrorContract;
import yc.com.pinyin_study.error.model.bean.ErrorInfo;
import yc.com.pinyin_study.error.presenter.ErrorPresenter;
import yc.com.pinyin_study.study.widget.CommonScrollView;

/**
 * Created by wanglin  on 2019/3/7 10:16.
 */
public class ErrorDetailActivity extends BaseActivity<ErrorPresenter> implements ErrorContract.View {


    @BindView(R.id.commonToolbar)
    CommonToolBar commonToolbar;
    @BindView(R.id.commonWebView)
    CommonWebView commonWebView;
    @BindView(R.id.stateView)
    StateView stateView;
    @BindView(R.id.commonScrollView)
    CommonScrollView commonScrollView;

    private String id;

    private String title;

    @Override
    public int getLayoutId() {
        return R.layout.activity_error_detail;
    }

    @Override
    public void init() {


        commonToolbar.showNavigation();
        commonToolbar.init(this);
//        commonToolbar.setRightVisable(true);
        mPresenter = new ErrorPresenter(this, this);

        if (getIntent() != null) {
            id = getIntent().getStringExtra("errorId");
            title = getIntent().getStringExtra("title");
        }
        commonToolbar.setTitle(title);
        mPresenter.getErrorInfo(id);
    }

    @Override
    public boolean isStatusBarMateria() {
        return true;
    }


    @Override
    public void showErrorList(List<ErrorInfo> errorInfos) {
    }

    @Override
    public void showErrorDetail(ErrorInfo data) {
        commonWebView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
        commonWebView.loadDataWithBaseURL(null, data.getContent(), "text/html", "utf-8", null);
    }

    @Override
    public void hide() {
        stateView.hide();
    }

    @Override
    public void showLoading() {
        stateView.showLoading(commonScrollView);
    }

    @Override
    public void showNoData() {
        stateView.showNoData(commonScrollView);

    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(commonScrollView, HttpConfig.NET_ERROR, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getErrorInfo(id);
            }
        });
    }

}
