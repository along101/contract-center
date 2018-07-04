package com.along101.contract.aop;

import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by qiankai02 on 2017/8/20.
 */
public class ResourcePointCuts {

    @Pointcut("execution(public * com.along101.contract.controller..*.*(..))")
    public void webController() {
    }

}
