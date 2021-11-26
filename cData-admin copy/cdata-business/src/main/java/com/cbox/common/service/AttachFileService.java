package com.cbox.common.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbox.base.config.FileUploadConfig;
import com.cbox.base.core.service.BaseService;
import com.cbox.base.utils.DateUtils;
import com.cbox.base.utils.StrUtil;

/**
 * @ClassName: AttachFileService 
 * @Function: 附件表的获取服务  
 * 
 * @author qiu 
 * @date 2021年4月9日 上午10:22:24 
 * @version 1.0
 */
@Service
@Transactional
public class AttachFileService extends BaseService {

    private String fileAttachTable = "d_sys_file_record";

    @Autowired
    private FileUploadConfig fileUploadConfig;

    /** 根据文件id得到文件服务器路径 */
    public String getFileRealPath(String fileId) {

        Map<String, Object> mapResult = this.getFileInfo(fileId);
        String realPath = StrUtil.getMapValue(mapResult, "server_local_path");

        return realPath;
    }

    /** 根据文件id得到文件网络路径 */
    public String getFileNetworkPath(String fileId) {

        Map<String, Object> mapResult = this.getFileInfo(fileId);
        String realPath = StrUtil.getMapValue(mapResult, "network_path");

        return realPath;
    }

    /** 根据文件id得到文件名 */
    public String getFileName(String fileId) {

        Map<String, Object> mapResult = this.getFileInfo(fileId);
        String fileName = StrUtil.getMapValue(mapResult, "org_name");

        return fileName;
    }

    /** 根据文件id得到文件信息 */
    public Map<String, Object> getFileInfo(String fileId) {

        List<String> listColumn0 = new ArrayList<String>();
        listColumn0.add("server_local_path");
        listColumn0.add("network_path");
        listColumn0.add("org_name");
        Map<String, Object> mapQueryParam0 = new HashMap<String, Object>();
        mapQueryParam0.put("id", fileId);
        Map<String, Object> mapQueryResult0 = this.queryOneNoRec(fileAttachTable, mapQueryParam0, listColumn0);

        return mapQueryResult0;
    }
    
    /** 得到数据存储的相对路径 */
    public String getRelativePath() {
        String relativePath = "application/" + getDatePath();
        return relativePath;
    }

    /** 得到数据存储的网络路径，以storage开头 */
    public String getNetWorkPath(String relativePath) {
        String saticAccess = fileUploadConfig.getStaticAccessPath().replace("*", "");
        String networkPath = "/" + saticAccess + fileUploadConfig.getArchivesFilePath() + relativePath;
        return networkPath;
    }

    /** 得到数据存储的绝对路径 */
    public String getServerLocalPath(String relativePath) {
        String serverLoaclPath = fileUploadConfig.getUploadFolder() + fileUploadConfig.getLocalPath() + fileUploadConfig.getArchivesFilePath() + relativePath;
        return serverLoaclPath;
    }
    
    private String getDatePath() {
        return DateUtils.format(new Date(), "yyyy") + "/" + DateUtils.format(new Date(), "MMdd") + "/" + DateUtils.format(new Date(), "HH") + "/";
    }

    /** 得到数字地球存储的文件路径 */
    public String getEarthBasePath() {
        String path = fileUploadConfig.getUploadFolder() + fileUploadConfig.getLocalPath() + "earthdata/";
        return path;
    }

}
