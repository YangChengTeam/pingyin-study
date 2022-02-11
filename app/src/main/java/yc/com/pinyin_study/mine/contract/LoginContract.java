package yc.com.pinyin_study.mine.contract;

import yc.com.base.IDialog;
import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by suns  on 2020/4/16 15:19.
 */
public interface LoginContract {
    interface View extends IView, IDialog {
        void showDisplayCode();
    }

    interface Presenter extends IPresenter {
    }
}
