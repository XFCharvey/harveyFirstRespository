package com.cbox.business.info.infopraise.service;

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
import com.cbox.business.info.infopraise.mapper.InfoPraiseMapper;

/**
 * @ClassName: InfoPraiseService
 * @Function: 资讯点赞
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class InfoPraiseService extends BaseService {

	@Autowired
	private InfoPraiseMapper infoPraiseMapper;

	/** listInfoPraise : 获取资讯点赞列表数据 **/
	public List<Map<String, Object>> listInfoPraise(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = infoPraiseMapper.listInfoPraise(param);

		return list;
	}

	/** getInfoPraise : 获取指定id的资讯点赞数据 **/
	public ResponseBodyVO getInfoPraise(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = infoPraiseMapper.getInfoPraise(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addInfoPraise : 新增资讯点赞 **/
	public ResponseBodyVO addInfoPraise(Map<String, Object> param) {

		int count = 0;
		// Table:d_info_praise
		if (param.get("info_id") != null) {
			Map<String, Object> mapParam = new HashMap<String, Object>();
			mapParam.put("info_id", param.get("info_id"));
			mapParam.put("p_person", param.get("p_person"));
			mapParam.put("p_time", param.get("p_time"));
			count += this.saveNoRec("d_info_praise", mapParam, false);

			Map<String, Object> mapCondition = new HashMap<String, Object>();
			mapCondition.put("rec_id", param.get("info_id"));
			Map<String, Object> mapcountParam = new HashMap<String, Object>();
			mapcountParam.put("praise_num", 1);
			count += this.updateIncrement(mapCondition, "d_info", mapcountParam);

		} else {
			Map<String, Object> mapParam = new HashMap<String, Object>();
			mapParam.put("activity_id", param.get("activity_id"));
			mapParam.put("p_person", param.get("p_person"));
			mapParam.put("p_time", param.get("p_time"));
			count += this.saveNoRec("d_info_praise", mapParam, false);

			Map<String, Object> mapCondition = new HashMap<String, Object>();
			mapCondition.put("rec_id", param.get("activity_id"));
			Map<String, Object> mapcountParam = new HashMap<String, Object>();
			mapcountParam.put("praise_num", 1);
			count += this.updateIncrement(mapCondition, "d_activity", mapcountParam);

		}

		return ServerRspUtil.formRspBodyVO(count, "新增资讯点赞失败");
	}

	/** delInfoPraise : 删除资讯点赞 **/
	public ResponseBodyVO delInfoPraise(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断
		int count = 0;
		// Table:d_info_praise
		if (param.get("info_id") != null) {
			Map<String, Object> mapCondition = new HashMap<String, Object>();
			mapCondition.put("info_id", param.get("info_id"));
			mapCondition.put("p_person", param.get("p_person"));
			count += this.deleteEmpty("d_info_praise", mapCondition);

			Map<String, Object> mapupCondition = new HashMap<String, Object>();
			mapupCondition.put("rec_id", param.get("info_id"));
			Map<String, Object> mapcountParam = new HashMap<String, Object>();
			mapcountParam.put("praise_num", 1);
			count += this.updateDecrement(mapupCondition, "d_info", mapcountParam);
		} else {
			Map<String, Object> mapCondition = new HashMap<String, Object>();
			mapCondition.put("activity_id", param.get("activity_id"));
			mapCondition.put("p_person", param.get("p_person"));
			count += this.deleteEmpty("d_info_praise", mapCondition);

			Map<String, Object> mapupCondition = new HashMap<String, Object>();
			mapupCondition.put("rec_id", param.get("activity_id"));
			Map<String, Object> mapcountParam = new HashMap<String, Object>();
			mapcountParam.put("praise_num", 1);
			count += this.updateDecrement(mapupCondition, "d_activity", mapcountParam);
		}

		return ServerRspUtil.formRspBodyVO(count, "删除资讯点赞失败");
	}

}
