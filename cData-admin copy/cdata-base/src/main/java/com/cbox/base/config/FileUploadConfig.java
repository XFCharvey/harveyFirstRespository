package com.cbox.base.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 文件上传配置
 */
@Component
@ConfigurationProperties("fileupload.config")
public class FileUploadConfig {
    private String uploadFolder;
    private String staticAccessPath;
    private String localPath;
    private String userHeaderPicPath;
    private String archivesFilePath;
    private String domain;

    public String getUploadFolder() {
        return uploadFolder;
    }

    public void setUploadFolder(String uploadFolder) {
        this.uploadFolder = uploadFolder;
    }

    public String getStaticAccessPath() {
        return staticAccessPath;
    }

    public void setStaticAccessPath(String staticAccessPath) {
        this.staticAccessPath = staticAccessPath;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getUserHeaderPicPath() {
        return userHeaderPicPath;
    }

    public void setUserHeaderPicPath(String userHeaderPicPath) {
        this.userHeaderPicPath = userHeaderPicPath;
    }

    public String getArchivesFilePath() {
        return archivesFilePath;
    }

    public void setArchivesFilePath(String archivesFilePath) {
        this.archivesFilePath = archivesFilePath;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

}
