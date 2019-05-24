package yc.com.pinyin_study.category.adapter;

import android.text.Html;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;


import java.util.List;

import yc.com.pinyin_study.R;
import yc.com.pinyin_study.category.model.domain.WeiKeCategory;

/**
 * Created by zhangkai on 2017/8/30.
 */

public class WeiKeInfoItemAdapter extends BaseQuickAdapter<WeiKeCategory, BaseViewHolder> {


    public WeiKeInfoItemAdapter(List<WeiKeCategory> data) {
        super(R.layout.weike_info_item, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, WeiKeCategory item) {

        helper.setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_unit_count, Html.fromHtml("<font color='#FB4C30'>" + item.getUserNum() + "</font>人已学习"));
        Glide.with(mContext).asBitmap().apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA)).load(item.getImg()).into((ImageView) helper.getView(R.id.iv_icon));


        if (item.getIsVip() == 0) {
            helper.setVisible(R.id.iv_is_vip, false);
        } else {
            helper.setVisible(R.id.iv_is_vip, true);
        }


    }
}
