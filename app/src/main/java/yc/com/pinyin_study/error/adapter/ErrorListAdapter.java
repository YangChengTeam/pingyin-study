package yc.com.pinyin_study.error.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import yc.com.pinyin_study.R;
import yc.com.pinyin_study.error.model.bean.ErrorInfo;

/**
 * Created by wanglin  on 2019/3/7 17:31.
 */
public class ErrorListAdapter extends BaseQuickAdapter<ErrorInfo, BaseViewHolder> {
    public ErrorListAdapter(List<ErrorInfo> data) {
        super(R.layout.error_item_view, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ErrorInfo item) {
        Glide.with(mContext).asBitmap().apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA)).load(item.getImg()).thumbnail(0.1f).into((ImageView) helper.getView(R.id.iv_item));
    }
}
