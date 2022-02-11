package yc.com.pinyin_study.pay.alipay;

import com.alibaba.fastjson.annotation.JSONField;
import com.vondear.rxtools.RxTimeTool;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by zhangkai on 2017/3/17.
 */

public class OrderInfo implements Serializable {

    private static final long serialVersionUID = -7060210533610464481L;

    private float money; //价格 单位元

    private String name; //会员类型名 也即商品名


    @JSONField(name = "order_sn")
    private String order_sn; //订单号

    private String message;


    private String type;//支付类型

    @JSONField(name = "params")
    private PayInfo payInfo;

    private String goodId;
    private String time;
    @JSONField(name = "status")
    private int state;


    private String goods_title;

    private String add_time;

    private long pay_time;

    @JSONField(name = "status_txt")
    private String status_text;


    public OrderInfo() {
    }


    public OrderInfo(float money, String name, String order_sn) {
        this.money = money;
        this.name = name;
        this.order_sn = order_sn;
    }


    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public PayInfo getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(PayInfo payInfo) {
        this.payInfo = payInfo;

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getGoodId() {
        return goodId;
    }

    public void setGoodId(String goodId) {
        this.goodId = goodId;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public String getGoods_title() {
        return goods_title;
    }

    public void setGoods_title(String goods_title) {
        this.goods_title = goods_title;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public long getPay_time() {
        return pay_time;
    }

    public void setPay_time(long pay_time) {
        this.pay_time = pay_time;
    }

    public String getStatus_text() {
        return status_text;
    }

    public void setStatus_text(String status_text) {
        this.status_text = status_text;
    }

//    public String getOrederTime() {
//        return RxTimeTool.date2String(new Date(add_time * 1000), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()));
//    }
}
