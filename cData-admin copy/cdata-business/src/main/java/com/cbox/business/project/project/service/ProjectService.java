package com.cbox.business.project.project.service;

import java.util.ArrayList;
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
import com.cbox.base.utils.ObjUtil;
import com.cbox.base.utils.StrUtil;
import com.cbox.business.project.project.mapper.ProjectMapper;
import com.cbox.business.project.projectnode.mapper.ProjectNodeMapper;
import com.cbox.business.user.sysUser.mapper.UserMapper;

/**
 * @ClassName: ProjectService
 * @Function: 项目表
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class ProjectService extends BaseService {

	@Autowired
	private ProjectMapper projectMapper;

	@Autowired
	private ProjectNodeMapper projectNodeMapper;

	@Autowired
	private UserMapper userMapper;

	/** listProject : 获取项目表列表数据 **/
	public List<Map<String, Object>> listProject(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = projectMapper.listProject(param);

		// 查询该项目节点数据
		Map<String, Object> mapProjectNodeCondition = new HashMap<String, Object>();
		List<Map<String, Object>> listProjectNodes = projectNodeMapper.listProjectNode(mapProjectNodeCondition);
		List<Map<String, Object>> listUser = userMapper.listUser(mapProjectNodeCondition);

		Map<String, List<Map<String, Object>>> mapListProjectNode = ObjUtil.transList(listProjectNodes, "project_id");
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> mapProject = list.get(i);
			String projectId = StrUtil.getMapValue(mapProject, "rec_id");

			// 查询该项目节点数据
			List<Map<String, Object>> listProjectNode = mapListProjectNode.get(projectId);
			if (ObjUtil.isNotNull(listProjectNode)) {
				mapProject.put("lately_node", listProjectNode.get(0));
			}

			/** 获取指定id的项目里所有用户的阿隆与官位关注数 **/
			Map<String, Object> mapProjectFollowCondition = new HashMap<String, Object>();
			mapProjectFollowCondition.put("project_id", projectId);
			Map<String, Object> mapProjectFollowTotal = projectMapper.getProjectFollowTotal(mapProjectFollowCondition);
			mapProject.put("official_follow_total",
					StrUtil.getMapValue(mapProjectFollowTotal, "official_follow_total"));
			mapProject.put("along_follow_total", StrUtil.getMapValue(mapProjectFollowTotal, "along_follow_total"));
			mapProject.put("customer_total", StrUtil.getMapValue(mapProjectFollowTotal, "customer_total"));

			List<String> rPersonList = new ArrayList<String>();
			String repairPerson = StrUtil.getMapValue(mapProject, "repair_person");
			if (StrUtil.isNotNull(repairPerson)) {
				String[] rPersons = repairPerson.split(",");
				for (int j = 0; j < rPersons.length; j++) {
					for (int z = 0; z < listUser.size(); z++) {
						Map<String, Object> user = listUser.get(z);
						String userName = StrUtil.getMapValue(user, "user_name");
						if (userName.equals(rPersons[j])) {
							String nickName = StrUtil.getMapValue(user, "nick_name");
							if (StrUtil.isNull(nickName)) {
								nickName = "-";
							}
							rPersonList.add(nickName);
						}
					}
				}
			}
			mapProject.put("rPerson_name", rPersonList);

			String gxAdmin = StrUtil.getMapValue(mapProject, "gx_admin");
			String nickName = null;
			if (StrUtil.isNotNull(gxAdmin)) {
				for (int z = 0; z < listUser.size(); z++) {
					Map<String, Object> user = listUser.get(z);
					String userName = StrUtil.getMapValue(user, "user_name");
					if (userName.equals(gxAdmin)) {
						nickName = StrUtil.getMapValue(user, "nick_name");
						if (StrUtil.isNull(nickName)) {
							nickName = "-";
						}
					}
				}
			}
			mapProject.put("gxAdmin_name", nickName);
		}

		return list;
	}

	/** getProject : 获取指定id的项目表数据 **/
	public ResponseBodyVO getProject(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = projectMapper.getProject(param);
		// 查询项目节点条件
		Map<String, Object> projectNodeCondition = new HashMap<String, Object>();
		projectNodeCondition.put("project_id", param.get("rec_id"));
		projectNodeCondition.put("limit", 5);
		// 查询该项目节点数据
		List<Map<String, Object>> listProjectNode = projectNodeMapper.listProjectNode(projectNodeCondition);
		// 往map添加节点数据
		mapResult.put("projectNode", listProjectNode);

		/** 获取指定id的项目里所有用户的阿隆与官位关注数 **/
		Map<String, Object> mapProjectFollowCondition = new HashMap<String, Object>();
		mapProjectFollowCondition.put("project_id", param.get("rec_id"));
		Map<String, Object> mapProjectFollowTotal = projectMapper.getProjectFollowTotal(mapProjectFollowCondition);
		mapResult.put("official_follow_total", StrUtil.getMapValue(mapProjectFollowTotal, "official_follow_total"));
		mapResult.put("along_follow_total", StrUtil.getMapValue(mapProjectFollowTotal, "along_follow_total"));
		mapResult.put("customer_total", StrUtil.getMapValue(mapProjectFollowTotal, "customer_total"));

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addProject : 新增项目表 **/
	public ResponseBodyVO addProject(Map<String, Object> param) {

		// Table:d_project
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("project_name", param.get("project_name"));
		mapParam.put("project_type", param.get("project_type"));
		mapParam.put("project_person", param.get("project_person"));
		mapParam.put("project_img", param.get("project_img"));
		mapParam.put("node_num", param.get("node_num"));
		mapParam.put("node_finish_num", param.get("node_finish_num"));
		mapParam.put("official_micro_num", param.get("official_micro_num"));
		mapParam.put("along_num", param.get("along_num"));
		mapParam.put("redLine_out_defect", param.get("redLine_out_defect"));
		mapParam.put("redLine_in_defect", param.get("redLine_in_defect"));
		mapParam.put("difference_num", param.get("difference_num"));
		mapParam.put("risk_num", param.get("risk_num"));
		mapParam.put("sale_num", param.get("sale_num"));
		mapParam.put("info_num", param.get("info_num"));
		mapParam.put("project_detail", param.get("project_detail"));
		mapParam.put("begin_time", param.get("begin_time"));
		mapParam.put("end_time", param.get("end_time"));
		mapParam.put("project_status", param.get("project_status"));
		int count = this.save("d_project", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "新增项目表失败");
	}

	/** updateProject : 修改项目表 **/
	public ResponseBodyVO updateProject(Map<String, Object> param) {

		// Table:d_project
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("project_name", param.get("project_name"));
		mapParam.put("project_type", param.get("project_type"));
		mapParam.put("project_person", param.get("project_person"));

		mapParam.put("repair_person", param.get("repair_person"));
		mapParam.put("gx_admin", param.get("gx_admin"));
		mapParam.put("project_code", param.get("project_code"));

		mapParam.put("project_range", param.get("project_range"));
		mapParam.put("project_img", param.get("project_img"));
		mapParam.put("node_num", param.get("node_num"));
		mapParam.put("node_finish_num", param.get("node_finish_num"));
		mapParam.put("official_micro_num", param.get("official_micro_num"));
		mapParam.put("along_num", param.get("along_num"));
		mapParam.put("redLine_out_defect", param.get("redLine_out_defect"));
		mapParam.put("redLine_in_defect", param.get("redLine_in_defect"));
		mapParam.put("difference_num", param.get("difference_num"));
		mapParam.put("risk_num", param.get("risk_num"));
		mapParam.put("sale_num", param.get("sale_num"));
		mapParam.put("info_num", param.get("info_num"));
		mapParam.put("project_detail", param.get("project_detail"));
		mapParam.put("begin_time", param.get("begin_time"));
		mapParam.put("end_time", param.get("end_time"));
		mapParam.put("project_status", param.get("project_status"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.update(mapCondition, "d_project", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改项目表失败");
	}

	/** delProject : 删除项目表 **/
	public ResponseBodyVO delProject(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断

		// Table:d_project
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.delete("d_project", mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除项目表失败");
	}

}