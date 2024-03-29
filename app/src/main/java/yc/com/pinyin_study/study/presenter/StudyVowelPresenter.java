package yc.com.pinyin_study.study.presenter;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import yc.com.base.BasePresenter;
import yc.com.pinyin_study.base.observer.BaseCommonObserver;
import yc.com.pinyin_study.study.contract.StudyVowelContract;
import yc.com.pinyin_study.study.model.domain.VowelInfoWrapper;
import yc.com.pinyin_study.study.model.domain.WordInfo;
import yc.com.pinyin_study.study.model.engine.StudyVowelEngine;
import yc.com.pinyin_study.study.utils.SoundmarkHelper;

/**
 * Created by wanglin  on 2018/11/1 09:36.
 */
public class StudyVowelPresenter extends BasePresenter<StudyVowelEngine, StudyVowelContract.View> implements StudyVowelContract.Presenter {


    public StudyVowelPresenter(Context context, StudyVowelContract.View view) {
        super(context, view);
        mEngine = new StudyVowelEngine(context);
    }

    @Override
    public void loadData(boolean isForceUI, boolean isLoadingUI) {
        if (!isForceUI) return;
        getVowelInfos();


    }

    private void getVowelInfos() {
//        List<WordInfo> wordInfos = SoundmarkHelper.getWordInfos();
//
//        if (wordInfos != null) {
//            combinationData(wordInfos, new ArrayList<List<WordInfo>>());
//        }

        mEngine.getVowelInfos().subscribe(new BaseCommonObserver<VowelInfoWrapper>(mContext) {
            @Override
            public void onSuccess(VowelInfoWrapper data, String message) {
                if (data != null) {
                    List<WordInfo> infoList = data.getList();
//                    mView.showVowelInfoList(infoList);
                    SoundmarkHelper.setWordInfos(infoList);
                    List<List<WordInfo>> listList = new ArrayList<>();
                    combinationData(infoList, listList);


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


    private void combinationData(List<WordInfo> infoList, List<List<WordInfo>> listList) {
        if (infoList == null || infoList.size() == 0) return;

        if (listList == null) {
            listList = new ArrayList<>();
        }
        int temp = 0;
        for (int i = 0; i < infoList.size(); ) {
            WordInfo wordInfo = infoList.get(i);
//            LogUtil.msg("i-->" + i + "--size==" + infoList.size() + "---" + wordInfo.getType_text());
            List<WordInfo> wordInfos = new ArrayList<>();
            wordInfos.add(wordInfo);
            for (int j = i + 1; j < infoList.size(); j++) {


                WordInfo wordInfo1 = infoList.get(j);
                if (TextUtils.equals(wordInfo.getType_text(), wordInfo1.getType_text())) {
                    wordInfos.add(wordInfo1);
                    if (j == infoList.size() - 1) {
                        temp = j;
                        listList.add(wordInfos);
                        break;
                    }
                } else {
                    temp = j;
                    listList.add(wordInfos);
                    break;
                }


            }
            i = temp;
            if (i == infoList.size() - 1) {
                break;
            }
        }
        mView.shoVowelNewInfos(listList);


    }


}
