package com.cbox.business.worktask.worktask.mapper;
 
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
 
/**
 * @ClassName: WorktaskMapper
 * @Function: 任务表
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface WorktaskMapper {
 
    /** listWorktask : 获取任务表列表数据 **/
    public List<Map<String, Object>> listWorktask(Map<String, Object> param);
 
    /** getWorktask : 获取指定id的任务表数据 **/
    public Map<String, Object> getWorktask(Map<String, Object> param);

    /** listWorktask : 获取预警（完成期限于今日差距小于5）任务表列表数据 **/
    public List<Map<String, Object>> listOverdueWorktask(Map<String, Object> param);

    /** getWorktask : 获取指定编号的任务表数据 **/
    public Map<String, Object> getWorktaskByCode(Map<String, Object> param);
    
    /** getWorktaskTypeGroup : 统计任务工单类型比例分布 **/
    public Map<String, Object> getWorktaskTypeGroup(Map<String, Object> param);
    
    /** listWorktaskCountOfUser : 获取预警任务的用户及工作任务数量 **/
    public List<Map<String, Object>> listWorktaskCountOfUser(Map<String, Object> param);

    public Map<String, Object> getCustomerWorktaskNum(Map<String, Object> param);
 
}