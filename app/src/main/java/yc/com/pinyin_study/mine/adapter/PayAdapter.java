package yc.com.pinyin_study.mine.adapter;

import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
public class PayAdapter extends BaseQuickAdapter<GoodInfo, BaseViewHolder> {

    private SparseArray<ImageView> sparseArray;


    public PayAdapter(List<GoodInfo> data) {
        super(R.layout.pay_info_item, data);
        sparseArray = new SparseArray<>();
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodInfo item) {
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

        if (UserInfoHelper.isPhonogramOrPhonicsVip()) {
            imageView.setImageResource(R.mipmap.pay_selected);
            layout.setVisibility(View.GONE);
            imageView.setTag(true);
            return;
        }


//        if (UserInfoHelper.isPhonogramOrPhonicsVip()) {
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

        if (UserInfoHelper.isPhonicsVip()) {
            imageView.setImageResource(R.mipmap.vip_info_unselect);
            if (goodId == Config.PHONOGRAM_VIP) {
                imageView.setImageResource(R.mipmap.vip_info_selected);
            }
            imageView.setTag(false);
            layout.setVisibility(View.VISIBLE);
            if (goodId == Config.PHONICS_VIP) {
                imageView.setImageResource(R.mipmap.pay_selected);
                layout.setVisibility(View.GONE);
                imageView.setTag(true);
            }
            return;
        }

        if (UserInfoHelper.isPhonogramVip()) {
            imageView.setImageResource(R.mipmap.vip_info_unselect);
            if (goodId == Config.PHONICS_VIP) {
                imageView.setImageResource(R.mipmap.vip_info_selected);
            }
            imageView.setTag(false);
            layout.setVisibility(View.VISIBLE);
            if (goodId == Config.PHONOGRAM_VIP) {
                imageView.setImageResource(R.mipmap.pay_selected);
                layout.setVisibility(View.GONE);
                imageView.setTag(true);
            }
            return;

        }

        if (position == 0) {
            imageView.setImageResource(R.mipmap.vip_info_selected);
        } else {
            imageView.setImageResource(R.mipmap.vip_info_unselect);
        }
        imageView.setTag(false);

    }


}
