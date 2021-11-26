package com.cbox.business.survey.surveydetail.bean;

import com.cbox.base.annotation.Excel;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @ClassName: SurveyDetailVO
 * @Function: 调研详情，用于导入导出
 * 
 * @author cbox
 * @version 1.0
 */
public class SurveyDetailVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/* 发送日期,datetime */
	@Excel(name = "发送日期")
	private String send_time;

	/* 成功调访日期,datetime */
	@Excel(name = "成功调访日期")
	private String survey_time;

	/* 项目名称,int */
	@Excel(name = "项目期别")
	private String project_name;

	/* 置业顾问姓名,varchar */
	@Excel(name = "置业顾问姓名")
	private String adviser_name;

	/* 跟进顾问姓名,varchar */
	@Excel(name = "跟进顾问姓名")
	private String adviser_name_up;

	/* 房修工程师姓名,varchar */
	@Excel(name = "房修工程师姓名")
	private String engineer_name;

	/* 节点名称,varchar */
	@Excel(name = "节点名称")
	private String node_name;

	/* 户型,varchar */
	@Excel(name = "户型")
	private String house_type;

	/* 面积,varchar */
	@Excel(name = "面积")
	private String area;

	/* 总价,int */
	@Excel(name = "总价")
	private String total_price;

	public String getSend_time() {
		return send_time;
	}

	public void setSend_time(String send_time) {
		this.send_time = send_time;
	}

	public String getSurvey_time() {
		return survey_time;
	}

	public void setSurvey_time(String survey_time) {
		this.survey_time = survey_time;
	}

	public String getProject_name() {
		return project_name;
	}

	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}

	public String getAdviser_name() {
		return adviser_name;
	}

	public void setAdviser_name(String adviser_name) {
		this.adviser_name = adviser_name;
	}

	public String getAdviser_name_up() {
		return adviser_name_up;
	}

	public void setAdviser_name_up(String adviser_name_up) {
		this.adviser_name_up = adviser_name_up;
	}

	public String getEngineer_name() {
		return engineer_name;
	}

	public void setEngineer_name(String engineer_name) {
		this.engineer_name = engineer_name;
	}

	public String getNode_name() {
		return node_name;
	}

	public void setNode_name(String node_name) {
		this.node_name = node_name;
	}

	public String getHouse_type() {
		return house_type;
	}

	public void setHouse_type(String house_type) {
		this.house_type = house_type;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getTotal_price() {
		return total_price;
	}

	public void setTotal_price(String total_price) {
		this.total_price = total_price;
	}

}
