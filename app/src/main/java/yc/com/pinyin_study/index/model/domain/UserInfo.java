package yc.com.pinyin_study.index.model.domain;

/**
 * Created by wanglin  on 2018/10/29 09:30.
 */
public class UserInfo {
    /**
     * is_reg : true
     * user_id : 208000
     * vip_test_hour : 1
     * vip_test_num : 0
     * status : 0
     */

    private boolean is_reg;
    private String user_id;
    private int vip_test_hour;
    private String vip_test_num;
    private String status;
    private int isVip;

    public boolean isIs_reg() {
        return is_reg;
    }

    public void setIs_reg(boolean is_reg) {
        this.is_reg = is_reg;
    }

    public String getUid() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getVip_test_hour() {
        return vip_test_hour;
    }

    public void setVip_test_hour(int vip_test_hour) {
        this.vip_test_hour = vip_test_hour;
    }

    public String getVip_test_num() {
        return vip_test_num;
    }

    public void setVip_test_num(String vip_test_num) {
        this.vip_test_num = vip_test_num;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getIsVip() {
        return isVip;
    }

    public void setIsVip(int isVip) {
        this.isVip = isVip;
    }
}
