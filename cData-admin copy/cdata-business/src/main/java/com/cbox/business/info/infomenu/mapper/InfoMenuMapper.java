package com.cbox.business.info.infomenu.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName: InfoMenuMapper
 * @Function: 资讯分类目录
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface InfoMenuMapper {

	/** listInfoMenu : 获取资讯分类目录列表数据 **/
	public List<Map<String, Object>> listInfoMenu(Map<String, Object> param);

	/** getInfoMenu : 获取指定id的资讯分类目录数据 **/
	public Map<String, Object> getInfoMenu(Map<String, Object> param);

    public int updateInfoMenu(@Param("newanc") String newanc, @Param("ance") String ance, @Param("ance1") String ance1);

    public int updateInfoMenulname(@Param("lname") String lname, @Param("slname") String slname);

    public List<Map<String, Object>> listInfoMenuAndSmalllevel(Map<String, Object> param);

    public List<Map<String, Object>> listInfoMenuSort(Map<String, Object> param);

    public List<Map<String, Object>> listInfoMenuTwo();

}
