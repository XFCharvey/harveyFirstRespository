package com.cbox.business.info.infomenu.service;

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
import com.cbox.business.info.infomenu.mapper.InfoMenuMapper;
import com.cbox.common.util.BaseTreeUtil;


/**
 * @ClassName: InfoMenuService
 * @Function: 资讯分类目录
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class InfoMenuService extends BaseService {

    @Autowired
    private InfoMenuMapper infoMenuMapper;
    

	/** listInfoMenu : 获取资讯分类目录列表数据 **/
	public List<Map<String, Object>> listInfoMenu(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = infoMenuMapper.listInfoMenu(param);

		return list;
	}

    public List<Map<String, Object>> listInfoMenuSort(Map<String, Object> param) {
        // TODO Auto-generated method stub
        super.appendUserInfo(param);

        List<Map<String, Object>> list = infoMenuMapper.listInfoMenuSort(param);

        return list;
    }

    public List<Map<String, Object>> listInfoMenuAndSmalllevel(Map<String, Object> param) {
        // TODO Auto-generated method stub
        super.appendUserInfo(param);

        List<Map<String, Object>> list = infoMenuMapper.listInfoMenuAndSmalllevel(param);

        return list;
    }

    public ResponseBodyVO listInfoMenutree(Map<String, Object> param) {
        // TODO Auto-generated method stub
        super.appendUserInfo(param);

        // 正常获取数据
        List<Map<String, Object>> list = infoMenuMapper.listInfoMenu(param);

        // 转换成前端所需的树形格式
        String id = "rec_id";
        String pId = "parent_id";
        String value = "rec_id";
        String name = "menu_name";
        boolean checked = false;
        List<Map<String, Object>> listReturn = BaseTreeUtil.toNodesForVueTree(list, id, pId, value, name, checked);

        return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, listReturn);
    }

	/** getInfoMenu : 获取指定id的资讯分类目录数据 **/
	public ResponseBodyVO getInfoMenu(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = infoMenuMapper.getInfoMenu(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addInfoMenu : 新增资讯分类目录 **/
	public ResponseBodyVO addInfoMenu(Map<String, Object> param) {

		// Table:s_info_menu
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("menu_name", param.get("menu_name"));
		List<Map<String, Object>> list = infoMenuMapper.listInfoMenu(mapParam);
		if(list.size()>0) {
			return ServerRspUtil.formRspBodyVO(-1, "添加失败，存在相同的资讯分类");
		}
		mapParam.put("parent_id", param.get("parent_id"));
		mapParam.put("ancestors", param.get("ancestors"));
        mapParam.put("rec_lname", param.get("rec_lname"));
        mapParam.put("menu_img", param.get("menu_img"));
		mapParam.put("menu_desc", param.get("menu_desc"));
		int count = this.save( "s_info_menu", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "新增资讯分类目录失败");
	}

	/** updateInfoMenu : 修改资讯分类目录 **/
	public ResponseBodyVO updateInfoMenu(Map<String, Object> param) {
        // Table:s_info_menu
        Map<String, Object> mapParam = new HashMap<String, Object>();
        /* step1  判断前段是否有选择修改父目录   */
        if (param.get("parentmenu") != null) {
            // 库表操作
            Map mapDB = (Map) param.get("parentmenu");
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
            int res = infoMenuMapper.updateInfoMenu(newanc, ance, ance1);

            String lname = StrUtil.getMapValue(param, "rec_lname");

            String slname = StrUtil.getMapValue(mapDB, "rec_lname") + "-" + StrUtil.getMapValue(param, "menu_name");

            mapParam.put("rec_lname", slname);
            int res1 = infoMenuMapper.updateInfoMenulname(lname, slname);

            mapParam.put("p_name", param.get("p_name"));
        } else {
            mapParam.put("parent_id", param.get("parent_id"));
            mapParam.put("ancestors", param.get("ancestors"));
            mapParam.put("rec_lname", param.get("menu_name"));
        }

        mapParam.put("menu_name", param.get("menu_name"));
        mapParam.put("menu_img", param.get("menu_img"));
        mapParam.put("menu_desc", param.get("menu_desc"));
        Map<String, Object> mapCondition = new HashMap<String, Object>();
        mapCondition.put("rec_id", param.get("rec_id"));
        int count = this.update(mapCondition, "s_info_menu", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改资讯分类目录失败"); 
	}

	/** delInfoMenu : 删除资讯分类目录 **/
	public ResponseBodyVO delInfoMenu(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断 

		// Table:s_info_menu
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.delete("s_info_menu",  mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除资讯分类目录失败");
	}






}
