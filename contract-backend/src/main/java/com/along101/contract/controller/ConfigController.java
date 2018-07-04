package com.along101.contract.controller;

import com.along101.contract.controller.response.Response;
import com.along101.contract.vo.CommonConfigVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangchengxi
 * Date 2018/6/4
 */
@Slf4j
@RestController
@RequestMapping("/web/configs")
public class ConfigController {

    @Autowired
    private Environment env;

    @RequestMapping("/common")
    public Response<CommonConfigVO> loadCommonConfig(){
        CommonConfigVO commonConfigVO  = new CommonConfigVO();
        commonConfigVO.setServerUrl(env.getProperty("contract.server.url"));
        return Response.success(commonConfigVO);
    }


}
