package com.cbox.business.project.projectcomplain.service;

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
import com.cbox.base.utils.StrUtil;
import com.cbox.business.project.projectcomplain.mapper.ProjectcomplainMapper;

/**
 * @ClassName: ProjectcomplainService
 * @Function: 项目投诉/维修统计
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class ProjectcomplainService extends BaseService {

	@Autowired
	private ProjectcomplainMapper projectcomplainMapper;

	/** listProjectcomplain : 获取项目投诉/维修统计列表数据 **/
	public List<Map<String, Object>> listProjectcomplain(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = projectcomplainMapper.listProjectcomplain(param);
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> proComplain = list.get(i);
			String satisfyRate = StrUtil.getMapValue(proComplain, "satisfy_rate");
			if (StrUtil.isNull(satisfyRate)) {
				proComplain.put("satisfy_rate", 0);
			}
		}

		return list;
	}

	/** getProjectcomplain : 获取指定id的项目投诉/维修统计数据 **/
	public ResponseBodyVO getProjectcomplain(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = projectcomplainMapper.getProjectcomplain(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addProjectcomplain : 新增项目投诉/维修统计 **/
	public ResponseBodyVO addProjectcomplain(Map<String, Object> param) {

		// Table:d_project_complain
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("project_id", param.get("project_id"));
		mapParam.put("time_type", param.get("time_type"));
		mapParam.put("complain_time", param.get("complain_time"));
		mapParam.put("total_num", param.get("total_num"));
		mapParam.put("close_num", param.get("close_num"));
		mapParam.put("close_rate", param.get("close_rate"));
		mapParam.put("satisfy_rate", param.get("satisfy_rate"));
		mapParam.put("reply_out_num", param.get("reply_out_num"));
		mapParam.put("replay_out_rate", param.get("replay_out_rate"));
		mapParam.put("deal_out_num", param.get("deal_out_num"));
		mapParam.put("deal_out_rate", param.get("deal_out_rate"));
		int count = this.save("d_project_complain", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "新增项目投诉/维修统计失败");
	}

	/** updateProjectcomplain : 修改项目投诉/维修统计 **/
	public ResponseBodyVO updateProjectcomplain(Map<String, Object> param) {

		// Table:d_project_complain
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("project_id", param.get("project_id"));
		mapParam.put("time_type", param.get("time_type"));
		mapParam.put("complain_time", param.get("complain_time"));
		mapParam.put("total_num", param.get("total_num"));
		mapParam.put("close_num", param.get("close_num"));
		mapParam.put("close_rate", param.get("close_rate"));
		mapParam.put("satisfy_rate", param.get("satisfy_rate"));
		mapParam.put("reply_out_num", param.get("reply_out_num"));
		mapParam.put("replay_out_rate", param.get("replay_out_rate"));
		mapParam.put("deal_out_num", param.get("deal_out_num"));
		mapParam.put("deal_out_rate", param.get("deal_out_rate"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.update(mapCondition, "d_project_complain", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改项目投诉/维修统计失败");
	}

	/** delProjectcomplain : 删除项目投诉/维修统计 **/
	public ResponseBodyVO delProjectcomplain(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断

		// Table:d_project_complain
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.delete("d_project_complain", mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除项目投诉/维修统计失败");
	}

}
