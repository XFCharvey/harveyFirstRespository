package com.cbox.business.project.projecthouses.service;

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
import com.cbox.base.utils.ObjUtil;
import com.cbox.base.utils.StrUtil;
import com.cbox.business.customer.customer.mapper.CustomerMapper;
import com.cbox.business.customer.customerhouses.mapper.CustomerHousesMapper;
import com.cbox.business.project.projecthouses.mapper.ProjectHousesMapper;

/**
 * @ClassName: ProjectHousesService
 * @Function: 项目楼栋房源
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class ProjectHousesService extends BaseService {

	@Autowired
	private ProjectHousesMapper projectHousesMapper;

	@Autowired
	private CustomerMapper customerMapper;

    @Autowired
    private CustomerHousesMapper customerHousesMapper;

	/** listProjectHouses : 获取项目楼栋房源列表数据 **/
	public List<Map<String, Object>> listProjectHouses(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> listFormCacheHouses = projectHousesMapper.listProjectHouses(param);// 缓存房源

		Map<String, Object> mapParam = new HashMap<String, Object>();

		// 关联签约客户 多对多
		for (int i = 0; i < listFormCacheHouses.size(); i++) {
			Map<String, Object> mapProjectHouse = listFormCacheHouses.get(i);

			String houserId = StrUtil.getMapValue(mapProjectHouse, "rec_id");
			mapParam.put("house_id", houserId);

			// 调用mapper,获取客户和房源关联
            List<Map<String, Object>> listCustomerHouse = customerHousesMapper.listCustomerHouses(mapParam);
			mapProjectHouse.put("contract_cus_list", listCustomerHouse);
		}

		return listFormCacheHouses;
	}
	
	/** listHouseOfCustomerId : 获取指定id客户的房源列表 **/
	public List<Map<String, Object>> listHouseOfCustomerId(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = projectHousesMapper.listHouseOfCustomerId(param);

		return list;
	}

	/** getProjectHouses : 获取指定id的项目楼栋房源数据 **/
	public ResponseBodyVO getProjectHouses(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = projectHousesMapper.getProjectHouses(param);
		//获取房源与客户关联
		Map<String, Object> mapParam = new HashMap<String, Object>();
		String houserId = StrUtil.getMapValue(mapParam, "rec_id");
		mapParam.put("house_id", houserId);
        List<Map<String, Object>> listCustomerHouse = customerHousesMapper.listCustomerHouses(mapParam);
		mapResult.put("contract_cus_list", listCustomerHouse);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}
	
	/** getHousesStatusGroup : 获取指定id项目的项目楼栋房源状态分组数量 **/
	public ResponseBodyVO getHousesStatusGroup(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = projectHousesMapper.getHousesStatusGroup(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addProjectHouses : 新增项目楼栋房源 **/
	public ResponseBodyVO addProjectHouses(Map<String, Object> param) {
		int count = 0;
		// Table:d_project_houses
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("project_id", param.get("project_id"));
		mapParam.put("building_id", param.get("building_id"));
		mapParam.put("house_name", param.get("house_name"));
		// 一栋楼栋不能拥有相同的房间名，如1栋不能有两个101
		List<Map<String, Object>> listHouses = projectHousesMapper.listProjectHouses(mapParam);
		if (ObjUtil.isNotNull(listHouses)) {
			return ServerRspUtil.formRspBodyVO(count, "所选楼栋已存在该房间名，请重新设置");
		} else {
			mapParam.put("house_status", param.get("house_status"));
			mapParam.put("house_type", param.get("house_type"));
			mapParam.put("house_area", param.get("house_area"));
			mapParam.put("single_price", param.get("single_price"));
			mapParam.put("total_price", param.get("total_price"));
			mapParam.put("contract_time", param.get("contract_time"));
			mapParam.put("counselor", param.get("counselor"));
			mapParam.put("belong_person", param.get("belong_person"));
			count += this.save("d_project_houses", mapParam);
			// 查询当前所属楼栋的所有房源条件
			Map<String, Object> buildingHousesCondition = new HashMap<String, Object>();
			buildingHousesCondition.put("building_id", param.get("building_id"));
			// 查找
			List<Map<String, Object>> listBuildingHouses = projectHousesMapper.listProjectHouses(mapParam);
			// 楼栋更新参数
			Map<String, Object> buildingParam = new HashMap<String, Object>();
			buildingParam.put("houses_num", listBuildingHouses.size());
			// 楼栋更新条件
			Map<String, Object> buildingCondition = new HashMap<String, Object>();
			buildingCondition.put("rec_id", param.get("building_id"));
			count += this.update(buildingCondition, "d_project_building", buildingParam);

			return ServerRspUtil.formRspBodyVO(count, "新增项目楼栋房源失败");
		}

	}

	/** updateProjectHouses : 修改项目楼栋房源 **/
	public ResponseBodyVO updateProjectHouses(Map<String, Object> param) {
		int count = 0;
		// Table:d_project_houses
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("project_id", param.get("project_id"));
		mapParam.put("building_id", param.get("building_id"));
		mapParam.put("house_name", param.get("house_name"));
		mapParam.put("house_status", param.get("house_status"));
		mapParam.put("house_type", param.get("house_type"));
		mapParam.put("house_area", param.get("house_area"));
		mapParam.put("single_price", param.get("single_price"));
		mapParam.put("total_price", param.get("total_price"));
		mapParam.put("contract_time", param.get("contract_time"));
		mapParam.put("counselor", param.get("counselor"));
		mapParam.put("belong_person", param.get("belong_person"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		// 查询要修改的这条房源原先的数据
		Map<String, Object> mapHouses = projectHousesMapper.getProjectHouses(mapCondition);
		String buildingId = StrUtil.getMapValue(mapHouses, "building_id");
		String houseName = StrUtil.getMapValue(mapHouses, "house_name");
		// 获取前端参数
		String buildingIdParam = StrUtil.getMapValue(param, "building_id");
		String houseNameParam = StrUtil.getMapValue(param, "house_name");
		if (buildingId.equals(buildingIdParam) && houseName.equals(houseNameParam)) {
			count += this.update(mapCondition, "d_project_houses", mapParam);
		} else {
			// 一栋楼栋不能拥有相同的房间名，如1栋不能有两个101
			Map<String, Object> mapHouseCondition = new HashMap<String, Object>();
			mapHouseCondition.put("building_id", param.get("building_id"));
			mapHouseCondition.put("house_name", param.get("house_name"));
			List<Map<String, Object>> listHouses = projectHousesMapper.listProjectHouses(mapHouseCondition);
			if (ObjUtil.isNotNull(listHouses)) {
				return ServerRspUtil.formRspBodyVO(count, "所选楼栋已存在该房间名，请重新设置");
			} else {
				count += this.update(mapCondition, "d_project_houses", mapParam);
			}
		}

		return ServerRspUtil.formRspBodyVO(count, "修改项目楼栋房源失败");
	}

	/** delProjectHouses : 删除项目楼栋房源 **/
	public ResponseBodyVO delProjectHouses(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断

		// Table:d_project_houses
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.delete("d_project_houses", mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除项目楼栋房源失败");
	}

}
