package com.cbox.business.filemanage.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cbox.base.core.controller.BaseController;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.utils.ExceptionUtil;
import com.cbox.base.utils.file.FileUtils;
import com.cbox.business.filemanage.entity.FileRecord;
import com.cbox.business.filemanage.entity.FileZoneRecord;
import com.cbox.business.filemanage.service.FileRecordService;

/**
 * <p>
 * 文件上传记录 前端控制器
 * </p>
 * 
 */
@RestController
@RequestMapping("/file")
public class FileRecordController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(FileRecordService.class);

    @Autowired
    private FileRecordService fileRecordService;

    /**
     * * 根据ID查找
     */
    @GetMapping("/{id}")
    public ResponseBodyVO findById(@PathVariable("id") String id) {
        return ServerRspUtil.success(fileRecordService.getById(id));
    }
    /**
     * 2 多个id查找
     */
    @GetMapping("/list/{id}")
    public ResponseBodyVO list(@PathVariable("ids") String ids) {
        return ServerRspUtil.success(fileRecordService.listByIds(ids));
    }

    /***
     * 保存数据 id存在就更新
     */
    @PostMapping("/save")
    public ResponseBodyVO save(@RequestBody FileRecord fileRecord) {
        boolean b = fileRecordService.saveOrUpdate(fileRecord);
        return b ? ServerRspUtil.success() : ServerRspUtil.error("操作失败");
    }

    /***
     * 根据ID删除数据
     */
    @DeleteMapping("/delById/{id}")
    public ResponseBodyVO delById(@PathVariable("id") String id) {
        if (StringUtils.isBlank(id)) {
            return ServerRspUtil.error("缺少必填参数");
        }
        return fileRecordService.removeById(id);
    }

    /***
     * 根据多个ID删除(批量删除)
     */
    @DeleteMapping("/delByIds/{ids}")
    public ResponseBodyVO delByIds(@PathVariable("ids") String ids) {
        if (StringUtils.isBlank(ids)) {
            return ServerRspUtil.error("缺少必填参数");
        }
        return fileRecordService.removeByIds(Arrays.asList(ids.split(",")));
    }

    /************************** 文件上传操作 *********************************/
    /***
     * 单文件上传（<5M）
     */
    @PostMapping("/upload")
    public ResponseBodyVO upload(HttpServletRequest request, Integer uploadType) {
        ResponseBodyVO result = fileRecordService.upload(request, uploadType);
        return result;
    }

    /***
     * 大文件分片上传
     */
    @PostMapping("/zone/upload")
    public ResponseBodyVO zoneUpload(HttpServletRequest request, String contentType, FileZoneRecord fileZoneRecord) {
        return fileRecordService.zoneUpload(request, contentType, fileZoneRecord);
    }

    /**
     * @Description 校验MD5，传入分片MD5和总的MD5，校验当前分片
     * @param zoneTotalMd5 文件md5
     * @param zoneMd5 分片md5
     * @param checkType 校验类型 1：校验文件 2 校验分片
     * @param isFirst 是否第一次分片校验 isFirst
     * @param contentType 文件类型
     * @param request
     * @return
     */
    @PostMapping("/zone/upload/md5Check")
    public ResponseBodyVO md5Check(String zoneTotalMd5, String zoneMd5, Integer checkType, String isFirst, String contentType, HttpServletRequest request) {
        return fileRecordService.md5Check(zoneTotalMd5, zoneMd5, checkType, isFirst, contentType, request);
    }

    /**
     * 合并文件，前端所有分片上传完成时，发起请求，将所有的文件合并成一个完整的文件，并删除服务器分片文件 前端需要传入总文件的MD5值
     */
    @PostMapping("/zone/upload/merge/{totalmd5}")
    public ResponseBodyVO mergeZoneFile(@PathVariable("totalmd5") String totalmd5, HttpServletRequest request) {
        return fileRecordService.mergeZoneFile(totalmd5, request);
    }

    /***
     * @Description 文件下载
     * @Param
     * @return
     **/
    @GetMapping("/download/{fileId}")
    public ResponseBodyVO downloadFile(HttpServletRequest request, HttpServletResponse response, @PathVariable("fileId") String fileId) throws UnsupportedEncodingException {
        FileRecord fileRecorddb = fileRecordService.getById(fileId);
        String filePath = fileRecorddb.getServerLocalPath();// 设置文件名，根据业务需要替换成要下载的文件名
        String fileOriginalName = fileRecorddb.getOrgName();
        if (filePath != null) {
            // 设置文件路径
            File file = new File(filePath);
            if (file.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition", "attachment;fileName=" + fileOriginalName);
                response.setContentType("multipart/form-data;charset=UTF-8");
                // 获得浏览器代理信息
                final String userAgent = request.getHeader("USER-AGENT");
                // 判断浏览器代理并分别设置响应给浏览器的编码格式
                String finalFileName = null;
                if (StringUtils.contains(userAgent, "MSIE") || StringUtils.contains(userAgent, "Trident")) {// IE浏览器
                    finalFileName = URLEncoder.encode(fileOriginalName, "UTF8");
                } else if (StringUtils.contains(userAgent, "Mozilla")) {// google,火狐浏览器
                    finalFileName = new String(fileOriginalName.getBytes(), "ISO8859-1");
                } else {
                    finalFileName = URLEncoder.encode(fileOriginalName, "UTF8");// 其他浏览器
                }
                // 设置文件名
                response.setHeader("Content-Disposition", "attachment;fileName=" + finalFileName);
                response.addHeader("Content-Length", "" + file.length());

                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        os.flush();
                        i = bis.read(buffer);
                    }
                    // System.out.println("下载成功");
                    // fileRecordService.recordDownloadLog(fileId,fileRecorddb);
                } catch (Exception e) {
                    logger.error(ExceptionUtil.getExceptionMessage(e));
                } finally {
                    FileUtils.closeIO(bis, fis);
                }
                return ServerRspUtil.success();
            }
        }
        return ServerRspUtil.error("下载错误");
    }

}
