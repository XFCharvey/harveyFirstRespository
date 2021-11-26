package com.cbox.business.question.questiontitle.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: QuestiontitleMapper
 * @Function: 题库题目
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface QuestiontitleMapper {

	/** listQuestiontitle : 获取题库题目列表数据 **/
	public List<Map<String, Object>> listQuestiontitle(Map<String, Object> param);

	/** getQuestiontitle : 获取指定id的题库题目数据 **/
	public Map<String, Object> getQuestiontitle(Map<String, Object> param);

}
