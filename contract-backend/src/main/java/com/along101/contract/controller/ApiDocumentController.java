package com.along101.contract.controller;

import com.along101.contract.controller.response.Response;
import com.along101.contract.service.CodegenService;
import com.along101.contract.service.NavigateService;
import com.along101.contract.vo.NavigateTreeVO;
import io.swagger.v3.oas.models.OpenAPI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/web/apidocuments")
public class ApiDocumentController {


    @Autowired
    private NavigateService navigateService;

    @Autowired
    private CodegenService codegenService;


    /**
     * 获取导航栏信息
     *
     * @param appId
     * @return
     */
    @RequestMapping(value = "/navigate/{appId}/revision/{revisionNumber}", method = RequestMethod.GET)
    public Response<List<NavigateTreeVO>> getApiNavigateTreeByContractId(@PathVariable String appId, @PathVariable String revisionNumber) {
        List<NavigateTreeVO> navigateTreeVO = navigateService.queryNavigateTree(appId, revisionNumber);
        return Response.success(navigateTreeVO);
    }


    /**
     * 获取Swagger json
     *
     * @return
     */
    @RequestMapping(value = "/swagger/json", method = RequestMethod.GET)
    public Response<List<OpenAPI>> getSwaggerJsons(@RequestParam Long fileId,
                                                   @RequestParam String revision,
                                                   @RequestParam String appId) {
        List<OpenAPI> swaggerJson = codegenService.findSwaggerJsons(fileId, revision, appId);
        return Response.success(swaggerJson);
    }

}
