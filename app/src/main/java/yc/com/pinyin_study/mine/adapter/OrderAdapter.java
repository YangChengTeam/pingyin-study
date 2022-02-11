package yc.com.pinyin_study.mine.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.core.content.ContextCompat;
import yc.com.pinyin_study.R;
import yc.com.pinyin_study.pay.alipay.OrderInfo;

/**
 * Created by wanglin  on 2019/4/25 15:23.
 */
public class OrderAdapter extends BaseQuickAdapter<OrderInfo, BaseViewHolder> {


    public OrderAdapter(List<OrderInfo> data) {
        super(R.layout.order_item_info, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderInfo item) {
        helper.setText(R.id.tv_order_time, item.getAdd_time())
                .setText(R.id.tv_order_sn, item.getOrder_sn())
                .setText(R.id.tv_order_money, String.valueOf(item.getMoney()))
                .setText(R.id.tv_order_state, item.getStatus_text())
                .setTextColor(R.id.tv_order_state, item.getState() == 4 ?
                        ContextCompat.getColor(mContext, R.color.gray_81817e) :
                        ContextCompat.getColor(mContext, R.color.app_selected_color));
    }


}
