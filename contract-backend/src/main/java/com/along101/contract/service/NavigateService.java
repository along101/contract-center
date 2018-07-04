package com.along101.contract.service;

import com.google.common.collect.Lists;
import com.along101.contract.codegen.CodegenUtils;
import com.along101.contract.vo.NavigateTreeVO;
import com.along101.contract.vo.ProtoFileVO;
import com.along101.contract.vo.VersionInfo;
import com.along101.raptor.codegen.java.option.InterfaceMetaInfo;
import com.along101.raptor.codegen.java.option.MethodMetaInfo;
import com.along101.raptor.codegen.java.option.ProtoFileMetaInfo;
import com.squareup.wire.schema.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhangchengxi
 * Date 2018/5/8
 */
@Component
public class NavigateService {

    @Autowired
    private ProtoFileService protoFileService;
    @Autowired
    private VersionService versionService;
    @Autowired
    private DBSchemaLoader dbSchemaLoader;

    /**
     * 根据 revisionNumber 获取不同来源的导航树,如果 revisionNumber 是"draft",就在protofile表中
     * 获取数据,否则在file_history中获取数据
     *
     * @param appId
     * @param revisionNumber
     * @return
     */
    public List<NavigateTreeVO> queryNavigateTree(String appId, String revisionNumber) {
        if (VersionInfo.isDraft(revisionNumber)) {
            return getDraftNavigateTree(appId);
        } else {
            return getReleasedNavigateTree(appId, revisionNumber);
        }
    }

    /**
     * 数据源为proto_file表,生成的 navigate 中的 protoId 为 proto_file 中的主键
     *
     * @param appId
     * @return
     */
    private List<NavigateTreeVO> getDraftNavigateTree(String appId) {
        List<NavigateTreeVO> releasedNavigateTree = Lists.newArrayList();

        List<ProtoFileVO> protoFileVOS = protoFileService.findContractsByAppid(appId,VersionInfo.DRAFT);

        // 尝试所有文件放在一起构造
        try{
            Schema schema = dbSchemaLoader.generateSchema(protoFileVOS);
            releasedNavigateTree = getNavigateTree(protoFileVOS, schema);
            return releasedNavigateTree;
        }catch (Exception ignore){
        }

        Map<Integer, List<ProtoFileVO>> groupByStatus = protoFileVOS.stream().collect(Collectors.groupingBy(ProtoFileVO::getStatus));

        // 构造发布的部分
        List<ProtoFileVO> releasedProtoFiles = protoFileVOS.stream().filter(ProtoFileVO.Status.RELEASED.filter()).collect(Collectors.toList());
        List<ProtoFileVO> draftProtoFileVOS = protoFileVOS.stream().filter(ProtoFileVO.Status.newOrUpdated()).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(releasedProtoFiles)) {
            try{
                Schema schema = dbSchemaLoader.generateSchema(releasedProtoFiles);
                releasedNavigateTree = getNavigateTree(releasedProtoFiles, schema);
            }catch (Exception ignore){
                // 如果删除已发布的文件,可能会导致生schema成不成功
                // 当编译schema失败的时候所有文件当做未发布文件处理
                draftProtoFileVOS.addAll(releasedProtoFiles);
            }
        }


        //构造草稿部分
        List<NavigateTreeVO> draftNavigateTree = Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(draftProtoFileVOS)){
            for (ProtoFileVO draftProtoFile : draftProtoFileVOS) {
                NavigateTreeVO draft = new NavigateTreeVO();
                draft.setParentId(0L);
                draft.setProtoId(draftProtoFile.getId());
                draft.setType(NavigateTreeVO.TypeEnum.PROTO.getName());
                draft.setLabel(draftProtoFile.getName());
                draftNavigateTree.add(draft);
            }
        }


        releasedNavigateTree.addAll(draftNavigateTree);
        return releasedNavigateTree;

    }

    /**
     * 数据源为file_history 生成的 navigate 中的 protoId 为 file_history 中的主键
     *
     * @param appId
     * @param revisionNumber
     * @return
     */
    private List<NavigateTreeVO> getReleasedNavigateTree(String appId, String revisionNumber) {
        List<ProtoFileVO> protoFileVOS = versionService.queryReleasedProtoFileVOs(appId, revisionNumber);
        Schema schema = dbSchemaLoader.loadFromDBByAppIdAndVersion(appId, revisionNumber);
        return getNavigateTree(protoFileVOS, schema);

    }

    private List<NavigateTreeVO> getNavigateTree(List<ProtoFileVO> protoFileVOS, Schema schema) {
        List<NavigateTreeVO> navigateTree = protoFileVOS.stream().sorted(Comparator.comparing(ProtoFileVO::getName)).map(protoFileVO -> {
            Long protoFileId = protoFileVO.getId();
            Location location = CodegenUtils.getLocation(protoFileVO.getPackageName(), protoFileVO.getName());
            ProtoFile protoFile = schema.protoFile(location.path());
            return resolveProtoFile(protoFile, protoFileId);
        }).collect(Collectors.toList());

        return navigateTree;
    }

    /**
     * @param protoFile
     * @param id        前端通过这个Id 获取文件内容和文档
     * @return
     */
    public NavigateTreeVO resolveProtoFile(ProtoFile protoFile, Long id) {

        ProtoFileMetaInfo protoFileMetaInfo = ProtoFileMetaInfo.readFrom(protoFile);
        NavigateTreeVO grandParent = new NavigateTreeVO();
        grandParent.setParentId(0L);
        grandParent.setProtoId(id);
        grandParent.setType(NavigateTreeVO.TypeEnum.PROTO.getName());
        grandParent.setLabel(protoFileMetaInfo.getProtoFile());
        grandParent.setSummary(protoFileMetaInfo.getSummary());
        grandParent.setStatus(ProtoFileVO.Status.RELEASED.getCode());

        ArrayList<NavigateTreeVO> parents = Lists.newArrayList();
        for (Service service : protoFile.services()) {
            InterfaceMetaInfo interfaceMetaInfo = InterfaceMetaInfo.readFrom(protoFile, service);
            NavigateTreeVO parent = new NavigateTreeVO();
            parent.setParentId(grandParent.getId());
            parent.setProtoId(id);
            String summary = interfaceMetaInfo.getSummary();
            parent.setLabel(service.name());
            parent.setSummary(summary);
            parent.setType(NavigateTreeVO.TypeEnum.SERVICE.getName());
            parent.setStatus(ProtoFileVO.Status.RELEASED.getCode());

            parents.add(parent);

            ArrayList<NavigateTreeVO> children = Lists.newArrayList();

            for (Rpc rpc : service.rpcs()) {
                MethodMetaInfo methodMetaInfo = MethodMetaInfo.readFrom(rpc);
                NavigateTreeVO child = new NavigateTreeVO();
                child.setParentId(parent.getId());
                child.setProtoId(id);
                String rpcSummary = methodMetaInfo.getSummary();
                child.setLabel(rpc.name());
                child.setSummary(rpcSummary);
                child.setType(NavigateTreeVO.TypeEnum.METHOD.getName());
                child.setStatus(ProtoFileVO.Status.RELEASED.getCode());

                children.add(child);
            }
            parent.setChildren(children);
        }
        grandParent.setChildren(parents);

        return grandParent;
    }

}
