package com.cbox.business.compulcourses.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: CompulCoursesMapper
 * @Function: 必修课程安排表
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface CompulCoursesMapper {

	/** listCompulCourses : 获取必修课程安排表列表数据 **/
	public List<Map<String, Object>> listCompulCourses(Map<String, Object> param);

    /** listCompulCoursesByUser : 根据用户获取判断必修课程是否加入计划，且成绩是否合格 **/
    public List<Map<String, Object>> listCompulCoursesByUser(Map<String, Object> param);
	/** getCompulCourses : 获取指定id的必修课程安排表数据 **/
    public Map<String, Object> getCompulCourses(Map<String, Object> param);

    public Map<String, Object> getCountNum(Map<String, Object> param);
    //
    public Map<String, Object> getCompulCoursesByCourseID(Map<String, Object> param);

}
