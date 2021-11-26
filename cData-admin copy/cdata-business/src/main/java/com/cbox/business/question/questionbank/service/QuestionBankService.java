package com.cbox.business.question.questionbank.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.beust.jcommander.internal.Lists;
import com.cbox.base.constant.HttpStatus;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.service.BaseService;
import com.cbox.base.utils.ObjUtil;
import com.cbox.base.utils.SecurityUtils;
import com.cbox.base.utils.StrUtil;
import com.cbox.business.question.examrecord.mapper.ExamRecordMapper;
import com.cbox.business.question.questionbank.mapper.QuestionBankMapper;
import com.cbox.business.question.questiontitle.mapper.QuestiontitleMapper;
import com.cbox.business.question.questiontitlegroup.mapper.QuestiontitlegroupMapper;
import com.cbox.business.question.questiontitleoption.mapper.QuestionTitleOptionMapper;
import com.google.common.base.Joiner;

/**
 * @ClassName: QuestionBankService
 * @Function: 题库主题
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class QuestionBankService extends BaseService {

	@Autowired
	private QuestionBankMapper questionBankMapper;

	@Autowired
	private QuestiontitlegroupMapper questiontitlegroupMapper;

	@Autowired
	private QuestiontitleMapper questiontitleMapper;

	@Autowired
	private ExamRecordMapper examRecordMapper;

	@Autowired
	private QuestionTitleOptionMapper questionTitleOptionMapper;

	/** listQuestionBank : 获取题库主题列表数据 **/
	public List<Map<String, Object>> listQuestionBank(Map<String, Object> param, boolean Betype) {
		if (Betype) {
			String loginDeptId = SecurityUtils.getLoginUser().getUser().getDeptId().toString();
			param.put("q_launch_unit", loginDeptId);
		}
		super.appendUserInfo(param);

		List<Map<String, Object>> listquestionBank = questionBankMapper.listQuestionBank(param);

		for (int i = 0; i < listquestionBank.size(); i++) {
			Map<String, Object> mapbank = listquestionBank.get(i);
			String bankrecID = StrUtil.getMapValue(mapbank, "rec_id");
			String bankType = StrUtil.getMapValue(mapbank, "q_type");
			// 取出当前登录用户的所属部门及所有上级部门，内容为用逗号分隔的字符串
			String upDeptAuth = SecurityUtils.getUpDeptAuth();
			List<String> upDeptAuthList = Arrays.asList(upDeptAuth.split(","));

			double joinHead = 0;
			if ("ask".equals(bankType)) {
				Map<String, Object> bankID = new HashMap<String, Object>();
				bankID.put("qbank_id", bankrecID);
				List<Map<String, Object>> bankRecord = examRecordMapper.listExamRecord(bankID);
				if (bankRecord.size() != 0) {
					int invitationNum = StrUtil.getMapIntValue(mapbank, "invitation_num");
					if (invitationNum != 0) {
						int inputNum = bankRecord.size();
						joinHead += new BigDecimal((float) inputNum / invitationNum).setScale(2, BigDecimal.ROUND_DOWN)
								.doubleValue();
					}

				} else {
					joinHead = 0;
				}

				mapbank.put("isJoin", "不可参与");
				// 取出每个活动的邀请范围的内容
				String range = StrUtil.getMapValue(mapbank, "q_range_dept");
				// 将内容转换成以逗号分隔的数组
				String[] rangeArr = range.split(",");
				// 遍历邀请范围数组
				for (int j = 0; j < rangeArr.length; j++) {
					String rangeAlone = rangeArr[j];
					// 判断邀请范围的每个单个部门ID是否存在当前登录的用户所属的部门及上级部门
					if (upDeptAuthList.contains(rangeAlone)) {
						mapbank.put("isJoin", "可参与");
						break;
					}

				}
			}
			mapbank.put("joinHead", joinHead);

		}
		return listquestionBank;
	}

    /** listQuestionBankWithCount : 获取问卷年数据，携带数量 **/
    public List<Map<String, Object>> listQuestionBankWithCount(Map<String, Object> param) {

        List<Map<String, Object>> listQuestionCount = questionBankMapper.getQuestionCount(param);
        Map<String, Map<String, Object>> mapQuestionCount = ObjUtil.transListToMap(listQuestionCount, "question_id");

        List<Map<String, Object>> listquestionBank = questionBankMapper.listQuestionBank(param);

        for (int i = 0; i < listquestionBank.size(); i++) {
            Map<String, Object> mapbank = listquestionBank.get(i);

            String bankrecID = StrUtil.getMapValue(mapbank, "rec_id");

            Map<String, Object> mapCount = mapQuestionCount.get(bankrecID);
            if (ObjUtil.isNotNull(mapCount)) {
                mapbank.put("q_count", mapCount.get("q_count"));
            } else {
                mapbank.put("q_count", "0");
            }
        }
        return listquestionBank;
    }

	/** listFilterInfoQuestionBank : 获取过滤后未绑定资讯的题库主题列表数据 **/
	public List<Map<String, Object>> listFilterInfoQuestionBank(Map<String, Object> param) {
		// TODO Auto-generated method stub
		String loginDeptId = SecurityUtils.getLoginUser().getUser().getDeptId().toString();
		param.put("q_launch_unit", loginDeptId);
		super.appendUserInfo(param);

		List<Map<String, Object>> listExamQuestionBank = questionBankMapper.listFilterInfoQuestionBank(param);

		return listExamQuestionBank;
	}

	/** getQuestionInfoById : 通过问卷id获取所有相关问卷的资料 **/
	public String getQuestionInfoById(Map<String, Object> param) {
		// TODO Auto-generated method stub
		Map<String, Object> saveResult = new LinkedHashMap<String, Object>();
		List<Map<String, Object>> groupSaveList = new ArrayList<Map<String, Object>>();

		// Step1:获取题库、分组、题目信息
		// 得到题库
		Map<String, Object> mapResult = questionBankMapper.getQuestionBank(param);

		Map<String, Object> gAtParam = new HashMap<String, Object>();
		gAtParam.put("question_id", param.get("rec_id"));
		// 得到指定问卷所有分组
		List<Map<String, Object>> groupList = questiontitlegroupMapper.listQuestiontitlegroup(gAtParam);
		// 得到指定问卷的题目
		List<Map<String, Object>> titleList = questiontitleMapper.listQuestiontitle(gAtParam);

		// Step2:先取出没有分组的题目
		List<Map<String, Object>> titleSaveList = new ArrayList<Map<String, Object>>();
		for (int j = 0; j < titleList.size(); j++) {
			String groupId = StrUtil.getMapValue(titleList.get(j), "group_id");
			if (StrUtil.isNull(groupId)) {
				titleSaveList.add(this.formatGoup(titleList.get(j)));
			}
		}
		if (ObjUtil.isNotNull(titleSaveList)) {
			Map<String, Object> groupSaveParam = new LinkedHashMap<String, Object>();

			groupSaveParam.put("group_id", 0);
			groupSaveParam.put("group_name", "default");
			groupSaveParam.put("group_sort", 0);
			groupSaveParam.put("titles", titleSaveList);
			groupSaveList.add(groupSaveParam);
		}

		// Step3: 得到每个分组的题目
		for (int i = 0; i < groupList.size(); i++) {
			Map<String, Object> mapGroup = groupList.get(i);
			titleSaveList = new ArrayList<Map<String, Object>>();
			String groupRecId = StrUtil.getMapValue(mapGroup, "rec_id");

			for (int j = 0; j < titleList.size(); j++) {
				Map<String, Object> mapTitle = titleList.get(j);
				String groupId = StrUtil.getMapValue(mapTitle, "group_id");
				if (groupRecId.equals(groupId)) {
					titleSaveList.add(this.formatGoup(mapTitle));
				}
			}

			if (ObjUtil.isNotNull(titleSaveList)) {
				Map<String, Object> groupSaveParam = new LinkedHashMap<String, Object>();
				groupSaveParam.put("group_id", mapGroup.get("rec_id"));
				groupSaveParam.put("group_name", mapGroup.get("group_name"));
				groupSaveParam.put("group_sort", mapGroup.get("group_sort"));
				groupSaveParam.put("titles", titleSaveList);
				groupSaveList.add(groupSaveParam);
			}
		}

		saveResult.put("question_id", param.get("rec_id"));
		saveResult.put("question_name", mapResult.get("q_name"));
		saveResult.put("groups", groupSaveList);

		// Step4:输出，或写入文件
		String mapJson = JSON.toJSONString(saveResult);

		System.out.println(mapJson);

		return mapJson;
	}

	public Map<String, Object> formatGoup(Map<String, Object> titleMap) {

		Map<String, Object> opParam = new LinkedHashMap<String, Object>();
		opParam.put("title_id", titleMap.get("rec_id"));
		// 存放指定分组及每个答案
		List<Map<String, Object>> optionSaveList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> optionList = questionTitleOptionMapper.listQuestionTitleOption(opParam);
		for (int n = 0; n < optionList.size(); n++) {
			Map<String, Object> opSaveParam = new LinkedHashMap<String, Object>();
			opSaveParam.put("option_id", optionList.get(n).get("rec_id"));
			opSaveParam.put("option_name", optionList.get(n).get("option_name"));
			opSaveParam.put("option_sort", optionList.get(n).get("option_sort"));
			optionSaveList.add(opSaveParam);
		}
		Map<String, Object> titleSaveParam = new LinkedHashMap<String, Object>();
		titleSaveParam.put("title_id", titleMap.get("rec_id"));
		titleSaveParam.put("title_name", titleMap.get("title_name"));
		titleSaveParam.put("title_sort", titleMap.get("title_sort"));
		titleSaveParam.put("title_type", titleMap.get("title_type"));
		titleSaveParam.put("options", optionSaveList);

		return titleSaveParam;

	};

	/** listQuestionBank : 获取过滤后可参与的题库主题列表数据 **/
	public List<Map<String, Object>> filterQuestionBank(Map<String, Object> param) {
		// TODO Auto-generated method stub
		super.appendUserInfo(param);

		List<Map<String, Object>> list = questionBankMapper.filterQuestionBank(param);

		List<Map<String, Object>> resultList = Lists.newArrayList();
		// 取出当前登录用户的所属部门及所有上级部门，内容为用逗号分隔的字符串
		String upDeptAuth = SecurityUtils.getUpDeptAuth();
		// 遍历所有活动
		for (int i = 0; i < list.size(); i++) {
			// 创建一个bExist，用于后面做判断是否属于邀请范围内
			int bExistNum = 0;
			Map<String, Object> mapQuestionBank = list.get(i);
			// 取出每个活动的邀请范围的内容
			String range = StrUtil.getMapValue(mapQuestionBank, "q_range_dept");
			// 将内容转换成以逗号分隔的数组
			String[] rangeArr = range.split(",");
			// 遍历邀请范围数组
			for (int j = 0; j < rangeArr.length; j++) {
				String rangeAlone = rangeArr[j];
				// 判断邀请范围的每个单个部门ID是否存在当前登录的用户所属的部门及上级部门
				int contains = upDeptAuth.indexOf(rangeAlone);
				if (contains > -1) {
					bExistNum += 1;
				}

			}
			// 如果这条活动的范围没有当前用户的所有部门，就过滤掉这条活动
			if (bExistNum == 0) {
				resultList.add(mapQuestionBank);
			}
		}
		return resultList;
	}

	/** getQuestionBank : 获取指定id的题库主题数据 **/
	public ResponseBodyVO getQuestionBank(Map<String, Object> param) {

		Map<String, Object> mapResult = questionBankMapper.getQuestionBank(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** copyQuestionBank: 复制题库及题目与选项 **/
	public ResponseBodyVO copyQuestionBank(Map<String, Object> param) {
		// TODO Auto-generated method stub
		int count = 0;
		// 查询指定ID的题库
		Map<String, Object> mapQuestionResult = questionBankMapper.getQuestionBank(param);
		// 要复制的题库参数
		Map<String, Object> mapCopyBankParam = new HashMap<String, Object>();
		mapCopyBankParam.put("q_type", StrUtil.getMapValue(mapQuestionResult, "q_type"));
		String bankType = StrUtil.getMapValue(mapQuestionResult, "q_type");
		mapCopyBankParam.put("q_name", StrUtil.getMapValue(mapQuestionResult, "q_name") + "-副本");
		mapCopyBankParam.put("q_detail", StrUtil.getMapValue(mapQuestionResult, "q_detail"));
		mapCopyBankParam.put("end_text", StrUtil.getMapValue(mapQuestionResult, "end_text"));// 结束页面文字（问卷特有）
		mapCopyBankParam.put("q_launch_unit", StrUtil.getMapValue(mapQuestionResult, "q_launch_unit"));
//		mapCopyBankParam.put("exam_title_num", StrUtil.getMapValue(mapQuestionResult, "exam_title_num"));//数量为空时会报错
		mapCopyBankParam.put("exam_title_num", mapQuestionResult.get("exam_title_num"));
		if ("ask".equals(bankType)) {
			mapCopyBankParam.put("exam_maxtime", "0");
		} else {
			mapCopyBankParam.put("exam_maxtime", StrUtil.getMapValue(mapQuestionResult, "exam_maxtime"));
		}
		mapCopyBankParam.put("full_score", StrUtil.getMapValue(mapQuestionResult, "full_score"));
		mapCopyBankParam.put("qualified_score", StrUtil.getMapValue(mapQuestionResult, "qualified_score"));
		mapCopyBankParam.put("q_img", StrUtil.getMapValue(mapQuestionResult, "q_img"));
		mapCopyBankParam.put("q_range_dept", StrUtil.getMapValue(mapQuestionResult, "q_range_dept"));
		mapCopyBankParam.put("invitation_num", StrUtil.getMapValue(mapQuestionResult, "invitation_num"));

		if (StrUtil.isNotNull(mapQuestionResult.get("begin_time"))) {
			mapCopyBankParam.put("begin_time", StrUtil.getMapValue(mapQuestionResult, "begin_time"));
		}
		if (StrUtil.isNotNull(mapQuestionResult.get("end_time"))) {
			mapCopyBankParam.put("end_time", StrUtil.getMapValue(mapQuestionResult, "end_time"));
		}
		mapCopyBankParam.put("q_status", "0");
		count += this.save("d_question_bank", mapCopyBankParam);
		String newBankId = StrUtil.getMapValue(mapCopyBankParam, "rec_id");
		Map<String, Object> mapTitleCodintion = new HashMap<String, Object>();
		mapTitleCodintion.put("question_id", StrUtil.getMapValue(param, "rec_id"));
		List<Map<String, Object>> listTitleResult = questiontitleMapper.listQuestiontitle(mapTitleCodintion);
		for (int i = 0; i < listTitleResult.size(); i++) {
			Map<String, Object> mapTitle = listTitleResult.get(i);
			String titleType = StrUtil.getMapValue(mapTitle, "title_type");
			String titleID = StrUtil.getMapValue(mapTitle, "rec_id");
			Map<String, Object> mapCopyTitleParam = new HashMap<String, Object>();
			mapCopyTitleParam.put("question_id", newBankId);
			mapCopyTitleParam.put("title_name", StrUtil.getMapValue(mapTitle, "title_name"));
			mapCopyTitleParam.put("title_type", StrUtil.getMapValue(mapTitle, "title_type"));
			mapCopyTitleParam.put("title_sort", StrUtil.getMapValue(mapTitle, "title_sort"));
			mapCopyTitleParam.put("title_answer", StrUtil.getMapValue(mapTitle, "title_answer"));
			mapCopyTitleParam.put("title_score", StrUtil.getMapValue(mapTitle, "title_score"));
			count += this.save("d_question_title", mapCopyTitleParam);
			String newTitleID = StrUtil.getMapValue(mapCopyTitleParam, "rec_id");
			if ("radio".equals(titleType) || "multi".equals(titleType)) {
				Map<String, Object> mapOptionCondition = new HashMap<String, Object>();
				mapOptionCondition.put("title_id", titleID);
				List<Map<String, Object>> listOption = questionTitleOptionMapper
						.listQuestionTitleOption(mapOptionCondition);
				List<String> answerList = new ArrayList<String>();
				for (int j = 0; j < listOption.size(); j++) {
					Map<String, Object> mapOption = listOption.get(j);

					Map<String, Object> mapCopyOptionParam = new HashMap<String, Object>();
					mapCopyOptionParam.put("title_id", newTitleID);
					mapCopyOptionParam.put("option_name", StrUtil.getMapValue(mapOption, "option_name"));
					mapCopyOptionParam.put("is_singlerow", StrUtil.getMapValue(mapOption, "is_singlerow"));// 是否单行：0-否（默认），1-是
					mapCopyOptionParam.put("option_sort", StrUtil.getMapValue(mapOption, "option_sort"));
					mapCopyOptionParam.put("is_answer", StrUtil.getMapValue(mapOption, "is_answer"));
					count += this.save("d_question_title_option", mapCopyOptionParam);
					String optionId = StrUtil.getMapValue(mapCopyOptionParam, "rec_id");
					if ("1".equals(StrUtil.getMapValue(mapOption, "is_answer"))) {
						answerList.add(optionId);
					}
				}
				String strAnswer = Joiner.on(",").join(answerList);
				Map<String, Object> mapUpdateAnswerCondition = new HashMap<String, Object>();
				mapUpdateAnswerCondition.put("rec_id", newTitleID);
				Map<String, Object> mapAnswer = new HashMap<String, Object>();
				mapAnswer.put("title_answer", strAnswer);
				count += this.update(mapUpdateAnswerCondition, "d_question_title", mapAnswer);
			}
		}

		return null;
	}

	/** addQuestionBank : 新增题库主题 **/
	public ResponseBodyVO addQuestionBank(Map<String, Object> param) {
		String deptId = SecurityUtils.getLoginUser().getUser().getDeptId().toString();
		// Table:d_question_bank
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("q_type", param.get("q_type"));
		mapParam.put("q_name", param.get("q_name"));
		mapParam.put("q_detail", param.get("q_detail"));
		mapParam.put("end_text", param.get("end_text"));// 結束頁文字
		mapParam.put("q_launch_unit", deptId);
		mapParam.put("exam_title_num", param.get("exam_title_num"));
		mapParam.put("exam_maxtime", param.get("exam_maxtime"));
		mapParam.put("qualified_score", param.get("qualified_score"));
		mapParam.put("q_range_dept", param.get("q_range_dept"));
		int invitNum = 0;
		if (param.get("q_range_dept") != null) {
			String deptstr = StrUtil.getMapValue(param, "q_range_dept");
			String[] deptstrArr = deptstr.split(",");
			for (int i = 0; i < deptstrArr.length; i++) {
				String deptmap = deptstrArr[i];
				Map<String, Object> maprange = new HashMap<String, Object>();
				maprange.put("q_range_dept", deptmap);
				Map<String, Object> invitationNum = questionBankMapper.countinvitationNum(maprange);
				invitNum += StrUtil.getMapIntValue(invitationNum, "invitationNum");
			}
		} else {
		}
		mapParam.put("invitation_num", invitNum);
		mapParam.put("q_img", param.get("q_img"));
		mapParam.put("begin_time", param.get("begin_time"));
		mapParam.put("end_time", param.get("end_time"));
		mapParam.put("q_status", param.get("q_status"));
		int count = this.save("d_question_bank", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "新增题库主题失败");
	}

	/** updateQuestionBank : 修改题库主题 **/
	public ResponseBodyVO updateQuestionBank(Map<String, Object> param) {

		// Table:d_question_bank
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("q_type", param.get("q_type"));
		mapParam.put("q_name", param.get("q_name"));
		mapParam.put("q_detail", param.get("q_detail"));
		mapParam.put("end_text", param.get("end_text"));// 結束頁文字
		mapParam.put("q_launch_unit", param.get("q_launch_unit"));
		mapParam.put("exam_title_num", param.get("exam_title_num"));
		mapParam.put("exam_maxtime", param.get("exam_maxtime"));
		mapParam.put("qualified_score", param.get("qualified_score"));
		mapParam.put("q_img", param.get("q_img"));
		mapParam.put("q_range_dept", param.get("q_range_dept"));
		int invitNum = 0;
		if (param.get("q_range_dept") != null) {
			String deptstr = StrUtil.getMapValue(param, "q_range_dept");
			String[] deptstrArr = deptstr.split(",");
			for (int i = 0; i < deptstrArr.length; i++) {
				String deptmap = deptstrArr[i];
				Map<String, Object> maprange = new HashMap<String, Object>();
				maprange.put("q_range_dept", deptmap);
				Map<String, Object> invitationNum = questionBankMapper.countinvitationNum(maprange);
				invitNum += StrUtil.getMapIntValue(invitationNum, "invitationNum");
			}
		} else {
		}
		mapParam.put("begin_time", param.get("begin_time"));
		mapParam.put("end_time", param.get("end_time"));
		mapParam.put("q_status", param.get("q_status"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.update(mapCondition, "d_question_bank", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改题库主题失败");
	}

	/** delQuestionBank : 删除题库主题 **/
	public ResponseBodyVO delQuestionBank(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断

		// Table:d_question_bank
		Map<String, Object> mapCondition = new HashMap<String, Object>();

		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.delete("d_question_bank", mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除题库主题失败");
	}

}
