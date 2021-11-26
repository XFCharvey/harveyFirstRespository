package com.cbox.wxlogin.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeptSelectMapper {

    List<Map<String, Object>> listDept(Map<String, Object> param);

}
