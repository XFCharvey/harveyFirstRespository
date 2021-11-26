package com.cbox.business.filemanage.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <p>
 * 文件分片记录
 * </p>
 *
 * @since 2021-01-05
 */
public class FileZoneRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分片ID
     */
    // @TableId(value="id",type=IdType.ID_WORKER_STR)
    // @ApiModelProperty(value = "分片ID")
    private String id;

    /**
     * 分片名称
     */
    // @TableField("zone_name")
    // @ApiModelProperty(value = "分片名称")
    private String zoneName;

    /**
     * 分片的文件路径
     */
    // @TableField("zone_path")
    // @ApiModelProperty(value = "分片的文件路径")
    private String zonePath;

    /**
     * 分片MD5
     */
    // @TableField("zone_md5")
    // @ApiModelProperty(value = "分片MD5")
    private String zoneMd5;

    /**
     * 分片记录MD5值
     */
    // @TableField("zone_record_date")
    // @ApiModelProperty(value = "分片记录MD5值")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date zoneRecordDate;

    /**
     * 上传完成校验日期
     */
    // @TableField("zone_check_date")
    // @ApiModelProperty(value = "上传完成校验日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date zoneCheckDate;

    /**
     * 总的分片数
     */
    // @TableField("zone_total_count")
    // @ApiModelProperty(value = "总的分片数")
    private Integer zoneTotalCount;

    /**
     * 总文件的MD5值
     */
    // @TableField("zone_total_md5")
    // @ApiModelProperty(value = "总文件的MD5值")
    private String zoneTotalMd5;

    /**
     * 当前分片索引
     */
    // @TableField("zone_now_index")
    // @ApiModelProperty(value = "当前分片索引")
    private Integer zoneNowIndex;

    /**
     * 文件开始位置
     */
    // @TableField("zone_start_size")
    // @ApiModelProperty(value = "文件开始位置")
    private Long zoneStartSize;

    /**
     * 文件结束位置
     */
    // @TableField("zone_end_size")
    // @ApiModelProperty(value = "文件结束位置")
    private Long zoneEndSize;

    /**
     * 文件总大小
     */
    // @TableField("zone_total_size")
    // @ApiModelProperty(value = "文件总大小")
    private Long zoneTotalSize;
    /**
     * 分片文件后缀
     */
    // @TableField("zone_suffix")
    // @ApiModelProperty(value = "分片文件后缀")
    private String zoneSuffix;

    /**
     * 文件记录ID
     */
    // @TableField("file_record_id")
    // @ApiModelProperty(value = "文件记录ID")
    private String fileRecordId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getZonePath() {
        return zonePath;
    }

    public void setZonePath(String zonePath) {
        this.zonePath = zonePath;
    }

    public String getZoneMd5() {
        return zoneMd5;
    }

    public void setZoneMd5(String zoneMd5) {
        this.zoneMd5 = zoneMd5;
    }

    public Date getZoneRecordDate() {
        return zoneRecordDate;
    }

    public void setZoneRecordDate(Date zoneRecordDate) {
        this.zoneRecordDate = zoneRecordDate;
    }

    public Date getZoneCheckDate() {
        return zoneCheckDate;
    }

    public void setZoneCheckDate(Date zoneCheckDate) {
        this.zoneCheckDate = zoneCheckDate;
    }

    public Integer getZoneTotalCount() {
        return zoneTotalCount;
    }

    public void setZoneTotalCount(Integer zoneTotalCount) {
        this.zoneTotalCount = zoneTotalCount;
    }

    public String getZoneTotalMd5() {
        return zoneTotalMd5;
    }

    public void setZoneTotalMd5(String zoneTotalMd5) {
        this.zoneTotalMd5 = zoneTotalMd5;
    }

    public Integer getZoneNowIndex() {
        return zoneNowIndex;
    }

    public void setZoneNowIndex(Integer zoneNowIndex) {
        this.zoneNowIndex = zoneNowIndex;
    }

    public Long getZoneStartSize() {
        return zoneStartSize;
    }

    public void setZoneStartSize(Long zoneStartSize) {
        this.zoneStartSize = zoneStartSize;
    }

    public Long getZoneEndSize() {
        return zoneEndSize;
    }

    public void setZoneEndSize(Long zoneEndSize) {
        this.zoneEndSize = zoneEndSize;
    }

    public Long getZoneTotalSize() {
        return zoneTotalSize;
    }

    public void setZoneTotalSize(Long zoneTotalSize) {
        this.zoneTotalSize = zoneTotalSize;
    }

    public String getZoneSuffix() {
        return zoneSuffix;
    }

    public void setZoneSuffix(String zoneSuffix) {
        this.zoneSuffix = zoneSuffix;
    }

    public String getFileRecordId() {
        return fileRecordId;
    }

    public void setFileRecordId(String fileRecordId) {
        this.fileRecordId = fileRecordId;
    }

}
