package com.cbox.business.timetask.mock.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MockProjectMapper {

    Map<String, Object> getGroupByGuid(Map<String, Object> mapProGroupParam);

    Map<String, Object> getProjectByGuid(Map<String, Object> mapProjectParam);

    List<Map<String, Object>> listHousesByStatus(Map<String, Object> param);

    List<Map<String, Object>> listProject();

    /** 批量插入房间数据 **/
    public int insertHousesBatch(Map<String, Object> param);

    /** 批量插入问题数据 **/
    public int insertProblemBatch(Map<String, Object> param);

}
