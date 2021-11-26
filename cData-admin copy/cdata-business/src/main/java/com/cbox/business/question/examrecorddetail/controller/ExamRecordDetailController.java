package com.cbox.business.question.examrecorddetail.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cbox.base.core.controller.BaseController;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.page.TableDataInfo;
import com.cbox.business.question.examrecorddetail.service.ExamRecordDetailService;


/**
 * @ClassName: ExamRecordDetailController
 * @Function: 考试/问卷记录详情
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/examrecorddetail")
public class ExamRecordDetailController extends BaseController {

	@Autowired
	private ExamRecordDetailService examRecordDetailService;

	
	/** listExamRecordDetail : 获取考试/问卷记录详情列表数据 **/
	@RequestMapping(value = "listExamRecordDetail", method = RequestMethod.POST)
	public TableDataInfo listExamRecordDetail(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list= examRecordDetailService.listExamRecordDetail(param);

		return getDataTable(list);
	}

	/** getExamRecordDetail : 获取指定id的考试/问卷记录详情数据 **/
	@RequestMapping(value = "getExamRecordDetail", method = RequestMethod.POST)
	public ResponseBodyVO getExamRecordDetail(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return examRecordDetailService.getExamRecordDetail(param);
	}

	/** addExamRecordDetail : 新增考试/问卷记录详情 **/
	@RequestMapping(value = "addExamRecordDetail", method = RequestMethod.POST)
	public ResponseBodyVO addExamRecordDetail(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("record_id,title_id,answers", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return examRecordDetailService.addExamRecordDetail(param);
	}

	/** updateExamRecordDetail : 修改考试/问卷记录详情 **/
	@RequestMapping(value = "updateExamRecordDetail", method = RequestMethod.POST)
	public ResponseBodyVO updateExamRecordDetail(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return examRecordDetailService.updateExamRecordDetail(param);
	}

	/** delExamRecordDetail : 删除考试/问卷记录详情 **/
	@RequestMapping(value = "delExamRecordDetail", method = RequestMethod.POST)
	public ResponseBodyVO delExamRecordDetail(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return examRecordDetailService.delExamRecordDetail(param);
	}


}
