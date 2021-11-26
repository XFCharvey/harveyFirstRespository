package com.cbox.business.project.projectdefecttype.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: ProjectDefectTypeMapper
 * @Function: 项目不利因素分类组
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface ProjectDefectTypeMapper {

	/** listProjectDefectType : 获取项目不利因素分类组列表数据 **/
	public List<Map<String, Object>> listProjectDefectType(Map<String, Object> param);

	/** getProjectDefectType : 获取指定id的项目不利因素分类组数据 **/
	public Map<String, Object> getProjectDefectType(Map<String, Object> param);


}
