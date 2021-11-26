package com.cbox.business.filemanage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cbox.business.filemanage.entity.FileRecord;

/**
 * <p>
 * 文件上传记录 Mapper 接口
 * </p>
 * 
 */
@Mapper
public interface FileRecordMapper {

    FileRecord getById(@Param("id") String id);

    List<FileRecord> listByIds(@Param("ids") List<String> ids);

    int save(FileRecord fileRecord);

    int update(FileRecord fileRecord);

    int deleteById(@Param("id") String id);

    int deleteBatchIds(@Param("ids") List<String> ids);

    List<FileRecord> selectList(@Param("md5Value") String md5Value, @Param("uploadType") Integer uploadType);

}