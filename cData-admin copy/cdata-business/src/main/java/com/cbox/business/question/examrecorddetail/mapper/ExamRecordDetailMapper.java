package com.cbox.business.question.examrecorddetail.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: ExamRecordDetailMapper
 * @Function: 考试/问卷记录详情
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface ExamRecordDetailMapper {

	/** listExamRecordDetail : 获取考试/问卷记录详情列表数据 **/
	public List<Map<String, Object>> listExamRecordDetail(Map<String, Object> param);

	/** getExamRecordDetail : 获取指定id的考试/问卷记录详情数据 **/
	public Map<String, Object> getExamRecordDetail(Map<String, Object> param);


}
