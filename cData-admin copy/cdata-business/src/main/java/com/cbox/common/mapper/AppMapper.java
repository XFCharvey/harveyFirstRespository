package com.cbox.common.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AppMapper {

    int saveAppInfo(Map<String, String> param);
    
    int disableAppInfo(Map<String, String> param);

}
