package com.cbox.business.worktask.worktaskdeal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: WorktaskDealMapper
 * @Function: 任务处理表
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface WorktaskDealMapper {

    /** listWorktaskDeal : 获取任务处理表列表数据 **/
    public List<Map<String, Object>> listWorktaskDeal(Map<String, Object> param);

    /** getWorktaskDeal : 获取指定id的任务处理表数据 **/
    public Map<String, Object> getWorktaskDeal(Map<String, Object> param);

}