package com.cbox.business.worktask.worktask.bean;

import com.cbox.base.annotation.Excel;
import com.cbox.base.core.domain.BaseEntity;

public class WorkTaskVO extends BaseEntity {

	/** 任务名称 */
	@Excel(name = "任务名称")
	private String task_name;

	/** 任务类型 */
	@Excel(name = "任务类型")
	private String task_type_label;

	/** 完成期限 */
	@Excel(name = "完成期限")
	private String task_deadline;

	/** 剩余天数 */
	@Excel(name = "剩余天数")
	private String date_disparity;

	/** 状态 */
	@Excel(name = "状态")
	private String task_status_label;

	/** 处理人 */
	@Excel(name = "处理人")
	private String deal_name;

	/** 派发人 */
	@Excel(name = "派发人")
	private String user_name;

	/** 创建时间 */
	@Excel(name = "创建时间")
	private String rec_time;

	public String getTask_name() {
		return task_name;
	}

	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}

	public String getTask_type_label() {
		return task_type_label;
	}

	public void setTask_type_label(String task_type_label) {
		this.task_type_label = task_type_label;
	}

	public String getTask_deadline() {
		return task_deadline;
	}

	public void setTask_deadline(String task_deadline) {
		this.task_deadline = task_deadline;
	}

	public String getDate_disparity() {
		return date_disparity;
	}

	public void setDate_disparity(String date_disparity) {
		this.date_disparity = date_disparity;
	}

	public String getTask_status_label() {
		return task_status_label;
	}

	public void setTask_status_label(String task_status_label) {
		this.task_status_label = task_status_label;
	}

	public String getDeal_name() {
		return deal_name;
	}

	public void setDeal_name(String deal_name) {
		this.deal_name = deal_name;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getRec_time() {
		return rec_time;
	}

	public void setRec_time(String rec_time) {
		this.rec_time = rec_time;
	}
}
