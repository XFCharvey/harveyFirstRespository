package com.cbox.business.compulcourses.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbox.base.constant.HttpStatus;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.service.BaseService;
import com.cbox.base.utils.DateUtils;
import com.cbox.base.utils.SecurityUtils;
import com.cbox.base.utils.StrUtil;
import com.cbox.business.compulcourses.mapper.CompulCoursesMapper;


/**
 * @ClassName: CompulCoursesService
 * @Function: 必修课程安排表
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class CompulCoursesService extends BaseService {

    @Autowired
    private CompulCoursesMapper compulCoursesMapper;
    
	/** listCompulCourses : 获取必修课程安排表列表数据 **/
	public List<Map<String, Object>> listCompulCourses(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = compulCoursesMapper.listCompulCourses(param);

		return list;
	}

    /** listCompulCoursesByUser : 根据用户获取判断必修课程是否加入计划，且成绩是否合格 **/
    public List<Map<String, Object>> listCompulCoursesByUser(Map<String, Object> param) {
        super.appendUserInfo(param);
        String userName = SecurityUtils.getUsername();
        String nowYear = DateUtils.getYear();
        param.put("learn_person", userName);
        param.put("plan_year", nowYear);

        List<Map<String, Object>> listCompulCourses = compulCoursesMapper.listCompulCoursesByUser(param);

        return listCompulCourses;
    }
    /** getCompulCourses : 获取指定id的必修课程安排表数据 **/
	public ResponseBodyVO getCompulCourses(Map<String, Object> param) {
		super.appendUserInfo(param);

        Map<String, Object> mapResult = compulCoursesMapper.getCompulCourses(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addCompulCourses : 新增必修课程安排表 **/
	public ResponseBodyVO addCompulCourses(Map<String, Object> param) {

        int count = 0;
		// Table:d_compulsory_courses
        String courseId = StrUtil.getMapValue(param, "course_id");

        String[] courseIds = courseId.split(",");// 可能是批量增加
        for (int i = 0; i < courseIds.length; i++) {
            Map<String, Object> mapParam = new HashMap<String, Object>();
            mapParam.put("course_id", courseIds[i]);
            mapParam.put("plan_year", param.get("plan_year"));
            count = this.save("d_compulsory_courses", mapParam);
        }
        Map<String, Object> yearParam = new HashMap<String, Object>();
        yearParam.put("plan_year", param.get("plan_year"));

        Map<String, Object> countNum = compulCoursesMapper.getCountNum(yearParam);
        String coursesNum = StrUtil.getMapValue(countNum, "countNum");
        Map<String, Object> totalParam = new HashMap<String, Object>();
        totalParam.put("hours_compulsory", coursesNum);
        count += this.update(yearParam, "d_courses_hours", totalParam);


		return ServerRspUtil.formRspBodyVO(count, "新增必修课程安排表失败");
	}

	/** updateCompulCourses : 修改必修课程安排表 **/
	public ResponseBodyVO updateCompulCourses(Map<String, Object> param) {

		// Table:d_compulsory_courses
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("course_id", param.get("course_id"));
		mapParam.put("plan_year", param.get("plan_year"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.update(mapCondition, "d_compulsory_courses", mapParam);


		return ServerRspUtil.formRspBodyVO(count, "修改必修课程安排表失败"); 
	}

	/** delCompulCourses : 删除必修课程安排表 **/
	public ResponseBodyVO delCompulCourses(Map<String, Object> param) {
        int count = 0;
		// TODO : 删除前的逻辑判断 
        Map<String, Object> yearParam = new HashMap<String, Object>();
        yearParam.put("plan_year", param.get("plan_year"));
		// Table:d_compulsory_courses
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
        count = this.delete("d_compulsory_courses", mapCondition);

        Map<String, Object> countNum = compulCoursesMapper.getCountNum(yearParam);
        String coursesNum = StrUtil.getMapValue(countNum, "countNum");
        Map<String, Object> totalParam = new HashMap<String, Object>();
        totalParam.put("hours_compulsory", coursesNum);
        count += this.update(yearParam, "d_courses_hours", totalParam);

		return ServerRspUtil.formRspBodyVO(count, "删除必修课程安排表失败");
	}


}
