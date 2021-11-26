package com.cbox.business.user.sysUser.bean;

import com.cbox.base.annotation.Excel;
import com.cbox.base.core.domain.BaseEntity;

public class UsersImport extends BaseEntity {

    /** 民兵姓名 */
    @Excel(name = "民兵姓名")
    private String real_name;

    /** 民兵手机号 */
    @Excel(name = "民兵手机号")
    private String phonenumber;

    /** 民兵性别 */
    @Excel(name = "民兵性别")
    private String sex;

    /** 民兵性别 */
    @Excel(name = "民兵身份类型")
    private String identity_type;

    /** 民兵性别 */
    @Excel(name = "民兵身份证")
    private String id_number;

    public String getIdentity_type() {
        return identity_type;
    }

    public void setIdentity_type(String identity_type) {
        this.identity_type = identity_type;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

}
