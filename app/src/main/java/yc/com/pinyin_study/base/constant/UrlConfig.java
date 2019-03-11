package yc.com.pinyin_study.base.constant;

/**
 * Created by wanglin  on 2018/10/25 14:04.
 */
public interface UrlConfig {

    boolean isDebug = false;
    String debug_url = "http://tic.upkao.com/Xmyinbiao/";
    String base_url = "http://tic.upkao.com/xmpinyin/";

    /**
     * 首页index
     */
    String main_index_url = (isDebug ? debug_url : base_url) + "index/init";

    /**
     * 微课主页
     */
    String category_url = (isDebug ? debug_url : base_url) + "index/weike_list";

    /**
     * 微课详情
     */
    String weike_info_url = (isDebug ? debug_url : base_url) + "index/weike_info";


    /**
     * vip信息
     */
    String vip_info_url = (isDebug ? debug_url : base_url) + "index/vip_list";

    /**
     * 创建订单
     */
    String pay_url = (isDebug ? debug_url : base_url) + "orders/pay";

    /**
     * 学习列表
     */
    String study_list_url = (isDebug ? debug_url : base_url) + "index/vowel_lists";

    /**
     * 学习详情页
     */
    String study_detail_url = (isDebug ? debug_url : base_url) + "index/vowel_detail";

    /**
     * 所有的元音数据
     */
    String vowel_all_url = (isDebug ? debug_url : base_url) + "index/vowel_all";

    /**
     * 上传手机号码
     */
    String upd_info_url = (isDebug ? debug_url : base_url) + "index/upd_user_info";

    /**
     * 是否绑定手机号
     */
    String is_bind_mobile_url = (isDebug ? debug_url : base_url) + "index/is_bind_mobile";


    /**
     * 首页广告
     */
    String index_menu_url = (isDebug ? debug_url : base_url) + "index/menu_adv";

    /**
     * 易错知识点
     */
    String error_list = (isDebug ? debug_url : base_url) + "pinyin/lists";

    /**
     * 易错知识点详情
     */
    String error_detail_info = (isDebug ? debug_url : base_url) + "pinyin/detail";
}
