package com.cbox.business.question.questionanalysis.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: QuestionAnalysisMapper
 * @Function: 问卷分析要素
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface QuesAnalysisMapper {

	/** listQuestionAnalysis : 获取问卷分析要素列表数据 **/
	public List<Map<String, Object>> listQuestionAnalysis(Map<String, Object> param);

	/** getQuestionAnalysis : 获取指定id的问卷分析要素数据 **/
	public Map<String, Object> getQuestionAnalysis(Map<String, Object> param);


}
