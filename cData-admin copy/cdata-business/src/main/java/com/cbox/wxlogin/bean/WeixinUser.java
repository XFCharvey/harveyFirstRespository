package com.cbox.wxlogin.bean;

import java.util.Date;

public class WeixinUser {
    private String nickname;
    private String openid;
    private String sex;
    private String province;
    private String city;
    private String country;
    private String headimgurl;
    private String unionid;
    private Date last_online;
    private String user_id;

    private String rec_id;
    private String rec_status;
    private String rec_person;
    private Date rec_time;
    private String rec_updateperson;
    private Date rec_updatetime;

    public String getRec_id() {
        return rec_id;
    }

    public void setRec_id(String rec_id) {
        this.rec_id = rec_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public Date getLast_online() {
        return last_online;
    }

    public void setLast_online(Date last_online) {
        this.last_online = last_online;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getRec_status() {
        return rec_status;
    }

    public void setRec_status(String rec_status) {
        this.rec_status = rec_status;
    }

    public String getRec_person() {
        return rec_person;
    }

    public void setRec_person(String rec_person) {
        this.rec_person = rec_person;
    }

    public Date getRec_time() {
        return rec_time;
    }

    public void setRec_time(Date rec_time) {
        this.rec_time = rec_time;
    }

    public String getRec_updateperson() {
        return rec_updateperson;
    }

    public void setRec_updateperson(String rec_updateperson) {
        this.rec_updateperson = rec_updateperson;
    }

    public Date getRec_updatetime() {
        return rec_updatetime;
    }

    public void setRec_updatetime(Date rec_updatetime) {
        this.rec_updatetime = rec_updatetime;
    }

}
