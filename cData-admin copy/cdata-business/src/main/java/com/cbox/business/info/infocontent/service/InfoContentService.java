package com.cbox.business.info.infocontent.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbox.base.config.CboxConfig;
import com.cbox.base.constant.HttpStatus;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.service.BaseService;
import com.cbox.base.utils.DateUtils;
import com.cbox.base.utils.ObjUtil;
import com.cbox.base.utils.SecurityUtils;
import com.cbox.base.utils.StrUtil;
import com.cbox.business.activity.activity.mapper.ActivityMapper;
import com.cbox.business.check.checkinfo.mapper.CheckInfoMapper;
import com.cbox.business.compulcourses.mapper.CompulCoursesMapper;
import com.cbox.business.filemanage.entity.FileRecord;
import com.cbox.business.info.infocontent.mapper.InfoContentMapper;
import com.cbox.business.user.sysUser.mapper.UserMapper;

import net.coobird.thumbnailator.Thumbnails;
import ws.schild.jave.MultimediaInfo;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.VideoInfo;

import com.cbox.business.filemanage.service.FileRecordService;
import com.cbox.business.filemanage.entity.FileRecord;

/**
 * @ClassName: InfoContentService
 * @Function: 资讯信息
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class InfoContentService extends BaseService {

	@Autowired
	private InfoContentMapper infoContentMapper;
	@Autowired
	ActivityMapper activityMapper;

	@Autowired
	private CheckInfoMapper checkInfoMapper;

	@Autowired
	private CompulCoursesMapper compulCoursesMapper;

	@Autowired
	private FileRecordService fileRecordService;

	@Autowired
	UserMapper userMapper;

	/** listInfoContent : 获取资讯信息列表数据 **/
	public List<Map<String, Object>> listInfoContent(Map<String, Object> param, boolean typeFlag) {

		super.appendUserInfo(param);

		List<Map<String, Object>> list = infoContentMapper.listInfoContent(param);

		return list;
	}

	/** 查询当前登录用户发布的资讯 **/
	public List<Map<String, Object>> listInfoContentByPerson(Map<String, Object> param) {
		// TODO Auto-generated method stub
		String userName = SecurityUtils.getUsername();
		param.put("rec_person", userName);

		List<Map<String, Object>> list = infoContentMapper.listInfoContent(param);

		return list;
	}

	/** 发现推荐列表 **/
	public List<Map<String, Object>> listInfoRecommend(Map<String, Object> param) {
		super.appendUserInfo(param);

		String nowYear = DateUtils.getYear();
		List<Map<String, Object>> list = infoContentMapper.listInfoRecommend(param);
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> mapInfo = list.get(i);
			String isCourse = StrUtil.getMapValue(mapInfo, "is_course");
			if ("1".equals(isCourse)) {
				Map<String, Object> mapMustCourseCondition = new HashMap<String, Object>();
				mapMustCourseCondition.put("course_id", mapInfo.get("rec_id"));
				mapMustCourseCondition.put("plan_year", nowYear);
				Map<String, Object> mapMustCourse = compulCoursesMapper
						.getCompulCoursesByCourseID(mapMustCourseCondition);
				if (ObjUtil.isNotNull(mapMustCourse)) {
					mapInfo.put("courseType", "必修课");
					mapInfo.put("course_type", "1");
				} else {
					mapInfo.put("courseType", "非必修");
					mapInfo.put("course_type", "2");
				}

			} else {
				mapInfo.put("course_type", "0");
			}
		}

		return list;
	}

	/** getInfoContent : 获取指定id的资讯信息数据 **/
	public ResponseBodyVO getInfoContent(Map<String, Object> param) {
		super.appendUserInfo(param);
		String nowYear = DateUtils.getYear();

		Map<String, Object> mapResult = infoContentMapper.getInfoContent(param);
		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addInfoContent : 新增资讯信息 **/
	public ResponseBodyVO addInfoContent(Map<String, Object> param) {
		int count = 0;
		// Table:d_info
		String nowTime = DateUtils.getTime();
		String infoType = StrUtil.getMapValue(param, "info_type");
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("info_menuid", param.get("info_menuid"));
		mapParam.put("info_title", param.get("info_title"));
		mapParam.put("title_img", param.get("title_img"));
		mapParam.put("info_type", param.get("info_type"));
		mapParam.put("info_detail", param.get("info_detail"));
		mapParam.put("info_video", param.get("info_video"));//关联附件id
		if ("video".equals(infoType)) {// 视频
			String videoId = StrUtil.getNotNullStrValue(param.get("info_video"));
			addVideoInfo(mapParam, videoId);
		}
		mapParam.put("file_id", param.get("file_id"));//关联附件id
		mapParam.put("info_level", param.get("info_level"));
		mapParam.put("info_validity_term", param.get("info_validity_term"));
		mapParam.put("info_status", param.get("info_status"));
		mapParam.put("is_accusation,", param.get("is_accusation,"));
		mapParam.put("publish_unit", param.get("publish_unit"));
		mapParam.put("publish_person", param.get("publish_person"));
		mapParam.put("info_source", param.get("info_source"));
		mapParam.put("publish_time", nowTime);
		count += this.save("d_info", mapParam);
		String newinfoId = StrUtil.getMapValue(mapParam, "rec_id");
		Map<String, Object> mapCheckParam = new HashMap<String, Object>();
		mapCheckParam.put("dept_id", param.get("publish_unit"));
		mapCheckParam.put("check_type", "info");
		mapCheckParam.put("relation_id", newinfoId);
		mapCheckParam.put("check_title", "资讯：“" + param.get("info_title") + "” 等待审核");
		mapCheckParam.put("check_desc", param.get("publish_person") + "发布文章： “" + param.get("info_title") + "” 等待审核");
		mapCheckParam.put("publish_unitid", param.get("publish_unit"));
		mapCheckParam.put("publish_time", nowTime);
		count += this.save("d_check_info", mapCheckParam);

		return ServerRspUtil.formRspBodyVO(count, "新增资讯信息失败");
	}

	/**
	 * 视频信息处理，计算video_type和视频时长info_video_len
	 * 
	 * @param mapParam
	 * @param videoId
	 */
	public void addVideoInfo(Map<String, Object> mapParam, String videoId) {
		// 文件查询
		FileRecord fileRecord = fileRecordService.getById(videoId);
		File tempFile = new File(fileRecord.getServerLocalPath());
		int duration = 0;
		int videoHeight = 0;
		int videoWidth = 0;
		try {
			MultimediaObject multimediaObject = new MultimediaObject(tempFile);
			MultimediaInfo info = multimediaObject.getInfo();
			duration = (int) Math.ceil((double) info.getDuration() / 1000);
			VideoInfo video = info.getVideo();
			videoHeight = video.getSize().getHeight();
			videoWidth = video.getSize().getWidth();
		} catch (Exception e) {
			System.err.println("获取视频时长失败" + e);
		}
		// 视频类型：row-横类型，col-竖类型
		String video_type = videoHeight >= videoWidth ? "col" : "row";
		mapParam.put("video_type", video_type);
		mapParam.put("info_video_len", duration);
		mapParam.put("info_read_time", (duration / 2));// 学习时间为视频的一半
	}

	/** updateInfoContent : 修改资讯信息 **/
	public ResponseBodyVO updateInfoContent(Map<String, Object> param) {
		String nowTime = DateUtils.getTime();
		int count = 0;
		// Table:d_info
		String infoType = StrUtil.getMapValue(param, "info_type");
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("info_menuid", param.get("info_menuid"));
		mapParam.put("info_title", param.get("info_title"));
		mapParam.put("title_img", param.get("title_img"));
		mapParam.put("info_type", infoType);
		mapParam.put("file_id", param.get("file_id"));//关联附件id
		mapParam.put("info_detail", param.get("info_detail"));
		mapParam.put("info_video", param.get("info_video"));
		mapParam.put("video_type", param.get("video_type"));
		mapParam.put("info_video_len", param.get("info_video_len"));
		mapParam.put("info_level", param.get("info_level"));
		mapParam.put("info_validity_term", param.get("info_validity_term"));
		mapParam.put("info_read_time", param.get("info_read_time"));
		mapParam.put("is_accusation", param.get("is_accusation"));
		mapParam.put("info_status", param.get("info_status"));
		if (param.get("question_id") != null) {
			mapParam.put("question_id", param.get("question_id"));
			mapParam.put("is_course", param.get("is_course"));
		} else {
			Map<String, Object> checkCondition = new HashMap<String, Object>();
			checkCondition.put("check_type", "info");
			checkCondition.put("relation_id", param.get("rec_id"));
			checkCondition.put("check_status", "0");
			List<Map<String, Object>> listInfoCheckInfo = checkInfoMapper.listCheckInfo(checkCondition);

			if (ObjUtil.isNull(listInfoCheckInfo)) {
				Map<String, Object> mapCheckParam = new HashMap<String, Object>();
				mapCheckParam.put("dept_id", param.get("publish_unit"));
				mapCheckParam.put("check_type", "info");
				mapCheckParam.put("relation_id", param.get("rec_id"));
				mapCheckParam.put("check_title", "资讯：“" + param.get("info_title") + "” 重新上架，等待审核");
				mapCheckParam.put("check_desc",
						param.get("publish_person") + "重新上架文章： “" + param.get("info_title") + "” 等待审核");
				mapCheckParam.put("publish_unitid", param.get("publish_unit"));
				mapCheckParam.put("publish_time", nowTime);
				count += this.save("d_check_info", mapCheckParam);
				mapParam.put("info_status", '0');
			}

		}
		mapParam.put("course_integral", param.get("course_integral"));
		mapParam.put("publish_unit", param.get("publish_unit"));
		mapParam.put("publish_person", param.get("publish_person"));
		mapParam.put("info_source", param.get("info_source"));
		mapParam.put("publish_time", nowTime);
		if ("video".equals(infoType)) {// 视频
			String videoId = StrUtil.getNotNullStrValue(param.get("info_video"));
			addVideoInfo(mapParam, videoId);
		}
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		count += this.update(mapCondition, "d_info", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改资讯信息失败");
	}

	/** updateInfoContent : 修改资讯状态 **/
	public ResponseBodyVO updateInfoContentStatus(Map<String, Object> param) {

		// Table:d_info
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("info_status", param.get("info_status"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.update(mapCondition, "d_info", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改资讯信息失败");
	}

	/** delInfoContent : 删除资讯信息 **/
	public ResponseBodyVO delInfoContent(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断

		// Table:d_info
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.delete("d_info", mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除资讯信息失败");
	}

	/** 压缩资讯图片，这个接口用作单独调用手工压缩修改历史数据 **/
	public ResponseBodyVO compressTitleImg(Map<String, Object> param) {
		String filePath = CboxConfig.getUploadPath();

		List<Map<String, Object>> list = infoContentMapper.listInfoContent(param);
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> mapInfo = list.get(i);

			String titleImg = StrUtil.getMapValue(mapInfo, "title_img");

			this.compressFile(titleImg, filePath);
		}

		// 遍历处理活动的图片
		List<Map<String, Object>> listactivity = activityMapper.listActivity(param);
		for (int i = 0; i < listactivity.size(); i++) {
			Map<String, Object> mapInfo = listactivity.get(i);

			String titleImg = StrUtil.getMapValue(mapInfo, "act_img");

			this.compressFile(titleImg, filePath);
		}

		return ServerRspUtil.success();
	}

	private void compressFile(String img, String filePath) {

		if (StrUtil.isNull(img)) {
			return;
		}

		try {
			String fileFullPath = img.replace("/profile/upload", filePath);
			// System.out.println(fileFullPath);

			File srcFile = new File(fileFullPath);
			BufferedImage sourceImg = ImageIO.read(new FileInputStream(srcFile));
			if (sourceImg.getWidth() > 750) {
				// 按指定大小把图片进行缩放4:3（会遵循原图高宽比例）。750是一般默认的手机宽度
				Thumbnails.of(srcFile).size(750, 562).toFile(srcFile);// 遵循原图比例缩或放到750*某个高度
			}

			int iIndex = fileFullPath.lastIndexOf(".");
			String newFileName = fileFullPath.substring(0, iIndex) + "_small" + fileFullPath.substring(iIndex);
			int iSmallSize = 300;
			if (sourceImg.getWidth() > iSmallSize) {
				Thumbnails.of(srcFile).size(iSmallSize, (int) iSmallSize * 3 / 4).toFile(new File(newFileName));// 遵循原图比例缩或放到750*某个高度
			} else {
				// 否则大小不变
				Thumbnails.of(srcFile).size(sourceImg.getWidth(), sourceImg.getHeight()).toFile(new File(newFileName));
			}

			System.out.println("compress success:" + fileFullPath);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
