package com.cbox.business.app.apploginlog.service;

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
import com.cbox.base.utils.SecurityUtils;
import com.cbox.business.app.apploginlog.mapper.AppLogMapper;


/**
 * @ClassName: AppLoginLogService
 * @Function: app登录日志
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class AppLogService extends BaseService {

    @Autowired
    private AppLogMapper appLogMapper;
    

	/** listAppLoginLog : 获取app登录日志列表数据 **/
	public List<Map<String, Object>> listAppLoginLog(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = appLogMapper.listAppLoginLog(param);

		return list;
	}

	/** getAppLoginLog : 获取指定id的app登录日志数据 **/
	public ResponseBodyVO getAppLoginLog(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = appLogMapper.getAppLoginLog(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addAppLoginLog : 新增app登录日志 **/
	public ResponseBodyVO addAppLoginLog(Map<String, Object> param) {

        String nowTime = DateUtils.getTime();
        String userName = SecurityUtils.getUsername();

		// Table:d_user_applogin_log
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("user_name", param.get("user_name"));
		mapParam.put("login_time", param.get("login_time"));
        int count = this.saveNoRec("d_user_applogin_log", mapParam, false);

		return ServerRspUtil.formRspBodyVO(count, "新增app登录日志失败");
	}

	/** updateAppLoginLog : 修改app登录日志 **/
	public ResponseBodyVO updateAppLoginLog(Map<String, Object> param) {

		// Table:d_user_applogin_log
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("user_name", param.get("user_name"));
		mapParam.put("login_time", param.get("login_time"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
        int count = this.updateNoRec(mapCondition, "d_user_applogin_log", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改app登录日志失败"); 
	}

	/** delAppLoginLog : 删除app登录日志 **/
	public ResponseBodyVO delAppLoginLog(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断 

		// Table:d_user_applogin_log
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
        int count = this.deleteEmpty("d_user_applogin_log", mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除app登录日志失败");
	}


}
