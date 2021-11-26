package com.cbox.business.project.projectnodedelayhis.service;

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
import com.cbox.business.project.projectnode.service.ProjectNodeService;

import com.cbox.business.project.projectnodedelayhis.mapper.ProjectNodeDelayHisMapper;

/**
 * @ClassName: ProjectNodeDelayHisService
 * @Function: 项目节点延期
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class ProjectNodeDelayHisService extends BaseService {

	@Autowired
	private ProjectNodeDelayHisMapper projectNodeDelayHisMapper;

	@Autowired
	private ProjectNodeService ProjectNodeService;

	/** listProjectNodeDelayHis : 获取项目节点延期列表数据 **/
	public List<Map<String, Object>> listProjectNodeDelayHis(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = projectNodeDelayHisMapper.listProjectNodeDelayHis(param);

		return list;
	}

	/** getProjectNodeDelayHis : 获取指定id的项目节点延期数据 **/
	public ResponseBodyVO getProjectNodeDelayHis(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = projectNodeDelayHisMapper.getProjectNodeDelayHis(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addProjectNodeDelayHis : 新增项目节点延期 **/
	public ResponseBodyVO addProjectNodeDelayHis(Map<String, Object> param) {

		// Table:d_project_node_delayhis
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("node_id", param.get("node_id"));
		mapParam.put("delay_deadline", param.get("delay_deadline"));
		mapParam.put("node_deadline", param.get("node_deadline"));
		mapParam.put("delay_reason", param.get("delay_reason"));
		int count = this.save("d_project_node_delayhis", mapParam);

		// 新增项目节点延期时，更新项目节点表的延期时间 delay_deadline
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("node_id"));
		int updateCount = this.update(mapCondition, "d_project_node", mapParam);

		if (updateCount < 0) {
			return ServerRspUtil.formRspBodyVO(count, "项目节点延期失败,项目节点数据存在异常");
		} else {
			return ServerRspUtil.formRspBodyVO(count, "新增项目节点延期失败");
		}
	}

	/** updateProjectNodeDelayHis : 修改项目节点延期 **/
	public ResponseBodyVO updateProjectNodeDelayHis(Map<String, Object> param) {

		// Table:d_project_node_delayhis
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("node_id", param.get("node_id"));
		mapParam.put("delay_deadline", param.get("delay_deadline"));
		mapParam.put("node_deadline", param.get("node_deadline"));
		mapParam.put("delay_reason", param.get("delay_reason"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.update(mapCondition, "d_project_node_delayhis", mapParam);

		// 修改项目节点延期时，更新项目节点表的延期时间 delay_deadline
		Map<String, Object> mapNodeCondition = new HashMap<String, Object>();
		mapNodeCondition.put("rec_id", param.get("node_id"));
		int updateCount = this.update(mapCondition, "d_project_node", mapParam);

		if (updateCount < 0) {
			return ServerRspUtil.formRspBodyVO(updateCount, "项目节点延期失败,项目节点数据存在异常");
		} else {
			return ServerRspUtil.formRspBodyVO(count, "修改项目节点延期失败");
		}
	}

	/** delProjectNodeDelayHis : 删除项目节点延期 **/
	public ResponseBodyVO delProjectNodeDelayHis(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断

		// Table:d_project_node_delayhis
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.delete("d_project_node_delayhis", mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除项目节点延期失败");
	}

}
