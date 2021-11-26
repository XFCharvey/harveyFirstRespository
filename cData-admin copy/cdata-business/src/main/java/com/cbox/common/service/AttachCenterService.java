package com.cbox.common.service;

import java.io.File;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cbox.base.config.CboxConfig;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.service.BaseService;
import com.cbox.base.utils.StrUtil;
import com.cbox.base.utils.id.GlobalRecIdUtil;
import com.google.common.collect.Maps;

/**
 * 
 * @ClassName: AttachCenterService
 * @Function:附件/文件相关的上传下载服务
 * 
 * @author cbox
 * @date 2019年9月29日 上午10:48:22
 * @version 1.0
 */
@Service
@Transactional
public class AttachCenterService extends BaseService {

	private String fileAttachTable = "d_sys_file_attachment";

	/**
	 * 文件上传。考虑到页面缓存更新的问题，所以如果是修改的情况，则先删除原有文件，然后再新增文件的方式来处理。
	 * 
	 * @param request
	 * @param upfile
	 * @return
	 */
	public ResponseBodyVO uploadAdd(HttpServletRequest request, @RequestParam("file") MultipartFile upfile) {

		if (upfile == null) {
			ServerRspUtil.error("文件上传失败请重试！");
		}

		// 判断当前上传的文件是否在库中已经存在
		boolean bFileExist = false;
		String imgFileName = request.getParameter("imgFileName");
		if (StrUtil.isNotNull(imgFileName) && !"undefined".equals(imgFileName)) {
			Map<String, Object> selectParams = new HashMap<String, Object>();
			selectParams.put("file_name", imgFileName);
			Map<String, Object> imgBeanMap = this.queryOne(fileAttachTable, selectParams);

			// 删除原来的上传文件
			if (imgBeanMap != null) {
				bFileExist = true;

				String filePath = (String) imgBeanMap.get("file_path");
				String targetFilePath = CboxConfig.getUploadPath() + filePath + imgFileName;
				FileUtils.deleteQuietly(new File(targetFilePath));
			}
		}

		String rec_id = GlobalRecIdUtil.nextRecId();// 文件名唯一id

		/* Step1：上传文件 */
		long fileSize = upfile.getSize();
		String originalName = upfile.getOriginalFilename();
		String fileSuffix = this.getSuffix(originalName);
		String targetFileName = rec_id + "." + this.getSuffix(originalName);// 保存的文件名
		String savePath = CboxConfig.getDatePath();
		try {
			String targetFilePath = CboxConfig.getUploadPath() + savePath + targetFileName;
			System.out.println("targetFilePath:" + targetFilePath);
			File targetFile = new File(targetFilePath);

			if (!targetFile.getParentFile().exists()) {
				targetFile.getParentFile().mkdirs();
			}
			upfile.transferTo(targetFile);
		} catch (Exception e) {
			e.printStackTrace();
		}

		/* Step2：保存入库 */
		int count = 1;
		if (!bFileExist) {
			Map<String, Object> updateParams = Maps.newHashMap();
			updateParams.put("org_file_name", originalName);
			updateParams.put("file_name", targetFileName);
			updateParams.put("file_path", savePath);
			// updateParams.put("file_type", "");
			updateParams.put("file_size", fileSize);
			updateParams.put("file_suffix", fileSuffix);
			// updateParams.put("img_height", "");
			// updateParams.put("img_width", "");
			// updateParams.put("av_times", "");
			// updateParams.put("av_image", "");
			updateParams.put("rec_id", rec_id);
			count = this.save(fileAttachTable, updateParams);
		} else {
			// 更新文件名称和文件路径
			Map<String, Object> updateParams = Maps.newHashMap();
			updateParams.put("file_name", targetFileName);
			updateParams.put("file_path", savePath);
			Map<String, Object> conditionMap = new HashMap<String, Object>();
			conditionMap.put("file_name", imgFileName);
			count = this.update(conditionMap, fileAttachTable, updateParams);
		}
		if (count > 0) {
			/* Step3：返回信息 */
			Map<String, Object> result = Maps.newHashMap();
			result.put("fileId", rec_id);// fileId
			result.put("fileName", originalName);// 原始文件名
			result.put("url", savePath);// 存储URL
			Float size = Float.parseFloat(String.valueOf(fileSize)) / 1024;
			BigDecimal b = new BigDecimal(size);
			size = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();// 2表示2位 ROUND_HALF_UP表明四舍五入，
			result.put("fileSize", size);// 文件大小（单位Kb）
			// result.put("fileSuffix", fileSuffix);// 扩展名
			return ServerRspUtil.success("文件上传成功", result);
		} else {
			return ServerRspUtil.error("文件上传失败请重试！");
		}

	}

	/**
	 * 上传文件删除
	 * 
	 * @param fileId
	 * @param delType delType=true:删除附件表中的记录、删除对应路径下的文件
	 * @return
	 */
	public ResponseBodyVO uploadDelete(String fileId, boolean delType) {
		int count = 0;
		// boolean bFileExist = false;
		if (StrUtil.isNotNull(fileId)) {
			Map<String, Object> selectParams = Maps.newHashMap();
			if (fileId.indexOf(".") > 0) {
				selectParams.put("file_name", fileId);
			} else {
				selectParams.put("rec_id", fileId);
			}
			Map<String, Object> fileMap = this.queryOne(fileAttachTable, selectParams);

			// 删除原来的上传文件
			if (delType && fileMap != null) {
				// bFileExist = true;
				String filePath = (String) fileMap.get("file_path");
				String targetFilePath = CboxConfig.getUploadPath() + filePath + fileMap.get("file_name");
				FileUtils.deleteQuietly(new File(targetFilePath));
			}
			// 删除文件记录
			count = this.delete(fileAttachTable, selectParams);
		}

		if (count > 0) {
			return ServerRspUtil.success("文件删除成功");
		} else {
			return ServerRspUtil.error("文件删除失败！");
		}
	}

	public String getFilePath(String fileName) {
		// 必传参数校验
		if (StrUtil.isNull(fileName)) {
			return "";
		}

		// 获取数据 Table:d_sys_file_attachment
		List<String> listColumn0 = new ArrayList<String>();
		listColumn0.add("file_path");
		listColumn0.add("file_name");
		Map<String, Object> mapQueryParam0 = new HashMap<String, Object>();
		mapQueryParam0.put("file_name", fileName);
		Map<String, Object> mapQueryResult0 = this.queryOne(fileAttachTable, mapQueryParam0, listColumn0);

		String filePath = "";
		if (null != mapQueryResult0 && !mapQueryResult0.isEmpty()) {
			filePath = String.valueOf(mapQueryResult0.get("file_path"));
		}

		return filePath;
	}

	public Map<String, Object> getFileAttach(String file) {
		// 必传参数校验
		if (StrUtil.isNull(file)) {
			return null;
		}

		// 获取数据 Table:d_sys_file_attachment
		List<String> listColumn0 = new ArrayList<String>();
		listColumn0.add("file_path");
		listColumn0.add("file_name");
		listColumn0.add("org_file_name");
		listColumn0.add("file_suffix");
		// listColumn0.add("file_type");
		// listColumn0.add("file_size");
		// listColumn0.add("img_height");
		// listColumn0.add("img_width");
		// listColumn0.add("av_times");
		// listColumn0.add("av_image");
		Map<String, Object> qryParam = new HashMap<String, Object>();
		if (file.indexOf(".") > 0) {
			qryParam.put("file_name", file);
		} else {
			qryParam.put("rec_id", file);
		}
		Map<String, Object> mapQueryResult0 = this.queryOne(fileAttachTable, qryParam, listColumn0);

		return mapQueryResult0;
	}

	/* 获取文件的后缀名 */
	private String getSuffix(String fileName) {
		return fileName.substring(fileName.lastIndexOf(".") + 1);
	}

	/**
	 * 插入一条附件记录，并返回rec_id
	 * 
	 * @param fileUrl 以/分隔的文件及路径
	 * @param fileSize
	 * @return
	 */
	public String addAttachRecord(String fileUrl, String fileSize) {

		int index = fileUrl.lastIndexOf("/");

		if (index == -1) {
			//fileUrl格式不对
			return "";
		}

		String rec_id = GlobalRecIdUtil.nextRecId();// 文件名唯一id
		String savePath = fileUrl.substring(0, index);
		String originalName = fileUrl.substring(index + 1);
		String fileSuffix = this.getSuffix(originalName);
		String targetFileName = originalName;

		Map<String, Object> updateParams = Maps.newHashMap();
		updateParams.put("org_file_name", originalName);
		updateParams.put("file_name", targetFileName);
		updateParams.put("file_path", savePath);
		updateParams.put("file_size", fileSize);
		updateParams.put("file_suffix", fileSuffix);
		updateParams.put("rec_id", rec_id);
		int count = this.save(fileAttachTable, updateParams);

		return rec_id;
	}

}
