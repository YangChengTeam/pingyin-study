package yc.com.pinyin_study.error.contract;

import java.util.List;

import yc.com.base.IHide;
import yc.com.base.ILoading;
import yc.com.base.INoData;
import yc.com.base.INoNet;
import yc.com.base.IPresenter;
import yc.com.base.IView;
import yc.com.pinyin_study.error.model.bean.ErrorInfo;

/**
 * Created by wanglin  on 2019/3/7 17:22.
 */
public interface ErrorContract {

    interface View extends IView,ILoading,INoNet,INoData,IHide {
        void showErrorList(List<ErrorInfo> errorInfos);

        void showErrorDetail(ErrorInfo data);
    }

    interface Presenter extends IPresenter {
        void getErrorInfoList();

        void getErrorInfo(String id);
    }
}
