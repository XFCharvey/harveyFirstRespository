package com.cbox.business.project.projectinfo.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: ProjectInfoMapper
 * @Function: 项目资料
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface ProjectInfoMapper {

	/** listProjectInfo : 获取项目资料列表数据 **/
	public List<Map<String, Object>> listProjectInfo(Map<String, Object> param);

	/** getProjectInfo : 获取指定id的项目资料数据 **/
	public Map<String, Object> getProjectInfo(Map<String, Object> param);


}
