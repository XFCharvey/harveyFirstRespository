package com.cbox.common.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里短信配置
 */
@Component
@ConfigurationProperties("ali.sms")
public class AliSmsConfig {

    private String domain = "dysmsapi.aliyuncs.com";
    private String sysVersion = "2017-05-25";
    private String regionId = "cn-hangzhou";// 地域ID
    private String accessKeyId;// 您的AccessKey ID
    private String accessKeySecret;// 您的AccessKey Secret
    private String signName;// 默认签名

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getSysVersion() {
        return sysVersion;
    }

    public void setSysVersion(String sysVersion) {
        this.sysVersion = sysVersion;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

}