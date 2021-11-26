package com.cbox.business.project.statistics.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: StatisticsMapper
 * @Function: 项目相关统计
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface StatisticsMapper {

	/** listProjectQuestionTotal : 获取不同项目交付，开放统计列表 **/
	public List<Map<String, Object>> listProjectQuestionTotal(Map<String, Object> param);

	/** getProjectproblemTotal : 获取指定项目、具体问题类型 房间数、问题数统计 **/
	public Map<String, Object> getProjectproblemTotal(Map<String, Object> param);

	/** listProjectQPositionGroup : 获取指定项目、问题类型工地按部位统计饼图统计 **/
	public List<Map<String, Object>> listProjectQPositionGroup(Map<String, Object> param);

	/** listProjectQContractorGroup :获取指定项目、问题类型工地承建商统计列表 **/
	public List<Map<String, Object>> listProjectQContractorGroup(Map<String, Object> param);

	/** listProjectJfQyTotal : 获取项目客户总数，昨日新增交付、开放问题数数据列表 **/
	public List<Map<String, Object>> listProjectJfQyTotal(Map<String, Object> param);

	/** listProjectSurveyTotal : 获取不同项目不同阶段调研总量列表数据 **/
	public List<Map<String, Object>> listProjectSurveyTotal(Map<String, Object> param);

	/** listProjectSurveyYesAdd : 获取不同项目不同阶段调研昨日新增量列表数据 **/
	public List<Map<String, Object>> listProjectSurveyYesAdd(Map<String, Object> param);

	/** listProjectcomplainByMonth : 获取不同或指定项目今年本月 投诉量，关闭率，回访满意度列表数据 **/
	public List<Map<String, Object>> listProjectcomplainByMonth(Map<String, Object> param);

	/** listCusTotalAndYesAdd : 获取不同或指定项目今年本季度 投诉量，关闭率，回访满意度列表数据 **/
	public List<Map<String, Object>> listCusTotalAndYesAdd(Map<String, Object> param);

	/** listProjectcomplainByQuarter : 获取项目客户总数，昨日新增数数据列表 **/
	public List<Map<String, Object>> listProjectcomplainByQuarter(Map<String, Object> param);

	/** listProjectcomplainByYear : 获取不同或指定项目今年 投诉量，关闭率，回访满意度列表数据 **/
	public List<Map<String, Object>> listProjectcomplainByYear(Map<String, Object> param);

	/** listProCusTypeGroup : 获取项目客户类型分类统计数据列表 **/
	public List<Map<String, Object>> listProCusTypeGroup(Map<String, Object> param);

	/** listProCusStatusGroup : 获取项目客户状态分类统计数据列表 **/
	public List<Map<String, Object>> listProCusStatusGroup(Map<String, Object> param);

	/** listProCusLevelGroup : 获取项目客户级别分类统计数据列表 **/
	public List<Map<String, Object>> listProCusLevelGroup(Map<String, Object> param);

	/** listProCusContractTimeGroup : 获取项目客户签约时间分布统计数据列表 **/
	public List<Map<String, Object>> listProCusContractTimeGroup(Map<String, Object> param);

	/** listProCusAgeGroup : 获取项目客户年龄分布统计数据列表 **/
	public List<Map<String, Object>> listProCusAgeGroup(Map<String, Object> param);

	/** "listProCusBindGroup" : 获取项目阿隆官微绑定统计数据列表 **/
	public List<Map<String, Object>> listProCusBindGroup(Map<String, Object> param);

	/** getCusTotalAndYesAdd : 获取所有项目客户总数，昨日新增数数据 **/
	public Map<String, Object> getCusTotalAndYesAdd(Map<String, Object> param);

	/** getProCusTypeGroup : 获取所有项目客户类型分类统计数据 **/
	public Map<String, Object> getProCusTypeGroup(Map<String, Object> param);

	/** getProCusStatusGroup : 获取所有项目客户状态分类统计数据 **/
	public Map<String, Object> getProCusStatusGroup(Map<String, Object> param);

	/** getProCusLevelGroup : 获取所有项目客户级别分类统计数据 **/
	public Map<String, Object> getProCusLevelGroup(Map<String, Object> param);

	/** getProCusContractTimeGroup : 获取所有项目客户签约时间分布统计数据 **/
	public Map<String, Object> getProCusContractTimeGroup(Map<String, Object> param);

	/** getProCusAgeGroup : 获取所有项目客户年龄分布统计数据 **/
	public Map<String, Object> getProCusAgeGroup(Map<String, Object> param);

}
