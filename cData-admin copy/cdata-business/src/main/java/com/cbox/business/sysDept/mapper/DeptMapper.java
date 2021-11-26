package com.cbox.business.sysDept.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DeptMapper {

    List<Map<String, Object>> listDept(Map<String, Object> param);

    Map<String, Object> getDept(Map<String, Object> param);

    int updateDept(@Param("newanc") String newanc, @Param("ance") String ance, @Param("ance1") String ance1);

}
