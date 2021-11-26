package com.cbox.wxlogin.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserNoTokenMapper {

    List<Map<String, Object>> listUser(Map<String, Object> param);
}
