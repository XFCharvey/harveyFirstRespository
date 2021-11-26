package com.cbox.business.filemanage.controller;

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
import com.cbox.business.filemanage.service.FileManageService;


/**
 * @ClassName: FileManageController
 * @Function: 文件管理
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/filemanage")
public class FileManageController extends BaseController {

	@Autowired
	private FileManageService FileManageService;

	
	/** listFileManage : 获取文件管理列表数据 **/
	@RequestMapping(value = "listFileManage", method = RequestMethod.POST)
	public TableDataInfo listFileManage(@RequestBody Map<String, Object> param) {

        // 校验必填参数
        String checkResult = this.validInput("create_by", param);
		startPage();
		List<Map<String, Object>> list= FileManageService.listFileManage(param);

		return getDataTable(list);
	}

    /** getFileByIds : 获取指定ids的文件信息 **/
    @RequestMapping(value = "getFileByIds", method = RequestMethod.POST)
    public ResponseBodyVO getFileByIds(@RequestBody Map<String, Object> param) {

        // 校验必填参数
        String checkResult = this.validInput("id", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }

        return FileManageService.getFileByIds(param);
    }

	/** getFileManage : 获取指定id的文件管理数据 **/
	@RequestMapping(value = "getFileManage", method = RequestMethod.POST)
	public ResponseBodyVO getFileManage(@RequestBody Map<String, Object> param) {

		// 校验必填参数
        String checkResult = this.validInput("id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return FileManageService.getFileManage(param);
	}

	/** addFileManage : 新增文件管理 **/
	@RequestMapping(value = "addFileManage", method = RequestMethod.POST)
	public ResponseBodyVO addFileManage(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("id,org_name,server_local_name,server_local_path,network_path,upload_type,md5_value,file_size,is_merge,is_zone,zone_total,zone_date,zone_merge_date,file_type,upload_device,upload_ip,upload_count,download_count,storage_date", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return FileManageService.addFileManage(param);
	}

	/** updateFileManage : 修改文件管理 **/
	@RequestMapping(value = "updateFileManage", method = RequestMethod.POST)
	public ResponseBodyVO updateFileManage(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return FileManageService.updateFileManage(param);
	}

	/** delFileManage : 删除文件管理 **/
	@RequestMapping(value = "delFileManage", method = RequestMethod.POST)
	public ResponseBodyVO delFileManage(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return FileManageService.delFileManage(param);
	}


}
