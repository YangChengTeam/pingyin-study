package yc.com.pinyin_study.study.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;

import android.text.TextUtils;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;


import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

import yc.com.blankj.utilcode.util.LogUtils;
import yc.com.blankj.utilcode.util.StringUtils;
import yc.com.blankj.utilcode.util.ToastUtils;
import yc.com.pinyin_study.R;
import yc.com.pinyin_study.study.listener.OnAVManagerListener;
import yc.com.pinyin_study.study.listener.OnUIApplyControllerListener;
import yc.com.rthttplibrary.util.LogUtil;
import yc.com.rthttplibrary.util.ToastUtil;


/**
 * Created by wanglin  on 2018/11/1 15:32.
 * 音视频播放管理类
 */
public class AVManager implements OnAVManagerListener {

    private SharedPreferences mSharedPreferences;
    private SpeechRecognizer mIat;
    private Context mContext;
    private OnUIApplyControllerListener uiApplyControllerListener;
    private KSYMediaPlayer mKsyMediaPlayer;//播放MP3

    /**
     * 引擎类型
     */
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private boolean mTranslateEnable;
    private String audioFilePath;
    private File audioFile;
    private int ret;
    private MediaPlayer mPlayer;//播放录音文件

    public AVManager(Context context, OnUIApplyControllerListener onUIControllerListener, String parent) {
        this.mContext = context;
        this.uiApplyControllerListener = onUIControllerListener;

        mIat = SpeechRecognizer.createRecognizer(context, mInitListener);
        mSharedPreferences = context.getSharedPreferences(IatSettings.PREFER_NAME, Activity.MODE_PRIVATE);

        setParam(parent);

    }


    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = code -> {
        if (code != ErrorCode.SUCCESS) {
//                ToastUtils.showLong("初始化失败，错误码：" + code);
            LogUtil.msg("初始化失败，错误码：" + code);
        }
    };

    /**
     * 参数设置
     *
     * @return
     */
    public void setParam(String parent) {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        this.mTranslateEnable = mSharedPreferences.getBoolean(mContext.getString(R.string.pref_key_translate), false);
        if (mTranslateEnable) {
            LogUtils.e("translate enable");
            mIat.setParameter(SpeechConstant.ASR_SCH, "1");
            mIat.setParameter(SpeechConstant.ADD_CAP, "translate");
            mIat.setParameter(SpeechConstant.TRS_SRC, "its");
        }

        String lag = mSharedPreferences.getString("iat_language_preference",
                "en_us");
        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
            mIat.setParameter(SpeechConstant.ACCENT, null);

            if (mTranslateEnable) {
                mIat.setParameter(SpeechConstant.ORI_LANG, "en");
                mIat.setParameter(SpeechConstant.TRANS_LANG, "cn");
            }
        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);

            if (mTranslateEnable) {
                mIat.setParameter(SpeechConstant.ORI_LANG, "cn");
                mIat.setParameter(SpeechConstant.TRANS_LANG, "en");
            }
        }

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "4000"));

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "0"));

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");


        audioFilePath = Environment.getExternalStorageDirectory() + "/msc/record/" + parent + "/iat.wav";

        audioFile = new File(audioFilePath);

        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, audioFilePath);
    }


    /**
     * 开始播放音乐
     *
     * @param musicUrl
     */

    @Override
    public void playMusic(String musicUrl, boolean isOnce, int playStep) {
        stopMusic();

        if (TextUtils.isEmpty(musicUrl)) return;
        if (null == mKsyMediaPlayer) {
            mKsyMediaPlayer = new KSYMediaPlayer.Builder(mContext).build();
            mKsyMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }

        try {
            mKsyMediaPlayer.setDataSource(musicUrl);
            mKsyMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mKsyMediaPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                uiApplyControllerListener.playBeforeUpdateUI();
                //开始播放动画，如果当前有动画正在播放
                //开始播放
                mp.start();
            }
        });
        mKsyMediaPlayer.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer mp) {
                uiApplyControllerListener.playAfterUpdateUI();
                stopMusic();
            }
        });

        mKsyMediaPlayer.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                ToastUtil.toast(mContext, "播放失败！");
                uiApplyControllerListener.playErrorUpdateUI();
                return false;
            }
        });

    }

    @Override
    public void playMusic(String musicUrl) {
        playMusic(musicUrl, true, 0);
    }


    /**
     * 停止播放录音
     */
    private void stopPlayTape() {
        uiApplyControllerListener.playRecordAfterUpdateUI();
        if (mPlayer != null) {
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
            }
//            mPlayer.release();
            mPlayer.reset();
            mPlayer = null;
        }
    }

    @Override
    public void stopMusic() {
        //停止ItemView缩放动画播放
//        uiApplyControllerListener.playAfterUpdateUI();
        //停止音乐播放
        if (null != mKsyMediaPlayer) {
            if (mKsyMediaPlayer.isPlaying()) {
                mKsyMediaPlayer.stop();
            }
            mKsyMediaPlayer.release();
            mKsyMediaPlayer.reset();
            mKsyMediaPlayer.resetListeners();
            mKsyMediaPlayer = null;
        }
    }


    @Override
    public void playAssetFile(String assetFilePath,boolean isOnce, final int step) {


    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public void destroy() {
        if (mIat != null) {
            mIat.cancel();
            mIat.destroy();
        }
    }


    private String currentWord;
    private boolean isWord;

    @Override
    public void startRecordAndSynthesis(String word, boolean isWord) {
        stopMusic();
        this.currentWord = word;
        this.isWord = isWord;
        uiApplyControllerListener.recordBeforeUpdateUI();
        ret = mIat.startListening(mRecognizerListener);

        if (ret != ErrorCode.SUCCESS) {
//            ToastUtils.showLong("听写失败,错误码：" + ret);
        } else {
            //ToastUtils.showLong("开始");
        }
    }

    @Override
    public void stopRecord() {
        mIat.stopListening();
        uiApplyControllerListener.recordAfterUpdataUI();
    }

    @Override
    public boolean isRecording() {
        return mIat != null && mIat.isListening();
    }


    @Override
    public void playRecordFile() {

        try {
            if (audioFile != null && audioFile.exists()) {

                stopPlayTape();
                if (mPlayer == null)
                    mPlayer = new MediaPlayer();


                //设置要播放的文件
                mPlayer.setDataSource(audioFile.getAbsolutePath());
                mPlayer.prepare();
                uiApplyControllerListener.playRecordBeforeUpdateUI();
                //播放
                mPlayer.start();
                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        uiApplyControllerListener.playRecordAfterUpdateUI();
                    }
                });
                mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        uiApplyControllerListener.playRecordAfterUpdateUI();
                        ToastUtil.toast(mContext, "文件播放失败");
                        return false;

                    }
                });
            } else {
                ToastUtil.toast(mContext, "请先录音，再播放");
            }
        } catch (Exception e) {
            LogUtils.e("prepare() failed");
        }
    }

    /**
     * 用HashMap存储听写结果
     */
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            //ToastUtils.showLong("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            uiApplyControllerListener.recordAfterUpdataUI();

            LogUtil.msg("error-->" + error.getErrorDescription() + "--code--" + error.getErrorCode());
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            if (mTranslateEnable && error.getErrorCode() == 14002) {
                ToastUtils.showLong(error.getPlainDescription(true) + "\n请确认是否已开通翻译功能");
            } else {
//                ToastUtils.showLong("听写识别错误，请重试");
                if (mIat != null) {
                    mIat.stopListening();
                }
            }
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            //ToastUtils.showLong("结束说话");


        }


        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            uiApplyControllerListener.recordAfterUpdataUI();
            printResult(results);

        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            LogUtils.e("音量大小--->" + volume);
//            mTapeImageView.getDrawable().setLevel((int) (3000 + 6000 * volume * 18 / 100));
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    private void printResult(RecognizerResult results) {
        String text = VoiceJsonParser.parseIatResult(results.getResultString());

        if (StringUtils.isEmpty(text)) {
            return;
        }

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }
        String voiceText = resultBuffer.toString();
        if (!StringUtils.isEmpty(voiceText)) {
            LogUtils.e("result text --->" + voiceText);
        }
        compareReResult(currentWord, voiceText);

        if (mIat != null) {
            mIat.stopListening();
        }
    }


    /**
     * 将录入的语音与源语音进行对比
     *
     * @param sourceSen
     * @param speakSen
     * @return
     */
    private void compareReResult(String sourceSen, String speakSen) {

        try {
            if (TextUtils.isEmpty(sourceSen) || TextUtils.isEmpty(speakSen)) {
                return;
            }
            if (isWord) {//单词
                compareWord(sourceSen, speakSen);
                return;
            }
            String regEx = " |、|，|。|；|？|！|,|\\.|;|\\?|!|]|:|：|\"|-";
            Pattern p = Pattern.compile(regEx);

            //按照句子结束符分割句子
            String[] words = p.split(sourceSen);
            List<String> sourceList = new ArrayList<>();
            for (int i = 0; i < words.length; i++) {
                if (!StringUtils.isTrimEmpty(words[i])) {
                    sourceList.add(words[i]);
                }
            }

            List<String> speakList = new ArrayList<>();
            String[] speakWords = p.split(speakSen);
            for (int m = 0; m < speakWords.length; m++) {
                if (!StringUtils.isTrimEmpty(speakWords[m])) {
                    speakList.add(speakWords[m]);
                }
            }

            int matchCount = 0;
            float percent = 0;
            for (String str : sourceList) {
                if (speakList.contains(str)) {
                    matchCount++;
                }
            }

            if (matchCount > 0 && sourceList.size() > 0) {
                percent = (float) matchCount / (float) sourceList.size() * 100;
            }

            LogUtil.msg("result: " + percent);
            //todo 设置听写评测结果
            uiApplyControllerListener.showEvaluateResult(percent + "");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 比较单词
     */
    private void compareWord(String sourceSen, String speakSen) {
        int sourceLength = sourceSen.length();
        int speakLength = speakSen.length();

        int temp;
        if (sourceLength <= speakLength) {
            temp = sourceLength;
        } else {
            temp = speakLength;
        }

        int count = 0;
        for (int i = 0; i < temp; i++) {
            if (sourceSen.charAt(i) == speakSen.charAt(i)) {
                count++;
            }
        }

        double f1 = new BigDecimal((float) count / sourceLength).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        uiApplyControllerListener.showEvaluateResult((f1 * 100) + "");

    }



}
