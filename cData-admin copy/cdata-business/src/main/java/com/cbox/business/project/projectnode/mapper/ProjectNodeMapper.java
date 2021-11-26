package com.cbox.business.project.projectnode.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: ProjectNodeMapper
 * @Function: 项目节点
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface ProjectNodeMapper {

	/** listProjectNode : 获取项目节点列表数据 **/
	public List<Map<String, Object>> listProjectNode(Map<String, Object> param);

	/** getProjectNode : 获取指定id的项目节点数据 **/
	public Map<String, Object> getProjectNode(Map<String, Object> param);


}
