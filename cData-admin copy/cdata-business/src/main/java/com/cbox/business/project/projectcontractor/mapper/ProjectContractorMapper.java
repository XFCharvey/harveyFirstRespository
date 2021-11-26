package com.cbox.business.project.projectcontractor.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: ProjectContractorMapper
 * @Function: 项目承建商
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface ProjectContractorMapper {

	/** listProjectContractor : 获取项目承建商列表数据 **/
	public List<Map<String, Object>> listProjectContractor(Map<String, Object> param);

	/** getProjectContractor : 获取指定id的项目承建商数据 **/
	public Map<String, Object> getProjectContractor(Map<String, Object> param);


}
