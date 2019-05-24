package yc.com.pinyin_study.study.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.LogUtil;
import com.kk.utils.ToastUtil;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.xinqu.videoplayer.XinQuVideoPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;
import yc.com.base.BaseFragment;
import yc.com.blankj.utilcode.util.SPUtils;
import yc.com.pinyin_study.R;
import yc.com.pinyin_study.base.activity.WebActivity;
import yc.com.pinyin_study.base.constant.BusAction;
import yc.com.pinyin_study.base.constant.Config;
import yc.com.pinyin_study.base.constant.SpConstant;
import yc.com.pinyin_study.base.fragment.BasePayFragment;
import yc.com.pinyin_study.base.utils.UIUtils;
import yc.com.pinyin_study.base.widget.MainToolBar;
import yc.com.pinyin_study.base.widget.StateView;
import yc.com.pinyin_study.index.utils.UserInfoHelper;
import yc.com.pinyin_study.study.adapter.StudyMainAdapter;
import yc.com.pinyin_study.study.adapter.StudyVowelAdapter;
import yc.com.pinyin_study.study.contract.StudyContract;
import yc.com.pinyin_study.study.model.domain.StudyInfoWrapper;
import yc.com.pinyin_study.study.presenter.StudyPresenter;
import yc.com.pinyin_study.study.utils.AVMediaManager;
import yc.com.pinyin_study.study.utils.ObserverManager;
import yc.com.tencent_adv.AdvDispatchManager;
import yc.com.tencent_adv.AdvType;
import yc.com.tencent_adv.OnAdvStateListener;

/**
 * Created by wanglin  on 2018/10/24 17:21.
 */
public class StudyFragment extends BaseFragment<StudyPresenter> implements StudyContract.View, Observer, OnAdvStateListener {

    @BindView(R.id.study_viewPager)
    ViewPager studyViewPager;
    @BindView(R.id.iv_show_vowel)
    ImageView ivShowVowel;
    @BindView(R.id.main_toolbar)
    MainToolBar mainToolbar;
    @BindView(R.id.iv_next)
    ImageView ivNext;
    @BindView(R.id.iv_pre)
    ImageView ivPre;

    @BindView(R.id.stateView)
    StateView stateView;
    @BindView(R.id.container)
    LinearLayout container;

    @BindView(R.id.bottomContainer)
    FrameLayout bottomContainer;


    private List<Fragment> fragments = new ArrayList<>();
    private int currentPos = 0;
    private int totalPages;//总页码


    private ObserverManager observerManager;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_study;
    }

    @Override
    public void init() {
        mPresenter = new StudyPresenter(getActivity(), this);

        totalPages = SPUtils.getInstance().getInt(SpConstant.STUDY_PAGES, 0);
        if (totalPages == 0) {
            mPresenter.getStudyPages();
        } else {
            initViewpager(totalPages);
        }

        initListener();
        mainToolbar.init(((BaseActivity) getActivity()), WebActivity.class);
        mainToolbar.setTvRightTitleAndIcon(getString(R.string.diandu), R.mipmap.diandu);
        mainToolbar.setBackGround(R.color.transparant);

        UIUtils.getInstance(getActivity()).measureViewLoction(mainToolbar);
        mainToolbar.setTitle("");
        observerManager = ObserverManager.getInstance();
        observerManager.addMyObserver(this);


        if (UserInfoHelper.isCloseAdv()) {
            bottomContainer.setVisibility(View.GONE);
        } else {
            AdvDispatchManager.getManager().init(getActivity(), AdvType.BANNER, bottomContainer, null, Config.TENCENT_ADV, Config.BANNER_BOTTOM_ADV, this);
        }

    }


    private void initListener() {
        RxView.clicks(ivShowVowel).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                StudyVowelFragment studyVowelFragment = new StudyVowelFragment();
                studyVowelFragment.setOnClickListener(new StudyVowelFragment.onClickListener() {
                    @Override
                    public void onClick(int pos) {
                        if (pos < totalPages) {
                            studyViewPager.setCurrentItem(pos);
                            currentPos = pos;
                        }

                        if (pos == 0) {
                            ivPre.setImageResource(R.mipmap.study_pre_normal);
                        } else if (pos == totalPages - 1) {
                            ivNext.setImageResource(R.mipmap.study_next_normal_);
                        }
                    }
                });

                studyVowelFragment.show(getFragmentManager(), "");
            }
        });

        RxView.clicks(ivNext).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //todo 下一页

                currentPos++;
                if (currentPos < totalPages) {
                    if (isCanNext(currentPos)) {
                        next(currentPos);
                    } else {
                        currentPos--;
                        showPayDialog();
                    }
                } else {
                    currentPos--;
                    ToastUtil.toast2(getActivity(), "已经是最后一页了");
                }


            }
        });
        RxView.clicks(ivPre).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

                // TODO: 2018/11/2 上一页
                if (currentPos > 0) {
                    currentPos--;
                    pre(currentPos);
                } else {
                    ivPre.setImageResource(R.mipmap.study_pre_normal);
                    ToastUtil.toast2(getActivity(), "已经是第一页了");
                }
            }
        });


    }


    private void next(int pos) {//下一页

        studyViewPager.setCurrentItem(pos);

        ivPre.setImageResource(R.mipmap.study_pre_selected);
        if (pos == totalPages - 1) {
            ivNext.setImageResource(R.mipmap.study_next_normal_);
        }

    }

    private void pre(int pos) {//上一页

        studyViewPager.setCurrentItem(pos);
        ivNext.setImageResource(R.mipmap.study_next_selected);
        if (pos == 0) {
            ivPre.setImageResource(R.mipmap.study_pre_normal);
        }
    }


    @Override
    public void showStudyPages(Integer data) {
        totalPages = data;
        initViewpager(data);
    }

    private void initViewpager(Integer data) {
        for (int i = 0; i < data; i++) {
            StudyMainFragment studyMainFragment = new StudyMainFragment();
            studyMainFragment.setPos(i);
            fragments.add(studyMainFragment);
        }

        StudyMainAdapter mainAdapter = new StudyMainAdapter(getChildFragmentManager(), fragments);
        studyViewPager.setAdapter(mainAdapter);
//        studyViewPager.setOffscreenPageLimit(2);
        studyViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                XinQuVideoPlayer.releaseAllVideos();
                AVMediaManager.getInstance().releaseAudioManager();

//                LogUtil.msg("currentPos: scroll-->" + currentPos + "--position-->" + position);
                if (isCanNext(position)) {
                    if (currentPos > position) {
                        pre(position);
                    } else {
                        next(position);
                    }
                    currentPos = position;
                } else {
                    studyViewPager.setCurrentItem(currentPos);
                    showPayDialog();
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void showStudyInfo(StudyInfoWrapper data) {

    }


    private boolean isCanNext(int pos) {
        boolean isNext = false;
        if (UserInfoHelper.isPhonogramVip() || pos < 7) {
            isNext = true;
        }
        return isNext;

    }

    private void showPayDialog() {
        BasePayFragment basePayFragment = new BasePayFragment();
        basePayFragment.show(getFragmentManager(), "");
    }

    @Override
    public void hide() {
        stateView.hide();

    }

    @Override
    public void showLoading() {
        stateView.showLoading(container);

    }

    @Override
    public void showNoData() {
        stateView.showNoData(container);
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(container, HttpConfig.NET_ERROR, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getStudyPages();
            }
        });

    }

//    @Override
//    public void fetchData() {
//        super.fetchData();
////        mPresenter.getStudyPages();
//        LogUtil.msg("tag  fetchData");
//    }


    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof String) {
            String mess = ((String) arg);
            if (TextUtils.equals(mess, "消失了")) {
                mainToolbar.setTitle(getString(R.string.app_name));
            } else if (TextUtils.equals(mess, "出现了")) {
                mainToolbar.setTitle("");
            }
//            LogUtil.msg("mess:  " + mess);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        observerManager.removeObserver(this);
    }

    @Override
    public void onShow() {

    }

    @Override
    public void onDismiss(long delayTime) {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onNativeExpressDismiss(NativeExpressADView view) {

    }

    @Override
    public void onNativeExpressShow(Map<NativeExpressADView, Integer> mDatas) {

    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.PAY_SUCCESS)
            }
    )
    public void paySuccess(String info) {
        if (bottomContainer.getVisibility() == View.VISIBLE) {
            bottomContainer.setVisibility(View.GONE);
        }
    }
}
