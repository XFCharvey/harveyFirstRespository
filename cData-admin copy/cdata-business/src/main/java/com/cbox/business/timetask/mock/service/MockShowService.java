package com.cbox.business.timetask.mock.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.beust.jcommander.internal.Maps;
import com.cbox.base.core.service.BaseService;
import com.cbox.base.utils.DateUtils;
import com.cbox.base.utils.ObjUtil;
import com.cbox.base.utils.SecurityUtils;
import com.cbox.base.utils.StrUtil;
import com.cbox.business.timetask.mock.util.MockUtil;
import com.cbox.business.project.project.mapper.ProjectMapper;

/**
 * @ClassName: MockShowService
 * @Function: 抓取明源的管理大屏的数据
 * 
 * @author qiuzq
 * @date 2021年10月24日 下午4:40:18
 * @version 1.0
 */
@Service
public class MockShowService extends BaseService {

	// @Autowired
	// private ProjectHousesMapper projectHousesMapper;

	@Autowired
	private ProjectMapper projectMapper;

	List<Map<String, Object>> listProject = null;

	public static void main(String[] args) {
		MockShowService showService = new MockShowService();
		showService.getShow();
	}

	/** 获得展示的数据 */
	public void getShow() {

		HttpClient httpClient = MockUtil.mockLogin();
		// HttpClient httpClient = new HttpClient();

		String dataUrl = "";
		String resContent = "";
		List<String[]> listData;
		Map<String, String> params;

		try {

			// Step1：得到sessionID
			dataUrl = "http://my.sunac.com.cn:10080/WebReport/ReportServer?formlet=rc.frm";
			String getContent = MockUtil.visitUrlGet(dataUrl, httpClient);
			int iFirstIndex = getContent.indexOf("sessionID=");
			String nextContent = getContent.substring(iFirstIndex);
			String sessionID = nextContent.substring(10, nextContent.indexOf("\""));
			System.out.println("sessionID:" + sessionID);

			// Step2：提取。by-本月,bj-本季,bn-本年

			// 初始化项目
			listProject = projectMapper.listProject(new HashMap<String, Object>());

			// 提取本月
			String dateType = "by";
			this.execCall(sessionID, dateType, httpClient);

			// 提取本季
//			dateType = "bj";
//			this.execCall(sessionID, dateType, httpClient);

			// 提取本年
			dateType = "bn";
			this.execCall(sessionID, dateType, httpClient);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void execCall(String sessionID, String dateType, HttpClient httpClient) {
		String dataUrl = "";
		String resContent = "";
		List<String[]> listData;
		Map<String, String> params;

		System.out.println("*******************" + dateType + "*************************");

		try {
			// 设置参数
			params = Maps.newHashMap();
			params.put("sessionID", sessionID);
			params.put("sj", dateType);// by-本月,bj-本季,bn-本年
			params.put("t", "2");
			params.put("qy", "zb.0001.000121.000121_08");
			dataUrl = "http://my.sunac.com.cn:10080/WebReport/ReportServer?op=fr_dialog&cmd=parameters_d";
			MockUtil.visitUrlPost(dataUrl, params, httpClient);

//			// Step2：提取报表数据
//			int iLen = 54;
//			for (int i = 1; i < iLen; i++) {
//				String reportType = "REPORT" + i;
//				listData = this.callReport(httpClient, reportType, "", sessionID);
//			}

			// 提取不同项目ts/wx
			List<Integer> listProjects = new ArrayList<Integer>();
			listProjects.add(1);
			listProjects.add(21);
			listProjects.add(37);
			for (int i = 0; i < listProjects.size(); i++) {
				String reportType = "REPORT" + listProjects.get(i);
				listData = this.callReport(httpClient, reportType, "", sessionID);
				addProjectcomplain(dateType, reportType, listData);
			}

			Map<String, Object> mapWx = new HashMap<String, Object>();
			Map<String, Object> mapTs = new HashMap<String, Object>();
			Map<String, Object> mapJf = new HashMap<String, Object>();
			boolean wxFlag = true;
			String wxId = null;
			String tsId = null;
			String jfId = null;

			// 提取维修总数
			List<Integer> listWxTotals = new ArrayList<Integer>();
			listWxTotals.add(3);
			listWxTotals.add(8);
			listWxTotals.add(9);
			listWxTotals.add(10);
			listWxTotals.add(11);
			listWxTotals.add(12);
			listWxTotals.add(13);
			listWxTotals.add(14);
			for (int i = 0; i < listWxTotals.size(); i++) {
				String reportType = "REPORT" + listWxTotals.get(i);
				listData = this.callReport(httpClient, reportType, "", sessionID);
				addTotals(i, dateType, mapWx, listData, wxFlag);
				wxId = StrUtil.getMapValue(mapWx, "rec_id");
				System.out.println("wxId" + wxId);
			}

			// 提取投诉总数
			List<Integer> listTsTotals = new ArrayList<Integer>();
			listTsTotals.add(22);
			listTsTotals.add(23);
			listTsTotals.add(24);
			listTsTotals.add(25);
			listTsTotals.add(26);
			listTsTotals.add(27);
			listTsTotals.add(28);
			listTsTotals.add(29);
			for (int i = 0; i < listTsTotals.size(); i++) {
				String reportType = "REPORT" + listTsTotals.get(i);
				listData = this.callReport(httpClient, reportType, "", sessionID);
				wxFlag = false;
				addTotals(i, dateType, mapTs, listData, wxFlag);
				tsId = StrUtil.getMapValue(mapTs, "rec_id");
				System.out.println("tsId" + tsId);
			}

			// 提取交付总数
			List<Integer> listJfTotals = new ArrayList<Integer>();
			listJfTotals.add(40);// 毛坯
			listJfTotals.add(41);// 精装
			listJfTotals.add(42);// 预计交付量
			listJfTotals.add(43);// 到访交付量
			listJfTotals.add(44);// 整改完成
			listJfTotals.add(45);// 到访交付率
			listJfTotals.add(46);// 按时完成率
			listJfTotals.add(47);// 回访满意度
			for (int i = 0; i < listJfTotals.size(); i++) {
				String reportType = "REPORT" + listJfTotals.get(i);
				listData = this.callReport(httpClient, reportType, "", sessionID);
				addJfTotals(i, dateType, mapJf, listData);
				jfId = StrUtil.getMapValue(mapJf, "rec_id");
				System.out.println("jfId" + jfId);
			}

			// 提取问题及排行榜
			List<Integer> listRanking = new ArrayList<Integer>();
			listRanking.add(2);
			listRanking.add(7);
			listRanking.add(6);
			listRanking.add(20);
			listRanking.add(31);
			listRanking.add(48);
			listRanking.add(49);
			for (int i = 0; i < listRanking.size(); i++) {
				String reportType = "REPORT" + listRanking.get(i);
				listData = this.callReport(httpClient, reportType, "", sessionID);
				String questionType = null;
				String complainId = null;
				switch (i) {
				case 0:
					questionType = "1";
					complainId = wxId;
					break;
				case 1:
					questionType = "2";
					complainId = wxId;
					break;
				case 2:
					questionType = "3";
					complainId = wxId;
					break;
				case 3:
					questionType = "6";
					complainId = tsId;
					break;
				case 4:
					questionType = "7";
					complainId = tsId;
					break;
				case 5:
					questionType = "8";
					complainId = jfId;
					break;
				case 6:
					questionType = "10";
					complainId = jfId;
					break;
				}
				addProjectcquestion(complainId, questionType, listData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 抓取指定报表的数据 */
	private List<String[]> callReport(HttpClient httpClient, String widgetName, String id, String sessionID) {
		String dataUrl = "";
		String resContent = "";
		List<String[]> listData = null;
		Map<String, String> params;

		try {
			params = Maps.newHashMap();
			params.put("widgetName", widgetName);
			params.put("_", id);
			params.put("sessionID", sessionID);
			dataUrl = "http://my.sunac.com.cn:10080/WebReport/ReportServer?op=fr_form&cmd=load_report_content&__parameters__=&noCache=&pageIndex=1&__boxModel__=true&reload=null&_PAPERWIDTH=1538&_PAPERHEIGHT=724&_SHOWPARA=true";
			resContent = MockUtil.visitUrlPost(dataUrl, params, httpClient);
			resContent = String.valueOf(JSON.parseObject(resContent).get("htmlTag"));
			listData = MockUtil.getTableData(resContent);

			System.out.println(widgetName);
			MockUtil.printList(listData);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return listData;

	}

	/** 根据业务规则进行入库操作。入库：d_project_complain，d_project_complain_question */
//	public void dealResource(String dateType, String reportType, List<String[]> listData) {
//
//		try {
//			// 入库：d_project_complain
//			addProjectcomplain(dateType, reportType, listData);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public void addTotals(int index, String dateType, Map<String, Object> param, List<String[]> listData,
			boolean wxFlag) {

		String data = listData.get(2)[0];
		if (index == 0) {
			param.put("close_rate", formatValue(data));
		}
		if (index == 1) {
			param.put("satisfy_rate", formatValue(data));
		}
		if (index == 2) {
			param.put("replay_out_rate", formatValue(data));
		}
		if (index == 3) {
			param.put("deal_out_rate", formatValue(data));
		}
		if (index == 4) {
			param.put("close_num", data);
		}
		if (index == 5) {
			param.put("total_num", data);
		}
		if (index == 6) {
			param.put("reply_out_num", data);
		}
		if (index == 7) {
			param.put("deal_out_num", data);
			param.put("project_id", 0);
			String timeType = null;
			if (dateType.equals("by")) {
				timeType = "1";
			} else if (dateType.equals("bj")) {
				timeType = "2";
			} else if (dateType.equals("bn")) {
				timeType = "3";
			}
			param.put("time_type", timeType);
			String complainTime = formatComplainTime(dateType);
			param.put("complain_time", complainTime);
			if (wxFlag) {
				// 維修添加
				String complainType = "2";
				param.put("complain_type", complainType);
				// 入库前，先删除已有的数据
				deleteComplain("0", timeType, complainTime, complainType);
				this.saveNoRec("d_project_complain", param, true);
			} else {
				// 投訴添加
				String complainType = "1";
				param.put("complain_type", complainType);
				// 入库前，先删除已有的数据
				deleteComplain("0", timeType, complainTime, complainType);
				this.saveNoRec("d_project_complain", param, true);
			}
		}
	}

	public void addJfTotals(int index, String dateType, Map<String, Object> param, List<String[]> listData) {

		String data = listData.get(2)[0];
		if (index == 0) {
			param.put("r_defect_num", formatValue(data));
		}
		if (index == 1) {
			param.put("h_defect_num", formatValue(data));
		}
		if (index == 2) {
			param.put("pre_deliver_num", formatValue(data));
		}
		if (index == 3) {
			param.put("actual_deliver_num", formatValue(data));
		}
		if (index == 4) {
			param.put("update_rate", formatValue(data));
		}
		if (index == 5) {
			param.put("deliver_rate", formatValue(data));
		}
		if (index == 6) {
			param.put("finish_rate", formatValue(data));
		}
		if (index == 7) {
			param.put("satisfy_rate", formatValue(data));
			param.put("project_id", 0);
			String timeType = null;
			if (dateType.equals("by")) {
				timeType = "1";
			} else if (dateType.equals("bj")) {
				timeType = "2";
			} else if (dateType.equals("bn")) {
				timeType = "3";
			}
			param.put("time_type", timeType);
			String complainTime = formatComplainTime(dateType);
			param.put("complain_time", complainTime);
			// 交付新增
			String complainType = "3";
			param.put("complain_type", complainType);
			// 入库前，先删除已有的数据
			deleteComplain("0", timeType, complainTime, complainType);
			this.saveNoRec("d_project_complain", param, true);
		}
	}

	/** 入库d_project_complain，本月、本季度、本年 */
	public void addProjectcomplain(String dateType, String reportType, List<String[]> listData) {
		String timeType = null;
		if (dateType.equals("by")) {
			timeType = "1";
		} else if (dateType.equals("bj")) {
			timeType = "2";
		} else if (dateType.equals("bn")) {
			timeType = "3";
		}

		String complainTime = formatComplainTime(dateType);

		// 1-投诉，2-保修，3-交付
		String complainType = null;
		if ("REPORT1".equals(reportType)) {
			complainType = "1";
		} else if ("REPORT21".equals(reportType)) {
			complainType = "2";
		} else if ("REPORT37".equals(reportType)) {
			complainType = "3";
		}
		// 入库前，先删除已有的数据
		deleteComplain("", timeType, complainTime, complainType);

		// 入库操作
		for (int i = 0; i < listData.size(); i++) {
			// 第二条开始为数据
			int index = i + 1;
			if (index == listData.size()) {
				break;// 防止空指针异常
			}
			String[] data = listData.get(index);
			String projectName = StrUtil.getNotNullStrValue(data[1], "x");

			// 项目名称不为空时，存数据
			if (projectName.equals("x")) {
				break;
			} else {
				if (complainType.equals("3")) {
					addJfComplain(complainType, timeType, dateType, complainTime, data);
				} else {
					addProjectcomplain(complainType, timeType, dateType, complainTime, data);
				}
			}
		}
	}

	public void addProjectcquestion(String complainId, String questionType, List<String[]> listData) {
		// 入库操作
		for (int i = 0; i < listData.size(); i++) {
			// 第三条开始为数据
			int index = i + 2;
			if (index == listData.size()) {
				break;
			}
			String[] data = listData.get(index);
			String str = StrUtil.getNotNullStrValue(data[1], "x");

			// 不为空时，存数据
			if (str.equals("x")) {
				break;
			} else {
				addProjectcquestion(complainId, questionType, data);
			}
		}
	}

	public void addProjectcquestion(String complainId, String questionType, String[] data) {

		Map<String, Object> mapQues = new HashMap<String, Object>();
		mapQues.put("complain_id", complainId);
		mapQues.put("question_type", questionType);
		if (questionType.equals("1") || questionType.equals("6") || questionType.equals("8")) {
			mapQues.put("question_name", formatValue(data[1]));
			mapQues.put("num", formatValue(data[2]));
			mapQues.put("rate", formatValue(data[3]));
		}
		if (questionType.equals("2") || questionType.equals("7")) {
			mapQues.put("question_name", formatValue(data[1]));
			mapQues.put("num", formatValue(data[3]));
			mapQues.put("rate", formatValue(data[4]));
		}
		if (questionType.equals("3") || questionType.equals("10")) {
			mapQues.put("question_name", formatValue(data[2]));
			mapQues.put("num", formatValue(data[3]));
			mapQues.put("rate", formatValue(data[5]));
		}
		this.saveNoRec("d_project_complain_question", mapQues);
	}

	/** 入库【投诉/维修】类型d_project_complain，本月、本季度、本年 */
	public void addProjectcomplain(String complainType, String timeType, String dateType, String complainTime,
			String[] Data) {

		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("complain_type", complainType);
		mapParam.put("time_type", timeType);
		mapParam.put("complain_time", complainTime);
		mapParam.put("project_id", getProjectId(Data[1]));
		mapParam.put("close_rate", formatValue(Data[2]));
		mapParam.put("satisfy_rate", formatValue(Data[3]));
		mapParam.put("total_num", Data[6]);
		mapParam.put("close_num", Data[7]);
		mapParam.put("reply_out_num", Data[8]);
		mapParam.put("replay_out_rate", formatValue(Data[9]));
		mapParam.put("deal_out_num", Data[10]);
		mapParam.put("deal_out_rate", formatValue(Data[11]));
		this.saveNoRec("d_project_complain", mapParam);
	}

	/** 入库【交付】类型d_project_complain，本月、本季度、本年 */
	public void addJfComplain(String complainType, String timeType, String dateType, String complainTime,
			String[] Data) {

		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("complain_type", complainType);
		mapParam.put("time_type", timeType);
		mapParam.put("complain_time", complainTime);
		mapParam.put("project_id", getProjectId(Data[1]));// 关联项目id
		mapParam.put("r_defect_num", formatValue(Data[2]));// 毛坯
		mapParam.put("h_defect_num", formatValue(Data[3]));// 精装
		mapParam.put("deliver_rate", formatValue(Data[6]));// 交付率
		mapParam.put("update_rate", formatValue(Data[7]));// 完成率
		mapParam.put("pre_deliver_num", formatValue(Data[8]));// 预计交付量
		mapParam.put("actual_deliver_num", formatValue(Data[9]));// 到访交付量
		mapParam.put("close_rate", formatValue(Data[10]));// 按时关闭率
		mapParam.put("satisfy_rate", formatValue(Data[11]));// 回复满意度
		this.saveNoRec("d_project_complain", mapParam);
	}

	// 删除已有数据
	public void deleteComplain(String projectId, String timeType, String complainTime, String complainType) {
		// 删除操作
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		if (StrUtil.isNotNull(projectId)) {
			mapCondition.put("project_id", projectId); // 锁定福建公司
		}
		mapCondition.put("time_type", timeType); // 锁定 时间类型
		mapCondition.put("complain_time", complainTime); // 锁定 时间
		mapCondition.put("complain_type", complainType); // 锁定 维修or投诉类型
		this.deleteEmpty("d_project_complain", mapCondition);
	}

	/** 获取项目id */
	public String getProjectId(String proName) {
		String projectId = null;
		if (ObjUtil.isNotNull(listProject)) {
			for (int i = 0; i < listProject.size(); i++) {
				Map<String, Object> mapPro = listProject.get(i);
				String name = StrUtil.getMapValue(mapPro, "project_name");
				if (name.equals(proName)) {
					projectId = StrUtil.getMapValue(mapPro, "rec_id");
					break;
				}
			}
		}
		return projectId;
	}

	// 去除百分号，空字符等
	public String formatValue(String value) {
		String formatValue = "0";
		if (StrUtil.isNotNull(value)) {
			if (value.contains("%")) {
				int indexPer = value.indexOf("%");
				formatValue = value.substring(0, indexPer);
				return formatValue;
			} else if (value.equals("--")) {
				return formatValue;
			} else if (value.equals("x")) {
				return formatValue;
			} else if (value.equals("&gt;&gt;")) {
				return formatValue;
			}
		}
		return value;
	}

	public String formatComplainTime(String dateType) {
		// 获取前一天的月份
		Calendar calender = Calendar.getInstance();
		calender.add(Calendar.DAY_OF_MONTH, -1);
		Date date = calender.getTime();
		String month = DateUtils.format(date, "YYYY-MM");

		// 获取当前季度的第一个月
		int currentMonth = calender.get(Calendar.MONTH) + 1;
		Date now = null;
		try {
			if (currentMonth >= 1 && currentMonth <= 3)
				calender.set(Calendar.MONTH, 0);
			else if (currentMonth >= 4 && currentMonth <= 6)
				calender.set(Calendar.MONTH, 3);
			else if (currentMonth >= 7 && currentMonth <= 9)
				calender.set(Calendar.MONTH, 6);
			else if (currentMonth >= 10 && currentMonth <= 12)
				calender.set(Calendar.MONTH, 9);
			calender.set(Calendar.DATE, 1);
			now = calender.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String quaterMonth = DateUtils.format(now, "YYYY-MM");

		// 当前年
		String currentYear = DateUtils.format(now, "YYYY");

		if (dateType.equals("by")) {
			return month;
		} else if (dateType.equals("bj")) {
			return quaterMonth;
		} else {
			return currentYear;
		}
	}

}
