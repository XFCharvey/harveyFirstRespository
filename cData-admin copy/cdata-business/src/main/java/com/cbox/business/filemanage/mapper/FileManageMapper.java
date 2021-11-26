package com.cbox.business.filemanage.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: FileManageMapper
 * @Function: 文件管理
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface FileManageMapper {

	/** listFileManage : 获取文件管理列表数据 **/
	public List<Map<String, Object>> listFileManage(Map<String, Object> param);

    /** getFileByIds : 获取指定ids的文件信息 **/
    public List<Map<String, Object>> getFileByIds(Map<String, Object> param);

	/** getFileManage : 获取指定id的文件管理数据 **/
	public Map<String, Object> getFileManage(Map<String, Object> param);


}
