package com.cbox.business.question.questiontitleoption.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: QuestionTitleOptionMapper
 * @Function: 题目选项
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface QuestionTitleOptionMapper {

	/** listQuestionTitleOption : 获取题目选项列表数据 **/
	public List<Map<String, Object>> listQuestionTitleOption(Map<String, Object> param);

	/** getQuestionTitleOption : 获取指定id的题目选项数据 **/
    public Map<String, Object> getQuestionTitleOption(Map<String, Object> param);


}
