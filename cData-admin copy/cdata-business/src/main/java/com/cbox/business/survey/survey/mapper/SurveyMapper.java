package com.cbox.business.survey.survey.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: SurveyMapper
 * @Function: 客户调研
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface SurveyMapper {

	/** listSurvey : 获取客户调研列表数据 **/
	public List<Map<String, Object>> listSurvey(Map<String, Object> param);

	/** getSurvey : 获取指定id的客户调研数据 **/
	public Map<String, Object> getSurvey(Map<String, Object> param);


}
