package com.along101.contract.service;

import com.along101.contract.vo.ProtoFileVO;
import com.google.common.collect.Sets;
import com.along101.contract.ContractBackendApplicationTests;
import com.along101.contract.vo.ProtoFileVO;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author zhangchengxi
 * Date 2018/6/7
 */
//@Transactional
public class VersionServiceTest extends ContractBackendApplicationTests {

    @Autowired
    private VersionService versionService;

    @Test
    public void release() {
        versionService.release("333","", Sets.newConcurrentHashSet(Lists.newArrayList(132L,133L)));

    }


    @Test
    public void queryReleasedProtoFileVO() {
        List<ProtoFileVO> protoFileVOS = versionService.queryReleasedProtoFileVOs("333", "270ebc5aea4196e96efbae6ab923c214a358b41c");
        protoFileVOS.stream().map(ProtoFileVO::getId).forEach(System.out::println);
    }

    @Test
    public void rollback(){
        versionService.rollback("333",15L);

    }


}