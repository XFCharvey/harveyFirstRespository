package com.cbox.business.project.projectcontractor.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbox.base.constant.HttpStatus;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.service.BaseService;
import com.cbox.base.utils.ObjUtil;
import com.cbox.base.utils.StrUtil;
import com.cbox.base.utils.poi.ExcelUtil;
import com.cbox.business.customer.customer.bean.CustomerImport;
import com.cbox.business.project.projectcontractor.bean.ContractorVO;
import com.cbox.business.project.projectcontractor.mapper.ProjectContractorMapper;
import com.cbox.common.service.AttachFileService;
import com.cbox.business.project.project.mapper.ProjectMapper;

/**
 * @ClassName: ProjectContractorService
 * @Function: 项目承建商
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class ProjectContractorService extends BaseService {

	@Autowired
	private ProjectContractorMapper projectContractorMapper;

	@Autowired
	private AttachFileService attachFileService;

	@Autowired
	private ProjectMapper projectMapper;

	/** listProjectContractor : 获取项目承建商列表数据 **/
	public List<Map<String, Object>> listProjectContractor(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = projectContractorMapper.listProjectContractor(param);

		return list;
	}

	/** getProjectContractor : 获取指定id的项目承建商数据 **/
	public ResponseBodyVO getProjectContractor(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = projectContractorMapper.getProjectContractor(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/**
	 * addProjectContractorBatch : 批量新增项目承建商
	 * 
	 * @throws Exception
	 **/
	public ResponseBodyVO addProjectContractorBatch(Map<String, Object> param) throws Exception {

		// Table:d_project_contractor
		int count = 0;
		// 获取上传文件地址
		String fileId = StrUtil.getMapValue(param, "import_file");
		String fileName = attachFileService.getFileRealPath(fileId);
		File file = new File(fileName);

		// 解析excel文件
		ExcelUtil<ContractorVO> util = new ExcelUtil<ContractorVO>(ContractorVO.class);
		int[] headRowIdx = { 2, 3 };
		int dataRowIdx = 4;
		List<ContractorVO> contractorList = util.importExcel(file, headRowIdx, dataRowIdx);

		// 入库操作(单个sheet)
		Map<String, Object> mapParam = null;
		String projectId = StrUtil.getMapValue(param, "project_id");
		if (ObjUtil.isNotNull(contractorList)) {
			for (int i = 0; i < contractorList.size(); i++) {
				mapParam = new HashMap<String, Object>();
				ContractorVO contractorVO = contractorList.get(i);
				mapParam.put("project_id", projectId);
				mapParam.put("contractor_name", contractorVO.getContractor_name());
				mapParam.put("contractor_position", contractorVO.getContractor_position());
				count = count + this.save("d_project_contractor", mapParam);
			}
		}
		// 更新项目承建商数
		Map<String, Object> updateParam = new HashMap<String, Object>();
		updateParam.put("project_id", projectId);
		updateConTotal(updateParam);

		String msg = "成功导入【" + count + "】条承建商信息";
		return ServerRspUtil.success(msg, null);

		/** 多个sheet **/
//		InputStream inputStream = new FileInputStream(file);
//		Workbook wb = new XSSFWorkbook(inputStream);
//		int sheetTotal = wb.getNumberOfSheets();// 获取sheet的个数
//		List<String> projectNameList = new ArrayList<String>();
//
//		// 获取不同sheet的承建商数据
//		List<List<ContractorVO>> allContractorList = new ArrayList<List<ContractorVO>>();
//		for (int i = 0; i < sheetTotal; i++) {
//			Sheet sheet = wb.getSheetAt(i);
//			String sheetName = sheet.getSheetName();
//			projectNameList.add(sheetName);
//			// 表头和数据所在行索引
//			int[] headRowIdx = { 2, 3 };
//			int dataRowIdx = 4;
//			InputStream inputStream2 = new FileInputStream(file);
//			List<ContractorVO> contractorList = util.importExcel(sheetName, inputStream2, headRowIdx, dataRowIdx);
//			allContractorList.add(contractorList);
//		}
//		inputStream.close();
//
//		// 入库操作
//		Map<String, Object> mapParam = new HashMap<String, Object>();
//		List<Map<String, Object>> projectList = projectMapper.listProject(mapParam);// mapParam空条件
//		for (int i = 0; i < allContractorList.size(); i++) {
//			List<ContractorVO> contractorInsertList = allContractorList.get(i);
//
//			int projectId = getIdByProjectName(projectNameList.get(i), projectList);
//
//			for (int j = 0; j < contractorInsertList.size(); j++) {
//				ContractorVO contractorVO = contractorInsertList.get(j);
//
//				String contractorName = contractorVO.getContractor_name();
//				if (StrUtil.isNotNull(contractorName)) {
//					mapParam.put("project_id", projectId);
//					mapParam.put("contractor_name", contractorVO.getContractor_name());
//					mapParam.put("contractor_position", contractorVO.getContractor_position());
//					count = count + this.save("d_project_contractor", mapParam);
//				}
//			}
//
//			if (projectId != 0) {
//				// 更新项目承建商数
//				Map<String, Object> updateParam = new HashMap<String, Object>();
//				updateParam.put("project_id", projectId);
//				updateConTotal(updateParam);
//			}
//		}

	}

	// 通过项目名称匹配项目id
	public int getIdByProjectName(String projectName, List<Map<String, Object>> projectList) {
		int projectId = 0;
		for (int i = 0; i < projectList.size(); i++) {
			Map<String, Object> projectParam = projectList.get(i);
			String Name = StrUtil.getMapValue(projectParam, "project_name");
			if (Name.contains(projectName)) {
				projectId = StrUtil.getMapIntValue(projectParam, "rec_id");
				break;
			}
		}
		return projectId;
	}

	/** addProjectContractor : 新增项目承建商 **/
	public ResponseBodyVO addProjectContractor(Map<String, Object> param) {

		// Table:d_project_contractor
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("contractor_name", param.get("contractor_name"));
		String projectId = StrUtil.getMapValue(param, "project_id");
		mapParam.put("project_id", projectId);
		mapParam.put("contractor_position", param.get("contractor_position"));
		mapParam.put("contractor_person", param.get("contractor_person"));
		mapParam.put("contractor_phone", param.get("contractor_phone"));
		mapParam.put("contractor_level", param.get("contractor_level"));
		mapParam.put("contractor_comment", param.get("contractor_comment"));
		int count = this.save("d_project_contractor", mapParam);

		if (StrUtil.isNotNull(projectId)) {
			updateConTotal(mapParam);
		}

		return ServerRspUtil.formRspBodyVO(count, "新增项目承建商失败");
	}

	public int updateConTotal(Map<String, Object> param) {
		Map<String, Object> projectContractorCondition = new HashMap<String, Object>();
		projectContractorCondition.put("project_id", param.get("project_id"));
		List<Map<String, Object>> listProjectContractor = projectContractorMapper
				.listProjectContractor(projectContractorCondition);
		// 最新的项目承建商数
		Map<String, Object> projectParam = new HashMap<String, Object>();
		projectParam.put("contractor_num", listProjectContractor.size());
		// 更新项目最新的承建商数
		Map<String, Object> projectCondition = new HashMap<String, Object>();
		projectCondition.put("rec_id", param.get("project_id"));
		int count = this.update(projectCondition, "d_project", projectParam);

		return count;
	}

	/** updateProjectContractor : 修改项目承建商 **/
	public ResponseBodyVO updateProjectContractor(Map<String, Object> param) {

		// Table:d_project_contractor
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("contractor_name", param.get("contractor_name"));
		mapParam.put("project_id", param.get("project_id"));
		mapParam.put("contractor_position", param.get("contractor_position"));
		mapParam.put("contractor_person", param.get("contractor_person"));
		mapParam.put("contractor_phone", param.get("contractor_phone"));
		mapParam.put("contractor_level", param.get("contractor_level"));
		mapParam.put("contractor_comment", param.get("contractor_comment"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.update(mapCondition, "d_project_contractor", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改项目承建商失败");
	}

	/** delProjectContractor : 删除项目承建商 **/
	public ResponseBodyVO delProjectContractor(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断

		// Table:d_project_contractor
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		Map<String, Object> mapContractor = projectContractorMapper.getProjectContractor(param);
		int count = this.delete("d_project_contractor", mapCondition);

		Map<String, Object> projectContractorCondition = new HashMap<String, Object>();
		projectContractorCondition.put("project_id", mapContractor.get("project_id"));
		List<Map<String, Object>> listProjectContractor = projectContractorMapper
				.listProjectContractor(projectContractorCondition);
		Map<String, Object> projectParam = new HashMap<String, Object>();
		if (ObjUtil.isNotNull(listProjectContractor)) {
			// 最新的项目承建商数
			projectParam.put("contractor_num", listProjectContractor.size());
		} else {
			projectParam.put("contractor_num", 0);
		}
		// 更新项目最新的承建商数
		Map<String, Object> projectCondition = new HashMap<String, Object>();
		projectCondition.put("rec_id", mapContractor.get("project_id"));
		count += this.update(projectCondition, "d_project", projectParam);

		return ServerRspUtil.formRspBodyVO(count, "删除项目承建商失败");
	}

}
