package com.cbox.business.info.infoviewhis.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbox.base.constant.HttpStatus;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.service.BaseService;
import com.cbox.base.utils.DateUtils;
import com.cbox.base.utils.ObjUtil;
import com.cbox.base.utils.SecurityUtils;
import com.cbox.base.utils.StrUtil;
import com.cbox.business.info.infoviewhis.mapper.InfoViewhisMapper;

/**
 * @ClassName: InfoViewhisService
 * @Function: 浏览历史
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class InfoViewhisService extends BaseService {

	@Autowired
	private InfoViewhisMapper infoViewhisMapper;

	/** listInfoViewhis : 获取浏览历史列表数据 **/
	public List<Map<String, Object>> listInfoViewhis(Map<String, Object> param) {
		super.appendUserInfo(param);
		String userName = SecurityUtils.getUsername();
		param.put("view_person", userName);

		List<Map<String, Object>> list = infoViewhisMapper.listInfoViewhis(param);

		return list;
	}

	/** getInfoViewhis : 获取指定id的浏览历史数据 **/
	public ResponseBodyVO getInfoViewhis(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = infoViewhisMapper.getInfoViewhis(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addInfoViewhis : 新增浏览历史 **/
	public ResponseBodyVO addInfoViewhis(Map<String, Object> param) {
		int count = 0;
		String nowTime = DateUtils.getTime();
		String userName = SecurityUtils.getUsername();
		Map<String, Object> mapselParam = new HashMap<String, Object>();
		mapselParam.put("info_id", param.get("info_id"));
		mapselParam.put("view_person", userName);

		Map<String, Object> mapViewhis = infoViewhisMapper.getUserInfoViewhis(mapselParam);
		if (ObjUtil.isNotNull(mapViewhis)) {
			int viewNum = StrUtil.getMapIntValue(mapViewhis, "view_num");
			Map<String, Object> mapViewHisUpParam = new HashMap<String, Object>();
			mapViewHisUpParam.put("info_id", param.get("info_id"));
			mapViewHisUpParam.put("view_person", userName);
			mapViewHisUpParam.put("view_time", nowTime);
			mapViewHisUpParam.put("view_num", viewNum + 1);
			mapViewHisUpParam.put("view_status", "1");
			Map<String, Object> mapCondition = new HashMap<String, Object>();
			mapCondition.put("info_id", param.get("info_id"));
			mapCondition.put("view_person", userName);
			count += this.updateNoRec(mapCondition, "d_info_viewhis", mapViewHisUpParam);
			// 自增浏览次数

		} else {
			// 新增一条
			Map<String, Object> mapParam = new HashMap<String, Object>();
			mapParam.put("info_id", param.get("info_id"));
			mapParam.put("view_person", userName);
			mapParam.put("view_num", "1");
			mapParam.put("view_time", nowTime);
			mapParam.put("view_status", "1");
			count += this.saveNoRec("d_info_viewhis", mapParam, false);
			Map<String, Object> mapinfoCondition = new HashMap<String, Object>();
			mapinfoCondition.put("rec_id", param.get("info_id"));
			Map<String, Object> mapcountParam = new HashMap<String, Object>();
			mapcountParam.put("read_num", 1);
			mapcountParam.put("play_num", 1);

			count += this.updateIncrement(mapinfoCondition, "d_info", mapcountParam);
		}

		// Table:d_info_viewhis
		return ServerRspUtil.formRspBodyVO(count, "新增或修改浏览历史失败");

	}

	/** delInfoViewhis : 删除浏览历史 **/
	public ResponseBodyVO delInfoViewhis(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断

		Map<String, Object> mapViewhisParam = new HashMap<String, Object>();
		mapViewhisParam.put("view_status", "2");
		// Table:d_info_viewhis
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("info_id", param.get("info_id"));
		mapCondition.put("view_person", param.get("view_person"));
		int count = this.updateNoRec(mapCondition, "d_info_viewhis", mapViewhisParam);

		return ServerRspUtil.formRspBodyVO(count, "删除浏览历史失败");
	}

	public ResponseBodyVO delallInfoViewhis(Map<String, Object> param) {
		// TODO Auto-generated method stub
		Map<String, Object> mapViewhisParam = new HashMap<String, Object>();
		mapViewhisParam.put("view_status", "2");
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("view_person", param.get("view_person"));
		int count = this.updateNoRec(mapCondition, "d_info_viewhis", mapViewhisParam);

		return ServerRspUtil.formRspBodyVO(count, "清空浏览历史失败");
	}

}
