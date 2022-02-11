package yc.com.pinyin_study.category.presenter;

import android.content.Context;

import yc.com.base.BasePresenter;
import yc.com.pinyin_study.base.observer.BaseCommonObserver;
import yc.com.pinyin_study.category.contract.WeiKeDetailContract;
import yc.com.pinyin_study.category.model.domain.CourseInfo;
import yc.com.pinyin_study.category.model.engine.WeiKeDetailEngine;

/**
 * Created by zhangkai on 2017/8/30.
 */

public class WeiKeDetailPresenter extends BasePresenter<WeiKeDetailEngine, WeiKeDetailContract.View> implements WeiKeDetailContract.Presenter {

    private final String WEIKE_INFO = "weike_info";
    private final String SPOKEN_INFO = "spoken_info";

    public WeiKeDetailPresenter(Context context, WeiKeDetailContract.View iView) {
        super(context, iView);
        mEngine = new WeiKeDetailEngine(context);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }

    @Override
    public void getWeikeCategoryInfo(String id) {
        mView.showLoading();

        mEngine.getWeikeCategoryInfo(id, 1, 20).subscribe(new BaseCommonObserver<CourseInfo>(mContext) {
            @Override
            public void onSuccess(CourseInfo courseInfoResultInfo, String message) {
                if (courseInfoResultInfo != null) {
                    mView.hide();
                    mView.showWeikeInfo(courseInfoResultInfo);
                } else {
                    mView.showNoData();
                }


            }

            @Override
            public void onFailure(int code, String errorMsg) {

            }


            @Override
            public void onRequestComplete() {

            }
        });

    }


}
