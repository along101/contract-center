package com.along101.contract.dao;

import com.along101.contract.ContractBackendApplicationTests;
import com.along101.contract.entity.ProtoFileEntity;
import com.google.common.collect.Lists;
import com.along101.contract.ContractBackendApplicationTests;
import com.along101.contract.entity.ProtoFileEntity;
import org.assertj.core.util.Sets;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author zhangchengxi
 * Date 2018/5/7
 */
public class ProtoFileEntityMapperTest extends ContractBackendApplicationTests {

    @Autowired
    private ProtoFileEntityMapper protoFileEntityMapper;

    @Test
    public void testInsert(){
        ProtoFileEntity protoFileEntity = new ProtoFileEntity();
        protoFileEntity.setName("test.proto");
        protoFileEntity.setContent("test content");
        protoFileEntity.setPackageName("com.along101");
        protoFileEntity.setStatus(0);
        protoFileEntity.setOrgId(0L);
        protoFileEntity.setAppid("test.appid");
        protoFileEntity.setIsActive(true);

        protoFileEntityMapper.insert(protoFileEntity);
    }

    @Test
    public void testLoadAll(){
        List<ProtoFileEntity> protoFileEntities = protoFileEntityMapper.queryAll();
        System.out.println(protoFileEntities.size());

    }

    @Test
    public void changeStatus(){
        protoFileEntityMapper.changeStatus(Lists.newArrayList(1L,2L,3L),1);
    }

    @Test
    public void queryByIdsAndAppId(){
        HashSet<Long> longs = Sets.newHashSet(Lists.newArrayList(132L, 133L));
        List<ProtoFileEntity> protoFileEntities = protoFileEntityMapper.queryByIdsAndAppId(longs, "333");
        Assert.assertEquals(2,protoFileEntities.size());
    }

}
