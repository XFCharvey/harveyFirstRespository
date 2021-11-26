package com.cbox.common.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cbox.base.config.CboxConfig;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.utils.StrUtil;
import com.cbox.base.utils.file.FileUtils;
import com.cbox.common.service.AttachCenterService;

@Controller
@RequestMapping("/common")
public class AttachCenterController {

    @Autowired
    AttachCenterService attachCenterService;

    /** 上传 */
    @ResponseBody
    @RequestMapping(value = "uploadFile", method = { RequestMethod.POST })
    public ResponseBodyVO upload(HttpServletRequest request, HttpServletResponse response, @RequestParam("file") MultipartFile upfile) {

        ResponseBodyVO resp = attachCenterService.uploadAdd(request, upfile);
        return resp;
    }

    /** 删除文件 */
    @ResponseBody
    @RequestMapping(value = "deleteFile", method = { RequestMethod.POST })
    public ResponseBodyVO delete(@RequestBody Map<String, Object> param) {
        String fileId = StrUtil.getMapValue(param, "fileId");
        boolean delType = Boolean.parseBoolean(StrUtil.getMapValue(param, "delType", "false"));
        ResponseBodyVO resp = attachCenterService.uploadDelete(fileId, delType);
        return resp;
    }

    /**
     * show:以读取数据流的方式，返回图片信息供前端展示
     *
     * @date: 2019年9月29日 下午4:17:55
     * @author cbox
     * @param request fileName-存储的文件路径+名称
     * @param response 通过OutputStream写会给客户端
     */
    @RequestMapping("/showFileImg")
    public void show(HttpServletRequest request, HttpServletResponse response) {
        InputStream inStream = null;
        OutputStream outStream = null;
        try {
            String fileId = (String) request.getParameter("fileId");

            Map<String, Object> mapAttach = attachCenterService.getFileAttach(fileId);
            if (!CollectionUtils.isEmpty(mapAttach)) {
                String filePath = String.valueOf(mapAttach.get("file_path"));
                String fileName = String.valueOf(mapAttach.get("file_name"));

                String fileurl = CboxConfig.getUploadPath() + filePath + fileName;

                File filePic = new File(fileurl);
                inStream = new FileInputStream(filePic);
                int fileLength = inStream.available(); // 得到文件大小
                byte data[] = new byte[fileLength];
                inStream.read(data); // 读数据

                response.setContentType("application/octet-stream"); // 设置返回的文件类型
                outStream = response.getOutputStream(); // 得到向客户端输出二进制数据的对象
                outStream.write(data); // 输出数据
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FileUtils.closeIO(inStream, outStream);
        }
    }

    /** 下载文件 */
    @GetMapping("/downloadFile")
    public void download(String fileId, HttpServletRequest request, HttpServletResponse response) {
        InputStream inStream = null;
        OutputStream outStream = null;
        
       System.out.println("run");
        try {
            // String fileId = request.getParameter("fileId");

            Map<String, Object> mapAttach = attachCenterService.getFileAttach(fileId);
            if (!CollectionUtils.isEmpty(mapAttach)) {
                String filePath = String.valueOf(mapAttach.get("file_path"));
                String fileName = String.valueOf(mapAttach.get("file_name"));
                String fileOriginalName = String.valueOf(mapAttach.get("org_file_name"));

                String fileurl = CboxConfig.getUploadPath() + filePath + fileName;

                File file = new File(fileurl);
                // 将文件读入文件流
                inStream = new FileInputStream(fileurl);
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
                // 设置HTTP响应头
                response.reset();// 重置 响应头
                response.setContentType("application/x-download");// 告知浏览器下载文件，而不是直接打开，浏览器默认为打开
                response.addHeader("Content-Disposition", "attachment;filename=\"" + finalFileName + "\"");// 下载文件的名称
                response.addHeader("Content-Length", "" + file.length());
                // 循环取出流中的数据
                byte[] b = new byte[1024];
                int len;
                outStream = response.getOutputStream();
                while ((len = inStream.read(b)) > 0) {
                    outStream.write(b, 0, len);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FileUtils.closeIO(inStream, outStream);
        }
    }

    /** 查询文件信息 */
    @ResponseBody
    @RequestMapping(value = "info", method = { RequestMethod.POST })
    public ResponseBodyVO info(HttpServletRequest request, HttpServletResponse response) {

        String fileName = (String) request.getParameter("fileName");
        Map<String, Object> mapAttach = attachCenterService.getFileAttach(fileName);

        Map<String, Object> map2 = new HashMap<String, Object>();

        ResponseBodyVO resp = null;
        if (null != mapAttach && !mapAttach.isEmpty()) {
            map2.put("src", fileName);// 存储的文件名
            map2.put("orgFileName", mapAttach.get("org_file_name"));// 原始文件名
            map2.put("fileSuffix", mapAttach.get("file_suffix"));// 扩展名
            resp = ServerRspUtil.success("获取附件信息成功", map2);
        } else {
            resp = ServerRspUtil.error("为获取到附件信息");
        }

        return resp;
    }

}
