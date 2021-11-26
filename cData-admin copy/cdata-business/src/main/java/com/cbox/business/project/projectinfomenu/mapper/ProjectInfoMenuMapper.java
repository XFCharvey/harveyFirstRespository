package com.cbox.business.project.projectinfomenu.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: ProjectInfoMenuMapper
 * @Function: 项目资料分类
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface ProjectInfoMenuMapper {

	/** listProjectInfoMenu : 获取项目资料分类列表数据 **/
	public List<Map<String, Object>> listProjectInfoMenu(Map<String, Object> param);

	/** getProjectInfoMenu : 获取指定id的项目资料分类数据 **/
	public Map<String, Object> getProjectInfoMenu(Map<String, Object> param);

    public int updateInfoMenulname(String lname, String slname);

    public int updateInfoMenu(String newanc, String ance, String ance1);

}
