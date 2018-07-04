package com.along101.contract.dao;

import com.along101.contract.entity.ReleaseHistoryEntity;
import com.along101.contract.entity.ReleaseHistoryEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReleaseHistoryEntityMapper {

    int insert(ReleaseHistoryEntity record);

    int insertSelective(ReleaseHistoryEntity record);

    ReleaseHistoryEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReleaseHistoryEntity record);

    int updateByPrimaryKey(ReleaseHistoryEntity record);

    ReleaseHistoryEntity getLatestReleaseHistory(String appId);

    ReleaseHistoryEntity selectByRevisionNumber(@Param("appId")String appId,@Param("revisionNumber")String revisionNumber);

    List<ReleaseHistoryEntity> queryReleaseHistories(String appId);

    ReleaseHistoryEntity selectByTag(@Param("appId") String appId, @Param("tag")String tag);
}