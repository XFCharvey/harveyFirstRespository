package com.cbox.business.question.analysis.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.cbox.base.config.CboxConfig;
import com.cbox.base.constant.Constants;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.service.BaseService;
import com.cbox.base.utils.DateUtils;
import com.cbox.base.utils.ObjUtil;
import com.cbox.base.utils.StrUtil;
import com.cbox.base.utils.StringUtils;
import com.cbox.business.question.analysis.mapper.QuestionAnalysisMapper;
import com.cbox.business.question.analysis.util.WorkTransUtil;
import com.cbox.business.question.examrecorddetail.mapper.ExamRecordDetailMapper;
import com.cbox.business.question.questiontitle.mapper.QuestiontitleMapper;
import com.cbox.business.question.questiontitlegroup.mapper.QuestiontitlegroupMapper;
import com.cbox.business.question.questiontitleoption.mapper.QuestionTitleOptionMapper;

/**
 * @ClassName: QuestionAnalysisService
 * @Function: 问卷交叉分析
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class QuestionAnalysisService extends BaseService {

	@Autowired
	private QuestiontitlegroupMapper questiontitlegroupMapper;

	@Autowired
	private QuestiontitleMapper questiontitleMapper;

	@Autowired
	private QuestionTitleOptionMapper questionTitleOptionMapper;

	@Autowired
	private ExamRecordDetailMapper examRecordDetailMapper;

	@Autowired
	private QuestionAnalysisMapper questionAnalysisMapper;

	/**
	 * exportAnalysisByTitle:根据题目信息，导出相关选票的excel文件
	 * 
	 * @throws IOException
	 **/
	public String exportAnalysisInfo(Map<String, Object> param) throws IOException {

		String titleName = StrUtil.getMapValue(param, "title_name");
		String fileName = StrUtil.getMapValue(param, "q_name") + "-" + titleName + ".xls";

		if (ObjUtil.isNotNull(param)) {
			// 1.创建工作簿
			// 2.创建表名,多级
			// 3.获取sheet
			// 4.创建行
			// 5.创建单元格
			// 6.写入数据
			Workbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = (HSSFSheet) workbook.createSheet();
			workbook.setSheetName(0, titleName);
			HSSFSheet sheet0 = (HSSFSheet) workbook.getSheetAt(0);
			List<Map<String, Object>> options = (List<Map<String, Object>>) param.get("options");

			if (ObjUtil.isNotNull(options)) {
				Row row0 = sheet0.createRow(0);
				Cell cell = row0.createCell(1);
				cell.setCellValue("票数");
				cell = row0.createCell(2);
				cell.setCellValue("比例");

				// 选项、票数从第二行开始
				for (int i = 0; i < options.size(); i++) {
					Map<String, Object> mapOption = options.get(i);
					Row row = sheet0.createRow(i + 1);

					// 选项、票数、比例固定三个位置
					Cell cellI = null;
					cellI = row.createCell(0);
					cellI.setCellValue(StrUtil.getMapValue(mapOption, "option_name"));
					cellI = row.createCell(1);
					cellI.setCellValue(StrUtil.getMapValue(mapOption, "choiceNum") + "票");
					cellI = row.createCell(2);
					cellI.setCellValue(StrUtil.getMapValue(mapOption, "choicePercent") + "%");

				}
			}

			// 创建不同sheet项
			List<Map<String, Object>> optionAttrs = (List<Map<String, Object>>) param.get("optionAttrs");
			if (ObjUtil.isNotNull(optionAttrs)) {
				for (int i = 0; i < optionAttrs.size(); i++) {
					Map<String, Object> optionAttr = optionAttrs.get(i);
					sheet = (HSSFSheet) workbook.createSheet();
					int sheetIndex = i + 1;
					workbook.setSheetName(sheetIndex,
							"按【" + StrUtil.getMapValue(optionAttr, "analysis_name") + "】分类统计");
					HSSFSheet sheetAttr = (HSSFSheet) workbook.getSheetAt(sheetIndex);

					List<Map<String, Object>> analysisResult = (List<Map<String, Object>>) optionAttr
							.get("analysis_result");
					// 投票比例内容
					if (ObjUtil.isNotNull(analysisResult)) {
						Row row0 = sheetAttr.createRow(0);
						Cell cell = row0.createCell(2);
						cell.setCellValue("票数");
						cell = row0.createCell(3);
						cell.setCellValue("比例");

						// 选项、票数从第二行开始
						int indexNext = 1;
						for (int j = 0; j < analysisResult.size(); j++) {
							Map<String, Object> mapResult = analysisResult.get(j);
							Row row = sheetAttr.createRow(indexNext);

							Cell cellI = null;
							cellI = row.createCell(0);
							cellI.setCellValue(StrUtil.getMapValue(mapResult, "item_key"));
							List<Map<String, Object>> itemList = (List<Map<String, Object>>) mapResult
									.get("item_result");
							if (ObjUtil.isNotNull(itemList)) {
								for (int z = 0; z < itemList.size(); z++) {
									Map<String, Object> item = itemList.get(z);
									if (z > 0) {
										indexNext = indexNext + 1;
										Row rowCreate = sheetAttr.createRow(indexNext);
										cellI = rowCreate.createCell(1);
										cellI.setCellValue(StrUtil.getMapValue(item, "option_name"));
										cellI = rowCreate.createCell(2);
										cellI.setCellValue(StrUtil.getMapValue(item, "choiceNum") + "票");
										cellI = rowCreate.createCell(3);
										cellI.setCellValue(StrUtil.getMapValue(item, "choicePercent") + "%");
									} else {
										cellI = row.createCell(1);
										cellI.setCellValue(StrUtil.getMapValue(item, "option_name"));
										cellI = row.createCell(2);
										cellI.setCellValue(StrUtil.getMapValue(item, "choiceNum") + "票");
										cellI = row.createCell(3);
										cellI.setCellValue(StrUtil.getMapValue(item, "choicePercent") + "%");
									}
								}
								indexNext=indexNext+itemList.size()-1;
							}
						}
					}
				}
			}

			// 6.创建流用于输出
			FileOutputStream fileOutputStream = new FileOutputStream(getAbsoluteFile(fileName));
			// 7.输出
			workbook.write(fileOutputStream);
			
			fileOutputStream.close();

			System.out.println(fileName + "：已生成！！！");

		}

		return fileName;
	}

	public String formatTitleType(String titleType) {
		String type = "";
		if (titleType.equals("radio")) {
			type = "(单选)";
		} else {
			type = "(多选)";
		}
		return type;
	}

	/**
	 * 获取下载路径
	 * 
	 * @param filename 文件名称
	 */
	public String getAbsoluteFile(String filename) {
		String downloadPath = CboxConfig.getDownloadPath() + filename;
		File desc = new File(downloadPath);
		if (!desc.getParentFile().exists()) {
			desc.getParentFile().mkdirs();
		}
		return downloadPath;
	}

	private static final String getPathFileName(String uploadDir, String fileName) throws IOException {
		int dirLastIndex = CboxConfig.getProfile().length() + 1;
		String currentDir = StringUtils.substring(uploadDir, dirLastIndex);
		String pathFileName = Constants.RESOURCE_PREFIX + "/" + currentDir + "/" + fileName;
		return pathFileName;
	}

	/**
	 * getQuestionResult:
	 *
	 * @date: 2021年10月19日 下午10:32:48
	 * @author qiuzq
	 * @param param 传参question_id,has_result(0-无，只是题目；1-有结果）
	 * @return
	 */
	public ResponseBodyVO getQuestionResult(@RequestBody Map<String, Object> param) {

		// 从传参中判断是否需要返回结果
		String hasResult = StrUtil.getMapValue(param, "has_result", "1");// 默认是要结果
		boolean bResult = false;
		if ("1".equals(hasResult)) {
			bResult = true;
		}

		// 获取分组数据
		List<Map<String, Object>> listGroup = questiontitlegroupMapper.listQuestiontitlegroup(param);

		// 获取title数据
		List<Map<String, Object>> listTitles = questiontitleMapper.listQuestiontitle(param);
		for (int j = 0; j < listTitles.size(); j++) {
			Map<String, Object> mapTitle = listTitles.get(j);
			String titleGroupId = StrUtil.getMapValue(mapTitle, "group_id");
			if (StrUtil.isNull(titleGroupId)) {
				titleGroupId = "0";
				mapTitle.put("group_id", titleGroupId);
			}
		}
		Map<String, List<Map<String, Object>>> mapListTitles = ObjUtil.transList(listTitles, "group_id");

		// 获取Options选项数据
		List<Map<String, Object>> listOptions = questionTitleOptionMapper.listQuestionTitleOption(param);
		Map<String, List<Map<String, Object>>> mapListOptions = ObjUtil.transList(listOptions, "title_id");

		Map<String, List<Map<String, Object>>> mapListRecord = null;
		Map<String, List<Map<String, Object>>> mapListRecordAttr = null;
		Map<String, String> mapAnalysisOption = new HashMap<String, String>();
		Map<String, Map<String, Object>> mapAnalysisAttr = new HashMap<String, Map<String, Object>>();
		if (bResult) {
			// 获取调研选择结果数据
			List<Map<String, Object>> listRecord = examRecordDetailMapper.listExamRecordDetail(param);
			mapListRecord = ObjUtil.transList(listRecord, "title_id");

			String questionId = StrUtil.getMapValue(param, "question_id");
			// 获取调研人的属性数据
			Map<String, Object> mapParamAtrr = new HashMap<String, Object>();
			mapParamAtrr.put("qbank_id", questionId);
			List<Map<String, Object>> listRecordAttr = this.queryNoRec("d_exam_record_option", mapParamAtrr);
			mapListRecordAttr = ObjUtil.transList(listRecordAttr, "record_id");

			mapParamAtrr = new HashMap<String, Object>();
			mapParamAtrr.put("question_id", questionId);
			List<Map<String, Object>> listAnalysisAttr = this.queryNoRec("d_question_analysis", mapParamAtrr);
			for (int w = 0; w < listAnalysisAttr.size(); w++) {
				Map<String, Object> map = listAnalysisAttr.get(w);
				mapAnalysisOption.put(StrUtil.getMapValue(map, "item_key"), StrUtil.getMapValue(map, "item_name"));
			}
			mapAnalysisAttr = ObjUtil.transListToMap(listAnalysisAttr, "item_key");
		}

		// 设置默认的空分组
		String defaultGroupId = "0";
		List<Map<String, Object>> listDefaultTitles = mapListTitles.get(defaultGroupId);
		if (!ObjUtil.isNull(listDefaultTitles)) {
			Map<String, Object> mapNullGroup = new HashMap<String, Object>();
			mapNullGroup.put("group_name", "");
			mapNullGroup.put("rec_id", "0");
			listGroup.add(mapNullGroup);
		}

		for (int i = 0; i < listGroup.size(); i++) {
			Map<String, Object> mapGroup = listGroup.get(i);
			String groupId = StrUtil.getMapValue(mapGroup, "rec_id");

			List<Map<String, Object>> listGroupTitles = new ArrayList<Map<String, Object>>();

			List<Map<String, Object>> listCurrTitles = mapListTitles.get(groupId);
			// 遍历所有的题目
			if (ObjUtil.isNull(listCurrTitles))
				continue;
			for (int j = 0; j < listCurrTitles.size(); j++) {
				Map<String, Object> mapTitle = listCurrTitles.get(j);
				String titleId = StrUtil.getMapValue(mapTitle, "rec_id");
				String analysisItems = StrUtil.getMapValue(mapTitle, "analysis_items");

				List<Map<String, Object>> listTitleOptions = new ArrayList<Map<String, Object>>();

				Map<String, Integer> mapOptionCount = new LinkedHashMap<String, Integer>();
				List<Map<String, Object>> listCurrOptions = mapListOptions.get(titleId);
				List<Map<String, Object>> listCurrRecord = null;
				if (bResult) {
					listCurrRecord = mapListRecord.get(titleId);
				}

				if (ObjUtil.isNull(listCurrOptions))
					continue;

				for (int h = 0; h < listCurrOptions.size(); h++) {
					Map<String, Object> mapOption = listCurrOptions.get(h);
					String opRecId = StrUtil.getMapValue(mapOption, "rec_id");

					// 需要获取结果
					if (bResult) {
						this.calcResult(titleId, analysisItems, mapOption, listCurrRecord, mapOptionCount,
								mapListRecordAttr);
					}

					listTitleOptions.add(mapOption);
				}

				mapTitle.put("options", listTitleOptions);

				// if (!analysisItems.contains(itemKey)) {
				// continue;
				// }

				List<Map<String, Object>> listOptionAttrs = new ArrayList<Map<String, Object>>();
				if (ObjUtil.isNotNull(mapOptionCount) && StrUtil.isNotNull(analysisItems)) {
					String[] aItems = analysisItems.split(",");

					for (int g = 0; g < aItems.length; g++) {

						// 先得到总数量

						Map<String, Integer> mapCount = new HashMap<String, Integer>();
						for (Map.Entry<String, Integer> entry : mapOptionCount.entrySet()) {

							String[] keys = entry.getKey().split("@");
							if (!keys[0].equals(aItems[g])) {
								continue;
							}
							String key = keys[1];

							int choiceNum = entry.getValue();

							int joinTotal = 0;
							if (mapCount.containsKey(key)) {
								joinTotal = mapCount.get(key);
							}
							joinTotal += choiceNum;

							mapCount.put(key, joinTotal);
						}

						Map<String, List<Map<String, Object>>> mapListOptionAttr = new LinkedHashMap<String, List<Map<String, Object>>>();
						this.initOptionMap(mapListOptionAttr, aItems[g], mapAnalysisAttr, mapListOptions);// 初始化map，以便出来的数据是排序的

						for (Map.Entry<String, Integer> entry : mapOptionCount.entrySet()) {

							String[] keys = entry.getKey().split("@");
							if (!keys[0].equals(aItems[g])) {
								continue;
							}

							String key = keys[1];

							List<Map<String, Object>> listOptionAttr = new ArrayList<Map<String, Object>>();
							if (mapListOptionAttr.containsKey(key)) {
								listOptionAttr = mapListOptionAttr.get(key);
							}

							double choiceNum = entry.getValue();
							String choicePercent = "0";
							int iTotal = mapCount.get(key);
							if (choiceNum != 0) {
								java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
								choicePercent = df.format((choiceNum / iTotal) * 100);
							}

							Map<String, Object> map = new HashMap<String, Object>();
							map.put("item_key", keys[0]);
							map.put("item_value", keys[1]);
							map.put("option_id", keys[2]);
							map.put("option_name", keys[3]);
							map.put("choiceNum", choiceNum); // 数量
							map.put("choicePercent", choicePercent); // 比例

							listOptionAttr.add(map);

							mapListOptionAttr.put(key, listOptionAttr);
						}

						List<Map<String, Object>> listOptionAttrsResult = new ArrayList<Map<String, Object>>();
						for (Map.Entry<String, List<Map<String, Object>>> entry : mapListOptionAttr.entrySet()) {
							Map<String, Object> mapAttrsTmp = new HashMap<String, Object>();
							mapAttrsTmp.put("item_key", entry.getKey());
							mapAttrsTmp.put("item_result", entry.getValue());
							listOptionAttrsResult.add(mapAttrsTmp);
						}

						Map<String, Object> mapAttrsTmp = new HashMap<String, Object>();
						mapAttrsTmp.put("analysis_key", aItems[g]);
						mapAttrsTmp.put("analysis_name", mapAnalysisOption.get(aItems[g]));
						mapAttrsTmp.put("analysis_result", listOptionAttrsResult);
						listOptionAttrs.add(mapAttrsTmp);
					}

				}
				mapTitle.put("optionAttrs", listOptionAttrs);

				listGroupTitles.add(mapTitle);
			}
			mapGroup.put("titles", listGroupTitles);
		}

		return ServerRspUtil.success(listGroup);
	}

	/** 计算统计结果数据 */
	private void calcResult(String titleId, String analysisItems, Map<String, Object> mapOption,
			List<Map<String, Object>> listCurrRecord, Map<String, Integer> mapOptionCount,
			Map<String, List<Map<String, Object>>> mapListRecordAttr) {

		String opRecId = StrUtil.getMapValue(mapOption, "rec_id");
		String opName = StrUtil.getMapValue(mapOption, "option_name");

		// 计算票数和比例
		double choiceNum = 0;
		int joinTotal = 0;
		if (ObjUtil.isNull(listCurrRecord))
			return;
		for (int g = 0; g < listCurrRecord.size(); g++) {
			Map<String, Object> mapOptionRecord = listCurrRecord.get(g);
			String recordAnswer = StrUtil.getMapValue(mapOptionRecord, "answers");
			String recordTitleId = StrUtil.getMapValue(mapOptionRecord, "title_id");
			String recordId = StrUtil.getMapValue(mapOptionRecord, "record_id");

			if (StrUtil.isNull(recordAnswer)) {
				continue;
			}

			if (titleId.equals(recordTitleId)) {
				joinTotal++;
			}

			// 统计总数
			String[] answerArr = recordAnswer.split(",");
			boolean iscontain = Arrays.asList(answerArr).contains(opRecId);
			if (iscontain) {
				choiceNum = choiceNum + 1;

				// 分属性统计
				List<Map<String, Object>> listCurrRecordAtrr = mapListRecordAttr.get(recordId);// 得到当前这个人的分类属性
				if (ObjUtil.isNull(listCurrRecordAtrr) || StrUtil.isNull(analysisItems))
					continue;

				for (int k = 0; k < listCurrRecordAtrr.size(); k++) {
					// 按每个人的属性筛选
					Map<String, Object> mapAtrr = listCurrRecordAtrr.get(k);

					String itemKey = StrUtil.getMapValue(mapAtrr, "item_key");

					// 如果不包含，则跳出
					if (!analysisItems.contains(itemKey)) {
						continue;
					}

					// 选项id, 属性key，属性值
					String attrKey = itemKey + "@" + StrUtil.getMapValue(mapAtrr, "item_value") + "@" + opRecId + "@"
							+ opName;

					int iCount = 1;
					if (mapOptionCount.containsKey(attrKey)) {
						iCount = mapOptionCount.get(attrKey) + 1;
					}
					mapOptionCount.put(attrKey, iCount);
				}
			}

		}

		// 汇总的数量
		mapOption.put("choiceNum", choiceNum);
		String choicePercent = "0";
		if (choiceNum != 0) {
			java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
			choicePercent = df.format((choiceNum / joinTotal) * 100);
		}
		mapOption.put("choicePercent", choicePercent);

	}

	public ResponseBodyVO getQuestionPersonResult(@RequestBody Map<String, Object> param) {

		// 首先得到题目结构
		param.put("has_result", "0");
		ResponseBodyVO resVO = this.getQuestionResult(param);
		List<Map<String, Object>> listGroup = (List<Map<String, Object>>) resVO.getData();

		// 获得指定的客户id和题目的答题数据
		List<Map<String, Object>> listRecord = examRecordDetailMapper.listExamRecordDetail(param);
		if (StrUtil.isNotNull(listRecord)) {
			Map<String, List<Map<String, Object>>> mapListRecord = ObjUtil.transList(listRecord, "title_id");

			for (int i = 0; i < listGroup.size(); i++) {
				Map<String, Object> mapGroup = listGroup.get(i);

				List<Map<String, Object>> listTitles = (List<Map<String, Object>>) mapGroup.get("titles");

				if (ObjUtil.isNotNull(listTitles)) {
					for (int j = 0; j < listTitles.size(); j++) {
						Map<String, Object> mapTitle = listTitles.get(j);
						String titleId = StrUtil.getMapValue(mapTitle, "rec_id");

						List<Map<String, Object>> listOptions = (List<Map<String, Object>>) mapTitle.get("options");

						List<Map<String, Object>> listRecordTmp = mapListRecord.get(titleId);
						Map<String, Object> mapRecord = null;
						if (ObjUtil.isNotNull(listRecordTmp)) {
							mapRecord = listRecordTmp.get(0);
						}

						for (int h = 0; h < listOptions.size(); h++) {
							Map<String, Object> mapOption = listOptions.get(h);

							String opRecId = StrUtil.getMapValue(mapOption, "rec_id");

							String recordAnswer = StrUtil.getMapValue(mapRecord, "answers");
							String[] answerArr = recordAnswer.split(",");
							boolean isContain = Arrays.asList(answerArr).contains(opRecId);
							if (isContain) {
								mapOption.put("is_check", "1");
							} else {
								mapOption.put("is_check", "0");
							}
						}
					}
				}

			}
		}

		return ServerRspUtil.success(listGroup);
	}

	/** 获得分析的选项数据 */
	public ResponseBodyVO getAnalysisOption(@RequestBody Map<String, Object> param) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("question_id", StrUtil.getMapValue(param, "question_id"));
		List<Map<String, Object>> listOption = this.queryNoRec("d_question_analysis", mapParam);

		return ServerRspUtil.success(listOption);
	}

	/** 设置每道题应用的分析项 */
	public ResponseBodyVO setTitleAnalysisOption(@RequestBody Map<String, Object> param) {

		Map<String, Object> mapData = new HashMap<String, Object>();
		String questionObj = StrUtil.getMapValue(param, "question_obj");
		if (StrUtil.isNotNull(questionObj)) {
			String[] questionObjs = questionObj.split(";");
			for (String qobj : questionObjs) {
				String[] datas = qobj.split(",");
				if (datas.length < 2) {
					continue;
				}

				String value = "";
				if (mapData.containsKey(datas[0])) {
					value = mapData.get(datas[0]) + "," + datas[1];
				} else {
					value = datas[1];
				}
				mapData.put(datas[0], value);
			}
		}

		if (ObjUtil.isNotNull(mapData)) {

			for (Entry<String, Object> entry : mapData.entrySet()) {
				// 遍历，更新title表
				Map<String, Object> mapParam = new HashMap<String, Object>();
				mapParam.put("analysis_items", entry.getValue());
				Map<String, Object> mapCondition = new HashMap<String, Object>();
				mapCondition.put("rec_id", entry.getKey());
				this.update(mapCondition, "d_question_title", mapParam);
			}

		}

		return ServerRspUtil.success();
	}

	/** 重新计算问题的选项属性 */
	public ResponseBodyVO calcQuestionOption(@RequestBody Map<String, Object> param) {

		String questionId = StrUtil.getMapValue(param, "question_id");

		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("question_id", questionId);
		List<Map<String, Object>> listAnalysisAttr = this.queryNoRec("d_question_analysis", mapParam);

		List<Map<String, Object>> listTitles = this.queryNoRec("d_question_title", mapParam);
		Map<String, Map<String, Object>> mapTitles = ObjUtil.transListToMap(listTitles, "rec_id");

		mapParam = new HashMap<String, Object>();
		mapParam.put("qbank_id", questionId);
		List<Map<String, Object>> listRecord = this.queryNoRec("d_exam_record", mapParam);

		// 获取Options选项数据
		List<Map<String, Object>> listOptions = questionTitleOptionMapper.listQuestionTitleOption(param);
		Map<String, Map<String, Object>> mapListOptions = ObjUtil.transListToMap(listOptions, "rec_id");

		List<Map<String, Object>> listRecordDetail = examRecordDetailMapper.listExamRecordDetail(param);
		Map<String, List<Map<String, Object>>> mapListRecordDetail = ObjUtil.transList(listRecordDetail, "record_id");

		// 首先删除分析项表的数据
		this.deleteEmpty("d_exam_record_option", mapParam);

		int iBatch = 0;

		List<Map<String, Object>> listBatchParam = new ArrayList<Map<String, Object>>();
		// 遍历所有的调研记录
		for (int i = 0; i < listRecord.size(); i++) {

			Map<String, Object> mapRecord = listRecord.get(i);
			String recordId = StrUtil.getMapValue(mapRecord, "rec_id");

			List<Map<String, Object>> listCurrDetail = mapListRecordDetail.get(recordId);
			if (ObjUtil.isNull(listCurrDetail)) {
				continue;
			}
			Map<String, Map<String, Object>> mapCurrDetail = ObjUtil.transListToMap(listCurrDetail, "title_id");

			// 遍历所有的分析项进行处理
			for (int w = 0; w < listAnalysisAttr.size(); w++) {
				Map<String, Object> map = listAnalysisAttr.get(w);
				String itemKey = StrUtil.getMapValue(map, "item_key");
				String itemName = StrUtil.getMapValue(map, "item_name");
				String itemType = StrUtil.getMapValue(map, "item_type");
				String relaTitleId = StrUtil.getMapValue(map, "rela_title_id");

				// todo.. 按每道题过滤分析项

				Map<String, Object> mapInsert = new HashMap<String, Object>();
				mapInsert.put("qbank_id", questionId);
				mapInsert.put("record_id", recordId);
				mapInsert.put("item_key", itemKey);
				String itemValue = "";

				if ("1".equals(itemType)) {
					// 默认的几种处理方式
					if ("sex".equals(itemKey)) {
						itemValue = this.calcBaseOption(itemKey, StrUtil.getMapValue(mapRecord, "person_sex"));
					} else if ("age".equals(itemKey)) {
						itemValue = this.calcBaseOption(itemKey, StrUtil.getMapValue(mapRecord, "person_birthyear"));
					} else if ("work".equals(itemKey)) {
						itemValue = this.calcBaseOption(itemKey, StrUtil.getMapValue(mapRecord, "person_work"));
					}

				} else if ("2".equals(itemType)) {
					// 找到答案
					// 答案转义，作为value

					String answer = StrUtil.getMapValue(mapCurrDetail.get(relaTitleId), "answers");
					itemValue = StrUtil.getMapValue(mapListOptions.get(answer), "option_name");

				}

				if (StrUtil.isNotNull(itemValue)) {
					mapInsert.put("item_value", itemValue);
					listBatchParam.add(mapInsert);
					iBatch++;
				}
			}

			if (i == listRecord.size() - 1 || iBatch > 90) {
				if (ObjUtil.isNotNull(listBatchParam)) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("list", listBatchParam);
					questionAnalysisMapper.insertRecordOptionBatch(map);

					listBatchParam = new ArrayList<Map<String, Object>>();
					iBatch = 0;
				}
			}
		}

		return ServerRspUtil.success();
	}

	/** 计算基础属性 */
	private String calcBaseOption(String itemKey, String value) {
		String itemValue = "";
		if ("sex".equals(itemKey)) {
			if ("0".equals(value)) {
				itemValue = "男";
			} else if ("1".equals(value)) {
				itemValue = "女";
			}
		} else if ("age".equals(itemKey)) {
			Calendar cal = Calendar.getInstance();
			int yearNow = cal.get(Calendar.YEAR); // 当前年份
			try {
				int yearBirth = Integer.parseInt(value);
				int age = yearNow - yearBirth;

				if (age < 20) {
					itemValue = "小于20岁";
				} else if (age >= 20 && age < 30) {
					itemValue = "20-30岁";
				} else if (age >= 30 && age < 40) {
					itemValue = "30-40岁";
				} else if (age >= 40 && age < 50) {
					itemValue = "40-50岁";
				} else {
					itemValue = "大于50岁";
				}

			} catch (Exception e) {
				// e.printStackTrace();
			}
		} else if ("work".equals(itemKey)) {
			if (WorkTransUtil.mapWorkDict.containsKey(value)) {
				itemValue = StrUtil.getMapValue(WorkTransUtil.mapWorkDict, value);
			} else {
				itemValue = "其他";
			}

		}

		return itemValue;
	}

	/** 初始化map，以便返回前端按正常排序展示 */
	private void initOptionMap(Map<String, List<Map<String, Object>>> mapListOptionAttr, String itemKey,
			Map<String, Map<String, Object>> mapAnalysisAttr, Map<String, List<Map<String, Object>>> mapListOptions) {

		Map<String, Object> map = mapAnalysisAttr.get(itemKey);
		String itemType = StrUtil.getMapValue(map, "item_type");

		if ("1".equals(itemType)) {
			// 默认的几种处理方式
			if ("sex".equals(itemKey)) {
				mapListOptionAttr.put("男", new ArrayList<Map<String, Object>>());
				mapListOptionAttr.put("女", new ArrayList<Map<String, Object>>());
			} else if ("age".equals(itemKey)) {
				mapListOptionAttr.put("小于20岁", new ArrayList<Map<String, Object>>());
				mapListOptionAttr.put("20-30岁", new ArrayList<Map<String, Object>>());
				mapListOptionAttr.put("30-40岁", new ArrayList<Map<String, Object>>());
				mapListOptionAttr.put("40-50岁", new ArrayList<Map<String, Object>>());
				mapListOptionAttr.put("大于50岁", new ArrayList<Map<String, Object>>());
			} else if ("work".equals(itemKey)) {
				mapListOptionAttr.put("企业家", new ArrayList<Map<String, Object>>());
				mapListOptionAttr.put("政府公务员", new ArrayList<Map<String, Object>>());
				mapListOptionAttr.put("老师", new ArrayList<Map<String, Object>>());
				mapListOptionAttr.put("医生", new ArrayList<Map<String, Object>>());
				mapListOptionAttr.put("企业白领", new ArrayList<Map<String, Object>>());
				mapListOptionAttr.put("技术人员/工程师", new ArrayList<Map<String, Object>>());
				mapListOptionAttr.put("高级知识分子", new ArrayList<Map<String, Object>>());
				mapListOptionAttr.put("其他", new ArrayList<Map<String, Object>>());
			}
		} else if ("2".equals(itemType)) {
			// 从选项结果中获取数据
			Map<String, Object> mapAttr = mapAnalysisAttr.get(itemKey);
			String relaTitleId = StrUtil.getMapValue(mapAttr, "rela_title_id");
			List<Map<String, Object>> listCurrOptions = mapListOptions.get(relaTitleId);

			for (int h = 0; h < listCurrOptions.size(); h++) {
				Map<String, Object> mapOption = listCurrOptions.get(h);
				String optionName = StrUtil.getMapValue(mapOption, "option_name");

				mapListOptionAttr.put(optionName, new ArrayList<Map<String, Object>>());
			}
		}

	}

}
