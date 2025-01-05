package org.flexitech.projects.embedded.truckscale.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class Interceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Object loggedUser = request.getSession().getAttribute("loggedInUser");

        String uri = request.getRequestURI();
        if (loggedUser != null || uri.endsWith("login.fxt") || uri.endsWith("logout.fxt")) {
            return true;
        }

        response.sendRedirect("login.fxt");
        return false;
    }
}
