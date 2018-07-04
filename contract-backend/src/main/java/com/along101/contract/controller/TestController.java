package com.along101.contract.controller;

import com.along101.atlas.api.AppControllerApiClient;
import com.along101.atlas.api.CloudControllerApiClient;
import com.along101.atlas.model.AppDto;
import com.along101.atlas.model.EnvUrl;
import com.along101.contract.entity.ProtoFileEntity;
import com.along101.contract.service.CodegenService;
import com.along101.contract.service.ProtoFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by qiankai02 on 2017/8/14.
 */
@RestController
@RequestMapping(value = "/test")
@Slf4j
public class TestController {

    @Autowired
    AppControllerApiClient appControllerApiClient;
    @Autowired
    private ProtoFileService protoFileService;
    @Autowired
    private CloudControllerApiClient cloudControllerApiClient;
    @Autowired
    private CodegenService codegenService;

    @RequestMapping(method = RequestMethod.GET)
    public String test() {
        String detail = cloudControllerApiClient.deleteCloudUsingDELETE(47L).getBody().getDetail();
        log.info(detail);

        AppDto appDto = new AppDto();
        appDto.setId(20L);
        appDto.setAppId("1000002070l");
        appDto.setName("demo-test");
        List<EnvUrl> envUrls = new ArrayList<>();
        EnvUrl envUrl = new EnvUrl();
        envUrl.setEnvName("uat");
        envUrl.setUrl("/testttt");
        envUrls.add(envUrl);
        appDto.setEnvUrls(envUrls);
        AppDto detail1 = appControllerApiClient.updateAppUsingPUT(appDto).getBody().getDetail();

        return detail1.getName();
    }


    @RequestMapping(path = "upload", method = RequestMethod.POST)
    @ResponseBody
    public String upLoadFile(@RequestParam String appId,
                             @RequestParam Long orgId,
                             @RequestParam String username,
                             MultipartFile[] multipartFiles) throws IOException {
        List<ProtoFileEntity> protoFileEntities = protoFileService.addProtoFiles(appId, orgId, username, multipartFiles);
        return "";
    }

    @RequestMapping(path = "downloadzip", method = RequestMethod.GET)
    public void download(@RequestParam String path, HttpServletResponse response) throws IOException {
        response.setContentType("application/force-download");
        response.setHeader("Content-Disposition", "attachment; filename=abc.zip");

        ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());
        zipOutputStream.putNextEntry(new ZipEntry("a/b/a.proto"));
        zipOutputStream.write("a".getBytes(StandardCharsets.UTF_8));
        zipOutputStream.putNextEntry(new ZipEntry("a/b/c.proto"));
        zipOutputStream.write("c".getBytes(StandardCharsets.UTF_8));
        zipOutputStream.close();
    }
}
