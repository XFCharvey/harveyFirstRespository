package com.cbox.business.compulcourses.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cbox.base.core.controller.BaseController;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.page.TableDataInfo;
import com.cbox.business.compulcourses.service.CompulCoursesService;


/**
 * @ClassName: CompulCoursesController
 * @Function: 必修课程安排表
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/compulcourses")
public class CompulCoursesController extends BaseController {

	@Autowired
	private CompulCoursesService compulCoursesService;

	
	/** listCompulCourses : 获取必修课程安排表列表数据 **/
	@RequestMapping(value = "listCompulCourses", method = RequestMethod.POST)
	public TableDataInfo listCompulCourses(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list= compulCoursesService.listCompulCourses(param);

		return getDataTable(list);
	}

    /** listCompulCoursesByUser : 获取必修课程安排表列表数据 **/
    @RequestMapping(value = "listCompulCoursesByUser", method = RequestMethod.POST)
    public TableDataInfo listCompulCoursesByUser(@RequestBody Map<String, Object> param) {

        startPage();
        List<Map<String, Object>> list = compulCoursesService.listCompulCoursesByUser(param);

        return getDataTable(list);
    }
	/** getCompulCourses : 获取指定id的必修课程安排表数据 **/
	@RequestMapping(value = "getCompulCourses", method = RequestMethod.POST)
	public ResponseBodyVO getCompulCourses(@RequestBody Map<String, Object> param) {

		return compulCoursesService.getCompulCourses(param);
	}

	/** addCompulCourses : 新增必修课程安排表 **/
	@RequestMapping(value = "addCompulCourses", method = RequestMethod.POST)
	public ResponseBodyVO addCompulCourses(@RequestBody Map<String, Object> param) {

		// 校验必填参数
        String checkResult = this.validInput("course_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return compulCoursesService.addCompulCourses(param);
	}

	/** updateCompulCourses : 修改必修课程安排表 **/
	@RequestMapping(value = "updateCompulCourses", method = RequestMethod.POST)
	public ResponseBodyVO updateCompulCourses(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return compulCoursesService.updateCompulCourses(param);
	}

	/** delCompulCourses : 删除必修课程安排表 **/
	@RequestMapping(value = "delCompulCourses", method = RequestMethod.POST)
	public ResponseBodyVO delCompulCourses(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return compulCoursesService.delCompulCourses(param);
	}


}
