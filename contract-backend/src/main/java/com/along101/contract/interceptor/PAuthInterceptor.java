package com.along101.contract.interceptor;

import com.along101.contract.context.IdentityContext;
import com.along101.auth.common.identity.Identity;
import com.along101.auth.utils.PauthTokenUtil;
import com.along101.contract.context.IdentityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhangchengxi
 * Date 2018/6/7
 */
@Component
public class PAuthInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private PauthTokenUtil pauthTokenUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Identity tokenInfo = pauthTokenUtil.getTokenInfo(request);
        IdentityContext.setIdentity(tokenInfo);
        return true;
    }

    @Override
    public void postHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        IdentityContext.remove();
    }
}
