package com.cbox.business.question.analysis.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: QuestionAnalysisMapper
 * @Function: 
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface QuestionAnalysisMapper {

    /** 批量插入 **/
    public int insertRecordOptionBatch(Map<String, Object> param);
}
