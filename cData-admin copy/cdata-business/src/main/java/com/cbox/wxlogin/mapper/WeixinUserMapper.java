package com.cbox.wxlogin.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cbox.wxlogin.bean.WeixinUser;

@Mapper
public interface WeixinUserMapper {

    /**
     * 通过openid获取微信用户信息
     */
    WeixinUser findByOpenId(@Param("openid") String openid);

    /**
     * 通过unionid获取微信用户信息
     */
    WeixinUser findByUnionid(@Param("unionid") String unionid);

    int save(Map<String, Object> param);

    WeixinUser findById(@Param("rec_id") String rec_id);

    Map<String, Object> getSysUser(Map<String, Object> param);

    List<Map<String, Object>> listAppLoginLog(Map<String, Object> loginLogParam);
}
