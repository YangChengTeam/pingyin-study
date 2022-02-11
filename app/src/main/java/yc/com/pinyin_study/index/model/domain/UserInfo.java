package yc.com.pinyin_study.index.model.domain;

import com.alibaba.fastjson.annotation.JSONField;

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
    @JSONField(name = "id")
    private String user_id;
    private int vip_test_hour;
    private String vip_test_num;
    private String status;
    private int isVip;


    private String pwd;
    private String mobile;
    /**
     * id : 697871
     * imeil : 3d685df9486bd3f
     * logo : null
     * gender : null
     * app_id : 8
     * username : null
     * salt :
     */


    private String logo;
    private int gender;
    private String app_id;
    private String username;
    private String salt;

    public boolean isIs_reg() {
        return is_reg;
    }

    public void setIs_reg(boolean is_reg) {
        this.is_reg = is_reg;
    }

    public String getUser_id() {
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

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
