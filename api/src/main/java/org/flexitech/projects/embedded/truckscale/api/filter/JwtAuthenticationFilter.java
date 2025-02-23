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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.CommonConstants;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.common.enums.APIResponceCode;
import org.flexitech.projects.embedded.truckscale.common.network.response.ErrorResponse;
import org.flexitech.projects.embedded.truckscale.dto.user.UserDTO;
import org.flexitech.projects.embedded.truckscale.services.auth.JwtService;
import org.flexitech.projects.embedded.truckscale.services.user.UserService;
import org.flexitech.projects.embedded.truckscale.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class JwtAuthenticationFilter implements Filter {
	
	private final Logger logger = LogManager.getLogger(getClass());

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
	    System.out.println("Reach filter....");

	    String requestURI = httpRequest.getRequestURI();
	    if (requestURI.contains("/auth/login") || requestURI.contains("/auth/token") || requestURI.endsWith("/health")) {
	        chain.doFilter(request, response);
	        return;
	    }

	    ErrorResponse<String> errorResponse = new ErrorResponse<String>();
	    String authHeader = httpRequest.getHeader("Authorization");

	    if (authHeader != null && authHeader.startsWith("Bearer ")) {
	        String token = authHeader.substring(7);
	        logger.debug("Token : {}", token);
	        try {
	            String sessionToken = jwtService.extractSessionToken(token);
	            UserDTO userDTO = userService.findUserBySessionToken(sessionToken);
	            if (CommonValidators.isValidObject(userDTO)) {
	                if (jwtService.validateToken(token, sessionToken)) {
	                    httpRequest.setAttribute(CommonConstants.API_ACCESSOR, userDTO);
	                    chain.doFilter(request, response);
	                    return;
	                } else {
	                    httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	                    httpResponse.setContentType("application/json");
	                    errorResponse.setResponseCode(APIResponceCode.UNAUTHORIZED.getCode());
	                    errorResponse.setResponseMessage("Invalid or expired token");
	                    errorResponse.setError("Invalid or expired token");
	                    httpResponse.getWriter().write(JsonUtils.convertToJson(errorResponse));
	                    return;
	                }
	            } else {
	                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	                httpResponse.setContentType("application/json");
	                errorResponse.setResponseCode(APIResponceCode.UNAUTHORIZED.getCode());
	                errorResponse.setResponseMessage("Invalid or expired token");
	                errorResponse.setError("Invalid or expired token");
	                httpResponse.getWriter().write(JsonUtils.convertToJson(errorResponse));
	                return;
	            }

	        } catch (Exception e) {
	            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	            httpResponse.setContentType("application/json");
	            errorResponse.setResponseCode(APIResponceCode.UNAUTHORIZED.getCode());
	            errorResponse.setResponseMessage("Invalid or expired token");
	            errorResponse.setError("Invalid or expired token");
	            httpResponse.getWriter().write(JsonUtils.convertToJson(errorResponse));
	            return;
	        }
	    }

	    httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	    httpResponse.setContentType("application/json");
	    errorResponse.setResponseCode(APIResponceCode.UNAUTHORIZED.getCode());
	    errorResponse.setResponseMessage("Missing or invalid Authorization header");
	    errorResponse.setError("Missing or invalid Authorization header");
	    httpResponse.getWriter().write(JsonUtils.convertToJson(errorResponse));
	}


	@Override
	public void destroy() {
		// Clean-up if necessary
	}
}
