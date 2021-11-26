package com.cbox.business.filemanage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cbox.business.filemanage.entity.FileZoneRecord;

/**
 * <p>
 * 文件分片记录 Mapper 接口
 * </p>
 */
@Mapper
public interface FileZoneRecordMapper {

    FileZoneRecord getFileZoneRecord(@Param("zoneMd5") String zoneMd5, @Param("zoneTotalMd5") String zoneTotalMd5);

    List<FileZoneRecord> selectList(@Param("zoneTotalMd5") String zoneTotalMd5);

    int save(FileZoneRecord fileZoneRecord);

    int removeByIds(@Param("ids") List<String> ids);

}