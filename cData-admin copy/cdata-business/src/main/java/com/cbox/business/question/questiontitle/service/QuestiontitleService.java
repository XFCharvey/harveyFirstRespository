package com.cbox.business.question.questiontitle.service;

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
import com.cbox.base.utils.StrUtil;
import com.cbox.business.question.questionbank.mapper.QuestionBankMapper;
import com.cbox.business.question.questiontitle.mapper.QuestiontitleMapper;
import com.cbox.business.question.questiontitleoption.mapper.QuestionTitleOptionMapper;

/**
 * @ClassName: QuestiontitleService
 * @Function: 题库题目
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class QuestiontitleService extends BaseService {

	@Autowired
	private QuestiontitleMapper questiontitleMapper;

	@Autowired
	private QuestionTitleOptionMapper questionTitleOptionMapper;

	@Autowired
	private QuestionBankMapper questionBankMapper;

	/** listQuestiontitle : 获取题库题目列表数据 **/
	public List<Map<String, Object>> listQuestiontitle(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = questiontitleMapper.listQuestiontitle(param);

		return list;
	}

	/** getQuestiontitle : 获取指定id的题库题目数据 **/
	public ResponseBodyVO getQuestiontitle(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = questiontitleMapper.getQuestiontitle(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addQuestiontitle : 新增题库题目 **/
	@SuppressWarnings("unchecked")
	public ResponseBodyVO addQuestiontitle(Map<String, Object> param) {
		int count = 0;
		// Table:d_question_title
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("question_id", param.get("question_id"));
		mapParam.put("title_name", param.get("title_name"));
		mapParam.put("title_type", param.get("title_type"));
		mapParam.put("group_id", param.get("group_id"));
		mapParam.put("title_sort", param.get("title_sort"));
		mapParam.put("title_answer", param.get("title_answer"));
		mapParam.put("title_score", param.get("title_score"));
		count = this.save("d_question_title", mapParam);
		// 题目分数
		int titleScore = StrUtil.getMapIntValue(param, "title_score");
		// 查询题库条件
		Map<String, Object> mapQuetionCondition = new HashMap<String, Object>();
		mapQuetionCondition.put("rec_id", param.get("question_id"));
		// 题库总分
		Map<String, Object> mapQuetionUpdateParam = new HashMap<String, Object>();
		mapQuetionUpdateParam.put("full_score", titleScore);
		// 总分更新
		count += this.updateIncrement(mapQuetionCondition, "d_question_bank", mapQuetionUpdateParam);

		Map<String, Object> questionBank = questionBankMapper.getQuestionBank(mapQuetionCondition);

		if (param.get("optionList") != null) {
			List<String> listanswerid = new ArrayList<String>();
			List<Map<String, Object>> listParamoption = (List<Map<String, Object>>) param.get("optionList");

			for (int i = 0; i < listParamoption.size(); i++) {
				Map<String, Object> mapparamOptionDB = listParamoption.get(i);
				Map<String, Object> optionParam = new HashMap<String, Object>();
				optionParam.put("title_id", StrUtil.getMapValue(mapParam, "rec_id"));
				optionParam.put("option_name", mapparamOptionDB.get("option_name"));
				optionParam.put("is_singlerow", mapparamOptionDB.get("is_singlerow"));//是否单行：0-否（默认），1-是
				optionParam.put("option_sort", mapparamOptionDB.get("option_sort"));
				optionParam.put("is_answer", mapparamOptionDB.get("is_answer"));
				count = this.save("d_question_title_option", optionParam);
				String isanswer = StrUtil.getMapValue(mapparamOptionDB, "is_answer");
				String newrecid = StrUtil.getMapValue(optionParam, "rec_id");
				if ("1".equals(isanswer)) {
					listanswerid.add(newrecid);
				}
			}
			String answerid = String.join(",", listanswerid);
			Map<String, Object> mapparamanswer = new HashMap<String, Object>();
			mapparamanswer.put("title_answer", answerid);
			Map<String, Object> mapConditionOption = new HashMap<String, Object>();
			mapConditionOption.put("rec_id", StrUtil.getMapValue(mapParam, "rec_id"));
			count = this.update(mapConditionOption, "d_question_title", mapparamanswer);

			String bankType = StrUtil.getMapValue(questionBank, "q_type");
			if ("ask".equals(bankType)) {
				Map<String, Object> mapQuetionTitleNumParam = new HashMap<String, Object>();
				mapQuetionTitleNumParam.put("exam_title_num", 1);
				this.updateIncrement(mapQuetionCondition, "d_question_bank", mapQuetionTitleNumParam);

			}
		}

		return ServerRspUtil.formRspBodyVO(count, "新增题库题目失败");
	}

	/** updateQuestiontitle : 修改题库题目 **/
	@SuppressWarnings("unchecked")
	public ResponseBodyVO updateQuestiontitle(Map<String, Object> param) {

		// Table:d_question_title
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("question_id", param.get("question_id"));
		mapParam.put("title_name", param.get("title_name"));
		mapParam.put("title_type", param.get("title_type"));
		mapParam.put("group_id", param.get("group_id"));
		mapParam.put("title_sort", param.get("title_sort"));
		mapParam.put("title_answer", param.get("title_answer"));
		mapParam.put("title_score", param.get("title_score"));
		Map<String, Object> mapConditionTitle = new HashMap<String, Object>();
		mapConditionTitle.put("rec_id", param.get("rec_id"));
		int count = this.update(mapConditionTitle, "d_question_title", mapParam);
		if (param.get("optionList") != null) {
			/** 取title的rec_id作为title_id查询option表属于此title_id的所有数据 **/
			Map<String, Object> mapConditionTitleoption = new HashMap<String, Object>();
			mapConditionTitleoption.put("title_id", param.get("rec_id"));
			List<Map<String, Object>> listOption = questionTitleOptionMapper
					.listQuestionTitleOption(mapConditionTitleoption);

			List<String> listanswerid = new ArrayList<String>();
			Map<String, Object> mapparamrecid = new HashMap<String, Object>();
			List<Map<String, Object>> listParamoption = (List<Map<String, Object>>) param.get("optionList");
			for (int i = 0; i < listParamoption.size(); i++) {
				Map<String, Object> mapparamOptionDB = (Map<String, Object>) listParamoption.get(i);
				if (mapparamOptionDB.get("rec_id") != null) {
					String recidid = StrUtil.getMapValue(mapparamOptionDB, "rec_id");
					mapparamrecid.put(recidid, recidid);
					Map<String, Object> optionParam = new HashMap<String, Object>();
					optionParam.put("title_id", param.get("rec_id"));
					optionParam.put("option_name", mapparamOptionDB.get("option_name"));
					optionParam.put("is_singlerow", mapparamOptionDB.get("is_singlerow"));//是否单行：0-否（默认），1-是
					optionParam.put("option_sort", mapparamOptionDB.get("option_sort"));
					optionParam.put("is_answer", mapparamOptionDB.get("is_answer"));
					String isanswer = StrUtil.getMapValue(mapparamOptionDB, "is_answer");
					if (isanswer.equals("1")) {
						listanswerid.add(StrUtil.getMapValue(mapparamOptionDB, "rec_id"));
					}
					Map<String, Object> mapConditionOption = new HashMap<String, Object>();
					mapConditionOption.put("rec_id", mapparamOptionDB.get("rec_id"));
					count = this.update(mapConditionOption, "d_question_title_option", optionParam);
				} else {
					Map<String, Object> optionParam = new HashMap<String, Object>();
					optionParam.put("title_id", param.get("rec_id"));
					optionParam.put("option_name", mapparamOptionDB.get("option_name"));
					optionParam.put("is_singlerow", mapparamOptionDB.get("is_singlerow"));
					optionParam.put("option_sort", mapparamOptionDB.get("option_sort"));
					optionParam.put("is_answer", mapparamOptionDB.get("is_answer"));
					String isanswer = StrUtil.getMapValue(mapparamOptionDB, "is_answer");
					count = this.save("d_question_title_option", optionParam);
					if (isanswer.equals("1")) {
						listanswerid.add(StrUtil.getMapValue(optionParam, "rec_id"));
					}
				}

			}
			String answerid = String.join(",", listanswerid);
			System.out.println(answerid);
			Map<String, Object> mapparamanswer = new HashMap<String, Object>();
			mapparamanswer.put("title_answer", answerid);
			Map<String, Object> mapConditionOption = new HashMap<String, Object>();
			mapConditionOption.put("rec_id", StrUtil.getMapValue(param, "rec_id"));
			count = this.update(mapConditionOption, "d_question_title", mapparamanswer);

			for (int j = 0; j < listOption.size(); j++) {
				Map<String, Object> mapRecidDB = (Map<String, Object>) listOption.get(j);
				String paramRecid = StrUtil.getMapValue(mapRecidDB, "rec_id");
				if (!mapparamrecid.containsKey(paramRecid)) {
					// 需要删除，这里写删除的逻辑
					Map<String, Object> delmapCondition = new HashMap<String, Object>();
					delmapCondition.put("rec_id", paramRecid);
					count += this.delete("d_question_title_option", delmapCondition);

				}
			}
		}

		return ServerRspUtil.formRspBodyVO(count, "修改题库题目失败");
	}

	/** delQuestiontitle : 删除题库题目 **/
	public ResponseBodyVO delQuestiontitle(Map<String, Object> param) {
		int count = 0;
		// TODO : 删除前的逻辑判断
		// Table:d_question_title
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		// 查询指定题目的数据
		Map<String, Object> mapTitleResult = questiontitleMapper.getQuestiontitle(mapCondition);

		String bankID = StrUtil.getMapValue(mapTitleResult, "question_id");

		Map<String, Object> mapBankCondition = new HashMap<String, Object>();
		mapBankCondition.put("rec_id", bankID);

		Map<String, Object> mapBankResult = questionBankMapper.getQuestionBank(mapBankCondition);
		String bankType = StrUtil.getMapValue(mapBankResult, "q_type");

		if ("ask".equals(bankType)) {
			Map<String, Object> decrementTitleNumParam = new HashMap<String, Object>();
			decrementTitleNumParam.put("exam_title_num", 1);
			count += this.updateDecrement(mapCondition, "d_question_bank", decrementTitleNumParam);
			count += this.delete("d_question_title", mapCondition);
		} else {
			int titleScore = StrUtil.getMapIntValue(mapTitleResult, "title_score");
			// 题库减去删除的这一个题目的分数
			Map<String, Object> decrementParam = new HashMap<String, Object>();
			decrementParam.put("full_score", titleScore);
			//
			count += this.updateDecrement(mapCondition, "d_question_bank", decrementParam);
			//
			count += this.delete("d_question_title", mapCondition);
		}
		mapCondition = new HashMap<String, Object>();
		mapCondition.put("title_id", param.get("rec_id"));
		count += this.delete("d_question_title_option", mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除题库题目失败");
	}

}
