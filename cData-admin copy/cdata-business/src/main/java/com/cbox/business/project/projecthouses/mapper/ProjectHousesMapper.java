package com.cbox.business.project.projecthouses.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: ProjectHousesMapper
 * @Function: 项目楼栋房源
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface ProjectHousesMapper {

	/** listProjectHouses : 获取项目楼栋房源列表数据 **/
	public List<Map<String, Object>> listProjectHouses(Map<String, Object> param);

	/** getProjectHouses : 获取指定id的项目楼栋房源数据 **/
	public Map<String, Object> getProjectHouses(Map<String, Object> param);
	
	/** getHousesStatusGroup : 获取指定id项目的项目楼栋房源状态分组数量 **/
	public Map<String, Object> getHousesStatusGroup(Map<String, Object> param);
	
	/** listHouseOfCustomerId : 获取指定id客户的房源列表 **/
	public List<Map<String, Object>> listHouseOfCustomerId(Map<String, Object> param);

}
