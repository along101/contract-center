package com.along101.contract.service;

import com.along101.contract.codegen.CodegenUtils;
import com.along101.contract.dao.ProtoFileEntityMapper;
import com.along101.contract.entity.ProtoFileEntity;
import com.along101.contract.vo.NavigateTreeVO;
import com.along101.contract.ContractBackendApplicationTests;
import com.along101.contract.codegen.CodegenUtils;
import com.along101.contract.dao.ProtoFileEntityMapper;
import com.along101.contract.entity.ProtoFileEntity;
import com.along101.contract.vo.NavigateTreeVO;
import com.squareup.wire.schema.DBSchemaLoader;
import com.squareup.wire.schema.ProtoFile;
import com.squareup.wire.schema.Schema;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @author zhangchengxi
 * Date 2018/5/8
 */
@Transactional
public class NavigateServiceTest extends ContractBackendApplicationTests {

    @Autowired
    private NavigateService navigateService;
    @Autowired
    private ProtoFileEntityMapper protoFileEntityMapper;
    @Autowired
    private DBSchemaLoader dbSchemaLoader;

    @Test
    public void testResolveAndAddProtoFile() {
        Schema schema = dbSchemaLoader.loadFromDBByAppIdAndVersion("test.appId", "");

        List<ProtoFileEntity> protoFileEntities = protoFileEntityMapper.queryAll();
        for (ProtoFileEntity protoFileEntity : protoFileEntities) {
            ProtoFile protoFile = schema.protoFile(CodegenUtils.getLocation(protoFileEntity.getPackageName(), protoFileEntity.getName()).path());
            navigateService.resolveProtoFile(protoFile, protoFileEntity.getId());
        }
    }

    @Test
    public void testQueryNavigateTree() {
        List<NavigateTreeVO> navigateTreeVO = navigateService.queryNavigateTree("111111", "draft");
        Assert.assertTrue(Objects.nonNull(navigateTreeVO));
    }


}