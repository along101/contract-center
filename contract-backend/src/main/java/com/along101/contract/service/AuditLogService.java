package com.along101.contract.service;


import com.along101.contract.dao.AuditLogEntityMapper;
import com.along101.contract.entity.AuditLogEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by qiankai02 on 2017/8/15.
 */
@Service
@Slf4j
public class AuditLogService {

    @Autowired
    private AuditLogEntityMapper auditLogEntityMapper;

    @Transactional
    public void save(AuditLogEntity auditLogEntity) {
        auditLogEntityMapper.insert(auditLogEntity);
    }
}
