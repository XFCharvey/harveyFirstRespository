package com.cbox.business.survey.surveydetail.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: SurveyDetailMapper
 * @Function: 客户调研
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface SurveyDetailMapper {

	/** listSurveyDetail : 获取客户调研列表数据 **/
	public List<Map<String, Object>> listSurveyDetail(Map<String, Object> param);

	/** getSurveyDetail : 获取指定id的客户调研数据 **/
	public Map<String, Object> getSurveyDetail(Map<String, Object> param);


}
