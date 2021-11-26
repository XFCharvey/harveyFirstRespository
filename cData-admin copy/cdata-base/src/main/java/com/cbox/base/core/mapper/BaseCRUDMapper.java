package com.cbox.base.core.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BaseCRUDMapper {

    /**
     * 保存，自动插入rec_系列的字段
     */
    int save(Map<String, Object> tableMap);

    /**
     * 保存，不插入rec_系列字段
     */
    int saveNoRec(Map<String, Object> tableMap);

    /**
     * 保存，不插入rec_系列字段，但是有rec_id，会返回自增rec_id值
     */
    int saveNoRecHasRecId(Map<String, Object> tableMap);

    /**
     * 部门保存，会返回dept_id值
     */
    int saveDeptHasDeptId(Map<String, Object> tableMap);

    /**
     * 用户保存，会返回user_id值
     */
    int saveUserHasUserId(Map<String, Object> tableMap);

    /**
     * 更新（根据rec_id）
     */
    int update(Map<String, Object> tableMap);

    /**
     * 更新（根据传入Map条件）
     */
    int updateConditionsNoRec(Map<String, Object> tableMap);

    int updateConditions(Map<String, Object> tableMap);

    /**
     * 更新自增
     */
    int updateIncrement(Map<String, Object> tableMap);

    /**
     * 更新自减
     */
    int updateDecrement(Map<String, Object> tableMap);

    /**
     * 逻辑删除
     */
    int delete(Map<String, Object> tableMap);

    /**
     * 物理删除
     */
    int deleteEmpty(Map<String, Object> tableMap);

    /**
     * 查询列表
     */
    List<Map<String, Object>> query(Map<String, Object> tableMap);

    List<Map<String, Object>> queryNoRec(Map<String, Object> tableMap);

    /**
     * 模糊查询
     */
    List<Map<String, Object>> queryLike(Map<String, Object> tableMap);

    /**
     * 查询详情
     */
    Map<String, Object> queryOne(Map<String, Object> tableMap);

    /**
     * 查询总记录数
     */
    int count(Map<String, Object> tableMap);

    int countLike(Map<String, Object> tableMap);

    /**
     * 是否存在
     * 
     * @param tableMap
     * @return
     */
    int isExist(Map<String, Object> tableMap);

}
