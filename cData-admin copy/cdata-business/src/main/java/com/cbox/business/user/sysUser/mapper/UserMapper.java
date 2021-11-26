package com.cbox.business.user.sysUser.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    List<Map<String, Object>> listUser(Map<String, Object> param);

    List<Map<String, Object>> identityTypeValue(Map<String, Object> dictParams);

    List<Map<String, Object>> listfilterUser(Map<String, Object> param);

    Map<String, Object> getSysUser(Map<String, Object> param);

    Map<String, Object> getSysUserByPhone(Map<String, Object> param);

    List<Map<String, Object>> listfilterOperateUser(Map<String, Object> param);

    Map<String, Object> getDeptUser(Map<String, Object> param);

    int deleteDeptUser(Map<String, Object> param);

}
