package com.along101.contract.controller;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HSController {

    @ApolloConfig
    private Config config;


    @RequestMapping("/hs")
    public String healthCheck() {
        return config.getProperty("healthcheck.result","");

    }

}
