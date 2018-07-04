package com.along101.contract.interceptor;

import com.along101.contract.context.VersionContext;
import com.along101.contract.vo.VersionInfo;
import com.along101.contract.context.VersionContext;
import com.along101.contract.vo.VersionInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhangchengxi
 * Date 2018/6/11
 */
@Component
public class VersionInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String versionNumber = request.getParameter("versionNumber");
        if (StringUtils.isNotEmpty(versionNumber)) {
            VersionInfo versionInfo = VersionInfo.builder().versionNumber(versionNumber).build();
            VersionContext.setVersionInfo(versionInfo);
        }
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        VersionContext.remove();

    }
}
