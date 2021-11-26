package com.cbox.business.project.projectdefect.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: ProjectDefectMapper
 * @Function: 项目不利因素
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface ProjectDefectMapper {

	/** listProjectDefect : 获取项目不利因素列表数据 **/
	public List<Map<String, Object>> listProjectDefect(Map<String, Object> param);

	/** getProjectDefect : 获取指定id的项目不利因素数据 **/
	public Map<String, Object> getProjectDefect(Map<String, Object> param);


}
