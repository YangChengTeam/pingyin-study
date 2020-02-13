package yc.com.pinyin_study.base;

import android.os.Build;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.kk.securityhttp.domain.GoagalInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.share.UMShareImpl;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.game.UMGameAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.UMShareAPI;

import java.util.HashMap;
import java.util.Map;

import androidx.multidex.MultiDexApplication;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import yc.com.blankj.utilcode.util.Utils;
import yc.com.pinyin_study.base.constant.Config;
import yc.com.pinyin_study.index.utils.UserInfoHelper;
import yc.com.toutiao_adv.TTAdManagerHolder;


/**
 * Created by wanglin  on 2018/10/29 08:52.
 */
public class EnglishStudyApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        Utils.init(this);
        Observable.just("").observeOn(Schedulers.io()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                init();
                SpeechUtility.createUtility(EnglishStudyApp.this, SpeechConstant.APPID + "=5bdacd35");
            }
        });
        TTAdManagerHolder.init(this, Config.TOUTIAO_AD_ID);
    }

    private void init() {
        //友盟统计
        UMGameAgent.setDebugMode(false);
        UMGameAgent.init(this);
        UMGameAgent.setPlayerLevel(1);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);

        //初始化友盟SDK
        UMShareAPI.get(this);//初始化sd
        UMConfigure.init(this, Config.UMENG_KEY, "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");//58edcfeb310c93091c000be2 5965ee00734be40b580001a0
//        //开启debug模式，方便定位错误，具体错误检查方式可以查看
//        //http://dev.umeng.com/social/android/quick-integration的报错必看，正式发布，请关闭该模式
//        UMConfigure.setLogEnabled(Constant.DEBUG)

        UMShareImpl.Builder builder = new UMShareImpl.Builder();

        builder.setWeixin("wx6008335a0fe5f400", "c263f08dbc2a7dc641c8c8c9367c26e1")
                .build(this);


        //全局信息初始化
        GoagalInfo.get().init(getApplicationContext());
        HttpConfig.setPublickey("-----BEGIN PUBLIC KEY-----\n" +
                "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEA1zQ4FOFmngBVc05sg7X5\n" +
                "Z/e3GrhG4rRAiGciUCsrd/n4wpQcKNoOeiRahxKT1FVcC6thJ/95OgBN8jaDzKdd\n" +
                "cMUti9gGzBDpGSS8MyuCOBXc6KCOYzL6Q4qnlGW2d09blZSpFUluDBBwB86yvOxk\n" +
                "5oEtnf6WPw2wiWtm7JR1JrE1k+adYfy+Cx9ifJX3wKZ5X3n+CdDXbUCPBD63eMBn\n" +
                "dy1RYOgI1Sc67bQlQGoFtrhXOGrJ8vVoRNHczaGeBOev96/V0AiEY2f5Kw5PAWhw\n" +
                "NrAF94DOLu/4OyTVUg9rDC7M97itzBSTwvJ4X5JA9TyiXL6c/77lThXvX+8m/VLi\n" +
                "mLR7PNq4e0gUCGmHCQcbfkxZVLsa4CDg2oklrT4iHvkK4ZtbNJ2M9q8lt5vgsMkb\n" +
                "bLLqe9IuTJ9O7Pemp5Ezf8++6FOeUXBQTwSHXuxBNBmZAonNZO1jACfOzm83zEE2\n" +
                "+Libcn3EBgxPnOB07bDGuvx9AoSzLjFk/T4ScuvXKEhk1xqApSvtPADrRSskV0aE\n" +
                "G5F8PfBF//krOnUsgqAgujF9unKaxMJXslAJ7kQm5xnDwn2COGd7QEnOkFwqMJxr\n" +
                "DmcluwXXaZXt78mwkSNtgorAhN6fXMiwRFtwywqoC3jYXlKvbh3WpsajsCsbTiCa\n" +
                "SBq4HbSs5+QTQvmgUTPwQikCAwEAAQ==" +
                "-----END PUBLIC KEY-----");
        setHttpDefaultParams();

        UserInfoHelper.getIndexMenuInfo(this);
        UserInfoHelper.getStudyPages(this);
    }

    public void setHttpDefaultParams() {
        //设置http默认参数
        String agent_id = "1";
        Map<String, String> params = new HashMap<>();
        if (GoagalInfo.get().channelInfo != null && GoagalInfo.get().channelInfo.agent_id != null) {
            params.put("from_id", GoagalInfo.get().channelInfo.from_id + "");
            params.put("author", GoagalInfo.get().channelInfo.author + "");
            agent_id = GoagalInfo.get().channelInfo.agent_id;
        }
        params.put("agent_id", agent_id);
        params.put("ts", System.currentTimeMillis() + "");
        params.put("device_type", "2");
        params.put("app_id", "9");
        params.put("imeil", GoagalInfo.get().uuid);
        String sv = android.os.Build.MODEL.contains(android.os.Build.BRAND) ? android.os.Build.MODEL + " " + android
                .os.Build.VERSION.RELEASE : Build.BRAND + " " + android
                .os.Build.MODEL + " " + android.os.Build.VERSION.RELEASE;
        params.put("sys_version", sv);
        if (GoagalInfo.get().packageInfo != null) {
            params.put("app_version", GoagalInfo.get().packageInfo.versionCode + "");
        }
        HttpConfig.setDefaultParams(params);
    }


}
