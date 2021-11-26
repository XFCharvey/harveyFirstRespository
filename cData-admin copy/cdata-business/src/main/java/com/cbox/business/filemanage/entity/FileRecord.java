package com.cbox.business.filemanage.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <p>
 * 文件上传记录
 * </p>
 * 
 * @since 2021-01-05
 */
public class FileRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    // @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 源文件名
     */
    // @TableField("org_name")
    private String orgName;

    /**
     * 服务器生成的文件名
     */
    // @TableField("server_local_name")
    private String serverLocalName;

    /**
     * 服务器储存路径
     */
    // @TableField("server_local_path")
    private String serverLocalPath;

    /**
     * 网络路径，生成的文件夹+系统生成文件名
     */
    // @TableField("network_path")
    private String networkPath;

    /**
     * 上传类型 1:用户头像 2文档、其他
     */
    // @TableField("upload_type")
    private Integer uploadType;

    /**
     * 文件MD5值
     */
    // @TableField("md5_value")
    private String md5Value;

    /**
     * 文件大小
     */
    // @TableField("file_size")
    private Long fileSize;

    /**
     * 是否分片 0 否 1是
     */
    // @TableField("is_zone")
    private Integer isZone;

    /**
     * 分片总数
     */
    // @TableField("zone_total")
    private Integer zoneTotal;

    /**
     * 分片时间
     */
    // @TableField("zone_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date zoneDate;

    /**
     * 分片合并时间
     */
    // @TableField("zone_merge_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date zoneMergeDate;

    /**
     * 文件类型
     */
    // @TableField("file_type")
    private String fileType;

    /**
     * 设备信息
     */
    // @TableField("upload_device")
    private String uploadDevice;

    /**
     * 上传设备IP
     */
    // @TableField("upload_ip")
    private String uploadIp;

    /**
     * 储存日期
     */
    // @TableField("storage_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date storageDate;

    /**
     * 下载统计
     */
    // @TableField("download_count")
    private Integer downloadCount;

    /**
     * 上传统计
     */
    // @TableField("upload_count")
    private Integer uploadCount;

    /**
     * 是否合并
     */
    // @TableField("is_merge")
    private Integer isMerge;

    /**
     * 上传人员
     */
    // @TableField("create_by")
    private String createBy;

    /**
     * 上传日期
     */
    // @TableField("create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 删除标记 1正常 -1删除
     */
    // @TableField("del_flag")
    // @TableLogic
    private Integer delFlag;
    
    /**
     * 最后修改人员
     */
    private String updateBy;

    /**
     * 修改日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getServerLocalName() {
        return serverLocalName;
    }

    public void setServerLocalName(String serverLocalName) {
        this.serverLocalName = serverLocalName;
    }

    public String getServerLocalPath() {
        return serverLocalPath;
    }

    public void setServerLocalPath(String serverLocalPath) {
        this.serverLocalPath = serverLocalPath;
    }

    public String getNetworkPath() {
        return networkPath;
    }

    public void setNetworkPath(String networkPath) {
        this.networkPath = networkPath;
    }

    public Integer getUploadType() {
        return uploadType;
    }

    public void setUploadType(Integer uploadType) {
        this.uploadType = uploadType;
    }

    public String getMd5Value() {
        return md5Value;
    }

    public void setMd5Value(String md5Value) {
        this.md5Value = md5Value;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getIsZone() {
        return isZone;
    }

    public void setIsZone(Integer isZone) {
        this.isZone = isZone;
    }

    public Integer getZoneTotal() {
        return zoneTotal;
    }

    public void setZoneTotal(Integer zoneTotal) {
        this.zoneTotal = zoneTotal;
    }

    public Date getZoneDate() {
        return zoneDate;
    }

    public void setZoneDate(Date zoneDate) {
        this.zoneDate = zoneDate;
    }

    public Date getZoneMergeDate() {
        return zoneMergeDate;
    }

    public void setZoneMergeDate(Date zoneMergeDate) {
        this.zoneMergeDate = zoneMergeDate;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getUploadDevice() {
        return uploadDevice;
    }

    public void setUploadDevice(String uploadDevice) {
        this.uploadDevice = uploadDevice;
    }

    public String getUploadIp() {
        return uploadIp;
    }

    public void setUploadIp(String uploadIp) {
        this.uploadIp = uploadIp;
    }

    public Date getStorageDate() {
        return storageDate;
    }

    public void setStorageDate(Date storageDate) {
        this.storageDate = storageDate;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    public Integer getUploadCount() {
        return uploadCount;
    }

    public void setUploadCount(Integer uploadCount) {
        this.uploadCount = uploadCount;
    }

    public Integer getIsMerge() {
        return isMerge;
    }

    public void setIsMerge(Integer isMerge) {
        this.isMerge = isMerge;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}
