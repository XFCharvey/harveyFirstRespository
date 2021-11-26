package com.cbox.business.timetask.rule.mapper;

import java.util.List;
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
public interface RuleCommonMapper {

    /** 批量插入 **/
    public int insertRuleDetailBatch(Map<String, Object> param);

    /** 批量更新d_project_node表 **/
    public int updateProjctNodeBatch(Map<String, Object> param);

    /** 签约 **/
    public List<Map<String, Object>> getHouseBySign(Map<String, Object> param);

    /** 交房  */
    public List<Map<String, Object>> getHouseByDeliver(Map<String, Object> param);

    /** 投诉单  */
    public List<Map<String, Object>> getComplainTask(Map<String, Object> param);

    /** 获取未完成的项目数据  */
    public List<Map<String, Object>> getProjectNoEnd(Map<String, Object> param);
}
