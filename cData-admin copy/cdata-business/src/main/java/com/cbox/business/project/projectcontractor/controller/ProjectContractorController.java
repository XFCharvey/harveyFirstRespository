package com.cbox.business.project.projectcontractor.controller;

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
import com.cbox.business.project.projectcontractor.service.ProjectContractorService;

/**
 * @ClassName: ProjectContractorController
 * @Function: 项目承建商
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/project/projectcontractor")
public class ProjectContractorController extends BaseController {

	@Autowired
	private ProjectContractorService projectContractorService;

	/** listProjectContractor : 获取项目承建商列表数据 **/
	@RequestMapping(value = "listProjectContractor", method = RequestMethod.POST)
	public TableDataInfo listProjectContractor(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list = projectContractorService.listProjectContractor(param);

		return getDataTable(list);
	}

	/** getProjectContractor : 获取指定id的项目承建商数据 **/
	@RequestMapping(value = "getProjectContractor", method = RequestMethod.POST)
	public ResponseBodyVO getProjectContractor(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectContractorService.getProjectContractor(param);
	}
	
	/** addProjectContractorBatch : 获取指定id的项目承建商数据 
	 * @throws Exception **/
	@RequestMapping(value = "addProjectContractorBatch", method = RequestMethod.POST)
	public ResponseBodyVO addProjectContractorBatch(@RequestBody Map<String, Object> param) throws Exception {

		// 校验必填参数
		String checkResult = this.validInput("import_file", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectContractorService.addProjectContractorBatch(param);
	}

	/** addProjectContractor : 新增项目承建商 **/
	@RequestMapping(value = "addProjectContractor", method = RequestMethod.POST)
	public ResponseBodyVO addProjectContractor(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("contractor_name,project_id,contractor_person,contractor_phone", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectContractorService.addProjectContractor(param);
	}

	/** updateProjectContractor : 修改项目承建商 **/
	@RequestMapping(value = "updateProjectContractor", method = RequestMethod.POST)
	public ResponseBodyVO updateProjectContractor(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectContractorService.updateProjectContractor(param);
	}

	/** delProjectContractor : 删除项目承建商 **/
	@RequestMapping(value = "delProjectContractor", method = RequestMethod.POST)
	public ResponseBodyVO delProjectContractor(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectContractorService.delProjectContractor(param);
	}

}
