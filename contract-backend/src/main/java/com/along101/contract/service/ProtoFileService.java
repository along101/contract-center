package com.along101.contract.service;

import com.google.common.collect.Lists;
import com.along101.contract.codegen.CodegenUtils;
import com.along101.contract.common.exception.ContractException;
import com.along101.contract.common.utils.ConvertUtils;
import com.along101.contract.controller.query.ContractQuery;
import com.along101.contract.dao.ProtoFileEntityMapper;
import com.along101.contract.entity.FileHistoryEntity;
import com.along101.contract.entity.ProtoFileEntity;
import com.along101.contract.vo.ProtoFileVO;
import com.along101.contract.vo.VersionInfo;
import com.squareup.wire.schema.DBSchemaLoader;
import com.squareup.wire.schema.Schema;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProtoFileService {


    @Autowired
    private ProtoFileEntityMapper protoFileEntityMapper;
    @Autowired
    private DBSchemaLoader dbSchemaLoader;
    @Autowired
    private VersionService versionService;


    @Transactional
    public List<ProtoFileEntity> addProtoFiles(String appId, Long orgId, String username, MultipartFile[] files) {

        List<ProtoFileEntity> protoFileEntities = Arrays.stream(files).map(this::parseFile).collect(Collectors.toList());

        return addProtoFiles(appId, orgId, username, protoFileEntities);
    }

    @Transactional
    public List<ProtoFileEntity> addProtoFiles(String appId, Long orgId, List<ProtoFileVO> files, String username) {
        List<ProtoFileEntity> protoFileEntities = files.stream().map(this::parseFile).collect(Collectors.toList());
        return addProtoFiles(appId, orgId, username, protoFileEntities);
    }

    private List<ProtoFileEntity> addProtoFiles(String appId, Long orgId, String username, List<ProtoFileEntity> protoFileEntities) {
        Set<Long> ids = protoFileEntities.stream().map(ProtoFileEntity::getId).filter(Objects::nonNull).collect(Collectors.toSet());
        deleteProtoFiles(ids, appId);

        for (ProtoFileEntity fileEntity : protoFileEntities) {

            //以下信息是不包括在 file 中的, 其他信息通过 file 可以解析得到
            fileEntity.setOrgId(orgId);
            fileEntity.setInsertBy(username);
            fileEntity.setUpdateBy(username);

            fileEntity.setIsActive(true);
            //验证每一个文件的合法性
            validate(fileEntity, appId);
        }

        //测试编译能否成功,验证上传文件整体的合法性
        Schema schema = null;
        try {
            List<ProtoFileVO> converted = ConvertUtils.convert(protoFileEntities, ProtoFileVO.class);
            schema = dbSchemaLoader.testCompile(converted, null, appId);
        } catch (Exception ignore) {
            //  TODO: 2018/6/6 报错给前端展示
        }

        //保存文件
        // TODO: 2018/5/10 L1 批量
        for (ProtoFileEntity protoFileEntity : protoFileEntities) {
            save(protoFileEntity);
        }

        return protoFileEntities;
    }


    @Transactional(rollbackFor = Exception.class)
    public int deleteProtoFiles(Set<Long> protoIds, String appId) {
        // TODO: 2018/5/10 L1 batch
        // TODO: 2018/5/10 L1 这里先硬删除,去掉索引,加上校验逻辑之后改成软删
        if (CollectionUtils.isEmpty(protoIds)) {
            return 0;
        }

//        List<ProtoFileEntity> protoFileEntities = protoFileEntityMapper.queryByIdsAndAppId(protoIds, appId);
//        List<ProtoFileVO> converted = ConvertUtils.convert(protoFileEntities, ProtoFileVO.class);
//        versionService.addDeleteRecord(converted);
//
//        for (Long id : protoIds) {
//            protoFileEntityMapper.deleteByPrimaryKeyAndAppId(id, appId);
//        }
//
//        return protoIds.size();

        ArrayList<Long> idList = Lists.newArrayList(protoIds);
        protoFileEntityMapper.changeStatus(idList, ProtoFileVO.Status.DELETE.getCode());

        return protoIds.size();
    }

    /**
     * 检验上传的proto文件的合法性:
     * 1. 上传参数中的appId 是否和 option 中的 appId 一致
     * 2. packageName+name唯一
     *
     * @param protoFileEntity
     * @param appid
     * @return
     */
    private void validate(ProtoFileEntity protoFileEntity, String appid) {
        boolean sameAppId = appid.equals(protoFileEntity.getAppid());
        // TODO: 2018/5/9 L1  packageName+name唯一  先由数据库唯一索引保证
        if (!sameAppId) {
            throw new ContractException("appId 未指定或者 option中的appId与上传项目的appId不匹配,文件:" + protoFileEntity.getName());
        }
    }

    private void save(ProtoFileEntity protoFileEntity) {
        ProtoFileEntity exist = protoFileEntityMapper.findByPackageAndNameAndAppId(protoFileEntity);
        if (Objects.isNull(exist)) {
            protoFileEntity.setStatus(ProtoFileVO.Status.NEW.getCode());
            protoFileEntityMapper.insert(protoFileEntity);
        } else {
            protoFileEntity.setStatus(ProtoFileVO.Status.UPDATE.getCode());
            protoFileEntity.setId(exist.getId());
            protoFileEntity.setIsActive(true);
            protoFileEntityMapper.updateByPrimaryKeySelective(protoFileEntity);
        }
    }

    public ProtoFileVO findContractById(Long id) {
        ProtoFileEntity protoFileEntity = protoFileEntityMapper.selectByPrimaryKey(id);
        if (protoFileEntity == null) {
            return null;
        }
        ProtoFileVO protoFileVO = ConvertUtils.convert(protoFileEntity, ProtoFileVO.class);
        return protoFileVO;
    }

    public ProtoFileVO findContractById(Long id, String revision) {
        ProtoFileVO protoFileVO;
        if (VersionInfo.isDraft(revision)) {
            ProtoFileEntity protoFileEntity = protoFileEntityMapper.selectByPrimaryKey(id);
            if (protoFileEntity == null) {
                return null;
            }
            protoFileVO = ConvertUtils.convert(protoFileEntity, ProtoFileVO.class);
        } else {
            FileHistoryEntity releasedProtoFile = versionService.getReleasedProtoFile(id);
            if (releasedProtoFile == null) {
                return null;
            }
            protoFileVO = ConvertUtils.convert(releasedProtoFile, ProtoFileVO.class);
        }

        return protoFileVO;
    }


    public List<ProtoFileVO> findContractsByAppid(String appid, String revision) {
        if (VersionInfo.isDraft(revision)) {
            // 如果是草稿,从proto_file表获取数据
            List<ProtoFileEntity> contracts = protoFileEntityMapper.findContractsByAppid(appid);
            List<ProtoFileVO> protoFileVOS = ConvertUtils.convert(contracts, ProtoFileVO.class);
            return protoFileVOS;
        } else {
            List<ProtoFileVO> protoFileVOS = versionService.queryReleasedProtoFileVOs(appid, revision);
            return protoFileVOS;
        }

    }

    public List<ProtoFileVO> findContractsByCondition(ContractQuery contractQuery) {

        String rpcName = contractQuery.getRpcMethodName() == null ? "" : contractQuery.getRpcMethodName();
        String insertBy = contractQuery.getInsertBy() == null ? "" : contractQuery.getInsertBy();

        List<ProtoFileEntity> contracts = protoFileEntityMapper.findContractsByCondition(rpcName, insertBy);
        List<ProtoFileVO> protoFileVOS = ConvertUtils.convert(contracts, ProtoFileVO.class);
        return protoFileVOS;
    }

    private ProtoFileEntity parseFile(MultipartFile file) {
        String content = CodegenUtils.readContentFromMultipartFile(file);
        return parseFile(file.getOriginalFilename(), content);
    }

    private ProtoFileEntity parseFile(ProtoFileVO file) {
        return parseFile(file.getName(), file.getContent());
    }

    private ProtoFileEntity parseFile(String fileName, String content) {
        ProtoFileEntity protoFileVO = CodegenUtils.appendProtoFile(fileName, content);
        protoFileVO.setStatus(0);

        return protoFileVO;

    }

    public List<ProtoFileVO> queryContractByIds(String appId, Set<Long> fileIds) {
        List<ProtoFileVO> contractsByAppid = findContractsByAppid(appId,VersionInfo.DRAFT);
        return contractsByAppid.stream().filter(contract -> fileIds.contains(contract.getId())).collect(Collectors.toList());
    }

    public void changeStatus(List<Long> draftIds, ProtoFileVO.Status released) {
        if (CollectionUtils.isNotEmpty(draftIds)) {
            protoFileEntityMapper.changeStatus(draftIds, released.getCode());
        }
    }

    public int completeDelete(List<Long> protoFileIds, String appId) {
        for (Long protoFileId : protoFileIds) {
            protoFileEntityMapper.deleteByPrimaryKeyAndAppId(protoFileId, appId);
        }
        return protoFileIds.size();
    }
}
