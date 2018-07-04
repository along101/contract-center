package com.squareup.wire.schema;

import com.google.common.collect.Maps;
import com.along101.contract.codegen.CodegenUtils;
import com.along101.contract.common.exception.ContractException;
import com.along101.contract.common.utils.ConvertUtils;
import com.along101.contract.dao.ProtoFileEntityMapper;
import com.along101.contract.entity.ProtoFileEntity;
import com.along101.contract.service.VersionService;
import com.along101.contract.vo.ProtoFileVO;
import com.squareup.wire.schema.internal.parser.ProtoFileElement;
import com.squareup.wire.schema.internal.parser.ProtoParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author zhangchengxi
 * Date 2018/5/7
 */
@Slf4j
@Component
public class DBSchemaLoader {
    private static final String GOOGLE_PROTO_APP_ID = "google";

    @Autowired
    private ProtoFileEntityMapper protoFileEntityMapper;

    @Autowired
    private VersionService versionService;


    public Schema generateSchema(List<ProtoFileVO> protoFileVOS) {
        HashMap<String, ProtoFile> googleProtoFileMap = getCommonProtoFiles();
        HashMap<String, ProtoFile> protoFileMap = getProtoFileMap(protoFileVOS);
        protoFileMap.putAll(googleProtoFileMap);

        return new Linker(protoFileMap.values()).link();
    }


    public Schema loadFromDBByAppIdAndVersion(String appId, String version) {
        List<ProtoFileVO> protoFileVOS = versionService.queryReleasedProtoFileVOs(appId, version);
        return generateSchema(protoFileVOS);
    }

    private HashMap<String, ProtoFile> getCommonProtoFiles() {
        List<ProtoFileEntity> googleProtoFileEntities = protoFileEntityMapper.queryByAppId(GOOGLE_PROTO_APP_ID);
        List<ProtoFileVO> converted = ConvertUtils.convert(googleProtoFileEntities, ProtoFileVO.class);
        return getProtoFileMap(converted);
    }

    private HashMap<String, ProtoFile> getProtoFileMap(List<ProtoFileVO> protoFileVOS) {
        HashMap<String, ProtoFile> tempProtoFileMap = Maps.newHashMap();
        for (ProtoFileVO protoFileVO : protoFileVOS) {
            String packageName = protoFileVO.getPackageName();
            Location location = CodegenUtils.getLocation(packageName, protoFileVO.getName());

            ProtoFileElement element = ProtoParser.parse(location, protoFileVO.getContent());
            ProtoFile protoFile = ProtoFile.get(element);
            tempProtoFileMap.put(location.path(), protoFile);
        }
        return tempProtoFileMap;
    }

    /**
     * 测试新增/修改的proto文件是否合法
     *
     * @param newProtoFileVOS 新增/修改的Proto文件
     * @param appId
     * @return 编译成功后的Schema
     */
    public Schema testCompile(List<ProtoFileVO> newProtoFileVOS,List<ProtoFileVO> deleteProtoFileVOs, String appId) {
        HashMap<String, ProtoFile> commonProtoFiles = getCommonProtoFiles();

        // 当发布之后删除已发布的文件,会导致用下面这个方法获得的文件比实际的多
        List<ProtoFileVO> stableProtoFileVOs = versionService.queryLatestReleasedProtoFileVOs(appId);

        if (CollectionUtils.isNotEmpty(deleteProtoFileVOs)) {
            for (ProtoFileVO deleteProtoFileVO : deleteProtoFileVOs) {
                Iterator<ProtoFileVO> iterator = stableProtoFileVOs.iterator();
                if (iterator.hasNext()) {
                    ProtoFileVO stableProtoFileVO = iterator.next();
                    if (deleteProtoFileVO.getName().equals(stableProtoFileVO.getName()) && deleteProtoFileVO.getPackageName().equals(deleteProtoFileVO.getPackageName())) {
                        iterator.remove();
                    }
                }
            }
        }

        HashMap<String, ProtoFile> entityMap = getProtoFileMap(stableProtoFileVOs);
        HashMap<String, ProtoFile> newEntityMap = getProtoFileMap(newProtoFileVOS);

        entityMap.putAll(commonProtoFiles);
        entityMap.putAll(newEntityMap);


        Schema schema;
        try {
            schema = new Linker(entityMap.values()).link();
        } catch (Exception e) {
            throw new ContractException(e, "上传的文件编译错误:" + e.getMessage());
        }

        return schema;
    }

}
