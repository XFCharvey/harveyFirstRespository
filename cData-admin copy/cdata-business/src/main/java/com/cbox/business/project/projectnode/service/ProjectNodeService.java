package com.cbox.business.project.projectnode.service;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbox.base.constant.HttpStatus;
import com.cbox.base.core.domain.AjaxResult;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.service.BaseService;
import com.cbox.base.utils.ObjUtil;
import com.cbox.base.utils.StrUtil;
import com.cbox.base.utils.poi.ExcelUtil;
import com.cbox.business.project.projectnode.bean.NodeVO;
import com.cbox.business.project.projectnode.mapper.ProjectNodeMapper;
import com.cbox.common.service.AttachFileService;

/**
 * @ClassName: ProjectNodeService
 * @Function: 项目节点
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class ProjectNodeService extends BaseService {

	@Autowired
	private ProjectNodeMapper projectNodeMapper;

	@Autowired
	private AttachFileService attachFileService;

	/** listProjectNode : 获取项目节点列表数据 **/
	public List<Map<String, Object>> listProjectNode(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = projectNodeMapper.listProjectNode(param);

		return list;
	}
	// Download template

	/** getDownloadTemplate： 获取项目批量节点导入下载模板 **/
	public AjaxResult getDownloadTemplate() {
		// 空数据
		List<NodeVO> listNodeVO = new ArrayList<NodeVO>();
		NodeVO nodeVo = new NodeVO();
		listNodeVO.add(nodeVo);

		// 解析excel文件
		ExcelUtil<NodeVO> util = new ExcelUtil<NodeVO>(NodeVO.class);
		String sheetName = "项目节点批量导入模板";
		AjaxResult result = util.exportExcel(listNodeVO, sheetName);

		return result;
	}

	/** getProjectNode : 获取指定id的项目节点数据 **/
	public ResponseBodyVO getProjectNode(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = projectNodeMapper.getProjectNode(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addProjectNode : 新增项目节点 **/
	public ResponseBodyVO addProjectNode(Map<String, Object> param) {
		int count = 0;
		// Table:d_project_node
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("project_id", param.get("project_id"));
		mapParam.put("node_name", param.get("node_name"));
		mapParam.put("node_deadline", param.get("node_deadline"));
		mapParam.put("node_status", param.get("node_status"));
		mapParam.put("finish_time", param.get("finish_time"));
		mapParam.put("finish_detail", param.get("finish_detail"));
		mapParam.put("node_file", param.get("node_file"));
		count += this.save("d_project_node", mapParam);

		// 更新最新项目结点数
		count += updateProjectNodeNum(mapParam);

		return ServerRspUtil.formRspBodyVO(count, "新增项目节点失败");
	}

	/**
	 * importProjectNode : 批量导入项目节点数据
	 * 
	 * @throws Exception
	 **/
	public AjaxResult importProjectNode(Map<String, Object> param) throws Exception {
		// Table:d_project_node
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("node_file", param.get("node_file"));

		// 获取上传文件地址
		String fileId = StrUtil.getMapValue(param, "node_file");
		String fileName = attachFileService.getFileRealPath(fileId);
		File file = new File(fileName);

		// 解析excel文件
		ExcelUtil<NodeVO> util = new ExcelUtil<NodeVO>(NodeVO.class);
		List<NodeVO> importNodeList = util.importExcel(file);
		int count = 0;

		// 批量导入项目节点
		if (ObjUtil.isNotNull(importNodeList)) {
			for (int i = 0; i < importNodeList.size(); i++) {
				NodeVO nodeVo = importNodeList.get(i);
				Map<String, Object> nodeVoParam = new HashMap<String, Object>();
				nodeVoParam.put("project_id", param.get("project_id"));
				nodeVoParam.put("node_name", nodeVo.getNode_name());
				nodeVoParam.put("node_deadline", nodeVo.getNode_deadline());
				int iCount = this.save("d_project_node", nodeVoParam);
				count = count + iCount;
			}
		}

		// 更新最新项目结点数
		updateProjectNodeNum(param);

		// 生成返回信息
		int code = HttpStatus.SUCCESS;
		String msg = "成功导入" + count + "条项目节点";
		AjaxResult ajaxResult = new AjaxResult(code, msg);

		return ajaxResult;
	}

	/** updateProjectNodeNum : 更新项目存在的节点数 **/
	public int updateProjectNodeNum(Map<String, Object> param) {

		// Table:d_project_node
		// 查询当前新增的节点的项目的所有节点
		Map<String, Object> projectNodeCondition = new HashMap<String, Object>();
		projectNodeCondition.put("project_id", param.get("project_id"));
		List<Map<String, Object>> listProjectNode = projectNodeMapper.listProjectNode(projectNodeCondition);
		// 最新的项目节点数
		Map<String, Object> projectParam = new HashMap<String, Object>();
		if (ObjUtil.isNotNull(listProjectNode)) {
			projectParam.put("node_num", listProjectNode.size());
		} else {
			projectParam.put("node_num", 0);
		}
		// 更新项目最新的节点数
		Map<String, Object> projectCondition = new HashMap<String, Object>();
		projectCondition.put("rec_id", param.get("project_id"));
		int count = this.update(projectCondition, "d_project", projectParam);

		return count;
	}

	/**
	 * updateProjectNode : 修改项目节点
	 * 
	 * @throws ParseException
	 **/
	public ResponseBodyVO updateProjectNode(Map<String, Object> param) throws ParseException {

		// Table:d_project_node
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("project_id", param.get("project_id"));
		mapParam.put("node_name", param.get("node_name"));
		String nodeDeadline = StrUtil.getMapValue(param, "node_deadline");
		String delayDeadline = StrUtil.getMapValue(param, "delay_deadline");
		String finishTime = StrUtil.getMapValue(param, "finish_time");
		mapParam.put("node_deadline", param.get("node_deadline"));
		mapParam.put("delay_deadline", param.get("delay_deadline"));
		mapParam.put("finish_time", param.get("finish_time"));
		String nodeStatus = getNodeStatus(nodeDeadline, delayDeadline, finishTime);
		mapParam.put("node_status", nodeStatus);
		mapParam.put("finish_detail", param.get("finish_detail"));
		mapParam.put("node_file", param.get("node_file"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.update(mapCondition, "d_project_node", mapParam);

		// 更新项目完成节点数
		Map<String, Object> mapConditionParam = new HashMap<String, Object>();
		mapConditionParam.put("project_id", param.get("project_id"));
		List<Map<String, Object>> listProNode = projectNodeMapper.listProjectNode(param);
		updateFinishNum(mapParam, listProNode);

		return ServerRspUtil.formRspBodyVO(count, "修改项目节点失败");
	}

	/** 更新项目完成节点数 **/
	public void updateFinishNum(Map<String, Object> param, List<Map<String, Object>> listProNode) {

		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("project_id"));

		int finishNum = 0;
		for (int i = 0; i < listProNode.size(); i++) {
			Map<String, Object> proNode = listProNode.get(i);
			String nodeStatus2 = StrUtil.getMapValue(proNode, "node_status");
			if (nodeStatus2.equals("4")) {
				finishNum = finishNum + 1;
			} else if (nodeStatus2.equals("5")) {
				finishNum = finishNum + 1;
			} else if (nodeStatus2.equals("6")) {
				finishNum = finishNum + 1;
			}
		}
		Map<String, Object> mapProNodeParam = new HashMap<String, Object>();
		mapProNodeParam.put("node_finish_num", finishNum);
		this.update(mapCondition, "d_project", mapProNodeParam);
	}

	/** 根据完成期限、延期完成期限、完成时间，确定节点状态 **/
	public String getNodeStatus(String nodeDeadline, String delayDeadline, String finishTime) throws ParseException {
		String nodeStatus = "0";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		// 存在完成时间
		if (StrUtil.isNotNull(finishTime)) {
			Date dateFinish = formatter.parse(finishTime);
			// 正常期限
			if (StrUtil.isNotNull(nodeDeadline)) {
				Date dateDeadline = formatter.parse(nodeDeadline);
				// 小于完成期限 nodeStatus 4-正常完成
				if (dateFinish.getTime() <= dateDeadline.getTime()) {
					nodeStatus = "4";
				} else if (dateFinish.getTime() > dateDeadline.getTime()) {
					// 5-延期完成
					nodeStatus = "5";
				}
			}

			// 设置了延期期限
			if (StrUtil.isNotNull(delayDeadline)) {
				Date dateDelay = formatter.parse(delayDeadline);
				// 小于完成延期期限 nodeStatus 4-正常完成
				if (dateFinish.getTime() <= dateDelay.getTime()) {
					nodeStatus = "4";
				} else if (dateFinish.getTime() > dateDelay.getTime()) {
					// 6-超时延期完成
					nodeStatus = "6";
				}
			}
		}
		return nodeStatus;
	}

	/** delProjectNode : 删除项目节点 **/
	public ResponseBodyVO delProjectNode(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断

		// Table:d_project_node
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.delete("d_project_node", mapCondition);

		// 更新最新项目结点数
		count += updateProjectNodeNum(param);

		return ServerRspUtil.formRspBodyVO(count, "删除项目节点失败");
	}

}
