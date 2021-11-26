package com.cbox.business.project.argwbind.bean;

import com.cbox.base.annotation.Excel;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @ClassName: ArgwBindVO
 * @Function: 阿融官微绑定，用于导入导出
 * 
 * @author cbox
 * @version 1.0
 */
public class ArgwBindVO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/* 关联项目表rec_id,int */
	@Excel(name = "关联项目表rec_id")
	private String project_id;

	/* 绑定类型：1-阿融绑定，2-官微绑定,char */
	@Excel(name = "绑定类型：1-阿融绑定，2-官微绑定")
	private String type;

	/* 关联文件id,varchar */
	@Excel(name = "关联文件id")
	private String file_id;

	public String getProject_id() {
		return project_id;
	}
	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFile_id() {
		return file_id;
	}
	public void setFile_id(String file_id) {
		this.file_id = file_id;
	}

}
