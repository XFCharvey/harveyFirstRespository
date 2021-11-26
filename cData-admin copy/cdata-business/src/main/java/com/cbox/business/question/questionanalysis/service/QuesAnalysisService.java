package com.cbox.business.question.questionanalysis.service;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbox.base.annotation.DataScope;
import com.cbox.base.constant.HttpStatus;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.service.BaseService;
import com.cbox.base.utils.SecurityUtils;

import com.cbox.business.question.questionanalysis.mapper.QuesAnalysisMapper;


/**
 * @ClassName: QuestionAnalysisService
 * @Function: 问卷分析要素
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class QuesAnalysisService extends BaseService {

    @Autowired
    private QuesAnalysisMapper questionAnalysisMapper;
    
	/** listQuestionAnalysis : 获取问卷分析要素列表数据 **/
	public List<Map<String, Object>> listQuestionAnalysis(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = questionAnalysisMapper.listQuestionAnalysis(param);

		return list;
	}

	/** getQuestionAnalysis : 获取指定id的问卷分析要素数据 **/
	public ResponseBodyVO getQuestionAnalysis(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = questionAnalysisMapper.getQuestionAnalysis(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addQuestionAnalysis : 新增问卷分析要素 **/
	public ResponseBodyVO addQuestionAnalysis(Map<String, Object> param) {

		// Table:d_question_analysis
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("item_type", param.get("item_type"));
		mapParam.put("item_key", param.get("item_key"));
		mapParam.put("item_name", param.get("item_name"));
		mapParam.put("rela_dict_id", param.get("rela_dict_id"));
		mapParam.put("question_id", param.get("question_id"));
		mapParam.put("rela_title_id", param.get("rela_title_id"));
		int count = this.save( "d_question_analysis", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "新增问卷分析要素失败");
	}

	/** updateQuestionAnalysis : 修改问卷分析要素 **/
	public ResponseBodyVO updateQuestionAnalysis(Map<String, Object> param) {

		// Table:d_question_analysis
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("item_type", param.get("item_type"));
		mapParam.put("item_key", param.get("item_key"));
		mapParam.put("item_name", param.get("item_name"));
		mapParam.put("rela_dict_id", param.get("rela_dict_id"));
		mapParam.put("question_id", param.get("question_id"));
		mapParam.put("rela_title_id", param.get("rela_title_id"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.update(mapCondition, "d_question_analysis", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改问卷分析要素失败"); 
	}

	/** delQuestionAnalysis : 删除问卷分析要素 **/
	public ResponseBodyVO delQuestionAnalysis(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断 

		// Table:d_question_analysis
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.delete("d_question_analysis",  mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除问卷分析要素失败");
	}


}
