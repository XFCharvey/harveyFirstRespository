package com.cbox.business.project.statistics.service;

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
import com.cbox.business.project.statistics.mapper.StatisticsMapper;

/**
 * @ClassName: StatisticsService
 * @Function: 项目相关统计
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class StatisticsService extends BaseService {

	@Autowired
	private StatisticsMapper statisticsMapper;

	/** listProjectQuestionTotal : 获取不同项目昨日新增，交付统计列表 **/
	public List<Map<String, Object>> listProjectQuestionTotal(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = statisticsMapper.listProjectQuestionTotal(param);

		return list;
	}

	/** getProjectProblemData : 获取指定时间范围内指定项目项目、具体问题类型 房间数、问题数统计\部位统计饼图统计\工地承建商统计 **/
	public ResponseBodyVO getProjectProblemData(Map<String, Object> param) {
		super.appendUserInfo(param);
		Map<String, Object> mapResult = new HashMap<String, Object>();

		Map<String, Object> roomAndPStatistic = statisticsMapper.getProjectproblemTotal(param);
		List<Map<String, Object>> positionTotalList = statisticsMapper.listProjectQPositionGroup(param);
		List<Map<String, Object>> contractorTotalList = statisticsMapper.listProjectQContractorGroup(param);

		mapResult.put("roomAndP_statistic", roomAndPStatistic);
		mapResult.put("position_statistic", positionTotalList);
		mapResult.put("contractor_statistic", contractorTotalList);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);

	}
	
	/** listProjectJfQyTotal : 获取项目客户总数，昨日新增交付、开放问题数数据列表 **/
	public List<Map<String, Object>> listProjectJfQyTotal(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = statisticsMapper.listProjectJfQyTotal(param);

		return list;
	}

	/** listProjectSurveyTotal : 获取不同项目不同阶段调研总量列表数据 **/
	public List<Map<String, Object>> listProjectSurveyTotal(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = statisticsMapper.listProjectSurveyTotal(param);

		return list;
	}

	/** listProjectSurveyYesAdd : 获取不同项目不同阶段调研昨日新增量列表数据 **/
	public List<Map<String, Object>> listProjectSurveyYesAdd(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = statisticsMapper.listProjectSurveyYesAdd(param);

		return list;
	}

	/** listProjectcomplainByMonth : 获取不同或指定项目今年本月 投诉量，关闭率，回访满意度列表数据 **/
	public List<Map<String, Object>> listProjectcomplainByMonth(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = statisticsMapper.listProjectcomplainByMonth(param);

		return list;
	}

	/** listProjectcomplainByQuarter : 获取不同或指定项目今年本季度 投诉量，关闭率，回访满意度列表数据 **/
	public List<Map<String, Object>> listProjectcomplainByQuarter(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = statisticsMapper.listProjectcomplainByQuarter(param);

		return list;
	}

	/** listProjectcomplainByYear : 获取不同或指定项目今年 投诉量，关闭率，回访满意度列表数据 **/
	public List<Map<String, Object>> listProjectcomplainByYear(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = statisticsMapper.listProjectcomplainByYear(param);

		return list;
	}

	/** listCusTotalAndYesAdd : 获取项目客户总数，昨日新增数数据列表 **/
	public List<Map<String, Object>> listCusTotalAndYesAdd(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = statisticsMapper.listCusTotalAndYesAdd(param);

		return list;
	}

	/** listProCusTypeGroup : 获取项目客户类型分类统计数据列表 **/
	public List<Map<String, Object>> listProCusTypeGroup(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = statisticsMapper.listProCusTypeGroup(param);

		return list;
	}

	/** listProCusStatusGroup : 获取项目客户状态分类统计数据列表 **/
	public List<Map<String, Object>> listProCusStatusGroup(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = statisticsMapper.listProCusStatusGroup(param);

		return list;
	}

	/** listProCusLevelGroup : 获取项目客户级别分类统计数据列表 **/
	public List<Map<String, Object>> listProCusLevelGroup(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = statisticsMapper.listProCusLevelGroup(param);

		return list;
	}

	/** listProCusContractTimeGroup : 获取项目客户签约时间分布统计数据列表 **/
	public List<Map<String, Object>> listProCusContractTimeGroup(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = statisticsMapper.listProCusContractTimeGroup(param);

		return list;
	}

	/** listProCusAgeGroup : 获取项目客户年龄分布统计数据列表 **/
	public List<Map<String, Object>> listProCusAgeGroup(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = statisticsMapper.listProCusAgeGroup(param);

		return list;
	}

	/** listProCusBindGroup : 获取项目阿隆官微绑定统计数据列表 **/
	public List<Map<String, Object>> listProCusBindGroup(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = statisticsMapper.listProCusBindGroup(param);

		return list;
	}

	/** getCusTotalAndYesAdd : 获取所有项目客户总数，昨日新增数数据 **/
	public ResponseBodyVO getCusTotalAndYesAdd(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = statisticsMapper.getCusTotalAndYesAdd(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);

	}

	/** getProCusTypeGroup : 获取所有项目客户类型分类统计数据 **/
	public ResponseBodyVO getProCusTypeGroup(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = statisticsMapper.getProCusTypeGroup(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);

	}

	/** getProCusStatusGroup : 获取所有项目客户状态分类统计数据 **/
	public ResponseBodyVO getProCusStatusGroup(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = statisticsMapper.getProCusStatusGroup(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);

	}

	/** getProCusLevelGroup : 获取所有项目客户级别分类统计数据 **/
	public ResponseBodyVO getProCusLevelGroup(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = statisticsMapper.getProCusLevelGroup(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);

	}

	/** getProCusContractTimeGroup : 获取所有项目客户签约时间分布统计数据 **/
	public ResponseBodyVO getProCusContractTimeGroup(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = statisticsMapper.getProCusContractTimeGroup(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);

	}

	/** getProCusAgeGroup : 获取所有项目客户年龄分布统计数据 **/
	public ResponseBodyVO getProCusAgeGroup(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = statisticsMapper.getProCusAgeGroup(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);

	}

}
