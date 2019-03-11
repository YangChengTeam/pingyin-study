package yc.com.pinyin_study.study.activity;

import android.view.ContextMenu;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import yc.com.base.BaseActivity;
import yc.com.pinyin_study.R;
import yc.com.pinyin_study.study.widget.ZoomImageView;

/**
 * Created by wanglin  on 2018/11/5 16:03.
 */
public class PreviewActivity extends BaseActivity {
    @BindView(R.id.zoomImageView)
    ZoomImageView zoomImageView;

    @Override
    public boolean isStatusBarMateria() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_preview_picture;
    }

    @Override
    public void init() {
        String img = getIntent().getStringExtra("img");

        Glide.with(this).asBitmap().apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA)).load(img).into(zoomImageView);
        zoomImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


}
