package yc.com.pinyin_study.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import butterknife.BindView;
import yc.com.pinyin_study.R;


/**
 * Created by wanglin  on 2018/10/24 17:45.
 */
public class MainToolBar extends BaseToolBar {


    public MainToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.main_toolbar_view;
    }
}
