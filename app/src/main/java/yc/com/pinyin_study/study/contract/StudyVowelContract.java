package yc.com.pinyin_study.study.contract;

import java.util.List;

import yc.com.base.IPresenter;
import yc.com.base.IView;
import yc.com.pinyin_study.study.model.domain.WordInfo;

/**
 * Created by wanglin  on 2018/11/1 09:37.
 */
public interface StudyVowelContract {
    interface View extends IView{
        void showVowelInfoList(List<WordInfo> infoList);

        void shoVowelNewInfos(List<List<WordInfo>> soundmarkInfo);
    }

    interface Presenter extends IPresenter {
    }
}
