package com.cbox.business.project.projectcontractor.bean;

import com.cbox.base.annotation.Excel;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @ClassName: ContractorVO
 * @Function: 承建商，用于导入导出
 * 
 * @author cbox
 * @version 1.0
 */
public class ContractorVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/* 承建商名称,varchar */
	@Excel(name = "承建商名称")
	private String contractor_name;

	/* 项目负责部位,varchar */
	@Excel(name = "项目负责部位")
	private String contractor_position;

	/* 关联项目ID,int */
	@Excel(name = "项目名称")
	private String project_name;

	public String getContractor_name() {
		return contractor_name;
	}

	public void setContractor_name(String contractor_name) {
		this.contractor_name = contractor_name;
	}

	public String getContractor_position() {
		return contractor_position;
	}

	public void setContractor_position(String contractor_position) {
		this.contractor_position = contractor_position;
	}

	public String getProject_name() {
		return project_name;
	}

	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}

}