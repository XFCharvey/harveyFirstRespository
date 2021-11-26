package com.cbox.business.activity.activity.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbox.base.config.CboxConfig;
import com.cbox.base.constant.Constants;
import com.cbox.base.constant.HttpStatus;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.service.BaseService;
import com.cbox.base.utils.DateUtils;
import com.cbox.base.utils.SecurityUtils;
import com.cbox.base.utils.StrUtil;
import com.cbox.base.utils.StringUtils;
import com.cbox.business.activity.activity.mapper.ActivityMapper;
import com.cbox.business.activity.activityenroll.mapper.ActivityEnrollMapper;
import com.cbox.framework.config.ServerConfig;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * @ClassName: ActivityService
 * @Function: 线下活动表
 * 
 * @author cbox
 * @version 1.0
 */

@Service
@Transactional
public class ActivityService extends BaseService {

	@Autowired
	ActivityMapper activityMapper;

	@Autowired
	private ServerConfig serverConfig;

	@Autowired
	ActivityEnrollMapper activityEnrollMapper;

	/** listActivity : 普通获取列表数据 **/
	public List<Map<String, Object>> listActivity(Map<String, Object> param, boolean typeFlag) {
		// TODO Auto-generated method stub
		super.appendUserInfo(param);

		List<Map<String, Object>> listactivity = activityMapper.listActivity(param);

		return listactivity;
	}

	/** getActivity : 获取指定id的数据 **/
	public ResponseBodyVO getActivity(Map<String, Object> param) {
		// super.appendUserInfo(param);
		// 取出当前登录用户的所属部门及所有上级部门，内容为用逗号分隔的字符串

		Map<String, Object> mapResult = activityMapper.getActivity(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/**
	 * addActivity : 新增表数据
	 * 
	 * @throws WriterException
	 * @throws IOException
	 **/
	public ResponseBodyVO addActivity(Map<String, Object> param) throws IOException, WriterException {
		// Table:d_activity
        String nowTime = DateUtils.getTime();
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("act_name", param.get("act_name"));
		mapParam.put("project_id", param.get("project_id"));
		mapParam.put("act_attr", param.get("act_attr"));
		mapParam.put("act_img", param.get("act_img"));
		mapParam.put("act_type", param.get("act_type"));
		mapParam.put("act_detail", param.get("act_detail"));
		mapParam.put("begin_time", param.get("begin_time"));
		mapParam.put("live_person", param.get("live_person"));
		mapParam.put("act_range_dept", param.get("act_range_dept"));
		mapParam.put("coop_unit", param.get("coop_unit"));// 合作单位
		mapParam.put("act_range", param.get("act_range"));// 适用范围
		mapParam.put("act_quota", param.get("act_quota"));// 设置报名数
		mapParam.put("invitation_num", param.get("invitation_num"));
		mapParam.put("end_time", param.get("end_time"));
		String sign_endtime = StrUtil.getMapValue(param, "sign_endtime");
		if (StrUtil.isNull(sign_endtime)) {
			mapParam.put("sign_endtime", param.get("begin_time"));
		} else {
			mapParam.put("sign_endtime", param.get("sign_endtime"));
		}
		mapParam.put("act_range_dept", param.get("act_range_dept"));
		mapParam.put("dept_id", param.get("dept_id"));
		mapParam.put("auto_check", param.get("auto_check"));

		int count = this.save("d_activity", mapParam);

        String deptName = SecurityUtils.getLoginUser().getUser().getDept().getDeptName();
        String parentDept = SecurityUtils.getLoginUser().getUser().getDept().getParentId().toString();
        String deptID = SecurityUtils.getLoginUser().getUser().getDept().getDeptId().toString();
        String newActID = StrUtil.getMapValue(mapParam, "rec_id");
        Map<String, Object> mapCheckParam = new HashMap<String, Object>();
        if ("0".equals(parentDept)) {
            mapCheckParam.put("dept_id", deptID);
        } else {
            mapCheckParam.put("dept_id", parentDept);
        }
        mapCheckParam.put("check_type", "act");
        mapCheckParam.put("relation_id", newActID);
        mapCheckParam.put("check_title", deptName + "创建新的活动待审核");
        mapCheckParam.put("check_desc", deptName + "#" + "创建新的活动");
        mapCheckParam.put("publish_unitid", param.get("dept_id"));
        mapCheckParam.put("publish_time", nowTime);
        count += this.save("d_check_info", mapCheckParam);
        String newCheckId = StrUtil.getMapValue(mapCheckParam, "rec_id");

        // 发布审核消息通知给单位
        Map<String, Object> noticeParam = new HashMap<String, Object>();
        noticeParam.put("notice_title", "活动创建等待审核");
        noticeParam.put("notice_type", "check");
        noticeParam.put("relation_id", newCheckId);
        noticeParam.put("notice_headimg", param.get("act_img"));
        noticeParam.put("notice_content", "活动：“" + param.get("act_name") + "”等待审核处理");
        noticeParam.put("notice_time", nowTime);
        if ("0".equals(parentDept)) {
            noticeParam.put("notice_dept", deptID);
        } else {
            noticeParam.put("notice_dept", parentDept);
        }
        count += this.saveNoRec("d_notice", noticeParam, false);

		// 根据rec_id生成活动签到二维码
		String recId = StrUtil.getMapValue(mapParam, "rec_id");
		generateActQRCode(recId);

		return ServerRspUtil.formRspBodyVO(count, "新增线下活动表失败");
	}

	/**
	 * updateActivity : 修改活动表数据
	 * 
	 * @throws WriterException
	 * @throws IOException
	 **/
	public ResponseBodyVO updateActivity(Map<String, Object> param) throws IOException, WriterException {

		// Table:d_activity
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("act_name", param.get("act_name"));
		mapParam.put("project_id", param.get("project_id"));
		mapParam.put("act_attr", param.get("act_attr"));
		mapParam.put("act_img", param.get("act_img"));
		mapParam.put("act_type", param.get("act_type"));
		mapParam.put("act_detail", param.get("act_detail"));
		mapParam.put("act_report", param.get("act_report"));
		mapParam.put("act_report_img", param.get("act_report_img"));
		mapParam.put("begin_time", param.get("begin_time"));
		mapParam.put("end_time", param.get("end_time"));
		mapParam.put("sign_endtime", param.get("sign_endtime"));
		mapParam.put("dept_id", param.get("dept_id"));
		mapParam.put("act_status", param.get("act_status"));
		mapParam.put("auto_check", param.get("auto_check"));
		mapParam.put("act_range_dept", param.get("act_range_dept"));
		mapParam.put("live_person", param.get("live_person"));
		mapParam.put("act_range_dept", param.get("act_range_dept"));
		mapParam.put("invitation_num", param.get("invitation_num"));
		mapParam.put("coop_unit", param.get("coop_unit"));// 合作单位
		mapParam.put("act_range", param.get("act_range"));// 适用范围
		mapParam.put("question_id", param.get("question_id"));// 关联问卷
		mapParam.put("act_quota", param.get("act_quota"));// 设置报名数
		mapParam.put("act_join_num", param.get("act_join_num"));// 活动参与用户数
		mapParam.put("ques_num", param.get("ques_num"));// 问卷回访数

		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.update(mapCondition, "d_activity", mapParam);

		// 未存在二维码时,根据rec_id生成活动签到二维码
		String actCode = StrUtil.getMapValue(param, "act_code");
		if (StrUtil.isNull(actCode)) {
			String recId = StrUtil.getMapValue(param, "rec_id");
			generateActQRCode(recId);
		}

		return ServerRspUtil.formRspBodyVO(count, "修改活动表失败");
	}

	/** delCommodity : 删除活动表数据 **/
	public ResponseBodyVO delActivity(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断

		// Table:d_activity
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.delete("d_activity", mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除活动表失败");
	}

	/** 生成二维码并更新活动签到二维码 **/
	public void generateActQRCode(String redId) throws IOException, WriterException {

		String baseDir = CboxConfig.getUploadPath();// 上传文件路径
		String recId = redId;
		String actFileName = recId + ".png";// 二维码内容
		String fileName = DateUtils.datePath() + "/" + actFileName;// 指定存放目录
		File desc = getAbsoluteFile(baseDir, fileName);// 不存在则创建目录
		String pathFileName = getPathFileName(baseDir, fileName);// 获取相对路径

		String codeContent = "http://cdata.simplesty.com/#/?activityId=" + recId;// 二维码内容
		String wirtePath = desc.getPath();// 二维码生成路径
		generateQRCodeImage(codeContent, 350, 350, wirtePath);// 生成二维码宽高：350

		// 更新活动二维码
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", recId);
		Map<String, Object> refreshCondition = new HashMap<String, Object>();
		refreshCondition.put("act_code", pathFileName);
		this.update(mapCondition, "d_activity", refreshCondition);

	}

	/**
	 * 生成二维码
	 * 
	 * @param text     二维码内容
	 * @param width    宽度
	 * @param height   高度
	 * @param filePath 文件路径
	 * @throws IOException
	 * @throws WriterException
	 */
	public static void generateQRCodeImage(String text, int width, int height, String filePath)
			throws IOException, WriterException {
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
		Path path = FileSystems.getDefault().getPath(filePath);
		MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
	}

	private static final File getAbsoluteFile(String uploadDir, String fileName) throws IOException {
		File desc = new File(uploadDir + File.separator + fileName);

		if (!desc.getParentFile().exists()) {
			desc.getParentFile().mkdirs();
		}
		if (!desc.exists()) {
			desc.createNewFile();
		}
		return desc;
	}

	private static final String getPathFileName(String uploadDir, String fileName) throws IOException {
		int dirLastIndex = CboxConfig.getProfile().length() + 1;
		String currentDir = StringUtils.substring(uploadDir, dirLastIndex);
		String pathFileName = Constants.RESOURCE_PREFIX + "/" + currentDir + "/" + fileName;
		return pathFileName;
	}

}
