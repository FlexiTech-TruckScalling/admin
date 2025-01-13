package org.flexitech.projects.embedded.truckscale.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.flexitech.projects.embedded.truckscale.common.CommonConstants;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class Interceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Object loggedUser = request.getSession().getAttribute(CommonConstants.SESSION_LOGGED_USER);

        String uri = request.getRequestURI();
        if (loggedUser != null || uri.endsWith("login.fxt") || uri.endsWith("logout.fxt")) {
            return true;
        }
        String contextPath = request.getContextPath();
        response.sendRedirect(contextPath + "/login.fxt");
        return false;
    }
}
