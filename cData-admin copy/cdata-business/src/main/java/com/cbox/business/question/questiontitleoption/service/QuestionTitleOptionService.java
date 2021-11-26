package com.cbox.business.question.questiontitleoption.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.service.BaseService;
import com.cbox.base.utils.StrUtil;
import com.cbox.business.question.examrecord.mapper.ExamRecordMapper;
import com.cbox.business.question.examrecorddetail.mapper.ExamRecordDetailMapper;
import com.cbox.business.question.questiontitleoption.mapper.QuestionTitleOptionMapper;

/**
 * @ClassName: QuestionTitleOptionService
 * @Function: 题目选项
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class QuestionTitleOptionService extends BaseService {

	@Autowired
	private ExamRecordDetailMapper examRecordDetailMapper;

	@Autowired
	private ExamRecordMapper examRecordMapper;

	@Autowired
	private QuestionTitleOptionMapper questionTitleOptionMapper;

	/** listQuestionTitleOption : 获取题目选项列表数据 **/
	/** 参数param内为title的rec_id **/
	public List<Map<String, Object>> listQuestionTitleOption(Map<String, Object> param) {
		super.appendUserInfo(param);
		Map<String, Object> querycondition = new HashMap<String, Object>();
		querycondition.put("title_id", param.get("title_id"));
		querycondition.put("qbank_id", param.get("qbank_id"));

        // List<Map<String, Object>> listbankRecord = examRecordMapper.listExamRecord(querycondition);
        // int joinTotal = listbankRecord.size();

        // 改为统计每个题目的参与人数来算百分比
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("title_id", param.get("title_id"));
        int joinTotal = this.count("d_exam_record_detail", params);


		List<Map<String, Object>> listOption = questionTitleOptionMapper.listQuestionTitleOption(querycondition);
		for (int i = 0; i < listOption.size(); i++) {
			Map<String, Object> opMap = listOption.get(i);
			String opRecId = StrUtil.getMapValue(opMap, "rec_id");
			double choiceNum = 0;
			List<Map<String, Object>> optionRecord = examRecordDetailMapper.listExamRecordDetail(querycondition);
			for (int j = 0; j < optionRecord.size(); j++) {
				Map<String, Object> mapOptionRecord = optionRecord.get(j);
				String recordAnswer = StrUtil.getMapValue(mapOptionRecord, "answers");
				String[] answerArr = recordAnswer.split(",");
				boolean iscontain = Arrays.asList(answerArr).contains(opRecId);
				if (iscontain) {
					choiceNum = choiceNum + 1;
				}
			}
			opMap.put("choiceNum", choiceNum);
			if (choiceNum != 0) {
				double choicePercent = formatDouble(choiceNum / joinTotal * 100);
				opMap.put("choicePercent", choicePercent);
			} else {
				opMap.put("choicePercent", 0);
			}
		}
		return listOption;
	}

	// 取两位小数
	public Double formatDouble(double num) {
		BigDecimal bd = new BigDecimal(num);
		num = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return num;
	}

	/** addQuestionTitleOption : 新增题目选项 **/
	public ResponseBodyVO addQuestionTitleOption(Map<String, Object> param) {

		// Table:d_question_title_option
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("title_id", param.get("title_id"));
		mapParam.put("option_name", param.get("option_name"));
		mapParam.put("is_singlerow", param.get("is_singlerow"));//是否单行：0-否（默认），1-是
		mapParam.put("option_sort", param.get("option_sort"));
		int count = this.save("d_question_title_option", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "新增题目选项失败");
	}

	/** updateQuestionTitleOption : 修改题目选项 **/
	public ResponseBodyVO updateQuestionTitleOption(Map<String, Object> param) {

		// Table:d_question_title_option
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("title_id", param.get("title_id"));
		mapParam.put("option_name", param.get("option_name"));
		mapParam.put("is_singlerow", param.get("is_singlerow"));//是否单行：0-否（默认），1-是
		mapParam.put("option_sort", param.get("option_sort"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.update(mapCondition, "d_question_title_option", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改题目选项失败");
	}

	/** delQuestionTitleOption : 删除题目选项 **/
	public ResponseBodyVO delQuestionTitleOption(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断

		// Table:d_question_title_option
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.delete("d_question_title_option", mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除题目选项失败");
	}

}
