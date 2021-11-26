package com.cbox.business.filemanage.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.cbox.base.config.FileUploadConfig;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.service.BaseService;
import com.cbox.base.utils.DateUtils;
import com.cbox.base.utils.ExceptionUtil;
import com.cbox.base.utils.SecurityUtils;
import com.cbox.base.utils.file.FileHandleUtil;
import com.cbox.base.utils.file.FileUtils;
import com.cbox.base.utils.id.GlobalRecIdUtil;
import com.cbox.base.utils.ip.IpUtils;
import com.cbox.business.filemanage.entity.FileRecord;
import com.cbox.business.filemanage.entity.FileZoneRecord;
import com.cbox.business.filemanage.mapper.FileRecordMapper;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * <p>
 * 文件上传记录 服务实现类
 * </p>
 */
@Service
public class FileRecordService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(FileRecordService.class);

    @Autowired
    private FileRecordMapper fileRecordMapper;

    @Autowired
    private FileUploadConfig fileUploadConfig;

    @Autowired
    private FileZoneRecordService fileZoneRecordService;

    public FileRecord getById(String id) {
        return fileRecordMapper.getById(id);
    }

    public List<FileRecord> listByIds(String ids) {
        List<String> idList = Splitter.on(",").trimResults().splitToList(ids);
        return listByIds(idList);
    }
    
    public List<FileRecord> listByIds(List<String> idList) {
        return fileRecordMapper.listByIds(idList);
    }

    public boolean saveOrUpdate(FileRecord fileRecord) {
        if (StringUtils.isBlank(fileRecord.getId())) {
            fileRecord.setId(GlobalRecIdUtil.nextRecId());
            fileRecord.setCreateBy(SecurityUtils.getUsername());
            return fileRecordMapper.save(fileRecord) > 0;
        } else {
            return fileRecordMapper.update(fileRecord) > 0;
        }
    }

    /**
     * *删除文件 单个
     * 
     * @param id
     * @return
     */
    public ResponseBodyVO removeById(String id) {
        FileRecord fileRecord = getById(id);
        if (fileRecord != null) {
            fileRecord.getServerLocalPath();
            boolean result = fileRecordMapper.deleteById(id) > 0;
            if (result) {
                String filePath = fileRecord.getServerLocalPath();
                if (StringUtils.isNotBlank(filePath)) {
                    FileUtils.deleteFile(filePath);
                }
                return ServerRspUtil.success("文件删除成功", null);
            } else {
                return ServerRspUtil.error("文件删除失败");
            }
        } else {
            return ServerRspUtil.error("文件不存在");
        }
    }

    /**
     * *批量删除文件
     * 
     * @param ids
     * @return
     */
    public ResponseBodyVO removeByIds(List<String> ids) {
        List<FileRecord> fileList = fileRecordMapper.listByIds(ids);
        if (fileList != null) {
            boolean result = fileRecordMapper.deleteBatchIds(ids) > 0;
            if (result) {
                fileList.forEach(fileRecord -> {
                    String filePath = fileRecord.getServerLocalPath();
                    if (StringUtils.isNotBlank(filePath)) {
                        FileUtils.deleteFile(filePath);
                    }
                });
                return ServerRspUtil.success();
            } else {
                return ServerRspUtil.error("文件删除失败");
            }
        } else {
            return ServerRspUtil.error("文件不存在");
        }
    }

    /**
     * 单文件上传
     * 
     * @param request
     * @param uploadType
     * @return
     */
    public ResponseBodyVO upload(HttpServletRequest request, Integer uploadType) {
        Long nowtime = System.currentTimeMillis();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multipartRequest.getFile("file");
        if (multipartFile.isEmpty()) {
            return ServerRspUtil.error("文件不能为空");
        }
        String contentType = multipartFile.getContentType();
        logger.info("contentType:" + contentType);
        // if(contentType==null||!isFileter(contentType)){//拦截，是否在上传名单
        // return new ResponseBodyVO(ResponseBodyVOCode.FILENOTSUPPORT);
        // }

        String fileName = multipartFile.getOriginalFilename();
        Long size = multipartFile.getSize();
        logger.info(fileName + "-->" + size);
        String saticAccess = fileUploadConfig.getStaticAccessPath().replace("*", "");
        try {
            // 本地路径 1 用户头像
            uploadType = uploadType != null && uploadType == 1 ? uploadType : 2;
            // 计算MD5值
            String filemd5 = DigestUtils.md5DigestAsHex(multipartFile.getInputStream());
            // 查询数据库是否已经有了，有直接写入，没有，写入磁盘
            FileRecord fileRecord = selByMD5AndUpType(filemd5, uploadType);
            Map<String, Object> resultmap = Maps.newHashMap();
            String fileType = contentType.split("/")[0];

            String fileId = "";
            if (fileRecord == null) {
                String pathTypeDir = (uploadType == 1 ? fileUploadConfig.getUserHeaderPicPath() : fileUploadConfig.getArchivesFilePath()) + fileType + "/";
                String path_date = getDatePath();
                String localPath = getUploadFolder() + fileUploadConfig.getLocalPath() + pathTypeDir + path_date;
                // 随机生成服务器本地路径
                String fileSuffix = getFileSuffix(fileName);
                String serverFileName = GlobalRecIdUtil.nextRecId() + fileSuffix;
                FileHandleUtil.upload(multipartFile.getInputStream(), localPath, serverFileName);
                String netWorkPath = "/" + saticAccess + pathTypeDir + path_date + serverFileName;

                fileRecord = new FileRecord();
                fileRecord.setDownloadCount(0);
                fileRecord.setUploadCount(1);
                fileRecord.setIsMerge(1);// 单文件，完整文件
                fileRecord.setIsZone(0);
                fileRecord.setFileSize(size);
                fileRecord.setFileType(fileType);
                fileRecord.setMd5Value(filemd5);
                fileRecord.setOrgName(fileName);
                fileRecord.setUploadType(uploadType);
                fileRecord.setServerLocalName(serverFileName);
                fileRecord.setServerLocalPath(localPath + serverFileName);
                fileRecord.setNetworkPath(netWorkPath);
                fileRecord.setStorageDate(getDateToYear(null));// 默认一百年
                fileId = saveFileRecord(request, fileRecord);
            } else {
                fileId = saveFileRecord(request, fileRecord);
            }
            resultmap = getResultFileMap(fileId, fileRecord.getOrgName(), fileRecord.getNetworkPath());
            Float fileSize = Float.parseFloat(String.valueOf(fileRecord.getFileSize())) / 1024;
            BigDecimal b = new BigDecimal(size);
            fileSize = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();// 2表示2位 ROUND_HALF_UP表明四舍五入，
            resultmap.put("fileSize", String.valueOf(fileSize));// 文件大小（单位Kb）
            logger.info("耗时： " + (System.currentTimeMillis() - nowtime) + " ms");
            return ServerRspUtil.success(resultmap);
        } catch (Exception e) {
            logger.error(ExceptionUtil.getExceptionMessage(e));
            return ServerRspUtil.error("文件上传错误，错误消息：" + e.getMessage());
        }
    }

    /**
     * *日期路径 // 年月日/时分 如果短时间内，上传并发量大，还可分得更细 秒 毫秒 等等
     * 
     * @return
     */
    private String getDatePath() {
        return DateUtils.format(new Date(), "yyyy") + "/" + DateUtils.format(new Date(), "MMdd") + "/" + DateUtils.format(new Date(), "HH") + "/";
    }

    public ResponseBodyVO zoneUpload(HttpServletRequest request, String contentType, FileZoneRecord fileZoneRecord) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> files = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entry : files.entrySet()) {
            MultipartFile multipartFile = entry.getValue();
            if (multipartFile.isEmpty()) {
                return ServerRspUtil.error("请选择文件");
            }
            String fileName = multipartFile.getOriginalFilename();
            if (fileName.equals("blob")) {
                fileName = fileZoneRecord.getZoneName();
            }
            // String contentType = multipartFile.getContentType();
            // if(contentType==null||!isFileter(contentType)){
            // return new ResponseBodyVO(ResponseBodyVOCode.FILENOTSUPPORT);
            // }
            String fileType = contentType.split("/")[0];
            Long size = multipartFile.getSize();
            logger.info(fileName + "-->" + size);
            try {
                Map<String, Object> map = Maps.newHashMap();
                synchronized (UUID.randomUUID()) {
                    // 查询数据库是否已经有了，有直接写入，没有，写入磁盘
                    FileZoneRecord fileZoneRecorddb = fileZoneRecordService.selByMD5AndZoneTotalMd5(fileZoneRecord.getZoneMd5(), fileZoneRecord.getZoneTotalMd5());
                    if (fileZoneRecorddb == null) {
                        String pathTypeDir = fileUploadConfig.getArchivesFilePath();
                        // 年月日/时分 如果短时间内，上传并发量大，还可分得更细 秒 毫秒 等等
                        // 写入临时目录，用总文件MD5值做文件夹
                        String localPath = "";
                        // 随机生成服务器本地路径
                        String fileSuffix = getFileSuffix(fileName);
                        // 分片文件MD5，如果前端没有计，算一下
                        String filemd5 = "";
                        if (fileZoneRecord.getZoneMd5() == null || fileZoneRecord.getZoneMd5().trim().length() == 0) {
                            filemd5 = DigestUtils.md5DigestAsHex(multipartFile.getInputStream());
                            fileZoneRecord.setZoneMd5(filemd5);
                        } else {
                            filemd5 = fileZoneRecord.getZoneMd5();
                        }

                        String serverFileName = filemd5 + fileSuffix + ".temp";
                        String fileRecordId = "";
                        FileRecord fileRecorddb = null;
                        synchronized (UUID.randomUUID().toString()) {
                            // 记录 已经文件存在，就不更新了，否则新增一条记录,合并时用
                            fileRecorddb = this.selByMD5AndUpType(fileZoneRecord.getZoneTotalMd5(), 2);
                        }
                        if (fileRecorddb == null) {
                            localPath = getUploadFolder() + fileUploadConfig.getLocalPath() + pathTypeDir + "temp/" + fileZoneRecord.getZoneTotalMd5();

                            FileRecord fileRecord = new FileRecord();
                            fileRecord.setFileSize(fileZoneRecord.getZoneTotalSize());
                            fileRecord.setFileType(fileType);
                            fileRecord.setMd5Value(fileZoneRecord.getZoneTotalMd5());
                            fileRecord.setOrgName(fileName);
                            fileRecord.setUploadType(2);
                            fileRecord.setServerLocalPath(localPath);
                            fileRecord.setStorageDate(getDateToYear(100));// 默认一百年
                            fileRecord.setIsZone(1);
                            fileRecord.setIsMerge(0);// 没有合并
                            fileRecord.setDownloadCount(0);
                            fileRecord.setUploadCount(1);
                            logger.info("fileRecord:" + fileRecord);
                            fileRecord.setZoneTotal(fileZoneRecord.getZoneTotalCount());
                            fileRecord.setZoneDate(new Date());
                            fileRecordId = saveFileRecord(request, fileRecord);
                        } else {
                            // 分片且已经合并过了，就不再往下执行，否则继续
                            if (fileRecorddb.getIsZone() == 1 && fileRecorddb.getIsMerge() == 1) {// 如果文件已经合并过了，直接返回
                                return ServerRspUtil.error("文件已经上传");
                            }
                            fileRecordId = fileRecorddb.getId();
                            localPath = fileRecorddb.getServerLocalPath();
                        }

                        // 将文件写入目录
                        FileHandleUtil.upload(multipartFile.getInputStream(), localPath, serverFileName);

                        // 记录分片文件
                        fileZoneRecord.setId(GlobalRecIdUtil.nextRecId() + "");
                        fileZoneRecord.setZoneMd5(filemd5);// 计算当前分片MD5
                        fileZoneRecord.setFileRecordId(fileRecordId);
                        fileZoneRecord.setZoneName(serverFileName);
                        fileZoneRecord.setZonePath(localPath);// 只存文件夹，合并时，直接读取这个文件所有文件
                        fileZoneRecord.setZoneRecordDate(new Date());
                        fileZoneRecord.setZoneSuffix(fileSuffix);
                        fileZoneRecordService.save(fileZoneRecord);
                        map.put("fileZone", fileZoneRecord);
                        map.put("isExist", false);// 不存在
                        map.put("zoneNowIndex", fileZoneRecord.getZoneNowIndex());

                    } else {
                        map.put("fileZone", fileZoneRecorddb);
                        map.put("isExist", true);// 存在
                        map.put("zoneNowIndex", fileZoneRecorddb.getZoneNowIndex());
                    }
                }
                return ServerRspUtil.success(map);
            } catch (Exception e) {
                logger.error(ExceptionUtil.getExceptionMessage(e));
                return ServerRspUtil.error("文件上传错误，错误消息：" + e.getMessage());
            }
        }
        return ServerRspUtil.error("文件上传失败");
    }

    private Map<String, Object> getResultFileMap(String id, String fileName, String networkPath) {
        Map<String, Object> resultmap = Maps.newHashMap();
        resultmap.put("fileId", id);
        resultmap.put("fileName", fileName);
        resultmap.put("networkPath", networkPath);
        return resultmap;
    }

    public ResponseBodyVO md5Check(String zoneTotalMd5, String zoneMd5, Integer checkType, String isFirst, String contentType, HttpServletRequest request) {
        if (checkType == 1) {// 校验文件
            // if(contentType==null||!isFileter(contentType)){
            // return new ResponseBodyVO(ResponseBodyVOCode.FILENOTSUPPORT);
            // }
            FileRecord fileRecordb = this.selByMD5AndUpType(zoneTotalMd5, 2);
            if (fileRecordb != null) {
                saveFileRecord(request, fileRecordb);
            }
            return fileRecordb != null && fileRecordb.getIsMerge() == 1 ?
                    ServerRspUtil.success("文件秒传", getResultFileMap(fileRecordb.getId(), fileRecordb.getOrgName(), fileRecordb.getNetworkPath()))
                    : ServerRspUtil.error("请选择文件上传");
        } else {
            FileZoneRecord fileZoneRecordb = null;
            List<String> zoneMd5List = Lists.newArrayList();
            if ("1".equals(isFirst)) {
                List<FileZoneRecord> list = fileZoneRecordService.selByTotalMd5(zoneTotalMd5);
                for (int i = 0; i < list.size(); i++) {
                    FileZoneRecord fz = list.get(i);
                    zoneMd5List.add(fz.getZoneMd5());
                    if (StringUtils.equals(fz.getZoneMd5(), zoneMd5)) {
                        fileZoneRecordb = fz;
                        // break;
                    }
                }
            } else {
                fileZoneRecordb = fileZoneRecordService.selByMD5AndZoneTotalMd5(zoneMd5, zoneTotalMd5);
            }
            return fileZoneRecordb != null ? ServerRspUtil.success(zoneMd5List) : ServerRspUtil.error("分片文件不存在，继续上传");
        }
    }

    public ResponseBodyVO mergeZoneFile(String totalmd5, HttpServletRequest request) {
        // 查询所有的分片文件
        if (totalmd5 != null && totalmd5.trim().length() > 0) {
            FileRecord fileRecordb = this.selByMD5AndUpType(totalmd5, 2);
            Map<String, Object> result = Maps.newHashMap();
            if (fileRecordb.getIsZone() == 1 && fileRecordb.getIsMerge() == 1) {
                String message = "文件已经上传成功了，文件路径：" + fileRecordb.getNetworkPath();
                result = getResultFileMap(fileRecordb.getId(), fileRecordb.getOrgName(), fileRecordb.getNetworkPath());
                return ServerRspUtil.success(message, result);
            }
            String fileType = fileRecordb.getFileType();
            List<FileZoneRecord> fileZoneRecords = fileZoneRecordService.selByTotalMd5(totalmd5);
            if (fileZoneRecords.size() > 0) {
                String path_date = getDatePath();
                String pathTypeDir = fileUploadConfig.getArchivesFilePath() + fileType + "/";
                String localPath = getUploadFolder() + fileUploadConfig.getLocalPath() + pathTypeDir + path_date;
                // 随机生成服务器本地路径
                String fileSuffix = getFileSuffix(fileRecordb.getOrgName());
                String serverFileName = GlobalRecIdUtil.nextRecId() + fileSuffix;
                String saticAccess = fileUploadConfig.getStaticAccessPath().replace("*", "");
                String netWorkPath = "/" + saticAccess + pathTypeDir + path_date + serverFileName;
                fileRecordb.setServerLocalName(serverFileName);
                fileRecordb.setServerLocalPath(localPath + serverFileName);
                fileRecordb.setNetworkPath(netWorkPath);
                FileOutputStream destTempfos = null;
                try {
                    String zonePath = fileZoneRecords.get(0).getZonePath();
                    File parentFileDir = new File(zonePath);// 得到上级文件夹
                    if (parentFileDir.isDirectory() && parentFileDir.listFiles() != null) {
                        FileHandleUtil.createDirIfNotExists(localPath);
                        File destTempFile = new File(localPath, serverFileName);
                        if (!destTempFile.exists()) {
                            // 先得到文件的上级目录，并创建上级目录，在创建文件,
                            destTempFile.getParentFile().mkdir();
                            try {
                                // 创建文件
                                destTempFile.createNewFile(); // 上级目录没有创建，这里会报错
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        logger.info("" + parentFileDir.listFiles().length);
                        List<String> ids = new ArrayList<>();
                        for (FileZoneRecord fileZoneR : fileZoneRecords) {
                            File partFile = new File(parentFileDir, fileZoneR.getZoneName());
                            destTempfos = new FileOutputStream(destTempFile, true);
                            // 遍历"所有分片文件"到"最终文件"中
                            FileUtils.copyFile(partFile, destTempfos);
                            destTempfos.close();
                            ids.add(fileZoneR.getId());
                        }

                        // 删除临时目录中的分片文件
                        String tempDir = getUploadFolder() + fileUploadConfig.getLocalPath() + fileUploadConfig.getArchivesFilePath() + "/temp/" + fileRecordb.getMd5Value();
                        FileHandleUtil.deleteFolder(tempDir);
                        fileZoneRecordService.removeByIds(ids);// 删除分片信息
                        fileRecordb.setZoneMergeDate(new Date());
                        fileRecordb.setIsMerge(1);// 更新已经合并
                        String fileId = saveFileRecord(request, fileRecordb);
                        result = getResultFileMap(fileId, fileRecordb.getOrgName(), netWorkPath);
                        // result.put("fileInfo", fileRecordb);
                        return ServerRspUtil.success(result);
                    } else {
                        fileRecordb = this.selByMD5AndUpType(totalmd5, 2);
                        result = getResultFileMap(fileRecordb.getId(), fileRecordb.getOrgName(), fileRecordb.getNetworkPath());
                        // result.put("fileInfo", fileRecordb);
                        return ServerRspUtil.success(result);
                    }
                } catch (Exception e) {
                    logger.error(ExceptionUtil.getExceptionMessage(e));
                    return ServerRspUtil.error("操作失败，原因：" + e.getMessage());
                } finally {
                    try {
                        if (destTempfos != null) {
                            destTempfos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return ServerRspUtil.error("合并错误");
    }

    //
    public FileRecord selByMD5AndUpType(String md5, Integer uploadType) {
        List<FileRecord> list = fileRecordMapper.selectList(md5, uploadType);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /***
     * 获取文件后缀
     * 
     * @param fileName
     * @return
     */
    private String getFileSuffix(String fileName) {
        if (fileName == null || fileName.length() == 0) {
            return "";
        }
        if (fileName.indexOf(".") > -1) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return "";
    }

    /**
     * 将当前时间往后推一年
     */
    public Date getDateToYear(Integer year) {
        if (year == null) {// 默认一百年
            year = 100;
        }
        // 获取时间加一年
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);// 设置起时间
        cal.add(Calendar.YEAR, year);// 增加year年
        return cal.getTime();
    }

    public String getUploadFolder() {
        return fileUploadConfig.getUploadFolder();
    }

    /**
     * 文件保存记录
     */
    public String saveFileRecord(HttpServletRequest request, FileRecord fileRecord) {
        fileRecord.setDelFlag(1);
        String device = request.getHeader("User-Agent");
        fileRecord.setUploadDevice(device);
        String ipAddr = IpUtils.getIpAddr(request);
        if (fileRecord.getUploadCount() == 1 && fileRecord.getId() == null) {
            fileRecord.setUploadIp(ipAddr);
            fileRecord.setId(GlobalRecIdUtil.nextRecId());
            fileRecord.setCreateBy(SecurityUtils.getUsername());
            fileRecordMapper.save(fileRecord);
        } else {
            fileRecord.setUpdateBy(SecurityUtils.getUsername());
            fileRecordMapper.update(fileRecord);
        }
        return fileRecord.getId();
    }

}
