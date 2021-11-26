package com.cbox.business.question.questiontitlegroup.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: QuestiontitlegroupMapper
 * @Function: 题目分组
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface QuestiontitlegroupMapper {

	/** listQuestiontitlegroup : 获取题目分组列表数据 **/
	public List<Map<String, Object>> listQuestiontitlegroup(Map<String, Object> param);

	/** getQuestiontitlegroup : 获取指定id的题目分组数据 **/
	public Map<String, Object> getQuestiontitlegroup(Map<String, Object> param);


}
