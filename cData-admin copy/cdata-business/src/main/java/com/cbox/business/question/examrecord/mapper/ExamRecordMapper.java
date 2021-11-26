package com.cbox.business.question.examrecord.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: ExamRecordMapper
 * @Function: 考试/问卷记录
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface ExamRecordMapper {

	/** listExamRecord : 获取考试/问卷记录列表数据 **/
	public List<Map<String, Object>> listExamRecord(Map<String, Object> param);

	/** getExamRecord : 获取指定id的考试/问卷记录数据 **/
	public Map<String, Object> getExamRecord(Map<String, Object> param);

    public Map<String, Object> getExamRecordByUser(Map<String, Object> param);

    public List<Map<String, Object>> listCommonExamRecord(Map<String, Object> param);

}
