package com.cbox.business.filemanage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbox.base.core.service.BaseService;
import com.cbox.business.filemanage.entity.FileZoneRecord;
import com.cbox.business.filemanage.mapper.FileZoneRecordMapper;

/**
 * <p>
 * 文件分片记录 服务实现类
 * </p>
 * 
 */
@Service
public class FileZoneRecordService extends BaseService {

    @Autowired
    private FileZoneRecordMapper fileZoneRecordMapper;

    /**
     * : 按分片md5+文件md5 查询
     * 
     * @param zoneMd5 分片md5
     * @param zoneTotalMd5 文件md5
     * @return
     */
    public FileZoneRecord selByMD5AndZoneTotalMd5(String zoneMd5, String zoneTotalMd5) {
        return fileZoneRecordMapper.getFileZoneRecord(zoneMd5, zoneTotalMd5);
    }

    /**
     * : 按分 +文件md5 查询 所有分片
     * 
     * @param zoneTotalMd5 文件md5
     * @return
     */
    public List<FileZoneRecord> selByTotalMd5(String zoneTotalMd5) {
        List<FileZoneRecord> list = fileZoneRecordMapper.selectList(zoneTotalMd5);
        return list;
    }

    public int save(FileZoneRecord fileZoneRecord) {
        return fileZoneRecordMapper.save(fileZoneRecord);
    }

    public int removeByIds(List<String> ids) {
        return fileZoneRecordMapper.removeByIds(ids);
    }
}
