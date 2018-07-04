package com.along101.contract.aop;

import com.along101.contract.annotation.AuthorityAnnotation;
import com.along101.contract.common.exception.ContractException;
import com.along101.atlas.api.AppControllerApiClient;
import com.along101.atlas.model.AppDto;
import com.along101.atlas.model.AppDtoPlus;
import com.along101.atlas.model.UserDto;
import com.along101.auth.common.identity.Identity;
import com.along101.auth.utils.PauthTokenUtil;
import com.along101.contract.annotation.AuthorityAnnotation;
import com.along101.contract.common.exception.ContractException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Component
@Aspect
@Slf4j
public class AuthorityAspect {
    @Autowired
    private PauthTokenUtil pauthTokenUtil;

    @Autowired
    private AppControllerApiClient appControllerApiClient;

    @Before("ResourcePointCuts.webController()")
    public void checkAccess(JoinPoint joinPoint) throws Exception {
        //get current user
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        //skip all get request
        if ("GET".equals(request.getMethod()))
            return;

        //get annotation
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        AuthorityAnnotation authorityAnnotation = method.getAnnotation(AuthorityAnnotation.class);

        //needing to check access
        if (authorityAnnotation != null) {
            String appId = request.getParameter("appId");
            log.info("appId: " + appId);
            AppDtoPlus appDtoPlus = appControllerApiClient.getAppPlusByAppIdUsingGET(appId).getBody().getDetail();

            Identity tokenInfo = pauthTokenUtil.getTokenInfo(request);
            String loginName = tokenInfo.getName();
            //默认只有一个负责人
            List<UserDto> userDtos = appDtoPlus.getUserDtos();
            log.info("current user is " + loginName + ", app manager is " + userDtos.stream().map(item -> item.getUserName()));
            if (!hasRight(userDtos, loginName)) {
                String message = loginName + "不是项目负责人，无权执行操作";
                throw new ContractException(message);
            }
        }
    }

    private boolean hasRight(List<UserDto> userDtos, String loginName) {
        for (UserDto user : userDtos) {
            if (loginName.equals(user.getUserName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * check whether the user get the required role or not
     *
     * @param processedRoles the roles user already have
     * @param requireRoles   the roles needed to visit the resource
     * @return
     */
    private static boolean containsRole(List<String> processedRoles, String[] requireRoles) {
        boolean flag = false;
        for (String role : requireRoles) {
            if (processedRoles.contains(role)) {
                flag = true;
                break;
            }
        }
        return flag;
    }
}
