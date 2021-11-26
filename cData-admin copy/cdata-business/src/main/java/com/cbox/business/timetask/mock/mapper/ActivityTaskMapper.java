package com.cbox.business.timetask.mock.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ActivityTaskMapper {

    List<Map<String, Object>> listOverdueActivity();

}
