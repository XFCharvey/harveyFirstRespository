package com.cbox.business.worktask.worktask.service;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.cbox.base.utils.DateUtils;
import com.cbox.base.utils.ObjUtil;
import com.cbox.base.utils.SecurityUtils;
import com.cbox.base.utils.StrUtil;
import com.cbox.base.utils.poi.ExcelUtil;
import com.cbox.business.customer.customer.bean.CustomerVo;
import com.cbox.business.worktask.worktask.bean.WorkTaskVO;
import com.cbox.business.worktask.worktask.mapper.WorktaskMapper;
import com.cbox.business.worktask.worktaskdeal.mapper.WorktaskDealMapper;

/**
 * @ClassName: WorktaskService
 * @Function: 任务表
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class WorktaskService extends BaseService {

	@Autowired
	private WorktaskMapper worktaskMapper;

	@Autowired
	private WorktaskDealMapper worktaskDealMapper;

	/** listWorktaskCountOfUser : 获取预警任务的用户及工作任务数量 **/
	public List<Map<String, Object>> listWorktaskCountOfUser(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = worktaskMapper.listWorktaskCountOfUser(param);

		return list;
	}

	/** getWorktaskTypeGroup : 统计任务工单类型比例分布 **/
	public ResponseBodyVO getWorktaskTypeGroup(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = worktaskMapper.getWorktaskTypeGroup(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** listWorktask : 获取任务表列表数据 **/
	public List<Map<String, Object>> listWorktask(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = worktaskMapper.listWorktask(param);

		return list;
	}

	/** exportWorktasks : 导出任务表列表数据 **/
	public AjaxResult exportWorktasks(Map<String, Object> param) {
		super.appendUserInfo(param);
		List<Map<String, Object>> list = worktaskMapper.listWorktask(param);

		List<WorkTaskVO> listWorkTaskVO = new ArrayList<WorkTaskVO>();
		if (ObjUtil.isNotNull(list)) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> mapWorkTaskVO = list.get(i);
				WorkTaskVO workTaskVO = new WorkTaskVO();
				workTaskVO.setTask_name(StrUtil.getMapValue(mapWorkTaskVO, "task_name"));
				String taskType = StrUtil.getMapValue(mapWorkTaskVO, "task_type");
				workTaskVO.setTask_type_label(formatTaskType(taskType));
				workTaskVO.setTask_deadline(StrUtil.getMapValue(mapWorkTaskVO, "task_deadline"));
				workTaskVO.setDate_disparity(StrUtil.getMapValue(mapWorkTaskVO, "date_disparity"));
				String taskStatus = StrUtil.getMapValue(mapWorkTaskVO, "task_status");
				workTaskVO.setTask_status_label(formatTaskStatus(taskStatus));
				workTaskVO.setDeal_name(StrUtil.getMapValue(mapWorkTaskVO, "deal_name"));
				workTaskVO.setUser_name(StrUtil.getMapValue(mapWorkTaskVO, "user_name"));
				workTaskVO.setRec_time(StrUtil.getMapValue(mapWorkTaskVO, "rec_time"));
				listWorkTaskVO.add(workTaskVO);
			}
		}

		// 解析excel文件
		ExcelUtil<WorkTaskVO> util = new ExcelUtil<WorkTaskVO>(WorkTaskVO.class);
		String sheetName = "任务单信息文件";
		AjaxResult result = util.exportExcel(listWorkTaskVO, sheetName);
		return result;
	}

	// 转义任务单类型。任务类型：complain-投诉，consult-咨询，repair-维修，task-任务单, event-重大事件
	public String formatTaskType(String taskType) {
		String taskTypeLabel = "";

		if (taskType.equals("complain")) {
			taskTypeLabel = "投诉";
		} else if (taskType.equals("consult")) {
			taskTypeLabel = "咨询";
		} else if (taskType.equals("repair")) {
			taskTypeLabel = "维修";
		} else if (taskType.equals("task")) {
			taskTypeLabel = "任务单";
		} else if (taskType.equals("event")) {
			taskTypeLabel = "重大事件";
		}
		return taskTypeLabel;
	}

	// 转义任务单状态。任务状态：0-待派单，1-待联系，2-实施中，3-已关闭，4-已完成
	public String formatTaskStatus(String taskStatus) {
		String taskStatusLabel = "";

		if (taskStatus.equals("0")) {
			taskStatusLabel = "待派单";
		} else if (taskStatus.equals("1")) {
			taskStatusLabel = "待联系";
		} else if (taskStatus.equals("2")) {
			taskStatusLabel = "实施中";
		} else if (taskStatus.equals("3")) {
			taskStatusLabel = "已关闭";
		} else if (taskStatus.equals("4")) {
			taskStatusLabel = "已完成";
		}
		return taskStatusLabel;
	}

	/** listWorktask : 获取预警（完成期限于今日差距小于5）任务表列表数据 **/
	public List<Map<String, Object>> listOverdueWorktask(Map<String, Object> param) {
		// TODO Auto-generated method stub
		super.appendUserInfo(param);

		List<Map<String, Object>> list = worktaskMapper.listOverdueWorktask(param);

		return list;
	}

	/** 获取抄送人包含当前用户的任务 **/
	public List<Map<String, Object>> listWorktaskCopyPerson(Map<String, Object> param) {
		// TODO Auto-generated method stub
		super.appendUserInfo(param);
		String userName = SecurityUtils.getUsername();

		List<Map<String, Object>> listResult = new ArrayList<Map<String, Object>>();

		List<Map<String, Object>> listTask = worktaskMapper.listWorktask(param);
		for (int i = 0; i < listTask.size(); i++) {
			Map<String, Object> mapTask = listTask.get(i);
			String copyPerson = StrUtil.getMapValue(mapTask, "copy_person");
			List<String> copyPersonList = Arrays.asList(copyPerson.split(","));
			if (copyPersonList.contains(userName)) {
				listResult.add(mapTask);
			}
		}
		return listResult;
	}

	/** getSubTaskList : 获取指定id派生的子任务数据 **/
	public ResponseBodyVO getSubTaskList(Map<String, Object> param) {
		super.appendUserInfo(param);

		// 查询任务历史
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("relate_taskid", param.get("relate_taskid"));

		List<Map<String, Object>> listTask = this.query("d_worktask", mapParam);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, listTask);
	}

	/** getWorktask : 获取指定id的任务表数据 **/
	public ResponseBodyVO getWorktask(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = worktaskMapper.getWorktask(param);
		// 查询任务历史
		Map<String, Object> mapTaskDealCondition = new HashMap<String, Object>();
		mapTaskDealCondition.put("task_id", param.get("rec_id"));

		List<Map<String, Object>> listTaskDeal = worktaskDealMapper.listWorktaskDeal(mapTaskDealCondition);
		mapResult.put("procesHis", listTaskDeal);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** getCustomerWorktaskNum : 获取指定客户除任务类型外的任务表数据数量 **/
	public ResponseBodyVO getCustomerWorktaskNum(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = worktaskMapper.getCustomerWorktaskNum(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addWorktask : 新增任务表 **/
	public ResponseBodyVO addWorktask(Map<String, Object> param) {

		// Table:d_worktask
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("project_id", param.get("project_id"));
		mapParam.put("houses_id", param.get("houses_id"));
		mapParam.put("relate_taskid", param.get("relate_taskid"));
		mapParam.put("customer_id", param.get("customer_id"));
		mapParam.put("task_name", param.get("task_name"));
		mapParam.put("task_type", param.get("task_type"));
		mapParam.put("task_status", param.get("task_status"));
		mapParam.put("deal_person", param.get("deal_person"));
		mapParam.put("task_detail", param.get("task_detail"));
		mapParam.put("task_image", param.get("task_image"));
		mapParam.put("deal_person", param.get("deal_person"));
		mapParam.put("copy_person", param.get("copy_person"));
		mapParam.put("task_deadline", param.get("task_deadline"));
		mapParam.put("cause_analysis", param.get("cause_analysis"));
		mapParam.put("deal_plan", param.get("deal_plan"));
		mapParam.put("rela_num", param.get("rela_num"));
		int count = this.save("d_worktask", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "新增任务表失败");
	}

	/** updateWorktask : 修改任务表 **/
	public ResponseBodyVO updateWorktask(Map<String, Object> param) {
		String nowTime = DateUtils.getDate();
		// Table:d_worktask
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("task_name", param.get("task_name"));
		mapParam.put("task_type", param.get("task_type"));
		mapParam.put("finish_rate", param.get("finish_rate"));
		mapParam.put("deal_person", param.get("deal_person"));
		mapParam.put("project_id", param.get("project_id"));
		mapParam.put("task_status", param.get("task_status"));
		if ("4".equals(StrUtil.getMapValue(param, "task_status"))) {
			mapParam.put("finish_time", nowTime);
		}
		mapParam.put("task_detail", param.get("task_detail"));
		mapParam.put("task_image", param.get("task_image"));
		mapParam.put("deal_person", param.get("deal_person"));
		mapParam.put("copy_person", param.get("copy_person"));
		mapParam.put("task_deadline", param.get("task_deadline"));
		mapParam.put("cause_analysis", param.get("cause_analysis"));
		mapParam.put("deal_plan", param.get("deal_plan"));
		mapParam.put("rela_num", param.get("rela_num"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.update(mapCondition, "d_worktask", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改任务表失败");
	}

	/** delWorktask : 删除任务表 **/
	public ResponseBodyVO delWorktask(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断

		// Table:d_worktask
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.delete("d_worktask", mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除任务表失败");
	}

}