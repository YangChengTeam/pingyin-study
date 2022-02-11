package yc.com.pinyin_study.mine.model.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

import yc.com.pinyin_study.pay.alipay.OrderInfo;

/**
 * Created by wanglin  on 2019/4/27 11:46.
 */
public class OrderInfoWrapper {
    @JSONField(name = "order_list")
    private List<OrderInfo> list;

    public List<OrderInfo> getList() {
        return list;
    }

    public void setList(List<OrderInfo> list) {
        this.list = list;
    }
}
