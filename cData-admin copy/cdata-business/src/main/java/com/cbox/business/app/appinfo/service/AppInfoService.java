package com.cbox.business.app.appinfo.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbox.base.constant.HttpStatus;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.service.BaseService;
import com.cbox.business.app.appinfo.mapper.AppInfoMapper;


/**
 * @ClassName: AppInfoService
 * @Function: app登录信息
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class AppInfoService extends BaseService {

    @Autowired
    private AppInfoMapper appInfoMapper;

	/** getAppInfo : 获取指定id的app登录信息数据 **/
	public ResponseBodyVO getAppInfo(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = appInfoMapper.getAppInfo(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addAppInfo : 新增app登录信息 **/
	public ResponseBodyVO addAppInfo(Map<String, Object> param) {

		// Table:d_app_info
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("user_name", param.get("user_name"));
		mapParam.put("osname", param.get("osname"));
		mapParam.put("osversion", param.get("osversion"));
		mapParam.put("token", param.get("token"));
		mapParam.put("clientid", param.get("clientid"));
		mapParam.put("appid", param.get("appid"));
		mapParam.put("appkey", param.get("appkey"));
		mapParam.put("version", param.get("version"));
		mapParam.put("brand", param.get("brand"));
		mapParam.put("model", param.get("model"));
		mapParam.put("uuid", param.get("uuid"));
		mapParam.put("status", param.get("status"));
		int count = this.save( "d_app_info", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "新增app登录信息失败");
	}

	/** updateAppInfo : 修改app登录信息 **/
	public ResponseBodyVO updateAppInfo(Map<String, Object> param) {

		// Table:d_app_info
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("user_name", param.get("user_name"));
		mapParam.put("osname", param.get("osname"));
		mapParam.put("osversion", param.get("osversion"));
		mapParam.put("token", param.get("token"));
		mapParam.put("clientid", param.get("clientid"));
		mapParam.put("appid", param.get("appid"));
		mapParam.put("appkey", param.get("appkey"));
		mapParam.put("version", param.get("version"));
		mapParam.put("brand", param.get("brand"));
		mapParam.put("model", param.get("model"));
		mapParam.put("uuid", param.get("uuid"));
		mapParam.put("status", param.get("status"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.update(mapCondition, "d_app_info", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改app登录信息失败"); 
	}

	/** delAppInfo : 删除app登录信息 **/
	public ResponseBodyVO delAppInfo(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断 

		// Table:d_app_info
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.delete("d_app_info",  mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除app登录信息失败");
	}


}
