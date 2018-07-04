package com.along101.contract.dao;

import com.along101.contract.ContractBackendApplicationTests;
import com.along101.contract.entity.ReleaseHistoryEntity;
import com.along101.contract.ContractBackendApplicationTests;
import com.along101.contract.entity.ReleaseHistoryEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

/**
 * @author zhangchengxi
 * Date 2018/6/7
 */
@Transactional
public class ReleaseHistoryEntityMapperTest extends ContractBackendApplicationTests {

    @Autowired
    private ReleaseHistoryEntityMapper releaseHistoryEntityMapper;

    @Test
    public void getLatestReleaseHistory() {
        ReleaseHistoryEntity latestReleaseHistory = releaseHistoryEntityMapper.getLatestReleaseHistory("333");
        assertNotNull(latestReleaseHistory);
        ReleaseHistoryEntity notExist = releaseHistoryEntityMapper.getLatestReleaseHistory("notExist");
        assertNull(notExist);
    }

    @Test
    public void selectByRevisionNumber(){
        ReleaseHistoryEntity releaseHistoryEntity = releaseHistoryEntityMapper.selectByRevisionNumber("1","6ffe75b8bf062093a0455c27fa8b1fcf77c29058");
        assertNotNull(releaseHistoryEntity);
    }

    @Test
    public void test(){
        ReleaseHistoryEntity releaseHistoryEntity = releaseHistoryEntityMapper.selectByTag("333", "heheh");
        assertNotNull(releaseHistoryEntity);
    }
}