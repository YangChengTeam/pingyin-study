package yc.com.pinyin_study.study.adapter;

import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kk.utils.LogUtil;
import com.kk.utils.ScreenUtil;

import java.util.List;

import yc.com.base.BaseView;
import yc.com.pinyin_study.R;
import yc.com.pinyin_study.index.utils.UserInfoHelper;
import yc.com.pinyin_study.study.model.domain.WordInfo;

/**
 * Created by wanglin  on 2018/11/1 09:12.
 */
public class StudyVowelAdapter extends BaseQuickAdapter<WordInfo, BaseViewHolder> {
    public StudyVowelAdapter(List<WordInfo> data) {
        super(R.layout.study_vowel_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WordInfo item) {
//        helper.setText(R.id.tv_vowel, item.getName());
        Glide.with(mContext).asBitmap().load(item.getImg()).into((ImageView) helper.getView(R.id.iv_vowel));

        setItemSpace(helper);
        setItemState(helper, item);


    }


    private void setItemSpace(BaseViewHolder holder) {

        int position = holder.getLayoutPosition();

        LinearLayout.MarginLayoutParams layoutParams = (LinearLayout.MarginLayoutParams) holder.itemView.getLayoutParams();

        if (position % 2 == 0) {
            layoutParams.leftMargin = ScreenUtil.dip2px(mContext, 5);
        } else {
            layoutParams.rightMargin = ScreenUtil.dip2px(mContext, 5);
        }
        holder.itemView.setLayoutParams(layoutParams);

    }

    private void setItemState(BaseViewHolder helper, WordInfo item) {

        if (UserInfoHelper.isPhonogramVip()) {
            helper.setVisible(R.id.iv_cover_layer, false);
            helper.setVisible(R.id.iv_lock, false);
        } else {
            helper.setVisible(R.id.iv_cover_layer, item.getIs_vip() == 1);
            helper.setVisible(R.id.iv_lock, item.getIs_vip() == 1);
        }


    }

}
