package com.along101.contract.controller;

import com.along101.contract.controller.query.AppQuery;
import com.along101.atlas.api.AppControllerApiClient;
import com.along101.atlas.api.OrgControllerApiClient;
import com.along101.atlas.model.*;
import com.along101.contract.controller.query.AppQuery;
import com.along101.contract.controller.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/web/projects")
public class ProjectController {

    @Autowired
    private AppControllerApiClient appControllerApiClient;

    @Autowired
    private OrgControllerApiClient orgControllerApiClient;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Response<List<AppDto>> getAllApps() {
        List<AppDto> appList = appControllerApiClient.getAllAppsUsingGET(null).getBody().getDetail();
        return Response.success(appList);
    }

    @RequestMapping(value = "/orgs/all", method = RequestMethod.GET)
    public Response<List<OrgDto>> getAllOrgs() {
        List<OrgDto> orgDtos = orgControllerApiClient.getAllOrgsUsingGET().getBody().getDetail();
        return Response.success(orgDtos);
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public Response<PageDtoAppDtoPlus> findAppsByPage(@RequestParam Integer page, @RequestParam Integer size) {
        PageDtoAppDtoPlus appDtoPlus = appControllerApiClient.getAppPlusByPageUsingGET(page, size).getBody().getDetail();
        return Response.success(appDtoPlus);
    }

    @RequestMapping(value = "/condition", method = RequestMethod.GET)
    public Response<PageDtoAppDtoPlus> findAppsByCondition(@RequestParam Integer page, @RequestParam Integer size, AppQuery appQuery) {
        if("".equals(appQuery.getAppId())){
            // TODO: 2018/5/14  如果appId为"",将搜索不出结果,建议atlas该一下接口
            appQuery.setAppId(null);
        }
        PageDtoAppDtoPlus appDtoPage = appControllerApiClient.getAppPlusByConditionsUsingGET(page, size, appQuery.getAppId(), appQuery.getAppName(), appQuery.getOrgId(), appQuery.getUserWorkNumber()).getBody().getDetail();
        return Response.success(appDtoPage);
    }

    //按name 查找app, 用于自动补全,模糊查询
    @RequestMapping(value = "/fuzzy/appname", method = RequestMethod.GET)
    public Response<List<AppDto>> fuzzySearchAppsByAppName(@RequestParam(value = "appName") String appName) {
        List<AppDto> appDtos = appControllerApiClient.fuzzySearchAppsByAppNameUsingGET(appName).getBody().getDetail();
        return Response.success(appDtos);
    }

    //
    @RequestMapping(value = "/app/{appId}", method = RequestMethod.GET)
    public Response<AppDtoPlus> findAppByAppId(@PathVariable String appId) {
        //AppDto appDto = appControllerApiClient.getAppByAppIdUsingGET(appId).getBody().getDetail();
        AppDtoPlus appDtoPlus = appControllerApiClient.getAppPlusByAppIdUsingGET(appId).getBody().getDetail();
        return Response.success(appDtoPlus);
    }

}
