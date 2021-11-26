package com.cbox.business.project.argwbind.service;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbox.base.annotation.DataScope;
import com.cbox.base.constant.HttpStatus;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.service.BaseService;
import com.cbox.base.utils.SecurityUtils;

import com.cbox.business.project.argwbind.mapper.ArgwBindMapper;


/**
 * @ClassName: ArgwBindService
 * @Function: 阿融官微绑定
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class ArgwBindService extends BaseService {

    @Autowired
    private ArgwBindMapper argwBindMapper;
    
	/** listArgwBind : 获取阿融官微绑定列表数据 **/
	public List<Map<String, Object>> listArgwBind(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = argwBindMapper.listArgwBind(param);

		return list;
	}

	/** getArgwBind : 获取指定id的阿融官微绑定数据 **/
	public ResponseBodyVO getArgwBind(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = argwBindMapper.getArgwBind(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addArgwBind : 新增阿融官微绑定 **/
	public ResponseBodyVO addArgwBind(Map<String, Object> param) {

		// Table:d_argw_bind
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("project_id", param.get("project_id"));
		mapParam.put("type", param.get("type"));
		mapParam.put("file_id", param.get("file_id"));
		int count = this.save( "d_argw_bind", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "新增阿融官微绑定失败");
	}
	
	/** addArgwBindBatch : 批量阿融官微绑定 **/
	public ResponseBodyVO addArgwBindBatch(Map<String, Object> param) {

		// Table:d_argw_bind
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("project_id", param.get("project_id"));
		mapParam.put("type", param.get("type"));
		mapParam.put("file_id", param.get("file_id"));
//		int count = this.save( "d_argw_bind", mapParam);

		return ServerRspUtil.formRspBodyVO(-1, "待确定批量导入规则");
	}

	/** updateArgwBind : 修改阿融官微绑定 **/
	public ResponseBodyVO updateArgwBind(Map<String, Object> param) {

		// Table:d_argw_bind
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("project_id", param.get("project_id"));
		mapParam.put("type", param.get("type"));
		mapParam.put("file_id", param.get("file_id"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.update(mapCondition, "d_argw_bind", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改阿融官微绑定失败"); 
	}

	/** delArgwBind : 删除阿融官微绑定 **/
	public ResponseBodyVO delArgwBind(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断 

		// Table:d_argw_bind
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.delete("d_argw_bind",  mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除阿融官微绑定失败");
	}


}
