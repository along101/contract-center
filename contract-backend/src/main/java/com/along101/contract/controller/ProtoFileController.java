package com.along101.contract.controller;

import com.along101.contract.annotation.AuthorityAnnotation;
import com.along101.contract.service.ProtoFileService;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.along101.atlas.api.AppControllerApiClient;
import com.along101.atlas.model.AppDto;
import com.along101.auth.common.identity.Identity;
import com.along101.auth.utils.PauthTokenUtil;
import com.along101.contract.annotation.AuthorityAnnotation;
import com.along101.contract.common.utils.ConvertUtils;
import com.along101.contract.controller.query.ContractQuery;
import com.along101.contract.controller.response.Response;
import com.along101.contract.entity.ProtoFileEntity;
import com.along101.contract.service.CodegenService;
import com.along101.contract.service.ProtoFileService;
import com.along101.contract.vo.DownLoadMode;
import com.along101.contract.vo.ProtoFileVO;
import com.along101.raptor.codegen.java.maven.PomModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 对契约文件的操作
 */
@Slf4j
@RestController
@RequestMapping("/web/contracts")
public class ProtoFileController {

    @Autowired
    private ProtoFileService protoFileService;

    @Autowired
    private PauthTokenUtil pauthTokenUtil;

    @Autowired
    private CodegenService codegenService;

    @Autowired
    private AppControllerApiClient appControllerApiClient;

    /**
     * 上传契约文件, 只保存文件内容到数据库
     *
     * @return
     */
    @RequestMapping(value = "/new", method = RequestMethod.POST, consumes = "multipart/form-data")
    @AuthorityAnnotation
    public Response uploadNewProtoFile(@RequestParam String appId, Long orgId, @RequestParam MultipartFile[] file, HttpServletRequest httpRequest) {
        Identity tokenInfo = pauthTokenUtil.getTokenInfo(httpRequest);
        String userName = tokenInfo.getName();
        if (userName == null || "".equals(userName)) {
            userName = "SYSTEM";
        }
        protoFileService.addProtoFiles(appId, orgId, userName, file);
        return Response.success("upload success");
    }

    /**
     * 用于页面单击"添加契约文件按钮"时调用
     *
     * @param appId
     * @param protoFileVO
     * @param httpRequest
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @AuthorityAnnotation
    public Response addNewContract(@RequestParam String appId, @RequestBody ProtoFileVO protoFileVO, HttpServletRequest httpRequest) {

        Identity tokenInfo = pauthTokenUtil.getTokenInfo(httpRequest);
        String userName = tokenInfo.getName();
        if (userName == null || "".equals(userName)) {
            userName = "SYSTEM";
        }

        protoFileVO.setUpdateBy(userName);
        ArrayList<ProtoFileVO> protoFileVOS = Lists.newArrayList(protoFileVO);
        List<ProtoFileEntity> fileEntities = protoFileService.addProtoFiles(appId, null, protoFileVOS, userName);

//        protoFileVO.setUpdateBy(userName);
//        protoFileService.addContract(protoFileVO);
        return Response.success(fileEntities.get(0).getId());
    }

    @RequestMapping(value = "/check", method = RequestMethod.POST)
    //@AuthorityAnnotation
    public Response checkNewContract(@RequestParam String appId, @RequestBody ProtoFileVO protoFileVO, HttpServletRequest httpRequest) {
        // TODO: 2018/6/21 不知道这个方法写的是什么 🤷‍
        Identity tokenInfo = pauthTokenUtil.getTokenInfo(httpRequest);
        String userName = tokenInfo.getName();
        if (userName == null || "".equals(userName)) {
            userName = "SYSTEM";
        }
        protoFileVO.setUpdateBy(userName);
        ProtoFileEntity protoFileEntity = ConvertUtils.convert(protoFileVO, ProtoFileEntity.class);

        ProtoFileVO protoFile = protoFileService.findContractById(protoFileVO.getId());
        //todo, 根据传入 fileId 检查指定 proto content 是否合格
        return Response.success("check success");
    }

    /**
     * 用以单个 fileId 为参数的删除
     * @param id
     * @param appId
     * @param httpRequest
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @AuthorityAnnotation
    public Response deleteContract(@PathVariable Long id, @RequestParam String appId, HttpServletRequest httpRequest) {
        Set<Long> idSet = ImmutableSet.<Long>builder().add(id).build();
        protoFileService.deleteProtoFiles(idSet, appId);
        return Response.success("delete success");
    }

    /**
     * 用于以 fileId list 为参数的删除动作
     * @param ids
     * @param appId
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    //@AuthorityAnnotation
    public Response deleteContractByFileIds(@RequestBody List<Long> ids, @RequestParam String appId) {
        Set<Long> idSet = ImmutableSet.<Long>builder().addAll(ids).build();
        protoFileService.deleteProtoFiles(idSet, appId);
        return Response.success("delete success");
    }

    /**
     * 根据 proto file id 查询文件
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Response<ProtoFileVO> findContractById(@RequestParam Long id, @RequestParam String revision) {
        ProtoFileVO protoFileVO = protoFileService.findContractById(id, revision);
        return Response.success(protoFileVO);
    }

    @RequestMapping(value = "/appid/{appid}", method = RequestMethod.GET)
    public Response<List<ProtoFileVO>> findProtoFilesByAppid(@PathVariable String appid,@RequestParam("revision") String revision) {
        List<ProtoFileVO> contracts = protoFileService.findContractsByAppid(appid,revision);
        return Response.success(contracts);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @AuthorityAnnotation
    public Response updateContract(@RequestParam String appId, @RequestBody ProtoFileVO protoFileVO, HttpServletRequest httpRequest) {

        Identity tokenInfo = pauthTokenUtil.getTokenInfo(httpRequest);
        String userName = tokenInfo.getName();
        if (userName == null || "".equals(userName)) {
            userName = "SYSTEM";
        }
        //设置 updateBy
        protoFileVO.setUpdateBy(userName);
        ArrayList<ProtoFileVO> protoFileVOS = Lists.newArrayList(protoFileVO);
        protoFileService.addProtoFiles(appId, null, protoFileVOS, userName);
        return Response.success("update success");

    }

//    @RequestMapping(value = "/deploy", method = RequestMethod.PUT)
//    public Response deployContract(@RequestParam String appId, @RequestParam Long fileId, HttpServletRequest httpRequest) {
//
//        Identity tokenInfo = pauthTokenUtil.getTokenInfo(httpRequest);
//        String userName = tokenInfo.getName();
//        if (userName == null || "".equals(userName)) {
//            userName = "SYSTEM";
//        }
//
//        ProtoFileVO protoFileVO = protoFileService.findContractById(fileId);
//        //发布
//        protoFileVO.setStatus(1);
//        protoFileVO.setUpdateBy(userName);
//        protoFileService.deployContract(protoFileVO);
//        return Response.success("deploy success");
//    }

    @RequestMapping(value = "/condition", method = RequestMethod.GET)
    public Response<List<ProtoFileVO>> findContractByCondition(ContractQuery contractQuery) {
        List<ProtoFileVO> contracts = protoFileService.findContractsByCondition(contractQuery);
        return Response.success(contracts);
    }

    /**
     * 根据mode下载文件
     * @param appId
     * @param response
     * @param groupId
     * @param artifactId
     * @param version
     * @param mode
     * @param revision
     * @throws IOException
     */
    @RequestMapping(value = "/zip/{appId}", method = RequestMethod.GET)
    public void downloadCode(@PathVariable("appId") String appId, HttpServletResponse response,
                             @RequestParam("groupId") String groupId,
                             @RequestParam("artifactId") String artifactId,
                             @RequestParam("version") String version,
                             @RequestParam("mode") String mode,
                             @RequestParam("revision") String revision) throws IOException {
        response.setContentType("application/force-download");
        response.setHeader("Content-Disposition", "attachment; filename=" + mode + ".zip");
        PomModel pomModel = PomModel.builder().version(version).groupId(groupId).artifactId(artifactId).build();
        if (DownLoadMode.SWAGGER.getMode().equals(mode)) {
            codegenService.generateSwaggerJson(response.getOutputStream(), appId, revision);
        } else if (DownLoadMode.FILE.getMode().equals(mode)) {
            codegenService.generateProtoFile(response.getOutputStream(), pomModel, appId, revision);
        } else {
            codegenService.generateServerProject(response.getOutputStream(), pomModel, appId, revision);

        }
    }

    private Boolean checkOrg(String appId, HttpServletRequest request) {

        Identity tokenInfo = pauthTokenUtil.getTokenInfo(request);
        String orgName = tokenInfo.getOrganzation();

        AppDto appDto = appControllerApiClient.getAppByAppIdUsingGET(appId).getBody().getDetail();
        if (orgName.equals(appDto.getOrgDto().getName())) {
            return true;
        } else {
            return false;
        }
    }

}

