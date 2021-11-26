package com.cbox.business.info.infocontent.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: InfoContentMapper
 * @Function: 资讯信息
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface InfoContentMapper {

	/** listInfoContent : 获取资讯信息列表数据 **/
	public List<Map<String, Object>> listInfoContent(Map<String, Object> param);
	
    /** 发现-推荐列表 **/
    public List<Map<String, Object>> listInfoRecommend(Map<String, Object> param);

	/** getInfoContent : 获取指定id的资讯信息数据 **/
	public Map<String, Object> getInfoContent(Map<String, Object> param);

    /** listInfoContent : 获取未设置为必修的课程类型的资讯信息列表数据 **/
    public List<Map<String, Object>> listCoursesfilete(Map<String, Object> param);

    public List<Map<String, Object>> listCoursesfileteLearn(Map<String, Object> param);

}
