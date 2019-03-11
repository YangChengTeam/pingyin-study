package yc.com.pinyin_study.index.contract;

import yc.com.base.IPresenter;
import yc.com.base.IView;
import yc.com.pinyin_study.index.model.domain.IndexInfoWrapper;

/**
 * Created by wanglin  on 2018/10/29 09:10.
 */
public interface IndexContract {

    interface View extends IView {
        void showIndexInfo(IndexInfoWrapper indexInfoWrapper);
    }

    interface Presenter extends IPresenter {
        void getIndexInfo();
    }
}
