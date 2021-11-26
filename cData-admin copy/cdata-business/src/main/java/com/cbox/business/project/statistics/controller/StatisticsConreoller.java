package com.cbox.business.project.statistics.controller;

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
import com.cbox.business.project.statistics.service.StatisticsService;

/**
 * @ClassName: StatisticsConreoller
 * @Function: 项目相关统计
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/project/statistics")
public class StatisticsConreoller extends BaseController {

	@Autowired
	private StatisticsService statisticsService;

	/** listProjectQuestionTotal : 获取不同项目昨日新增，交付统计列表 **/
	@RequestMapping(value = "listProjectQuestionTotal", method = RequestMethod.POST)
	public TableDataInfo listProjectQuestionTotal(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list = statisticsService.listProjectQuestionTotal(param);

		return getDataTable(list);
	}

	/**
	 * getProjectProblemData : 获取指定时间范围内指定项目项目、具体问题类型 房间数、问题数统计\部位统计饼图统计\工地承建商统计
	 **/
	@RequestMapping(value = "getProjectProblemData", method = RequestMethod.POST)
	public ResponseBodyVO getProjectProblemData(@RequestBody Map<String, Object> param) {

		return statisticsService.getProjectProblemData(param);
	}

	/** listProjectJfQyTotal : 获取项目客户总数，昨日新增交付、开放问题数数据列表 **/
	@RequestMapping(value = "listProjectJfQyTotal", method = RequestMethod.POST)
	public TableDataInfo listProjectJfQyTotal(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list = statisticsService.listProjectJfQyTotal(param);

		return getDataTable(list);
	}

	/** listProjectSurveyTotal : 获取不同项目不同阶段调研总量列表数据 **/
	@RequestMapping(value = "listProjectSurveyTotal", method = RequestMethod.POST)
	public TableDataInfo listProjectSurveyTotal(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list = statisticsService.listProjectSurveyTotal(param);

		return getDataTable(list);
	}

	/** listProjectSurveyYesAdd : 获取不同项目不同阶段调研昨日新增量列表数据 **/
	@RequestMapping(value = "listProjectSurveyYesAdd", method = RequestMethod.POST)
	public TableDataInfo listProjectSurveyYesAdd(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list = statisticsService.listProjectSurveyYesAdd(param);

		return getDataTable(list);
	}

	/** listProjectcomplainByMonth : 获取不同或指定项目今年本月 投诉量，关闭率，回访满意度列表数据 **/
	@RequestMapping(value = "listProjectcomplainByMonth", method = RequestMethod.POST)
	public TableDataInfo listProjectcomplainByMonth(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list = statisticsService.listProjectcomplainByMonth(param);

		return getDataTable(list);
	}

	/** listProjectcomplainByQuarter : 获取不同或指定项目今年本季度 投诉量，关闭率，回访满意度列表数据 **/
	@RequestMapping(value = "listProjectcomplainByQuarter", method = RequestMethod.POST)
	public TableDataInfo listProjectcomplainByQuarter(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list = statisticsService.listProjectcomplainByQuarter(param);

		return getDataTable(list);
	}

	/** listProjectcomplainByYear : 获取不同或指定项目今年 投诉量，关闭率，回访满意度列表数据 **/
	@RequestMapping(value = "listProjectcomplainByYear", method = RequestMethod.POST)
	public TableDataInfo listProjectcomplainByYear(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list = statisticsService.listProjectcomplainByYear(param);

		return getDataTable(list);
	}

	/** listCusTotalAndYesAdd : 获取项目客户总数，昨日新增数数据列表 **/
	@RequestMapping(value = "listCusTotalAndYesAdd", method = RequestMethod.POST)
	public TableDataInfo listCusTotalAndYesAdd(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list = statisticsService.listCusTotalAndYesAdd(param);

		return getDataTable(list);
	}

	/** listProCusTypeGroup : 获取项目客户类型分类统计数据列表 **/
	@RequestMapping(value = "listProCusTypeGroup", method = RequestMethod.POST)
	public TableDataInfo listProCusTypeGroup(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list = statisticsService.listProCusTypeGroup(param);

		return getDataTable(list);
	}

	/** listProCusStatusGroup : 获取项目客户状态分类统计数据列表 **/
	@RequestMapping(value = "listProCusStatusGroup", method = RequestMethod.POST)
	public TableDataInfo listProCusStatusGroup(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list = statisticsService.listProCusStatusGroup(param);

		return getDataTable(list);
	}

	/** listProCusLevelGroup : 获取项目客户级别分类统计数据列表 **/
	@RequestMapping(value = "listProCusLevelGroup", method = RequestMethod.POST)
	public TableDataInfo listProCusLevelGroup(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list = statisticsService.listProCusLevelGroup(param);

		return getDataTable(list);
	}

	/** listProCusContractTimeGroup : 获取项目客户签约时间分布统计数据列表 **/
	@RequestMapping(value = "listProCusContractTimeGroup", method = RequestMethod.POST)
	public TableDataInfo listProCusContractTimeGroup(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list = statisticsService.listProCusContractTimeGroup(param);

		return getDataTable(list);
	}

	/** listProCusAgeGroup : 获取项目客户年龄分布统计数据列表 **/
	@RequestMapping(value = "listProCusAgeGroup", method = RequestMethod.POST)
	public TableDataInfo listProCusAgeGroup(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list = statisticsService.listProCusAgeGroup(param);

		return getDataTable(list);
	}

	/** listProCusBindGroup : 获取项目阿隆官微绑定统计数据列表 **/
	@RequestMapping(value = "listProCusBindGroup", method = RequestMethod.POST)
	public TableDataInfo listProCusBindGroup(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list = statisticsService.listProCusBindGroup(param);

		return getDataTable(list);
	}

	/** getCusTotalAndYesAdd : 获取所有项目客户总数，昨日新增数数据 **/
	@RequestMapping(value = "getCusTotalAndYesAdd", method = RequestMethod.POST)
	public ResponseBodyVO getCusTotalAndYesAdd(@RequestBody Map<String, Object> param) {

		return statisticsService.getCusTotalAndYesAdd(param);
	}

	/** getProCusTypeGroup : 获取所有项目客户类型分类统计数据 **/
	@RequestMapping(value = "getProCusTypeGroup", method = RequestMethod.POST)
	public ResponseBodyVO getProCusTypeGroup(@RequestBody Map<String, Object> param) {

		return statisticsService.getProCusTypeGroup(param);
	}

	/** getProCusStatusGroup : 获取所有项目客户状态分类统计数据 **/
	@RequestMapping(value = "getProCusStatusGroup", method = RequestMethod.POST)
	public ResponseBodyVO getProCusStatusGroup(@RequestBody Map<String, Object> param) {

		return statisticsService.getProCusStatusGroup(param);
	}

	/** getProCusLevelGroup : 获取所有项目客户级别分类统计数据 **/
	@RequestMapping(value = "getProCusLevelGroup", method = RequestMethod.POST)
	public ResponseBodyVO getProCusLevelGroup(@RequestBody Map<String, Object> param) {

		return statisticsService.getProCusLevelGroup(param);
	}

	/** getProCusContractTimeGroup : 获取所有项目客户签约时间分布统计数据 **/
	@RequestMapping(value = "getProCusContractTimeGroup", method = RequestMethod.POST)
	public ResponseBodyVO getProCusContractTimeGroup(@RequestBody Map<String, Object> param) {

		return statisticsService.getProCusContractTimeGroup(param);
	}

	/** getProCusAgeGroup : 获取所有项目客户年龄分布统计数据 **/
	@RequestMapping(value = "getProCusAgeGroup", method = RequestMethod.POST)
	public ResponseBodyVO getProCusAgeGroup(@RequestBody Map<String, Object> param) {

		return statisticsService.getProCusAgeGroup(param);
	}

}
