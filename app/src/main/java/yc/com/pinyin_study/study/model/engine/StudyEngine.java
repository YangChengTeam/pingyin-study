package yc.com.pinyin_study.study.model.engine;

import android.content.Context;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import yc.com.pinyin_study.base.model.engine.BaseEngine;
import yc.com.pinyin_study.study.model.domain.StudyInfoWrapper;
import yc.com.pinyin_study.study.model.domain.StudyPages;
import yc.com.rthttplibrary.bean.ResultInfo;

/**
 * Created by wanglin  on 2018/10/30 16:36.
 */
public class StudyEngine extends BaseEngine {

    public StudyEngine(Context context) {
        super(context);
    }

    public Observable<ResultInfo<StudyInfoWrapper>> getStudyDetail(int page) {

        return request.getStudyDetail(page).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }


    public Observable<ResultInfo<StudyPages>> getStudyPages() {

        return request.getStudyPages().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
