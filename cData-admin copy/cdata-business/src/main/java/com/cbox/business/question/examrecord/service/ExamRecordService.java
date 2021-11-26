package com.cbox.business.question.examrecord.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.cbox.business.question.examrecord.bean.ExportrecordVO;
import com.cbox.business.question.examrecord.mapper.ExamRecordMapper;
import com.cbox.business.question.questionbank.mapper.QuestionBankMapper;

/**
 * @ClassName: ExamRecordService
 * @Function: 考试/问卷记录
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class ExamRecordService extends BaseService {

	@Autowired
	private ExamRecordMapper examRecordMapper;

	@Autowired
	private QuestionBankMapper questionBankMapper;

	/** listExamRecord : 获取考试/问卷记录列表数据 **/
	public List<Map<String, Object>> listExamRecord(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = examRecordMapper.listExamRecord(param);

		return list;
	}

    /** listCommonExamRecord : 获取客户除偏好问卷以外的所有问卷记录列表数据 **/
    public List<Map<String, Object>> listCommonExamRecord(Map<String, Object> param) {
        // TODO Auto-generated method stub
        super.appendUserInfo(param);

        List<Map<String, Object>> listCommonExamRecord = examRecordMapper.listCommonExamRecord(param);

        return listCommonExamRecord;
    }

	/** getExamRecord : 获取指定id的考试/问卷记录数据 **/
	public ResponseBodyVO getExamRecord(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = examRecordMapper.getExamRecord(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** exportRecord : 获取指定id的考试/问卷记录数据 **/
	public AjaxResult exportRecord(Map<String, Object> param) {
		super.appendUserInfo(param);
		List<ExportrecordVO> listRecordVO = new ArrayList<ExportrecordVO>();// 缓存要导出的获奖记录
		Map<String, Object> mapCondition = new HashMap<String, Object>();

		mapCondition.put("reward_status", "1");

		List<Map<String, Object>> list = examRecordMapper.listExamRecord(param);
		String[] recIds = StrUtil.getMapValue(param, "rec_id").split(",");
		// 获取获奖记录，并更新状态
		if (ObjUtil.isNotNull(list)) {
			for (int i = 0; i < list.size(); i++) {

				Map<String, Object> getRecord = list.get(i);
				String rewardStatus = StrUtil.getMapValue(getRecord, "reward_status");
				String rewardId = StrUtil.getMapValue(getRecord, "reward_id");
				if (rewardStatus.equals("0") && !rewardId.equals("8")) {
					Map<String, Object> mapRecordParam = new HashMap<String, Object>();
					mapRecordParam.put("rec_id", getRecord.get("rec_id"));
					this.updateNoRec(mapRecordParam, "d_exam_record", mapCondition);// 更新未中奖的状态为兑奖中
				}
				if ((rewardStatus.equals("0") || rewardStatus.equals("1")) && !rewardId.equals("8")) {
					ExportrecordVO recordVo = new ExportrecordVO();
					recordVo.setExam_person(StrUtil.getMapValue(getRecord, "exam_person"));
					recordVo.setPerson_phone(StrUtil.getMapValue(getRecord, "person_phone"));
					recordVo.setReward_id(StrUtil.getMapValue(getRecord, "reward_id"));
					recordVo.setReward_name(StrUtil.getMapValue(getRecord, "reward_name"));
					recordVo.setReward_value(StrUtil.getMapValue(getRecord, "reward_value"));
					listRecordVO.add(recordVo);
				}
			}
		}

		// 解析excel文件
		ExcelUtil<ExportrecordVO> util = new ExcelUtil<ExportrecordVO>(ExportrecordVO.class);
		// 获取日期
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String datef = sdf.format(date);
		String sheetName = datef + "日导出兑奖数据";
		AjaxResult result = util.exportExcel(listRecordVO, sheetName);

		return result;
	}

	/** addExamRecord : 新增考试/问卷记录 **/
	@SuppressWarnings("unchecked")
	public ResponseBodyVO addExamRecord(Map<String, Object> param) {
		int count = 0;
		String userName = SecurityUtils.getUsername();
		String nowTime = DateUtils.getTime();
		String nowYear = DateUtils.getYear();

        // 根据传入的customer_id取客户信息
        Map<String, Object> mapParamCustomer = new HashMap<String, Object>();
        mapParamCustomer.put("rec_id", param.get("customer_id"));
        Map<String, Object> mapCustomer = this.queryOneNoRec("d_customer", mapParamCustomer);

		// Table:d_exam_record
		Map<String, Object> mapRecordParam = new HashMap<String, Object>();
		mapRecordParam.put("qbank_id", param.get("qbank_id"));
		mapRecordParam.put("exam_person", userName);
		mapRecordParam.put("exam_time", nowTime);
		mapRecordParam.put("record_type", param.get("record_type"));
		mapRecordParam.put("use_time", param.get("use_time"));



		// 关联的学习计划的条件
		Map<String, Object> mapInfoLearnCondition = new HashMap<String, Object>();
		mapInfoLearnCondition.put("info_id", param.get("info_id"));
		mapInfoLearnCondition.put("learn_person", userName);
		mapInfoLearnCondition.put("learn_year", nowYear);

		// 关联的学习计划得的结果修改内容
		Map<String, Object> mapInfoLearnParam = new HashMap<String, Object>();
		mapInfoLearnParam.put("answer_time", nowTime);

		int Totalscore = 0;
		List<Map<String, Object>> listTitle = (List<Map<String, Object>>) param.get("titleList");
		// 遍历题库每个题目的数据
		Map<String, Object> bankConditionparam = new HashMap<String, Object>();
		bankConditionparam.put("rec_id", param.get("qbank_id"));
		Map<String, Object> bankMap = questionBankMapper.getQuestionBank(bankConditionparam);
		if ("exam".equals(bankMap.get("q_type"))) {
			for (int i = 0; i < listTitle.size(); i++) {
				Map<String, Object> titleMap = (Map<String, Object>) listTitle.get(i);
				List<Map<String, Object>> listOption = (List<Map<String, Object>>) titleMap.get("optionList");
				String titleanswer = StrUtil.getMapValue(titleMap, "title_answer");
				int titlescroe = StrUtil.getMapIntValue(titleMap, "title_score");

				if ("input".equals(titleMap.get("title_type"))) {
					String paramTitleAnswer = StrUtil.getMapValue(titleMap, "input_answer");
					System.out.println(paramTitleAnswer);
					if (titleanswer.equals(paramTitleAnswer)) {
						Totalscore += titlescroe;
						System.out.println(Totalscore);
					}
				} else {
					List<String> listoptionrecid = new ArrayList<String>();
					// 遍历题目的每个选项数据
					for (int j = 0; j < listOption.size(); j++) {
						Map<String, Object> optionMap = (Map<String, Object>) listOption.get(j);
						// 将所有前端的选项放在listoptionrecid空的list里
						listoptionrecid.add(StrUtil.getMapValue(optionMap, "rec_id"));
					}
					// 多个值就按逗号分隔
					String optionrecid = String.join(",", listoptionrecid);
					System.out.println(optionrecid);
					// 判断是否回答正确,正确就加分
					if (optionrecid.equals(titleanswer)) {
						Totalscore += titlescroe;
					}
				}
				mapRecordParam.put("exam_score", Totalscore);
				mapInfoLearnParam.put("answer_score", Totalscore);
			}
			if (bankMap.get("qualified_score") != null) {
				// 总分
				int fullScore = StrUtil.getMapIntValue(bankMap, "full_score");
				// 优秀分数
				double excellentScore = fullScore * 0.9;
				// 及格分数
				double qualifiedScore = fullScore * 0.7;
				if (Totalscore < excellentScore && Totalscore >= qualifiedScore) {
					mapRecordParam.put("is_passed", "1");
					mapInfoLearnParam.put("answer_status", "1");
				} else if (Totalscore >= excellentScore) {
					mapRecordParam.put("is_passed", "2");
					mapInfoLearnParam.put("answer_status", "1");
				} else {
					mapRecordParam.put("is_passed", "0");
					mapInfoLearnParam.put("answer_status", "2");
				}
			}

            // 只有考试才需要操作
            count = this.saveNoRec("d_exam_record_his", mapRecordParam, true);
            count += this.updateNoRec(mapInfoLearnCondition, "d_info_learn", mapInfoLearnParam);
		} else {
			mapRecordParam.put("exam_score", null);
			mapRecordParam.put("is_passed", null);

            // 根据客户信息插入到调研表中
            if (ObjUtil.isNotNull(mapCustomer)) {
                mapRecordParam.put("customer_id", param.get("customer_id"));

                mapRecordParam.put("exam_person", StrUtil.getMapValue(mapCustomer, "customer_name"));
                mapRecordParam.put("person_phone", StrUtil.getMapValue(mapCustomer, "customer_phone"));
                String birthDate = StrUtil.getMapValue(mapCustomer, "customer_birthdate");
                if (StrUtil.isNotNull(birthDate) && birthDate.length() > 4) {
                    birthDate = birthDate.substring(0, 4);
                    mapRecordParam.put("person_birthyear", birthDate);
                }
                mapRecordParam.put("person_sex", StrUtil.getMapValue(mapCustomer, "customer_sex"));
                mapRecordParam.put("person_work", StrUtil.getMapValue(mapCustomer, "customer_vocation"));
            }
		}



		Map<String, Object> mapParamRe = new HashMap<String, Object>();
        if ("exam".equals(bankMap.get("q_type"))) {
            mapParamRe.put("exam_person", userName);
        } else {
            mapParamRe.put("customer_id", param.get("customer_id"));
        }
		mapParamRe.put("qbank_id", param.get("qbank_id"));
		List<Map<String, Object>> listRecord = examRecordMapper.listExamRecord(mapParamRe);
		if (listRecord == null || listRecord.size() == 0) {
            count = this.saveNoRec("d_exam_record", mapRecordParam, true);
		} else {
            Map<String, Object> mpRecord = listRecord.get(0);
            int lastScore = StrUtil.getMapIntValue(mpRecord, "exam_score");
            int thatScore = StrUtil.getMapIntValue(mapRecordParam, "exam_score");
            if (thatScore >= lastScore) {
                Map<String, Object> mapCondition = new HashMap<String, Object>();
                mapCondition.put("rec_id", mpRecord.get("rec_id"));
                count = this.updateNoRec(mapCondition, "d_exam_record", mapRecordParam);

                mapRecordParam.put("rec_id", mpRecord.get("rec_id"));

                // 先清空掉答题详情
                mapCondition = new HashMap<String, Object>();
                mapCondition.put("record_id", mpRecord.get("rec_id"));
                mapCondition.put("qbank_id", param.get("qbank_id"));
                count = this.deleteEmpty("d_exam_record_detail", mapCondition);
            }
		}

		String recId = StrUtil.getMapValue(mapRecordParam, "rec_id");
		Map<String, Object> mapParamdetail = new HashMap<String, Object>();
		for (int i = 0; i < listTitle.size(); i++) {
			Map<String, Object> titleMap = (Map<String, Object>) listTitle.get(i);

			List<Map<String, Object>> listOption = (List<Map<String, Object>>) titleMap.get("optionList");
			List<String> listoptionid = new ArrayList<String>();
			// 判断是不是填空题
			if ("input".equals(titleMap.get("title_type"))) {
				mapParamdetail.put("answers", titleMap.get("input_answer"));
			} else {
				for (int j = 0; j < listOption.size(); j++) {
					Map<String, Object> optionMap = (Map<String, Object>) listOption.get(j);
					listoptionid.add(StrUtil.getMapValue(optionMap, "rec_id"));
				}
				String answerid = String.join(",", listoptionid);
				mapParamdetail.put("answers", answerid);
			}

			mapParamdetail.put("record_id", recId);
			mapParamdetail.put("title_id", titleMap.get("rec_id"));
            mapParamdetail.put("qbank_id", param.get("qbank_id"));

			count = this.saveNoRec("d_exam_record_detail", mapParamdetail, false);
		}

		return ServerRspUtil.formRspBodyVO(count, "新增考试/问卷记录失败");
	}

	/** delExamRecord : 删除考试/问卷记录 **/
	public ResponseBodyVO delExamRecord(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断

		// Table:d_exam_record
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.deleteEmpty("d_exam_record", mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除考试/问卷记录失败");
	}


}
