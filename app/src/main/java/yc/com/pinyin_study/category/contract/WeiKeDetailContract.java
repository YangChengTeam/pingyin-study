package yc.com.pinyin_study.category.contract;

import yc.com.base.IHide;
import yc.com.base.ILoading;
import yc.com.base.INoData;
import yc.com.base.INoNet;
import yc.com.base.IPresenter;
import yc.com.base.IView;
import yc.com.pinyin_study.category.model.domain.CourseInfo;

/**
 * Created by zhangkai on 2017/8/30.
 */

public interface WeiKeDetailContract {
    interface View extends IView, ILoading, INoData, INoNet ,IHide{

        void showWeikeInfo(CourseInfo courseInfo);
    }

    interface Presenter extends IPresenter {
        void getWeikeCategoryInfo(String id);

    }
}
