package com.cbox.business.customer.customer.bean;

import com.cbox.base.annotation.Excel;
import com.cbox.base.core.domain.BaseEntity;

public class CustomerImport extends BaseEntity {

    /** 客户姓名 */
    @Excel(name = "客户姓名")
    private String customer_name;

    /** 客户手机号 */
    @Excel(name = "客户手机号")
    private String customer_phone;

    /** 客户性别 */
    @Excel(name = "客户性别")
    private String customer_sex;

    /** 客户身份证 */
    @Excel(name = "客户身份证")
    private String customer_id;

    /** 客户类型 */
    @Excel(name = "客户类型")
    private String customer_type;

    /** 客户状态 */
    @Excel(name = "客户状态")
    private String customer_status;

    /** 阿隆关注 */
    @Excel(name = "阿隆关注")
    private String along_follow;

    /** 官微关注 */
    @Excel(name = "官微关注")
    private String official_micro_follow;

    /** 客户地址 */
    @Excel(name = "客户地址")
    private String customer_addr;

    /** 客户职业 */
    @Excel(name = "客户职业")
    private String customer_vocation;

    /** 客户兴趣爱好特长 */
    @Excel(name = "客户兴趣爱好特长")
    private String customer_hobby;

    /** 推荐人 */
    @Excel(name = "推荐人")
    private String recommender;
    
    /** 房间号 */
    @Excel(name = "房间号")
    private String house_name;

    public String getHouse_name() {
		return house_name;
	}

	public void setHouse_name(String house_name) {
		this.house_name = house_name;
	}

	public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public String getCustomer_sex() {
        return customer_sex;
    }

    public void setCustomer_sex(String customer_sex) {
        this.customer_sex = customer_sex;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public String getCustomer_type() {
        return customer_type;
    }

    public void setCustomer_type(String customer_type) {
        this.customer_type = customer_type;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_status() {
        return customer_status;
    }

    public void setCustomer_status(String customer_status) {
        this.customer_status = customer_status;
    }

    public String getAlong_follow() {
        return along_follow;
    }

    public void setAlong_follow(String along_follow) {
        this.along_follow = along_follow;
    }

    public String getOfficial_micro_follow() {
        return official_micro_follow;
    }

    public void setOfficial_micro_follow(String official_micro_follow) {
        this.official_micro_follow = official_micro_follow;
    }

    public String getCustomer_addr() {
        return customer_addr;
    }

    public void setCustomer_addr(String customer_addr) {
        this.customer_addr = customer_addr;
    }

    public String getCustomer_vocation() {
        return customer_vocation;
    }

    public void setCustomer_vocation(String customer_vocation) {
        this.customer_vocation = customer_vocation;
    }

    public String getCustomer_hobby() {
        return customer_hobby;
    }

    public void setCustomer_hobby(String customer_hobby) {
        this.customer_hobby = customer_hobby;
    }

    public String getRecommender() {
        return recommender;
    }

    public void setRecommender(String recommender) {
        this.recommender = recommender;
    }

}
