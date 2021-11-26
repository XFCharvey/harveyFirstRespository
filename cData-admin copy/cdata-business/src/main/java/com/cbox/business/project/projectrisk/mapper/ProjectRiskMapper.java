package com.cbox.business.project.projectrisk.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: ProjectRiskMapper
 * @Function: 项目风险
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface ProjectRiskMapper {

	/** listProjectRisk : 获取项目风险列表数据 **/
	public List<Map<String, Object>> listProjectRisk(Map<String, Object> param);

	/** getProjectRisk : 获取指定id的项目风险数据 **/
	public Map<String, Object> getProjectRisk(Map<String, Object> param);


}
