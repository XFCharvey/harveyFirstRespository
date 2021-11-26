package com.cbox.business.question.questiontitlegroup.service;

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

import com.cbox.business.question.questiontitlegroup.mapper.QuestiontitlegroupMapper;


/**
 * @ClassName: QuestiontitlegroupService
 * @Function: 题目分组
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class QuestiontitlegroupService extends BaseService {

    @Autowired
    private QuestiontitlegroupMapper questiontitlegroupMapper;
    
	/** listQuestiontitlegroup : 获取题目分组列表数据 **/
	public List<Map<String, Object>> listQuestiontitlegroup(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = questiontitlegroupMapper.listQuestiontitlegroup(param);

		return list;
	}

	/** getQuestiontitlegroup : 获取指定id的题目分组数据 **/
	public ResponseBodyVO getQuestiontitlegroup(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = questiontitlegroupMapper.getQuestiontitlegroup(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addQuestiontitlegroup : 新增题目分组 **/
	public ResponseBodyVO addQuestiontitlegroup(Map<String, Object> param) {

		// Table:d_question_title_group
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("question_id", param.get("question_id"));
		mapParam.put("group_name", param.get("group_name"));
		mapParam.put("group_sort", param.get("group_sort"));
		int count = this.save( "d_question_title_group", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "新增题目分组失败");
	}

	/** updateQuestiontitlegroup : 修改题目分组 **/
	public ResponseBodyVO updateQuestiontitlegroup(Map<String, Object> param) {

		// Table:d_question_title_group
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("question_id", param.get("question_id"));
		mapParam.put("group_name", param.get("group_name"));
		mapParam.put("group_sort", param.get("group_sort"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.update(mapCondition, "d_question_title_group", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改题目分组失败"); 
	}

	/** delQuestiontitlegroup : 删除题目分组 **/
	public ResponseBodyVO delQuestiontitlegroup(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断 

		// Table:d_question_title_group
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.delete("d_question_title_group",  mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除题目分组失败");
	}


}
