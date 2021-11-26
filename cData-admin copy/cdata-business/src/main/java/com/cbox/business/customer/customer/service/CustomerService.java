package com.cbox.business.customer.customer.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbox.base.constant.HttpStatus;
import com.cbox.base.core.domain.AjaxResult;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.service.BaseService;
import com.cbox.base.utils.DateUtils;
import com.cbox.base.utils.ObjUtil;
import com.cbox.base.utils.SecurityUtils;
import com.cbox.base.utils.StrUtil;
import com.cbox.base.utils.poi.ExcelUtil;
import com.cbox.business.activity.activitysignin.mapper.ActivitySigninMapper;
import com.cbox.business.customer.customer.bean.CustomerImport;
import com.cbox.business.customer.customer.bean.CustomerVo;
import com.cbox.business.customer.customer.mapper.CustomerMapper;
import com.cbox.business.customer.customerfamily.mapper.CustomerFamilyMapper;
import com.cbox.business.customer.customerhouses.mapper.CustomerHousesMapper;
import com.cbox.business.question.examrecord.mapper.ExamRecordMapper;
import com.cbox.business.question.examrecorddetail.mapper.ExamRecordDetailMapper;
import com.cbox.business.worktask.worktask.mapper.WorktaskMapper;
import com.cbox.common.service.AttachFileService;
import com.cbox.business.customer.customerrelation.mapper.CustomerRelationMapper;

/**
 * @ClassName: CustomerService
 * @Function: 客户信息
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class CustomerService extends BaseService {

	@Autowired
	private CustomerMapper customerMapper;

	@Autowired
	private CustomerRelationMapper customerRelationMapper;

	@Autowired
	private AttachFileService attachFileService;

	@Autowired
	private CustomerHousesMapper customerHousesMapper;

	@Autowired
	private WorktaskMapper worktaskMapper;

	@Autowired
	private CustomerFamilyMapper customerFamilyMapper;

	@Autowired
	private ActivitySigninMapper activitySigninMapper;

	@Autowired
	private ExamRecordMapper examRecordMapper;

	@Autowired
	private ExamRecordDetailMapper examRecordDetailMapper;

	/** listCustomer : 获取客户信息列表数据 **/
	public List<Map<String, Object>> listCustomer(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = customerMapper.listCustomer(param);

		// 查询该用户的关联人房间数
		Map<String, Object> mapRelationCus = new HashMap<String, Object>();
		List<Map<String, Object>> listRelationCus = customerRelationMapper.listCustomerRelation(mapRelationCus);
		Map<String, List<Map<String, Object>>> mapListCustomers = ObjUtil.transList(listRelationCus, "customer_id");

		if (ObjUtil.isNotNull(list)) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> customerVO = list.get(i);
				String customerId = StrUtil.getMapValue(customerVO, "rec_id");
				List<Map<String, Object>> relationCusList = mapListCustomers.get(customerId);
				int relationCusTotal = 0;
				if(ObjUtil.isNotNull(relationCusList)) {
					relationCusTotal = relationCusList.size();
				}
				customerVO.put("relation_cusTotal", relationCusTotal);
			}
		}

		// 查询用户的房产数据
//		Map<String, Object> mapCustomerHousesCondition = new HashMap<String, Object>();
//		List<Map<String, Object>> listCustomerHouses = customerHousesMapper
//				.listCustomerHouses(mapCustomerHousesCondition);
//		Map<String, List<Map<String, Object>>> mapListCustomerHouse = ObjUtil.transList(listCustomerHouses,
//				"customer_id");
//
//		for (int i = 0; i < list.size(); i++) {
//			Map<String, Object> mapCustomer = list.get(i);
//			String customerId = StrUtil.getMapValue(mapCustomer, "rec_id");
//
//			// 查询该客户的房产关联数据
//			List<Map<String, Object>> listCustomerHouse = mapListCustomerHouse.get(customerId);
//			if (ObjUtil.isNotNull(listCustomerHouse)) {
//				mapCustomer.put("lately_house", listCustomerHouse.get(0));
//			}
//		}
		return list;
	}

	/** listCustomerOfHouse : 获取导出客户信息列表数据 **/
	public List<Map<String, Object>> listCustomerOfHouse(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = customerMapper.listCustomerOfHouse(param);

		return list;
	}

	/** exportCustomerOfHouse : 导出客户与房间信息列表excel文件 **/
	public AjaxResult exportCustomerOfHouse(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = customerMapper.listCustomerOfHouse(param);
		List<CustomerVo> listCustomerVo = new ArrayList<CustomerVo>();
		if (ObjUtil.isNotNull(list)) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> mapCustomerVo = list.get(i);
				CustomerVo customerVo = new CustomerVo();
				customerVo.setCustomer_name(StrUtil.getMapValue(mapCustomerVo, "customer_name"));
				customerVo.setCustomer_phone(StrUtil.getMapValue(mapCustomerVo, "customer_phone"));
				customerVo.setCustomer_addr(StrUtil.getMapValue(mapCustomerVo, "customer_addr"));
				customerVo.setCustomer_id(StrUtil.getMapValue(mapCustomerVo, "customer_id"));
				customerVo.setHouse_name(StrUtil.getMapValue(mapCustomerVo, "house_name"));
				listCustomerVo.add(customerVo);
			}
		}

		// 解析excel文件
		ExcelUtil<CustomerVo> util = new ExcelUtil<CustomerVo>(CustomerVo.class);
		String sheetName = "客户房间信息文件";
		AjaxResult result = util.exportExcel(listCustomerVo, sheetName);
		return result;
	}

	/** listCustomerApp : app获取客户信息列表数据 **/
	public List<Map<String, Object>> listCustomerApp(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = customerMapper.listCustomerApp(param);

		return list;
	}

	/** getCustomer : 获取指定id的客户信息数据 **/
	public ResponseBodyVO getCustomer(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = customerMapper.getCustomer(param);
		String customerId = StrUtil.getMapValue(mapResult, "rec_id");
		Map<String, Object> mapCustomerHousesCondition = new HashMap<String, Object>();
		mapCustomerHousesCondition.put("customer_id", customerId);
		List<Map<String, Object>> listCustomerHouse = customerHousesMapper
				.listCustomerHouses(mapCustomerHousesCondition);
		if (ObjUtil.isNotNull(listCustomerHouse) && listCustomerHouse.size() > 0) {
			mapResult.put("lately_house", listCustomerHouse.get(0));
		}

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** getCustomerOtherTotal : 获取指定客户的统计数量模块数据值 **/
	public ResponseBodyVO getCustomerOtherTotal(Map<String, Object> param) {
		super.appendUserInfo(param);
		Map<String, Object> mapResult = new HashMap<String, Object>();

		String customerId = StrUtil.getMapValue(param, "rec_id");
		String customerPhone = StrUtil.getMapValue(param, "customer_phone");
		Map<String, Object> mapCustomerRecidCondition = new HashMap<String, Object>();
		mapCustomerRecidCondition.put("customer_id", customerId);
		// 查询该客户房产数量
		int customerHousesNum = 0;
		List<Map<String, Object>> listCustomerHouses = customerHousesMapper
				.listCustomerHouses(mapCustomerRecidCondition);
		if (ObjUtil.isNotNull(listCustomerHouses)) {
			customerHousesNum = listCustomerHouses.size();
		}
		mapResult.put("customer_houses_num", customerHousesNum);
		// 查询该客户投诉维修数量
		int customerWorkTaskNum = 0;
		Map<String, Object> mapCustomerWorkNum = worktaskMapper.getCustomerWorktaskNum(mapCustomerRecidCondition);
		customerWorkTaskNum = StrUtil.getMapIntValue(mapCustomerWorkNum, "customer_work_num");
		mapResult.put("customer_workTask_num", customerWorkTaskNum);
		// 查询该客户家庭成员数量
		int customerFamilyNum = 0;
		List<Map<String, Object>> listCustomerFamily = customerFamilyMapper
				.listCustomerFamily(mapCustomerRecidCondition);
		if (ObjUtil.isNotNull(listCustomerFamily)) {
			customerFamilyNum = listCustomerFamily.size();
		}
		mapResult.put("customer_family_num", customerFamilyNum);
		// 查询该客户参与过的活动数量（签到算参与）
		int customerActNum = 0;
		Map<String, Object> mapCustomerActCondition = new HashMap<String, Object>();
		mapCustomerActCondition.put("signin_phone", customerPhone);
		List<Map<String, Object>> listCustomerAct = activitySigninMapper.listActivitySignin(mapCustomerActCondition);
		if (ObjUtil.isNotNull(listCustomerAct)) {
			customerActNum = listCustomerAct.size();
		}
		mapResult.put("customer_act_num", customerActNum);
		// 查询该客户参与过的调研数量
		int customerAskNum = 0;
		Map<String, Object> mapCustomerAskCondition = new HashMap<String, Object>();
		mapCustomerAskCondition.put("person_phone", customerPhone);
		List<Map<String, Object>> listCustomerAsk = examRecordMapper.listCommonExamRecord(mapCustomerAskCondition);
		if (ObjUtil.isNotNull(listCustomerAsk)) {
			customerAskNum = listCustomerAsk.size();
		}
		mapResult.put("customer_ask_num", customerAskNum);
		// 查询该用户的偏好倾向数量
		int customerPreferenceNum = 0;
		Map<String, Object> mapCustomerPreferenceCondition = new HashMap<String, Object>();
		mapCustomerPreferenceCondition.put("person_phone", customerPhone);
		mapCustomerPreferenceCondition.put("qbank_id", "171");
		Map<String, Object> mapCustomerRecord = examRecordMapper.getExamRecordByUser(mapCustomerPreferenceCondition);
		if (ObjUtil.isNotNull(mapCustomerRecord)) {
			String recordID = StrUtil.getMapValue(mapCustomerRecord, "rec_id");
			Map<String, Object> mapCustomerRecordDetailCondition = new HashMap<String, Object>();
			mapCustomerRecordDetailCondition.put("record_id", recordID);
			List<Map<String, Object>> listCustomerRecordDetail = examRecordDetailMapper
					.listExamRecordDetail(mapCustomerRecordDetailCondition);
			if (ObjUtil.isNotNull(listCustomerRecordDetail)) {
				customerPreferenceNum = listCustomerRecordDetail.size();
			}
			mapResult.put("customer_preference_num", customerPreferenceNum);
		}

		// 查询该用户的关联人房间数
		Map<String, Object> mapRelationCus = new HashMap<String, Object>();
		mapRelationCus.put("customer_id", customerId);
		List<Map<String, Object>> listRelationCusHouses = customerRelationMapper.listRelationCusHouses(mapRelationCus);
		int total = 0;
		if (ObjUtil.isNotNull(listRelationCusHouses)) {
			total = listRelationCusHouses.size();
		}
		mapResult.put("relation_cus_houses_total", total);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addCustomer : 新增客户信息 **/
	public ResponseBodyVO addCustomer(Map<String, Object> param) {
		String nowTime = DateUtils.getTime();
		int count = 0;
		// Table:d_customer
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("customer_name", param.get("customer_name"));
		mapParam.put("customer_type", param.get("customer_type"));
		mapParam.put("customer_status", param.get("customer_status"));
		mapParam.put("customer_phone", param.get("customer_phone"));
		mapParam.put("customer_lable", param.get("customer_lable"));
		mapParam.put("customer_sex", param.get("customer_sex"));
		// 客户身份证
		String customerId = StrUtil.getMapValue(param, "customer_id");
		// 根据身份证拼接出生日年月份字符串
		String customer_birthdate = customerId.substring(6, 10) + "-" + customerId.substring(10, 12) + "-"
				+ customerId.substring(12, 14);
		mapParam.put("customer_id", customerId);
		mapParam.put("customer_birthdate", customer_birthdate);
		mapParam.put("customer_addr", param.get("customer_addr"));
		mapParam.put("official_micro_follow", param.get("official_micro_follow"));
		mapParam.put("along_follow", param.get("along_follow"));
		mapParam.put("customer_photo", param.get("customer_photo"));
		mapParam.put("customer_vocation", param.get("customer_vocation"));
		mapParam.put("customer_industry", param.get("customer_industry"));
		mapParam.put("customer_hobby", param.get("customer_hobby"));
		mapParam.put("customer_income", param.get("customer_income"));
		mapParam.put("customer_company", param.get("customer_company"));
		mapParam.put("customer_circle", param.get("customer_circle"));
		mapParam.put("focus", param.get("focus"));
		mapParam.put("recommender", param.get("recommender"));
		count += this.save("d_customer", mapParam);

		// 同时增加当前登录用户对该新客户的服务历史
		String newCustomerId = StrUtil.getMapValue(mapParam, "rec_id");
		String userName = SecurityUtils.getUsername();
		Map<String, Object> mapUserServeHisParam = new HashMap<String, Object>();
		mapUserServeHisParam.put("serve_person", userName);
		mapUserServeHisParam.put("served_customer", newCustomerId);
		mapUserServeHisParam.put("serve_count", 1);
		mapUserServeHisParam.put("serve_time", nowTime);

		count += this.save("d_user_servehis", mapUserServeHisParam);
		return ServerRspUtil.formRspBodyVO(count, "新增客户信息失败");
	}

	/** 文件批量导入客户 **/
	public ResponseBodyVO importBatchCustomer(Map<String, Object> param) {
		// TODO Auto-generated method stub
		int count = 0;
		int iSuccCount = 0;
		int iFailCount = 0;

		StringBuffer failBuffer = new StringBuffer();
		try {
			// 获取上传文件地址
			String fileId = StrUtil.getMapValue(param, "import_file");
			String fileName = attachFileService.getFileRealPath(fileId);
			File file = new File(fileName);

			// 解析excel文件
			ExcelUtil<CustomerImport> util = new ExcelUtil<CustomerImport>(CustomerImport.class);
			List<CustomerImport> customerImportList = util.importExcel(file);

			// 遍历数据
			for (int i = 0; i < customerImportList.size(); i++) {
				CustomerImport customerImport = customerImportList.get(i);
				// 插入参数
				Map<String, Object> verifyParams = new HashMap<String, Object>();
				// 验证是否存在相同的民兵身份证
//				Map<String, Object> idNumber = new HashMap<String, Object>();
//				idNumber.put("customer_id", customerImport.getCustomer_id());
//				List<Map<String, Object>> customerListByIDNumeResult = customerMapper.listCustomer(idNumber);
//
//				Map<String, Object> phoneNumber = new HashMap<String, Object>();
//				phoneNumber.put("customer_phone", customerImport.getCustomer_phone());
//				List<Map<String, Object>> customerListByPhoneResult = customerMapper.listCustomer(phoneNumber);

//				// 判断是否存在相同的身份证
//				if (ObjUtil.isNotNull(customerListByIDNumeResult) || customerListByIDNumeResult.size() > 0) {
//					// 已经存在，则跳过不添加
//					iFailCount++;
//					failBuffer.append(" 身份证为：“ " + customerImport.getCustomer_id() + " ” 的客户信息已存在。");
//					continue;
//				}
//				// 判断是否有相同的手机号存在
//				if (ObjUtil.isNotNull(customerListByPhoneResult) || customerListByPhoneResult.size() > 0) {
//					// 已经存在，则跳过不添加
//					iFailCount++;
//					failBuffer.append(" 手机号为：“ " + customerImport.getCustomer_phone() + " ” 的客户信息已存在。");
//					continue;
//				}

				// 通过验证条件，则新增该条数据
				verifyParams.put("customer_name", customerImport.getCustomer_name());
				verifyParams.put("customer_phone", customerImport.getCustomer_phone());
				String customerId = customerImport.getCustomer_id();
				String customer_birthdate = customerId.substring(6, 10) + "-" + customerId.substring(10, 12) + "-"
						+ customerId.substring(12, 14);
				verifyParams.put("customer_id", customerId);
				verifyParams.put("customer_birthdate", customer_birthdate);// 生日
				verifyParams.put("customer_addr", customerImport.getCustomer_addr());
				verifyParams.put("customer_vocation", customerImport.getCustomer_vocation());
				verifyParams.put("customer_hobby", customerImport.getCustomer_hobby());
				verifyParams.put("recommender", customerImport.getRecommender());
				// 性别
				if ("男".equals(customerImport.getCustomer_sex())) {
					verifyParams.put("customer_sex", "0");
				} else if ("女".equals(customerImport.getCustomer_sex())) {
					verifyParams.put("customer_sex", "1");
				}
				// 是否关注官微
				if ("已关注".equals(customerImport.getOfficial_micro_follow())) {
					verifyParams.put("official_micro_follow", "1");
				} else if ("未关注".equals(customerImport.getOfficial_micro_follow())) {
					verifyParams.put("official_micro_follow", "0");
				}
				// 阿隆是否关注
				if ("已关注".equals(customerImport.getAlong_follow())) {
					verifyParams.put("along_follow", "1");
				} else if ("未关注".equals(customerImport.getAlong_follow())) {
					verifyParams.put("along_follow", "0");
				}
				// 客户类型
				if ("普通用户".equals(customerImport.getCustomer_type())) {
					verifyParams.put("customer_type", "1");
				} else if ("白金用户".equals(customerImport.getCustomer_type())) {
					verifyParams.put("customer_type", "2");
				} else if ("归心合伙人".equals(customerImport.getCustomer_type())) {
					verifyParams.put("customer_type", "3");
				} else if ("敏感客户".equals(customerImport.getCustomer_type())) {
					verifyParams.put("customer_type", "4");
				} else if ("特殊身份客户".equals(customerImport.getCustomer_type())) {
					verifyParams.put("customer_type", "5");
				}
				// 客户状态
				if ("意向客户".equals(customerImport.getCustomer_status())) {
					verifyParams.put("customer_status", "0");
				} else if ("调研客户".equals(customerImport.getCustomer_status())) {
					verifyParams.put("customer_status", "3");
				} else if ("业主".equals(customerImport.getCustomer_status())) {
					verifyParams.put("customer_status", "5");
				} else if ("准业主".equals(customerImport.getCustomer_status())) {
					verifyParams.put("customer_status", "8");
				}
				count += this.save("d_customer", verifyParams);

				iSuccCount++;
			}
			if (count > 0) {
				String resultMsg = "已成功导入" + iSuccCount + "条数据";
				if (iFailCount > 0) {
					resultMsg += iFailCount + "条数据导入失败！详情如下：" + failBuffer.toString();
				}
				return ServerRspUtil.success(resultMsg);
			} else {
				return ServerRspUtil.error("导入失败，该张表所有客户身份证或手机号都已存在，请重新核对后上传！");
			}
		} catch (Exception e) {
			count = 0;
			e.printStackTrace();
			return ServerRspUtil.error(failBuffer.toString());
		}

	}

	/** updateCustomer : 修改客户信息 **/
	public ResponseBodyVO updateCustomer(Map<String, Object> param) {

		// Table:d_customer
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("customer_name", param.get("customer_name"));
		mapParam.put("customer_type", param.get("customer_type"));
		mapParam.put("customer_status", param.get("customer_status"));
		mapParam.put("customer_phone", param.get("customer_phone"));
		mapParam.put("customer_lable", param.get("customer_lable"));
		mapParam.put("customer_sex", param.get("customer_sex"));
		// 客户身份证
		String customerId = StrUtil.getMapValue(param, "customer_id");
		// 根据身份证拼接出生日年月份字符串
		String customer_birthdate = customerId.substring(6, 10) + "-" + customerId.substring(10, 12) + "-"
				+ customerId.substring(12, 14);
		mapParam.put("customer_id", customerId);
		mapParam.put("customer_birthdate", customer_birthdate);// 生日
		mapParam.put("customer_addr", param.get("customer_addr"));
		mapParam.put("official_micro_follow", param.get("official_micro_follow"));
		mapParam.put("along_follow", param.get("along_follow"));
		mapParam.put("customer_photo", param.get("customer_photo"));
		mapParam.put("customer_vocation", param.get("customer_vocation"));
		mapParam.put("customer_industry", param.get("customer_industry"));
		mapParam.put("customer_hobby", param.get("customer_hobby"));
		mapParam.put("customer_income", param.get("customer_income"));
		mapParam.put("customer_company", param.get("customer_company"));
		mapParam.put("customer_circle", param.get("customer_circle"));
		mapParam.put("focus", param.get("focus"));
		mapParam.put("recommender", param.get("recommender"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.update(mapCondition, "d_customer", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改客户信息失败");
	}

	/** delCustomer : 删除客户信息 **/
	public ResponseBodyVO delCustomer(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断
		int count = 0;
		// Table:d_customer
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		count += this.delete("d_customer", mapCondition);

		// 同时删除该客户的被服务历史
		Map<String, Object> mapServeCondition = new HashMap<String, Object>();
		String[] recId = StrUtil.getMapValue(param, "rec_id").split(",");
		if (recId.length > 0 && recId != null) {
			for (int i = 0; i < recId.length; i++) {
				mapServeCondition.put("served_customer", recId[i]);
				count += this.delete("d_user_servehis", mapServeCondition);
			}
		}
		return ServerRspUtil.formRspBodyVO(count, "删除客户信息失败");
	}

}
