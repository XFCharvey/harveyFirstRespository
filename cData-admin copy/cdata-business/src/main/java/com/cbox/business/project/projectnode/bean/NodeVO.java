package com.cbox.business.project.projectnode.bean;

import java.io.Serializable;

import com.cbox.base.annotation.Excel;

/**
 * @ClassName: NodeVO
 * @Function: 项目节点，用于导入导出
 * 
 * @author cbox
 * @version 1.0
 */
public class NodeVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/* 节点名称,varchar */
	@Excel(name = "节点名称")
	private String node_name;
	
	/* 被调研人手机号,varchar */
	@Excel(name = "2021版")
	private String node_deadline;

	public NodeVO() {
		super();
	}

	public NodeVO(String node_name, String node_deadline) {
		super();
		this.node_name = node_name;
		this.node_deadline = node_deadline;
	}

	public String getNode_name() {
		return node_name;
	}

	public void setNode_name(String node_name) {
		this.node_name = node_name;
	}

	public String getNode_deadline() {
		return node_deadline;
	}

	public void setNode_deadline(String node_deadline) {
		this.node_deadline = node_deadline;
	}
}
