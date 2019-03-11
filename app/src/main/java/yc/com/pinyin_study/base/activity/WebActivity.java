package yc.com.pinyin_study.base.activity;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import butterknife.BindView;
import yc.com.base.BaseActivity;
import yc.com.pinyin_study.R;
import yc.com.pinyin_study.base.widget.CommonWebView;
import yc.com.pinyin_study.base.widget.MainToolBar;

/**
 * Created by wanglin  on 2018/11/8 15:03.
 */
public class WebActivity extends BaseActivity {
    @BindView(R.id.main_toolbar)
    MainToolBar mainToolbar;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.commonWebView)
    CommonWebView commonWebView;

    private String url = "http://en.upkao.com/";

    @Override
    public boolean isStatusBarMateria() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    public void init() {
        mainToolbar.showNavigationIcon();
        mainToolbar.init(this);
        mainToolbar.setRightContainerVisible(false);


        commonWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (progressBar.getVisibility() == View.GONE) {
                    progressBar.setVisibility(View.VISIBLE);
                }

                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                }

            }


        });

        commonWebView.loadUrl(url);

    }


}
