package com.cbox.quartz.mapper;

import java.util.List;
import java.util.Map;

import com.cbox.base.core.domain.entity.SysDept;

public interface DepartMapper {

    /**
     * 查询部门列表
     * 
     * @param dept 部门信息
     * @return
     */
    public List<SysDept> selectDeptList(SysDept dept);

    /**
     * 插入部门任务表
     */
    public int insertTask(Map<String, String> param);

}
