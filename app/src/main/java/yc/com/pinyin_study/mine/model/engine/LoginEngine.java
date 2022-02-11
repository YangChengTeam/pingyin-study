package yc.com.pinyin_study.mine.model.engine;

import android.content.Context;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import yc.com.pinyin_study.base.model.engine.BaseEngine;
import yc.com.pinyin_study.index.model.domain.UserInfo;
import yc.com.pinyin_study.index.model.domain.UserInfoWrapper;
import yc.com.pinyin_study.index.utils.UserInfoHelper;
import yc.com.rthttplibrary.bean.ResultInfo;

/**
 * Created by suns  on 2020/4/16 15:11.
 */
public class LoginEngine extends BaseEngine {
    public LoginEngine(Context context) {
        super(context);
    }

    public Observable<ResultInfo<UserInfoWrapper>> login(String username, String password) {

//        Map<String, String> params = new HashMap<>();
//        params.put("username", username);
//        params.put("pwd", password);
//        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.PHONE_LOGIN, new TypeReference<ResultInfo<UserInfoWrapper>>() {
//        }.getType(), params, true, true, true);

        return request.login(username, password).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ResultInfo<String>> sendCode(String phone) {
//        Map<String, String> params = new HashMap<>();
//        params.put("mobile", phone);
//        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.SEND_CODE, new TypeReference<ResultInfo<String>>() {
//        }.getType(), params, true, true, true);

        return request.sendCode(phone).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<ResultInfo<UserInfoWrapper>> register(String mobile, String code, String pwd) {
//        Map<String, String> params = new HashMap<>();
//        params.put("mobile", mobile);
//        params.put("code", code);
//        params.put("pwd", pwd);
//        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.PHONE_REGISTER, new TypeReference<ResultInfo<UserInfoWrapper>>() {
//        }.getType(), params, true, true, true);

        return request.register(mobile, code, pwd).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 修改密码
     *
     * @param pwd    原密码
     * @param newPwd 新密码
     * @return
     */
    public Observable<ResultInfo<UserInfo>> modifyPwd(String pwd, String newPwd) {
//        Map<String, String> params = new HashMap<>();
//        params.put("user_id", UserInfoHelper.getUid());
//        params.put("pwd", pwd);
//        params.put("new_pwd", newPwd);
//        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.MODIFY_PWD, new TypeReference<ResultInfo<UserInfo>>() {
//        }.getType(), params, true, true, true);

        return request.modifyPwd(UserInfoHelper.getUid(), pwd, newPwd).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ResultInfo<UserInfoWrapper>> codeLogin(String mobile, String code) {
//        Map<String, String> params = new HashMap<>();
//        params.put("mobile", mobile);
//        params.put("code", code);
//        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.CODE_LOGIN, new TypeReference<ResultInfo<UserInfoWrapper>>() {
//                }.getType(), params, true, true
//                , true);

        return request.codeLogin(mobile, code).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ResultInfo<String>> setPwd(String pwd) {
//        Map<String, String> params = new HashMap<>();
//        params.put("user_id", UserInfoHelper.getUid());
//        params.put("new_pwd", pwd);
//        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.SET_PWD, new TypeReference<ResultInfo<String>>() {
//        }.getType(), params, true, true, true);

        return request.setPwd(UserInfoHelper.getUid(),pwd).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());


    }
}
