package yc.com.pinyin_study.base.adapter;

import android.text.TextPaint;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import yc.com.pinyin_study.R;
import yc.com.pinyin_study.base.constant.Config;
import yc.com.pinyin_study.base.model.domain.GoodInfo;
import yc.com.pinyin_study.index.utils.UserInfoHelper;

/**
 * Created by wanglin  on 2018/10/30 08:50.
 */
public class BasePayAdapter extends BaseQuickAdapter<GoodInfo, BaseViewHolder> {

    private SparseArray<ImageView> sparseArray;

    public BasePayAdapter(List<GoodInfo> data) {
        super(R.layout.vip_info_item, data);
        sparseArray = new SparseArray<>();
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodInfo item) {
        helper.setText(R.id.tv_vip_title, item.getTitle())
                .setText(R.id.tv_current_price, String.format(mContext.getString(R.string.vip_price), item.getReal_price()))
                .setText(R.id.tv_new_origin_price, String.format(mContext.getString(R.string.vip_price), item.getPrice()))
                .setText(R.id.tv_vip_subtitle, item.getSub_title());
        ((TextView) helper.getView(R.id.tv_new_origin_price)).getPaint().setFlags(TextPaint.STRIKE_THRU_TEXT_FLAG | TextPaint.ANTI_ALIAS_FLAG);
        Glide.with(mContext).asBitmap().load(item.getIcon()).into((ImageView) helper.getView(R.id.iv_vip_index));

        helper.setText(R.id.tv_vip_price, String.format(mContext.getString(R.string.vip_price), item.getReal_price()))
                .setText(R.id.tv_origin_price, String.format(mContext.getString(R.string.origin_price), item.getPrice()));
        helper.addOnClickListener(R.id.iv_info_item);
        int position = helper.getAdapterPosition();
        ImageView view = helper.getView(R.id.iv_info_item);
        LinearLayout layout = helper.getView(R.id.ll_price);
        sparseArray.put(position, view);
        setIvState(view, layout, position, item);


    }

    public ImageView getIv(int position) {
        return sparseArray.get(position);
    }


    private void setIvState(ImageView imageView, LinearLayout layout, int position, GoodInfo item) {
        int goodId = Integer.parseInt(item.getId());

        if (UserInfoHelper.isPhonogramVip()) {
            imageView.setImageResource(R.mipmap.pay_selected);
            layout.setVisibility(View.GONE);
            imageView.setTag(true);
            return;
        }


//        if (UserInfoHelper.isPhonogramOrPhonicsVip() ) {
//            imageView.setImageResource(R.mipmap.vip_info_unselect);
//            if (goodId == Config.SUPER_VIP) {
//                imageView.setImageResource(R.mipmap.vip_info_selected);
//            }
//            layout.setVisibility(View.VISIBLE);
//            imageView.setTag(false);
//            if (goodId == Config.PHONICS_VIP || goodId == Config.PHONOGRAM_VIP || goodId == Config.PHONOGRAMORPHONICS_VIP) {
//                imageView.setImageResource(R.mipmap.pay_selected);
//                layout.setVisibility(View.GONE);
//                imageView.setTag(true);
//            }
//            return;
//        }
//
//        if (UserInfoHelper.isPhonicsVip()) {
//            imageView.setImageResource(R.mipmap.vip_info_unselect);
//            if (goodId == Config.PHONOGRAM_VIP) {
//                imageView.setImageResource(R.mipmap.vip_info_selected);
//            }
//            imageView.setTag(false);
//            layout.setVisibility(View.VISIBLE);
//            if (goodId == Config.PHONICS_VIP) {
//                imageView.setImageResource(R.mipmap.pay_selected);
//                layout.setVisibility(View.GONE);
//                imageView.setTag(true);
//            }
//            return;
//        }
//
//        if (UserInfoHelper.isPhonogramVip()) {
//            imageView.setImageResource(R.mipmap.vip_info_unselect);
//            if (goodId == Config.PHONICS_VIP) {
//                imageView.setImageResource(R.mipmap.vip_info_selected);
//            }
//            imageView.setTag(false);
//            layout.setVisibility(View.VISIBLE);
//            if (goodId == Config.PHONOGRAM_VIP) {
//                imageView.setImageResource(R.mipmap.pay_selected);
//                layout.setVisibility(View.GONE);
//                imageView.setTag(true);
//            }
//            return;
//
//        }

        if (position == 0) {
            imageView.setImageResource(R.mipmap.vip_info_selected);
        } else {
            imageView.setImageResource(R.mipmap.vip_info_unselect);
        }
        imageView.setTag(false);

    }


}
