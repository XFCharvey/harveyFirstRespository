package com.cbox.business.main.service;

import java.util.ArrayList;
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
import com.cbox.base.utils.ObjUtil;
import com.cbox.base.utils.StrUtil;
import com.cbox.business.question.examrecord.mapper.ExamRecordMapper;
import com.cbox.business.customer.customer.mapper.CustomerMapper;
import com.cbox.business.worktask.worktask.mapper.WorktaskMapper;
import com.cbox.business.customer.customerhouses.mapper.CustomerHousesMapper;
import com.cbox.business.project.statistics.mapper.StatisticsMapper;

/**
 * @ClassName: MainCountService
 * @Function: 相关统计
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class MainCountService extends BaseService {

	@Autowired
	private CustomerMapper customerMapper;

	@Autowired
	private WorktaskMapper worktaskMapper;

	@Autowired
	private ExamRecordMapper examRecordMapper;

	@Autowired
	private CustomerHousesMapper customerHousesMapper;

	@Autowired
	private StatisticsMapper statisticsMapper;

	/** getmaincount : 获取app首页客户总数，阿隆、官微专注总数，风险预警及新增总数，阶段调研总数 **/
	public ResponseBodyVO getmaincount(Map<String, Object> param) {
		super.appendUserInfo(param);
		Map<String, Object> mapResult = new HashMap<String, Object>();

		// step1: 关联客户总数，昨日新增，阿隆|官微绑定数
		List<Map<String, Object>> listCusHouses = customerMapper.listCustomerApp(param);
		int customerTotal = 0;
		int iAlongFollow = 0;
		int iOfficialFollow = 0;
		int iCusNewlyAdded = 0;
		for (int i = 0; i < listCusHouses.size(); i++) {
			Map<String, Object> customer = listCusHouses.get(i);
			String customerStatus = StrUtil.getMapValue(customer, "customer_status");
			if (customerStatus.equals("5") || customerStatus.equals("8")) {
				customerTotal = customerTotal + 1;
				if (StrUtil.getMapValue(customer, "along_follow").equals("1")) {
					iAlongFollow = iAlongFollow + 1;
				}
				if (StrUtil.getMapValue(customer, "official_micro_follow").equals("1")) {
					iOfficialFollow = iOfficialFollow + 1;
				}
				if (StrUtil.getMapValue(customer, "rec_time").contains(DateUtils.getDate())) {
					iOfficialFollow = iOfficialFollow + 1;
				}
			}
		}
		Map<String, Object> mapCustomer = new HashMap<String, Object>();
		mapCustomer.put("customer_total", customerTotal);
		mapCustomer.put("cus_newly_added", iCusNewlyAdded);
		mapCustomer.put("along_total", iAlongFollow);
		mapCustomer.put("official_total", iOfficialFollow);
		mapResult.put("customer", mapCustomer);

		// step2: 风险预警，昨日新增
		Map<String, Object> mapTaskWarn = new HashMap<String, Object>();
		Map<String, Object> mapWarnParam = new HashMap<String, Object>();
		mapWarnParam.put("warning", "5");
		List<Map<String, Object>> listTaskWarn = worktaskMapper.listOverdueWorktask(mapWarnParam);
		int iWarnNewlyAdded = 0;
		for (int i = 0; i < listTaskWarn.size(); i++) {
			Map<String, Object> workTask = listTaskWarn.get(i);
			if (StrUtil.getMapValue(workTask, "date_disparity").equals("5")) {
				iWarnNewlyAdded = iWarnNewlyAdded + 1;
			}
		}

		mapTaskWarn.put("warn_total", listTaskWarn.size());
		mapTaskWarn.put("warn_newly_added", iWarnNewlyAdded);
		mapResult.put("task_warning", mapTaskWarn);

		// step3: 阶段调研：固定统计172，173，174，175
		Map<String, Object> mapQueryParam = new HashMap<String, Object>();// 查询参数
		mapQueryParam.put("exam_range", "172,173,174,175");
		List<Map<String, Object>> listExeamRecord = examRecordMapper.listExamRecord(mapQueryParam);
		int iExamCount = 0;
		if (ObjUtil.isNotNull(listExeamRecord)) {
			iExamCount = listExeamRecord.size();
		}
		mapResult.put("exam_count", iExamCount);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

}
