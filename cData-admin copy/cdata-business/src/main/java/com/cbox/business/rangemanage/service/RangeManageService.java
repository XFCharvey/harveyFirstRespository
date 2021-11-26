package com.cbox.business.rangemanage.service;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import com.cbox.base.utils.ObjUtil;
import com.cbox.base.utils.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbox.base.constant.HttpStatus;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.service.BaseService;

import com.cbox.business.rangemanage.mapper.RangeManageMapper;


/**
 * @ClassName: RangeManageService
 * @Function: 片区管理
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class RangeManageService extends BaseService {

    @Autowired
	/*
	 *为了解决使用<property>标签注入对象过多的问题，Spring引入自动装配机制，简化开发者配置难度，降低xml文件配置大小。
	 */
    private RangeManageMapper rangeManageMapper;
    
	/** listRangeManage : 获取片区管理列表数据 **/
	/*
	 *List集合中的对象是一个Map对象,而这个Map对象的键是String类型,值是Object类型
	 */
	public List<Map<String, Object>> listRangeManage(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = rangeManageMapper.listRangeManage(param);

		return list;
	}

	/** getRangeManage : 获取指定id的片区管理数据 **/
	public ResponseBodyVO getRangeManage(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = rangeManageMapper.getRangeManage(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addRangeManage : 新增片区管理 **/
	public ResponseBodyVO addRangeManage(Map<String, Object> param) {

		List<Map<String, Object>> list = rangeManageMapper.listRangeManage(new HashMap<String, Object>());

		// Table:d_position_admin
		Map<String, Object> mapParam = new HashMap<String, Object>();
		String positionValue = StrUtil.getMapValue(param,"position_value");

		//检查是否有相同的片区编码
		boolean existFlag = false;
		if (ObjUtil.isNotNull(list)) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> tagParam = list.get(i);
				String tagFormCacheName = StrUtil.getMapValue(tagParam, "position_value");
				if (tagFormCacheName.equals(positionValue)) {
					existFlag = true;
					break;
				}
			}
		}

		if (existFlag) {
			return ServerRspUtil.formRspBodyVO(-1, "新增失败,存在相同片区编码");
		} else {
			mapParam.put("p_username", param.get("p_username"));
			mapParam.put("position_value", param.get("position_value"));
			mapParam.put("position_label", param.get("position_label"));
			mapParam.put("position_desc", param.get("position_desc"));
			int count = this.save( "d_position_admin", mapParam);

			return ServerRspUtil.formRspBodyVO(count, "新增片区管理失败");
		}
	}

	/** updateRangeManage : 修改片区管理 **/
	public ResponseBodyVO updateRangeManage(Map<String, Object> param) {

		// Table:d_position_admin
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("p_username", param.get("p_username"));
		mapParam.put("position_value", param.get("position_value"));
		mapParam.put("position_label", param.get("position_label"));
		mapParam.put("position_desc", param.get("position_desc"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.update(mapCondition, "d_position_admin", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改片区管理失败"); 
	}

	/** delRangeManage : 删除片区管理 **/
	public ResponseBodyVO delRangeManage(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断 

		// Table:d_position_admin
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.delete("d_position_admin",  mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除片区管理失败");
	}


}
