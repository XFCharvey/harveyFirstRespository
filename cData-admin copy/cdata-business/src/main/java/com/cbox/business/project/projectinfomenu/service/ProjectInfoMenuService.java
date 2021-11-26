package com.cbox.business.project.projectinfomenu.service;

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
import com.cbox.base.utils.StrUtil;
import com.cbox.business.project.projectinfomenu.mapper.ProjectInfoMenuMapper;
import com.cbox.common.util.BaseTreeUtil;


/**
 * @ClassName: ProjectInfoMenuService
 * @Function: 项目资料分类
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class ProjectInfoMenuService extends BaseService {

    @Autowired
    private ProjectInfoMenuMapper projectInfoMenuMapper;
    
	/** listProjectInfoMenu : 获取项目资料分类列表数据 **/
	public List<Map<String, Object>> listProjectInfoMenu(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = projectInfoMenuMapper.listProjectInfoMenu(param);

		return list;
	}

    /** listProjectInfoMenutree : 获取项目资料分类目录列表返回树形数据 **/
    public ResponseBodyVO listProjectInfoMenutree(Map<String, Object> param) {
        // TODO Auto-generated method stub

        super.appendUserInfo(param);

        // 正常获取数据
        List<Map<String, Object>> list = projectInfoMenuMapper.listProjectInfoMenu(param);

        // 转换成前端所需的树形格式
        String id = "rec_id";
        String pId = "parent_id";
        String value = "rec_id";
        String name = "menu_name";
        boolean checked = false;
        List<Map<String, Object>> listReturn = BaseTreeUtil.toNodesForVueTree(list, id, pId, value, name, checked);

        return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, listReturn);
    }

	/** getProjectInfoMenu : 获取指定id的项目资料分类数据 **/
	public ResponseBodyVO getProjectInfoMenu(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = projectInfoMenuMapper.getProjectInfoMenu(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addProjectInfoMenu : 新增项目资料分类 **/
	public ResponseBodyVO addProjectInfoMenu(Map<String, Object> param) {

		// Table:d_project_info_menu
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("parent_id", param.get("parent_id"));
		mapParam.put("ancestors", param.get("ancestors"));
        mapParam.put("menu_name", param.get("menu_name"));
        mapParam.put("menu_detail", param.get("menu_detail"));
		int count = this.save( "d_project_info_menu", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "新增项目资料分类失败");
	}

	/** updateProjectInfoMenu : 修改项目资料分类 **/
    @SuppressWarnings("unchecked")
    public ResponseBodyVO updateProjectInfoMenu(Map<String, Object> param) {

		// Table:d_project_info_menu
		Map<String, Object> mapParam = new HashMap<String, Object>();

        /* step1  判断前段是否有选择修改父目录   */
        if (param.get("parentmenu") != null) {
            // 库表操作
            Map<String, Object> mapDB = (Map<String, Object>) param.get("parentmenu");
            // 拼接祖级列表
            String pid = StrUtil.getMapValue(mapDB, "rec_id");
            String ance = StrUtil.getMapValue(param, "ancestors");
            // 当前要修改的目录的祖级列表及当前目录的id，为了修改当前目录的子级目录的祖级列表
            String ance1 = StrUtil.getMapValue(param, "ancestors") + "," + StrUtil.getMapValue(param, "rec_id");
            // 拼接当前目录要修改为的父目录的祖级列表+rec_id，即为当前修改的目录的最新祖级列表，并且去替换掉当前目录下的子目录原先的祖级列表
            String newanc = StrUtil.getMapValue(mapDB, "ancestors") + "," + StrUtil.getMapValue(mapDB, "rec_id");

            mapParam.put("parent_id", pid);
            mapParam.put("ancestors", newanc);
            // 修改当前目录的子目录的祖级列表，传递条件
            int res = projectInfoMenuMapper.updateInfoMenu(newanc, ance, ance1);

        } else {
            mapParam.put("parent_id", param.get("parent_id"));
            mapParam.put("ancestors", param.get("ancestors"));
        }

		mapParam.put("menu_name", param.get("menu_name"));
        mapParam.put("menu_detail", param.get("menu_detail"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.update(mapCondition, "d_project_info_menu", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改项目资料分类失败"); 
	}

	/** delProjectInfoMenu : 删除项目资料分类 **/
	public ResponseBodyVO delProjectInfoMenu(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断 

		// Table:d_project_info_menu
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.delete("d_project_info_menu",  mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除项目资料分类失败");
	}



}
