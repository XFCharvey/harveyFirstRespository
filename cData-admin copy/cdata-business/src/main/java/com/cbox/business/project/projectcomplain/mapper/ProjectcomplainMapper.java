package com.cbox.business.project.projectcomplain.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: ProjectcomplainMapper
 * @Function: 项目投诉/维修统计
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface ProjectcomplainMapper {

	/** listProjectcomplain : 获取项目投诉/维修统计列表数据 **/
	public List<Map<String, Object>> listProjectcomplain(Map<String, Object> param);

	/** getProjectcomplain : 获取指定id的项目投诉/维修统计数据 **/
	public Map<String, Object> getProjectcomplain(Map<String, Object> param);


}
