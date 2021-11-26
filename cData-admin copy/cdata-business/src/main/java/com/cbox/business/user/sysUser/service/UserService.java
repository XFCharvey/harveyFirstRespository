package com.cbox.business.user.sysUser.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbox.base.constant.HttpStatus;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.service.BaseService;
import com.cbox.base.utils.DateUtils;
import com.cbox.base.utils.SecurityUtils;
import com.cbox.base.utils.StrUtil;
import com.cbox.base.utils.poi.ExcelUtil;
import com.cbox.business.user.sysUser.bean.UsersImport;
import com.cbox.business.user.sysUser.mapper.UserMapper;
import com.cbox.common.service.AttachFileService;
import com.cbox.system.service.ISysDictDataService;
import com.cbox.business.user.userservehis.mapper.UserServehisMapper;
import com.cbox.business.user.userworklog.mapper.UserWorklogMapper;
import com.cbox.business.worktask.worktask.mapper.WorktaskMapper;

@Service
public class UserService extends BaseService {

	@Autowired
	UserMapper userMapper;

	@Autowired
	private AttachFileService attachFileService;

	@Autowired
	private UserServehisMapper userServehisMapper;

	@Autowired
	private UserWorklogMapper userWorklogMapper;

	@Autowired
	private WorktaskMapper worktaskMapper;

	@Autowired
	ISysDictDataService iSysDictDataService;

	/** getCusWorkCheckCount : 获取我的客戶數量(浏览过的)，工作日志數量，待审核数量，待处理任务 **/
	public ResponseBodyVO getCusWorkCheckCount(Map<String, Object> param) {
		// TODO Auto-generated method stub
		Map<String, Object> mapResult = new HashMap<String, Object>();

		// step1: 我浏览过的客户数量
		String userName = SecurityUtils.getUsername();
		param.put("serve_person", userName);
		List<Map<String, Object>> listUserServeHis = userServehisMapper.listUserServehis(param);

		// Step2:我的工作日志數量
		param.put("log_person", userName);
		List<Map<String, Object>> listUserWorkLog = userWorklogMapper.listUserWorklog(param);

		// Step3:待审核数量
		param.put("status", "2");
		List<Map<String, Object>> listUser = userMapper.listUser(param);

		// Step4:待处理任务
		param.put("deal_person", userName);
		param.put("task_status", "0");
		List<Map<String, Object>> listWork = worktaskMapper.listWorktask(param);

		mapResult.put("my_cus_total", listUserServeHis.size());
		mapResult.put("my_log_total", listUserWorkLog.size());
		mapResult.put("my_cheak_total", listUser.size());
		mapResult.put("my_work_total", listWork.size());

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** listUser : 查询用户数据 **/
	public List<Map<String, Object>> listUser(Map<String, Object> param) {
		// TODO Auto-generated method stub
		super.appendUserInfo(param);

		List<Map<String, Object>> list = userMapper.listUser(param);

		return list;
	}

	/** listUser : 过滤掉已受民兵之家邀请的用户，查出剩余的 **/
	public List<Map<String, Object>> listfilterUser(Map<String, Object> param) {
		// TODO Auto-generated method stub
		super.appendUserInfo(param);

		List<Map<String, Object>> list = userMapper.listfilterUser(param);

		return list;
	}

	/** listUser : 过滤掉已受运营邀请的用户，查出剩余的 **/
	public List<Map<String, Object>> listfilterOperateUser(Map<String, Object> param) {
		// TODO Auto-generated method stub
		super.appendUserInfo(param);
		String loginDept = SecurityUtils.getLoginUser().getUser().getDeptId().toString();
		param.put("dept_id", loginDept);

		List<Map<String, Object>> list = userMapper.listfilterOperateUser(param);

		return list;
	}

	// 根据指定userid查询user
	public ResponseBodyVO getSysUser(Map<String, Object> param) {
		// TODO Auto-generated method stub
		Map<String, Object> mapResult = userMapper.getSysUser(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	// 根据指定手机查询用户
	public ResponseBodyVO getSysUserByPhone(Map<String, Object> param) {
		// TODO Auto-generated method stub
		Map<String, Object> mapResult = userMapper.getSysUserByPhone(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	// 批量文件导入用户数据
	public ResponseBodyVO importBatchUser(Map<String, Object> param) {
		int count = 0;
		int iSuccCount = 0;
		int iFailCount = 0;

		StringBuffer failBuffer = new StringBuffer();
		try {

			// 获取前端传递的固定值
			String deptId = StrUtil.getMapValue(param, "dept_id"); // 所属单位
			String create_by = SecurityUtils.getLoginUser().getUsername();// 创建导入人
			String nowtime = DateUtils.getTime();

			// 获取上传文件地址
			String fileId = StrUtil.getMapValue(param, "import_file");
			String fileName = attachFileService.getFileRealPath(fileId);
			File file = new File(fileName);

			// 解析excel文件
			ExcelUtil<UsersImport> util = new ExcelUtil<UsersImport>(UsersImport.class);
			List<UsersImport> userImportList = util.importExcel(file);

			Map<String, Object> verifyParams = new HashMap<String, Object>();

			Map<String, Object> dictParams = new HashMap<String, Object>();
			dictParams.put("dict_type", "identity_type");
			List<Map<String, Object>> identityType = userMapper.identityTypeValue(dictParams);
			// 遍历数据
			for (int i = 0; i < userImportList.size(); i++) {
				UsersImport userImport = userImportList.get(i);
				String identity = userImport.getIdentity_type();

				for (int j = 0; j < identityType.size(); j++) {
					Map<String, Object> mapIdentityType = identityType.get(j);
					String dictLabel = StrUtil.getMapValue(mapIdentityType, "dict_label");
					String dictValue = StrUtil.getMapValue(mapIdentityType, "dict_value");
					if (identity.equals(dictLabel)) {
						verifyParams.put("identity_type", dictValue);
					}
				}

				// 验证是否存在相同的民兵身份证
				Map<String, Object> idNumber = new HashMap<String, Object>();
				idNumber.put("id_number", userImport.getId_number());
				List<Map<String, Object>> userListByIDNumeResult = userMapper.listUser(idNumber);

				Map<String, Object> phoneNumber = new HashMap<String, Object>();
				phoneNumber.put("phonenumber", userImport.getPhonenumber());
				List<Map<String, Object>> userListByPhoneResult = userMapper.listUser(phoneNumber);

				// 判断是否存在相同的身份证
				if (userListByIDNumeResult.size() > 0) {
					// 已经存在，则跳过不添加
					iFailCount++;
					failBuffer.append(" 身份证为：“ " + userImport.getId_number() + " ” 的民兵信息已存在。");
					continue;
				}
				// 判断是否有相同的手机号存在
				if (userListByPhoneResult.size() > 0) {
					// 已经存在，则跳过不添加
					iFailCount++;
					failBuffer.append(" 手机号为：“ " + userImport.getPhonenumber() + " ” 的民兵信息已存在。");
					continue;
				}

				// 通过验证条件，则新增该条数据
				verifyParams.put("real_name", userImport.getReal_name());
				verifyParams.put("nick_name", userImport.getReal_name());
				if ("男".equals(userImport.getSex())) {
					verifyParams.put("sex", "0");
				} else if ("女".equals(userImport.getSex())) {
					verifyParams.put("sex", "1");
				} else {
					verifyParams.put("sex", "2");
				}

				verifyParams.put("id_number", userImport.getId_number());
				verifyParams.put("user_name", userImport.getPhonenumber());
				verifyParams.put("phonenumber", userImport.getPhonenumber());
				verifyParams.put("dept_id", deptId);
				verifyParams.put("create_by", create_by);
				verifyParams.put("create_time", nowtime);
				verifyParams.put("update_by", create_by);
				verifyParams.put("update_time", nowtime);
				verifyParams.put("status", 0);
				count = this.saveNoRec("sys_user", verifyParams);

				iSuccCount++;
			}
			if (count > 0) {
				String resultMsg = "已成功导入" + iSuccCount + "条数据";
				if (iFailCount > 0) {
					resultMsg += iFailCount + "条数据导入失败！详情如下：" + failBuffer.toString();
				}
				return ServerRspUtil.success(resultMsg);
			} else {
				return ServerRspUtil.error("导入失败，该张表所有人员身份证或手机号都已存在，请重新核对后上传！");
			}
		} catch (Exception e) {
			count = 0;
			e.printStackTrace();
			return ServerRspUtil.error(failBuffer.toString());
		}

	}

	/** addUserdetail : 新增用户数据 **/
	public ResponseBodyVO addUserdetail(Map<String, Object> param) {
		String nowtime = DateUtils.getTime();
		// TODO Auto-generated method stub
		int count = 0;
		String userName = SecurityUtils.getUsername();
		Map<String, Object> userPhoneCondition = new HashMap<String, Object>();
		userPhoneCondition.put("phonenumber", StrUtil.getMapValue(param, "phonenumber"));
		List<Map<String, Object>> listUserByPhone = userMapper.listUser(userPhoneCondition);
		int userPhoneNum = listUserByPhone.size();
		Map<String, Object> userIDCondition = new HashMap<String, Object>();
		userIDCondition.put("id_number", StrUtil.getMapValue(param, "id_number"));
		List<Map<String, Object>> listUserByID = userMapper.listUser(userIDCondition);
		int userIDNum = listUserByID.size();
		if ((userPhoneNum == 0 || listUserByPhone == null) && (userIDNum == 0 || listUserByID == null)) {
			Map<String, Object> userParam = new HashMap<String, Object>();
			userParam.put("dept_id", param.get("dept_id"));
			userParam.put("nick_name", param.get("real_name"));
			userParam.put("real_name", param.get("real_name"));
			userParam.put("id_number", param.get("id_number"));
			userParam.put("identity_type", param.get("identity_type"));
			userParam.put("user_name", param.get("phonenumber"));
			userParam.put("phonenumber", param.get("phonenumber"));
			userParam.put("sex", param.get("sex"));
			userParam.put("status", 0);
			userParam.put("user_type", "app");
			userParam.put("create_by", userName);
			userParam.put("create_time", nowtime);
			userParam.put("update_by", userName);
			userParam.put("update_time", nowtime);
			count = this.saveNoRec("sys_user", userParam, false);
		} else {
			return ServerRspUtil.formRspBodyVO(count, "该手机号或身份证已存在");
		}

		return ServerRspUtil.formRspBodyVO(count, "增加用户数据失败");
	}

	/** updateUser : 修改用户数据可多条 **/
	public ResponseBodyVO updateUsers(Map<String, Object> param) {
		int count = 0;
		String nowtime = DateUtils.getTime();
		String userName = SecurityUtils.getUsername();
		// Table:d_activity_enroll
		String userId = StrUtil.getMapValue(param, "user_id");
		Map<String, Object> useridmap = new HashMap<String, Object>();
		String[] userIds = userId.split(",");// 可能是批量审批
		for (int i = 0; i < userIds.length; i++) {
			Map<String, Object> mapParam = new HashMap<String, Object>();
			useridmap.put("user_id", userIds[i]);
			StrUtil.setMapValue(mapParam, "dept_id", param);
			StrUtil.setMapValue(mapParam, "nick_name", param);
			StrUtil.setMapValue(mapParam, "real_name", param);
			StrUtil.setMapValue(mapParam, "user_type", param);
			StrUtil.setMapValue(mapParam, "avatar", param);
			StrUtil.setMapValue(mapParam, "identity_type", param);
			StrUtil.setMapValue(mapParam, "sex", param);
			StrUtil.setMapValue(mapParam, "status", param);
			mapParam.put("user_type", "app");
			mapParam.put("update_by", userName);
			mapParam.put("update_time", nowtime);
			Map<String, Object> mapuser = userMapper.getSysUser(useridmap);
			String userIDNumParam = StrUtil.getMapValue(param, "id_number");
			String userPhoneParam = StrUtil.getMapValue(param, "phonenumber");
			String userIDNum = StrUtil.getMapValue(mapuser, "id_number");
			String userPhone = StrUtil.getMapValue(mapuser, "phonenumber");
			if (userIDNumParam.equals(userIDNum)) {
				StrUtil.setMapValue(mapParam, "id_number", param);
			} else {
				Map<String, Object> IDCondition = new HashMap<String, Object>();
				IDCondition.put("id_number", userIDNumParam);
				List<Map<String, Object>> listUserByID = userMapper.listUser(IDCondition);
				if (listUserByID.size() != 0) {
					return ServerRspUtil.formRspBodyVO(count, "身份证号码已存在");
				} else {
					StrUtil.setMapValue(mapParam, "id_number", param);
				}
			}
			if (userPhoneParam.equals(userPhone)) {
				StrUtil.setMapValue(mapParam, "phonenumber", param);
			} else {
				Map<String, Object> PhoneCondition = new HashMap<String, Object>();
				PhoneCondition.put("phonenumber", userPhoneParam);
				List<Map<String, Object>> listUserByPhone = userMapper.listUser(PhoneCondition);
				if (listUserByPhone.size() != 0) {
					return ServerRspUtil.formRspBodyVO(count, "该手机号已存在");
				} else {
					StrUtil.setMapValue(mapParam, "phonenumber", param);
				}
			}

			// 更新字段

			// 条件
			Map<String, Object> mapCondition = new HashMap<String, Object>();
			mapCondition.put("user_id", userIds[i]);
			count = this.updateNoRec(mapCondition, "sys_user", mapParam);
		}

		return ServerRspUtil.formRspBodyVO(count, "修改用户数据失败");
	}

	// public ResponseBodyVO updateUserStatus(Map<String, Object> param) {
	// int count = 0;
	// String nowtime = DateUtils.getTime();
	// String userName = SecurityUtils.getUsername();
	// // Table:d_activity_enroll
	// String userId = StrUtil.getMapValue(param, "user_id");
	// Map<String, Object> useridmap = new HashMap<String, Object>();
	// String[] userIds = userId.split(",");// 可能是批量审批
	// for (int i = 0; i < userIds.length; i++) {
	// Map<String, Object> mapParam = new HashMap<String, Object>();
	// useridmap.put("user_id", userIds[i]);
	// useridmap.put("status", param.get("status"));
	// }
	//
	// return ServerRspUtil.formRspBodyVO(count, "修改用户状态失败");
	// }

	public ResponseBodyVO updateUserStatus(Map<String, Object> param) {
		// TODO Auto-generated method stub
		int count = 0;
		String nowtime = DateUtils.getTime();
		String userName = SecurityUtils.getUsername();
		// Table:d_activity_enroll
		String userId = StrUtil.getMapValue(param, "user_id");
		String[] userIds = userId.split(",");// 可能是批量审批
		for (int i = 0; i < userIds.length; i++) {
			Map<String, Object> useridmap = new HashMap<String, Object>();
			useridmap.put("user_id", userIds[i]);
			Map<String, Object> mapParam = new HashMap<String, Object>();
			String status = StrUtil.getMapValue(param, "status");
			if (StrUtil.isNotNull(status)) {
				mapParam.put("status", status);
			} else {
				mapParam.put("status", "3");
			}
			mapParam.put("update_by", userName);
			mapParam.put("update_time", nowtime);
			count += this.updateNoRec(useridmap, "sys_user", mapParam);
		}
		return ServerRspUtil.formRspBodyVO(count, "修改用户数据状态失败");
	}

}
