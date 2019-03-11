package yc.com.pinyin_study.study.fragment;

import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.core.Builder;
import com.app.hubert.guide.core.Controller;
import com.app.hubert.guide.listener.OnGuideChangedListener;
import com.app.hubert.guide.listener.OnLayoutInflatedListener;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.HighLight;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.LogUtil;
import com.kk.utils.ScreenUtil;
import com.umeng.analytics.MobclickAgent;
import com.xinqu.videoplayer.XinQuVideoPlayer;
import com.xinqu.videoplayer.XinQuVideoPlayerStandard;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.functions.Action1;
import yc.com.base.BaseFragment;
import yc.com.blankj.utilcode.util.SPUtils;
import yc.com.pinyin_study.R;
import yc.com.pinyin_study.base.constant.SpConstant;
import yc.com.pinyin_study.base.utils.UIUtils;
import yc.com.pinyin_study.base.widget.StateView;
import yc.com.pinyin_study.study.activity.PreviewActivity;
import yc.com.pinyin_study.study.adapter.StudyWordAdapter;
import yc.com.pinyin_study.study.contract.StudyContract;
import yc.com.pinyin_study.study.listener.OnAVManagerListener;
import yc.com.pinyin_study.study.listener.OnUIApplyControllerListener;
import yc.com.pinyin_study.study.listener.OnUIPracticeControllerListener;
import yc.com.pinyin_study.study.model.domain.StudyInfo;
import yc.com.pinyin_study.study.model.domain.StudyInfoWrapper;
import yc.com.pinyin_study.study.model.domain.WordInfo;
import yc.com.pinyin_study.study.presenter.StudyPresenter;
import yc.com.pinyin_study.study.utils.AVManager;
import yc.com.pinyin_study.study.utils.ObserverManager;
import yc.com.pinyin_study.study.utils.PpAudioManager;
import yc.com.pinyin_study.study.widget.CommonScrollView;
import yc.com.pinyin_study.study.widget.MediaPlayerView;

/**
 * Created by wanglin  on 2018/10/26 16:23.
 */
public class StudyMainFragment extends BaseFragment<StudyPresenter> implements StudyContract.View, OnUIPracticeControllerListener, OnUIApplyControllerListener {


    @BindView(R.id.stateView)
    StateView stateView;

    @BindView(R.id.tv_perception_voice)
    TextView tvPerceptionVoice;
    @BindView(R.id.iv_perception_voice)
    ImageView ivPerceptionVoice;
    @BindView(R.id.ll_perception_voice)
    LinearLayout llPerceptionVoice;
    @BindView(R.id.iv_pronounce_icon)
    ImageView ivPronounceIcon;
    @BindView(R.id.rl_pronounce)
    RelativeLayout rlPronounce;
    @BindView(R.id.ll_perception_container)
    LinearLayout llPerceptionContainer;

    @BindView(R.id.videoPlayer)
    XinQuVideoPlayerStandard mJCVideoPlayer;
    @BindView(R.id.ll_study_container)
    LinearLayout llStudyContainer;

    @BindView(R.id.tv_number_progress)
    TextView tvNumberProgress;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tv_practice_soundmark)
    TextView tvPracticeSoundmark;
    @BindView(R.id.iv_practice)
    ImageView ivPractice;

    @BindView(R.id.iv_top_carton)
    ImageView ivTopCarton;
    @BindView(R.id.ll_practice_container)
    LinearLayout llPracticeContainer;
    @BindView(R.id.iv_essentials_example)
    ImageView ivEssentialsExample;
    @BindView(R.id.rl_essentials)
    RelativeLayout rlEssentials;
    @BindView(R.id.tv_essentials_desp)
    TextView tvEssentialsDesp;
    @BindView(R.id.mediaPlayerView)
    MediaPlayerView mediaPlayerView;
    @BindView(R.id.ll_essentials_container)
    LinearLayout llEssentialsContainer;
    @BindView(R.id.apply_recyclerView)
    RecyclerView applyRecyclerView;
    @BindView(R.id.ll_apply_container)
    LinearLayout llApplyContainer;
    @BindView(R.id.nestedScrollView)
    CommonScrollView nestedScrollView;
    @BindView(R.id.iv_pic)
    ImageView ivPic;

    @BindView(R.id.tv_perception_pinyin)
    TextView tvPerceptionPinyin;
    @BindView(R.id.tv_perception_word)
    TextView tvPerceptionWord;

    @BindView(R.id.ll_study_total_container)
    LinearLayout llStudyTotalContainer;
    @BindView(R.id.ll_top_tint)
    LinearLayout llTopTint;


    private int playStep = 1;//播放步骤


    private int pos;

    private OnAVManagerListener mListener;//发音练习管理类
    private OnAVManagerListener avManagerListener;//播放录音管理类
    private int[] firstLayoutIds = new int[]{R.layout.study_perception_guide, R.layout.study_study_guide};
    private StudyWordAdapter studyWordAdapter;


    private WordInfo currentInfo;
    private ImageView playImg;
    private ImageView recordImg;
    private LinearLayout layoutResult;
    private ImageView ivSpeakResult;
    private TextView tvResultHint;
    private ImageView ivRecordPlayback;


    @Override
    public int getLayoutId() {
        return R.layout.study_main_item;
    }


    @Override
    public void init() {
        mPresenter = new StudyPresenter(getActivity(), this);
        mJCVideoPlayer.widthRatio = 16;
        mJCVideoPlayer.heightRatio = 9;

    }


    private void initView(List<WordInfo> wordInfos) {

        applyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        studyWordAdapter = new StudyWordAdapter(wordInfos);
        applyRecyclerView.setAdapter(studyWordAdapter);
        applyRecyclerView.setNestedScrollingEnabled(false);

    }


    @Override
    public void showStudyPages(Integer data) {

    }

    private StudyInfo mStudyInfo;
    private boolean isFirst;

    @Override
    public void showStudyInfo(StudyInfoWrapper data) {
        //显示引导页
        boolean isShowFirst = SPUtils.getInstance().getBoolean(SpConstant.IS_SHOW_FIRST, true);
        if (isShowFirst) {
            views.clear();
            views.add(llPerceptionContainer);
            views.add(llStudyContainer);
//            startGuide(views, firstLayoutIds);
            isFirst = true;
            SPUtils.getInstance().put(SpConstant.IS_SHOW_FIRST, false);
        }


        StudyInfo studyInfo = data.getInfo();
        mStudyInfo = studyInfo;
        tvPerceptionVoice.setText(studyInfo.getName());
        Glide.with(getActivity()).asBitmap().load(studyInfo.getImage()).into(ivPronounceIcon);
        Glide.with(getActivity()).asGif().load(R.mipmap.study_essentials_guide).into(ivEssentialsExample);
        Glide.with(getActivity()).asBitmap().load(studyInfo.getImg()).into(ivPic);
        tvEssentialsDesp.setText(studyInfo.getDesp());
        mediaPlayerView.setPath(studyInfo.getDesp_audio());
        tvPerceptionPinyin.setText(studyInfo.getName());
        tvPerceptionWord.setText(studyInfo.getHanzi());
        initView(data.getWords());
        playVideo(studyInfo);
        initListener(studyInfo);
    }

    private View currentView;

    private void initListener(final StudyInfo studyInfo) {
        RxView.clicks(llPerceptionVoice).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                currentView = ivPerceptionVoice;
                mListener.playMusic(studyInfo.getVoice());
            }
        });


        //1.播放引导音 2.播放发音 3.播放di声音4.录音并播放5.播放第二段引导音6
        RxView.clicks(ivPractice).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

                if (mListener.isPlaying()) {
                    playPracticeAfterUpdateUI();
                } else {
                    //todo 播放并录音
                    playStep = 1;
                    mListener.playAssetFile("guide_01.mp3", playStep);
                    if (mStudyInfo != null) {
                        tvPracticeSoundmark.setVisibility(View.VISIBLE);
                        tvPracticeSoundmark.setText(mStudyInfo.getName());
                    }
                }

            }
        });

        RxView.clicks(rlPronounce).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), PreviewActivity.class);
                if (mStudyInfo != null)
                    intent.putExtra("img", mStudyInfo.getImage());
                startActivity(intent);
            }
        });

//        RxView.clicks(rlEssentials).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
//            @Override
//            public void call(Void aVoid) {
//                Intent intent = new Intent(getActivity(), PreviewActivity.class);
//                if (mStudyInfo != null) {
//                    intent.putExtra("img", mStudyInfo.getMouth_cover());
//                }
//                startActivity(intent);
//
//            }
//        });

        studyWordAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                playImg = (ImageView) adapter.getViewByPosition(applyRecyclerView, position, R.id.iv_play);
                currentView = playImg;
                layoutResult = (LinearLayout) adapter.getViewByPosition(applyRecyclerView, position, R.id.layout_result);

                studyWordAdapter.startAnimation(position);
                studyWordAdapter.showActionContainer(position);

                resetState();
                view.setSelected(true);

                currentInfo = studyWordAdapter.getItem(position);
                startPlay();
            }
        });

        studyWordAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public boolean onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                layoutResult = (LinearLayout) adapter.getViewByPosition(applyRecyclerView, position, R.id.layout_result);
                currentInfo = studyWordAdapter.getItem(position);
                switch (view.getId()) {
                    case R.id.ll_play:
                        // TODO: 2018/11/1 播放
                        playImg = (ImageView) adapter.getViewByPosition(applyRecyclerView, position, R.id.iv_play);
                        currentView = playImg;
                        startPlay();
                        break;
                    case R.id.ll_record:
                        if (currentInfo == null) return false;
                        recordImg = (ImageView) adapter.getViewByPosition(applyRecyclerView, position, R.id.iv_record);

                        ivSpeakResult = (ImageView) adapter.getViewByPosition(applyRecyclerView, position, R.id.iv_speak_result);
                        tvResultHint = (TextView) adapter.getViewByPosition(applyRecyclerView, position, R.id.tv_result_hint);
                        if (avManagerListener.isRecording()) {
                            avManagerListener.stopRecord();
                        } else {
                            avManagerListener.startRecordAndSynthesis(currentInfo.getWord().replaceAll("#", ""), true);
                        }
                        break;
                    case R.id.ll_record_playback:
                        ivRecordPlayback = (ImageView) adapter.getViewByPosition(applyRecyclerView, position, R.id.iv_record_playback);
                        avManagerListener.playRecordFile();
                        break;
                }

                return false;
            }
        });


        nestedScrollView.setOnScrollListener(new CommonScrollView.onScrollListener() {
            @Override
            public void onScroll(int l, int scrollY, int oldl, int oldScrollY) {

                if (!isFirst) {
                    int[] position = new int[2];
                    llTopTint.getLocationOnScreen(position);
                    int measuredHeight = llTopTint.getMeasuredHeight();
                    int toolBarHeight = UIUtils.getInstance(getActivity()).getLocation()[1];

                    int scrollHeight = position[1] + measuredHeight - toolBarHeight;

                    if (scrollHeight <= 0) {
                        ObserverManager.getInstance().notifyMyObservers("消失了");
                    } else {
                        ObserverManager.getInstance().notifyMyObservers("出现了");
                    }
                }
//                LogUtil.msg("scrollY==" + scrollY + "   oldScrollY==" + oldScrollY + "--position="
//                        + scrollHeight
//                        + "--height=" + measuredHeight + "--pos=" + position[1]);


            }
        });

        mediaPlayerView.setOnMediaClickListener(new MediaPlayerView.onMediaClickListener() {
            @Override
            public void onMediaClick() {
                MobclickAgent.onEvent(getActivity(), "pronunciation_key", "发音要领");
            }
        });

    }

    private void startPlay() {
        if (currentInfo == null) return;
        avManagerListener.playMusic(currentInfo.getMp3());
    }

    private void resetState() {
        int count = applyRecyclerView.getChildCount();
        for (int i = 0; i < count; i++) {
            applyRecyclerView.getChildAt(i).setSelected(false);
        }
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    private List<View> views = new ArrayList<>();

    @Override
    public void fetchData() {

        mPresenter.getStudyDetail(pos);
        mListener = new PpAudioManager(getActivity(), this);
        avManagerListener = new AVManager(getActivity(), this, pos + "/pinyin");

    }

    //设置引导视图
    private void showGuide(final List<View> viewList, int[] layoutIds, final int scrollHeight) {

        Builder builder = getBuilder("guide1", 1, scrollHeight);

        if (viewList != null && viewList.size() > 0) {

            for (int i = 0; i < viewList.size(); i++) {

                builder.addGuidePage(GuidePage.newInstance()
                        .addHighLight(viewList.get(i), HighLight.Shape.RECTANGLE, 16)
                        .setEverywhereCancelable(false)

                        .setLayoutRes(layoutIds[i], R.id.iv_next)

                        .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                            @Override
                            public void onLayoutInflated(View view, final Controller controller) {


                            }


                        }));

            }

            builder.show();
        }
    }


    private boolean isPracticeShow = true;
    private boolean isApplyShow = true;

    private Builder getBuilder(String label, final int step, final int scrollHeight) {

        return NewbieGuide.with(StudyMainFragment.this)
                .setLabel(label)
                .alwaysShow(true)
                .setOnGuideChangedListener(new OnGuideChangedListener() {
                    @Override
                    public void onShowed(Controller controller) {
                    }

                    @Override
                    public void onRemoved(Controller controller) {

                        if (step == 1 && isPracticeShow) {
                            isPracticeShow = false;
                            nestedScrollView.scrollTo(0, scrollHeight);
                            int[] location = new int[2];
                            llPracticeContainer.getLocationOnScreen(location);
                            RectF rect = new RectF();
                            rect.set(location[0] - ScreenUtil.dip2px(getActivity(), 7), location[1] - ScreenUtil.dip2px(getActivity(), 10), location[0] + llPerceptionContainer.getRight() - ScreenUtil.dip2px(getActivity(), 10), llPerceptionContainer.getBottom() + location[1] + ScreenUtil.dip2px(getActivity(), 60));

                            int[] location1 = new int[2];
                            llEssentialsContainer.getLocationOnScreen(location1);
                            RectF rectF = new RectF();
                            rectF.set(location1[0] - ScreenUtil.dip2px(getActivity(), 7), location1[1] - ScreenUtil.dip2px(getActivity(), 10),
                                    location1[0] + llEssentialsContainer.getRight() - ScreenUtil.dip2px(getActivity(), 10),
                                    llEssentialsContainer.getBottom() + location1[1] - ScreenUtil.dip2px(getActivity(), 40));

                            practiceGuide(rect, rectF);
                        } else if (step == 2 && isApplyShow) {
                            isApplyShow = false;
                            nestedScrollView.scrollBy(0, ScreenUtil.getHeight(getActivity()) - UIUtils.getInstance(getActivity()).getBottomBarHeight());
                            int[] location = new int[2];
                            llApplyContainer.getLocationOnScreen(location);

                            RectF rectF = new RectF(location[0] - ScreenUtil.dip2px(getActivity(), 7), location[1] - ScreenUtil.dip2px(getActivity(), 10), location[0] + llApplyContainer.getRight() - ScreenUtil.dip2px(getActivity(), 10), llApplyContainer.getBottom() + location[1] + ScreenUtil.dip2px(getActivity(), 10));
                            applyGuide(rectF);

                        } else if (step == 3) {
                            //
                            nestedScrollView.scrollBy(0, -ScreenUtil.getHeight(getActivity()));
                        }
                    }
                });
    }


    private void practiceGuide(final RectF rect, final RectF rectF) {
        llPracticeContainer.post(new Runnable() {
            @Override
            public void run() {
                Builder builder = getBuilder("guide2", 2, 0);
                builder.addGuidePage(GuidePage.newInstance()
                        .addHighLight(rect, HighLight.Shape.RECTANGLE, 16)
                        .setEverywhereCancelable(false)
                        .setLayoutRes(R.layout.study_practice_guide, R.id.iv_next))
                        .addGuidePage(GuidePage.newInstance()
                                .addHighLight(rectF, HighLight.Shape.RECTANGLE, 16)
                                .setEverywhereCancelable(false)
                                .setLayoutRes(R.layout.study_essentials_guide, R.id.iv_next));


                builder.show();
            }

        });
    }

    private void applyGuide(final RectF rect) {
        llPracticeContainer.post(new Runnable() {
            @Override
            public void run() {
                Builder builder = getBuilder("guide3", 3, 0);
                builder.addGuidePage(GuidePage.newInstance()
                        .addHighLight(rect, HighLight.Shape.RECTANGLE, 16)
                        .setEverywhereCancelable(false)
                        .setLayoutRes(R.layout.study_apply_guide, R.id.iv_next));


                builder.show();
            }

        });
    }


    private void startGuide(final List<View> views, final int[] layoutIds) {

        getActivity().getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                int[] location = new int[2];
                llStudyContainer.getLocationOnScreen(location);

                UIUtils instance = UIUtils.getInstance(getActivity());
                final int[] topLocation = instance.getLocation();
                location[1] = location[1] + llStudyTotalContainer.getBottom() - llStudyTotalContainer.getTop() - topLocation[1] - ScreenUtil.dip2px(getActivity(), 100);

                showGuide(views, layoutIds, location[1]);

            }
        }, 1000);
    }


    /**
     * 播放视频
     *
     * @param studyInfo
     */
    private void playVideo(StudyInfo studyInfo) {
        Glide.with(this).load(studyInfo.getCover()).thumbnail(0.1f).into(mJCVideoPlayer.thumbImageView);

        mJCVideoPlayer.setUp(studyInfo.getVideo(), XinQuVideoPlayer.SCREEN_WINDOW_LIST, false, null == studyInfo.getName() ? "" : studyInfo.getName());
    }


    @Override
    public void onPause() {
        super.onPause();
        XinQuVideoPlayer.goOnPlayOnPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayerView.destroy();
        avManagerListener.destroy();
    }


    @Override
    public void playBeforeUpdateUI() {
        if (currentView == ivPerceptionVoice) {
            Glide.with(getActivity()).asGif().load(R.mipmap.small_trumpet_stop).into(ivPerceptionVoice);
        } else if (currentView == playImg) {
            studyWordAdapter.resetDrawable();
            layoutResult.setVisibility(View.GONE);
            playImg.setImageResource(R.mipmap.study_essentials_pause);
        }


    }

    @Override
    public void playAfterUpdateUI() {
        Glide.with(this).clear(ivPerceptionVoice);
        ivPerceptionVoice.setImageResource(R.mipmap.small_trumpet);
        if (playImg != null)
            playImg.setImageResource(R.mipmap.study_essentials_play);
    }


    @Override
    public void playPracticeBeforeUpdateUI(int progress) {
        ivPractice.setImageResource(R.mipmap.study_practice_pause);
        tvNumberProgress.setText(String.format(getString(R.string.practice_progress), progress));
    }

    @Override
    public void playPracticeFirstUpdateUI() {

        playStep = 2;
        if (mStudyInfo == null) return;
        mListener.playMusic(mStudyInfo.getVoice(), false, playStep);

    }

    @Override
    public void playPracticeSecondUpdateUI() {

        playStep = 3;
        mListener.playAssetFile("user_tape_tips.mp3", playStep);
    }

    @Override
    public void recordUpdateUI() {
        ivTopCarton.setVisibility(View.VISIBLE);
    }

    @Override
    public void playPracticeAfterUpdateUI() {
        ivPractice.setImageResource(R.mipmap.study_practice_play);
        if (isAdded())
            tvNumberProgress.setText(String.format(getString(R.string.practice_progress), 0));
        ivTopCarton.setVisibility(View.GONE);
        mListener.stopMusic();
        tvPracticeSoundmark.setVisibility(View.GONE);

    }

    @Override
    public void updateProgressBar(int percent) {
        progressBar.setProgress(percent);
        if (percent != 100) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void playPracticeThirdUpdateUI() {
        playStep = 1;
        mListener.playAssetFile("guide_02.mp3", playStep);
        ivTopCarton.setVisibility(View.GONE);
    }


    @Override
    public void hide() {
        stateView.hide();
    }

    @Override
    public void showLoading() {
        stateView.showLoading(nestedScrollView);
    }

    @Override
    public void showNoData() {
        stateView.showNoData(nestedScrollView);

    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(nestedScrollView, HttpConfig.NET_ERROR, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getStudyDetail(pos);
            }
        });
    }

    @Override
    public void recordBeforeUpdateUI() {
        layoutResult.setVisibility(View.GONE);
        Glide.with(getActivity()).asGif().load(R.mipmap.study_essentials_record_stop).into(recordImg);
    }

    @Override
    public void recordAfterUpdataUI() {
        Glide.with(this).clear(recordImg);
        recordImg.setImageResource(R.mipmap.study_essentials_record_start);
    }

    @Override
    public void playRecordBeforeUpdateUI() {
        ivRecordPlayback.setImageResource(R.mipmap.study_essentials_playback_stop);
    }

    @Override
    public void playRecordAfterUpdateUI() {
        ivRecordPlayback.setImageResource(R.mipmap.study_essentials_playback_start);
    }

    @Override
    public void showEvaluateResult(String percent) {
        int result = (int) Float.parseFloat(percent);
        layoutResult.setVisibility(View.VISIBLE);
        if (result >= 60) {
            ivSpeakResult.setImageResource(R.mipmap.read_item_result_yes);
            tvResultHint.setText(String.format(getString(R.string.evaluating_good_result), result + ""));
        } else {
            ivSpeakResult.setImageResource(R.mipmap.listen_result_no);
            tvResultHint.setText("加油");
        }
    }

    @Override
    public void playErrorUpdateUI() {
        studyWordAdapter.resetDrawable();
    }

}

