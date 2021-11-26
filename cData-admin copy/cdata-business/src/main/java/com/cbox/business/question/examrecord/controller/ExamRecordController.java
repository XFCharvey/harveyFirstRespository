package com.cbox.business.question.examrecord.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cbox.base.core.controller.BaseController;
import com.cbox.base.core.domain.AjaxResult;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.page.TableDataInfo;
import com.cbox.business.question.examrecord.service.ExamRecordService;

/**
 * @ClassName: ExamRecordController
 * @Function: 考试/问卷记录
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/examrecord")
public class ExamRecordController extends BaseController {

	@Autowired
	private ExamRecordService examRecordService;

	/** listExamRecord : 获取考试/问卷记录列表数据 **/
	@RequestMapping(value = "listExamRecord", method = RequestMethod.POST)
	public TableDataInfo listExamRecord(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list = examRecordService.listExamRecord(param);

		return getDataTable(list);
	}

    /** listCommonExamRecord : 获取客户除偏好问卷以外的所有问卷记录列表数据 **/
    @RequestMapping(value = "listCommonExamRecord", method = RequestMethod.POST)
    public TableDataInfo listCommonExamRecord(@RequestBody Map<String, Object> param) {

        startPage();
        List<Map<String, Object>> list = examRecordService.listCommonExamRecord(param);

        return getDataTable(list);
    }

	/** getExamRecord : 获取指定id的考试/问卷记录数据 **/
	@RequestMapping(value = "getExamRecord", method = RequestMethod.POST)
	public ResponseBodyVO getExamRecord(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return examRecordService.getExamRecord(param);
	}

	/** exportRecord : 导出获奖数据 **/
	@RequestMapping(value = "exportRecord", method = RequestMethod.POST)
	public AjaxResult exportRecord(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
//		if (!VALID_SUCC.equals(checkResult)) {
//			return ServerRspUtil.error(checkResult);
//		}

		return examRecordService.exportRecord(param);
	}

	/** addExamRecord : 新增考试/问卷记录 **/
	@RequestMapping(value = "addExamRecord", method = RequestMethod.POST)
	public ResponseBodyVO addExamRecord(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("qbank_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return examRecordService.addExamRecord(param);
	}

	/** delExamRecord : 删除考试/问卷记录 **/
	@RequestMapping(value = "delExamRecord", method = RequestMethod.POST)
	public ResponseBodyVO delExamRecord(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return examRecordService.delExamRecord(param);
	}

}
