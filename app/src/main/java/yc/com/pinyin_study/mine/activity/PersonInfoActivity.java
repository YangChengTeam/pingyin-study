package yc.com.pinyin_study.mine.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;
import yc.com.pinyin_study.R;
import yc.com.pinyin_study.base.constant.BusAction;
import yc.com.pinyin_study.base.widget.MainToolBar;
import yc.com.pinyin_study.index.model.domain.UserInfo;
import yc.com.pinyin_study.index.utils.UserInfoHelper;

/**
 * Created by suns  on 2020/4/15 16:13.
 */
public class PersonInfoActivity extends BaseActivity {
    @BindView(R.id.main_toolbar)
    MainToolBar mainToolbar;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_pwd)
    TextView tvPwd;
    @BindView(R.id.rl_pwd)
    RelativeLayout rlPwd;
    private boolean isNoPwd;

    @Override
    public boolean isStatusBarMateria() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_person_info;
    }

    @Override
    public void init() {
        mainToolbar.showNavigationIcon();
        mainToolbar.init(this);
        mainToolbar.setRightContainerVisible(false);
        mainToolbar.setTitle("个人信息");
        UserInfo userInfo = UserInfoHelper.getUserInfo();
        if (userInfo != null) {
            String phone = userInfo.getMobile();
            if (!TextUtils.isEmpty(phone)) {
                tvPhone.setText(phone.replace(phone.substring(3, 7), "****"));
            }
            if (TextUtils.isEmpty(userInfo.getPwd())) {
                isNoPwd = true;
            }
        }

        tvPwd.setText(isNoPwd ? "设置密码" : "修改密码");
        initListener();
    }

    private void initListener() {
        RxView.clicks(rlPwd).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (isNoPwd) {
                    Intent intent = new Intent(PersonInfoActivity.this, SetPwdActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(PersonInfoActivity.this, ModifyPWdActivity.class);
                    startActivity(intent);
                }
            }
        });
    }


    @Subscribe(thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.SET_PWD)
            })
    public void setPwdSuccess(String success) {
        isNoPwd = false;
    }

}
