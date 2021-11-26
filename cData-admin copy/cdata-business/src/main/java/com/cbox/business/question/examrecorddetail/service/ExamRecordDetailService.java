package com.cbox.business.question.examrecorddetail.service;

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
import com.cbox.business.question.examrecorddetail.mapper.ExamRecordDetailMapper;


/**
 * @ClassName: ExamRecordDetailService
 * @Function: 考试/问卷记录详情
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class ExamRecordDetailService extends BaseService {

    @Autowired
    private ExamRecordDetailMapper examRecordDetailMapper;
    
	/** listExamRecordDetail : 获取考试/问卷记录详情列表数据 **/
	public List<Map<String, Object>> listExamRecordDetail(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = examRecordDetailMapper.listExamRecordDetail(param);

		return list;
	}

	/** getExamRecordDetail : 获取指定id的考试/问卷记录详情数据 **/
	public ResponseBodyVO getExamRecordDetail(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = examRecordDetailMapper.getExamRecordDetail(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addExamRecordDetail : 新增考试/问卷记录详情 **/
	public ResponseBodyVO addExamRecordDetail(Map<String, Object> param) {

		// Table:d_exam_record_detail
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("record_id", param.get("record_id"));
		mapParam.put("title_id", param.get("title_id"));
		mapParam.put("answers", param.get("answers"));
        int count = this.saveNoRec("d_exam_record_detail", mapParam, false);

		return ServerRspUtil.formRspBodyVO(count, "新增考试/问卷记录详情失败");
	}

	/** updateExamRecordDetail : 修改考试/问卷记录详情 **/
	public ResponseBodyVO updateExamRecordDetail(Map<String, Object> param) {

		// Table:d_exam_record_detail
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("record_id", param.get("record_id"));
		mapParam.put("title_id", param.get("title_id"));
		mapParam.put("answers", param.get("answers"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
        int count = this.updateNoRec(mapCondition, "d_exam_record_detail", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改考试/问卷记录详情失败"); 
	}

	/** delExamRecordDetail : 删除考试/问卷记录详情 **/
	public ResponseBodyVO delExamRecordDetail(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断 

		// Table:d_exam_record_detail
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
        int count = this.deleteEmpty("d_exam_record_detail", mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除考试/问卷记录详情失败");
	}


}
