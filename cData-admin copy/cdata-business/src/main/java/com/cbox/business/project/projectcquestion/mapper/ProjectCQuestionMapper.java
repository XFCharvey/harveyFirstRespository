package com.cbox.business.project.projectcquestion.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: ProjectCQuestionMapper
 * @Function: 项目投诉/维修的存在问题
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface ProjectCQuestionMapper {

	/** listProjectCQuestion : 获取项目投诉/维修的存在问题列表数据 **/
	public List<Map<String, Object>> listProjectCQuestion(Map<String, Object> param);

	/** getProjectCQuestion : 获取指定id的项目投诉/维修的存在问题数据 **/
	public Map<String, Object> getProjectCQuestion(Map<String, Object> param);


}
