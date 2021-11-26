package com.cbox.business.user.userworklog.service;

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
import com.cbox.business.user.userworklog.mapper.UserWorklogMapper;

/**
 * @ClassName: UserWorklogService
 * @Function: 员工工作日志
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class UserWorklogService extends BaseService {

	@Autowired
	private UserWorklogMapper userWorklogMapper;

	/** listUserWorklogByDeptId : 获取指定年月当前登陆用户团队及下级团队员工工作日志量列表数据 **/
	public List<Map<String, Object>> listUserWorklogByDeptId(Map<String, Object> param) {
		super.appendUserInfo(param);
		Long dept_id = SecurityUtils.getLoginUser().getUser().getDeptId();

		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("dept_id", dept_id);
		mapParam.put("year_month", param.get("year_month"));
		mapParam.put("workdate", param.get("workdate"));

		List<Map<String, Object>> list = userWorklogMapper.listUserWorklogByDeptId(mapParam);

		return list;
	}

	/** listUserWorklogCountOfDay : 获取指定年月当前登陆用户团队及下级团队员工工作日志每日数量列表数据 **/
	public List<Map<String, Object>> listUserWorklogCountOfDay(Map<String, Object> param) {
		super.appendUserInfo(param);
		Long dept_id = SecurityUtils.getLoginUser().getUser().getDeptId();

		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("dept_id", dept_id);
		mapParam.put("year_month", param.get("year_month"));

		List<Map<String, Object>> list = userWorklogMapper.listUserWorklogCountOfDay(mapParam);

		return list;
	}

	/** listUserWorklog : 获取员工工作日志列表数据 **/
	public List<Map<String, Object>> listUserWorklog(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = userWorklogMapper.listUserWorklog(param);

		return list;
	}

	/** getUserWorklog : 获取指定id的员工工作日志数据 **/
	public ResponseBodyVO getUserWorklog(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = userWorklogMapper.getUserWorklog(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addUserWorklog : 新增员工工作日志 **/
	public ResponseBodyVO addUserWorklog(Map<String, Object> param) {
		String userName = SecurityUtils.getUsername();
		String recTime = DateUtils.getTime();
		// Table:d_user_worklog
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("log_date", param.get("log_date"));
		mapParam.put("log_person", userName);
		List<Map<String, Object>> list = userWorklogMapper.listUserWorklog(mapParam);// 查询是否存在这天的日志
		mapParam.put("log_title", param.get("log_title"));
		mapParam.put("log_content", param.get("log_content"));
		mapParam.put("rec_time", recTime);
		// 如果存在则进行修改操作,没人每天只增加一条日志
		if (ObjUtil.isNotNull(list)) {
			mapParam.put("rec_id", list.get(0).get("rec_id"));
			return this.updateUserWorklog(mapParam);
		} else {
			int count = this.saveNoRec("d_user_worklog", mapParam, false);
			return ServerRspUtil.formRspBodyVO(count, "新增员工工作日志失败");
		}
	}

	/** updateUserWorklog : 修改员工工作日志 **/
	public ResponseBodyVO updateUserWorklog(Map<String, Object> param) {
		String recTime = DateUtils.getTime();
		// Table:d_user_worklog
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("log_title", param.get("log_title"));
		mapParam.put("log_content", param.get("log_content"));
		mapParam.put("log_date", param.get("log_date"));
		mapParam.put("rec_time", recTime);
		mapParam.put("log_person", param.get("log_person"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.updateNoRec(mapCondition, "d_user_worklog", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改员工工作日志失败");
	}

	/** delUserWorklog : 删除员工工作日志 **/
	public ResponseBodyVO delUserWorklog(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断

		// Table:d_user_worklog
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
        int count = this.deleteEmpty("d_user_worklog", mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除员工工作日志失败");
	}

}
