package yc.com.pinyin_study.category.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BasePresenter;
import yc.com.pinyin_study.category.contract.CategoryMainContract;
import yc.com.pinyin_study.category.model.domain.WeiKeCategory;
import yc.com.pinyin_study.category.model.domain.WeiKeCategoryWrapper;
import yc.com.pinyin_study.category.model.engine.CategoryMainEngine;

/**
 * Created by wanglin  on 2018/10/25 14:09.
 */
public class CategoryMainPresenter extends BasePresenter<CategoryMainEngine, CategoryMainContract.View> implements CategoryMainContract.Presenter {
    public CategoryMainPresenter(Context context, CategoryMainContract.View view) {
        super(context, view);
        mEngine = new CategoryMainEngine(context);
    }

    @Override
    public void loadData(boolean isForceUI, boolean isLoadingUI) {

    }

    @Override
    public void getCategoryInfos(final int page, int pageSize, String pid, boolean isRefresh) {
        if (page == 1 && !isRefresh)
            mView.showLoading();
        Subscription subscription = mEngine.getCategoryInfos(page, pageSize, pid).subscribe(new Subscriber<ResultInfo<WeiKeCategoryWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (page == 1)
                    mView.hide();
            }

            @Override
            public void onNext(ResultInfo<WeiKeCategoryWrapper> weiKeCategoryWrapperResultInfo) {

                if (weiKeCategoryWrapperResultInfo != null) {
                    if (weiKeCategoryWrapperResultInfo.code == HttpConfig.STATUS_OK && weiKeCategoryWrapperResultInfo.data != null) {
                        mView.hide();
                        List<WeiKeCategory> weiKeCategoryList = weiKeCategoryWrapperResultInfo.data.getList();
                        mView.showWeiKeCategoryInfos(weiKeCategoryList);

                    } else {
                        if (page == 1)
                            mView.showNoData();
                    }
                } else {
                    if (page == 1)
                        mView.showNoNet();
                }
            }
        });

        mSubscriptions.add(subscription);
    }

    public void getSpellInfos(final int page, int page_size, boolean isRefresh) {
        if (page == 1 && !isRefresh)
            mView.showLoading();
        Subscription subscription = mEngine.getSpellInfos(page, page_size).subscribe(new Subscriber<ResultInfo<WeiKeCategoryWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (page == 1)
                    mView.hide();
            }

            @Override
            public void onNext(ResultInfo<WeiKeCategoryWrapper> weiKeCategoryWrapperResultInfo) {
                if (weiKeCategoryWrapperResultInfo != null) {
                    if (weiKeCategoryWrapperResultInfo.code == HttpConfig.STATUS_OK && weiKeCategoryWrapperResultInfo.data != null) {
                        mView.hide();
                        List<WeiKeCategory> weiKeCategoryList = weiKeCategoryWrapperResultInfo.data.getList();
                        mView.showWeiKeCategoryInfos(weiKeCategoryList);

                    } else {
                        if (page == 1)
                            mView.showNoData();
                    }
                } else {
                    if (page == 1)
                        mView.showNoNet();
                }
            }
        });

        mSubscriptions.add(subscription);
    }
}
