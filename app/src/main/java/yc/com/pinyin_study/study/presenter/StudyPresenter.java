package yc.com.pinyin_study.study.presenter;

import android.content.Context;

import yc.com.base.BasePresenter;
import yc.com.pinyin_study.base.observer.BaseCommonObserver;
import yc.com.pinyin_study.study.contract.StudyContract;
import yc.com.pinyin_study.study.model.domain.StudyInfoWrapper;
import yc.com.pinyin_study.study.model.domain.StudyPages;
import yc.com.pinyin_study.study.model.engine.StudyEngine;

/**
 * Created by wanglin  on 2018/10/30 16:40.
 */
public class StudyPresenter extends BasePresenter<StudyEngine, StudyContract.View> implements StudyContract.Presenter {
    public StudyPresenter(Context context, StudyContract.View view) {
        super(context, view);
        mEngine = new StudyEngine(context);
    }

    @Override
    public void loadData(boolean isForceUI, boolean isLoadingUI) {
        if (!isForceUI) return;


    }

    @Override
    public void getStudyPages() {

        mEngine.getStudyPages().subscribe(new BaseCommonObserver<StudyPages>(mContext) {
            @Override
            public void onSuccess(StudyPages data, String message) {
                if (data != null) {
                    mView.hide();
                    mView.showStudyPages(data.count);
                } else {
                    mView.showNoData();
                }

            }

            @Override
            public void onFailure(int code, String errorMsg) {
                mView.showNoNet();
            }



            @Override
            public void onRequestComplete() {

            }
        });


    }

    @Override
    public void getStudyDetail(int page) {
        mView.showLoading();
        mEngine.getStudyDetail(page).subscribe(new BaseCommonObserver<StudyInfoWrapper>(mContext) {
            @Override
            public void onSuccess(StudyInfoWrapper studyInfoWrapperResultInfo, String message) {
                if (studyInfoWrapperResultInfo != null) {
                    mView.hide();
                    mView.showStudyInfo(studyInfoWrapperResultInfo);
                } else {
                    mView.showNoData();
                }


            }

            @Override
            public void onFailure(int code, String errorMsg) {
                mView.showNoNet();
            }


            @Override
            public void onRequestComplete() {

            }
        });


    }
}
