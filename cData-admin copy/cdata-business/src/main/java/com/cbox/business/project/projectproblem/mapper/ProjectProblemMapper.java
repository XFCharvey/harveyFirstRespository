package com.cbox.business.project.projectproblem.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: ProjectProblemMapper
 * @Function: 项目开放、交付问题
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface ProjectProblemMapper {

	/** listProjectProblem : 获取项目开放、交付问题列表数据 **/
	public List<Map<String, Object>> listProjectProblem(Map<String, Object> param);

	/** getProjectProblem : 获取指定id的项目开放、交付问题数据 **/
	public Map<String, Object> getProjectProblem(Map<String, Object> param);


}
