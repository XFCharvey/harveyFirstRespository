package com.cbox.business.question.questionbank.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: QuestionBankMapper
 * @Function: 题库主题
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface QuestionBankMapper {

	/** listQuestionBank : 获取题库主题列表数据 **/
	public List<Map<String, Object>> listQuestionBank(Map<String, Object> param);

	/** getQuestionBank : 获取指定id的题库主题数据 **/
	public Map<String, Object> getQuestionBank(Map<String, Object> param);

    public Map<String, Object> countinvitationNum(Map<String, Object> param);

    public List<Map<String, Object>> filterQuestionBank(Map<String, Object> param);

    public List<Map<String, Object>> listFilterInfoQuestionBank(Map<String, Object> param);

    public List<Map<String, Object>> getQuestionCount(Map<String, Object> param);

}
