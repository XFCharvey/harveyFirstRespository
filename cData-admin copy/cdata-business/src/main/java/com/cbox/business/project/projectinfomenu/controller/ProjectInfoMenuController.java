package com.cbox.business.project.projectinfomenu.controller;

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
import com.cbox.business.project.projectinfomenu.service.ProjectInfoMenuService;


/**
 * @ClassName: ProjectInfoMenuController
 * @Function: 项目资料分类
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/project/projectinfomenu")
public class ProjectInfoMenuController extends BaseController {

	@Autowired
	private ProjectInfoMenuService projectInfoMenuService;

	
	/** listProjectInfoMenu : 获取项目资料分类列表数据 **/
	@RequestMapping(value = "listProjectInfoMenu", method = RequestMethod.POST)
	public TableDataInfo listProjectInfoMenu(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list= projectInfoMenuService.listProjectInfoMenu(param);

		return getDataTable(list);
	}

    /** listProjectInfoMenutree : 获取项目资料分类目录列表返回树形数据 **/
    @RequestMapping(value = "listProjectInfoMenutree", method = RequestMethod.POST)
    public ResponseBodyVO listProjectInfoMenutree(@RequestBody Map<String, Object> param) {

        return projectInfoMenuService.listProjectInfoMenutree(param);
    }

	/** getProjectInfoMenu : 获取指定id的项目资料分类数据 **/
	@RequestMapping(value = "getProjectInfoMenu", method = RequestMethod.POST)
	public ResponseBodyVO getProjectInfoMenu(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectInfoMenuService.getProjectInfoMenu(param);
	}

	/** addProjectInfoMenu : 新增项目资料分类 **/
	@RequestMapping(value = "addProjectInfoMenu", method = RequestMethod.POST)
	public ResponseBodyVO addProjectInfoMenu(@RequestBody Map<String, Object> param) {

		// 校验必填参数
        String checkResult = this.validInput("menu_name", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectInfoMenuService.addProjectInfoMenu(param);
	}

	/** updateProjectInfoMenu : 修改项目资料分类 **/
	@RequestMapping(value = "updateProjectInfoMenu", method = RequestMethod.POST)
	public ResponseBodyVO updateProjectInfoMenu(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectInfoMenuService.updateProjectInfoMenu(param);
	}

	/** delProjectInfoMenu : 删除项目资料分类 **/
	@RequestMapping(value = "delProjectInfoMenu", method = RequestMethod.POST)
	public ResponseBodyVO delProjectInfoMenu(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectInfoMenuService.delProjectInfoMenu(param);
	}


}
