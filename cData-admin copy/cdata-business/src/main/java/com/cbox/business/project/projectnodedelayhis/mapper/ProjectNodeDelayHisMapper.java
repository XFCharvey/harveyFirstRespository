package com.cbox.business.project.projectnodedelayhis.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: ProjectNodeDelayHisMapper
 * @Function: 项目节点延期
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface ProjectNodeDelayHisMapper {

	/** listProjectNodeDelayHis : 获取项目节点延期列表数据 **/
	public List<Map<String, Object>> listProjectNodeDelayHis(Map<String, Object> param);

	/** getProjectNodeDelayHis : 获取指定id的项目节点延期数据 **/
	public Map<String, Object> getProjectNodeDelayHis(Map<String, Object> param);


}
