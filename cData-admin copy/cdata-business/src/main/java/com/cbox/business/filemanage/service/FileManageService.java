package com.cbox.business.filemanage.service;

import java.util.ArrayList;
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
import com.cbox.business.filemanage.mapper.FileManageMapper;


/**
 * @ClassName: FileManageService
 * @Function: 文件管理
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class FileManageService extends BaseService {

    @Autowired
    private FileManageMapper fileManageMapper;
    
	/** listFileManage : 获取文件管理列表数据 **/
	public List<Map<String, Object>> listFileManage(Map<String, Object> param) {
        super.appendUserInfo(param);

        List<Map<String, Object>> list = fileManageMapper.listFileManage(param);

		return list;
	}

    /** getFileByIds : 获取指定ids的文件信息 **/
    public ResponseBodyVO getFileByIds(Map<String, Object> param) {
        super.appendUserInfo(param);

        // 在xml中对id值进行了遍历，所以这里要转换成list结构
        String id = StrUtil.getMapValue(param, "id");
        List<String> listId = new ArrayList<String>();
        if (StrUtil.isNotNull(id)) {
            String[] ids = id.split(",");

            for (int i = 0; i < ids.length; i++) {
                listId.add(ids[i]);
            }
        }
        param.remove("id");
        param.put("id", listId);

        List<Map<String, Object>> list = fileManageMapper.getFileByIds(param);

        return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, list);
    }

	/** getFileManage : 获取指定id的文件管理数据 **/
	public ResponseBodyVO getFileManage(Map<String, Object> param) {
		super.appendUserInfo(param);
        System.out.println(param.get("rec_id"));
        Map<String, Object> mapResult = fileManageMapper.getFileManage(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addFileManage : 新增文件管理 **/
	public ResponseBodyVO addFileManage(Map<String, Object> param) {

		// Table:d_sys_file_record
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("id", param.get("id"));
		mapParam.put("org_name", param.get("org_name"));
		mapParam.put("server_local_name", param.get("server_local_name"));
		mapParam.put("server_local_path", param.get("server_local_path"));
		mapParam.put("network_path", param.get("network_path"));
		mapParam.put("upload_type", param.get("upload_type"));
		mapParam.put("md5_value", param.get("md5_value"));
		mapParam.put("file_size", param.get("file_size"));
		mapParam.put("is_merge", param.get("is_merge"));
		mapParam.put("is_zone", param.get("is_zone"));
		mapParam.put("zone_total", param.get("zone_total"));
		mapParam.put("zone_date", param.get("zone_date"));
		mapParam.put("zone_merge_date", param.get("zone_merge_date"));
		mapParam.put("file_type", param.get("file_type"));
		mapParam.put("upload_device", param.get("upload_device"));
		mapParam.put("upload_ip", param.get("upload_ip"));
		mapParam.put("upload_count", param.get("upload_count"));
		mapParam.put("download_count", param.get("download_count"));
		mapParam.put("storage_date", param.get("storage_date"));
		int count = this.save( "d_sys_file_record", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "新增文件管理失败");
	}

	/** updateFileManage : 修改文件管理 **/
	public ResponseBodyVO updateFileManage(Map<String, Object> param) {

		// Table:d_sys_file_record
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("id", param.get("id"));
		mapParam.put("org_name", param.get("org_name"));
		mapParam.put("server_local_name", param.get("server_local_name"));
		mapParam.put("server_local_path", param.get("server_local_path"));
		mapParam.put("network_path", param.get("network_path"));
		mapParam.put("upload_type", param.get("upload_type"));
		mapParam.put("md5_value", param.get("md5_value"));
		mapParam.put("file_size", param.get("file_size"));
		mapParam.put("is_merge", param.get("is_merge"));
		mapParam.put("is_zone", param.get("is_zone"));
		mapParam.put("zone_total", param.get("zone_total"));
		mapParam.put("zone_date", param.get("zone_date"));
		mapParam.put("zone_merge_date", param.get("zone_merge_date"));
		mapParam.put("file_type", param.get("file_type"));
		mapParam.put("upload_device", param.get("upload_device"));
		mapParam.put("upload_ip", param.get("upload_ip"));
		mapParam.put("upload_count", param.get("upload_count"));
		mapParam.put("download_count", param.get("download_count"));
		mapParam.put("storage_date", param.get("storage_date"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.update(mapCondition, "d_sys_file_record", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改文件管理失败"); 
	}

	/** delFileManage : 删除文件管理 **/
	public ResponseBodyVO delFileManage(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断 

		// Table:d_sys_file_record
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.delete("d_sys_file_record",  mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除文件管理失败");
	}


}
