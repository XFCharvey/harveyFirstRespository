package com.cbox.business.question.examrecord.bean;

import com.cbox.base.annotation.Excel;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @ClassName: ExportrecordVO
 * @Function: 兑奖记录，用于导入导出
 * 
 * @author cbox
 * @version 1.0
 */
public class ExportrecordVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/* 考试人,varchar */
	@Excel(name = "姓名")
	private String exam_person;

	/* 被调研人手机号,varchar */
	@Excel(name = "手机号")
	private String person_phone;

	/* 中奖类型，varchar */
	@Excel(name = "中奖类型")
	private String reward_id;

	/* 中奖名称，varchar */
	@Excel(name = "中奖名称")
	private String reward_name;

	/* 中奖金额，varchar */
	@Excel(name = "金额")
	private String reward_value;

	public String getExam_person() {
		return exam_person;
	}

	public void setExam_person(String exam_person) {
		this.exam_person = exam_person;
	}

	public String getPerson_phone() {
		return person_phone;
	}

	public void setPerson_phone(String person_phone) {
		this.person_phone = person_phone;
	}

	public String getReward_id() {
		return reward_id;
	}

	public void setReward_id(String reward_id) {
		this.reward_id = reward_id;
	}

	public String getReward_name() {
		return reward_name;
	}

	public void setReward_name(String reward_name) {
		this.reward_name = reward_name;
	}

	public String getReward_value() {
		return reward_value;
	}

	public void setReward_value(String reward_value) {
		this.reward_value = reward_value;
	}

}
