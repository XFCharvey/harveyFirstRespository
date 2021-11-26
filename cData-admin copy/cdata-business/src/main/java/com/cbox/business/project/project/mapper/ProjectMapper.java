package com.cbox.business.project.project.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: ProjectMapper
 * @Function: 项目表
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface ProjectMapper {

    /** listProject : 获取项目表列表数据 **/
    public List<Map<String, Object>> listProject(Map<String, Object> param);

    /** getProject : 获取指定id的项目表数据 **/
    public Map<String, Object> getProject(Map<String, Object> param);

    /** 获取指定id的项目里所有用户的阿隆与官位关注数 **/
    public Map<String, Object> getProjectFollowTotal(Map<String, Object> mapProjectFollowCondition);

}