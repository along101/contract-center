package com.along101.contract.dao;

import com.along101.contract.ContractBackendApplicationTests;
import com.along101.contract.entity.FileHistoryEntity;
import com.along101.contract.ContractBackendApplicationTests;
import com.along101.contract.entity.FileHistoryEntity;
import com.along101.contract.entity.ProtoFileEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author zhangchengxi
 * Date 2018/6/7
 */
public class FileHistoryEntityMapperTest extends ContractBackendApplicationTests {

    @Autowired
    private FileHistoryEntityMapper fileHistoryEntityMapper;

    @Test
    public void queryReleasedProtoFileVOs() {
        List<FileHistoryEntity> fileHistoryEntities = fileHistoryEntityMapper.queryReleasedProtoFileVOs("333", 20L);
        assertEquals(2, fileHistoryEntities.size());
        fileHistoryEntities.stream().map(FileHistoryEntity::getId).forEach(System.out::println);
    }

    @Test
    public void queryDeletedProtoFileVOsAfterLatestRelease() {
        List<FileHistoryEntity> protoFileEntities = fileHistoryEntityMapper.queryDeletedProtoFileVOsAfterLatestRelease("333");
        assertTrue(57L == protoFileEntities.get(0).getId());
    }
}