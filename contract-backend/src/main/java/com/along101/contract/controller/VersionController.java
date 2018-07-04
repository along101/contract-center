package com.along101.contract.controller;

import com.google.common.collect.Sets;
import com.along101.contract.annotation.AuthorityAnnotation;
import com.along101.contract.controller.response.Response;
import com.along101.contract.entity.ReleaseHistoryEntity;
import com.along101.contract.service.VersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * @author zhangchengxi
 * Date 2018/6/8
 */
@RestController
@RequestMapping("/web/version/")
public class VersionController {

    @Autowired
    private VersionService versionService;

    @RequestMapping("release")
    @AuthorityAnnotation
    public Response release(@RequestBody List<Long> ids, @RequestParam String appId, @RequestParam(required = false, defaultValue = "") String tag) {
        ReleaseHistoryEntity release = versionService.release(appId, tag, Sets.newHashSet(ids));
        if(Objects.isNull(release)){
            Response.error("发布失败,没有可发布的文件");
        }
        return Response.success(release);
    }


    @RequestMapping("latestReleaseHistory")
    public Response<ReleaseHistoryEntity> getLatestReleaseHistory(@RequestParam String appId) {
        ReleaseHistoryEntity latestReleaseHistory = versionService.getLatestReleaseHistory(appId);
        return Response.success(latestReleaseHistory);
    }

    @RequestMapping("releaseHistoryList")
    public Response<List<ReleaseHistoryEntity>> queryReleaseHistoryList(@RequestParam String appId) {
        List<ReleaseHistoryEntity> latestReleaseHistory = versionService.queryReleaseHistories(appId);
        return Response.success(latestReleaseHistory);
    }

    @RequestMapping(value = "rollback",method = RequestMethod.POST)
    @AuthorityAnnotation
    public Response rollback(@RequestParam String appId, @RequestParam Long releaseHistoryId) {
        versionService.rollback(appId, releaseHistoryId);
        return Response.success("release success");
    }

    @RequestMapping(value = "changeTag",method = RequestMethod.POST)
    @AuthorityAnnotation
    public Response changeTag(@RequestParam String tag, @RequestParam Long releaseHistoryId) {
        versionService.changeTag(releaseHistoryId,tag);
        return Response.success("release success");
    }
}
