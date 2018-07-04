package com.along101.contract.dao;

import com.along101.contract.entity.AuditLogEntity;
import com.along101.contract.entity.AuditLogEntity;

public interface AuditLogEntityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AuditLogEntity record);

    int insertSelective(AuditLogEntity record);

    AuditLogEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AuditLogEntity record);

    int updateByPrimaryKey(AuditLogEntity record);
}