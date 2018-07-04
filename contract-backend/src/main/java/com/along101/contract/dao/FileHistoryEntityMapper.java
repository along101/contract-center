package com.along101.contract.dao;

import com.along101.contract.entity.FileHistoryEntity;
import com.along101.contract.entity.FileHistoryEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FileHistoryEntityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(FileHistoryEntity record);

    int insertSelective(FileHistoryEntity record);

    FileHistoryEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FileHistoryEntity record);

    int updateByPrimaryKeyWithBLOBs(FileHistoryEntity record);

    int updateByPrimaryKey(FileHistoryEntity record);

    List<FileHistoryEntity> queryReleasedProtoFileVOs(@Param("appId") String appId, @Param("releaseHistoryId") Long releaseHistoryId);

    List<FileHistoryEntity> queryDeletedProtoFileVOsAfterLatestRelease(String appId);
}