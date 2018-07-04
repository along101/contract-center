package com.along101.contract.aop;

import com.along101.contract.entity.AuditLogEntity;
import com.along101.contract.service.AuditLogService;
import com.along101.auth.common.identity.Identity;
import com.along101.auth.utils.PauthTokenUtil;
import com.along101.contract.entity.AuditLogEntity;
import com.along101.contract.service.AuditLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Date;

@Aspect
@Component
@Slf4j
@Order(4)
public class AuditLogAspect {

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private PauthTokenUtil pauthTokenUtil;

    @Before("ResourcePointCuts.webController()")
    public void doSthBefore(JoinPoint joinPoint) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        String methodName = method.getName();

        Parameter[] parameters = method.getParameters();

        StringBuilder paraStr = new StringBuilder();
        for (Parameter p : parameters) {
            paraStr.append(p.toString()).append(", ");
        }

        String returnType = method.getReturnType().getName();

        //可以获取请求request，进而获得对请求流程的控制
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String remoteAddr = getIpAddr(request);

        Identity tokenInfo = pauthTokenUtil.getTokenInfo(request);
        String userName = tokenInfo.getName();
        //获取请求URL
        String path = request.getServletPath();
        //获取请求方法 POST,PUT,GET,DELETE
        String httpMethod = request.getMethod();

        log.info("access: username={},path={},method={},remoteAddr={}", userName, path, httpMethod, remoteAddr);
        if ("GET".equalsIgnoreCase(httpMethod)) {
            return;
        }

        AuditLogEntity auditLogEntity = new AuditLogEntity();
        auditLogEntity.setHttpUri(path);
        auditLogEntity.setHttpMethod(httpMethod);
        auditLogEntity.setClientIp(remoteAddr);
        auditLogEntity.setClassMethodArgs(paraStr.toString());
        auditLogEntity.setClassMethod(methodName);
        auditLogEntity.setClassMethodReturn(returnType);
        auditLogEntity.setUserName(userName);
        auditLogEntity.setIsActive(true);
        Date now = new Date();
        auditLogEntity.setInsertTime(now);
        auditLogEntity.setUpdateTime(now);

        auditLogService.save(auditLogEntity);

    }


    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-real-ip");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
