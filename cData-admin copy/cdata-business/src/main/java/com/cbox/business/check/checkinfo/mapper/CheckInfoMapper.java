package com.cbox.business.check.checkinfo.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: CheckInfoMapper
 * @Function: 审核信息表
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface CheckInfoMapper {

	/** listCheckInfo : 获取审核信息表列表数据 **/
	public List<Map<String, Object>> listCheckInfo(Map<String, Object> param);

	/** getCheckInfo : 获取指定id的审核信息表数据 **/
	public Map<String, Object> getCheckInfo(Map<String, Object> param);

    public Map<String, Object> getRoleID(Map<String, Object> mapRoleCondition);

}
