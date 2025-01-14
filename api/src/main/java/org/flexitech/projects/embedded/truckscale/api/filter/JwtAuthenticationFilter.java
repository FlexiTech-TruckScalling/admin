package org.flexitech.projects.embedded.truckscale.api.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.flexitech.projects.embedded.truckscale.common.CommonConstants;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dto.user.UserDTO;
import org.flexitech.projects.embedded.truckscale.services.auth.JwtService;
import org.flexitech.projects.embedded.truckscale.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class JwtAuthenticationFilter implements Filter {

	@Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String requestURI = httpRequest.getRequestURI();
        if (requestURI.contains("/auth/login") || requestURI.contains("/auth/token")) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = httpRequest.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                String sessionToken = jwtService.extractSessionToken(token);
                UserDTO userDTO = userService.findUserBySessionToken(sessionToken);
                if(CommonValidators.isValidObject(userDTO)) {
                	if (jwtService.validateToken(token, sessionToken)) {
                        httpRequest.setAttribute(CommonConstants.API_ACCESSOR, userDTO);
                        chain.doFilter(request, response);
                        return;
                    }else {
                    	httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        httpResponse.getWriter().write("Invalid or expired token");
                        return;
                    }
                }else {
                	httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    httpResponse.getWriter().write("Invalid or expired token");
                    return;
                }
                
            } catch (Exception e) {
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.getWriter().write("Invalid or expired token");
                return;
            }
        }

        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.getWriter().write("Missing or invalid Authorization header");
    }

    @Override
    public void destroy() {
        // Clean-up if necessary
    }
}

