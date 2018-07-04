package com.along101.contract.service;

import com.along101.contract.vo.ProtoFileVO;
import com.along101.contract.vo.VersionInfo;
import com.google.common.collect.Lists;
import com.along101.contract.util.JsonToObjectUtil;
import com.along101.contract.vo.ProtoFileVO;
import com.along101.contract.vo.VersionInfo;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Sets;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@Rollback
@Slf4j
public class ProtoFileServiceTest {

    private static final String APPID = "ProtoFileServiceTest";

    @Autowired
    private ProtoFileService protoFileService;

    @Before
    public void init() {
        // TODO: 2018/5/10 完善单元测试
    }

    @Test
    public void testAddNewContract() throws Exception {
        ProtoFileVO protoFileVO = JsonToObjectUtil.getTestObject("service/ContractDto.json", ProtoFileVO.class);
        //插入
        //int result = protoFileService.addProtoFile(protoFileVO);
        Long entityId = protoFileVO.getId();

        log.info("entityId: " + entityId);

        //查找
        ProtoFileVO newContract = protoFileService.findContractById(entityId);

        Assert.assertEquals("contract1", newContract.getName());
        //Assert.assertEquals("contractApi",newContract.getServiceName());

        //更新
        newContract.setOrgId(4L);
//        protoFileService.deployContract(newContract);
        //再找
        newContract = protoFileService.findContractById(newContract.getId());
        Assert.assertEquals(4L, newContract.getOrgId().longValue());

        //删除
//        protoFileService.deleteContractById(newContract.getId());
        //再找
        newContract = protoFileService.findContractById(newContract.getId());
        Assert.assertNull(newContract);
    }

    @Test
    public void testDeleteProtoFile() {
        List<Long> ids = Lists.newArrayList(92L);
        HashSet<Long> integers = Sets.newHashSet(ids);
        protoFileService.deleteProtoFiles(integers, "333");

        List<ProtoFileVO> contracts = protoFileService.findContractsByAppid("333", VersionInfo.DRAFT);
        Optional<ProtoFileVO> any = contracts.stream().filter(protoFileVO -> protoFileVO.getId().equals(92L)).findAny();
        Assert.assertFalse(any.isPresent());
    }


}
