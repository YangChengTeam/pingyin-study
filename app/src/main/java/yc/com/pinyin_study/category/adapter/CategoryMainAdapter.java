package yc.com.pinyin_study.category.adapter;

import android.text.Html;
import android.text.Spanned;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import yc.com.blankj.utilcode.util.ImageUtils;
import yc.com.pinyin_study.R;
import yc.com.pinyin_study.category.model.domain.WeiKeCategory;
import yc.com.pinyin_study.category.widget.transformations.RoundedCornersTransformation;

/**
 * Created by wanglin  on 2018/10/25 11:31.
 */
public class CategoryMainAdapter extends BaseQuickAdapter<WeiKeCategory, BaseViewHolder> {
    public CategoryMainAdapter(List<WeiKeCategory> data) {
        super(R.layout.weikecategory_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WeiKeCategory item) {
        Glide.with(mContext).asBitmap()
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA).skipMemoryCache(true).transform(new RoundedCornersTransformation(18, 0))).load(item.getImg())
                .into((ImageView) helper.getView(R.id.iv_item_cover));


        Spanned spanned = Html.fromHtml("总共<font color='#ff0000'>" + item.getUnitNum() + "</font>单元");
        helper.setText(R.id.tv_item_title, item.getTitle())
                .setText(R.id.tv_item_total, spanned);
    }


}
