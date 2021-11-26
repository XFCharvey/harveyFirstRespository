package com.cbox.common.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cbox.base.config.CboxConfig;
import com.cbox.base.constant.Constants;
import com.cbox.base.core.domain.AjaxResult;
import com.cbox.base.utils.StrUtil;
import com.cbox.base.utils.StringUtils;
import com.cbox.base.utils.file.FileUploadUtils;
import com.cbox.base.utils.file.FileUtils;
import com.cbox.framework.config.ServerConfig;

import net.coobird.thumbnailator.Thumbnails;

/**
 * 通用请求处理
 * 
 * @author cbox
 */
@RestController
public class CommonController {
    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private ServerConfig serverConfig;

    /**
     * 通用下载请求
     * 
     * @param fileName 文件名称
     * @param delete 是否删除
     */
    @GetMapping("/common/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request) {
        try {
            if (!FileUtils.isValidFilename(fileName)) {
                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = CboxConfig.getDownloadPath() + fileName;

            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, realFileName));
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete) {
                FileUtils.deleteFile(filePath);
            }
        } catch (Exception e) {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 通用模板下载请求
     * 
     * @param fileName 模板文件名称
     */
    @GetMapping("/common/download/template")
    public void templateDownload(String fileName, HttpServletResponse response, HttpServletRequest request) {
        try {
            if (!FileUtils.isValidFilename(fileName)) {
                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            }
            String filePath = CboxConfig.getTemplatePath() + fileName;

            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, fileName));
            FileUtils.writeBytes(filePath, response.getOutputStream());

        } catch (Exception e) {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 通用上传请求
     */
    @PostMapping("/common/upload")
    public AjaxResult uploadFile(MultipartFile file) throws Exception {
        try {
            // 上传文件路径
            String filePath = CboxConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("fileName", fileName);
            ajax.put("url", url);
            return ajax;
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 通用上传请求
     */
    @PostMapping("/common/upload/titleimg")
    public AjaxResult uploadTitleImg(MultipartFile file, String smallSize) throws Exception {
        try {
            // 上传文件路径
            String filePath = CboxConfig.getUploadPath();

            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);

            String fileFullPath = fileName.replace("/profile/upload", filePath);

            File srcFile = new File(fileFullPath);
            BufferedImage sourceImg = ImageIO.read(new FileInputStream(srcFile));
            if (sourceImg.getWidth() > 750) {
                // 按指定大小把图片进行缩放4:3（会遵循原图高宽比例）。750是一般默认的手机宽度
                Thumbnails.of(srcFile).size(750, 562).toFile(srcFile);// 遵循原图比例缩或放到750*某个高度
            }

            int iSmallSize = StrUtil.getNotNullIntValue(smallSize);
            if (iSmallSize == 0) {
                iSmallSize = 375;
            }

            // 自动生成一个小图片 _small
            int iIndex = fileFullPath.lastIndexOf(".");
            String newFileName = fileFullPath.substring(0, iIndex) + "_small" + fileFullPath.substring(iIndex);
            if (sourceImg.getWidth() > iSmallSize) {
                Thumbnails.of(srcFile).size(iSmallSize, (int) iSmallSize * 3 / 4).toFile(new File(newFileName));// 遵循原图比例缩或放到750*某个高度
            } else {
                // 否则大小不变
                Thumbnails.of(srcFile).size(sourceImg.getWidth(), sourceImg.getHeight()).toFile(new File(newFileName));
            }

            String url = serverConfig.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("fileName", fileName);
            ajax.put("url", url);
            return ajax;
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 本地资源通用下载
     */
    @GetMapping("/common/download/resource")
    public void resourceDownload(String name, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 本地资源路径
        String localPath = CboxConfig.getProfile();
        // 数据库资源地址
        String downloadPath = localPath + StringUtils.substringAfter(name, Constants.RESOURCE_PREFIX);
        // 下载名称
        String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, downloadName));
        FileUtils.writeBytes(downloadPath, response.getOutputStream());
    }
    
    /**
     * 获取处理结果
     */
    @ResponseBody
    @RequestMapping(value = "/common/showTiff", method = RequestMethod.POST)
    public AjaxResult showTiff(@RequestParam("filename") String filename) {
        String filePath = filename;
        File file = new File(filePath);
        if (!file.exists()) {
            return AjaxResult.error("数据文件不存在");
        }
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            // Jackson会对byte[]会编码为Base64字符串，所以前端需要解码
            result.put("fileContent", FileUtils.readFileToByteArray(file));
        } catch (IOException e) {
        }
        result.put("filelLength", file.length());

        return AjaxResult.success(result);
    }
}
