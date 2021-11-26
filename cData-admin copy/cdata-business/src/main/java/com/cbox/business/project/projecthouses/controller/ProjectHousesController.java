package com.cbox.business.project.projecthouses.controller;

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
import com.cbox.business.project.projecthouses.service.ProjectHousesService;


/**
 * @ClassName: ProjectHousesController
 * @Function: 项目楼栋房源
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/project/projecthouses")
public class ProjectHousesController extends BaseController {

	@Autowired
	private ProjectHousesService projectHousesService;

	
	/** listProjectHouses : 获取项目楼栋房源列表数据 **/
	@RequestMapping(value = "listProjectHouses", method = RequestMethod.POST)
	public TableDataInfo listProjectHouses(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list= projectHousesService.listProjectHouses(param);

		return getDataTable(list);
	}
	
	/** listHouseOfCustomerId : 获取指定id客户的房源列表 **/
	@RequestMapping(value = "listHouseOfCustomerId", method = RequestMethod.POST)
	public TableDataInfo listHouseOfCustomerId(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list= projectHousesService.listHouseOfCustomerId(param);

		return getDataTable(list);
	}

	/** getProjectHouses : 获取指定id的项目楼栋房源数据 **/
	@RequestMapping(value = "getProjectHouses", method = RequestMethod.POST)
	public ResponseBodyVO getProjectHouses(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectHousesService.getProjectHouses(param);
	}
	
	/** getHousesStatusGroup : 获取指定id项目的项目楼栋房源状态分组数量 **/
	@RequestMapping(value = "getHousesStatusGroup", method = RequestMethod.POST)
	public ResponseBodyVO getHousesStatusGroup(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("project_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectHousesService.getHousesStatusGroup(param);
	}

	/** addProjectHouses : 新增项目楼栋房源 **/
	@RequestMapping(value = "addProjectHouses", method = RequestMethod.POST)
	public ResponseBodyVO addProjectHouses(@RequestBody Map<String, Object> param) {

		// 校验必填参数
        String checkResult = this.validInput("project_id,building_id,house_name,house_area,single_price,total_price", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectHousesService.addProjectHouses(param);
	}

	/** updateProjectHouses : 修改项目楼栋房源 **/
	@RequestMapping(value = "updateProjectHouses", method = RequestMethod.POST)
	public ResponseBodyVO updateProjectHouses(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectHousesService.updateProjectHouses(param);
	}

	/** delProjectHouses : 删除项目楼栋房源 **/
	@RequestMapping(value = "delProjectHouses", method = RequestMethod.POST)
	public ResponseBodyVO delProjectHouses(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectHousesService.delProjectHouses(param);
	}


}
