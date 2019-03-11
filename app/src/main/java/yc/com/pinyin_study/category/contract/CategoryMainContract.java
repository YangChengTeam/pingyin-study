package yc.com.pinyin_study.category.contract;

import java.util.List;

import yc.com.base.IHide;
import yc.com.base.ILoading;
import yc.com.base.INoData;
import yc.com.base.INoNet;
import yc.com.base.IPresenter;
import yc.com.base.IView;
import yc.com.pinyin_study.category.model.domain.WeiKeCategory;

/**
 * Created by wanglin  on 2018/10/25 14:11.
 */
public interface CategoryMainContract {

    interface View extends IView, ILoading, INoNet, INoData, IHide {
        void showWeiKeCategoryInfos(List<WeiKeCategory> weiKeCategoryList);
    }

    interface Presenter extends IPresenter {

        void getCategoryInfos(int page, int pageSize, String pid, boolean isRefresh);
    }
}
