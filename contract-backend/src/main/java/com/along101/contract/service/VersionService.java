package com.along101.contract.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.along101.auth.common.identity.Identity;
import com.along101.contract.common.exception.ContractException;
import com.along101.contract.common.utils.ConvertUtils;
import com.along101.contract.context.IdentityContext;
import com.along101.contract.dao.FileHistoryEntityMapper;
import com.along101.contract.dao.ReleaseHistoryEntityMapper;
import com.along101.contract.entity.FileHistoryEntity;
import com.along101.contract.entity.ReleaseHistoryEntity;
import com.along101.contract.vo.ProtoFileVO;
import com.along101.contract.vo.VersionInfo;
import com.squareup.wire.schema.DBSchemaLoader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhangchengxi
 * Date 2018/6/6
 */
@Slf4j
@Component
public class VersionService {

    @Autowired
    private FileHistoryEntityMapper fileMapper;
    @Autowired
    private ReleaseHistoryEntityMapper releaseMapper;
    @Autowired
    private DBSchemaLoader dbSchemaLoader;
    @Autowired
    private ProtoFileService protoFileService;

    @Transactional(rollbackFor = Exception.class)
    public ReleaseHistoryEntity release(String appId, String tag, Set<Long> fileIds) {
        // TODO: 2018/6/6 参数验证 : fileIds 全部属于 appId  (相信同事,不会乱搞的)
        List<ProtoFileVO> protoFileVOs = protoFileService.queryContractByIds(appId, fileIds);
        List<ProtoFileVO> unReleased = protoFileVOs.stream()
                .filter(protoFileVO -> !protoFileVO.getStatus().equals(ProtoFileVO.Status.RELEASED.getCode()))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(unReleased)) {
            log.warn("all files are released.fileIds:" + JSON.toJSONString(fileIds));
            return null;
        }
        // 验证文件合法性
        Map<Integer, List<ProtoFileVO>> statusMap = unReleased.stream().collect(Collectors.groupingBy(ProtoFileVO::getStatus));
        List<ProtoFileVO> deletedProtoFiles = statusMap.getOrDefault(ProtoFileVO.Status.DELETE.getCode(),Lists.newArrayList());
        List<ProtoFileVO> draftProtoFileVos = unReleased.stream().filter(ProtoFileVO.Status.newOrUpdated()).collect(Collectors.toList());
        // 没有抛出异常代表合法
        dbSchemaLoader.testCompile(draftProtoFileVos,deletedProtoFiles,appId);

        // 保存发布记录
        Long releaseId = saveReleaseHistory(appId, tag);
        // 插入删除记录
        addDeleteRecord(deletedProtoFiles, releaseId);
        // 删除proto file 表中数据
        protoFileService.completeDelete(deletedProtoFiles.stream().map(ProtoFileVO::getId).collect(Collectors.toList()),appId);
        // 保存发布文件
        save(draftProtoFileVos, releaseId);
        // 更改文件状态
        List<Long> draftIds = draftProtoFileVos.stream().map(ProtoFileVO::getId).collect(Collectors.toList());
        protoFileService.changeStatus(draftIds, ProtoFileVO.Status.RELEASED);

        return releaseMapper.selectByPrimaryKey(releaseId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void rollback(String appId, Long releaseHistoryId) {
        List<ProtoFileVO> protoFileVOS = queryReleasedProtoFileVOs(appId, releaseHistoryId);
        protoFileVOS.forEach(protoFileVO -> protoFileVO.setId(null));
        protoFileService.addProtoFiles(appId, 0L, protoFileVOS, IdentityContext.getIdentity().map(Identity::getName).orElse("UNDEFINE"));
    }

    public List<ReleaseHistoryEntity> queryReleaseHistories(String appId) {
        return releaseMapper.queryReleaseHistories(appId);
    }

    private Long saveReleaseHistory(String appId, String tag) {
        ReleaseHistoryEntity releaseHistoryEntity = new ReleaseHistoryEntity();
        releaseHistoryEntity.setAppid(appId);
        releaseHistoryEntity.setTag(tag);
        String name = IdentityContext.getIdentity().map(Identity::getName).orElse("UNDEFINE");
        releaseHistoryEntity.setInsertBy(name);
        releaseHistoryEntity.setUpdateBy(name);
        releaseMapper.insert(releaseHistoryEntity);
        return releaseHistoryEntity.getId();
    }

    public void changeTag(Long releaseHistoryId, String tag) {
        ReleaseHistoryEntity releaseHistoryEntity = releaseMapper.selectByPrimaryKey(releaseHistoryId);
        releaseHistoryEntity.setTag(tag);
        releaseHistoryEntity.setUpdateTime(new Date());
        String name = IdentityContext.getIdentity().map(Identity::getName).orElse("UNDEFINE");
        releaseHistoryEntity.setUpdateBy(name);
        releaseMapper.updateByPrimaryKeySelective(releaseHistoryEntity);
    }


    /**
     * 保存发布成功的proto文件
     * <p>
     * 通过每个proto文件的 packageName和文件名唯一确定一个文件,
     *
     * @param protoFileVOs
     * @param releaseId
     */
    private void save(List<ProtoFileVO> protoFileVOs, Long releaseId) {
        List<FileHistoryEntity> fileHistoryEntities = ConvertUtils.convert(protoFileVOs, FileHistoryEntity.class);
        for (FileHistoryEntity fileHistoryEntity : fileHistoryEntities) {
            fileHistoryEntity.setReleaseHistoryId(releaseId);
            fileHistoryEntity.setId(null);
            fileHistoryEntity.setStatus(ProtoFileVO.FileHistoryStatus.CREATE.getCode());

            fileMapper.insert(fileHistoryEntity);
        }
    }

    public ReleaseHistoryEntity getLatestReleaseHistory(String appId) {
        return releaseMapper.getLatestReleaseHistory(appId);
    }

    public List<ProtoFileVO> queryReleasedProtoFileVOs(String appId, String revisionNumber) {
        if (VersionInfo.DRAFT.equals(revisionNumber)) {
            return protoFileService.findContractsByAppid(appId,revisionNumber);
        }
        ReleaseHistoryEntity releaseHistoryEntity = releaseMapper.selectByRevisionNumber(appId, revisionNumber);
        if (Objects.isNull(releaseHistoryEntity)) {
            return Lists.newArrayList();
        }
        return queryReleasedProtoFileVOs(appId, releaseHistoryEntity.getId());
    }

    public List<ProtoFileVO> queryLatestReleasedProtoFileVOs(String appId) {
        ReleaseHistoryEntity latestReleaseHistory = getLatestReleaseHistory(appId);
        if (Objects.isNull(latestReleaseHistory)) {
            return Lists.newArrayList();
        }
        return queryReleasedProtoFileVOs(appId, latestReleaseHistory.getId());
    }

    private List<ProtoFileVO> queryReleasedProtoFileVOs(String appId, Long releaseHistoryId) {
        List<FileHistoryEntity> releasedProtoFiles = fileMapper.queryReleasedProtoFileVOs(appId, releaseHistoryId);
        List<FileHistoryEntity> notDeleted = releasedProtoFiles.stream()
                .filter(releasedProtoFile -> ProtoFileVO.FileHistoryStatus.DELETE.getCode() != releasedProtoFile.getStatus())
                .collect(Collectors.toList());
        return ConvertUtils.convert(notDeleted, ProtoFileVO.class);
    }

    public void addDeleteRecord(List<ProtoFileVO> protoFileVOs, Long releaseHsitoryId) {
        List<FileHistoryEntity> fileHistoryEntities = ConvertUtils.convert(protoFileVOs, FileHistoryEntity.class);
        for (FileHistoryEntity fileHistoryEntity : fileHistoryEntities) {
            fileHistoryEntity.setReleaseHistoryId(releaseHsitoryId);
            fileHistoryEntity.setId(null);
            fileHistoryEntity.setStatus(ProtoFileVO.FileHistoryStatus.DELETE.getCode());
            fileMapper.insert(fileHistoryEntity);
        }
    }


    public FileHistoryEntity getReleasedProtoFile(Long fileId) {
        return fileMapper.selectByPrimaryKey(fileId);
    }

    public List<ProtoFileVO> queryDeletedProtoFileVOsAfterLatestRelease(String appId) {
        List<FileHistoryEntity> protoFileEntities = fileMapper.queryDeletedProtoFileVOsAfterLatestRelease(appId);
        if (CollectionUtils.isNotEmpty(protoFileEntities)) {
            return ConvertUtils.convert(protoFileEntities, ProtoFileVO.class);
        }
        return Lists.newArrayList();
    }
}
