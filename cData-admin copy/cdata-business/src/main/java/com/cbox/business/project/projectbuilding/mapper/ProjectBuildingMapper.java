package com.cbox.business.project.projectbuilding.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: ProjectBuildingMapper
 * @Function: 项目楼栋
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface ProjectBuildingMapper {

	/** listProjectBuilding : 获取项目楼栋列表数据 **/
	public List<Map<String, Object>> listProjectBuilding(Map<String, Object> param);

	/** getProjectBuilding : 获取指定id的项目楼栋数据 **/
	public Map<String, Object> getProjectBuilding(Map<String, Object> param);


}
